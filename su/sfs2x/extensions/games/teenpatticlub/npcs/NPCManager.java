package su.sfs2x.extensions.games.teenpatticlub.npcs;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.smartfoxserver.bitswarm.sessions.Session;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.NpcBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;


public class NPCManager {
    Random rand = new Random();
    Main app;
    List <Integer> npcsForRoom;
    int[] ar = {2,3,1,2,3,1,4,2,1,4,3,1,2};



    public NPCManager() {
        app = Commands.appInstance;
        init();
    }

    public void init() {
        Appmethods.showLog("!!!!!!!!!!!!!INIT NPC NAMAGER!!!!!!!!!!!!!");
        npcsForRoom = (List)new ArrayList<>((Arrays.asList(ar)));
        Collections.shuffle(npcsForRoom, rand);

        String robotName = "OLOLO1488";
        try {
            User npcUser = app.getApi().createNPC(robotName, app.getParentZone(), true);
            JoinNpcToRoom(npcUser, 4);
        } catch (SFSException e) {
            e.printStackTrace();
        }
    }
    private void FillRooms() {

        Appmethods.showLog("**********DISTRIBUTE NPC");
//
//        try {
//
//
//        } catch (SFSException e) {
//			e.printStackTrace();
//		}
    }

    private void JoinNpcToRoom(User npcUser, int tableId) throws SFSException {
        String player = npcUser.getName();
        String type = "Public";
        GameBean gameBean = null;
        Room room = null;
        TableBean tableBean = null;
        ISFSObject sfso = new SFSObject();

        if (type.equals("Public")) {
            tableBean = Appmethods.getTableBean(tableId);

            if (tableBean != null) {
                boolean isGameStarted = false;
                
                if (tableBean.get_roomId().equals("null")) {
                    try {
                        room = Appmethods.createRoom();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    gameBean = new GameBean();
                    gameBean.setGameID(Appmethods.generateGameID());
                    gameBean.setRoomId(room.getName());
                    gameBean.setGameType("Public");
                    gameBean.setTableBeanId(tableBean.get_id());
                    tableBean.set_roomId(room.getName());
                    Commands.appInstance.getGames().put(room.getName(), gameBean);
                } else {
                    gameBean = Appmethods.getGameBean(tableBean.get_roomId());
                    room = Appmethods.getRoomByName(tableBean.get_roomId());

                    if (gameBean.isStarted()) {
                        isGameStarted = true;
                    }
                }
                
                Appmethods.joinRoom(npcUser, room);
                PlayerBean playerBean = new PlayerBean(player);
                playerBean.setInplay(15000.10f);
                playerBean.setStartInplay(15000.10f);
                playerBean.setAvatar("avatar-" + (rand.nextInt(5) + 1));
                gameBean.getPlayerBeenList().put(player, playerBean);
                gameBean.getSpectatorsList().add(player);
                
                if (!isGameStarted) {
                    sfso = gameBean.getSFSObject();
                    app.send("JoinUser", sfso, npcUser);

                } else {
                    sfso = gameBean.getSFSObject();
                    app.send("JoinUser", sfso, npcUser);
                    sfso = new SFSObject();


                    sfso = Appmethods.getSinkDataSFSObject(gameBean);
                    app.send("SinkData", sfso, npcUser);
                }
            }
        }
        if (gameBean != null) {

            int pos = 1;


            if ((((String) gameBean.getPlayers().get(pos)).equals("null")) && (!Appmethods.isUserExist(gameBean.getPlayers(), player))) {

                gameBean.addPlayer(pos, npcUser.getName());

                if (gameBean.isGameGenerating()) {
                    sfso.putInt("remainingSeconds", gameBean.getRemainingSeconds());
                }


                if (!gameBean.isStarted()) {

                    if (gameBean.getJoinedPlayers() == 2) {

                        gameBean.setGameGenerating(true);
                        ISFSObject obj = new SFSObject();
                        obj.putInt("sec", 30);
                        app.send("QuickStart", obj, room.getUserList());
                        gameBean.startTimer(31, "StartGame");
                    }
                }


                if (gameBean.getGameType().equals("Public")) {

                    Appmethods.updateDynamicRoom(gameBean);

                } else {
                    TableBean tBean = Appmethods.getPrivateTableBean(gameBean.getRoomId());
                    UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
                    ulBsn.updatePrivateTableLobby("Update", tBean);
                    ulBsn = null;

                    PlayerBean pBean = (PlayerBean) gameBean.getPlayerBeenList().get(player);

                    pBean.setInplay(pBean.getInplay() - 30.0F);


                    type = "Join";
                    if (room.getName().equals(player))
                        type = "Create";
                    Commands.appInstance.proxy.insertPrivateTableHistory(gameBean, player, type, tBean.getPrivateTableId().intValue());
                }

                sfso.putInt("pos", pos);
                sfso.putUtfString("joinedPlayer", npcUser.getName());
                sfso.putUtfString("avatar", Commands.appInstance.proxy.getPlayerAvatar(npcUser.getName()));
                sfso.putBool("isGameStarted", gameBean.isStarted());
                sfso.putUtfStringArray("players", gameBean.getPlayers());
                sfso.putBool("isGameGenerating", gameBean.isGameGenerating());
                sfso.putSFSArray("playerBeans", gameBean.getPlayerBeansSFSArray());
                app.send("JoinTable", sfso, room.getUserList());

            } else {

            }
        } else {
            Appmethods.showLog("JoinTableHandler:  GameBean is Null");
        }
//     else if (type.equals("Private"))
//     {
//
//       String roomId = params.getUtfString("roomId");
//
//       JoinUserBsn juBsn = new JoinUserBsn();
//       juBsn.joinUser(inPlay, roomId, npcUser);
//       juBsn = null;
//     }

    }
    private List<User> FetchNPC(List<String>npcNames) throws SFSException  {
        Appmethods.showLog("**********FetchNPC************");
        List<User> result = new ArrayList<>();
        User npcUser;
        for(String npcName :npcNames) {
             npcUser = app.getApi().createNPC(npcName, app.getParentZone(), true);
             result.add(npcUser);
            Appmethods.showLog("FetchNPC: NPC "+npcUser.getName()+" created.");
        }
        return result;
    }

}
