/*     */ package su.sfs2x.extensions.games.teenpatticlub.bean;
/*     */ 
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*     */ import java.util.ArrayList;

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
/*     */ public class PlayerRoundBean
/*     */ {
/*     */   private String playerId;
/*  20 */   private ArrayList<Integer> cards = new ArrayList();
/*     */   
/*  22 */   private boolean isLeaveTable = false;
/*  23 */   private boolean isSeen = false;
/*  24 */   private boolean isPack = false;
/*  25 */   private boolean isShow = false;
/*  26 */   private boolean isShowCards = false;
/*  27 */   private boolean isSideShow = false;
/*  28 */   private boolean isSitOut = false;
/*  29 */   private boolean isSendSession = false;
/*     */   
/*     */ 
/*  32 */   private boolean isSideShowSelected = false;
/*     */   
/*  34 */   private String sideShowPlayer = "null";
/*     */   
/*  36 */   private boolean isSideShowCalled = false;
/*     */   
/*     */ 
/*  39 */   private ArrayList<Integer> handAmounts = new ArrayList();
/*  40 */   private int totalBetAmount = 0;
/*  41 */   private float wonAmount = 0.0F;
/*  42 */   private String lastAction = "";
/*  43 */   private int rank = 0;
/*     */   
/*     */ 
/*     */   public ISFSObject getSFSObject()
/*     */   {
/*  48 */     ISFSObject sfso = new SFSObject();
/*     */     
/*  50 */     sfso.putUtfString("playerId", this.playerId);
/*  51 */     sfso.putIntArray("cards", this.cards);
/*  52 */     sfso.putBool("isLeaveTable", this.isLeaveTable);
/*  53 */     sfso.putBool("isSeen", this.isSeen);
/*  54 */     sfso.putBool("isPack", this.isPack);
/*  55 */     sfso.putBool("isShow", this.isShow);
/*  56 */     sfso.putBool("isSitOut", this.isSitOut);
/*     */     
/*  58 */     return sfso;
/*     */   }
/*     */   
/*     */   public PlayerRoundBean(String player)
/*     */   {
/*  63 */     this.playerId = player;
/*     */   }
/*     */   
/*     */   public boolean isSitOut() {
/*  67 */     return this.isSitOut;
/*     */   }
/*     */   
/*  70 */   public void setSitOut(boolean isSitOut) { this.isSitOut = isSitOut; }
/*     */   
/*     */   public boolean isSideShowCalled() {
/*  73 */     return this.isSideShowCalled;
/*     */   }
/*     */   
/*  76 */   public void setSideShowCalled(boolean isSideShowCalled) { this.isSideShowCalled = isSideShowCalled; }
/*     */   
/*     */   public boolean isSideShow() {
/*  79 */     return this.isSideShow;
/*     */   }
/*     */   
/*  82 */   public void setSideShow(boolean isSideShow) { this.isSideShow = isSideShow; }
/*     */   
/*     */   public boolean isSideShowSelected() {
/*  85 */     return this.isSideShowSelected;
/*     */   }
/*     */   
/*  88 */   public void setSideShowSelected(boolean isSideShowSelected) { this.isSideShowSelected = isSideShowSelected; }
/*     */   
/*     */   public String getSideShowPlayer() {
/*  91 */     return this.sideShowPlayer;
/*     */   }
/*     */   
/*  94 */   public void setSideShowPlayer(String sideShowPlayer) { this.sideShowPlayer = sideShowPlayer; }
/*     */   
/*     */   public String getLastAction() {
/*  97 */     return this.lastAction;
/*     */   }
/*     */   
/* 100 */   public void setLastAction(String lastAction) { this.lastAction = lastAction; }
/*     */   
/*     */   public int getRank() {
/* 103 */     return this.rank;
/*     */   }
/*     */   
/* 106 */   public void setRank(int rank) { this.rank = rank; }
/*     */   
/*     */   public boolean isShow() {
/* 109 */     return this.isShow;
/*     */   }
/*     */   
/* 112 */   public void setShow(boolean isShow) { this.isShow = isShow; }
/*     */   
/*     */   public boolean isShowCards() {
/* 115 */     return this.isShowCards;
/*     */   }
/*     */   
/* 118 */   public void setShowCards(boolean isShowCards) { this.isShowCards = isShowCards; }
/*     */   
/*     */   public ArrayList<Integer> getHandAmounts() {
/* 121 */     return this.handAmounts;
/*     */   }
/*     */   
/* 124 */   public void setHandAmounts(ArrayList<Integer> handAmounts) { this.handAmounts = handAmounts; }
/*     */   
/*     */   public int getTotalBetAmount() {
/* 127 */     return this.totalBetAmount;
/*     */   }
/*     */   
/* 130 */   public void setTotalBetAmount(int totalBetAmount) { this.totalBetAmount = totalBetAmount; }
/*     */   
/*     */   public String getPlayerId() {
/* 133 */     return this.playerId;
/*     */   }
/*     */   
/* 136 */   public void setPlayerId(String playerId) { this.playerId = playerId; }
/*     */   
/*     */   public ArrayList<Integer> getCards() {
/* 139 */     return this.cards;
/*     */   }
/*     */   
/* 142 */   public void setCards(ArrayList<Integer> cards) { this.cards = cards; }
/*     */   
/*     */   public float getWonAmount() {
/* 145 */     return this.wonAmount;
/*     */   }
/*     */   
/* 148 */   public void setWonAmount(float wonAmount) { this.wonAmount = Appmethods.setFloatFormat(wonAmount); }
/*     */   
/*     */   public boolean isLeaveTable() {
/* 151 */     return this.isLeaveTable;
/*     */   }
/*     */   
/* 154 */   public void setLeaveTable(boolean isLeaveTable) { this.isLeaveTable = isLeaveTable; }
/*     */   
/*     */   public boolean isSeen() {
/* 157 */     return this.isSeen;
/*     */   }
/*     */   
/* 160 */   public void setSeen(boolean isSeen) { this.isSeen = isSeen; }
/*     */   
/*     */   public boolean isPack() {
/* 163 */     return this.isPack;
/*     */   }
/*     */   
/* 166 */   public void setPack(boolean isPack) { this.isPack = isPack; }
/*     */   
/*     */   public boolean isSendSession()
/*     */   {
/* 170 */     return this.isSendSession;
/*     */   }
/*     */   
/*     */   public void setSendSession(boolean isSendSession) {
/* 174 */     this.isSendSession = isSendSession;
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bean/PlayerRoundBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */