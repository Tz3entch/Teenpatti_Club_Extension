package su.sfs2x.extensions.games.teenpatticlub.events;

import su.sfs2x.extensions.games.teenpatticlub.bsn.LobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class ServerReadyHandler extends BaseServerEventHandler{
	
    public void handleServerEvent(ISFSEvent event) throws SFSException 
    {
        if(event.getType().equals(SFSEventType.SERVER_READY))
        {
        	Appmethods.showLog("**************ServerReadyHandler*************");
        	Appmethods.showLog("3patticlub Server Started");

        	//String roomId = "Lobby";


        	
        	// Create one or more NPC users...


            //getApi().login(socketLessSession, npcUser, null, null, null);
            //Room room = Commands.appInstance.getParentZone().getRoomByName(roomId);
             LobbyBsn lobbyBsn = new LobbyBsn();
/* 27 */     String roomName = lobbyBsn.getLobbyRoomName();
/* 28 */     lobbyBsn = null;
/* 29 */     //Room lobby = Commands.appInstance.getParentZone().getRoomByName("Lobby");
/* 30 */     Room room = Commands.appInstance.getParentZone().getRoomByName(roomName);

            String robotName = "NPC0001";
            User npcUser = getApi().createNPC(robotName, getParentExtension().getParentZone(), true);
            ISession socketLessSession = Commands.appInstance.sfs.getSessionManager().createConnectionlessSession();
            getApi().login(socketLessSession, "NPC0001", "123456", "abc", null);

            getApi().joinRoom(npcUser, room);
            npcUser.setConnected(true);
            socketLessSession.setLoggedIn(true);


            Appmethods.showLog("NPC >> "+npcUser);
        }

    }

}
