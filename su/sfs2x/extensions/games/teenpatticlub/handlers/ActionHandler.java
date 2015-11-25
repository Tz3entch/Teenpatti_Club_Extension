 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class ActionHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("ActionHandler");
     Room room = null;
     room = sender.getLastJoinedRoom();
     GameBean gameBean = null;
     if (room != null) {
       gameBean = Appmethods.getGameBean(room.getName());
     }
     String command = params.getUtfString("command");
     Appmethods.showLog("ActionHandler " + command);
     ActionsBsn actionBsn = new ActionsBsn();
     String player = sender.getName();
     
 
     if (gameBean != null)
     {
       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
       pBean.setTimeUpCount(0);
       String str1;
       switch ((str1 = command).hashCode()) {
case -1237623077:  if (str1.equals("SideShowRequest")) {actionBsn.sideShowRequest(player, params, gameBean);} break; 
case 2479673:  if (str1.equals("Pack")) {actionBsn.pack(player, params, gameBean);} break; 
case 2572955:  if (str1.equals("Seen")){actionBsn.seen(player, params, gameBean);} break; 
case 2576157:  if (str1.equals("Show")) {actionBsn.show(player, params, gameBean);} break; 
case 64274229:  if (str1.equals("Blind")) {actionBsn.blind(player, params, gameBean);} break; 
case 65070844:  if (str1.equals("Chall")) {actionBsn.chall(player, params, gameBean);} break; 
case 341324149:  if (str1.equals("SideShowResponse")){actionBsn.sideShowResponse(player, params, gameBean);}break;
       }
       actionBsn = null;
     }
     else
     {
       trace(new Object[] { "ActionHandler : GameBean is null" });
     }
   }
 }


