package su.sfs2x.extensions.games.teenpatticlub.npcs;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;

public class DelayedPack implements Runnable {
    String player;
    ISFSObject sfso;
    GameBean gameBean;
    ActionsBsn absn = new ActionsBsn();
    public DelayedPack (String player, ISFSObject sfso, GameBean gameBean) {
        this.player=player;
        this.sfso=sfso;
        this.gameBean=gameBean;
    }
    @Override
    public void run() {
        absn.pack(player,sfso,gameBean);
    }
}