 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class SinkDataHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("*********** SinkDataHandler **********");
     String roomId = params.getUtfString("roomId");
     Room room = Commands.appInstance.getParentZone().getRoomByName(roomId);
     if (room != null)
     {
       GameBean gameBean = Appmethods.getGameBean(roomId);
       if (gameBean != null)
       {
         User user = Commands.appInstance.getParentZone().getUserByName(sender.getName());
         Appmethods.joinRoom(user, room);
         PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(sender.getName());
         if (pBean != null)
         {
           pBean.setActive(true);
           send("SinkData", Appmethods.getSinkDataSFSObject(gameBean), sender);
         }
       }
       else
       {
         trace(new Object[] { "GameBean not found " + roomId });
       }
       
     }
     else
     {
       trace(new Object[] { "Room not found " + roomId });
     }
   }
 }


