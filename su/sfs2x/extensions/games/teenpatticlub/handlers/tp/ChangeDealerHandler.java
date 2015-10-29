/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
/*    */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChangeDealerHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 25 */     Appmethods.showLog("ChangeDealerHandler");
/*    */     
/* 27 */     String player = sender.getName();
/* 28 */     Room room = null;
/* 29 */     room = sender.getLastJoinedRoom();
/* 30 */     GameBean gameBean = Appmethods.getGameBean(room.getName());
/*    */     
/* 32 */     if (gameBean != null)
/*    */     {
/*    */ 
/* 35 */       int dealerId = params.getInt("dealerId").intValue();
/* 36 */       float dealerCost = params.getFloat("dealerCost").floatValue();
/*    */       
/* 38 */       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/* 39 */       ISFSObject sfso = new SFSObject();
/* 40 */       sfso.putUtfString("player", player);
/* 41 */       if (gameBean.getGameRoundBean() != null)
/*    */       {
/* 43 */         PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
/* 44 */         if ((prBean != null) && (gameBean.isStarted()) && (!gameBean.isGameGenerating()))
/*    */         {
/* 46 */           if (pBean.getInplay() > dealerCost)
/*    */           {
/*    */ 
/* 49 */             Commands.appInstance.proxy.changeDealer(player, dealerId, dealerCost, gameBean.getGameID());
/*    */             
/*    */ 
/* 52 */             pBean.setInplay(pBean.getInplay() - dealerCost);
/*    */             
/* 54 */             sfso.putUtfString("comment", "success");
/* 55 */             sfso.putInt("dealerId", dealerId);
/* 56 */             sfso.putFloat("dealerCost", dealerCost);
/* 57 */             send("ChangeDealer", sfso, room.getUserList());
/*    */           }
/*    */           else
/*    */           {
/* 61 */             sfso.putUtfString("comment", "no sufficient chips");
/* 62 */             send("ChangeDealer", sfso, sender);
/*    */           }
/*    */         }
/*    */         else
/*    */         {
/* 67 */           sfso.putUtfString("comment", "you can not change dealer at this time");
/* 68 */           send("ChangeDealer", sfso, sender);
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 73 */         sfso.putUtfString("comment", "you can not change dealer at this time");
/* 74 */         send("ChangeDealer", sfso, sender);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/tp/ChangeDealerHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */