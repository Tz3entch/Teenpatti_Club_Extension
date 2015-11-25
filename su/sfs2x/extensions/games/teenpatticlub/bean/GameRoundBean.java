package su.sfs2x.extensions.games.teenpatticlub.bean;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

public class GameRoundBean {
    private int boot = 0;
    private int challLimit = 0;
    private int potLimit = 0;
    private String dealer;
    private ArrayList<String> players = new ArrayList();
    private ConcurrentHashMap<String, PlayerRoundBean> playerRoundBeans = new ConcurrentHashMap();
    private String turn = "null";
    private Integer potAmount = Integer.valueOf(0);
    private Integer bootAmount = Integer.valueOf(0);
    private ArrayList<Integer> deck = new ArrayList();
    private int handNo = 1;
    private int blindBet = 0;
    private int challBet = 0;

    private int turnNo = 0;
    private int handActivePlayersCount = 0;
    private String roomId = null;
    private ArrayList<PlayerRoundBean> highRankUsers = new ArrayList();
    private String wonPlayer = "null";
    private float wonAmount = 0.0F;
    private boolean isShowCalled = false;
    private String wonReason = "";
    private int handId = 0;

    private boolean isPotLimitExceed = false;


    public GameRoundBean(String dealer, ArrayList<String> players, int boot, int challLimit, int potLimit, String roomId) {
        this.dealer = dealer;
        this.players = players;
        this.boot = boot;
        this.challLimit = challLimit;
        this.potLimit = potLimit;
        this.roomId = roomId;
        setPlayerRoundBeans();

        Appmethods.showLog("GameRoundBean Players:" + players.toString());
    }


    public ISFSObject getSFSObject(ISFSObject sfso) {
        sfso.putUtfString("dealer", this.dealer);
        sfso.putSFSArray("playerRoundBeans", getPlayerRoundBeansSFSArray());
        sfso.putUtfString("turn", this.turn);
        sfso.putInt("potAmount", this.potAmount.intValue());
        sfso.putInt("handNo", this.handNo);
        sfso.putInt("blindBet", this.blindBet);
        sfso.putInt("challBet", this.challBet);
        sfso.putBool("isShowCalled", this.isShowCalled);
        return sfso;
    }


    public ISFSObject getGameWonSFSObject(ISFSObject sfso) {
        sfso.putUtfString("wonPlayer", this.wonPlayer);
        sfso.putFloat("wonAmount", this.wonAmount);
        sfso.putUtfString("wonReason", this.wonReason);
        sfso.putBool("isPotLimitExceed", this.isPotLimitExceed);
        return sfso;
    }


