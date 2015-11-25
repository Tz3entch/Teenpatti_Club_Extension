 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.LobbyBsn;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class GetLobbyRoomHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("********** GetLobbyRoomHandler **********");
     
     LobbyBsn lobbyBsn = new LobbyBsn();
     String roomName = lobbyBsn.getLobbyRoomName();
     lobbyBsn = null;
     Room lobby = Commands.appInstance.getParentZone().getRoomByName("Lobby");
     Room room = Commands.appInstance.getParentZone().getRoomByName(roomName);
     
     Appmethods.leaveRoom(sender, lobby);
     Appmethods.joinRoom(sender, room);
     
     ISFSObject sfso = new SFSObject();
     sfso.putUtfString("roomName", roomName);
     send("GetLobbyRoom", sfso, sender);
   }
 }


