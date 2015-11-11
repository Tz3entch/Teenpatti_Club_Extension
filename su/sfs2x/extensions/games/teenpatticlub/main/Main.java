/*     */ package su.sfs2x.extensions.games.teenpatticlub.main;
/*     */ 
/*     */ import com.smartfoxserver.v2.SmartFoxServer;
/*     */ import com.smartfoxserver.v2.api.CreateRoomSettings;
/*     */ import com.smartfoxserver.v2.api.ISFSApi;
/*     */ import com.smartfoxserver.v2.core.SFSEventType;
/*     */ import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
/*     */ import com.smartfoxserver.v2.entities.data.SFSArray;
/*     */ import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
/*     */ import com.smartfoxserver.v2.extensions.SFSExtension;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.classes.ConnectDisconnectUser;
import su.sfs2x.extensions.games.teenpatticlub.events.LoginEventHandler;
import su.sfs2x.extensions.games.teenpatticlub.events.LogoutEventHandler;
import su.sfs2x.extensions.games.teenpatticlub.events.ServerReadyHandler;
import su.sfs2x.extensions.games.teenpatticlub.events.UserDisconnectedEventHandler;
import su.sfs2x.extensions.games.teenpatticlub.events.UserJoinEventHandler;
import su.sfs2x.extensions.games.teenpatticlub.events.UserRegistrationEventHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.ActionHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.CreatePrivateTableHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.GetCurrentRunningGamesHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.GetLobbyHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.GetLobbyRoomHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.GetPlayerProfileHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.JoinTableHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.JoinUserHadler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.LeaveTableHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.SinkDataHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.UpdateProfileHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.UserPingBackHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.ChangeDealerHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.CheckDeviceIdHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.DealerTipHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.GetDealerHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.GetGiftsHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.GetPlayerSession;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.GetTransactionsHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.GetUserGiftsHadler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.GetUserLastSessionHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.MessageHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.RedeemChipsHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.SendGiftHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.SetAutoPlayHandler;
import su.sfs2x.extensions.games.teenpatticlub.handlers.tp.SitOutHandler;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

/*     */ public class Main extends SFSExtension
/*     */ {
/*  64 */   private ConcurrentHashMap<String, GameBean> games = null;
/*     */   public SmartFoxServer sfs;
/*  66 */   public SQLProxy proxy = null;
/*  67 */   public ArrayList<TableBean> publicTables = null;
/*  68 */   public ArrayList<TableBean> privateTables = null;
/*  69 */   public SFSArray gifts = null;
/*     */   public ConnectDisconnectUser cdUser;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init()
/*     */   {
/*  81 */     su.sfs2x.extensions.games.teenpatticlub.constants.Commands.appInstance = this;
/*     */     
/*  83 */     addEventHandler(SFSEventType.SERVER_READY, ServerReadyHandler.class);
              addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
/* 131 */     addEventHandler(SFSEventType.USER_JOIN_ROOM, UserJoinEventHandler.class);
/* 132 */     addEventHandler(SFSEventType.USER_DISCONNECT, UserDisconnectedEventHandler.class);
/* 133 */     addEventHandler(SFSEventType.USER_LOGOUT, LogoutEventHandler.class);

              //Appmethods.showLog("3patticlub Server Started");
/*     */     
/*  85 */     this.games = new ConcurrentHashMap<>();
/*  86 */     this.publicTables = new ArrayList<>();
/*  87 */     this.privateTables = new ArrayList<>();
/*  88 */     this.gifts = new SFSArray();
/*  89 */     this.sfs = SmartFoxServer.getInstance();
/*  90 */     this.proxy = new SQLProxy(getParentZone());
/*  91 */     this.proxy.getPublicTables();
/*  92 */     this.proxy.getPrivateTables();
/*  93 */     this.proxy.getGifts();
/*  94 */     this.proxy.serverRestartedCloseGames();
/*     */     
/*  96 */     this.cdUser = new ConnectDisconnectUser();
/*     */     try
/*     */     {
/*  99 */       makeLobbyRoom("Lobby");
/*     */     }
/*     */     catch (Exception localException) {}
/* 102 */     addRequestHandler("GetLobby", GetLobbyHandler.class);
/* 103 */     addRequestHandler("GetLobbyRoom", GetLobbyRoomHandler.class);
/* 104 */     addRequestHandler("JoinUser", JoinUserHadler.class);
/* 105 */     addRequestHandler("JoinTable", JoinTableHandler.class);
/* 106 */     addRequestHandler("LeaveTable", LeaveTableHandler.class);
/* 107 */     addRequestHandler("Action", ActionHandler.class);
/* 108 */     addRequestHandler("GetPlayerProfile", GetPlayerProfileHandler.class);
/* 109 */     addRequestHandler("UserPingBack", UserPingBackHandler.class);
/* 110 */     addRequestHandler("CreatePrivateTable", CreatePrivateTableHandler.class);
/* 111 */     addRequestHandler("GetCurrentRunningGames", GetCurrentRunningGamesHandler.class);
/* 112 */     addRequestHandler("ReedemChips", RedeemChipsHandler.class);
/* 113 */     addRequestHandler("GetTransactions", GetTransactionsHandler.class);
/* 114 */     addRequestHandler("SinkData", SinkDataHandler.class);
/* 115 */     addRequestHandler("UpdateProfile", UpdateProfileHandler.class);
/* 116 */     addRequestHandler("ChangeDealer", ChangeDealerHandler.class);
/* 117 */     addRequestHandler("GetUserLastSession", GetUserLastSessionHandler.class);
/* 118 */     addRequestHandler("GetUserSession", GetPlayerSession.class);
/* 119 */     addRequestHandler("DealerTip", DealerTipHandler.class);
/* 120 */     addRequestHandler("GetDealers", GetDealerHandler.class);
/* 121 */     addRequestHandler("ChatMessage", MessageHandler.class);
/* 122 */     addRequestHandler("GetGifts", GetGiftsHandler.class);
/* 123 */     addRequestHandler("GetUserGifts", GetUserGiftsHadler.class);
/* 124 */     addRequestHandler("SendGift", SendGiftHandler.class);
/* 125 */     addRequestHandler("SetAutoPlay", SetAutoPlayHandler.class);
/* 126 */     addRequestHandler("SitOut", SitOutHandler.class);
/* 127 */     addRequestHandler("CheckDeviceId", CheckDeviceIdHandler.class);
              addRequestHandler("UserRegistration", UserRegistrationEventHandler.class);
/*     */   }
/*     */   
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 139 */     super.destroy();
              removeEventHandler(SFSEventType.SERVER_READY);
              removeEventHandler(SFSEventType.USER_LOGIN);
/* 169 */     removeEventHandler(SFSEventType.USER_JOIN_ROOM);
/* 170 */     removeEventHandler(SFSEventType.USER_DISCONNECT);
/* 171 */     removeEventHandler(SFSEventType.USER_LOGOUT);

/* 140 */     removeRequestHandler("GetLobby");
/* 141 */     removeRequestHandler("GetLobbyRoom");
/* 142 */     removeRequestHandler("JoinUser");
/* 143 */     removeRequestHandler("JoinTable");
/* 144 */     removeRequestHandler("LeaveTable");
/* 145 */     removeRequestHandler("Action");
/* 146 */     removeRequestHandler("GetPlayerProfile");
/* 147 */     removeRequestHandler("UserPingBack");
/* 148 */     removeRequestHandler("CreatePrivateTable");
/* 149 */     removeRequestHandler("GetCurrentRunningGames");
/* 150 */     removeRequestHandler("ReedemChips");
/* 151 */     removeRequestHandler("GetTransactions");
/* 152 */     removeRequestHandler("SinkData");
/* 153 */     removeRequestHandler("UpdateProfile");
/* 154 */     removeRequestHandler("ChangeDealer");
/* 155 */     removeRequestHandler("GetUserLastSession");
/* 156 */     removeRequestHandler("DealerTip");
/* 157 */     removeRequestHandler("GetDealers");
/* 158 */     removeRequestHandler("GetUserSession");
/* 159 */     removeRequestHandler("ChatMessage");
/* 160 */     removeRequestHandler("GetGifts");
/* 161 */     removeRequestHandler("GetUserGifts");
/* 162 */     removeRequestHandler("SendGift");
/* 163 */     removeRequestHandler("SetAutoPlay");
/* 164 */     removeRequestHandler("SitOut");
/* 165 */     removeRequestHandler("CheckDeviceId");
              removeRequestHandler("UserRegistration");
/*     */   }
/*     */   
/*     */   public ConcurrentHashMap<String, GameBean> getGames() {
/* 175 */     return this.games;
/*     */   }
/*     */   
/*     */   public void makeLobbyRoom(String roomname) throws SFSCreateRoomException
/*     */   {
/* 180 */     CreateRoomSettings settings = new CreateRoomSettings();
/* 181 */     settings.setName(roomname);
/* 182 */     settings.setMaxUsers(100);
/* 183 */     settings.setHidden(false);
/* 184 */     settings.setGroupId("LobbyGroup");
/* 185 */     getApi().createRoom(getParentZone(), settings, null);
/*     */   }
/*     */   
/*     */   public void makeRoom(String roomname) throws SFSCreateRoomException
/*     */   {
/* 190 */     CreateRoomSettings settings = new CreateRoomSettings();
/* 191 */     settings.setName(roomname);
/* 192 */     settings.setMaxUsers(10);
/* 193 */     settings.setHidden(false);
/* 194 */     settings.setGroupId("GameGroup");
/* 195 */     settings.setAutoRemoveMode(SFSRoomRemoveMode.WHEN_EMPTY);
/* 196 */     getApi().createRoom(getParentZone(), settings, null);
/*     */   }
/*     */   
/*     */   public void logTrace(String str)
/*     */   {
/* 201 */     trace(new Object[] { "3patticlub Trace :" + str });
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/main/Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */