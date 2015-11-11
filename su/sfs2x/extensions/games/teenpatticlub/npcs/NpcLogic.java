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
import java.util.concurrent.ConcurrentHashMap;
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

    public NpcLogic(GameBean gameBean) {
        this.gameBean = gameBean;
        player = gameBean.getGameRoundBean().getTurn();
        room = Appmethods.getRoomByName(gameBean.getRoomId());
        user = room.getUserByName(player);
        prb = gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
    }

    public void performNpcTurn() {
        if (user.isNpc()) {
            User wonUser = findWonUser(gameBean);
            Appmethods.showLog("WON USER: "+ wonUser.getName());
            if (!wonUser.isNpc()) {
                switch (rand.nextInt(2)) {
                    case 0: pack();
                        break;
                    case 1: blind();
                }
            } else if (user.getName().equals(wonUser.getName())) {
                if (!prb.isSeen()) {
                    switch (rand.nextInt(3)) {
                        case 0: showTry();
                            break;
                        case 1: chaal();
                            break;
                        case 2: blind();

                    }
                } else {
                    switch (rand.nextInt(2)) {
                        case 0: showTry();
                            break;
                        case 1: chaal();
                    }
                }
            } else {
                if (!prb.isSeen()) {
                    switch (rand.nextInt(4)) {
                        case 0: showTry();
                            break;
                        case 1: chaal();
                            break;
                        case 2: blind();
                            break;
                        case 3: pack();

                    }
                } else {
                    switch (rand.nextInt(3)) {
                        case 0: showTry();
                            break;
                        case 1: chaal();
                            break;
                        case 2: pack();
                    }
                }
            }
        }
    }

    private void showTry() {
            if (activePlayers() == 2) {
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
        if (!prb.isSeen()) {
            seen();
        }
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
            seen();
        }
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

    private int activePlayers(){
        int result=0;
        for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*     */     {
/*  51 */       PlayerRoundBean prBean = (PlayerRoundBean)e.nextElement();
/*  52 */       if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut()))
/*     */       {
/*  54 */         result++;
/*     */       }
/*     */     }
        return  result;
    }

    public User findWonUser(GameBean gameBean){
        for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*     */     {
/*  51 */       PlayerRoundBean prBean = (PlayerRoundBean)e.nextElement();
/*  52 */       if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut()))
/*     */       {
/*  54 */         showBsn.getPlayerRank(prBean);
/*     */       }
/*     */     }
               String wonPlayer = null;
/*  59 */     String wonReason = null;
        int count;
/*  62 */     for (int i = 1; i <= 6; i++)
/*     */     {
/*  64 */       count = showBsn.getSameRankUsers(i, gameBean);
/*  65 */       if (count > 0)
/*     */       {
/*  67 */         if (count == 1)
/*     */         {
/*  69 */           wonPlayer = ((PlayerRoundBean)gameBean.getGameRoundBean().getHighRankUsers().get(0)).getPlayerId();
/*  70 */
/*     */         }
/*     */         else
/*     */         {
/*  86 */           GameLogic gl = new GameLogic();
/*     */
/*  88 */           if (i == 1)
/*     */           {
/*  90 */             wonPlayer = gl.compareTrails(gameBean.getGameRoundBean().getHighRankUsers());
/*  91 */             wonReason = "three of a kind with higher card";
/*     */           }
/*  93 */           else if (i == 2)
/*     */           {
/*  95 */             wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
/*  96 */             wonReason = "pure sequence with higher card";
/*     */           }
/*  98 */           else if (i == 3)
/*     */           {
/* 100 */             wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
/* 101 */             wonReason = "sequence with higher card";
/*     */           }
/* 103 */           else if (i == 4)
/*     */           {
/* 105 */             wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
/* 106 */             wonReason = "color with higher card";
/*     */           }
/* 108 */           else if (i == 5)
/*     */           {
/* 110 */             wonPlayer = gl.comparePair(gameBean.getGameRoundBean().getHighRankUsers());
/* 111 */             wonReason = "pair with higher card";
/*     */           }
/* 113 */           else if (i == 6)
/*     */           {
/* 115 */             wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
/* 116 */             wonReason = "higher card";
/*     */           }
/*     */
/* 119 */
/*     */         }
/*     */       }

        }
        gameBean.getGameRoundBean().setHighRankUsers(new ArrayList<PlayerRoundBean>());
        return  Commands.appInstance.getApi().getUserByName(wonPlayer);
    }

}
