 package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
 
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class RedeemChipsHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("RedeemChipsHandler");
     float amount = params.getFloat("amount").floatValue();
     String player = sender.getName();
     Commands.appInstance.proxy.redeemChips(player, amount);
     
     params.putUtfString("status", "completed");
     send("ReedemChips", params, sender);
   }
 }


