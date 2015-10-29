/*    */ package su.sfs2x.extensions.games.teenpatticlub.events;
/*    */ 
/*    */ import com.smartfoxserver.v2.core.ISFSEvent;
/*    */ import com.smartfoxserver.v2.core.SFSEventParam;
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.exceptions.SFSException;
/*    */ import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
/*    */ import java.io.PrintStream;
import su.sfs2x.extensions.games.teenpatticlub.classes.ConnectDisconnectUser;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserJoinEventHandler
/*    */   extends BaseServerEventHandler
/*    */ {
/*    */   public void handleServerEvent(ISFSEvent event)
/*    */     throws SFSException
/*    */   {
/* 23 */     Appmethods.showLog("************ UserJoinEventHandler ************");
/* 24 */     Room room = (Room)event.getParameter(SFSEventParam.ROOM);
/* 25 */     System.out.println(room.getName());
/*    */     
/* 27 */     User user = (User)event.getParameter(SFSEventParam.USER);
/*    */     
/* 29 */     Commands.appInstance.cdUser.addConnectedUser(user);
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/events/UserJoinEventHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */