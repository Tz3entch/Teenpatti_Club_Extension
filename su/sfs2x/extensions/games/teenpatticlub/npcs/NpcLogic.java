package su.sfs2x.extensions.games.teenpatticlub.npcs;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NpcLogic {

    private GameBean gameBean;
    private String player;
    private Room room ;
    private User user;
    private PlayerRoundBean prb;
    private ActionsBsn absn = new ActionsBsn();
    private Random rand = new Random();
    private ISFSObject sfso1;

    public NpcLogic(GameBean gameBean) {
        this.gameBean = gameBean;
        player = gameBean.getGameRoundBean().getTurn();
        room = Appmethods.getRoomByName(gameBean.getRoomId());
        user = room.getUserByName(player);
        prb = gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
    }

    public void performNpcTurn() {
        if (user.isNpc()) {
          chaal();
        }

    }

    private void  chaal() {
            if (!prb.isSeen()) {
                seen();
            }

            sfso1 = new SFSObject();
            sfso1.putUtfString("command", "Chall");
            sfso1.putInt("amount", gameBean.getGameRoundBean().getChallBet());
            try {
                TimeUnit.SECONDS.sleep(rand.nextInt(10)+3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            absn.chall(player, sfso1, gameBean);

    }

    private void blind() {
            if (!prb.isSeen()) {
                sfso1 = new SFSObject();
                sfso1.putUtfString("command", "Blind");
                sfso1.putInt("amount", gameBean.getGameRoundBean().getBlindBet());
                try {
                    TimeUnit.SECONDS.sleep(rand.nextInt(10) + 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                absn.blind(player, sfso1, gameBean);
            }
    }

    private void seen() {
            sfso1 = new SFSObject();
            sfso1.putUtfString("command", "Seen");
            try {
                TimeUnit.SECONDS.sleep(rand.nextInt(10)+3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            absn.seen(player, sfso1, gameBean);

    }

    private void pack() {
        sfso1 = new SFSObject();
        sfso1.putUtfString("command", "Pack");
        try {
            TimeUnit.SECONDS.sleep(rand.nextInt(10)+3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        absn.pack(player, sfso1, gameBean);

    }

    private void show() {
        if (!prb.isSeen()) {
            sfso1 = new SFSObject();
            sfso1.putUtfString("command", "Show");
            sfso1.putInt("amount", gameBean.getGameRoundBean().getBlindBet());
            try {
                TimeUnit.SECONDS.sleep(rand.nextInt(10) + 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            absn.show(player, sfso1, gameBean);
        }
    }
}
