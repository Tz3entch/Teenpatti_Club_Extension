 package su.sfs2x.extensions.games.teenpatticlub.events;
 
 import com.smartfoxserver.v2.core.ISFSEvent;
 import com.smartfoxserver.v2.core.SFSEventParam;
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.exceptions.SFSException;
 import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
 import java.util.List;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.DisconnectUserBsn;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
 
 public class UserDisconnectedEventHandler
   extends BaseServerEventHandler
 {
   public void handleServerEvent(ISFSEvent event) throws SFSException
   {
     User user = (User)event.getParameter(SFSEventParam.USER);
     Commands.appInstance.proxy.updateLogoutSession(user.getName());
     Room room = null;
     
     List<Room> joinedRooms = (List)event.getParameter(SFSEventParam.JOINED_ROOMS);
     if (joinedRooms.size() > 0)
     {
       for (int i = 0; i < joinedRooms.size(); i++)
       {
         room = (Room)joinedRooms.get(i);
         
         Appmethods.showLog("Room Name " + room.getName());
         
         if (room.getGroupId().equals("GameGroup"))
         {
           DisconnectUserBsn duserBsn = new DisconnectUserBsn();
           duserBsn.disconnectUser(user, room);
           duserBsn = null;
         }
       }
     }
     
     if ((Commands.appInstance.cdUser.connectedUsers.size() > 0) && (Commands.appInstance.cdUser.connectedUsers.containsKey(user.getName()))) {
       Commands.appInstance.cdUser.connectedUsers.remove(user.getName());
     }
   }
 }


