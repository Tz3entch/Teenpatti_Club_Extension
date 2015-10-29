/*    */ package su.sfs2x.extensions.games.teenpatticlub.events;
/*    */ 
/*    */ import com.smartfoxserver.v2.core.ISFSEvent;
/*    */ import com.smartfoxserver.v2.core.SFSEventParam;
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.exceptions.SFSException;
/*    */ import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
import su.sfs2x.extensions.games.teenpatticlub.bsn.DisconnectUserBsn;
import su.sfs2x.extensions.games.teenpatticlub.classes.ConnectDisconnectUser;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ public class UserDisconnectedEventHandler
/*    */   extends BaseServerEventHandler
/*    */ {
/*    */   public void handleServerEvent(ISFSEvent event) throws SFSException
/*    */   {
/* 23 */     User user = (User)event.getParameter(SFSEventParam.USER);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 28 */     Commands.appInstance.proxy.updateLogoutSession(user.getName());
/*    */     
/*    */ 
/* 31 */     Room room = null;
/*    */     
/* 33 */     List<Room> joinedRooms = (List)event.getParameter(SFSEventParam.JOINED_ROOMS);
/* 34 */     if (joinedRooms.size() > 0)
/*    */     {
/* 36 */       for (int i = 0; i < joinedRooms.size(); i++)
/*    */       {
/* 38 */         room = (Room)joinedRooms.get(i);
/*    */         
/* 40 */         Appmethods.showLog("Room Name " + room.getName());
/*    */         
/* 42 */         if (room.getGroupId().equals("GameGroup"))
/*    */         {
/* 44 */           DisconnectUserBsn duserBsn = new DisconnectUserBsn();
/* 45 */           duserBsn.disconnectUser(user, room);
/* 46 */           duserBsn = null;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 51 */     if ((Commands.appInstance.cdUser.connectedUsers.size() > 0) && (Commands.appInstance.cdUser.connectedUsers.containsKey(user.getName()))) {
/* 52 */       Commands.appInstance.cdUser.connectedUsers.remove(user.getName());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/events/UserDisconnectedEventHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */