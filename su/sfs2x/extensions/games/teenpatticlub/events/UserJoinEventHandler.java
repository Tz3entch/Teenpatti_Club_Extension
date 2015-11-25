 package su.sfs2x.extensions.games.teenpatticlub.events;
 
 import com.smartfoxserver.v2.core.ISFSEvent;
 import com.smartfoxserver.v2.core.SFSEventParam;
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.exceptions.SFSException;
 import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class UserJoinEventHandler
   extends BaseServerEventHandler
 {
   public void handleServerEvent(ISFSEvent event)
     throws SFSException
   {
     Appmethods.showLog("************ UserJoinEventHandler ************");
     Room room = (Room)event.getParameter(SFSEventParam.ROOM);
     System.out.println(room.getName());
     
     User user = (User)event.getParameter(SFSEventParam.USER);
     
     Commands.appInstance.cdUser.addConnectedUser(user);
   }
 }


