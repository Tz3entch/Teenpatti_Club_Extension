package su.sfs2x.extensions.games.teenpatticlub.npcs;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.SFSApi;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.util.ClientDisconnectionReason;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.timers.CheckRoomTimer;
import su.sfs2x.extensions.games.teenpatticlub.timers.TaskRunner;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;


public class NPCManager {
    private Random rand = new Random();
    private Main app;
    private int nameCount=0;
    private List <Integer> npcsForRoom;
    private LinkedList<String> unusedNpcNames;
    private HashMap<String, Integer> settings;
    private Integer [] ar = {2,3,1,2,3,1,5,2,1,4,3,1,2};
////  private Integer [] ar =   {5,0,0,0,0,0,0,0,0,0,0,0,0};
    private String[] arNames = {"Addison", "Ashley", "Ashton", "Avery", "Bailey", "Cameron", "Carson",
                        "Carter", "Casey", "Corey", "Dakota", "Devin", "Drew", "Emerson",
                        "Harley", "Harper", "Hayden", "Hunter", "Jaiden", "Jamie", "Jaylen",
                        "Jesse", "Jordan", "Justice", "Kai", "Kelly", "Kelsey", "Kendall",
                        "Kennedy", "Lane", "Logan", "Mackenzie", "Madison", "Marley", "Mason",
                        "Morgan", "Parker", "Peyton", "Piper", "Quinn", "Reagan", "Reese",
                        "Riley", "Rowan", "Ryan", "Shane", "Shawn", "Sydney", "Taylor",
                        "Tristan"};
    ScheduledFuture<?> taskHandle;



    public NPCManager() {
        app = Commands.appInstance;
    }

    public void init() {
        Appmethods.showLog("!!!!!!!!!!!!!INIT NPC NAMAGER!!!!!!!!!!!!!");
//        npcsForRoom = (List)new ArrayList<>((Arrays.asList(ar)));
        npcsForRoom = app.proxy.getNpcforRoom();
//        unusedNpcNames = new LinkedList<>(Arrays.asList(arNames));
        unusedNpcNames = app.proxy.getNpcNames();
        settings = app.proxy.getNpcSettings();
        Collections.shuffle(npcsForRoom, rand);
        Collections.shuffle((List)unusedNpcNames, rand);
        FillRooms();
//        Timer timer = new Timer();
//        timer.schedule(new CheckRoomTimer(this), settings.get("delay"), settings.get("period"));
        SmartFoxServer sfs = Commands.appInstance.sfs;
        taskHandle = sfs.getTaskScheduler().scheduleAtFixedRate(new CheckRoomTimer(this), settings.get("delay"), settings.get("period"), TimeUnit.MILLISECONDS);
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

    private void printQuery() {
        String g = "";
        for (String s: unusedNpcNames) {
            g+=s+", ";
        }
        Appmethods.showLog("Query:"+ g );
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


            if ((( gameBean.getPlayers().get(pos)).equals("null")) && (!Appmethods.isUserExist(gameBean.getPlayers(), player))) { //TODO ��������� ��� �����

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
                    float commission = Commands.appInstance.proxy.getPrivateCommission();

                    PlayerBean pBean = gameBean.getPlayerBeenList().get(player);

                    pBean.setInplay(pBean.getInplay() - 30.0F);


                    type = "Join";
                    if (room.getName().equals(player))
                        type = "Create";
                    Commands.appInstance.proxy.insertPrivateTableHistory(gameBean, player, type, tBean.getPrivateTableId().intValue(), commission);
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

    public static List<User> npcsInRoom(Room room) {
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
    private int getActivePlayers(GameBean gameBean) {
        int count = 0;
        ConcurrentHashMap <String, PlayerBean> players = gameBean.getPlayerBeenList();
        for (Object o : players.entrySet()) {
            ConcurrentHashMap.Entry pair = (ConcurrentHashMap.Entry) o;
            PlayerBean playerBean = (PlayerBean) pair.getValue();
            if (playerBean.isActive()) {
                count++;
            }
        }
        return  count;
    }


    public void checkRooms() {
        Appmethods.showLog("*****************************CHECKROOMS STARTED**************************************");
        System.out.println("*****CHECKROOMS STARTED****");
        app.proxy.insertRoomcheckerStartTime();
        StringBuilder actions = new StringBuilder();
        String msg = "";
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
            Appmethods.showLog("********************295****************************");
            if (gameBean.getPlayerBeenList().size() > 1 && (!gameBean.getGameRoundBean().getTurn().equals("null")) && room != null) {
                NpcLogic npcLogic = new NpcLogic(gameBean, 0);
                List<User> npcs = npcsInRoom(room);
                Appmethods.showLog("********************BEFORE FULL ROOM****************************");
                if (getActivePlayers(gameBean)>=gameBean.getMaxNoOfPlayers()&& npcs.size() > 0) {
                    int n = rand.nextInt(npcs.size());
                    String npcName = npcs.get(n).getName();
                    if (!npcName.equals(npcLogic.findWonUser(gameBean).getName())) {
                        Commands.appInstance.getApi().disconnectUser(npcs.get(n));
                        gameBean.getPlayerBeenList().get(npcName).setActive(false);
                        Appmethods.showLog("********** NPCManager: npc " + npcName + " will be removed! Reason: room is full!************");
                        msg = "Npc " + npcName + " was removed! Reason: room is full!";
                        actions.append(msg); //.append("\n");
                        unusedNpcNames.addLast(npcs.get(n).getName());
                       // app.cdUser.removeConnectedUser(npcs.get(n));
                        Commands.appInstance.getApi().disconnectUser(npcs.get(n));
                        npcs.remove(npcs.get(n));
                        //Appmethods.updateDynamicRoom(gameBean);
                        System.out.println("*****NPC REMOVED****");
                    }
                }


                Appmethods.showLog("********************BEFORE DUPLICATE ROOM****************************");
                tableBean = Appmethods.getTableBean(gameBean.getTableBeanId());
                room = Appmethods.getRoomByName(tableBean.get_roomId());
                if (room==null && getActivePlayers(gameBean)==5 && npcsInRoom(Appmethods.getRoomByName(gameBean.getRoomId())).size()==0) {
                    for (int j = 0; j < npcsForRoom.get(gameBean.getTableBeanId() - 1); j++) {
                        User npcUser = null;
                        try {
                            npcUser = app.getApi().createNPC(unusedNpcNames.removeFirst(), app.getParentZone(), true);
                            joinNpcToRoom(npcUser, gameBean.getTableBeanId(), j);
                            Appmethods.showLog("********** NPCManager: npc " + npcUser.getName() + " added! Reason: few players in duplicate room! ************");
                            msg = "Npc " + npcUser.getName() + " was added! Reason: few players in duplicate room!";
                            actions.append(msg); //.append("\n");
                            System.out.println("*****NPC ADDED****");
                        } catch (SFSException e) {
                            Appmethods.showLog("EXCEPTION IN DUPLICATE ROOM!!!");
                            e.printStackTrace();
                        }
                    }

                }
                Appmethods.showLog("********************AFTER DUPLICATE ROOM****************************");

                Appmethods.showLog("********************336 ENOUGH HANDS START****************************");
                ConcurrentHashMap<String, PlayerBean> playerBeans = gameBean.getPlayerBeenList();
                if (npcs.size() > 0) {
                    for (Iterator<User> iterator = npcs.iterator(); iterator.hasNext(); ) {
                        User npc = iterator.next();
                        pb = playerBeans.get(npc.getName());
                        Appmethods.showLog("********************BEFORE ENOUGH HANDS****************************");
                        if (pb.getTotalHands() > (settings.get("minHands") + rand.nextInt(settings.get("maxHands") - settings.get("minHands") + 1)) && gameBean.getPlayerBeenList().size() > 0) {
                            Appmethods.showLog("********************AFTER 344 IF****************************");
                            String wonName = null;
                               try {
                                   wonName = npcLogic.findWonUser(gameBean).getName();
                               } catch (Exception e) {
                                   e.printStackTrace();
                           }
                            if (wonName == null || (!npc.getName().equals(wonName))) {///
                                Appmethods.showLog("********************AFTER 347 IF****************************");
                                Commands.appInstance.getApi().disconnectUser(npc);/////////
                                gameBean.getPlayerBeenList().get(npc.getName()).setActive(false);
                                Appmethods.showLog("********** NPCManager: npc " + npc.getName() + " removed! Reason: Played enough hands!************");
                                msg = "Npc " + npc.getName() + " was removed! Reason: Played enough hands!";
                                actions.append(msg);//.append("\n");
                                printQuery();
                                unusedNpcNames.addLast(npc.getName());
                               // app.cdUser.removeConnectedUser(npc);
                                iterator.remove();
                                System.out.println("*****NPC REMOVED****");
                                printQuery();
                                Appmethods.showLog("********************AFTER ENOUGH HANDS****************************");

                                Appmethods.showLog("********************BEFORE REPLACE NPC****************************");
                                try {
                                    User npcUser = app.getApi().createNPC(unusedNpcNames.removeFirst(), app.getParentZone(), true);
                                    ArrayList<String> pos = gameBean.getPlayers();
                                    ArrayList<Integer> list = new ArrayList<>();
                                    for (String posit : pos) {
                                        if (posit.equals("null")) {
                                            list.add(pos.indexOf(posit));
                                        }
                                    }
                                    if (list.size() > 0) {
                                        joinNpcToRoom(npcUser, gameBean.getTableBeanId(), list.get(0));
                                        //Appmethods.updateGameBeanUpdateLobby(gameBean, room);
                                        Appmethods.showLog("********** NPCManager: npc " + npcUser.getName() + " added! Reason: To replace removed npc! ************");
                                        msg = "Npc " + npcUser.getName() + " added! Reason: To replace removed npc!";
                                        actions.append(msg);//.append("\n");
                                        System.out.println("*****NPC ADDED****");
                                        printQuery();
                                    }
                                } catch (SFSException e) {
                                    Appmethods.showLog("EXCEPTION IN NPC REPLACING!!!");
                                    e.printStackTrace();
                                }
                                Appmethods.showLog("********************AFTER REPLACE NPC****************************");
                            }
                        }
                    }
                }
                Appmethods.showLog("********************382 ENOUGH HANDS FINISH****************************");

                Appmethods.showLog("********************BEFORE FEW PLAYERS****************************");
                if ((gameBean.getPlayerBeenList().size() < npcsForRoom.get(gameBean.getTableBeanId() - 1)) || (npcs.size() < 1 && gameBean.getPlayerBeenList().size() == 1)) {
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
                        Appmethods.showLog("********** NPCManager: npc " + npcUser.getName() + " added! Reason: few players in room! ************");
                        msg = "Npc " + npcUser.getName() + " added! Reason: few players in room!";
                        actions.append(msg);//.append("\n");
                        System.out.println("*****NPC ADDED****");
                    } catch (SFSException e) {
                        Appmethods.showLog("EXCEPTION IN FEW PLAYERS!!!");
                        e.printStackTrace();
                    }
                }
                Appmethods.showLog("********************AFTER FEW PLAYERS****************************");
            }
        }
        Appmethods.showLog("********************BEFORE NEW PLAYER****************************");
        ArrayList<User> allUsers = (ArrayList<User>)Commands.appInstance.getParentZone().getUserList();
        for (User user: allUsers) {
            if (user.isNpc() && (!user.isPlayer())) {
                String name = user.getName();
                Commands.appInstance.getApi().disconnectUser(user);
                unusedNpcNames.addLast(name);
                Appmethods.showLog("********** NPCManager: npc " + name + " removed! Reason: Not a player!************");
                msg = "Npc " + name + " removed! Reason: Not a player!";
                actions.append(msg);//.append("\n");
            }
        }
        Appmethods.showLog("********************AFTER NEW PLAYER****************************");
        app.proxy.insertRoomcheckerActions(actions.toString());
        app.proxy.insertRoomcheckerEndTime();
        Appmethods.showLog("*****************************CHECKROOMS ENDED**************************************");
    }
}
