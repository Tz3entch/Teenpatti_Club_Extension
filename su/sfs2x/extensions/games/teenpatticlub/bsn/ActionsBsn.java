/*     */ package su.sfs2x.extensions.games.teenpatticlub.bsn;
/*     */ 
/*     */ import com.smartfoxserver.v2.entities.Room;
/*     */ import com.smartfoxserver.v2.entities.User;
/*     */ import com.smartfoxserver.v2.entities.Zone;
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.npcs.NpcLogic;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActionsBsn
/*     */ {
/*     */   public void seen(String player, ISFSObject params, GameBean gameBean)
/*     */   {
/*  30 */     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/*  31 */     PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
/*  32 */     prBean.setSeen(true);
/*  33 */     prBean.setLastAction("Seen");
/*  34 */     params.putUtfString("player", player);
/*  35 */     Commands.appInstance.send("Action", params, room.getUserList());
/*     */   }
/*     */   
/*     */ 
/*     */   public void pack(String player, ISFSObject params, GameBean gameBean)
/*     */   {
/*  41 */     checkIsUsingExtraTime(gameBean, player);
/*     */     
/*     */ 
/*  44 */     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/*  45 */     PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
/*  46 */     prBean.setPack(true);
/*  47 */     prBean.setLastAction("Pack");
/*  48 */     params.putUtfString("player", player);
/*  49 */     params.putUtfString("command", "Pack");
/*  50 */     Commands.appInstance.send("Action", params, room.getUserList());
/*     */     
/*  52 */     gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
/*  53 */     PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/*     */     
/*  55 */     Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean.getPlayerId(), 0, "Pack", pBean.getCommission());
/*     */     
/*     */ 
/*  58 */     if (gameBean.getGameRoundBean().getActivePlayersCount() >= 2)
/*     */     {
/*     */ 
/*  61 */       callNextTurn(gameBean, room);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*  66 */       ShowBsn showBsn = new ShowBsn();
/*  67 */       showBsn.show(gameBean, "Oppnent packed, You win");
/*  68 */       showBsn = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void leaveTable(String player, ISFSObject params, GameBean gameBean)
/*     */   {
/*  78 */     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/*  79 */     PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
/*  80 */     if (prBean != null)
/*     */     {
/*  82 */       prBean.setLeaveTable(true);
/*  83 */       prBean.setLastAction("LeaveTable");
/*     */     }
/*     */     
/*  86 */     User sender = Commands.appInstance.getParentZone().getUserByName(player);
/*     */     
/*  88 */     params = gameBean.getSFSObject();
/*  89 */     params = gameBean.getGameRoundBean().getSFSObject(params);
/*  90 */     params.putUtfString("player", player);
/*  91 */     params.putBool("isSpectator", false);
/*     */     
/*     */ 
/*  94 */     PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/*  95 */     Commands.appInstance.proxy.updateUserChips(player, Float.valueOf(pBean.getInplay()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */     if ((gameBean.getGameRoundBean().getActivePlayersCount() >= 2) && (!gameBean.getGameRoundBean().isShowCalled()))
/*     */     {
/* 104 */       if (gameBean.getGameRoundBean().getTurn().equals(player))
/*     */       {
/*     */ 
/* 107 */         callNextTurn(gameBean, room);
/*     */       }
/*     */     }
/* 110 */     else if (gameBean.getGameRoundBean().getActivePlayersCount() == 1)
/*     */     {
/* 112 */       if (!gameBean.isGameGenerating())
/*     */       {
/*     */ 
/* 115 */         ShowBsn showBsn = new ShowBsn();
/* 116 */         showBsn.show(gameBean, "Oppnent left, You win");
/* 117 */         showBsn = null;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 122 */     gameBean.removePlayer(player);
/* 123 */     gameBean.removePlayerBean(player);
/*     */     
/* 125 */     Commands.appInstance.send("LeaveTable", params, sender);
/* 126 */     Commands.appInstance.send("LeaveTable", params, room.getUserList());
/*     */     
/*     */ 
/* 129 */     Appmethods.updateGameBeanUpdateLobby(gameBean, room);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void blind(String player, ISFSObject params, GameBean gameBean)
/*     */   {
/* 138 */     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/* 139 */     PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
/* 140 */     PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/* 141 */     int blindAmount = params.getInt("amount").intValue();
/*     */     
/* 143 */     if (pBean.getInplay() >= blindAmount)
/*     */     {
/*     */ 
/* 146 */       checkIsUsingExtraTime(gameBean, player);
/*     */       
/* 148 */       if (blindAmount > gameBean.getGameRoundBean().getBlindBet())
/*     */       {
/* 150 */         gameBean.getGameRoundBean().setBlindBet(blindAmount);
/* 151 */         if (blindAmount * 2 < gameBean.getGameRoundBean().getChallLimit()) {
/* 152 */           gameBean.getGameRoundBean().setChallBet(blindAmount * 2);
/*     */         } else
/* 154 */           gameBean.getGameRoundBean().setChallBet(gameBean.getGameRoundBean().getChallLimit());
/*     */       }
/* 156 */       prBean.getHandAmounts().add(blindAmount);
/* 157 */       prBean.setTotalBetAmount(prBean.getTotalBetAmount() + blindAmount);
/* 158 */       pBean.setInplay(pBean.getInplay() - blindAmount);
/* 159 */       gameBean.getGameRoundBean().setPotAmount(gameBean.getGameRoundBean().getPotAmount() + blindAmount);
/*     */       
/* 161 */       params.putUtfString("player", player);
/* 162 */       Commands.appInstance.send("Action", params, room.getUserList());
/*     */       
/*     */ 
/*     */ 
/* 166 */       gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
/* 167 */       Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean.getPlayerId(), blindAmount, "Blind", pBean.getCommission());
/*     */       
/*     */ 
/*     */ 
/* 171 */       if (gameBean.getGameRoundBean().getPotAmount() >= gameBean.getGameRoundBean().getPotLimit())
/*     */       {
/* 173 */         gameBean.getGameRoundBean().setPotLimitExceed(true);
/*     */         
/* 175 */         ShowBsn showBsn = new ShowBsn();
/* 176 */         showBsn.show(gameBean, null);
/* 177 */         showBsn = null;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 182 */         callNextTurn(gameBean, room);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 187 */       Appmethods.showLog("############");
/* 188 */       Appmethods.showLog("Going Negative Inplay blind Player " + pBean.getPlayerId() + " Inplay " + pBean.getInplay() + " amount " + blindAmount);
/* 189 */       Appmethods.showLog("############");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void chall(String player, ISFSObject params, GameBean gameBean)
/*     */   {
/* 196 */     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/* 197 */     PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
/* 198 */     PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/* 199 */     int challAmount = params.getInt("amount").intValue();
/*     */     
/* 201 */     if (pBean.getInplay() >= challAmount)
/*     */     {
/*     */ 
/* 204 */       checkIsUsingExtraTime(gameBean, player);
/*     */       
/* 206 */       if (challAmount > gameBean.getGameRoundBean().getChallBet())
/*     */       {
/* 208 */         gameBean.getGameRoundBean().setChallBet(challAmount);
/* 209 */         if (challAmount / 2 < gameBean.getGameRoundBean().getChallLimit())
/* 210 */           gameBean.getGameRoundBean().setBlindBet(challAmount / 2);
/*     */       }
/* 212 */       prBean.getHandAmounts().add(Integer.valueOf(challAmount));
/* 213 */       prBean.setTotalBetAmount(prBean.getTotalBetAmount() + challAmount);
/* 214 */       pBean.setInplay(pBean.getInplay() - challAmount);
/* 215 */       gameBean.getGameRoundBean().setPotAmount(Integer.valueOf(gameBean.getGameRoundBean().getPotAmount().intValue() + challAmount));
/*     */       
/* 217 */       params.putUtfString("player", player);
/* 218 */       Commands.appInstance.send("Action", params, room.getUserList());
/*     */       
/*     */ 
/* 221 */       gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
/* 222 */       Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean.getPlayerId(), challAmount, "Chaal", pBean.getCommission());
/*     */       
/*     */ 
/*     */ 
/* 226 */       if (gameBean.getGameRoundBean().getPotAmount().intValue() >= gameBean.getGameRoundBean().getPotLimit())
/*     */       {
/* 228 */         gameBean.getGameRoundBean().setPotLimitExceed(true);
/*     */         
/* 230 */         ShowBsn showBsn = new ShowBsn();
/* 231 */         showBsn.show(gameBean, null);
/* 232 */         showBsn = null;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 237 */         callNextTurn(gameBean, room);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 242 */       Appmethods.showLog("############");
/* 243 */       Appmethods.showLog("Going Negative Inplay chaal Player " + pBean.getPlayerId() + " Inplay " + pBean.getInplay() + " amount " + challAmount);
/* 244 */       Appmethods.showLog("############");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void show(String player, ISFSObject params, GameBean gameBean)
/*     */   {
/* 251 */     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/* 252 */     PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
/* 253 */     PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/* 254 */     int showAmount = params.getInt("amount");
/*     */     
/* 256 */     if (pBean.getInplay() >= showAmount)
/*     */     {
/*     */ 
/* 259 */       checkIsUsingExtraTime(gameBean, player);
/* 260 */       prBean.getHandAmounts().add(Integer.valueOf(showAmount));
/* 261 */       prBean.setTotalBetAmount(prBean.getTotalBetAmount() + showAmount);
/* 262 */       pBean.setInplay(pBean.getInplay() - showAmount);
/* 263 */       gameBean.getGameRoundBean().setPotAmount(Integer.valueOf(gameBean.getGameRoundBean().getPotAmount().intValue() + showAmount));
/* 264 */       prBean.setShowCards(true);
/*     */       
/*     */ 
/* 267 */       params.putUtfString("player", player);
/* 268 */       Commands.appInstance.send("Action", params, room.getUserList());
/*     */       
/*     */ 
/* 271 */       gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
/* 272 */       Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean.getPlayerId(), showAmount, "Show", pBean.getCommission());
/*     */       
/*     */ 
/*     */ 
/* 276 */       ShowBsn showBsn = new ShowBsn();
/* 277 */       showBsn.show(gameBean, null);
/* 278 */       showBsn = null;
/*     */     }
/*     */     else
/*     */     {
/* 282 */       Appmethods.showLog("############");
/* 283 */       Appmethods.showLog("Going Negative Inplay show Player " + pBean.getPlayerId() + " Inplay " + pBean.getInplay() + " amount " + showAmount);
/* 284 */       Appmethods.showLog("############");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void sideShowRequest(String player, ISFSObject params, GameBean gameBean)
/*     */   {
/* 292 */     int pos = gameBean.getGameRoundBean().getPlayers().indexOf(player);
/* 293 */     User user = null;
/* 294 */     for (int i = 1; i < gameBean.getGameRoundBean().getPlayers().size(); i++)
/*     */     {
/* 296 */       int no = pos - i;
/* 297 */       if (no < 0) {
/* 298 */         no += gameBean.getGameRoundBean().getPlayers().size();
/*     */       }
/* 300 */       Appmethods.showLog("Pos No:" + no);
/* 301 */       String prePlayer = (String)gameBean.getGameRoundBean().getPlayers().get(no);
/* 302 */       if (!prePlayer.equals("null"))
/*     */       {
/* 304 */         PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(prePlayer);
/* 305 */         if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSideShowSelected()) && (!prBean.isSitOut()))
/*     */         {
/* 307 */           user = Commands.appInstance.getParentZone().getUserByName(prePlayer);
/* 308 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 312 */     if (user != null)
/*     */     {
/* 314 */       params.putUtfString("player", player);
/* 315 */       Commands.appInstance.send("Action", params, user);
/*     */     }
/*     */   }
/*     */   
/*     */   public void sideShowResponse(String player, ISFSObject params, GameBean gameBean)
/*     */   {
/* 321 */     ISFSObject sfso = new SFSObject();
/*     */     
/* 323 */     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/* 324 */     if (params.getUtfString("status").equals("Accepted"))
/*     */     {
/*     */ 
/* 327 */       String requestedPlayer = params.getUtfString("player");
/* 328 */       String respondPlayer = player;
/* 329 */       PlayerRoundBean prBean1 = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(requestedPlayer);
/* 330 */       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(requestedPlayer);
/* 331 */       PlayerRoundBean prBean2 = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(respondPlayer);
/* 332 */       int amount = gameBean.getGameRoundBean().getChallBet();
/*     */       
/* 334 */       if (pBean.getInplay() >= amount)
/*     */       {
/* 336 */         prBean1.setSideShowCalled(true);
/* 337 */         prBean1.setSideShowSelected(true);
/* 338 */         prBean2.setSideShowSelected(true);
/* 339 */         ShowBsn showBsn = new ShowBsn();
/* 340 */         String wonPlayer = showBsn.sideShowWonPlayer(prBean1, prBean2);
/*     */         
/*     */ 
/*     */ 
/* 344 */         if (wonPlayer.equals(requestedPlayer)) {
/* 345 */           prBean2.setPack(true);
/*     */         } else {
/* 347 */           prBean1.setPack(true);
/*     */         }
/*     */         
/* 350 */         prBean1.getHandAmounts().add(Integer.valueOf(amount));
/* 351 */         prBean1.setTotalBetAmount(prBean1.getTotalBetAmount() + amount);
/* 352 */         pBean.setInplay(pBean.getInplay() - amount);
/* 353 */         gameBean.getGameRoundBean().setPotAmount(Integer.valueOf(gameBean.getGameRoundBean().getPotAmount().intValue() + amount));
/*     */         
/* 355 */         sfso.putUtfString("player", requestedPlayer);
/* 356 */         sfso.putUtfString("winner", wonPlayer);
/* 357 */         sfso.putUtfString("acceptedPlayer", player);
/* 358 */         sfso.putInt("amount", amount);
/* 359 */         sfso.putUtfString("commnad", "SideShow");
/* 360 */         sfso.putUtfString("status", params.getUtfString("status"));
/*     */         
/* 362 */         Commands.appInstance.send("Action", sfso, room.getUserList());
/*     */         
/*     */ 
/* 365 */         gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
/* 366 */         Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean1.getPlayerId(), amount, "SideShow", pBean.getCommission());
/*     */         
/*     */ 
/* 369 */         callNextTurn(gameBean, room);
/*     */       }
/*     */       else
/*     */       {
/* 373 */         Appmethods.showLog("############");
/* 374 */         Appmethods.showLog("Going Negative Inplay sideShowResponse Player " + pBean.getPlayerId() + " Inplay " + pBean.getInplay() + " amount " + amount);
/* 375 */         Appmethods.showLog("############");
/*     */       }
/*     */       
/*     */     }
/*     */     else
/*     */     {
/* 381 */       String receiver = params.getUtfString("player");
/* 382 */       User user = Commands.appInstance.getParentZone().getUserByName(receiver);
/* 383 */       sfso.putUtfString("rejectedPlayer", player);
/* 384 */       sfso.putUtfString("commnad", "SideShow");
/* 385 */       sfso.putUtfString("status", params.getUtfString("status"));
/* 386 */       Commands.appInstance.send("Action", sfso, user);
/*     */     }
/*     */   }
/*     */   
/*     */   public void sitOut(String player, ISFSObject params, GameBean gameBean)
/*     */   {
/* 392 */     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/*     */     
/* 394 */     if (gameBean.getGameRoundBean().getTurn().equals(player))
/*     */     {
/*     */ 
/*     */ 
/* 398 */       checkIsUsingExtraTime(gameBean, player);
/* 399 */       gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 404 */     PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
/*     */     
/* 406 */     prBean.setSitOut(true);
/* 407 */     prBean.setLastAction("SitOut");
/* 408 */     params.putUtfString("player", player);
/* 409 */     params.putUtfString("command", "SitOut");
/*     */     
/* 411 */     Commands.appInstance.send("Action", params, room.getUserList());
/* 412 */     PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/*     */     
/* 414 */     Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean.getPlayerId(), 0, "SitOut", pBean.getCommission());
/*     */     
/* 416 */     if (gameBean.getGameRoundBean().getTurn().equals(player))
/*     */     {
/*     */ 
/* 419 */       if (gameBean.getGameRoundBean().getActivePlayersCount() >= 2)
/*     */       {
/*     */ 
/* 422 */         callNextTurn(gameBean, room);
/*     */       }
/* 424 */       else if (!gameBean.getGameRoundBean().isShowCalled())
/*     */       {
/*     */ 
/* 427 */         ShowBsn showBsn = new ShowBsn();
/* 428 */         showBsn.show(gameBean, "Oppnent sit out, You win");
/* 429 */         showBsn = null;
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 434 */     else if (gameBean.getGameRoundBean().getActivePlayersCount() == 1)
/*     */     {
/* 436 */       if (!gameBean.getGameRoundBean().isShowCalled())
/*     */       {
/*     */ 
/* 439 */         ShowBsn showBsn = new ShowBsn();
/* 440 */         showBsn.show(gameBean, "Oppnent sit out, You win");
/* 441 */         showBsn = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void callNextTurn(GameBean gameBean, Room room)
/*     */   {
/* 451 */     gameBean.getGameRoundBean().setTurnPlayer();
/* 452 */     checkIsSeenRoundCompleted(gameBean, room, gameBean.getGameRoundBean().getTurn());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 457 */     ISFSObject sfso = new SFSObject();
/* 458 */     sfso = gameBean.getGameRoundBean().getSFSObject(sfso);
/* 459 */     sfso.putInt("turnTime", 60);
/* 460 */     Commands.appInstance.send("Turn", sfso, room.getUserList());
/*     */
/*     */
/* 463 */     gameBean.startTimer(61, "Turn");
/*  67 */
/*  68 */
    NpcLogic npcl = new NpcLogic(gameBean);
    npcl.performNpcTurn();
/*  68 */
/*     */   }
/*     */   
/*     */   private void checkIsSeenRoundCompleted(GameBean gameBean, Room room, String player)
/*     */   {
/* 468 */     if (gameBean.getGameRoundBean().getHandNo() == 5)
/*     */     {
/* 470 */       PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
/* 471 */       if (!prBean.isSeen())
/*     */       {
/* 473 */         prBean.setSeen(true);
/* 474 */         ISFSObject sfso = new SFSObject();
/* 475 */         sfso.putUtfString("player", player);
/* 476 */         sfso.putUtfString("command", "Seen");
/* 477 */         Commands.appInstance.send("Action", sfso, room.getUserList());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void checkIsUsingExtraTime(GameBean gameBean, String player)
/*     */   {
/* 485 */     PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/* 486 */     if (pBean.isUsingExtraTime())
/*     */     {
/*     */ 
/* 489 */       int remainingSeconds = gameBean.getRemainingSeconds();
/* 490 */       pBean.setTimerBank(Integer.valueOf(remainingSeconds));
/* 491 */       pBean.setUsingExtraTime(false);
/* 492 */       gameBean.stopTimer();
/*     */     }
/*     */     else
/*     */     {
/* 496 */       gameBean.stopTimer();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bsn/ActionsBsn.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */