package su.sfs2x.extensions.games.teenpatticlub.npcs;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
import su.sfs2x.extensions.games.teenpatticlub.bsn.ShowBsn;
import su.sfs2x.extensions.games.teenpatticlub.classes.GameLogic;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NpcLogic {

    private GameBean gameBean;
    private String player;
    private Room room ;
    private User user;
    private PlayerRoundBean prb;
    private ActionsBsn absn = new ActionsBsn();
    private Random rand = new Random();
    private ShowBsn showBsn = new ShowBsn();
    private ISFSObject sfso1;
    private String wonReason;
    private int rank;
    public NpcLogic(GameBean gameBean) {
        this.gameBean = gameBean;
        player = gameBean.getGameRoundBean().getTurn();
        room = Appmethods.getRoomByName(gameBean.getRoomId());
        user = room.getUserByName(player);
        prb = gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
    }

    public void performNpcTurn() {
        if (user!=null&&user.isNpc()) {
            User wonUser = findWonUser(gameBean);
            Appmethods.showLog("BEST COMBO: "+ wonUser.getName()+" REASON: " + wonReason);
            if (!wonUser.isNpc()) {
                switch (rand.nextInt(4)) {
                    case 0: pack();
                        break;
                    case 1: blind();
                        break;
                    case 2: showPack();
                        break;
                    case 3: showBlind();
                }
            } else if (user.getName().equals(wonUser.getName())) {
                if (!prb.isSeen()) {
                    switch (rand.nextInt(2)) {
                        case 0: showChaal();
                            break;
                        case 1: blind();

                    }
                } else {
                    showChaal();
                }
            } else {
                if (!prb.isSeen()) {
                    switch (rand.nextInt(3)) {
                        case 0: showChaal();
                            break;
                        case 1: blind();
                            break;
                        case 2: pack();

                    }
                } else {
                    switch (rand.nextInt(3)) {
                        case 0: showChaal();
                            break;
                        case 1: chaal();
                            break;
                        case 2: pack();
                    }
                }
            }
        }
    }
    private void npcWait(){
        try {
            TimeUnit.SECONDS.sleep(rand.nextInt(10)+3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void showBlind() {
        if (activePlayers() == 2) {
            show();
        } else {
            blind();
        }
    }

    private void showPack() {
        if (activePlayers() == 2) {
            show();
        } else {
            pack();
        }
    }

    private void showChaal() {
        if ( rank == 6 &&(activePlayers() == 2 && gameBean.getGameRoundBean().getHandNo() > rand.nextInt(3) + 3) ) {
            show();
        } else if (rank == 5 && (activePlayers() == 2 && gameBean.getGameRoundBean().getHandNo() > rand.nextInt(1) + 4) ) {
            show();
        } else if ( (rank > 1 && rank < 5)&&(activePlayers() == 2 && gameBean.getGameRoundBean().getHandNo() > rand.nextInt(4) + 5) ) {
            show();
        } else {
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
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(new DelayedChaal(player, sfso1, gameBean), 13+rand.nextInt(13), TimeUnit.SECONDS);
            executor.shutdown();
    }

    private void blind() {
            if (!prb.isSeen()) {
                sfso1 = new SFSObject();
                sfso1.putUtfString("command", "Blind");
                sfso1.putInt("amount", gameBean.getGameRoundBean().getBlindBet());
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.schedule(new DelayedBlind(player, sfso1, gameBean), 13+rand.nextInt(13), TimeUnit.SECONDS);
                executor.shutdown();
            }
    }

    private void seen() {
            sfso1 = new SFSObject();
            sfso1.putUtfString("command", "Seen");
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(new DelayedSeen(player, sfso1, gameBean), 8+rand.nextInt(5), TimeUnit.SECONDS);
            executor.shutdown();


    }

    private void pack() {
        if (!prb.isSeen()) {
            seen();
        }
        sfso1 = new SFSObject();
        sfso1.putUtfString("command", "Pack");
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(new DelayedPack(player, sfso1, gameBean), 13+rand.nextInt(13), TimeUnit.SECONDS);
        executor.shutdown();

    }

    private void show() {
        if (!prb.isSeen()) {
            seen();
        }
            sfso1 = new SFSObject();
            sfso1.putUtfString("command", "Show");
            sfso1.putInt("amount", gameBean.getGameRoundBean().getBlindBet());
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(new DelayedShow(player, sfso1, gameBean), 13+rand.nextInt(13), TimeUnit.SECONDS);
            executor.shutdown();

    }

    private int activePlayers(){
        int result=0;
        for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
     {
       PlayerRoundBean prBean = (PlayerRoundBean)e.nextElement();
       if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut()))
       {
         result++;
       }
     }
        return  result;
    }

    public User findWonUser(GameBean gameBean) {
        for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements(); ) {
            PlayerRoundBean prBean = (PlayerRoundBean) e.nextElement();
            if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut())) {
                showBsn.getPlayerRank(prBean);
            }
        }
        String wonPlayer = null;
        String wonReason = null;
        int rank=0;
        int count;
        for (int i = 1; i <= 6; i++) {
            count = showBsn.getSameRankUsers(i, gameBean);
            if (count > 0) {
                if (count == 1) {
                    wonPlayer = ((PlayerRoundBean) gameBean.getGameRoundBean().getHighRankUsers().get(0)).getPlayerId();
                    if (i == 1) {
                        wonReason = "three of a kind";
                    } else if (i == 2) {
                        wonReason = "pure sequence";
                    } else if (i == 3) {
                        wonReason = "sequence";
                    } else if (i == 4) {
                        wonReason = "color";
                    } else if (i == 5) {
                        wonReason = "pair";
                    } else if (i == 6) {
                        wonReason = "high card";
                    }
                } else {
                    GameLogic gl = new GameLogic();

                    if (i == 1) {
                        wonPlayer = gl.compareTrails(gameBean.getGameRoundBean().getHighRankUsers());
                        wonReason = "three of a kind with higher card";
                    } else if (i == 2) {
                        wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
                        wonReason = "pure sequence with higher card";
                    } else if (i == 3) {
                        wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
                        wonReason = "sequence with higher card";
                    } else if (i == 4) {
                        wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
                        wonReason = "color with higher card";
                    } else if (i == 5) {
                        wonPlayer = gl.comparePair(gameBean.getGameRoundBean().getHighRankUsers());
                        wonReason = "pair with higher card";
                    } else if (i == 6) {
                        wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
                        wonReason = "higher card";
                    }

                }
                rank=i;
                break;
            }
        }
        gameBean.getGameRoundBean().setHighRankUsers(new ArrayList<PlayerRoundBean>());
        this.wonReason = wonReason;
        this.rank = rank;
        return Commands.appInstance.getApi().getUserByName(wonPlayer);
    }

}
