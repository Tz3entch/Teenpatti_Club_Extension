/*    */ package su.sfs2x.extensions.games.teenpatticlub.classes;
/*    */ 
/*    */ import com.smartfoxserver.v2.SmartFoxServer;
/*    */ import com.smartfoxserver.v2.api.ISFSApi;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.util.TaskScheduler;
/*    */ import java.util.HashMap;
/*    */ import java.util.concurrent.ScheduledFuture;
/*    */ import java.util.concurrent.TimeUnit;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.timers.TaskRunner;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConnectDisconnectUser
/*    */ {
/* 23 */   public HashMap<String, Long> connectedUsers = new HashMap();
/*    */   public ScheduledFuture<?> taskHandle;
/* 25 */   public boolean Is_Server_Ping_User_Enabled = true;
/* 26 */   public boolean Is_PING_AUTOPLAY_CHECK = false;
/* 27 */   public Boolean pauseTaskRunner = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addConnectedUser(User user)
/*    */   {
/* 34 */     if (this.Is_Server_Ping_User_Enabled)
/*    */     {
/*    */ 
/* 37 */       if ((this.taskHandle == null) || (this.taskHandle.isDone()) || (this.taskHandle.isCancelled()))
/*    */       {
/* 39 */         if (this.taskHandle != null)
/* 40 */           this.taskHandle.cancel(true);
/* 41 */         this.taskHandle = Commands.appInstance.sfs.getTaskScheduler().scheduleAtFixedRate(new TaskRunner(), 0, 2, TimeUnit.SECONDS);
/*    */       }
/*    */       
/* 44 */       if (this.connectedUsers.containsKey(user.getName()))
/*    */       {
/* 46 */         updateConnectedUser(user);
/*    */       }
/*    */       else
/*    */       {
/* 50 */         Commands.appInstance.trace(new Object[] { "Adding Connect User:" + user.getName() });
/* 51 */         this.connectedUsers.put(user.getName(), Long.valueOf(System.currentTimeMillis()));
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void updateConnectedUser(User user)
/*    */   {
/* 58 */     if (this.Is_Server_Ping_User_Enabled)
/*    */     {
/* 60 */       this.connectedUsers.put(user.getName(), Long.valueOf(System.currentTimeMillis()));
/*    */     }
/*    */   }
/*    */   
/*    */   public void removeConnectedUser(User user)
/*    */   {
/* 66 */     if (this.Is_Server_Ping_User_Enabled)
/*    */     {
/* 68 */       this.pauseTaskRunner = false;
/* 69 */       Commands.appInstance.getApi().disconnectUser(user);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/classes/ConnectDisconnectUser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */