/*    */ package su.sfs2x.extensions.games.teenpatticlub.events;
/*    */ 
/*    */ import com.smartfoxserver.v2.core.ISFSEvent;
/*    */ import com.smartfoxserver.v2.core.SFSEventParam;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.exceptions.SFSException;
/*    */ import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogoutEventHandler
/*    */   extends BaseServerEventHandler
/*    */ {
/*    */   public void handleServerEvent(ISFSEvent event)
/*    */     throws SFSException
/*    */   {
/* 24 */     User user = (User)event.getParameter(SFSEventParam.USER);
/* 25 */     String player = user.getName();
/* 26 */     Appmethods.showLog(player + " Disconnected");
/*    */     
/* 28 */     Commands.appInstance.proxy.updateLogoutSession(user.getName());
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/events/LogoutEventHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */