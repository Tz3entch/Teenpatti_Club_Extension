/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers;
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
/*    */ 
/*    */ public class UpdateProfileHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 20 */     Appmethods.showLog("UpdateProfileHandler");
/*    */     
/* 22 */     String player = sender.getName();
/* 23 */     String mobile = params.getUtfString("mobile");
/* 24 */     String email = params.getUtfString("email");
/* 25 */     String password = params.getUtfString("password");
/*    */     
/* 27 */     Commands.appInstance.proxy.updateProfile(player, mobile, email, password);
/* 28 */     send("UpdateProfile", params, sender);
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/UpdateProfileHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */