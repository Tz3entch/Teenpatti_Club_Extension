 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.JoinUserBsn;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class JoinUserHadler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("**********JoinUserHadler*******");
     
     String player = sender.getName();
     
     String type = params.getUtfString("type");
     
     float inPlay = Commands.appInstance.proxy.getPlayerInpaly(sender.getName());
     
     GameBean gameBean = null;
     Room room = null;
     TableBean tableBean = null;
     
     ISFSObject sfso = new SFSObject();
     
     if (type.equals("Public"))
     {
 
 
       int tableId = params.getInt("tableId");
       tableBean = Appmethods.getTableBean(tableId);
       
       if (tableBean != null)
       {
 
 
         boolean isGameStarted = false;
         if (tableBean.get_roomId().equals("null"))
         {
           try {
             room = Appmethods.createRoom();
           } catch (Exception localException) {}
           gameBean = new GameBean();
           gameBean.setGameID(Appmethods.generateGameID());
           gameBean.setRoomId(room.getName());
           gameBean.setGameType("Public");
           gameBean.setTableBeanId(tableBean.get_id().intValue());
           
           tableBean.set_roomId(room.getName());
           
           Commands.appInstance.getGames().put(room.getName(), gameBean);
 
         }
         else
         {
           gameBean = Appmethods.getGameBean(tableBean.get_roomId());
           room = Appmethods.getRoomByName(tableBean.get_roomId());
           
           if (gameBean.isStarted())
           {
 
             isGameStarted = true;
           }
         }
         
 
         Appmethods.joinRoom(sender, room);
         
 
 
         PlayerBean playerBean = new PlayerBean(player);
         playerBean.setInplay(inPlay);
         playerBean.setStartInplay(inPlay);
         gameBean.getPlayerBeenList().put(player, playerBean);
         
 
         gameBean.getSpectatorsList().add(player);
         
 
         if (!isGameStarted)
         {
           sfso = gameBean.getSFSObject();
           send("JoinUser", sfso, sender);
 
         }
         else
         {
           sfso = gameBean.getSFSObject();
           send("JoinUser", sfso, sender);
           sfso = new SFSObject();
           
 
           sfso = Appmethods.getSinkDataSFSObject(gameBean);
           send("SinkData", sfso, sender);
         }
       }
     }
     else if (type.equals("Private"))
     {
 
       String roomId = params.getUtfString("roomId");
       
       JoinUserBsn juBsn = new JoinUserBsn();
       juBsn.joinUser(inPlay, roomId, sender);
       juBsn = null;
     }
   }
 }


