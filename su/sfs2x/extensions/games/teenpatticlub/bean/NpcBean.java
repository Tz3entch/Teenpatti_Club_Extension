package su.sfs2x.extensions.games.teenpatticlub.bean;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;

import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

public class NpcBean {
    private String playerId;
    private float inPlay;
    private Integer timerBank = 1;
    private boolean isActive = true;
    private String avatar = "";
    private boolean isAutoPlay = false;
    private float commission = 0.0F;


    private boolean isUsingExtraTime = false;
    private int timeUpCount = 0;

    private float startInplay = 0.0F;
    private ArrayList<Float> amounts = new ArrayList();
    private int totalHands = 0;
    private int wonHands = 0;

    public NpcBean(String player) {
        setPlayerId(player);
        this.avatar = Commands.appInstance.proxy.getPlayerAvatar(player);
        this.commission = Commands.appInstance.proxy.getPlayerCommission(player);
    }

    public ISFSObject getSFSObject() {
        ISFSObject sfso = new SFSObject();
        sfso.putUtfString("playerId", this.playerId);
        sfso.putFloat("inPlay", this.inPlay);
        sfso.putUtfString("avatar", this.avatar);
        sfso.putBool("isAutoPlay", this.isAutoPlay);
        return sfso;
    }

    public ISFSObject getSessionSfsObject() {
        float total = 0.0F;
        ISFSObject sfso = new SFSObject();
        for (Float amount : this.amounts) {
            total += amount;
        }

        sfso.putFloat("won_or_lost_amount", total);
        sfso.putInt("totalHands", this.totalHands);
        sfso.putInt("wonHands", this.wonHands);

        return sfso;
    }

    public float getCommission() {
        return this.commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public boolean isAutoPlay() {
        return this.isAutoPlay;
    }

    public void setAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    public float getStartInplay() {
        return this.startInplay;
    }

    public void setStartInplay(float startInplay) {
        this.startInplay = startInplay;
    }

    public ArrayList<Float> getAmounts() {
        return this.amounts;
    }

    public void setAmounts(ArrayList<Float> amounts) {
        this.amounts = amounts;
    }

    public int getTotalHands() {
        return this.totalHands;
    }

    public void setTotalHands(int totalHands) {
        this.totalHands = totalHands;
    }

    public int getWonHands() {
        return this.wonHands;
    }

    public void setWonHands(int wonHands) {
        this.wonHands = wonHands;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getTimeUpCount() {
        return this.timeUpCount;
    }

    public void setTimeUpCount(int timeUpCount) {
        this.timeUpCount = timeUpCount;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isUsingExtraTime() {
        return this.isUsingExtraTime;
    }

    public void setUsingExtraTime(boolean isUsingExtraTime) {
        this.isUsingExtraTime = isUsingExtraTime;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public float getInplay() {
        return this.inPlay;
    }

    public void setInplay(float inplay) {
        this.inPlay = Appmethods.setFloatFormat(inplay);
    }

    public Integer getTimerBank() {
        return this.timerBank;
    }

    public void setTimerBank(Integer timerBank) {
        this.timerBank = timerBank;
    }

}
