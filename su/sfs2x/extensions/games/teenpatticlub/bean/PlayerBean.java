/*     */ package su.sfs2x.extensions.games.teenpatticlub.bean;
/*     */ 
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*     */ import java.util.ArrayList;
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
/*     */ public class PlayerBean
/*     */ {
/*     */   private String playerId;
/*     */   private float inPlay;
/*  22 */   private Integer timerBank = Integer.valueOf(1);
/*  23 */   private boolean isActive = true;
/*  24 */   private String avatar = "";
/*  25 */   private boolean isAutoPlay = false;
/*  26 */   private float commission = 0.0F;
/*     */   
/*     */ 
/*  29 */   private boolean isUsingExtraTime = false;
/*  30 */   private int timeUpCount = 0;
/*     */   
/*  32 */   private float startInplay = 0.0F;
/*  33 */   private ArrayList<Float> amounts = new ArrayList();
/*  34 */   private int totalHands = 0;
/*  35 */   private int wonHands = 0;
/*     */   
/*     */   public PlayerBean(String player) {
/*  38 */     setPlayerId(player);
/*  39 */     this.avatar = Commands.appInstance.proxy.getPlayerAvatar(player);
/*  40 */     this.commission = Commands.appInstance.proxy.getPlayerCommission(player);
/*     */   }
/*     */   
/*     */   public ISFSObject getSFSObject() {
/*  44 */     ISFSObject sfso = new SFSObject();
/*  45 */     sfso.putUtfString("playerId", this.playerId);
/*  46 */     sfso.putFloat("inPlay", this.inPlay);
/*  47 */     sfso.putUtfString("avatar", this.avatar);
/*  48 */     sfso.putBool("isAutoPlay", this.isAutoPlay);
/*  49 */     return sfso;
/*     */   }
/*     */   
/*     */   public ISFSObject getSessionSfsObject() {
/*  53 */     float total = 0.0F;
/*  54 */     ISFSObject sfso = new SFSObject();
/*  55 */     for (int i = 0; i < this.amounts.size(); i++)
/*     */     {
/*  57 */       total += ((Float)this.amounts.get(i)).floatValue();
/*     */     }
/*     */     
/*  60 */     sfso.putFloat("won_or_lost_amount", total);
/*  61 */     sfso.putInt("totalHands", this.totalHands);
/*  62 */     sfso.putInt("wonHands", this.wonHands);
/*     */     
/*  64 */     return sfso;
/*     */   }
/*     */   
/*     */   public float getCommission()
/*     */   {
/*  69 */     return this.commission;
/*     */   }
/*     */   
/*  72 */   public void setCommission(float commission) { this.commission = commission; }
/*     */   
/*     */   public boolean isAutoPlay() {
/*  75 */     return this.isAutoPlay;
/*     */   }
/*     */   
/*  78 */   public void setAutoPlay(boolean isAutoPlay) { this.isAutoPlay = isAutoPlay; }
/*     */   
/*     */   public float getStartInplay() {
/*  81 */     return this.startInplay;
/*     */   }
/*     */   
/*  84 */   public void setStartInplay(float startInplay) { this.startInplay = startInplay; }
/*     */   
/*     */   public ArrayList<Float> getAmounts() {
/*  87 */     return this.amounts;
/*     */   }
/*     */   
/*  90 */   public void setAmounts(ArrayList<Float> amounts) { this.amounts = amounts; }
/*     */   
/*     */   public int getTotalHands() {
/*  93 */     return this.totalHands;
/*     */   }
/*     */   
/*  96 */   public void setTotalHands(int totalHands) { this.totalHands = totalHands; }
/*     */   
/*     */   public int getWonHands() {
/*  99 */     return this.wonHands;
/*     */   }
/*     */   
/* 102 */   public void setWonHands(int wonHands) { this.wonHands = wonHands; }
/*     */   
/*     */   public String getAvatar() {
/* 105 */     return this.avatar;
/*     */   }
/*     */   
/* 108 */   public void setAvatar(String avatar) { this.avatar = avatar; }
/*     */   
/*     */   public int getTimeUpCount() {
/* 111 */     return this.timeUpCount;
/*     */   }
/*     */   
/* 114 */   public void setTimeUpCount(int timeUpCount) { this.timeUpCount = timeUpCount; }
/*     */   
/*     */   public boolean isActive() {
/* 117 */     return this.isActive;
/*     */   }
/*     */   
/* 120 */   public void setActive(boolean isActive) { this.isActive = isActive; }
/*     */   
/*     */   public boolean isUsingExtraTime() {
/* 123 */     return this.isUsingExtraTime;
/*     */   }
/*     */   
/* 126 */   public void setUsingExtraTime(boolean isUsingExtraTime) { this.isUsingExtraTime = isUsingExtraTime; }
/*     */   
/*     */   public String getPlayerId() {
/* 129 */     return this.playerId;
/*     */   }
/*     */   
/* 132 */   public void setPlayerId(String playerId) { this.playerId = playerId; }
/*     */   
/*     */   public float getInplay() {
/* 135 */     return this.inPlay;
/*     */   }
/*     */   
/* 138 */   public void setInplay(float inplay) { this.inPlay = Appmethods.setFloatFormat(inplay); }
/*     */   
/*     */   public Integer getTimerBank() {
/* 141 */     return this.timerBank;
/*     */   }
/*     */   
/* 144 */   public void setTimerBank(Integer timerBank) { this.timerBank = timerBank; }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bean/PlayerBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */