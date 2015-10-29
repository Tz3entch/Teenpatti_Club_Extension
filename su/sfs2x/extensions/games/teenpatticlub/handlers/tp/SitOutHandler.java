/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
/*    */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SitOutHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 26 */     Appmethods.showLog("SitOutHandler");
/* 27 */     String player = sender.getName();
/* 28 */     Room room = sender.getLastJoinedRoom();
/* 29 */     GameBean gameBean = Appmethods.getGameBean(room.getName());
/* 30 */     if (gameBean != null)
/*    */     {
/* 32 */       params.putUtfString("player", sender.getName());
/* 33 */       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(sender.getName());
/* 34 */       PlayerRoundBean prBean = null;
/* 35 */       if (gameBean.getGameRoundBean() != null)
/*    */       {
/* 37 */         prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
/*    */         
/* 39 */         if (prBean != null)
/*    */         {
/* 41 */           ActionsBsn aBsn = new ActionsBsn();
/* 42 */           aBsn.sitOut(player, params, gameBean);
/* 43 */           aBsn = null;
/*    */         }
/*    */         
/* 46 */         if (params.getBool("isSitOut").booleanValue())
/*    */         {
/*    */ 
/* 49 */           gameBean.removePlayerAndAddToSpectator(player);
/*    */           
/*    */ 
/* 52 */           Commands.appInstance.proxy.updateUserChips(player, Float.valueOf(pBean.getInplay()));
/*    */           
/* 54 */           params.putUtfStringArray("players", gameBean.getGameRoundBean().getPlayers());
/*    */         }
/*    */         
/*    */ 
/*    */       }
/*    */       else
/*    */       {
/* 61 */         gameBean.removePlayerAndAddToSpectator(player);
/* 62 */         params.putUtfStringArray("players", gameBean.getPlayers());
/*    */       }
/*    */       
/* 65 */       Appmethods.updateGameBeanUpdateLobby(gameBean, room);
/* 66 */       send("SitOut", params, room.getUserList());
/*    */     }
/*    */     else
/*    */     {
/* 70 */       Appmethods.showLog("GameBean Not Found");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/tp/SitOutHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */