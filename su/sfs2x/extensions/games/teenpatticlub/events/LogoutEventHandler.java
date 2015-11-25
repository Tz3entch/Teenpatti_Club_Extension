 package su.sfs2x.extensions.games.teenpatticlub.events;
 
 import com.smartfoxserver.v2.core.ISFSEvent;
 import com.smartfoxserver.v2.core.SFSEventParam;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.exceptions.SFSException;
 import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class LogoutEventHandler
   extends BaseServerEventHandler
 {
   public void handleServerEvent(ISFSEvent event)
     throws SFSException
   {
     User user = (User)event.getParameter(SFSEventParam.USER);
     String player = user.getName();
     Appmethods.showLog(player + " Disconnected");
     
     Commands.appInstance.proxy.updateLogoutSession(user.getName());
   }
 }