    public SFSArray getPlayerRoundBeansSFSArray() {
        SFSArray playerRoundBeans = new SFSArray();

        for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements(); ) {
            PlayerRoundBean bean = (PlayerRoundBean) e.nextElement();
            playerRoundBeans.addSFSObject(bean.getSFSObject());
        }
        return playerRoundBeans;
    }


    public boolean isPotLimitExceed() {
        return this.isPotLimitExceed;
    }

    public void setPotLimitExceed(boolean isPotLimitExceed) {
        this.isPotLimitExceed = isPotLimitExceed;
    }

    public int getHandId() {
        return this.handId;
    }

    public void setHandId(int handId) {
        this.handId = handId;
    }

    public Integer getBootAmount() {
        return this.bootAmount;
    }

    public void setBootAmount(Integer bootAmount) {
        this.bootAmount = bootAmount;
    }

    public String getWonReason() {
        return this.wonReason;
    }

    public void setWonReason(String wonReason) {
        this.wonReason = wonReason;
    }

    public boolean isShowCalled() {
        return this.isShowCalled;
    }

    public void setShowCalled(boolean isShowCalled) {
        this.isShowCalled = isShowCalled;
    }

    public float getWonAmount() {
        return this.wonAmount;
    }

    public void setWonAmount(float wonAmount) {
        this.wonAmount = Appmethods.setFloatFormat(wonAmount);
    }

    public String getWonPlayer() {
        return this.wonPlayer;
    }

    public void setWonPlayer(String wonPlayer) {
        this.wonPlayer = wonPlayer;
    }

    public ArrayList<PlayerRoundBean> getHighRankUsers() {
        return this.highRankUsers;
    }

    public void setHighRankUsers(ArrayList<PlayerRoundBean> highRankUsers) {
        this.highRankUsers = highRankUsers;
    }

    public int getBlindBet() {
        return this.blindBet;
    }

    public void setBlindBet(int blindBet) {
        this.blindBet = blindBet;
    }

    public int getChallBet() {
        return this.challBet;
    }

    public void setChallBet(int challBet) {
        this.challBet = challBet;
    }

    public int getHandNo() {
        return this.handNo;
    }

    public void setHandNo(int handNo) {
        this.handNo = handNo;
    }

    public ArrayList<String> getPlayers() {
        return this.players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public ConcurrentHashMap<String, PlayerRoundBean> getPlayerRoundBeans() {
        return this.playerRoundBeans;
    }

    public void setPlayerRoundBeans(ConcurrentHashMap<String, PlayerRoundBean> playerRoundBeans) {
        this.playerRoundBeans = playerRoundBeans;
    }

    public String getDealer() {
        return this.dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public Integer getPotAmount() {
        return this.potAmount;
    }

    public void setPotAmount(Integer potAmount) {
        this.potAmount = potAmount;
    }

    public int getBoot() {
        return this.boot;
    }

    public void setBoot(int boot) {
        this.boot = boot;
    }

    public int getChallLimit() {
        return this.challLimit;
    }

    public void setChallLimit(int challLimit) {
        this.challLimit = challLimit;
    }

    public int getPotLimit() {
        return this.potLimit;
    }

    public void setPotLimit(int potLimit) {
        this.potLimit = potLimit;
    }

    public String getTurn() {
        return this.turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }


    public void removePlayer(String name) {
        int pos = -1;
        pos = this.players.indexOf(name);
        if (pos != -1) {
            Appmethods.showLog("GRB Before Remove " + this.players.toString());
            this.players.remove(pos);
            this.players.add(pos, "null");
            Appmethods.showLog("GRB After Remove " + this.players.toString());
        }
    }

    private void setPlayerRoundBeans() {
        for (int i = 0; i < this.players.size(); i++) {
            if (!((String) this.players.get(i)).equals("null")) {
                PlayerRoundBean playerRoundBean = new PlayerRoundBean((String) this.players.get(i));
                this.playerRoundBeans.put((String) this.players.get(i), playerRoundBean);
            }
        }
    }

    public String getPlayerCardsString() {
        String cards = null;
        for (int i = 0; i < this.players.size(); i++) {


            if (!((String) this.players.get(i)).equals("null")) {
                PlayerRoundBean playerRoundBean = (PlayerRoundBean) this.playerRoundBeans.get(this.players.get(i));
                if (cards != null) {
                    cards = cards + "*" + playerRoundBean.getCards().toString();

                } else {
                    cards = playerRoundBean.getCards().toString();

                }


            } else if (cards != null) {
                cards = cards + "*null";
            } else {
                cards = "null";
            }
        }
        return cards;
    }


    public void setTurnPlayer() {
        Appmethods.showLog("setTurnPlayer");
        Appmethods.showLog("Players " + this.players);
        if (this.turn.equals("null")) {
            this.handActivePlayersCount = getActivePlayersCount();
            int pos = this.players.indexOf(this.dealer);

            Appmethods.showLog("Pos :" + pos);

            for (int i = 1; i < this.players.size(); i++) {
                int no = pos + i;
                if (no >= this.players.size()) {
                    no %= this.players.size();
                }
                Appmethods.showLog("Pos No:" + no);

                if (!((String) this.players.get(no)).equals("null")) {
                    this.turn = ((String) this.players.get(no));
                    break;
                }
            }
        } else {
            Appmethods.showLog("turn " + this.turn);
            int pos = this.players.indexOf(this.turn);
            Appmethods.showLog("1Pos :" + pos);

            for (int i = 1; i < this.players.size(); i++) {
                int no = pos + i;
                if (no >= this.players.size()) {
                    no %= this.players.size();
                }
                Appmethods.showLog("Pos No:" + no);
                if (!((String) this.players.get(no)).equals("null")) {
                    PlayerRoundBean prBean = (PlayerRoundBean) getPlayerRoundBeans().get(this.players.get(no));
                    if (prBean != null) {
                        if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut())) {
                            this.turn = ((String) this.players.get(no));
                            this.turnNo += 1;
                            break;
                        }
                    }
                }
            }

            Appmethods.showLog("turn " + this.turn);

            if (this.turnNo == this.handActivePlayersCount) {
                this.handActivePlayersCount = getActivePlayersCount();
                this.turnNo = 0;
                this.handNo += 1;
            }
        }

        setPlayerBits();
    }


    private void setPlayerBits() {
        if (getActivePlayersCount() == 2) {
            for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements(); ) {
                PlayerRoundBean bean = (PlayerRoundBean) e.nextElement();
                if ((!bean.isLeaveTable()) && (!bean.isPack()) && (!bean.isSitOut())) {
                    bean.setShow(true);
                }
            }
        }


        if ((isAllPlayersSeen()) && (getActivePlayersCount() >= 3) && (getSideShowNotSelectedPlayersCount())) {
            for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements(); ) {
                PlayerRoundBean bean = (PlayerRoundBean) e.nextElement();
                PlayerBean pBean = (PlayerBean) Appmethods.getGameBean(this.roomId).getPlayerBeenList().get(bean.getPlayerId());
                if ((!bean.isLeaveTable()) && (!bean.isPack()) && (!bean.isSitOut()) && (!bean.isSideShowSelected()) && (pBean.getInplay() >= this.challBet)) {
                    bean.setSideShow(true);
                }

            }

        } else {
            for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements(); ) {
                PlayerRoundBean bean = (PlayerRoundBean) e.nextElement();
                bean.setSideShow(false);
            }
        }
    }

    private boolean getSideShowNotSelectedPlayersCount() {
        int count = 0;
        for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements(); ) {
            PlayerRoundBean bean = (PlayerRoundBean) e.nextElement();
            PlayerBean pBean = (PlayerBean) Appmethods.getGameBean(this.roomId).getPlayerBeenList().get(bean.getPlayerId());
            if ((!bean.isLeaveTable()) && (!bean.isPack()) && (!bean.isSitOut()) && (!bean.isSideShowSelected()) && (pBean.getInplay() >= this.challBet)) {
                count++;
            }
        }
        if (count >= 2)
            return true;
        return false;
    }

    private boolean isAllPlayersSeen() {
        for (Enumeration<PlayerRoundBean> e = getPlayerRoundBeans().elements(); e.hasMoreElements(); ) {
            PlayerRoundBean bean = (PlayerRoundBean) e.nextElement();
            if (!bean.isSeen())
                return false;
        }
        return true;
    }

    public int getActivePlayersCount() {
        int count = 0;

        for (Enumeration<PlayerRoundBean> e = this.playerRoundBeans.elements(); e.hasMoreElements(); ) {
            PlayerRoundBean prBean = (PlayerRoundBean) e.nextElement();
            if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut())) {
                count++;
            }
        }
        return count;
    }

    private void shuffelCards() {
        ArrayList<Integer> cards = new ArrayList();
        Random randomGenerator = new Random();
        for (int idx = 1; idx <= 52; idx++) {
            cards.add(Integer.valueOf(idx));
        }


        for (int idx = 1; idx <= 52; idx++) {
            int randomInt = randomGenerator.nextInt(cards.size());
            this.deck.add((Integer) cards.get(randomInt));
            cards.remove(randomInt);
        }
    }

    private void distributeCards() {
        Integer shufflerPosition = Integer.valueOf(this.players.indexOf(this.dealer));

        for (int i = 0; i < 3; i++) {
            for (int j = 1; j <= this.players.size(); j++) {
                int agno;

                if (j + shufflerPosition.intValue() >= this.players.size()) {
                    agno = (j + shufflerPosition.intValue()) % this.players.size();
                } else {
                    agno = j + shufflerPosition.intValue();
                }

                if (!((String) this.players.get(agno)).equals("null")) {
                    PlayerRoundBean prBean = (PlayerRoundBean) getPlayerRoundBeans().get(this.players.get(agno));
                    prBean.getCards().add((Integer) this.deck.get(0));
                    this.deck.remove(0);
                }
            }
        }
    }

    private void cutBootAmount() {
        for (Enumeration<PlayerRoundBean> e = this.playerRoundBeans.elements(); e.hasMoreElements(); ) {
            PlayerRoundBean prBean = (PlayerRoundBean) e.nextElement();
            Appmethods.cutHandAmount(this.roomId, prBean, Integer.valueOf(this.boot));
            this.potAmount = Integer.valueOf(this.potAmount.intValue() + this.boot);
            this.bootAmount = Integer.valueOf(this.bootAmount.intValue() + this.boot);
            this.handId += 1;
            float commission = Commands.appInstance.proxy.getPlayerCommission(prBean.getPlayerId());

            Commands.appInstance.proxy.insertUserAction(this.roomId, prBean.getPlayerId(), this.boot, "Boot", commission);
        }
        this.blindBet = this.boot;
        this.challBet = (this.boot * 2);
    }


    public void gameInit() {
        setTurnPlayer();

        shuffelCards();

        distributeCards();

        cutBootAmount();
    }
}


