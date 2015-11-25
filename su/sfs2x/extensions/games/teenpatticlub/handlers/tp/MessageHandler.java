 package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;

 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
 
 public class MessageHandler extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     String message = params.getUtfString("message");
     String messageType = params.getUtfString("messageType");
     
     ISFSObject sfso = new SFSObject();
     Room room = null;
     room = sender.getLastJoinedRoom();
     
     sfso.putUtfString("message", message);
     sfso.putUtfString("messageType", messageType);
     sfso.putUtfString("sender", sender.getName());
     
     if (messageType.equals("private"))
     {
       String receiver = params.getUtfString("receiver");
       sfso.putUtfString("receiver", receiver);
       User receiverUser = Commands.appInstance.getApi().getUserByName(receiver);
       
       send("ChatMessage", sfso, receiverUser);
       send("ChatMessage", sfso, sender);
       
 
       Commands.appInstance.proxy.insertChatHistory(sender.getName(), message, messageType, receiver);
     }
     else if (messageType.equals("public"))
     {
       send("ChatMessage", sfso, room.getUserList());
       GameBean gameBean = Appmethods.getGameBean(room.getName());
       
 
       Commands.appInstance.proxy.insertChatHistory(sender.getName(), message, messageType, gameBean.getGameRoundBean().getPlayers().toString());
     }
   }
 }


