/*    */ package su.sfs2x.extensions.games.teenpatticlub.timers;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.Zone;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
import su.sfs2x.extensions.games.teenpatticlub.classes.ConnectDisconnectUser;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ public class TaskRunner implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 22 */     if (!Commands.appInstance.cdUser.pauseTaskRunner)
/*    */     {
/* 24 */       Iterator<Map.Entry<String, Long>> it = Commands.appInstance.cdUser.connectedUsers.entrySet().iterator();
/* 25 */       List<User> users = new ArrayList();
/* 26 */       User userToRemove = null;
/*    */       
/* 28 */       while (it.hasNext())
/*    */       {
/* 30 */         long currentTime = System.currentTimeMillis();
/*    */         
/* 32 */         Map.Entry<String, Long> pairs = (Map.Entry)it.next();
/*    */         
/*    */ 
/* 35 */         User user = Commands.appInstance.getParentZone().getUserByName((String)pairs.getKey());
/*    */         
/* 37 */         if ((currentTime - pairs.getValue()) / 1000L > 5L)
/*    */         {
/* 39 */           it.remove();
/* 40 */           userToRemove = user;
/*    */         }
/*    */         else {
/* 43 */           users.add(user);
/*    */         }
/*    */       }
/*    */      // || userToRemove.isNpc() == true
/* 47 */       if (userToRemove != null)
/*    */       {
/* 49 */         Commands.appInstance.cdUser.pauseTaskRunner = true;
/* 50 */         Appmethods.showLog("TaskRunner Remove user " + userToRemove.getName());
/* 51 */         Commands.appInstance.cdUser.removeConnectedUser(userToRemove);
/* 52 */         userToRemove = null;
/*    */       }
/*    */       
/*    */ 
/* 56 */       ISFSObject params = new SFSObject();
/* 57 */       params.putUtfString("info", "serverping");
/* 58 */       Commands.appInstance.send("ServerPingUser", params, users);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/timers/TaskRunner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */