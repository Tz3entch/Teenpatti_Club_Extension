/*     */ package su.sfs2x.extensions.games.teenpatticlub.utils;
/*     */ 
/*     */ import com.smartfoxserver.v2.api.ISFSApi;
/*     */ import com.smartfoxserver.v2.entities.Room;
/*     */ import com.smartfoxserver.v2.entities.User;
/*     */ import com.smartfoxserver.v2.entities.Zone;
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*     */ import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.*;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.npcs.NPCManager;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Appmethods
/*     */ {
/*  36 */   private static int roomId = 1000;
/*     */   
/*     */   public static Room createRoom() throws SFSCreateRoomException {
/*  39 */     if (roomId >= 10000)
/*  40 */       roomId = 1000;
/*  41 */     String roomname = "TP" + roomId;
/*  42 */     roomId += 1;
/*  43 */     Commands.appInstance.makeRoom(roomname);
/*  44 */     Room room = Commands.appInstance.getParentZone().getRoomByName(roomname);
/*  45 */     return room;
/*     */   }
/*     */   
/*     */   public static Room createPrivateRoom(String roomId) throws SFSCreateRoomException
/*     */   {
/*  50 */     Commands.appInstance.makeRoom(roomId);
/*  51 */     Room room = Commands.appInstance.getParentZone().getRoomByName(roomId);
/*  52 */     return room;
/*     */   }
/*     */   
/*     */   public static String generateGameID() {
/*  56 */     DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
/*  57 */     Calendar cal = Calendar.getInstance();
/*  58 */     return "TP_" + dateFormat.format(cal.getTime());
/*     */   }
/*     */   
/*     */ 
/*     */   public static void showLog(String str)
/*     */   {
/*  64 */     Commands.appInstance.logTrace(str);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void showSQLLog(String str)
/*     */   {
/*  70 */     Commands.appInstance.logTrace(str);
/*     */   }
/*     */   
/*     */   public static String getDateTime()
/*     */   {
/*  75 */     String currDate = null;
/*  76 */     Calendar currentTime = Calendar.getInstance();
/*  77 */     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*  78 */     dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
/*  79 */     currDate = dateFormat.format(currentTime.getTime());
/*  80 */     return currDate;
/*     */   }
/*     */   
/*     */   public static Room getRoomByName(String roomName)
/*     */   {
/*  85 */     Room room = null;
/*  86 */     room = Commands.appInstance.getParentZone().getRoomByName(roomName);
/*  87 */     showLog("tableId : " + roomName + "   Room : " + room);
/*  88 */     return room;
/*     */   }
/*     */   
/*     */   public static GameBean getGameBean(String roomId) {
/*  92 */     GameBean gameBean = null;
/*  93 */     gameBean = (GameBean)Commands.appInstance.getGames().get(roomId);
/*  94 */     return gameBean;
/*     */   }
/*     */   
/*     */   public static TableBean getTableBean(int tableId)
/*     */   {
/*  99 */     TableBean tableBean = null;
/*     */     
/* 101 */     for (TableBean bean : Commands.appInstance.publicTables)
/*     */     {
/* 103 */       if (tableId == bean.get_id())
/*     */       {
/* 105 */         tableBean = bean;
/* 106 */         break;
/*     */       }
/*     */     }
/* 109 */     return tableBean;
/*     */   }
/*     */   
/*     */   public static TableBean getPrivateTableBean(String roomId)
/*     */   {
/* 114 */     TableBean tableBean = null;
/* 115 */     for (TableBean bean : Commands.appInstance.privateTables)
/*     */     {
/* 117 */       if (roomId.equals(bean.get_roomId()))
/*     */       {
/* 119 */         tableBean = bean;
/* 120 */         break;
/*     */       }
/*     */     }
/* 123 */     return tableBean;
/*     */   }
/*     */   
/*     */   public static float setFloatFormat(float value)
/*     */   {
/* 128 */     float result = 0.0F;
/* 129 */     String str = String.format(Locale.ENGLISH, "%2f", value);
/* 130 */     result = Float.parseFloat(str);
/*     */     
/* 132 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ISFSObject getSinkDataSFSObject(GameBean gameBean)
/*     */   {
/* 145 */     ISFSObject sfso = new SFSObject();
/* 146 */     sfso = gameBean.getSFSObject();
/* 147 */     sfso = gameBean.getSinkDataSFSObject(sfso);
/* 148 */     sfso = gameBean.getGameRoundBean().getSFSObject(sfso);
/* 149 */     return sfso;
/*     */   }
/*     */   
/*     */   public static void joinRoom(User user, Room room)
/*     */   {
/*     */     try {
/* 155 */       Commands.appInstance.getApi().joinRoom(user, room, null, false, null);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 159 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void leaveRoom(User user, Room room)
/*     */   {
/* 165 */     Commands.appInstance.getApi().leaveRoom(user, room);
/* 166 */     showLog("User : " + user.getName() + " Leave the Room : " + room.getName());
/*     */   }
/*     */   
/*     */   public static void updateDynamicRoom(GameBean gameBean)
/*     */   {
/* 171 */     TableBean tableBean = getTableBean(gameBean.getTableBeanId());
/* 172 */     boolean bool = false;
/* 173 */     showLog("updateDynamicRoom " + gameBean.getTableBeanId());
/*     */     
/* 175 */     if ((gameBean.getJoinedPlayers() == gameBean.getMaxNoOfPlayers()) || (gameBean.getJoinedPlayers() == 0))
/*     */     {
/* 177 */       showLog("updateDynamicRoom 2");
/*     */       
/*     */ 
/* 180 */       GameBean bean = new GameBean();
/* 181 */       for (Enumeration<GameBean> e = Commands.appInstance.getGames().elements(); e.hasMoreElements();)
/*     */       {
/* 183 */         bean = (GameBean)e.nextElement();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */         if ((tableBean.get_id() == bean.getTableBeanId()) &&
/* 191 */           (!bean.getRoomId().equals(gameBean.getRoomId()))  &&
/* 192 */           (bean.getJoinedPlayers() != bean.getMaxNoOfPlayers()))
/*     */         {
/* 194 */           bool = true;
/* 195 */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 201 */       if (bool)
/*     */       {
/* 203 */         tableBean.set_roomId(bean.getRoomId());
/*     */       }
/*     */       else
/*     */       {
/* 207 */         tableBean.set_roomId("null");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 212 */       showLog("updateDynamicRoom 1");
/* 213 */       showLog("TBean roomid Before " + tableBean.get_roomId());
/* 214 */       GameBean bean = new GameBean();
/* 215 */       for (Enumeration<GameBean> e = Commands.appInstance.getGames().elements(); e.hasMoreElements();)
/*     */       {
/* 217 */         bean = (GameBean)e.nextElement();
/* 218 */         showLog("Gamebean " + bean.getRoomId());
/* 219 */         if (bean.getTableBeanId() == tableBean.get_id())
/*     */         {
                Room room = Appmethods.getRoomByName(bean.getRoomId());///////////////////
/* 221 */           if ((bean.getJoinedPlayers() != bean.getMaxNoOfPlayers()) && (bean.getJoinedPlayers() != 0 && NPCManager.npcsInRoom(room).size()>0))
/*     */           {
/*     */ 
/* 224 */             tableBean.set_roomId(bean.getRoomId());
/* 225 */     //       break; ///////////////////////////////////////////////////////////////////// I comment it to set LAST room visible.
/*     */           }
/*     */         }
/*     */       }
/* 229 */       showLog("TBean roomid after " + tableBean.get_roomId());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 234 */     Collection<User> users = Commands.appInstance.getParentZone().getUserList();
/* 235 */     List<User> listusers = new ArrayList();
/* 236 */     Object[] arr = users.toArray();
/* 237 */     for (int i = 0; i < arr.length; i++)
/*     */     {
/* 239 */       User no = (User)arr[i];
/* 240 */       listusers.add(no);
/*     */     }
/*     */     
/* 243 */     Commands.appInstance.send("UpdateLobby", tableBean.getSFSObject(), listusers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void leaveGameRoomAndJoinLobby1(User sender, Room room) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void removeRoom(Room room)
/*     */   {
/* 260 */     String roomName = room.getName();
/* 261 */     showLog(" removeRoom roomName : " + roomName);
/* 262 */     showLog(" Before room users : " + room.getUserList());
/* 263 */     for (User user : room.getUserList())
/*     */     {
/*     */ 
/* 266 */       leaveRoom(user, room);
/*     */     }
/* 268 */     showLog(" After room users : " + room.getUserList());
/* 269 */     Commands.appInstance.getApi().removeRoom(room);
/*     */     
/* 271 */     showLog(" room removed : " + roomName);
/* 272 */     roomName = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void removeGameBean(GameBean gameBean)
/*     */   {
/* 278 */     Room room = getRoomByName(gameBean.getRoomId());
/* 279 */     Commands.appInstance.getApi().removeRoom(room);
/*     */     
/* 281 */     gameBean.stopTimer();
/*     */     
/* 283 */     if (gameBean.getGameType().equals("Public"))
/*     */     {
/* 285 */       TableBean tBean = getTableBean(gameBean.getTableBeanId());
/* 286 */       if (!tBean.get_roomId().equals("null"))
/* 287 */         tBean.set_roomId("null");
/*     */     }
/* 289 */     Commands.appInstance.getGames().remove(gameBean.getRoomId());
/* 290 */     showLog("GameBean removed from hashmap");
/*     */   }
/*     */   
/*     */   public static void cutHandAmount(String roomId, PlayerRoundBean prBean, Integer amount)
/*     */   {
/* 295 */     GameBean gameBean = getGameBean(roomId);
/* 296 */     PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(prBean.getPlayerId());
/* 297 */     pBean.setInplay(pBean.getInplay() - amount.intValue());
/* 298 */     prBean.getHandAmounts().add(amount);
/* 299 */     prBean.setTotalBetAmount(prBean.getTotalBetAmount() + amount.intValue());
/*     */   }
/*     */   
/*     */   public static int getRandomNumber()
/*     */   {
/* 304 */     Random randomGenerator = new Random();
/* 305 */     int randomInt = randomGenerator.nextInt(9);
/* 306 */     return randomInt;
/*     */   }
/*     */   
/*     */   public static String getRandomNumberString()
/*     */   {
/* 311 */     String str = "";
/* 312 */     for (int i = 0; i < 5; i++)
/*     */     {
/* 314 */       str = str + getRandomNumber();
/*     */     }
/* 316 */     return str;
/*     */   }
/*     */   
/*     */   public static void updateGameBeanUpdateLobby(GameBean gameBean, Room room)
/*     */   {
/* 321 */     if (gameBean.getJoinedPlayers() == 0)
/*     */     {
/*     */ 
/* 324 */       if (room.getUserList().size() == 0)
/*     */       {
                  System.out.println("================CLOSING GAME FROM APPMETHODS 328====================");
/* 326 */         removeGameBean(gameBean);
/* 327 */         if (gameBean.getGameType().equals("Private"))
/*     */         {
/*     */ 
/* 330 */           TableBean tBean = getPrivateTableBean(gameBean.getRoomId());
/*     */           
/* 332 */           UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
/* 333 */           ulBsn.updatePrivateTableLobby("Delete", tBean);
/* 334 */           ulBsn = null;
/*     */           
/*     */ 
/* 337 */           Commands.appInstance.proxy.updatePrivateTable(tBean.getPrivateTableId().intValue());
/*     */           
/* 339 */           Commands.appInstance.privateTables.remove(tBean);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 344 */       else if (gameBean.getGameType().equals("PRIVATE"))
/*     */       {
/*     */ 
/* 347 */         TableBean tBean = getPrivateTableBean(gameBean.getRoomId());
/* 348 */         UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
/* 349 */         ulBsn.updatePrivateTableLobby("Update", tBean);
/* 350 */         ulBsn = null;
/*     */       }
/*     */       
/*     */     }
/* 354 */     else if (gameBean.getJoinedPlayers() == 1)
/*     */     { int n = 0;   ///////////////////////////////this fixes remove 1 npc in room problem
        room = Appmethods.getRoomByName(gameBean.getRoomId());
        List<User> players = room.getPlayersList();
        if (players.size() > 0) {
            for (User player : players) {
                if (player.isNpc()) {
                    n++;
                }
            }
        }

/*     */      if (n == 0)  {
/* 357 */       gameBean.setGameGenerating(false);
/* 358 */       gameBean.stopTimer();
/* 359 */       gameBean.setStarted(false);
/*     */       
/*     */ 
/* 362 */       gameBean.startTimer(60, "CloseGame");
/*     */       
/* 364 */       if (gameBean.getGameType().equals("Private"))
/*     */       {
/*     */ 
/* 367 */         TableBean tBean = getPrivateTableBean(gameBean.getRoomId());
/* 368 */         UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
/* 369 */         ulBsn.updatePrivateTableLobby("Update", tBean);
/* 370 */         ulBsn = null;
/*     */       }
/*     */      }
/*     */ 
/*     */     }
/* 375 */     else if (gameBean.getGameType().equals("Private"))
/*     */     {
/*     */ 
/* 378 */       TableBean tBean = getPrivateTableBean(gameBean.getRoomId());
/* 379 */       UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
/* 380 */       ulBsn.updatePrivateTableLobby("Update", tBean);
/* 381 */       ulBsn = null;
/*     */     }
/*     */     
/*     */ 
/* 385 */     if (gameBean.getGameType().equals("Public"))
/*     */     {
/*     */ 
/* 388 */       updateDynamicRoom(gameBean);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isUserExist(ArrayList<String> names, String player) {
/* 393 */     for (int i = 0; i < names.size(); i++)
/*     */     {
/* 395 */       if (((String)names.get(i)).equals(player))
/* 396 */         return true;
/*     */     }
/* 398 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/utils/Appmethods.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */