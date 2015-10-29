/*     */ package su.sfs2x.extensions.games.teenpatticlub.bean;
/*     */ 
/*     */ import com.smartfoxserver.v2.api.ISFSApi;
/*     */ import com.smartfoxserver.v2.entities.Room;
/*     */ import com.smartfoxserver.v2.entities.User;
/*     */ import com.smartfoxserver.v2.entities.Zone;
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSArray;
/*     */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.swing.Timer;
import su.sfs2x.extensions.games.teenpatticlub.bsn.StartGameBsn;
import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.timers.GameBeanTimer;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameBean
/*     */ {
/*     */   private String roomId;
/*     */   private String gameID;
/*  37 */   private int maxNoOfPlayers = 5;
/*  38 */   private int tableBeanId = 0;
/*  39 */   private ArrayList<String> players = new ArrayList();
/*  40 */   private String dealer = null;
/*  41 */   private int dealerPos = 0;
/*     */   
/*     */   private String logsData;
/*     */   
/*  45 */   private Timer timer = null;
/*  46 */   private boolean isStarted = false;
/*  47 */   private boolean isGameGenerating = false;
/*     */   
/*  49 */   private ConcurrentHashMap<String, PlayerBean> playerBeenList = new ConcurrentHashMap();
/*  50 */   private ArrayList<String> spectatorsList = new ArrayList();
/*     */   
/*  52 */   private GameRoundBean gameRoundBean = null;
/*  53 */   private long actionStartTime = 0L;
/*  54 */   private int sec = 0;
/*     */   
/*  56 */   private String gameType = "public";
/*  57 */   private String gameStartDate = "";
/*     */   
/*     */   public GameBean()
/*     */   {
/*  61 */     for (int i = 0; i < this.maxNoOfPlayers; i++)
/*     */     {
/*  63 */       this.players.add("null");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ISFSObject getSFSObject()
/*     */   {
/*  70 */     ISFSObject sfso = new SFSObject();
/*     */     
/*  72 */     sfso.putUtfStringArray("players", this.players);
/*  73 */     sfso.putUtfString("roomId", getRoomId());
/*  74 */     sfso.putUtfString("gameId", getGameID());
/*  75 */     sfso.putBool("isGameStarted", this.isStarted);
/*  76 */     sfso.putInt("remainingSeconds", getRemainingSeconds());
/*  77 */     sfso.putUtfString("gameType", this.gameType);
/*     */     
/*     */     TableBean tb = null;
/*  80 */     if (this.gameType.equals("Public")) {
/*  81 */       tb = Appmethods.getTableBean(this.tableBeanId);
/*     */     } else {
/*  83 */       tb = Appmethods.getPrivateTableBean(this.roomId);
/*     */     }
/*  85 */     sfso.putInt("boot", tb.get_boot().intValue());
/*  86 */     sfso.putInt("challLimit", tb.get_challLimit().intValue());
/*  87 */     sfso.putInt("potLimit", tb.get_potLimit().intValue());
/*  88 */     sfso.putInt("id", tb.get_id().intValue());
/*  89 */     sfso.putBool("isGameGenerating", isGameGenerating());
/*  90 */     sfso.putSFSArray("playerBeans", getPlayerBeansSFSArray());
/*     */     
/*  92 */     //TableBean tb = null;
/*  93 */     return sfso;
/*     */   }
/*     */   
/*     */   public ISFSObject getSinkDataSFSObject(ISFSObject sfso)
/*     */   {
/*  98 */     sfso.putBool("isGameGenerating", this.isGameGenerating);
/*     */     
/* 100 */     return sfso;
/*     */   }
/*     */   
/*     */   public SFSArray getPlayerBeansSFSArray()
/*     */   {
/* 105 */     SFSArray playerBeans = new SFSArray();
/*     */     
/* 107 */     for (Enumeration<PlayerBean> e = getPlayerBeenList().elements(); e.hasMoreElements();)
/*     */     {
/* 109 */       PlayerBean bean = (PlayerBean)e.nextElement();
/* 110 */       playerBeans.addSFSObject(bean.getSFSObject());
/*     */     }
/* 112 */     return playerBeans;
/*     */   }
/*     */   
/*     */   public String getGameStartDate()
/*     */   {
/* 117 */     return this.gameStartDate;
/*     */   }
/*     */   
/* 120 */   public void setGameStartDate(String gameStartDate) { this.gameStartDate = gameStartDate; }
/*     */   
/*     */   public boolean isGameGenerating() {
/* 123 */     return this.isGameGenerating;
/*     */   }
/*     */   
/* 126 */   public void setGameGenerating(boolean isGameGenerating) { this.isGameGenerating = isGameGenerating; }
/*     */   
/*     */   public GameRoundBean getGameRoundBean() {
/* 129 */     return this.gameRoundBean;
/*     */   }
/*     */   
/* 132 */   public void setGameRoundBean(GameRoundBean gameRoundBean) { this.gameRoundBean = gameRoundBean; }
/*     */   
/*     */   public ConcurrentHashMap<String, PlayerBean> getPlayerBeenList() {
/* 135 */     return this.playerBeenList;
/*     */   }
/*     */   
/*     */   public void setPlayerBeenList(ConcurrentHashMap<String, PlayerBean> playerBeenList) {
/* 139 */     this.playerBeenList = playerBeenList;
/*     */   }
/*     */   
/* 142 */   public ArrayList<String> getSpectatorsList() { return this.spectatorsList; }
/*     */   
/*     */   public void setSpectatorsList(ArrayList<String> spectatorsList)
/*     */   {
/* 146 */     this.spectatorsList = spectatorsList;
/*     */   }
/*     */   
/*     */   public int getTableBeanId() {
/* 150 */     return this.tableBeanId;
/*     */   }
/*     */   
/*     */   public void setTableBeanId(int tableBeanId) {
/* 154 */     this.tableBeanId = tableBeanId;
/*     */   }
/*     */   
/*     */   public String getRoomId() {
/* 158 */     return this.roomId;
/*     */   }
/*     */   
/*     */   public void setRoomId(String roomId) {
/* 162 */     this.roomId = roomId;
/*     */   }
/*     */   
/*     */   public String getGameID() {
/* 166 */     return this.gameID;
/*     */   }
/*     */   
/*     */   public void setGameID(String gameID) {
/* 170 */     this.gameID = gameID;
/*     */   }
/*     */   
/*     */   public String getGameType() {
/* 174 */     return this.gameType;
/*     */   }
/*     */   
/*     */   public void setGameType(String gameType) {
/* 178 */     this.gameType = gameType;
/*     */   }
/*     */   
/*     */   public int getMaxNoOfPlayers() {
/* 182 */     return this.maxNoOfPlayers;
/*     */   }
/*     */   
/*     */   public void setMaxNoOfPlayers(int maxNoOfPlayers) {
/* 186 */     this.maxNoOfPlayers = maxNoOfPlayers;
/*     */   }
/*     */   
/*     */   public ArrayList<String> getPlayers() {
/* 190 */     return this.players;
/*     */   }
/*     */   
/*     */   public void setPlayers(ArrayList<String> players) {
/* 194 */     this.players = players;
/*     */   }
/*     */   
/*     */   public String get_dealer() {
/* 198 */     return this.dealer;
/*     */   }
/*     */   
/*     */   public void set_dealer(String dealer) {
/* 202 */     this.dealer = dealer;
/*     */   }
/*     */   
/*     */   public String getLogsData() {
/* 206 */     return this.logsData;
/*     */   }
/*     */   
/*     */   public void setLogsData(String logsData) {
/* 210 */     this.logsData = logsData;
/*     */   }
/*     */   
/*     */   public Timer getTimer() {
/* 214 */     return this.timer;
/*     */   }
/*     */   
/*     */   public void setTimer(Timer timer) {
/* 218 */     this.timer = timer;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 222 */     return this.isStarted;
/*     */   }
/*     */   
/*     */   public void setStarted(boolean isStarted) {
/* 226 */     this.isStarted = isStarted;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addPlayer(int pos, String name)
/*     */   {
/* 235 */     Appmethods.showLog("Before Add " + this.players.toString());
/* 236 */     this.players.remove(pos);
/* 237 */     this.players.add(pos, name);
/* 238 */     Appmethods.showLog("After Add " + this.players.toString());
/* 239 */     this.spectatorsList.remove(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removePlayer(String name)
/*     */   {
/* 247 */     int pos = -1;
/* 248 */     pos = this.players.indexOf(name);
/* 249 */     if (pos != -1)
/*     */     {
/* 251 */       Appmethods.showLog("Before Remove " + this.players.toString() + "Pos " + pos);
/* 252 */       this.players.remove(pos);
/* 253 */       this.players.add(pos, "null");
/* 254 */       Appmethods.showLog("After Remove " + this.players.toString());
/*     */     }
/*     */     
/* 257 */     this.playerBeenList.remove(name);
/* 258 */     this.spectatorsList.remove(name);
/*     */   }
/*     */   
/*     */   public void removePlayerAndAddToSpectator(String name) {
/* 262 */     int pos = -1;
/* 263 */     pos = this.players.indexOf(name);
/* 264 */     if (pos != -1)
/*     */     {
/* 266 */       Appmethods.showLog("Before Remove GameBean" + this.players.toString());
/* 267 */       this.players.remove(pos);
/* 268 */       this.players.add(pos, "null");
/* 269 */       Appmethods.showLog("After Remove GameBean" + this.players.toString());
/*     */     }
/*     */     
/*     */ 
/* 273 */     getSpectatorsList().add(name);
/*     */   }
/*     */   
/*     */   public void removePlayerBean(String player)
/*     */   {
/* 278 */     this.playerBeenList.remove(player);
/*     */   }
/*     */   
/*     */   public int getJoinedPlayers() {
/* 282 */     int joinedPlayers = 0;
/* 283 */     for (int i = 0; i < this.maxNoOfPlayers; i++)
/*     */     {
/* 285 */       if (!((String)this.players.get(i)).equals("null"))
/*     */       {
/* 287 */         joinedPlayers++;
/*     */       }
/*     */     }
/* 290 */     return joinedPlayers;
/*     */   }
/*     */   
/*     */   private void setDealer()
/*     */   {
/* 295 */     if (this.dealer == null)
/*     */     {
/* 297 */       for (int i = 0; i < this.players.size(); i++)
/*     */       {
/* 299 */         if (!((String)this.players.get(i)).equals("null"))
/*     */         {
/* 301 */           this.dealer = ((String)this.players.get(i));
/* 302 */           this.dealerPos = i;
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     } else {
/* 308 */       for (int i = 1; i < this.players.size(); i++)
/*     */       {
/* 310 */         int no = this.dealerPos + i;
/* 311 */         if (no >= this.players.size())
/* 312 */           no %= this.players.size();
/* 313 */         if (!((String)this.players.get(no)).equals("null"))
/*     */         {
/* 315 */           this.dealer = ((String)this.players.get(no));
/* 316 */           this.dealerPos = no;
/* 317 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeDisconnectedUsers()
/*     */   {
/* 328 */     for (Enumeration<PlayerBean> e = this.playerBeenList.elements(); e.hasMoreElements();)
/*     */     {
/* 330 */       PlayerBean pBean = (PlayerBean)e.nextElement();
/* 331 */       if (!pBean.isActive())
/*     */       {
/*     */ 
/* 334 */         Commands.appInstance.proxy.insertUserLastSession(pBean, 1);
/* 335 */         Room room = Commands.appInstance.getParentZone().getRoomByName(this.roomId);
/*     */         
/* 337 */         int pos = this.players.indexOf(pBean.getPlayerId());
/* 338 */         Appmethods.showLog("Before Remove isActivePlayers " + this.players.toString() + "Pos " + pos);
/* 339 */         this.players.remove(pos);
/* 340 */         this.players.add(pos, "null");
/* 341 */         Appmethods.showLog("After Remove isActivePlayers " + this.players.toString());
/*     */         
/* 343 */         ISFSObject sfso = new SFSObject();
/* 344 */         sfso = getSFSObject();
/* 345 */         sfso.putUtfString("player", pBean.getPlayerId());
/* 346 */         sfso.putBool("isSpectator", false);
/*     */         
/* 348 */         this.playerBeenList.remove(pBean.getPlayerId());
/* 349 */         Commands.appInstance.send("LeaveTable", sfso, room.getUserList());
/*     */         
/* 351 */         Appmethods.showLog("Disconnected User");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void removeInsufficientAmountUsers()
/*     */   {
/* 359 */     for (Enumeration<PlayerBean> e = this.playerBeenList.elements(); e.hasMoreElements();)
/*     */     {
/* 361 */       PlayerBean pBean = (PlayerBean)e.nextElement();
/*     */       
/*     */       TableBean tb;
/* 364 */       if (this.gameType.equals("Public")) {
/* 365 */         tb = Appmethods.getTableBean(this.tableBeanId);
/*     */       } else {
/* 367 */         tb = Appmethods.getPrivateTableBean(this.roomId);
/*     */       }
/* 369 */       if (pBean.getInplay() < tb.get_challLimit().intValue())
/*     */       {
/*     */ 
/* 372 */         Commands.appInstance.proxy.insertUserLastSession(pBean, 0);
/*     */         
/*     */ 
/* 375 */         User player = Commands.appInstance.getApi().getUserByName(pBean.getPlayerId());
/* 376 */         Room room = Commands.appInstance.getParentZone().getRoomByName(this.roomId);
/*     */         
/* 378 */         int pos = this.players.indexOf(pBean.getPlayerId());
/* 379 */         Appmethods.showLog("Before Remove isActivePlayers " + this.players.toString() + "Pos " + pos);
/* 380 */         this.players.remove(pos);
/* 381 */         this.players.add(pos, "null");
/* 382 */         Appmethods.showLog("After Remove isActivePlayers " + this.players.toString());
/* 383 */         this.playerBeenList.remove(pBean.getPlayerId());
/*     */         
/* 385 */         ISFSObject sfso = new SFSObject();
/* 386 */         sfso = getSFSObject();
/* 387 */         sfso.putUtfString("player", pBean.getPlayerId());
/* 388 */         sfso.putBool("isSpectator", false);
/* 389 */         Commands.appInstance.send("InsufficientChips", sfso, room.getUserList());
/*     */         
/*     */ 
/* 392 */         Commands.appInstance.send("GetUserLastSession", Commands.appInstance.proxy.getUserLastSessionInfo(player.getName(), 0), player);
/*     */         
/* 394 */         Appmethods.showLog("INSUFFICIENT_CHIPS");
/*     */         
/* 396 */         Appmethods.leaveRoom(player, room);
/*     */       }
/* 398 */       //TableBean tb = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeTimeUpUsers() {
/* 403 */     for (Enumeration<PlayerBean> e = this.playerBeenList.elements(); e.hasMoreElements();)
/*     */     {
/* 405 */       PlayerBean pBean = (PlayerBean)e.nextElement();
/*     */       
/* 407 */       Appmethods.showLog("pBean.getTimeUpCount() " + pBean.getTimeUpCount() + " pBean.isAutoPlay() " + pBean.isAutoPlay());
/*     */       
/*     */ 
/* 410 */       if ((pBean.getTimeUpCount() == 2) && (!pBean.isAutoPlay()))
/*     */       {
/* 412 */         Commands.appInstance.proxy.insertUserLastSession(pBean, 0);
/*     */         
/* 414 */         User player = Commands.appInstance.getApi().getUserByName(pBean.getPlayerId());
/* 415 */         Room room = Commands.appInstance.getParentZone().getRoomByName(this.roomId);
/*     */         
/* 417 */         int pos = this.players.indexOf(pBean.getPlayerId());
/* 418 */         Appmethods.showLog("Before Remove isActivePlayers " + this.players.toString() + "Pos " + pos);
/* 419 */         this.players.remove(pos);
/* 420 */         this.players.add(pos, "null");
/* 421 */         Appmethods.showLog("After Remove isActivePlayers " + this.players.toString());
/* 422 */         this.playerBeenList.remove(pBean.getPlayerId());
/*     */         
/* 424 */         ISFSObject sfso = new SFSObject();
/* 425 */         sfso = getSFSObject();
/* 426 */         sfso.putUtfString("player", pBean.getPlayerId());
/* 427 */         sfso.putBool("isSpectator", false);
/* 428 */         Commands.appInstance.send("TwoTimesTimeUp", sfso, room.getUserList());
/*     */         
/* 430 */         Appmethods.showLog("TWO_TIMES_TIMEUP");
/*     */         
/*     */ 
/* 433 */         Commands.appInstance.send("GetUserLastSession", Commands.appInstance.proxy.getUserLastSessionInfo(player.getName(), 0), player);
/*     */         
/* 435 */         Appmethods.leaveRoom(player, room);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void startGame() {
/* 441 */     Appmethods.showLog("GameBean : startGame");
/* 442 */     this.isStarted = true;
/* 443 */     this.isGameGenerating = false;
/*     */     
/*     */ 
/* 446 */     removeDisconnectedUsers();
/* 447 */     removeInsufficientAmountUsers();
/* 448 */     removeTimeUpUsers();
/*     */     
/* 450 */     if (getJoinedPlayers() >= 2)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 455 */       String currDate = Appmethods.getDateTime();
/* 456 */       setGameStartDate(currDate);
/*     */       
/*     */ 
/* 459 */       setDealer();
/*     */       
/*     */       TableBean tb;
/* 462 */       if (this.gameType.equals("Public")) {
/* 463 */         tb = Appmethods.getTableBean(this.tableBeanId);
/*     */       } else {
/* 465 */         tb = Appmethods.getPrivateTableBean(this.roomId);
/*     */       }
/* 467 */       this.gameRoundBean = new GameRoundBean(this.dealer, this.players, tb.get_boot().intValue(), tb.get_challLimit().intValue(), tb.get_potLimit().intValue(), this.roomId);
/* 468 */       this.gameRoundBean.gameInit();
/*     */       
/* 470 */      // TableBean tb = null;
/*     */       
/* 472 */       StartGameBsn sgBsn = new StartGameBsn();
/* 473 */       sgBsn.startGame(this);
/* 474 */       sgBsn = null;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 479 */       this.dealer = null;
/* 480 */       this.isStarted = false;
/*     */       
/* 482 */       if (getJoinedPlayers() == 0)
/*     */       {
/* 484 */         Room room = Commands.appInstance.getParentZone().getRoomByName(this.roomId);
/*     */         
/* 486 */         if (getGameType().equals("Private"))
/*     */         {
/* 488 */           TableBean tBean = Appmethods.getPrivateTableBean(getRoomId());
/*     */           
/* 490 */           UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
/* 491 */           ulBsn.updatePrivateTableLobby("Delete", tBean);
/* 492 */           ulBsn = null;
/*     */           
/* 494 */           Commands.appInstance.proxy.updatePrivateTable(tBean.getPrivateTableId().intValue());
/*     */           
/* 496 */           Commands.appInstance.privateTables.remove(tBean);
/*     */         }
/* 498 */         else if (this.gameType.equals("Public"))
/*     */         {
/* 500 */           Appmethods.updateDynamicRoom(this);
/*     */         }
/*     */         
/*     */ 
/* 504 */         if (room.getUserList().size() == 0) {
/* 505 */           Appmethods.removeGameBean(this);
/*     */         }
/*     */         else {
/* 508 */           startTimer(5, "CloseGame");
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 514 */         startTimer(60, "CloseGame");
/*     */       }
/*     */     }
/*     */     
/* 518 */     if (getGameType().equals("Public"))
/*     */     {
/*     */ 
/* 521 */       Appmethods.updateDynamicRoom(this);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 526 */       TableBean tBean = Appmethods.getPrivateTableBean(getRoomId());
/* 527 */       UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
/* 528 */       ulBsn.updatePrivateTableLobby("Update", tBean);
/* 529 */       ulBsn = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public int getRemainingSeconds()
/*     */   {
/* 535 */     int secondsRemain = 0;
/* 536 */     long now = System.currentTimeMillis();
/* 537 */     long elapsed = now - this.actionStartTime;
/* 538 */     long remainingTime = this.sec * 1000 - elapsed;
/* 539 */     secondsRemain = (int)(remainingTime / 1000L);
/* 540 */     if (secondsRemain <= 0)
/* 541 */       secondsRemain = 0;
/* 542 */     return secondsRemain;
/*     */   }
/*     */   
/*     */   public void startTimer(int seconds, String command) {
/*     */     try {
/* 547 */       stopTimer(); } catch (Exception localException) {}
/* 548 */     this.timer = new Timer(seconds * 1000, new GameBeanTimer(this.roomId, command));
/* 549 */     this.timer.start();
/* 550 */     this.sec = seconds;
/* 551 */     this.actionStartTime = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public void stopTimer()
/*     */   {
/* 556 */     if (this.timer != null) {
/*     */       try {
/* 558 */         this.timer.stop(); } catch (Exception localException) {}
/* 559 */       this.timer = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bean/GameBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */