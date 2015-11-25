 package su.sfs2x.extensions.games.teenpatticlub.bsn;

 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class JoinUserBsn {
     public void joinUser(float inPlay, String roomId, User sender) {
         String player = sender.getName();
         GameBean gameBean = null;
         Room room = null;
         TableBean tableBean = null;
         ISFSObject sfso = new SFSObject();
         tableBean = Appmethods.getPrivateTableBean(roomId);

         if (tableBean != null) {
             boolean isGameStarted = false;
             room = Commands.appInstance.getParentZone().getRoomByName(roomId);
             if (room == null) {
                 try {
                     room = Appmethods.createPrivateRoom(roomId);
                 } catch (Exception localException) {
                 }
             }
             gameBean = Appmethods.getGameBean(roomId);

             if (gameBean == null) {
                 gameBean = new GameBean();
                 gameBean.setGameID(Appmethods.generateGameID());
                 gameBean.setRoomId(room.getName());
                 gameBean.setGameType("Private");

                 Commands.appInstance.getGames().put(room.getName(), gameBean);


             } else if (gameBean.isStarted()) {

                 isGameStarted = true;
             }


             Appmethods.joinRoom(sender, room);


             PlayerBean playerBean = new PlayerBean(player);
             playerBean.setInplay(inPlay);
             gameBean.getPlayerBeenList().put(player, playerBean);


             gameBean.getSpectatorsList().add(player);


             if (!isGameStarted) {
                 sfso = gameBean.getSFSObject();
                 Commands.appInstance.send("JoinUser", sfso, sender);

             } else {
                 sfso = gameBean.getSFSObject();
                 Commands.appInstance.send("JoinUser", sfso, sender);
                 sfso = new SFSObject();


                 sfso = Appmethods.getSinkDataSFSObject(gameBean);
                 Commands.appInstance.send("SinkData", sfso, sender);
             }
         } else {
             Appmethods.showLog("JoinUserBsn: Table Bean is Null");
         }
     }
 }


