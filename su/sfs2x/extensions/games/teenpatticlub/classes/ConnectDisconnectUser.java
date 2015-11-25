 package su.sfs2x.extensions.games.teenpatticlub.classes;

 import com.smartfoxserver.v2.entities.User;
 import java.util.HashMap;
 import java.util.concurrent.ScheduledFuture;
 import java.util.concurrent.TimeUnit;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.timers.TaskRunner;

 public class ConnectDisconnectUser
 {
   public HashMap<String, Long> connectedUsers = new HashMap();
   public ScheduledFuture<?> taskHandle;
   public boolean Is_Server_Ping_User_Enabled = true;
   public boolean Is_PING_AUTOPLAY_CHECK = false;
   public Boolean pauseTaskRunner = false;
   
 
 
 
   public void addConnectedUser(User user)
   {
     if (this.Is_Server_Ping_User_Enabled)
     {
 
       if ((this.taskHandle == null) || (this.taskHandle.isDone()) || (this.taskHandle.isCancelled()))
       {
         if (this.taskHandle != null)
           this.taskHandle.cancel(true);
         this.taskHandle = Commands.appInstance.sfs.getTaskScheduler().scheduleAtFixedRate(new TaskRunner(), 0, 2, TimeUnit.SECONDS);
       }
       
       if (this.connectedUsers.containsKey(user.getName()))
       {
         updateConnectedUser(user);
       }
       else
       {
         Commands.appInstance.trace(new Object[] { "Adding Connect User:" + user.getName() });
         this.connectedUsers.put(user.getName(), Long.valueOf(System.currentTimeMillis()));
       }
     }
   }
   
   public void updateConnectedUser(User user)
   {
     if (this.Is_Server_Ping_User_Enabled)
     {
       this.connectedUsers.put(user.getName(), Long.valueOf(System.currentTimeMillis()));
     }
   }
   
   public void removeConnectedUser(User user)
   {
     if (this.Is_Server_Ping_User_Enabled)
     {
       this.pauseTaskRunner = false;
       Commands.appInstance.getApi().disconnectUser(user);
     }
   }
 }


