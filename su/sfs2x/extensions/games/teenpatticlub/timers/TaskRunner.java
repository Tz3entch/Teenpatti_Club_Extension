 package su.sfs2x.extensions.games.teenpatticlub.timers;
 
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
 
 public class TaskRunner implements Runnable
 {
   public void run()
   {
     if (!Commands.appInstance.cdUser.pauseTaskRunner)
     {
       Iterator<Map.Entry<String, Long>> it = Commands.appInstance.cdUser.connectedUsers.entrySet().iterator();
       List<User> users = new ArrayList();
       User userToRemove = null;
       
       while (it.hasNext())
       {
         long currentTime = System.currentTimeMillis();
         
         Map.Entry<String, Long> pairs = (Map.Entry)it.next();
         
 
         User user = Commands.appInstance.getParentZone().getUserByName((String)pairs.getKey());
         
         if ((currentTime - pairs.getValue()) / 1000L > 5L)
         {
           it.remove();
           userToRemove = user;
         }
         else {
           users.add(user);
         }
       }

       if (userToRemove != null&&!userToRemove.isNpc())
       {
         Commands.appInstance.cdUser.pauseTaskRunner = true;
         Appmethods.showLog("TaskRunner Remove user " + userToRemove.getName());
         Commands.appInstance.cdUser.removeConnectedUser(userToRemove);
         userToRemove = null;
       }
       
 
       ISFSObject params = new SFSObject();
       params.putUtfString("info", "serverping");
       Commands.appInstance.send("ServerPingUser", params, users);
     }
   }
 }


