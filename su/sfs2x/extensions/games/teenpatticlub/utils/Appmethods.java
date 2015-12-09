 package su.sfs2x.extensions.games.teenpatticlub.utils;

 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
 import java.text.DateFormat;
 import java.text.SimpleDateFormat;
 import java.util.*;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.npcs.NPCManager;

 public class Appmethods
 {
   private static int roomId = 1000;
   
   public static Room createRoom() throws SFSCreateRoomException {
     if (roomId >= 10000)
       roomId = 1000;
     String roomname = "TP" + roomId;
     roomId += 1;
     Commands.appInstance.makeRoom(roomname);
     Room room = Commands.appInstance.getParentZone().getRoomByName(roomname);
     return room;
   }
   
   public static Room createPrivateRoom(String roomId) throws SFSCreateRoomException
   {
     Commands.appInstance.makeRoom(roomId);
     Room room = Commands.appInstance.getParentZone().getRoomByName(roomId);
     return room;
   }
   
   public static String generateGameID() {
     DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSS");
     Calendar cal = Calendar.getInstance();
     return "TP_" + dateFormat.format(cal.getTime());
   }
   
 
   public static void showLog(String str)
   {
     Commands.appInstance.logTrace(str);
   }
   
 
   public static void showSQLLog(String str)
   {
     Commands.appInstance.logTrace(str);
   }
   
   public static String getDateTime()
   {
     String currDate = null;
     Calendar currentTime = Calendar.getInstance();
     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
     currDate = dateFormat.format(currentTime.getTime());
     return currDate;
   }
   
   public static Room getRoomByName(String roomName)
   {
     Room room = null;
     room = Commands.appInstance.getParentZone().getRoomByName(roomName);
     showLog("tableId : " + roomName + "   Room : " + room);
     return room;
   }
   
   public static GameBean getGameBean(String roomId) {
     GameBean gameBean = null;
     gameBean = (GameBean)Commands.appInstance.getGames().get(roomId);
     return gameBean;
   }
   
   public static TableBean getTableBean(int tableId)
   {
     TableBean tableBean = null;
     
     for (TableBean bean : Commands.appInstance.publicTables)
     {
       if (tableId == bean.get_id())
       {
         tableBean = bean;
         break;
       }
     }
     return tableBean;
   }
   
   public static TableBean getPrivateTableBean(String roomId)
   {
     TableBean tableBean = null;
     for (TableBean bean : Commands.appInstance.privateTables)
     {
       if (roomId.equals(bean.get_roomId()))
       {
         tableBean = bean;
         break;
       }
     }
     return tableBean;
   }
   
   public static float setFloatFormat(float value)
   {
     float result = 0.0F;
     String str = String.format(Locale.ENGLISH, "%2f", value);
     result = Float.parseFloat(str);
     
     return result;
   }
   
 
 
 
 
 
 
 
 
   public static ISFSObject getSinkDataSFSObject(GameBean gameBean)
   {
     ISFSObject sfso = new SFSObject();
     sfso = gameBean.getSFSObject();
     sfso = gameBean.getSinkDataSFSObject(sfso);
     sfso = gameBean.getGameRoundBean().getSFSObject(sfso);
     return sfso;
   }
   
   public static void joinRoom(User user, Room room)
   {
     try {
       Commands.appInstance.getApi().joinRoom(user, room, null, false, null);
     }
     catch (Exception e)
     {
       e.printStackTrace();
     }
   }
   
   public static void leaveRoom(User user, Room room)
   {
     Commands.appInstance.getApi().leaveRoom(user, room);
     showLog("User : " + user.getName() + " Leave the Room : " + room.getName());
   }
   
   public static void updateDynamicRoom(GameBean gameBean)
   {
     TableBean tableBean = getTableBean(gameBean.getTableBeanId());
     boolean bool = false;
     showLog("updateDynamicRoom " + gameBean.getTableBeanId());

    Room room = Appmethods.getRoomByName(gameBean.getRoomId());///////////////////
     if ((gameBean.getJoinedPlayers() == gameBean.getMaxNoOfPlayers() && NPCManager.npcsInRoom(room).size()==0) || (gameBean.getJoinedPlayers() == 0))
     {
       showLog("updateDynamicRoom 2");
       
 
       GameBean bean = new GameBean();
       for (Enumeration<GameBean> e = Commands.appInstance.getGames().elements(); e.hasMoreElements();)
       {
         bean = (GameBean)e.nextElement();
         
 
 
 
 
 
         if ((tableBean.get_id() == bean.getTableBeanId()) &&
           (!bean.getRoomId().equals(gameBean.getRoomId()))  &&
           (bean.getJoinedPlayers() != bean.getMaxNoOfPlayers()))
         {
           bool = true;
           break;
         }
       }
       
 
 
       if (bool)
       {
         tableBean.set_roomId(bean.getRoomId());
       }
       else
       {
         tableBean.set_roomId("null");
       }
     }
     else
     {
       showLog("updateDynamicRoom 1");
       showLog("TBean roomid Before " + tableBean.get_roomId());
       GameBean bean = new GameBean();
       for (Enumeration<GameBean> e = Commands.appInstance.getGames().elements(); e.hasMoreElements();)
       {
         bean = (GameBean)e.nextElement();
         showLog("Gamebean " + bean.getRoomId());
         if (bean.getTableBeanId() == tableBean.get_id())
         {

           if ((bean.getJoinedPlayers() != bean.getMaxNoOfPlayers()) && (bean.getJoinedPlayers() != 0))
           {
 
             tableBean.set_roomId(bean.getRoomId());
       //      break; ///////////////////////////////////////////////////////////////////// I comment it to set LAST room visible.
           }
         }
       }
       showLog("TBean roomid after " + tableBean.get_roomId());
     }
     
 
 
     Collection<User> users = Commands.appInstance.getParentZone().getUserList();
     List<User> listusers = new ArrayList();
     Object[] arr = users.toArray();
     for (int i = 0; i < arr.length; i++)
     {
       User no = (User)arr[i];
       listusers.add(no);
     }
     
     Commands.appInstance.send("UpdateLobby", tableBean.getSFSObject(), listusers);
   }
   
 
 
 
 
 
   public static void leaveGameRoomAndJoinLobby1(User sender, Room room) {}
   
 
 
 
 
 
   public static void removeRoom(Room room)
   {
     String roomName = room.getName();
     showLog(" removeRoom roomName : " + roomName);
     showLog(" Before room users : " + room.getUserList());
     for (User user : room.getUserList())
     {
 
       leaveRoom(user, room);
     }
     showLog(" After room users : " + room.getUserList());
     Commands.appInstance.getApi().removeRoom(room);
     
     showLog(" room removed : " + roomName);
     roomName = null;
   }
   
 
   public static void removeGameBean(GameBean gameBean)
   {
     Room room = getRoomByName(gameBean.getRoomId());
     Commands.appInstance.getApi().removeRoom(room);
     
     gameBean.stopTimer();
     
     if (gameBean.getGameType().equals("Public"))
     {
       TableBean tBean = getTableBean(gameBean.getTableBeanId());
       if (!tBean.get_roomId().equals("null"))
         tBean.set_roomId("null");
     }
     Commands.appInstance.getGames().remove(gameBean.getRoomId());
     showLog("GameBean removed from hashmap");
   }
   
   public static void cutHandAmount(String roomId, PlayerRoundBean prBean, Integer amount)
   {
     GameBean gameBean = getGameBean(roomId);
     PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(prBean.getPlayerId());
     pBean.setInplay(pBean.getInplay() - amount.intValue());
     prBean.getHandAmounts().add(amount);
     prBean.setTotalBetAmount(prBean.getTotalBetAmount() + amount.intValue());
   }
   
   public static int getRandomNumber()
   {
     Random randomGenerator = new Random();
     int randomInt = randomGenerator.nextInt(9);
     return randomInt;
   }
   
   public static String getRandomNumberString()
   {
     String str = "";
     for (int i = 0; i < 5; i++)
     {
       str = str + getRandomNumber();
     }
     return str;
   }
   
   public static void updateGameBeanUpdateLobby(GameBean gameBean, Room room)
   {
     if (gameBean.getJoinedPlayers() == 0)
     {
 
       if (room.getUserList().size() == 0)
       {
                  System.out.println("================CLOSING GAME FROM APPMETHODS 328====================");
         removeGameBean(gameBean);
         if (gameBean.getGameType().equals("Private"))
         {
 
           TableBean tBean = getPrivateTableBean(gameBean.getRoomId());
           
           UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
           ulBsn.updatePrivateTableLobby("Delete", tBean);
           ulBsn = null;
           
 
           Commands.appInstance.proxy.updatePrivateTable(tBean.getPrivateTableId().intValue());
           
           Commands.appInstance.privateTables.remove(tBean);
         }
         
 
       }
       else if (gameBean.getGameType().equals("PRIVATE"))
       {
 
         TableBean tBean = getPrivateTableBean(gameBean.getRoomId());
         UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
         ulBsn.updatePrivateTableLobby("Update", tBean);
         ulBsn = null;
       }
       
     }
     else if (gameBean.getJoinedPlayers() == 1)
     { int n = 0;   ///////////////////////////////this fixes remove 1 npc in room problem
        room = Appmethods.getRoomByName(gameBean.getRoomId());
        List<User> players = room.getPlayersList();
        if (players.size() > 0) {
            for (User player : players) {
                if (player.isNpc()) {
                    n++;
                }
            }
        }

      if (n == 0)  {
       gameBean.setGameGenerating(false);
       gameBean.stopTimer();
       gameBean.setStarted(false);
       
 
       gameBean.startTimer(60, "CloseGame");
       
       if (gameBean.getGameType().equals("Private"))
       {
 
         TableBean tBean = getPrivateTableBean(gameBean.getRoomId());
         UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
         ulBsn.updatePrivateTableLobby("Update", tBean);
         ulBsn = null;
       }
      }
 
     }
     else if (gameBean.getGameType().equals("Private"))
     {
 
       TableBean tBean = getPrivateTableBean(gameBean.getRoomId());
       UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
       ulBsn.updatePrivateTableLobby("Update", tBean);
       ulBsn = null;
     }
     
 
     if (gameBean.getGameType().equals("Public"))
     {
 
       updateDynamicRoom(gameBean);
     }
   }
   
   public static boolean isUserExist(ArrayList<String> names, String player) {
     for (int i = 0; i < names.size(); i++)
     {
       if (((String)names.get(i)).equals(player))
         return true;
     }
     return false;
   }
 }


