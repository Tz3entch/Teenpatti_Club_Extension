 package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class SetAutoPlayHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("SetAutoPlayHandler");
     Room room = null;
     room = sender.getLastJoinedRoom();
     GameBean gameBean = Appmethods.getGameBean(room.getName());
     
     if (gameBean != null)
     {
       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(sender.getName());
       pBean.setAutoPlay(params.getBool("autoPlay").booleanValue());
       send("SetAutoPlay", params, sender);
     }
     else
     {
       Appmethods.showLog("GameBean Not Found");
     }
   }
 }


