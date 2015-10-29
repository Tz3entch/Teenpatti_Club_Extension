/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
/*    */ 
/*    */ import com.smartfoxserver.v2.api.ISFSApi;
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
/*    */ import java.util.ArrayList;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ public class MessageHandler extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 21 */     String message = params.getUtfString("message");
/* 22 */     String messageType = params.getUtfString("messageType");
/*    */     
/* 24 */     ISFSObject sfso = new SFSObject();
/* 25 */     Room room = null;
/* 26 */     room = sender.getLastJoinedRoom();
/*    */     
/* 28 */     sfso.putUtfString("message", message);
/* 29 */     sfso.putUtfString("messageType", messageType);
/* 30 */     sfso.putUtfString("sender", sender.getName());
/*    */     
/* 32 */     if (messageType.equals("private"))
/*    */     {
/* 34 */       String receiver = params.getUtfString("receiver");
/* 35 */       sfso.putUtfString("receiver", receiver);
/* 36 */       User receiverUser = Commands.appInstance.getApi().getUserByName(receiver);
/*    */       
/* 38 */       send("ChatMessage", sfso, receiverUser);
/* 39 */       send("ChatMessage", sfso, sender);
/*    */       
/*    */ 
/* 42 */       Commands.appInstance.proxy.insertChatHistory(sender.getName(), message, messageType, receiver);
/*    */     }
/* 44 */     else if (messageType.equals("public"))
/*    */     {
/* 46 */       send("ChatMessage", sfso, room.getUserList());
/* 47 */       GameBean gameBean = Appmethods.getGameBean(room.getName());
/*    */       
/*    */ 
/* 50 */       Commands.appInstance.proxy.insertChatHistory(sender.getName(), message, messageType, gameBean.getGameRoundBean().getPlayers().toString());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/tp/MessageHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */