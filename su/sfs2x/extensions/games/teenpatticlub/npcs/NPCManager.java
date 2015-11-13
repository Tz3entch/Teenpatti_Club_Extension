package su.sfs2x.extensions.games.teenpatticlub.npcs;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.timers.CheckRoomTimer;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;


public class NPCManager {
    Random rand = new Random();
    Main app;
    int nameCount=0;
    List <Integer> npcsForRoom;
    LinkedList<String> unusedNpcNames;
    Integer [] ar = {2,3,1,2,3,1,4,2,1,4,3,1,2};
//    Integer [] ar =   {1,0,0,0,0,0,0,0,0,0,0,0,0};
    String[] arNames = {"Addison", "Ashley", "Ashton", "Avery", "Bailey", "Cameron", "Carson",
                        "Carter", "Casey", "Corey", "Dakota", "Devin", "Drew", "Emerson",
                        "Harley", "Harper", "Hayden", "Hunter", "Jaiden", "Jamie", "Jaylen",
                        "Jesse", "Jordan", "Justice", "Kai", "Kelly", "Kelsey", "Kendall",
                        "Kennedy", "Lane", "Logan", "Mackenzie", "Madison", "Marley", "Mason",
                        "Morgan", "Parker", "Peyton", "Piper", "Quinn", "Reagan", "Reese",
                        "Riley", "Rowan", "Ryan", "Shane", "Shawn", "Sydney", "Taylor",
                        "Tristan"};



    public NPCManager() {
        app = Commands.appInstance;
    }

    public void init() {
        Appmethods.showLog("!!!!!!!!!!!!!INIT NPC NAMAGER!!!!!!!!!!!!!");
        npcsForRoom = (List)new ArrayList<>((Arrays.asList(ar)));
//        npcsForRoom = app.proxy.getNpcforRoom();
        unusedNpcNames = new LinkedList<>(Arrays.asList(arNames));
//        unusedNpcNames = app.proxy.getNpcNames();
        Collections.shuffle(npcsForRoom, rand);
        Collections.shuffle((List)unusedNpcNames, rand);
        FillRooms();
        Timer timer = new Timer();
        timer.schedule(new CheckRoomTimer(this), 60000, 180000);
    }

    private int getNpcsNumber() {
        int n =0;
        for (Integer i:npcsForRoom) {
            n+=i;
        }
        return n;
    }

    private void FillRooms() {

        Appmethods.showLog("*******************FILL ROOMS STARTED*************************");
        try {
            List<User> npcUserList = FetchNpcs(unusedNpcNames);
            nameCount=0;
            for (int i = 1; i < npcsForRoom.size()+1; i++) {
                for (int j = 0; j < npcsForRoom.get(i-1); j++) {
                    joinNpcToRoom(npcUserList.get(nameCount), i, j);
                    nameCount++;
                }

            }
        } catch (SFSException e) {
            e.printStackTrace();
        }
//
//        try {
//
//
//        } catch (SFSException e) {
//			e.printStackTrace();
//		}
    }

    private void joinNpcToRoom(User npcUser, int tableId, int pos) throws SFSException {
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
                npcUser.setPlayerId( rand.nextInt(1000), room);
                PlayerBean playerBean = new PlayerBean(player);
                playerBean.setInplay(25000.10f);
                playerBean.setStartInplay(25000.10f);
                playerBean.setAvatar("avatar-" + (rand.nextInt(6) + 1));
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


            if ((( gameBean.getPlayers().get(pos)).equals("null")) && (!Appmethods.isUserExist(gameBean.getPlayers(), player))) { //TODO исправить это говно

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

                    PlayerBean pBean = gameBean.getPlayerBeenList().get(player);

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
    private List<User> FetchNpcs(LinkedList<String>npcNames) throws SFSException  {
        Appmethods.showLog("**********FetchNPC************");
        List<User> result = new ArrayList<>();
        User npcUser;
        for(int i=0; i<getNpcsNumber(); i++) {
             npcUser = app.getApi().createNPC(npcNames.removeFirst(), app.getParentZone(), true);
             nameCount++;
             result.add(npcUser);
            Appmethods.showLog("FetchNPC: NPC "+npcUser.getName()+" created.");
        }
        return result;
    }

    private static List<User> npcsInRoom(Room room) {
        List<User> players = room.getPlayersList();
        List<User> result = new ArrayList<>();
        if (players.size() > 0) {
            for (User player : players) {
                if (player.isNpc()) {
                    result.add(player);
                }
            }
        }
        return result;
    }


    public void checkRooms() {
        Appmethods.showLog("*****************************CHECKROOMS STARTED**************************************");
        System.out.println("*****CHECKROOMS STARTED****");
        GameBean gameBean = null;
        Room room = null;
        TableBean tableBean;
        PlayerBean pb;
        ArrayList<TableBean> publicTables = Commands.appInstance.publicTables;
        ConcurrentHashMap<String, GameBean> gameBeanMap = Commands.appInstance.getGames();
        for (Object o : gameBeanMap.entrySet()) {
            ConcurrentHashMap.Entry pair = (ConcurrentHashMap.Entry) o;
            gameBean = (GameBean) pair.getValue();
            //room = Appmethods.getRoomByName(tableBean.get_roomId());
            room = Appmethods.getRoomByName(gameBean.getRoomId());

            if ((!gameBean.getGameRoundBean().getTurn().equals("null")) && room!=null) {
                NpcLogic npcLogic = new NpcLogic(gameBean);
                List<User> npcs = npcsInRoom(room);
                if (gameBean.getPlayerBeenList().size() >= gameBean.getMaxNoOfPlayers() && npcs.size() > 0) {
                    int n = rand.nextInt(npcs.size());
                    String npcName = npcs.get(n).getName();
                    if (!npcName.equals(npcLogic.findWonUser(gameBean).getName())) {
                    gameBean.getPlayerBeenList().get(npcName).setActive(false);
                    Appmethods.showLog("********** NPCManager: npc "+ npcName + " will be removed! Reason: room is full!************");
                    unusedNpcNames.addLast(npcs.get(n).getName());
                    app.cdUser.removeConnectedUser(npcs.get(n));
                    npcs.remove(npcs.get(n));
                    System.out.println("*****NPC REMOVED****");
                    }

                }
                ConcurrentHashMap<String, PlayerBean> playerBeans = gameBean.getPlayerBeenList();
                if (npcs.size() > 0) {
                    for (Iterator<User> iterator = npcs.iterator(); iterator.hasNext(); ) {
                        User npc = iterator.next();
                        pb = playerBeans.get(npc.getName());
                        if (pb.getTotalHands() > (4 + rand.nextInt(7)) && gameBean.getPlayerBeenList().size() > 1) {
                            String wonName = npcLogic.findWonUser(gameBean).getName();
                            if (wonName == null || (!npc.getName().equals(npcLogic.findWonUser(gameBean).getName()))) {
                                gameBean.getPlayerBeenList().get(npc.getName()).setActive(false);
                                Appmethods.showLog("********** NPCManager: npc " + npc.getName() + " removed! Reason: Played enough hands!************");
                                unusedNpcNames.addLast(npc.getName());
                                app.cdUser.removeConnectedUser(npc);
                                iterator.remove();
                                System.out.println("*****NPC REMOVED****");

                                try {
                                    User npcUser = app.getApi().createNPC(unusedNpcNames.removeFirst(), app.getParentZone(), true);
                                    ArrayList<String> pos = gameBean.getPlayers();
                                    ArrayList<Integer> list = new ArrayList<>();
                                    for (String posit : pos) {
                                        if (posit.equals("null")) {
                                            list.add(pos.indexOf(posit));
                                        }
                                    }
                                    if (list.size()>0) {
                                    joinNpcToRoom(npcUser, gameBean.getTableBeanId(), list.get(0));
                                    //Appmethods.updateGameBeanUpdateLobby(gameBean, room);
                                    Appmethods.showLog("********** NPCManager: npc " + npcUser.getName() + " added! Reason: To replace removed npc ************");
                                    System.out.println("*****NPC ADDED****");
                                    }
                                } catch (SFSException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                if ((gameBean.getPlayerBeenList().size() < npcsForRoom.get(gameBean.getTableBeanId() - 1))||(npcs.size()<1&&gameBean.getPlayerBeenList().size()==1)) {
                    try {
                        User npcUser = app.getApi().createNPC(unusedNpcNames.removeFirst(), app.getParentZone(), true);
                        ArrayList<String> pos = gameBean.getPlayers();
                        ArrayList<Integer> list = new ArrayList<>();
                        for (String posit : pos) {
                            if (posit.equals("null")) {
                                list.add(pos.indexOf(posit));
                            }
                        }
                        joinNpcToRoom(npcUser, gameBean.getTableBeanId(), list.get(0));
                        Appmethods.showLog("********** NPCManager: npc "+npcUser.getName()+" added! Reason: few players in room! ************");
                        System.out.println("*****NPC ADDED****");
                    } catch (SFSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//      it.remove();
    }
}
