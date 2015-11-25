 package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
 
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;

 public class GetUserGiftsHadler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     ISFSObject sfso = new SFSObject();
     sfso.putSFSArray("userGifts", Commands.appInstance.proxy.getUserGifts(sender.getName()));
     send("GetUserGifts", sfso, sender);
   }
 }


