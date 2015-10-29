/*    */ package su.sfs2x.extensions.games.teenpatticlub.bean;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerProfileBean
/*    */ {
/* 13 */   private Integer playerId = Integer.valueOf(0);
/*    */   private String playerName;
/*    */   private String playerIP;
/* 16 */   private String city = null;
/* 17 */   private String gender = null;
/* 18 */   private Integer chips = Integer.valueOf(0);
/* 19 */   private Integer totalChips = Integer.valueOf(0);
/*    */   
/*    */ 
/*    */ 
/*    */   public Integer getPlayerId()
/*    */   {
/* 25 */     return this.playerId;
/*    */   }
/*    */   
/* 28 */   public void setPlayerId(Integer playerId) { this.playerId = playerId; }
/*    */   
/*    */   public String getPlayerName()
/*    */   {
/* 32 */     return this.playerName;
/*    */   }
/*    */   
/* 35 */   public void setPlayerName(String playerName) { this.playerName = playerName; }
/*    */   
/*    */   public String getPlayerIP()
/*    */   {
/* 39 */     return this.playerIP;
/*    */   }
/*    */   
/* 42 */   public void setPlayerIP(String playerIP) { this.playerIP = playerIP; }
/*    */   
/*    */   public String getCity()
/*    */   {
/* 46 */     return this.city;
/*    */   }
/*    */   
/* 49 */   public void setCity(String city) { this.city = city; }
/*    */   
/*    */   public String getGender()
/*    */   {
/* 53 */     return this.gender;
/*    */   }
/*    */   
/* 56 */   public void setGender(String gender) { this.gender = gender; }
/*    */   
/*    */   public Integer getChips()
/*    */   {
/* 60 */     return this.chips;
/*    */   }
/*    */   
/* 63 */   public void setChips(Integer chips) { this.chips = chips; }
/*    */   
/*    */   public Integer getTotalChips()
/*    */   {
/* 67 */     return this.totalChips;
/*    */   }
/*    */   
/* 70 */   public void setTotalChips(Integer totalChips) { this.totalChips = totalChips; }
/*    */   
/*    */ 
/*    */   public ISFSObject getPlayerProfile()
/*    */   {
/* 75 */     ISFSObject sfso = new SFSObject();
/* 76 */     sfso.putInt("playerId", this.playerId.intValue());
/* 77 */     sfso.putUtfString("playerName", this.playerName);
/* 78 */     sfso.putUtfString("city", this.city);
/* 79 */     sfso.putUtfString("gender", this.gender);
/* 80 */     return sfso;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void validateData() {}
/*    */   
/*    */ 
/*    */   public void destroyBeanData()
/*    */   {
/* 90 */     this.playerId = null;
/* 91 */     this.playerIP = null;
/* 92 */     this.playerName = null;
/* 93 */     this.city = null;
/* 94 */     this.gender = null;
/* 95 */     this.chips = null;
/* 96 */     this.totalChips = null;
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bean/PlayerProfileBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */