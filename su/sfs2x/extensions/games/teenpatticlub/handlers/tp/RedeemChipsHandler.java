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
/*    */ 
/*    */ public class RedeemChipsHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 20 */     Appmethods.showLog("RedeemChipsHandler");
/* 21 */     float amount = params.getFloat("amount").floatValue();
/* 22 */     String player = sender.getName();
/* 23 */     Commands.appInstance.proxy.redeemChips(player, amount);
/*    */     
/* 25 */     params.putUtfString("status", "completed");
/* 26 */     send("ReedemChips", params, sender);
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/tp/RedeemChipsHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */