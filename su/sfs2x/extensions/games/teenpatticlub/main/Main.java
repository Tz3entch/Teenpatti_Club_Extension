 package su.sfs2x.extensions.games.teenpatticlub.main;

 import com.smartfoxserver.v2.SmartFoxServer;
 import com.smartfoxserver.v2.api.CreateRoomSettings;
 import com.smartfoxserver.v2.core.SFSEventType;
 import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
 import com.smartfoxserver.v2.entities.data.SFSArray;
 import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
 import com.smartfoxserver.v2.extensions.SFSExtension;
 import java.util.ArrayList;
 import java.util.concurrent.ConcurrentHashMap;
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

 public class Main extends SFSExtension
 {
   private ConcurrentHashMap<String, GameBean> games = null;
   public SmartFoxServer sfs;
   public SQLProxy proxy = null;
   public ArrayList<TableBean> publicTables = null;
   public ArrayList<TableBean> privateTables = null;
   public SFSArray gifts = null;
   public ConnectDisconnectUser cdUser;
 
   public void init()
   {
     su.sfs2x.extensions.games.teenpatticlub.constants.Commands.appInstance = this;
     
     addEventHandler(SFSEventType.SERVER_READY, ServerReadyHandler.class);
              addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
     addEventHandler(SFSEventType.USER_JOIN_ROOM, UserJoinEventHandler.class);
     addEventHandler(SFSEventType.USER_DISCONNECT, UserDisconnectedEventHandler.class);
     addEventHandler(SFSEventType.USER_LOGOUT, LogoutEventHandler.class);


     
     this.games = new ConcurrentHashMap<>();
     this.publicTables = new ArrayList<>();
     this.privateTables = new ArrayList<>();
     this.gifts = new SFSArray();
     this.sfs = SmartFoxServer.getInstance();
     this.proxy = new SQLProxy(getParentZone());
     this.proxy.getPublicTables();
     this.proxy.getPrivateTables();
     this.proxy.getGifts();
     this.proxy.serverRestartedCloseGames();
     
     this.cdUser = new ConnectDisconnectUser();
     try
     {
       makeLobbyRoom("Lobby");
     }
     catch (Exception localException) {}
     addRequestHandler("GetLobby", GetLobbyHandler.class);
     addRequestHandler("GetLobbyRoom", GetLobbyRoomHandler.class);
     addRequestHandler("JoinUser", JoinUserHadler.class);
     addRequestHandler("JoinTable", JoinTableHandler.class);
     addRequestHandler("LeaveTable", LeaveTableHandler.class);
     addRequestHandler("Action", ActionHandler.class);
     addRequestHandler("GetPlayerProfile", GetPlayerProfileHandler.class);
     addRequestHandler("UserPingBack", UserPingBackHandler.class);
     addRequestHandler("CreatePrivateTable", CreatePrivateTableHandler.class);
     addRequestHandler("GetCurrentRunningGames", GetCurrentRunningGamesHandler.class);
     addRequestHandler("ReedemChips", RedeemChipsHandler.class);
     addRequestHandler("GetTransactions", GetTransactionsHandler.class);
     addRequestHandler("SinkData", SinkDataHandler.class);
     addRequestHandler("UpdateProfile", UpdateProfileHandler.class);
     addRequestHandler("ChangeDealer", ChangeDealerHandler.class);
     addRequestHandler("GetUserLastSession", GetUserLastSessionHandler.class);
     addRequestHandler("GetUserSession", GetPlayerSession.class);
     addRequestHandler("DealerTip", DealerTipHandler.class);
     addRequestHandler("GetDealers", GetDealerHandler.class);
     addRequestHandler("ChatMessage", MessageHandler.class);
     addRequestHandler("GetGifts", GetGiftsHandler.class);
     addRequestHandler("GetUserGifts", GetUserGiftsHadler.class);
     addRequestHandler("SendGift", SendGiftHandler.class);
     addRequestHandler("SetAutoPlay", SetAutoPlayHandler.class);
     addRequestHandler("SitOut", SitOutHandler.class);
     addRequestHandler("CheckDeviceId", CheckDeviceIdHandler.class);
              addRequestHandler("UserRegistration", UserRegistrationEventHandler.class);
   }
   
 
   public void destroy()
   {
     super.destroy();
              removeEventHandler(SFSEventType.SERVER_READY);
              removeEventHandler(SFSEventType.USER_LOGIN);
     removeEventHandler(SFSEventType.USER_JOIN_ROOM);
     removeEventHandler(SFSEventType.USER_DISCONNECT);
     removeEventHandler(SFSEventType.USER_LOGOUT);

     removeRequestHandler("GetLobby");
     removeRequestHandler("GetLobbyRoom");
     removeRequestHandler("JoinUser");
     removeRequestHandler("JoinTable");
     removeRequestHandler("LeaveTable");
     removeRequestHandler("Action");
     removeRequestHandler("GetPlayerProfile");
     removeRequestHandler("UserPingBack");
     removeRequestHandler("CreatePrivateTable");
     removeRequestHandler("GetCurrentRunningGames");
     removeRequestHandler("ReedemChips");
     removeRequestHandler("GetTransactions");
     removeRequestHandler("SinkData");
     removeRequestHandler("UpdateProfile");
     removeRequestHandler("ChangeDealer");
     removeRequestHandler("GetUserLastSession");
     removeRequestHandler("DealerTip");
     removeRequestHandler("GetDealers");
     removeRequestHandler("GetUserSession");
     removeRequestHandler("ChatMessage");
     removeRequestHandler("GetGifts");
     removeRequestHandler("GetUserGifts");
     removeRequestHandler("SendGift");
     removeRequestHandler("SetAutoPlay");
     removeRequestHandler("SitOut");
     removeRequestHandler("CheckDeviceId");
              removeRequestHandler("UserRegistration");
   }
   
   public ConcurrentHashMap<String, GameBean> getGames() {
     return this.games;
   }
   
   public void makeLobbyRoom(String roomname) throws SFSCreateRoomException
   {
     CreateRoomSettings settings = new CreateRoomSettings();
     settings.setName(roomname);
     settings.setMaxUsers(100);
     settings.setHidden(false);
     settings.setGroupId("LobbyGroup");
     getApi().createRoom(getParentZone(), settings, null);
   }
   
   public void makeRoom(String roomname) throws SFSCreateRoomException
   {
     CreateRoomSettings settings = new CreateRoomSettings();
     settings.setName(roomname);
     settings.setMaxUsers(10);
     settings.setHidden(false);
     settings.setGroupId("GameGroup");
     settings.setAutoRemoveMode(SFSRoomRemoveMode.WHEN_EMPTY);
     getApi().createRoom(getParentZone(), settings, null);
   }
   
   public void logTrace(String str)
   {
     trace(new Object[] { "3patticlub Trace :" + str });
   }
 }


