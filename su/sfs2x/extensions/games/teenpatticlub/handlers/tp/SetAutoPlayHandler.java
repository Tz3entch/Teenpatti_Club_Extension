/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
/*    */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetAutoPlayHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 23 */     Appmethods.showLog("SetAutoPlayHandler");
/* 24 */     Room room = null;
/* 25 */     room = sender.getLastJoinedRoom();
/* 26 */     GameBean gameBean = Appmethods.getGameBean(room.getName());
/*    */     
/* 28 */     if (gameBean != null)
/*    */     {
/* 30 */       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(sender.getName());
/* 31 */       pBean.setAutoPlay(params.getBool("autoPlay").booleanValue());
/* 32 */       send("SetAutoPlay", params, sender);
/*    */     }
/*    */     else
/*    */     {
/* 36 */       Appmethods.showLog("GameBean Not Found");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/tp/SetAutoPlayHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */