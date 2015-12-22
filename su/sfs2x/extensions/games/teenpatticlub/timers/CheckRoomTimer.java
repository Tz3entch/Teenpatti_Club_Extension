package su.sfs2x.extensions.games.teenpatticlub.timers;

import su.sfs2x.extensions.games.teenpatticlub.npcs.NPCManager;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.TimerTask;

public class CheckRoomTimer implements Runnable {
    NPCManager npm;

    public CheckRoomTimer(NPCManager npm) {
        this.npm = npm;
    }

    public void run() {
        try {
            npm.checkRooms();
        } catch (Exception e) {
            e.printStackTrace();
            Appmethods.showLog("EXCEPTION IN RUN METHOD");
            Appmethods.showLog("LOCALIZED MESSAGE: " + e.getLocalizedMessage());
            Appmethods.showLog("MESSAGE: " + e.getMessage());
            Appmethods.showLog("E TO STRING: " + e.toString());

            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter( writer );
            e.printStackTrace(printWriter);
            printWriter.flush();

            String stackTrace = writer.toString();
            Appmethods.showLog("STACKTRACE: " + stackTrace);
            npm.checkRooms();
        }
    }
}
