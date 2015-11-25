 package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class GetPlayerSession
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     String player = params.getUtfString("player");
     Room room = sender.getLastJoinedRoom();
     GameBean gameBean = Appmethods.getGameBean(room.getName());
     ISFSObject sfso = new SFSObject();
     
     if (gameBean != null)
     {
       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
       
       sfso = pBean.getSessionSfsObject();
       sfso.putUtfString("player", player);
       
       send("GetUserSession", sfso, sender);
     }
   }
 }


