package su.sfs2x.extensions.games.teenpatticlub.bsn;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.npcs.NpcLogic;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

public class ActionsBsn {
    public void seen(String player, ISFSObject params, GameBean gameBean) {
        Room room = Appmethods.getRoomByName(gameBean.getRoomId());
        PlayerRoundBean prBean = (PlayerRoundBean) gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
        prBean.setSeen(true);
        prBean.setLastAction("Seen");
        params.putUtfString("player", player);
        Commands.appInstance.send("Action", params, room.getUserList());
    }


    public void pack(String player, ISFSObject params, GameBean gameBean) {
        checkIsUsingExtraTime(gameBean, player);


        Room room = Appmethods.getRoomByName(gameBean.getRoomId());
        PlayerRoundBean prBean = (PlayerRoundBean) gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
        prBean.setPack(true);
        prBean.setLastAction("Pack");
        params.putUtfString("player", player);
        params.putUtfString("command", "Pack");
        Commands.appInstance.send("Action", params, room.getUserList());

        gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
        PlayerBean pBean = (PlayerBean) gameBean.getPlayerBeenList().get(player);

        Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean.getPlayerId(), 0, "Pack", pBean.getCommission());


        if (gameBean.getGameRoundBean().getActivePlayersCount() >= 2) {

            callNextTurn(gameBean, room);

        } else {
            ShowBsn showBsn = new ShowBsn();
            showBsn.show(gameBean, "Oppnent packed, You win");
            showBsn = null;
        }
    }


    public void leaveTable(String player, ISFSObject params, GameBean gameBean) {
        Room room = Appmethods.getRoomByName(gameBean.getRoomId());
        PlayerRoundBean prBean = (PlayerRoundBean) gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
        if (prBean != null) {
            prBean.setLeaveTable(true);
            prBean.setLastAction("LeaveTable");
        }

        User sender = Commands.appInstance.getParentZone().getUserByName(player);

        params = gameBean.getSFSObject();
        params = gameBean.getGameRoundBean().getSFSObject(params);
        params.putUtfString("player", player);
        params.putBool("isSpectator", false);


        PlayerBean pBean = (PlayerBean) gameBean.getPlayerBeenList().get(player);
        Commands.appInstance.proxy.updateUserChips(player, Float.valueOf(pBean.getInplay()));


        if ((gameBean.getGameRoundBean().getActivePlayersCount() >= 2) && (!gameBean.getGameRoundBean().isShowCalled())) {
            if (gameBean.getGameRoundBean().getTurn().equals(player)) {

                callNextTurn(gameBean, room);
            }
        } else if (gameBean.getGameRoundBean().getActivePlayersCount() == 1) {
            if (!gameBean.isGameGenerating()) {

                ShowBsn showBsn = new ShowBsn();
                showBsn.show(gameBean, "Oppnent left, You win");
                showBsn = null;
            }
        }


        gameBean.removePlayer(player);
        gameBean.removePlayerBean(player);

        Commands.appInstance.send("LeaveTable", params, sender);
        Commands.appInstance.send("LeaveTable", params, room.getUserList());


        Appmethods.updateGameBeanUpdateLobby(gameBean, room);
    }


    public void blind(String player, ISFSObject params, GameBean gameBean) {
        Room room = Appmethods.getRoomByName(gameBean.getRoomId());
        PlayerRoundBean prBean = (PlayerRoundBean) gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
        PlayerBean pBean = (PlayerBean) gameBean.getPlayerBeenList().get(player);
        int blindAmount = params.getInt("amount").intValue();

        if (pBean.getInplay() >= blindAmount) {

            checkIsUsingExtraTime(gameBean, player);

            if (blindAmount > gameBean.getGameRoundBean().getBlindBet()) {
                gameBean.getGameRoundBean().setBlindBet(blindAmount);
                if (blindAmount * 2 < gameBean.getGameRoundBean().getChallLimit()) {
                    gameBean.getGameRoundBean().setChallBet(blindAmount * 2);
                } else
                    gameBean.getGameRoundBean().setChallBet(gameBean.getGameRoundBean().getChallLimit());
            }
            prBean.getHandAmounts().add(blindAmount);
            prBean.setTotalBetAmount(prBean.getTotalBetAmount() + blindAmount);
            pBean.setInplay(pBean.getInplay() - blindAmount);
            gameBean.getGameRoundBean().setPotAmount(gameBean.getGameRoundBean().getPotAmount() + blindAmount);

            params.putUtfString("player", player);
            Commands.appInstance.send("Action", params, room.getUserList());


            gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
            Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean.getPlayerId(), blindAmount, "Blind", pBean.getCommission());


            if (gameBean.getGameRoundBean().getPotAmount() >= gameBean.getGameRoundBean().getPotLimit()) {
                gameBean.getGameRoundBean().setPotLimitExceed(true);

                ShowBsn showBsn = new ShowBsn();
                showBsn.show(gameBean, null);
                showBsn = null;

            } else {
                callNextTurn(gameBean, room);
            }
        } else {
            Appmethods.showLog("############");
            Appmethods.showLog("Going Negative Inplay blind Player " + pBean.getPlayerId() + " Inplay " + pBean.getInplay() + " amount " + blindAmount);
            Appmethods.showLog("############");
        }
    }


    public void chall(String player, ISFSObject params, GameBean gameBean) {
        Room room = Appmethods.getRoomByName(gameBean.getRoomId());
        PlayerRoundBean prBean = (PlayerRoundBean) gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
        PlayerBean pBean = (PlayerBean) gameBean.getPlayerBeenList().get(player);
        int challAmount = params.getInt("amount").intValue();

        if (pBean.getInplay() >= challAmount) {

            checkIsUsingExtraTime(gameBean, player);

            if (challAmount > gameBean.getGameRoundBean().getChallBet()) {
                gameBean.getGameRoundBean().setChallBet(challAmount);
                if (challAmount / 2 < gameBean.getGameRoundBean().getChallLimit())
                    gameBean.getGameRoundBean().setBlindBet(challAmount / 2);
            }
            prBean.getHandAmounts().add(Integer.valueOf(challAmount));
            prBean.setTotalBetAmount(prBean.getTotalBetAmount() + challAmount);
            pBean.setInplay(pBean.getInplay() - challAmount);
            gameBean.getGameRoundBean().setPotAmount(Integer.valueOf(gameBean.getGameRoundBean().getPotAmount().intValue() + challAmount));

            params.putUtfString("player", player);
            Commands.appInstance.send("Action", params, room.getUserList());


            gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
            Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean.getPlayerId(), challAmount, "Chaal", pBean.getCommission());


            if (gameBean.getGameRoundBean().getPotAmount().intValue() >= gameBean.getGameRoundBean().getPotLimit()) {
                gameBean.getGameRoundBean().setPotLimitExceed(true);

                ShowBsn showBsn = new ShowBsn();
                showBsn.show(gameBean, null);
                showBsn = null;

            } else {
                callNextTurn(gameBean, room);
            }
        } else {
            Appmethods.showLog("############");
            Appmethods.showLog("Going Negative Inplay chaal Player " + pBean.getPlayerId() + " Inplay " + pBean.getInplay() + " amount " + challAmount);
            Appmethods.showLog("############");
        }
    }


    public void show(String player, ISFSObject params, GameBean gameBean) {
        Room room = Appmethods.getRoomByName(gameBean.getRoomId());
        PlayerRoundBean prBean = (PlayerRoundBean) gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
        PlayerBean pBean = (PlayerBean) gameBean.getPlayerBeenList().get(player);
        int showAmount = params.getInt("amount");

        if (pBean.getInplay() >= showAmount) {

            checkIsUsingExtraTime(gameBean, player);
            prBean.getHandAmounts().add(Integer.valueOf(showAmount));
            prBean.setTotalBetAmount(prBean.getTotalBetAmount() + showAmount);
            pBean.setInplay(pBean.getInplay() - showAmount);
            gameBean.getGameRoundBean().setPotAmount(Integer.valueOf(gameBean.getGameRoundBean().getPotAmount().intValue() + showAmount));
            prBean.setShowCards(true);


            params.putUtfString("player", player);
            Commands.appInstance.send("Action", params, room.getUserList());


            gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
            Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean.getPlayerId(), showAmount, "Show", pBean.getCommission());


            ShowBsn showBsn = new ShowBsn();
            showBsn.show(gameBean, null);
            showBsn = null;
        } else {
            Appmethods.showLog("############");
            Appmethods.showLog("Going Negative Inplay show Player " + pBean.getPlayerId() + " Inplay " + pBean.getInplay() + " amount " + showAmount);
            Appmethods.showLog("############");
        }
    }


    public void sideShowRequest(String player, ISFSObject params, GameBean gameBean) {
        int pos = gameBean.getGameRoundBean().getPlayers().indexOf(player);
        User user = null;
        for (int i = 1; i < gameBean.getGameRoundBean().getPlayers().size(); i++) {
            int no = pos - i;
            if (no < 0) {
                no += gameBean.getGameRoundBean().getPlayers().size();
            }
            Appmethods.showLog("Pos No:" + no);
            String prePlayer = (String) gameBean.getGameRoundBean().getPlayers().get(no);
            if (!prePlayer.equals("null")) {
                PlayerRoundBean prBean = (PlayerRoundBean) gameBean.getGameRoundBean().getPlayerRoundBeans().get(prePlayer);
                if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSideShowSelected()) && (!prBean.isSitOut())) {
                    user = Commands.appInstance.getParentZone().getUserByName(prePlayer);
                    break;
                }
            }
        }
        if (user != null) {
            params.putUtfString("player", player);
            Commands.appInstance.send("Action", params, user);
        }
    }

    public void sideShowResponse(String player, ISFSObject params, GameBean gameBean) {
        ISFSObject sfso = new SFSObject();

        Room room = Appmethods.getRoomByName(gameBean.getRoomId());
        if (params.getUtfString("status").equals("Accepted")) {

            String requestedPlayer = params.getUtfString("player");
            String respondPlayer = player;
            PlayerRoundBean prBean1 = (PlayerRoundBean) gameBean.getGameRoundBean().getPlayerRoundBeans().get(requestedPlayer);
            PlayerBean pBean = (PlayerBean) gameBean.getPlayerBeenList().get(requestedPlayer);
            PlayerRoundBean prBean2 = (PlayerRoundBean) gameBean.getGameRoundBean().getPlayerRoundBeans().get(respondPlayer);
            int amount = gameBean.getGameRoundBean().getChallBet();

            if (pBean.getInplay() >= amount) {
                prBean1.setSideShowCalled(true);
                prBean1.setSideShowSelected(true);
                prBean2.setSideShowSelected(true);
                ShowBsn showBsn = new ShowBsn();
                String wonPlayer = showBsn.sideShowWonPlayer(prBean1, prBean2);


                if (wonPlayer.equals(requestedPlayer)) {
                    prBean2.setPack(true);
                } else {
                    prBean1.setPack(true);
                }

                prBean1.getHandAmounts().add(Integer.valueOf(amount));
                prBean1.setTotalBetAmount(prBean1.getTotalBetAmount() + amount);
                pBean.setInplay(pBean.getInplay() - amount);
                gameBean.getGameRoundBean().setPotAmount(Integer.valueOf(gameBean.getGameRoundBean().getPotAmount().intValue() + amount));

                sfso.putUtfString("player", requestedPlayer);
                sfso.putUtfString("winner", wonPlayer);
                sfso.putUtfString("acceptedPlayer", player);
                sfso.putInt("amount", amount);
                sfso.putUtfString("commnad", "SideShow");
                sfso.putUtfString("status", params.getUtfString("status"));

                Commands.appInstance.send("Action", sfso, room.getUserList());


                gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
                Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean1.getPlayerId(), amount, "SideShow", pBean.getCommission());


                callNextTurn(gameBean, room);
            } else {
                Appmethods.showLog("############");
                Appmethods.showLog("Going Negative Inplay sideShowResponse Player " + pBean.getPlayerId() + " Inplay " + pBean.getInplay() + " amount " + amount);
                Appmethods.showLog("############");
            }

        } else {
            String receiver = params.getUtfString("player");
            User user = Commands.appInstance.getParentZone().getUserByName(receiver);
            sfso.putUtfString("rejectedPlayer", player);
            sfso.putUtfString("commnad", "SideShow");
            sfso.putUtfString("status", params.getUtfString("status"));
            Commands.appInstance.send("Action", sfso, user);
        }
    }

    public void sitOut(String player, ISFSObject params, GameBean gameBean) {
        Room room = Appmethods.getRoomByName(gameBean.getRoomId());

        if (gameBean.getGameRoundBean().getTurn().equals(player)) {


            checkIsUsingExtraTime(gameBean, player);
            gameBean.getGameRoundBean().setHandId(gameBean.getGameRoundBean().getHandId() + 1);
        }


        PlayerRoundBean prBean = (PlayerRoundBean) gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);

        prBean.setSitOut(true);
        prBean.setLastAction("SitOut");
        params.putUtfString("player", player);
        params.putUtfString("command", "SitOut");

        Commands.appInstance.send("Action", params, room.getUserList());
        PlayerBean pBean = (PlayerBean) gameBean.getPlayerBeenList().get(player);

        Commands.appInstance.proxy.insertUserAction(gameBean.getRoomId(), prBean.getPlayerId(), 0, "SitOut", pBean.getCommission());

        if (gameBean.getGameRoundBean().getTurn().equals(player)) {

            if (gameBean.getGameRoundBean().getActivePlayersCount() >= 2) {

                callNextTurn(gameBean, room);
            } else if (!gameBean.getGameRoundBean().isShowCalled()) {

                ShowBsn showBsn = new ShowBsn();
                showBsn.show(gameBean, "Oppnent sit out, You win");
                showBsn = null;
            }


        } else if (gameBean.getGameRoundBean().getActivePlayersCount() == 1) {
            if (!gameBean.getGameRoundBean().isShowCalled()) {

                ShowBsn showBsn = new ShowBsn();
                showBsn.show(gameBean, "Oppnent sit out, You win");
                showBsn = null;
            }
        }
    }


    private void callNextTurn(GameBean gameBean, Room room) {
        gameBean.getGameRoundBean().setTurnPlayer();
        checkIsSeenRoundCompleted(gameBean, room, gameBean.getGameRoundBean().getTurn());


        ISFSObject sfso = new SFSObject();
        sfso = gameBean.getGameRoundBean().getSFSObject(sfso);
        sfso.putInt("turnTime", 60);
        Commands.appInstance.send("Turn", sfso, room.getUserList());


        gameBean.startTimer(61, "Turn");


        NpcLogic npcl = new NpcLogic(gameBean, 0);
        npcl.performNpcTurn();

    }

    private void checkIsSeenRoundCompleted(GameBean gameBean, Room room, String player) {
        if (gameBean.getGameRoundBean().getHandNo() == 5) {
            PlayerRoundBean prBean = (PlayerRoundBean) gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
            if (!prBean.isSeen()) {
                prBean.setSeen(true);
                ISFSObject sfso = new SFSObject();
                sfso.putUtfString("player", player);
                sfso.putUtfString("command", "Seen");
                Commands.appInstance.send("Action", sfso, room.getUserList());
            }
        }
    }


    private void checkIsUsingExtraTime(GameBean gameBean, String player) {
        PlayerBean pBean = (PlayerBean) gameBean.getPlayerBeenList().get(player);
        if (pBean.isUsingExtraTime()) {

            int remainingSeconds = gameBean.getRemainingSeconds();
            pBean.setTimerBank(Integer.valueOf(remainingSeconds));
            pBean.setUsingExtraTime(false);
            gameBean.stopTimer();
        } else {
            gameBean.stopTimer();
        }
    }
}


