 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import java.text.DateFormat;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Calendar;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;

 public class GetCurrentRunningGamesHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     ArrayList<String> games = Commands.appInstance.proxy.getCurrentRunningGames(sender.getName());
     ISFSObject sfso = new SFSObject();
     sfso.putUtfStringArray("tables", games);
     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     Calendar cal = Calendar.getInstance();
     sfso.putUtfString("serverTime", dateFormat.format(cal.getTime()));
     send("GetCurrentRunningGames", sfso, sender);
   }
 }


