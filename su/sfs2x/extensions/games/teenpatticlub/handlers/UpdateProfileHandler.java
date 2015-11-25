 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class UpdateProfileHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("UpdateProfileHandler");
     
     String player = sender.getName();
     String mobile = params.getUtfString("mobile");
     String email = params.getUtfString("email");
     String password = params.getUtfString("password");
     
     Commands.appInstance.proxy.updateProfile(player, mobile, email, password);
     send("UpdateProfile", params, sender);
   }
 }


