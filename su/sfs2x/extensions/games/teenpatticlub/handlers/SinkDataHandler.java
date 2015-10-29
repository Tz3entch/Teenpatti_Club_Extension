/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.Zone;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
/*    */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SinkDataHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 23 */     Appmethods.showLog("*********** SinkDataHandler **********");
/* 24 */     String roomId = params.getUtfString("roomId");
/* 25 */     Room room = Commands.appInstance.getParentZone().getRoomByName(roomId);
/* 26 */     if (room != null)
/*    */     {
/* 28 */       GameBean gameBean = Appmethods.getGameBean(roomId);
/* 29 */       if (gameBean != null)
/*    */       {
/* 31 */         User user = Commands.appInstance.getParentZone().getUserByName(sender.getName());
/* 32 */         Appmethods.joinRoom(user, room);
/* 33 */         PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(sender.getName());
/* 34 */         if (pBean != null)
/*    */         {
/* 36 */           pBean.setActive(true);
/* 37 */           send("SinkData", Appmethods.getSinkDataSFSObject(gameBean), sender);
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 42 */         trace(new Object[] { "GameBean not found " + roomId });
/*    */       }
/*    */       
/*    */     }
/*    */     else
/*    */     {
/* 48 */       trace(new Object[] { "Room not found " + roomId });
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/SinkDataHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */