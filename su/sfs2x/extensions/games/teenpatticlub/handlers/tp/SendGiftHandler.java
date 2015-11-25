 package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class SendGiftHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("SendGiftHandler");
     
 
     String player = sender.getName();
     Room room = null;
     room = sender.getLastJoinedRoom();
     GameBean gameBean = Appmethods.getGameBean(room.getName());
     
     if (gameBean != null)
     {
       int giftId = params.getInt("giftId").intValue();
       String receiver = params.getUtfString("receiver");
       int qnt = params.getInt("quantity").intValue();
       double unitPrice = params.getDouble("unitPrice").doubleValue();
       String type = params.getUtfString("type");
       
       params.putUtfString("sender", player);
       
       if (type.equals("Transfer"))
       {
         unitPrice = 0.0D;
       }
       float amount = (float)(unitPrice * qnt);
       
       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
       
       if (pBean.getInplay() > amount)
       {
         if (type.equals("Buy"))
         {
 
           pBean.setInplay(pBean.getInplay() - amount);
           
           Commands.appInstance.proxy.cutUserChips(player, amount);
         }
         
 
         Commands.appInstance.proxy.updateGiftItem(player, giftId, qnt, receiver, unitPrice, type, gameBean.getGameID());
         
         params.putUtfString("comment", "success");
         send("SendGift", params, room.getUserList());
       }
       else
       {
         params.putUtfString("comment", "you do not have sufficient amount");
         send("SendGift", params, sender);
       }
     }
     else
     {
       params.putUtfString("comment", "you can not send gift at this time");
       send("SendGift", params, sender);
     }
   }
 }


