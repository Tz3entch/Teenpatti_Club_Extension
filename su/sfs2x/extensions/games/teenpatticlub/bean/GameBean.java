 package su.sfs2x.extensions.games.teenpatticlub.bean;

 import com.smartfoxserver.v2.api.ISFSApi;
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.Zone;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSArray;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import java.util.ArrayList;
 import java.util.Enumeration;
 import java.util.List;
 import java.util.Random;
 import java.util.concurrent.ConcurrentHashMap;
 import java.util.concurrent.TimeUnit;
 import javax.swing.Timer;

 import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.StartGameBsn;
import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
 import su.sfs2x.extensions.games.teenpatticlub.npcs.NpcLogic;
 import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.timers.GameBeanTimer;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;











 public class GameBean
 {
   private String roomId;
   private String gameID;
   private int maxNoOfPlayers = 5;
   private int tableBeanId = 0;
   private ArrayList<String> players = new ArrayList<>();
   private String dealer = null;
   private int dealerPos = 0;

   private String logsData;

   private Timer timer = null;
   private boolean isStarted = false;
   private boolean isGameGenerating = false;

   private ConcurrentHashMap<String, PlayerBean> playerBeenList = new ConcurrentHashMap<>();
   private ArrayList<String> spectatorsList = new ArrayList<>();

   private GameRoundBean gameRoundBean = null;
   private long actionStartTime = 0L;
   private int sec = 0;

   private String gameType = "public";
   private String gameStartDate = "";

   public GameBean()
   {
     for (int i = 0; i < this.maxNoOfPlayers; i++)
     {
       this.players.add("null");
     }
   }


   public ISFSObject getSFSObject()
   {
     ISFSObject sfso = new SFSObject();

     sfso.putUtfStringArray("players", this.players);
     sfso.putUtfString("roomId", getRoomId());
     sfso.putUtfString("gameId", getGameID());
     sfso.putBool("isGameStarted", this.isStarted);
     sfso.putInt("remainingSeconds", getRemainingSeconds());
     sfso.putUtfString("gameType", this.gameType);

     TableBean tb = null;
     if (this.gameType.equals("Public")) {
       tb = Appmethods.getTableBean(this.tableBeanId);
     } else {
       tb = Appmethods.getPrivateTableBean(this.roomId);
     }
     sfso.putInt("boot", tb.get_boot().intValue());
     sfso.putInt("challLimit", tb.get_challLimit().intValue());
     sfso.putInt("potLimit", tb.get_potLimit().intValue());
     sfso.putInt("id", tb.get_id().intValue());
     sfso.putBool("isGameGenerating", isGameGenerating());
     sfso.putSFSArray("playerBeans", getPlayerBeansSFSArray());


     return sfso;
   }

   public ISFSObject getSinkDataSFSObject(ISFSObject sfso)
   {
     sfso.putBool("isGameGenerating", this.isGameGenerating);

     return sfso;
   }

   public SFSArray getPlayerBeansSFSArray()
   {
     SFSArray playerBeans = new SFSArray();

     for (Enumeration<PlayerBean> e = getPlayerBeenList().elements(); e.hasMoreElements();)
     {
       PlayerBean bean = (PlayerBean)e.nextElement();
       playerBeans.addSFSObject(bean.getSFSObject());
     }
     return playerBeans;
   }

   public String getGameStartDate()
   {
     return this.gameStartDate;
   }

   public void setGameStartDate(String gameStartDate) { this.gameStartDate = gameStartDate; }

   public boolean isGameGenerating() {
     return this.isGameGenerating;
   }

/* 126 */   public void setGameGenerating(boolean isGameGenerating) { this.isGameGenerating = isGameGenerating; }

   public GameRoundBean getGameRoundBean() {
     return this.gameRoundBean;
   }

   public void setGameRoundBean(GameRoundBean gameRoundBean) { this.gameRoundBean = gameRoundBean; }

   public ConcurrentHashMap<String, PlayerBean> getPlayerBeenList() {
     return this.playerBeenList;
   }

   public void setPlayerBeenList(ConcurrentHashMap<String, PlayerBean> playerBeenList) {
     this.playerBeenList = playerBeenList;
   }

   public ArrayList<String> getSpectatorsList() { return this.spectatorsList; }

   public void setSpectatorsList(ArrayList<String> spectatorsList)
   {
     this.spectatorsList = spectatorsList;
   }

   public int getTableBeanId() {
     return this.tableBeanId;
   }

   public void setTableBeanId(int tableBeanId) {
     this.tableBeanId = tableBeanId;
   }

   public String getRoomId() {
     return this.roomId;
   }

   public void setRoomId(String roomId) {
     this.roomId = roomId;
   }

   public String getGameID() {
     return this.gameID;
   }

   public void setGameID(String gameID) {
     this.gameID = gameID;
   }

   public String getGameType() {
     return this.gameType;
   }

   public void setGameType(String gameType) {
     this.gameType = gameType;
   }

   public int getMaxNoOfPlayers() {
     return this.maxNoOfPlayers;
   }

   public void setMaxNoOfPlayers(int maxNoOfPlayers) {
     this.maxNoOfPlayers = maxNoOfPlayers;
   }

   public ArrayList<String> getPlayers() {
     return this.players;
   }

   public void setPlayers(ArrayList<String> players) {
     this.players = players;
   }

   public String get_dealer() {
     return this.dealer;
   }

   public void set_dealer(String dealer) {
     this.dealer = dealer;
   }

   public String getLogsData() {
     return this.logsData;
   }

   public void setLogsData(String logsData) {
     this.logsData = logsData;
   }

   public Timer getTimer() {
     return this.timer;
   }

   public void setTimer(Timer timer) {
     this.timer = timer;
   }

   public boolean isStarted() {
     return this.isStarted;
   }

   public void setStarted(boolean isStarted) {
     this.isStarted = isStarted;
   }





   public void addPlayer(int pos, String name)
   {
     Appmethods.showLog("Before Add " + this.players.toString());
     this.players.remove(pos);
     this.players.add(pos, name);
     Appmethods.showLog("After Add " + this.players.toString());
     this.spectatorsList.remove(name);
   }




   public void removePlayer(String name)
   {
     int pos = -1;
     pos = this.players.indexOf(name);
     if (pos != -1)
     {
       Appmethods.showLog("Before Remove " + this.players.toString() + "Pos " + pos);
       this.players.remove(pos);
       this.players.add(pos, "null");
       Appmethods.showLog("After Remove " + this.players.toString());
     }

     this.playerBeenList.remove(name);
     this.spectatorsList.remove(name);
   }

   public void removePlayerAndAddToSpectator(String name) {
     int pos = -1;
     pos = this.players.indexOf(name);
     if (pos != -1)
     {
       Appmethods.showLog("Before Remove GameBean" + this.players.toString());
       this.players.remove(pos);
       this.players.add(pos, "null");
       Appmethods.showLog("After Remove GameBean" + this.players.toString());
     }


     getSpectatorsList().add(name);
   }

   public void removePlayerBean(String player)
   {
     this.playerBeenList.remove(player);
   }

   public int getJoinedPlayers() {
     int joinedPlayers = 0;
     for (int i = 0; i < this.maxNoOfPlayers; i++)
     {
       if (!((String)this.players.get(i)).equals("null"))
       {
         joinedPlayers++;
       }
     }
     return joinedPlayers;
   }

   private void setDealer()
   {
     if (this.dealer == null)
     {
       for (int i = 0; i < this.players.size(); i++)
       {
         if (!((String)this.players.get(i)).equals("null"))
         {
           this.dealer = ((String)this.players.get(i));
           this.dealerPos = i;
         }

       }

     } else {
       for (int i = 1; i < this.players.size(); i++)
       {
         int no = this.dealerPos + i;
         if (no >= this.players.size())
           no %= this.players.size();
         if (!((String)this.players.get(no)).equals("null"))
         {
           this.dealer = ((String)this.players.get(no));
           this.dealerPos = no;
           break;
         }
       }
     }
   }




   private void removeDisconnectedUsers()
   {
     for (Enumeration<PlayerBean> e = this.playerBeenList.elements(); e.hasMoreElements();)
     {
       PlayerBean pBean = (PlayerBean)e.nextElement();
       if (!pBean.isActive())
       {

         Commands.appInstance.proxy.insertUserLastSession(pBean, 1);
         Room room = Commands.appInstance.getParentZone().getRoomByName(this.roomId);

         int pos = this.players.indexOf(pBean.getPlayerId());
         Appmethods.showLog("Before Remove isActivePlayers " + this.players.toString() + "Pos " + pos);
         this.players.remove(pos);
         this.players.add(pos, "null");
         Appmethods.showLog("After Remove isActivePlayers " + this.players.toString());

         ISFSObject sfso = new SFSObject();
         sfso = getSFSObject();
         sfso.putUtfString("player", pBean.getPlayerId());
         sfso.putBool("isSpectator", false);

         this.playerBeenList.remove(pBean.getPlayerId());
         Commands.appInstance.send("LeaveTable", sfso, room.getUserList());

         Appmethods.showLog("Disconnected User");
       }
     }
   }


   private void removeInsufficientAmountUsers()
   {
     for (Enumeration<PlayerBean> e = this.playerBeenList.elements(); e.hasMoreElements();)
     {
       PlayerBean pBean = (PlayerBean)e.nextElement();

       TableBean tb;
       if (this.gameType.equals("Public")) {
         tb = Appmethods.getTableBean(this.tableBeanId);
       } else {
         tb = Appmethods.getPrivateTableBean(this.roomId);
       }
       if (pBean.getInplay() < tb.get_challLimit().intValue())
       {

         Commands.appInstance.proxy.insertUserLastSession(pBean, 0);


         User player = Commands.appInstance.getApi().getUserByName(pBean.getPlayerId());
         Room room = Commands.appInstance.getParentZone().getRoomByName(this.roomId);

         int pos = this.players.indexOf(pBean.getPlayerId());
         Appmethods.showLog("Before Remove isActivePlayers " + this.players.toString() + "Pos " + pos);
         this.players.remove(pos);
         this.players.add(pos, "null");
         Appmethods.showLog("After Remove isActivePlayers " + this.players.toString());
         this.playerBeenList.remove(pBean.getPlayerId());

         ISFSObject sfso = new SFSObject();
         sfso = getSFSObject();
         sfso.putUtfString("player", pBean.getPlayerId());
         sfso.putBool("isSpectator", false);
         Commands.appInstance.send("InsufficientChips", sfso, room.getUserList());


         Commands.appInstance.send("GetUserLastSession", Commands.appInstance.proxy.getUserLastSessionInfo(player.getName(), 0), player);

         Appmethods.showLog("INSUFFICIENT_CHIPS");

         Appmethods.leaveRoom(player, room);
       }

     }
   }

   private void removeTimeUpUsers() {
     for (Enumeration<PlayerBean> e = this.playerBeenList.elements(); e.hasMoreElements();)
     {
       PlayerBean pBean = (PlayerBean)e.nextElement();

       Appmethods.showLog("pBean.getTimeUpCount() " + pBean.getTimeUpCount() + " pBean.isAutoPlay() " + pBean.isAutoPlay());


       if ((pBean.getTimeUpCount() == 2) && (!pBean.isAutoPlay()))
       {
         Commands.appInstance.proxy.insertUserLastSession(pBean, 0);

         User player = Commands.appInstance.getApi().getUserByName(pBean.getPlayerId());
         Room room = Commands.appInstance.getParentZone().getRoomByName(this.roomId);

         int pos = this.players.indexOf(pBean.getPlayerId());
         Appmethods.showLog("Before Remove isActivePlayers " + this.players.toString() + "Pos " + pos);
         this.players.remove(pos);
         this.players.add(pos, "null");
         Appmethods.showLog("After Remove isActivePlayers " + this.players.toString());
         this.playerBeenList.remove(pBean.getPlayerId());

         ISFSObject sfso = new SFSObject();
         sfso = getSFSObject();
         sfso.putUtfString("player", pBean.getPlayerId());
         sfso.putBool("isSpectator", false);
         Commands.appInstance.send("TwoTimesTimeUp", sfso, room.getUserList());

         Appmethods.showLog("TWO_TIMES_TIMEUP");


         Commands.appInstance.send("GetUserLastSession", Commands.appInstance.proxy.getUserLastSessionInfo(player.getName(), 0), player);

         Appmethods.leaveRoom(player, room);
       }
     }
   }

   public void startGame() {
     Appmethods.showLog("GameBean : startGame");
     this.isStarted = true;
     this.isGameGenerating = false;


     removeDisconnectedUsers();
     removeInsufficientAmountUsers();
       removeTimeUpUsers(); ///////////////////////////////////////////////////////////////////////////////////////////////////////

     if (getJoinedPlayers() >= 2)
     {



       String currDate = Appmethods.getDateTime();
       setGameStartDate(currDate);


       setDealer();

       TableBean tb;
       if (this.gameType.equals("Public")) {
         tb = Appmethods.getTableBean(this.tableBeanId);
       } else {
         tb = Appmethods.getPrivateTableBean(this.roomId);
       }
       this.gameRoundBean = new GameRoundBean(this.dealer, this.players, tb.get_boot().intValue(), tb.get_challLimit().intValue(), tb.get_potLimit().intValue(), this.roomId);
       this.gameRoundBean.gameInit();



       StartGameBsn sgBsn = new StartGameBsn();
       sgBsn.startGame(this);
       sgBsn = null;


         NpcLogic npcl = new NpcLogic(this);
         npcl.performNpcTurn();

     }
     else
     {
       this.dealer = null;
       this.isStarted = false;

       if (getJoinedPlayers() == 0)
       {
         Room room = Commands.appInstance.getParentZone().getRoomByName(this.roomId);

         if (getGameType().equals("Private"))
         {
           TableBean tBean = Appmethods.getPrivateTableBean(getRoomId());

           UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
           ulBsn.updatePrivateTableLobby("Delete", tBean);
           ulBsn = null;

           Commands.appInstance.proxy.updatePrivateTable(tBean.getPrivateTableId().intValue());

           Commands.appInstance.privateTables.remove(tBean);
         }
         else if (this.gameType.equals("Public"))
         {
           Appmethods.updateDynamicRoom(this);
         }


         if (getPlayerBeenList().size() == 0) {  //////////////////////////// changed from get UserList.size //getPlayers().size()
             System.out.println("================CLOSING GAME FROM GAMEBEANJAVA 505====================");
           Appmethods.removeGameBean(this);
         }
         else {

           startTimer(5, "CloseGame");
         }

       } else {
           int n = 0;
           Room room = Appmethods.getRoomByName(getRoomId());
           List<User> players = room.getPlayersList();
           if (players.size() > 0) {
               for (User player : players) {
                   if (player.isNpc()) {
                       n++;
                   }
               }
           }
           if (n == 0) {
               System.out.println("================CLOSING GAME FROM GAMEBEANJAVA 524====================");
               startTimer(60, "CloseGame");
           }
       }
     }

     if (getGameType().equals("Public"))
     {

       Appmethods.updateDynamicRoom(this);

     }
     else
     {
       TableBean tBean = Appmethods.getPrivateTableBean(getRoomId());
       UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
       ulBsn.updatePrivateTableLobby("Update", tBean);
       ulBsn = null;
     }
   }

   public int getRemainingSeconds()
   {
     int secondsRemain = 0;
     long now = System.currentTimeMillis();
     long elapsed = now - this.actionStartTime;
     long remainingTime = this.sec * 1000 - elapsed;
     secondsRemain = (int)(remainingTime / 1000L);
     if (secondsRemain <= 0)
       secondsRemain = 0;
     return secondsRemain;
   }

   public void startTimer(int seconds, String command) {
     try {
       stopTimer(); } catch (Exception localException) {}
     this.timer = new Timer(seconds * 1000, new GameBeanTimer(this.roomId, command));
     this.timer.start();
     this.sec = seconds;
     this.actionStartTime = System.currentTimeMillis();
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


