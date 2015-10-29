/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
/*    */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SendGiftHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 22 */     Appmethods.showLog("SendGiftHandler");
/*    */     
/*    */ 
/* 25 */     String player = sender.getName();
/* 26 */     Room room = null;
/* 27 */     room = sender.getLastJoinedRoom();
/* 28 */     GameBean gameBean = Appmethods.getGameBean(room.getName());
/*    */     
/* 30 */     if (gameBean != null)
/*    */     {
/* 32 */       int giftId = params.getInt("giftId").intValue();
/* 33 */       String receiver = params.getUtfString("receiver");
/* 34 */       int qnt = params.getInt("quantity").intValue();
/* 35 */       double unitPrice = params.getDouble("unitPrice").doubleValue();
/* 36 */       String type = params.getUtfString("type");
/*    */       
/* 38 */       params.putUtfString("sender", player);
/*    */       
/* 40 */       if (type.equals("Transfer"))
/*    */       {
/* 42 */         unitPrice = 0.0D;
/*    */       }
/* 44 */       float amount = (float)(unitPrice * qnt);
/*    */       
/* 46 */       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/*    */       
/* 48 */       if (pBean.getInplay() > amount)
/*    */       {
/* 50 */         if (type.equals("Buy"))
/*    */         {
/*    */ 
/* 53 */           pBean.setInplay(pBean.getInplay() - amount);
/*    */           
/* 55 */           Commands.appInstance.proxy.cutUserChips(player, amount);
/*    */         }
/*    */         
/*    */ 
/* 59 */         Commands.appInstance.proxy.updateGiftItem(player, giftId, qnt, receiver, unitPrice, type, gameBean.getGameID());
/*    */         
/* 61 */         params.putUtfString("comment", "success");
/* 62 */         send("SendGift", params, room.getUserList());
/*    */       }
/*    */       else
/*    */       {
/* 66 */         params.putUtfString("comment", "you do not have sufficient amount");
/* 67 */         send("SendGift", params, sender);
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 72 */       params.putUtfString("comment", "you can not send gift at this time");
/* 73 */       send("SendGift", params, sender);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/tp/SendGiftHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */