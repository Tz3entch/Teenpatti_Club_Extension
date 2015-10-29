/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CheckDeviceIdHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 19 */     Appmethods.showLog("CheckDeviceIdHandler");
/*    */     
/* 21 */     String id = params.getUtfString("deviceId");
/* 22 */     String player = sender.getName();
/*    */     
/* 24 */     ISFSObject sfso = Commands.appInstance.proxy.checkDeviceId(player, id);
/* 25 */     send("CheckDeviceId", sfso, sender);
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/tp/CheckDeviceIdHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */