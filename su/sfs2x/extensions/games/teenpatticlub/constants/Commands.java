 package su.sfs2x.extensions.games.teenpatticlub.constants;
 
 import su.sfs2x.extensions.games.teenpatticlub.main.Main;

 public class Commands
 {
   public static Main appInstance;
   public static final String GET_LOBBY_ROOM = "GetLobbyRoom";
   public static final String GET_LOBBY = "GetLobby";
   public static final String UPDATE_LOBBY = "UpdateLobby";
   public static final String SINK_DATA = "SinkData";
   public static final String SEAT_OCCUPIED = "SeatOccupied";
   public static final String EXTRA_TIME = "ExtraTime";
   public static final String JOIN_TABLE = "JoinTable";
   public static final String JOIN_USER = "JoinUser";
   public static final String LEAVE_TABLE = "LeaveTable";
   public static final String START_GAME = "StartGame";
   public static final String CLOSE_GAME = "CloseGame";
   public static final String ACTION = "Action";
   public static final String SEEN = "Seen";
   public static final String BLIND = "Blind";
   public static final String CHALL = "Chall";
   public static final String PACK = "Pack";
   public static final String BOOT = "Boot";
   public static final String SIDE_SHOW_REQUEST = "SideShowRequest";
   public static final String SIDE_SHOW_RESPONSE = "SideShowResponse";
   public static final String SIDE_SHOW = "SideShow";
   public static final String SHOW = "Show";
   public static final String TURN = "Turn";
   public static final String GAME_COMPLETED = "GameCompleted";
   public static final String GET_PLAYER_PROFILE = "GetPlayerProfile";
   public static final String INSUFFICIENT_CHIPS = "InsufficientChips";
   public static final String SERVER_PING_USER = "ServerPingUser";
   public static final String USER_PING_BACK = "UserPingBack";
   public static final String QUICK_START = "QuickStart";
   public static final String NEXT_GAME = "NextGame";
   public static final String USER_PROFILE = "UserProfile";
   public static final String USER_DISCONNECTED = "UserDisconnected";
   public static final String CREATE_PRIVATE_TABLE = "CreatePrivateTable";
   public static final String UPDATE_PRIVATE_TABLE_LOBBY = "UpdatePrivateTableLobby";
   public static final String GET_CURRENT_RUNNING_GAMES = "GetCurrentRunningGames";
   public static final String REEDEM_CHIPS = "ReedemChips";
   public static final String GET_TRANSACTIONS = "GetTransactions";
   public static final String TWO_TIMES_TIMEUP = "TwoTimesTimeUp";
   public static final String UPDATE_PROFILE = "UpdateProfile";
   public static final String CHANGE_DEALER = "ChangeDealer";
   public static final String GET_USER_LAST_SESSION = "GetUserLastSession";
   public static final String GET_PLAYER_SESSION = "GetUserSession";
   public static final String DEALER_TIP = "DealerTip";
   public static final String GET_DEALERS = "GetDealers";
   public static final String CHAT_MESSAGE = "ChatMessage";
   public static final String GET_GIFTS = "GetGifts";
   public static final String GET_USER_GIFTS = "GetUserGifts";
   public static final String SET_AUTOPLAY = "SetAutoPlay";
   public static final String SEND_GIFT = "SendGift";
   public static final String SIT_OUT = "SitOut";
   public static final String CHECK_DEVICE_ID = "CheckDeviceId";
   public static final Integer TRAIL = Integer.valueOf(1);
   public static final Integer STRAIGHT_FLUSH = Integer.valueOf(2);
   public static final Integer STRAIGHT = Integer.valueOf(3);
   public static final Integer FLUSH = Integer.valueOf(4);
   public static final Integer TWO_PAIR = Integer.valueOf(5);
   public static final Integer HIGH_CARD = Integer.valueOf(6);
   public static final float rake = 5.0F;
   public static final float privateTableRake = 30.0F;
   public static final String PUBLIC = "Public";
   public static final String PRIVATE = "Private";
 }


