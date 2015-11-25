 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;

 public class UserPingBackHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User user, ISFSObject params)
   {
     Commands.appInstance.cdUser.updateConnectedUser(user);
   }
 }


