 package su.sfs2x.extensions.games.teenpatticlub.bean;
 
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;

 public class PlayerProfileBean
 {
   private Integer playerId = Integer.valueOf(0);
   private String playerName;
   private String playerIP;
   private String city = null;
   private String gender = null;
   private Integer chips = Integer.valueOf(0);
   private Integer totalChips = Integer.valueOf(0);
   
 
 
   public Integer getPlayerId()
   {
     return this.playerId;
   }
   
   public void setPlayerId(Integer playerId) { this.playerId = playerId; }
   
   public String getPlayerName()
   {
     return this.playerName;
   }
   
   public void setPlayerName(String playerName) { this.playerName = playerName; }
   
   public String getPlayerIP()
   {
     return this.playerIP;
   }
   
   public void setPlayerIP(String playerIP) { this.playerIP = playerIP; }
   
   public String getCity()
   {
     return this.city;
   }
   
   public void setCity(String city) { this.city = city; }
   
   public String getGender()
   {
     return this.gender;
   }
   
   public void setGender(String gender) { this.gender = gender; }
   
   public Integer getChips()
   {
     return this.chips;
   }
   
   public void setChips(Integer chips) { this.chips = chips; }
   
   public Integer getTotalChips()
   {
     return this.totalChips;
   }
   
   public void setTotalChips(Integer totalChips) { this.totalChips = totalChips; }
   
 
   public ISFSObject getPlayerProfile()
   {
     ISFSObject sfso = new SFSObject();
     sfso.putInt("playerId", this.playerId.intValue());
     sfso.putUtfString("playerName", this.playerName);
     sfso.putUtfString("city", this.city);
     sfso.putUtfString("gender", this.gender);
     return sfso;
   }
   
 
 
   public void validateData() {}
   
 
   public void destroyBeanData()
   {
     this.playerId = null;
     this.playerIP = null;
     this.playerName = null;
     this.city = null;
     this.gender = null;
     this.chips = null;
     this.totalChips = null;
   }
 }


