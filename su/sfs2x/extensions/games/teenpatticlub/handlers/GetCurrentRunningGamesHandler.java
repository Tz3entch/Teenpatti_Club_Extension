/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Calendar;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetCurrentRunningGamesHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 27 */     ArrayList<String> games = Commands.appInstance.proxy.getCurrentRunningGames(sender.getName());
/* 28 */     ISFSObject sfso = new SFSObject();
/* 29 */     sfso.putUtfStringArray("tables", games);
/* 30 */     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 31 */     Calendar cal = Calendar.getInstance();
/* 32 */     sfso.putUtfString("serverTime", dateFormat.format(cal.getTime()));
/* 33 */     send("GetCurrentRunningGames", sfso, sender);
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/GetCurrentRunningGamesHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */