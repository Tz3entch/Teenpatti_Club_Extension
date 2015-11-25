 package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
 
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class CheckDeviceIdHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("CheckDeviceIdHandler");
     
     String id = params.getUtfString("deviceId");
     String player = sender.getName();
     
     ISFSObject sfso = Commands.appInstance.proxy.checkDeviceId(player, id);
     send("CheckDeviceId", sfso, sender);
   }
 }


