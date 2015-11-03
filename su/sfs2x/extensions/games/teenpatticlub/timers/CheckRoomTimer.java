package su.sfs2x.extensions.games.teenpatticlub.timers;

import su.sfs2x.extensions.games.teenpatticlub.events.ServerReadyHandler;
import su.sfs2x.extensions.games.teenpatticlub.npcs.NPCManager;

import java.util.TimerTask;

public class CheckRoomTimer extends TimerTask{
    NPCManager npm;
   public CheckRoomTimer(NPCManager npm) {
       this.npm=npm;
   }
    public void run() {
        npm.checkRooms();
    }
}