/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import su.sfs2x.extensions.games.teenpatticlub.classes.ConnectDisconnectUser;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
/*    */ 
/*    */ public class UserPingBackHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User user, ISFSObject params)
/*    */   {
/* 15 */     Commands.appInstance.cdUser.updateConnectedUser(user);
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/UserPingBackHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */