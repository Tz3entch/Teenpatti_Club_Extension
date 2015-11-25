 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class GetPlayerProfileHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("********** GetPlayerProfile ***********");
     String player = params.getUtfString("player");
     ISFSObject sfso = new SFSObject();
     sfso = Commands.appInstance.proxy.getPlayerProfile(player);
     send("GetPlayerProfile", sfso, sender);
   }
 }


