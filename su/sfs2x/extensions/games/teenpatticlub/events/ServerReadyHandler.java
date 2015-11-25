package su.sfs2x.extensions.games.teenpatticlub.events;

import su.sfs2x.extensions.games.teenpatticlub.npcs.NPCManager;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class ServerReadyHandler extends BaseServerEventHandler{
	
    public void handleServerEvent(ISFSEvent event) throws SFSException 
    {
        if(event.getType().equals(SFSEventType.SERVER_READY))
        {
        	Appmethods.showLog("**************ServerReadyHandler*************");
        	Appmethods.showLog("3patticlub Server Started");
            NPCManager npm = new NPCManager();
            npm.init();
        }

    }

}
