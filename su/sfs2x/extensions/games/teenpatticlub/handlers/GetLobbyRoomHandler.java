/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.Zone;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import su.sfs2x.extensions.games.teenpatticlub.bsn.LobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetLobbyRoomHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 24 */     Appmethods.showLog("********** GetLobbyRoomHandler **********");
/*    */     
/* 26 */     LobbyBsn lobbyBsn = new LobbyBsn();
/* 27 */     String roomName = lobbyBsn.getLobbyRoomName();
/* 28 */     lobbyBsn = null;
/* 29 */     Room lobby = Commands.appInstance.getParentZone().getRoomByName("Lobby");
/* 30 */     Room room = Commands.appInstance.getParentZone().getRoomByName(roomName);
/*    */     
/* 32 */     Appmethods.leaveRoom(sender, lobby);
/* 33 */     Appmethods.joinRoom(sender, room);
/*    */     
/* 35 */     ISFSObject sfso = new SFSObject();
/* 36 */     sfso.putUtfString("roomName", roomName);
/* 37 */     send("GetLobbyRoom", sfso, sender);
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/GetLobbyRoomHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */