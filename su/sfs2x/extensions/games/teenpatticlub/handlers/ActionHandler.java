/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
/*    */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ActionHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 24 */     Appmethods.showLog("ActionHandler");
/* 25 */     Room room = null;
/* 26 */     room = sender.getLastJoinedRoom();
/* 27 */     GameBean gameBean = null;
/* 28 */     if (room != null) {
/* 29 */       gameBean = Appmethods.getGameBean(room.getName());
/*    */     }
/* 31 */     String command = params.getUtfString("command");
/* 32 */     Appmethods.showLog("ActionHandler " + command);
/* 33 */     ActionsBsn actionBsn = new ActionsBsn();
/* 34 */     String player = sender.getName();
/*    */     
/*    */ 
/* 37 */     if (gameBean != null)
/*    */     {
/* 39 */       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/* 40 */       pBean.setTimeUpCount(0);
/*    */       String str1;
/* 42 */       switch ((str1 = command).hashCode()) {
case -1237623077:  if (str1.equals("SideShowRequest")) {actionBsn.sideShowRequest(player, params, gameBean);} break; 
case 2479673:  if (str1.equals("Pack")) {actionBsn.pack(player, params, gameBean);} break; 
case 2572955:  if (str1.equals("Seen")){actionBsn.seen(player, params, gameBean);} break; 
case 2576157:  if (str1.equals("Show")) {actionBsn.show(player, params, gameBean);} break; 
case 64274229:  if (str1.equals("Blind")) {actionBsn.blind(player, params, gameBean);} break; 
case 65070844:  if (str1.equals("Chall")) {actionBsn.chall(player, params, gameBean);} break; 
case 341324149:  if (str1.equals("SideShowResponse")){actionBsn.sideShowResponse(player, params, gameBean);}break;
/*    */       }
/*    */       
/*    */       //label362:
/* 68 */       actionBsn = null;
/*    */     }
/*    */     else
/*    */     {
/* 72 */       trace(new Object[] { "ActionHandler : GameBean is null" });
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/ActionHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */