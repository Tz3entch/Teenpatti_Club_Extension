package su.sfs2x.extensions.games.teenpatticlub.events;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.JoinUserBsn;
import su.sfs2x.extensions.games.teenpatticlub.bsn.LobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.handlers.JoinUserHadler;
import su.sfs2x.extensions.games.teenpatticlub.npcs.NPCManager;
import su.sfs2x.extensions.games.teenpatticlub.timers.CheckRoomTimer;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import java.util.List;
import java.util.Random;
import java.util.Timer;

public class ServerReadyHandler extends BaseServerEventHandler{
	
    public void handleServerEvent(ISFSEvent event) throws SFSException 
    {
        if(event.getType().equals(SFSEventType.SERVER_READY))
        {
        	Appmethods.showLog("**************ServerReadyHandler*************");
        	Appmethods.showLog("3patticlub Server Started");
//            NPCManager npm = new NPCManager();
//            npm.init();
//            Timer timer = new Timer();
//            timer.schedule(new CheckRoomTimer(npm), 30000, 60000);
        }

    }

}
//TODO Timeupcount