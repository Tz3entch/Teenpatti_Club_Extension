 package su.sfs2x.extensions.games.teenpatticlub.bean;
 
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import javax.swing.Timer;
 import su.sfs2x.extensions.games.teenpatticlub.timers.TableBeanTimer;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class TableBean
 {
   private Integer _id = Integer.valueOf(0);
   private Integer _boot = Integer.valueOf(0);
   private Integer _challLimit = Integer.valueOf(0);
   private Integer _potLimit = Integer.valueOf(0);
   
   private String _roomId;
   private String _type = "Public";
   private String _startDate = "";
   private String _password = "";
   private Timer timer = null;
   
   private Integer privateTableId = Integer.valueOf(0);
   
   public TableBean() {
     this._roomId = "null";
   }
   
 
   public Integer getPrivateTableId()
   {
     return this.privateTableId;
   }
   
   public void setPrivateTableId(Integer privateTableId) { this.privateTableId = privateTableId; }
   
   public String get_roomId() {
     return this._roomId;
   }
   
   public void set_roomId(String _roomId) { this._roomId = _roomId; }
   
   public Integer get_id() {
     return this._id;
   }
   
   public void set_id(Integer _id) { this._id = _id; }
   
   public Integer get_boot() {
     return this._boot;
   }
   
   public void set_boot(Integer _boot) { this._boot = _boot; }
   
   public Integer get_challLimit() {
     return this._challLimit;
   }
   
   public void set_challLimit(Integer _challLimit) { this._challLimit = _challLimit; }
   
   public Integer get_potLimit() {
     return this._potLimit;
   }
   
   public void set_potLimit(Integer _potLimit) { this._potLimit = _potLimit; }
   
   public String get_type() {
     return this._type;
   }
   
   public void set_type(String _type) { this._type = _type; }
   
   public String get_startDate() {
     return this._startDate;
   }
   
   public void set_startDate(String _startDate) { this._startDate = _startDate; }
   
   public String get_password() {
     return this._password;
   }
   
   public void set_password(String _password) { this._password = _password; }
   
 
   public ISFSObject getSFSObject()
   {
     ISFSObject sfso = new SFSObject();
     sfso.putInt("id", this._id.intValue());
     sfso.putInt("boot", this._boot.intValue());
     sfso.putInt("chall_limit", this._challLimit.intValue());
     sfso.putInt("pot_limit", this._potLimit.intValue());
     
     int joinedPlayers = 0;
     if (!this._roomId.equals("null"))
     {
       GameBean gameBean = Appmethods.getGameBean(this._roomId);
       if (gameBean != null)
       {
         joinedPlayers = gameBean.getJoinedPlayers();
       }
     }
     sfso.putInt("joinedPlayers", joinedPlayers);
     return sfso;
   }
   
   public ISFSObject getPrivateTableSFSObject() {
     ISFSObject sfso = new SFSObject();
     sfso = getSFSObject();
     sfso.putUtfString("owner", this._roomId);
     sfso.putUtfString("password", this._password);
     sfso.putUtfString("startDatetime", this._startDate);
     sfso.putInt("privateTableId", this.privateTableId.intValue());
     return sfso;
   }
   
   public void startTimer(int seconds) {
     try {
       stopTimer(); } catch (Exception localException) {}
     this.timer = new Timer(seconds * 1000, new TableBeanTimer(this._roomId));
   }
   
 
   public void stopTimer()
   {
     if (this.timer != null) {
       try {
         this.timer.stop(); } catch (Exception localException) {}
       this.timer = null;
     }
   }
 }


