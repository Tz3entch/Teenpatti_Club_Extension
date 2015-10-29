/*     */ package su.sfs2x.extensions.games.teenpatticlub.bean;
/*     */ 
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*     */ import javax.swing.Timer;
import su.sfs2x.extensions.games.teenpatticlub.timers.TableBeanTimer;
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
/*     */ 
/*     */ public class TableBean
/*     */ {
/*  22 */   private Integer _id = Integer.valueOf(0);
/*  23 */   private Integer _boot = Integer.valueOf(0);
/*  24 */   private Integer _challLimit = Integer.valueOf(0);
/*  25 */   private Integer _potLimit = Integer.valueOf(0);
/*     */   
/*     */   private String _roomId;
/*  28 */   private String _type = "Public";
/*  29 */   private String _startDate = "";
/*  30 */   private String _password = "";
/*  31 */   private Timer timer = null;
/*     */   
/*  33 */   private Integer privateTableId = Integer.valueOf(0);
/*     */   
/*     */   public TableBean() {
/*  36 */     this._roomId = "null";
/*     */   }
/*     */   
/*     */ 
/*     */   public Integer getPrivateTableId()
/*     */   {
/*  42 */     return this.privateTableId;
/*     */   }
/*     */   
/*  45 */   public void setPrivateTableId(Integer privateTableId) { this.privateTableId = privateTableId; }
/*     */   
/*     */   public String get_roomId() {
/*  48 */     return this._roomId;
/*     */   }
/*     */   
/*  51 */   public void set_roomId(String _roomId) { this._roomId = _roomId; }
/*     */   
/*     */   public Integer get_id() {
/*  54 */     return this._id;
/*     */   }
/*     */   
/*  57 */   public void set_id(Integer _id) { this._id = _id; }
/*     */   
/*     */   public Integer get_boot() {
/*  60 */     return this._boot;
/*     */   }
/*     */   
/*  63 */   public void set_boot(Integer _boot) { this._boot = _boot; }
/*     */   
/*     */   public Integer get_challLimit() {
/*  66 */     return this._challLimit;
/*     */   }
/*     */   
/*  69 */   public void set_challLimit(Integer _challLimit) { this._challLimit = _challLimit; }
/*     */   
/*     */   public Integer get_potLimit() {
/*  72 */     return this._potLimit;
/*     */   }
/*     */   
/*  75 */   public void set_potLimit(Integer _potLimit) { this._potLimit = _potLimit; }
/*     */   
/*     */   public String get_type() {
/*  78 */     return this._type;
/*     */   }
/*     */   
/*  81 */   public void set_type(String _type) { this._type = _type; }
/*     */   
/*     */   public String get_startDate() {
/*  84 */     return this._startDate;
/*     */   }
/*     */   
/*  87 */   public void set_startDate(String _startDate) { this._startDate = _startDate; }
/*     */   
/*     */   public String get_password() {
/*  90 */     return this._password;
/*     */   }
/*     */   
/*  93 */   public void set_password(String _password) { this._password = _password; }
/*     */   
/*     */ 
/*     */   public ISFSObject getSFSObject()
/*     */   {
/*  98 */     ISFSObject sfso = new SFSObject();
/*  99 */     sfso.putInt("id", this._id.intValue());
/* 100 */     sfso.putInt("boot", this._boot.intValue());
/* 101 */     sfso.putInt("chall_limit", this._challLimit.intValue());
/* 102 */     sfso.putInt("pot_limit", this._potLimit.intValue());
/*     */     
/* 104 */     int joinedPlayers = 0;
/* 105 */     if (!this._roomId.equals("null"))
/*     */     {
/* 107 */       GameBean gameBean = Appmethods.getGameBean(this._roomId);
/* 108 */       if (gameBean != null)
/*     */       {
/* 110 */         joinedPlayers = gameBean.getJoinedPlayers();
/*     */       }
/*     */     }
/* 113 */     sfso.putInt("joinedPlayers", joinedPlayers);
/* 114 */     return sfso;
/*     */   }
/*     */   
/*     */   public ISFSObject getPrivateTableSFSObject() {
/* 118 */     ISFSObject sfso = new SFSObject();
/* 119 */     sfso = getSFSObject();
/* 120 */     sfso.putUtfString("owner", this._roomId);
/* 121 */     sfso.putUtfString("password", this._password);
/* 122 */     sfso.putUtfString("startDatetime", this._startDate);
/* 123 */     sfso.putInt("privateTableId", this.privateTableId.intValue());
/* 124 */     return sfso;
/*     */   }
/*     */   
/*     */   public void startTimer(int seconds) {
/*     */     try {
/* 129 */       stopTimer(); } catch (Exception localException) {}
/* 130 */     this.timer = new Timer(seconds * 1000, new TableBeanTimer(this._roomId));
/*     */   }
/*     */   
/*     */ 
/*     */   public void stopTimer()
/*     */   {
/* 136 */     if (this.timer != null) {
/*     */       try {
/* 138 */         this.timer.stop(); } catch (Exception localException) {}
/* 139 */       this.timer = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bean/TableBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */