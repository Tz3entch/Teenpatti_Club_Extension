package su.sfs2x.extensions.games.teenpatticlub.events;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.JoinUserBsn;
import su.sfs2x.extensions.games.teenpatticlub.bsn.LobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.handlers.JoinUserHadler;
import su.sfs2x.extensions.games.teenpatticlub.npcs.NPCManager;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import java.util.List;
import java.util.Random;

public class ServerReadyHandler extends BaseServerEventHandler{
	
    public void handleServerEvent(ISFSEvent event) throws SFSException 
    {
        if(event.getType().equals(SFSEventType.SERVER_READY))
        {
        	Appmethods.showLog("**************ServerReadyHandler*************");
        	Appmethods.showLog("3patticlub Server Started");

        	//String roomId = "Lobby";


        	
        	// Create one or more NPC users...


            //getApi().login(socketLessSession, npcUser, null, null, null);
            //Room room = Commands.appInstance.getParentZone().getRoomByName(roomId);
             LobbyBsn lobbyBsn = new LobbyBsn();
/* 27 */     String roomName = lobbyBsn.getLobbyRoomName();
/* 28 */     lobbyBsn = null;
/* 29 */     //Room lobby = Commands.appInstance.getParentZone().getRoomByName("Lobby");
/* 30 */     Room lobby1 = Appmethods.getRoomByName(roomName);

            String robotName = "OLOLO1488";
            User npcUser = getApi().createNPC(robotName, getParentExtension().getParentZone(), true);
//            ISession socketLessSession = Commands.appInstance.sfs.getSessionManager().createConnectionlessSession();
//           User npcUser = getApi().login(socketLessSession, "NPC0001", "123456", "abc", null);

            // getApi().joinRoom(npcUser, lobby1);
//            ISFSObject obj = SFSObject.newInstance();
//            obj.putUtfString("type", "Public");
//            obj.putInt("tableId", 1);
//            JoinUserHadler jh = new JoinUserHadler();
//            jh.handleClientRequest(npcUser, obj);
            String player = npcUser.getName();
/*     */
/*  32 */     String type = "Public";
/*     */
/*  34 */     //float inPlay = Commands.appInstance.proxy.getPlayerInpaly(npcUser.getName());
/*     */
/*  36 */     GameBean gameBean = null;
/*  37 */     Room room = null;
/*  38 */     TableBean tableBean = null;
/*     */
/*  40 */     ISFSObject sfso = new SFSObject();
/*     */
/*  42 */     if (type.equals("Public"))
/*     */     {
/*     */
/*     */
/*  46 */       int tableId =13; //room number
/*  47 */       tableBean = Appmethods.getTableBean(tableId);
/*     */
/*  49 */       if (tableBean != null)
/*     */       {
/*     */
/*     */
/*  53 */         boolean isGameStarted = false;
/*  54 */         if (tableBean.get_roomId().equals("null"))
/*     */         {
/*     */           try {
/*  57 */             room = Appmethods.createRoom();
/*     */           } catch (Exception localException) {}
/*  59 */           gameBean = new GameBean();
/*  60 */           gameBean.setGameID(Appmethods.generateGameID());
/*  61 */           gameBean.setRoomId(room.getName());
/*  62 */           gameBean.setGameType("Public");
/*  63 */           gameBean.setTableBeanId(tableBean.get_id().intValue());
/*     */
/*  65 */           tableBean.set_roomId(room.getName());
/*     */
/*  67 */           Commands.appInstance.getGames().put(room.getName(), gameBean);
/*     */
/*     */         }
/*     */         else
/*     */         {
/*  72 */           gameBean = Appmethods.getGameBean(tableBean.get_roomId());
/*  73 */           room = Appmethods.getRoomByName(tableBean.get_roomId());
/*     */
/*  75 */           if (gameBean.isStarted())
/*     */           {
/*     */
/*  78 */             isGameStarted = true;
/*     */           }
/*     */         }
/*     */
/*     */
/*  83 */         Appmethods.joinRoom(npcUser, room);
/*     */
/*     */
/*     */
/*  87 */         PlayerBean playerBean = new PlayerBean(player);
                  Random rand = new Random();
/*  88 */         playerBean.setInplay(15000.10f);
/*  89 */         playerBean.setStartInplay(15000.10f);
                  playerBean.setAvatar("avatar-"+(rand.nextInt(5)+1));
/*  90 */         gameBean.getPlayerBeenList().put(player, playerBean);
/*     */
/*     */
/*  93 */         gameBean.getSpectatorsList().add(player);
/*     */
/*     */
/*  96 */         if (!isGameStarted)
/*     */         {
/*  98 */           sfso = gameBean.getSFSObject();
/*  99 */           send("JoinUser", sfso, npcUser);
/*     */
/*     */         }
/*     */         else
/*     */         {
/* 104 */           sfso = gameBean.getSFSObject();
/* 105 */           send("JoinUser", sfso, npcUser);
/* 106 */           sfso = new SFSObject();
/*     */
/*     */
/* 109 */           sfso = Appmethods.getSinkDataSFSObject(gameBean);
/* 110 */           send("SinkData", sfso, npcUser);
/*     */         }
/*     */       }
/*     */     }
            if (gameBean != null)
/*     */     {
/*     */ 
/*  38 */       int pos = 1; //sit position (0-4)
/*     */       
/*     */ 
/*  41 */       if ((((String)gameBean.getPlayers().get(pos)).equals("null")) && (!Appmethods.isUserExist(gameBean.getPlayers(), player)))
/*     */       {
/*     */ 
/*  44 */         gameBean.addPlayer(pos, npcUser.getName());
/*     */         
/*  46 */         if (gameBean.isGameGenerating())
/*     */         {
/*  48 */           sfso.putInt("remainingSeconds", gameBean.getRemainingSeconds());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */         if (!gameBean.isStarted())
/*     */         {
/*     */ 
/*  61 */           if (gameBean.getJoinedPlayers() == 2)
/*     */           {
/*     */ 
/*  64 */             gameBean.setGameGenerating(true);
/*  65 */             ISFSObject obj = new SFSObject();
/*  66 */             obj.putInt("sec", 30);
/*  67 */             send("QuickStart", obj, room.getUserList());
/*  68 */             gameBean.startTimer(31, "StartGame");
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*  73 */         if (gameBean.getGameType().equals("Public"))
/*     */         {
/*     */ 
/*  76 */           Appmethods.updateDynamicRoom(gameBean);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*  81 */           TableBean tBean = Appmethods.getPrivateTableBean(gameBean.getRoomId());
/*  82 */           UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
/*  83 */           ulBsn.updatePrivateTableLobby("Update", tBean);
/*  84 */           ulBsn = null;
/*     */           
/*  86 */           PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/*     */           
/*  88 */           pBean.setInplay(pBean.getInplay() - 30.0F);
/*     */           
/*     */ 
/*  91 */            type = "Join";
/*  92 */           if (room.getName().equals(player))
/*  93 */             type = "Create";
/*  94 */           Commands.appInstance.proxy.insertPrivateTableHistory(gameBean, player, type, tBean.getPrivateTableId().intValue());
/*     */         }
/*     */         
/*  97 */         sfso.putInt("pos", pos);
/*  98 */         sfso.putUtfString("joinedPlayer", npcUser.getName());
/*  99 */         sfso.putUtfString("avatar", Commands.appInstance.proxy.getPlayerAvatar(npcUser.getName()));
/* 100 */         sfso.putBool("isGameStarted", gameBean.isStarted());
/* 101 */         sfso.putUtfStringArray("players", gameBean.getPlayers());
/* 102 */         sfso.putBool("isGameGenerating", gameBean.isGameGenerating());
/* 103 */         sfso.putSFSArray("playerBeans", gameBean.getPlayerBeansSFSArray());
/* 104 */         send("JoinTable", sfso, room.getUserList());
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 109 */  //       send("SeatOccupied", params, npcUser);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 114 */       Appmethods.showLog("JoinTableHandler:  GameBean is Null");
/*     */     }
///* 114 */     else if (type.equals("Private"))
///*     */     {
///*     */
///* 117 */       String roomId = params.getUtfString("roomId");
///*     */
///* 119 */       JoinUserBsn juBsn = new JoinUserBsn();
///* 120 */       juBsn.joinUser(inPlay, roomId, npcUser);
///* 121 */       juBsn = null;
///*     */     }

/*     */
/*     */


           // npcUser.setConnected(true);

//            socketLessSession.setLoggedIn(true);


            Appmethods.showLog("NPC >> "+npcUser);
           // NPCManager npm = new NPCManager();
           // npm
        }

    }

}
//TODO Timeupcount