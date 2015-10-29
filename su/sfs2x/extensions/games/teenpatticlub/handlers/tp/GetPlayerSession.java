/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.entities.data.SFSObject;
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
/*    */ public class GetPlayerSession
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 24 */     String player = params.getUtfString("player");
/* 25 */     Room room = sender.getLastJoinedRoom();
/* 26 */     GameBean gameBean = Appmethods.getGameBean(room.getName());
/* 27 */     ISFSObject sfso = new SFSObject();
/*    */     
/* 29 */     if (gameBean != null)
/*    */     {
/* 31 */       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/*    */       
/* 33 */       sfso = pBean.getSessionSfsObject();
/* 34 */       sfso.putUtfString("player", player);
/*    */       
/* 36 */       send("GetUserSession", sfso, sender);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/tp/GetPlayerSession.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */