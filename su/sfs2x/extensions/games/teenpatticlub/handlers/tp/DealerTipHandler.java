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
/*    */ public class DealerTipHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 24 */     Appmethods.showLog("DealerTipHandler");
/* 25 */     Room room = sender.getLastJoinedRoom();
/* 26 */     float tip = params.getFloat("tip").floatValue();
/* 27 */     String player = sender.getName();
/*    */     
/* 29 */     GameBean gameBean = Appmethods.getGameBean(room.getName());
/*    */     
/* 31 */     if (gameBean != null)
/*    */     {
/* 33 */       ISFSObject sfso = new SFSObject();
/* 34 */       sfso.putUtfString("player", sender.getName());
/* 35 */       sfso.putFloat("tip", tip);
/*    */       
/* 37 */       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/*    */       
/* 39 */       if (gameBean.getGameRoundBean() != null)
/*    */       {
/* 41 */         PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
/*    */         
/* 43 */         if ((prBean != null) && (gameBean.isStarted()) && (!gameBean.isGameGenerating()))
/*    */         {
/* 45 */           if (pBean.getInplay() > tip)
/*    */           {
/* 47 */             Commands.appInstance.proxy.insertDealerTip(player, tip, gameBean.getGameID());
/* 48 */             pBean.setInplay(pBean.getInplay() - tip);
/*    */             
/* 50 */             sfso.putUtfString("comment", "success");
/* 51 */             send("DealerTip", sfso, room.getUserList());
/*    */           }
/*    */           else
/*    */           {
/* 55 */             sfso.putUtfString("comment", "no sufficient chips");
/* 56 */             send("DealerTip", sfso, sender);
/*    */           }
/*    */         }
/*    */         else
/*    */         {
/* 61 */           sfso.putUtfString("comment", "you can not give tip at this time");
/* 62 */           send("DealerTip", sfso, sender);
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 67 */         sfso.putUtfString("comment", "you can not give tip at this time");
/* 68 */         send("DealerTip", sfso, sender);
/*    */       }
/*    */       
/*    */     }
/*    */     else
/*    */     {
/* 74 */       Appmethods.showLog("GameBean not found");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/tp/DealerTipHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */