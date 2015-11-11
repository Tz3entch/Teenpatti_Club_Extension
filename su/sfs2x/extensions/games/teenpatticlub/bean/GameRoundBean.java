/*     */ package su.sfs2x.extensions.games.teenpatticlub.bean;
/*     */ 
/*     */ import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSArray;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.ConcurrentHashMap;

import com.smartfoxserver.v2.entities.data.SFSObject;
import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameRoundBean
/*     */ {
/*  26 */   private int boot = 0;
/*  27 */   private int challLimit = 0;
/*  28 */   private int potLimit = 0;
/*     */   private String dealer;
/*  30 */   private ArrayList<String> players = new ArrayList();
/*  31 */   private ConcurrentHashMap<String, PlayerRoundBean> playerRoundBeans = new ConcurrentHashMap();
/*  32 */   private String turn = "null";
/*  33 */   private Integer potAmount = Integer.valueOf(0);
/*  34 */   private Integer bootAmount = Integer.valueOf(0);
/*  35 */   private ArrayList<Integer> deck = new ArrayList();
/*  36 */   private int handNo = 1;
/*  37 */   private int blindBet = 0;
/*  38 */   private int challBet = 0;
/*     */   
/*  40 */   private int turnNo = 0;
/*  41 */   private int handActivePlayersCount = 0;
/*  42 */   private String roomId = null;
/*  43 */   private ArrayList<PlayerRoundBean> highRankUsers = new ArrayList();
/*  44 */   private String wonPlayer = "null";
/*  45 */   private float wonAmount = 0.0F;
/*  46 */   private boolean isShowCalled = false;
/*  47 */   private String wonReason = "";
/*  48 */   private int handId = 0;
/*     */   
/*  50 */   private boolean isPotLimitExceed = false;
/*     */   
/*     */ 
/*     */   public GameRoundBean(String dealer, ArrayList<String> players, int boot, int challLimit, int potLimit, String roomId)
/*     */   {
/*  55 */     this.dealer = dealer;
/*  56 */     this.players = players;
/*  57 */     this.boot = boot;
/*  58 */     this.challLimit = challLimit;
/*  59 */     this.potLimit = potLimit;
/*  60 */     this.roomId = roomId;
/*  61 */     setPlayerRoundBeans();
/*     */     
/*  63 */     Appmethods.showLog("GameRoundBean Players:" + players.toString());
/*     */   }
/*     */   
/*     */ 
/*     */   public ISFSObject getSFSObject(ISFSObject sfso)
/*     */   {
/*  69 */     sfso.putUtfString("dealer", this.dealer);
/*  70 */     sfso.putSFSArray("playerRoundBeans", getPlayerRoundBeansSFSArray());
/*  71 */     sfso.putUtfString("turn", this.turn);
/*  72 */     sfso.putInt("potAmount", this.potAmount.intValue());
/*  73 */     sfso.putInt("handNo", this.handNo);
/*  74 */     sfso.putInt("blindBet", this.blindBet);
/*  75 */     sfso.putInt("challBet", this.challBet);
/*  76 */     sfso.putBool("isShowCalled", this.isShowCalled);
/*  77 */     return sfso;
/*     */   }
/*     */   
/*     */ 
/*     */   public ISFSObject getGameWonSFSObject(ISFSObject sfso)
/*     */   {
/*  83 */     sfso.putUtfString("wonPlayer", this.wonPlayer);
/*  84 */     sfso.putFloat("wonAmount", this.wonAmount);
/*  85 */     sfso.putUtfString("wonReason", this.wonReason);
/*  86 */     sfso.putBool("isPotLimitExceed", this.isPotLimitExceed);
/*  87 */     return sfso;
/*     */   }
/*     */   
/*     */ 
/*     */   public SFSArray getPlayerRoundBeansSFSArray()
/*     */   {
/*  93 */     SFSArray playerRoundBeans = new SFSArray();
/*     */     
/*  95 */     for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*     */     {
/*  97 */       PlayerRoundBean bean = (PlayerRoundBean)e.nextElement();
/*  98 */       playerRoundBeans.addSFSObject(bean.getSFSObject());
/*     */     }
/* 100 */     return playerRoundBeans;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isPotLimitExceed()
/*     */   {
/* 106 */     return this.isPotLimitExceed;
/*     */   }
/*     */   
/* 109 */   public void setPotLimitExceed(boolean isPotLimitExceed) { this.isPotLimitExceed = isPotLimitExceed; }
/*     */   
/*     */   public int getHandId() {
/* 112 */     return this.handId;
/*     */   }
/*     */   
/* 115 */   public void setHandId(int handId) { this.handId = handId; }
/*     */   
/*     */   public Integer getBootAmount() {
/* 118 */     return this.bootAmount;
/*     */   }
/*     */   
/* 121 */   public void setBootAmount(Integer bootAmount) { this.bootAmount = bootAmount; }
/*     */   
/*     */   public String getWonReason() {
/* 124 */     return this.wonReason;
/*     */   }
/*     */   
/* 127 */   public void setWonReason(String wonReason) { this.wonReason = wonReason; }
/*     */   
/*     */   public boolean isShowCalled() {
/* 130 */     return this.isShowCalled;
/*     */   }
/*     */   
/* 133 */   public void setShowCalled(boolean isShowCalled) { this.isShowCalled = isShowCalled; }
/*     */   
/*     */   public float getWonAmount() {
/* 136 */     return this.wonAmount;
/*     */   }
/*     */   
/* 139 */   public void setWonAmount(float wonAmount) { this.wonAmount = Appmethods.setFloatFormat(wonAmount); }
/*     */   
/*     */   public String getWonPlayer() {
/* 142 */     return this.wonPlayer;
/*     */   }
/*     */   
/* 145 */   public void setWonPlayer(String wonPlayer) { this.wonPlayer = wonPlayer; }
/*     */   
/*     */   public ArrayList<PlayerRoundBean> getHighRankUsers() {
/* 148 */     return this.highRankUsers;
/*     */   }
/*     */   
/* 151 */   public void setHighRankUsers(ArrayList<PlayerRoundBean> highRankUsers) { this.highRankUsers = highRankUsers; }
/*     */   
/*     */   public int getBlindBet() {
/* 154 */     return this.blindBet;
/*     */   }
/*     */   
/* 157 */   public void setBlindBet(int blindBet) { this.blindBet = blindBet; }
/*     */   
/*     */   public int getChallBet() {
/* 160 */     return this.challBet;
/*     */   }
/*     */   
/* 163 */   public void setChallBet(int challBet) { this.challBet = challBet; }
/*     */   
/*     */   public int getHandNo() {
/* 166 */     return this.handNo;
/*     */   }
/*     */   
/* 169 */   public void setHandNo(int handNo) { this.handNo = handNo; }
/*     */   
/*     */   public ArrayList<String> getPlayers() {
/* 172 */     return this.players;
/*     */   }
/*     */   
/* 175 */   public void setPlayers(ArrayList<String> players) { this.players = players; }
/*     */   
/*     */   public ConcurrentHashMap<String, PlayerRoundBean> getPlayerRoundBeans() {
/* 178 */     return this.playerRoundBeans;
/*     */   }
/*     */   
/*     */   public void setPlayerRoundBeans(ConcurrentHashMap<String, PlayerRoundBean> playerRoundBeans) {
/* 182 */     this.playerRoundBeans = playerRoundBeans;
/*     */   }
/*     */   
/* 185 */   public String getDealer() { return this.dealer; }
/*     */   
/*     */   public void setDealer(String dealer) {
/* 188 */     this.dealer = dealer;
/*     */   }
/*     */   
/* 191 */   public Integer getPotAmount() { return this.potAmount; }
/*     */   
/*     */   public void setPotAmount(Integer potAmount) {
/* 194 */     this.potAmount = potAmount;
/*     */   }
/*     */   
/* 197 */   public int getBoot() { return this.boot; }
/*     */   
/*     */   public void setBoot(int boot) {
/* 200 */     this.boot = boot;
/*     */   }
/*     */   
/* 203 */   public int getChallLimit() { return this.challLimit; }
/*     */   
/*     */   public void setChallLimit(int challLimit) {
/* 206 */     this.challLimit = challLimit;
/*     */   }
/*     */   
/* 209 */   public int getPotLimit() { return this.potLimit; }
/*     */   
/*     */   public void setPotLimit(int potLimit) {
/* 212 */     this.potLimit = potLimit;
/*     */   }
/*     */   
/* 215 */   public String getTurn() { return this.turn; }
/*     */   
/*     */   public void setTurn(String turn) {
/* 218 */     this.turn = turn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removePlayer(String name)
/*     */   {
/* 225 */     int pos = -1;
/* 226 */     pos = this.players.indexOf(name);
/* 227 */     if (pos != -1)
/*     */     {
/* 229 */       Appmethods.showLog("GRB Before Remove " + this.players.toString());
/* 230 */       this.players.remove(pos);
/* 231 */       this.players.add(pos, "null");
/* 232 */       Appmethods.showLog("GRB After Remove " + this.players.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private void setPlayerRoundBeans()
/*     */   {
/* 238 */     for (int i = 0; i < this.players.size(); i++)
/*     */     {
/* 240 */       if (!((String)this.players.get(i)).equals("null"))
/*     */       {
/* 242 */         PlayerRoundBean playerRoundBean = new PlayerRoundBean((String)this.players.get(i));
/* 243 */         this.playerRoundBeans.put((String)this.players.get(i), playerRoundBean);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getPlayerCardsString()
/*     */   {
/* 250 */     String cards = null;
/* 251 */     for (int i = 0; i < this.players.size(); i++)
/*     */     {
/*     */ 
/*     */ 
/* 255 */       if (!((String)this.players.get(i)).equals("null"))
/*     */       {
/* 257 */         PlayerRoundBean playerRoundBean = (PlayerRoundBean)this.playerRoundBeans.get(this.players.get(i));
/* 258 */         if (cards != null)
/*     */         {
/* 260 */           cards = cards + "*" + playerRoundBean.getCards().toString();
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 265 */           cards = playerRoundBean.getCards().toString();
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */       }
/* 272 */       else if (cards != null) {
/* 273 */         cards = cards + "*null";
/*     */       } else {
/* 275 */         cards = "null";
/*     */       }
/*     */     }
/* 278 */     return cards;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTurnPlayer()
/*     */   {
/* 284 */     Appmethods.showLog("setTurnPlayer");
/* 285 */     Appmethods.showLog("Players " + this.players);
/* 286 */     if (this.turn.equals("null"))
/*     */     {
/* 288 */       this.handActivePlayersCount = getActivePlayersCount();
/* 289 */       int pos = this.players.indexOf(this.dealer);
/*     */       
/* 291 */       Appmethods.showLog("Pos :" + pos);
/*     */       
/* 293 */       for (int i = 1; i < this.players.size(); i++)
/*     */       {
/* 295 */         int no = pos + i;
/* 296 */         if (no >= this.players.size()) {
/* 297 */           no %= this.players.size();
/*     */         }
/* 299 */         Appmethods.showLog("Pos No:" + no);
/*     */         
/* 301 */         if (!((String)this.players.get(no)).equals("null"))
/*     */         {
/* 303 */           this.turn = ((String)this.players.get(no));
/* 304 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 310 */       Appmethods.showLog("turn " + this.turn);
/* 311 */       int pos = this.players.indexOf(this.turn);
/* 312 */       Appmethods.showLog("1Pos :" + pos);
/*     */       
/* 314 */       for (int i = 1; i < this.players.size(); i++)
/*     */       {
/* 316 */         int no = pos + i;
/* 317 */         if (no >= this.players.size()) {
/* 318 */           no %= this.players.size();
/*     */         }
/* 320 */         Appmethods.showLog("Pos No:" + no);
/* 321 */         if (!((String)this.players.get(no)).equals("null"))
/*     */         {
/* 323 */           PlayerRoundBean prBean = (PlayerRoundBean)getPlayerRoundBeans().get(this.players.get(no));
/* 324 */           if (prBean != null)
/*     */           {
/* 326 */             if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut()))
/*     */             {
/* 328 */               this.turn = ((String)this.players.get(no));
/* 329 */               this.turnNo += 1;
/* 330 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 336 */       Appmethods.showLog("turn " + this.turn);
/*     */       
/* 338 */       if (this.turnNo == this.handActivePlayersCount)
/*     */       {
/* 340 */         this.handActivePlayersCount = getActivePlayersCount();
/* 341 */         this.turnNo = 0;
/* 342 */         this.handNo += 1;
/*     */       }
/*     */     }
/*     */     
/* 346 */     setPlayerBits();
/*     */   }
/*     */   
/*     */ 
/*     */   private void setPlayerBits()
/*     */   {
/* 352 */     if (getActivePlayersCount() == 2)
/*     */     {
/* 354 */       for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*     */       {
/* 356 */         PlayerRoundBean bean = (PlayerRoundBean)e.nextElement();
/* 357 */         if ((!bean.isLeaveTable()) && (!bean.isPack()) && (!bean.isSitOut()))
/*     */         {
/* 359 */           bean.setShow(true);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 365 */     if ((isAllPlayersSeen()) && (getActivePlayersCount() >= 3) && (getSideShowNotSelectedPlayersCount()))
/*     */     {
/* 367 */       for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*     */       {
/* 369 */         PlayerRoundBean bean = (PlayerRoundBean)e.nextElement();
/* 370 */         PlayerBean pBean = (PlayerBean)Appmethods.getGameBean(this.roomId).getPlayerBeenList().get(bean.getPlayerId());
/* 371 */         if ((!bean.isLeaveTable()) && (!bean.isPack()) && (!bean.isSitOut()) && (!bean.isSideShowSelected()) && (pBean.getInplay() >= this.challBet))
/*     */         {
/* 373 */           bean.setSideShow(true);
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     } else {
/* 379 */       for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*     */       {
/* 381 */         PlayerRoundBean bean = (PlayerRoundBean)e.nextElement();
/* 382 */         bean.setSideShow(false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean getSideShowNotSelectedPlayersCount()
/*     */   {
/* 389 */     int count = 0;
/* 390 */     for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*     */     {
/* 392 */       PlayerRoundBean bean = (PlayerRoundBean)e.nextElement();
/* 393 */       PlayerBean pBean = (PlayerBean)Appmethods.getGameBean(this.roomId).getPlayerBeenList().get(bean.getPlayerId());
/* 394 */       if ((!bean.isLeaveTable()) && (!bean.isPack()) && (!bean.isSitOut()) && (!bean.isSideShowSelected()) && (pBean.getInplay() >= this.challBet))
/*     */       {
/* 396 */         count++;
/*     */       }
/*     */     }
/* 399 */     if (count >= 2)
/* 400 */       return true;
/* 401 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isAllPlayersSeen()
/*     */   {
/* 406 */     for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*     */     {
/* 408 */       PlayerRoundBean bean = (PlayerRoundBean)e.nextElement();
/* 409 */       if (!bean.isSeen())
/* 410 */         return false;
/*     */     }
/* 412 */     return true;
/*     */   }
/*     */   
/*     */   public int getActivePlayersCount() {
/* 416 */     int count = 0;
/*     */     
/* 418 */     for (Enumeration<PlayerRoundBean> e = this.playerRoundBeans.elements(); e.hasMoreElements();)
/*     */     {
/* 420 */       PlayerRoundBean prBean = (PlayerRoundBean)e.nextElement();
/* 421 */       if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut()))
/*     */       {
/* 423 */         count++;
/*     */       }
/*     */     }
/* 426 */     return count;
/*     */   }
/*     */   
/*     */   private void shuffelCards()
/*     */   {
/* 431 */     ArrayList<Integer> cards = new ArrayList();
/* 432 */     Random randomGenerator = new Random();
/* 433 */     for (int idx = 1; idx <= 52; idx++) {
/* 434 */       cards.add(Integer.valueOf(idx));
/*     */     }
/*     */     
/*     */ 
/* 438 */     for (int idx = 1; idx <= 52; idx++)
/*     */     {
/* 440 */       int randomInt = randomGenerator.nextInt(cards.size());
/* 441 */       this.deck.add((Integer)cards.get(randomInt));
/* 442 */       cards.remove(randomInt);
/*     */     }
/*     */   }
/*     */   
/*     */   private void distributeCards()
/*     */   {
/* 448 */     Integer shufflerPosition = Integer.valueOf(this.players.indexOf(this.dealer));
/*     */     
/* 450 */     for (int i = 0; i < 3; i++)
/*     */     {
/* 452 */       for (int j = 1; j <= this.players.size(); j++) {
/*     */         int agno;
/*     */         //int agno;
/* 455 */         if (j + shufflerPosition.intValue() >= this.players.size())
/*     */         {
/* 457 */           agno = (j + shufflerPosition.intValue()) % this.players.size();
/*     */         }
/*     */         else
/*     */         {
/* 461 */           agno = j + shufflerPosition.intValue();
/*     */         }
/*     */         
/* 464 */         if (!((String)this.players.get(agno)).equals("null"))
/*     */         {
/* 466 */           PlayerRoundBean prBean = (PlayerRoundBean)getPlayerRoundBeans().get(this.players.get(agno));
/* 467 */           prBean.getCards().add((Integer)this.deck.get(0));
/* 468 */           this.deck.remove(0);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void cutBootAmount() {
/* 475 */     for (Enumeration<PlayerRoundBean> e = this.playerRoundBeans.elements(); e.hasMoreElements();)
/*     */     {
/* 477 */       PlayerRoundBean prBean = (PlayerRoundBean)e.nextElement();
/* 478 */       Appmethods.cutHandAmount(this.roomId, prBean, Integer.valueOf(this.boot));
/* 479 */       this.potAmount = Integer.valueOf(this.potAmount.intValue() + this.boot);
/* 480 */       this.bootAmount = Integer.valueOf(this.bootAmount.intValue() + this.boot);
/* 481 */       this.handId += 1;
/* 482 */       float commission = Commands.appInstance.proxy.getPlayerCommission(prBean.getPlayerId());
/*     */       
/* 484 */       Commands.appInstance.proxy.insertUserAction(this.roomId, prBean.getPlayerId(), this.boot, "Boot", commission);
/*     */     }
/* 486 */     this.blindBet = this.boot;
/* 487 */     this.challBet = (this.boot * 2);
/*     */   }
/*     */   
/*     */ 
/*     */   public void gameInit()
/*     */   {
/* 493 */     setTurnPlayer();
/*     */     
/* 495 */     shuffelCards();
/*     */     
/* 497 */     distributeCards();
/*     */     
/* 499 */     cutBootAmount();
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bean/GameRoundBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */