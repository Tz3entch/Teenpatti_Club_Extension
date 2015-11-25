 package su.sfs2x.extensions.games.teenpatticlub.bsn;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import java.util.Enumeration;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
 import su.sfs2x.extensions.games.teenpatticlub.classes.CalculateWinningAmount;
 import su.sfs2x.extensions.games.teenpatticlub.classes.GameLogic;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class ShowBsn
 {
   public String sideShowWonPlayer(PlayerRoundBean prBean1, PlayerRoundBean prBean2)
   {
     String player = null;
     
     getPlayerRank(prBean1);
     getPlayerRank(prBean2);
     
     if (prBean1.getRank() < prBean2.getRank()) {
       player = prBean1.getPlayerId();
     } else {
       player = prBean2.getPlayerId();
     }
     return player;
   }
   
   public void show(GameBean gameBean, String reason)
   {
     gameBean.getGameRoundBean().setShowCalled(true);
     
     for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
     {
       PlayerRoundBean prBean = (PlayerRoundBean)e.nextElement();
       if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut()))
       {
         getPlayerRank(prBean);
       }
     }
     
     String wonPlayer = null;
     String wonReason = null;
     
     int count;
     for (int i = 1; i <= 6; i++)
     {
       count = getSameRankUsers(i, gameBean);
       if (count > 0)
       {
         if (count == 1)
         {
           wonPlayer = ((PlayerRoundBean)gameBean.getGameRoundBean().getHighRankUsers().get(0)).getPlayerId();
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
         }
         else
         {
           GameLogic gl = new GameLogic();
           
           if (i == 1)
           {
             wonPlayer = gl.compareTrails(gameBean.getGameRoundBean().getHighRankUsers());
             wonReason = "three of a kind with higher card";
           }
           else if (i == 2)
           {
             wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
             wonReason = "pure sequence with higher card";
           }
           else if (i == 3)
           {
             wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
             wonReason = "sequence with higher card";
           }
           else if (i == 4)
           {
             wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
             wonReason = "color with higher card";
           }
           else if (i == 5)
           {
             wonPlayer = gl.comparePair(gameBean.getGameRoundBean().getHighRankUsers());
             wonReason = "pair with higher card";
           }
           else if (i == 6)
           {
             wonPlayer = gl.compareStraightFlush(gameBean.getGameRoundBean().getHighRankUsers());
             wonReason = "higher card";
           }
           
           gl = null;
         }
       }
       
 
       if (wonPlayer != null)
       {
 
         Appmethods.showLog("Won Player " + wonPlayer);
         Appmethods.showLog("Won Reson " + wonReason);
         
         gameBean.getGameRoundBean().setWonPlayer(wonPlayer);
         if ((reason == null) || (reason.equals(""))) {
           gameBean.getGameRoundBean().setWonReason(wonReason); break;
         }
         gameBean.getGameRoundBean().setWonReason(reason);
         break;
       }
     }
     
     if (wonPlayer != null)
     {
 
       CalculateWinningAmount cwa = new CalculateWinningAmount();
       cwa.calculateWinningAmount(gameBean);
       cwa = null;
     }
     
 
     for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
     {
       PlayerRoundBean prBean = (PlayerRoundBean)e.nextElement();
       if ((!prBean.isLeaveTable()) && (!prBean.isSitOut()))
       {
         PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(prBean.getPlayerId());
         pBean.setTotalHands(pBean.getTotalHands() + 1);
         if (gameBean.getGameRoundBean().getWonPlayer().equals(pBean.getPlayerId()))
         {
           float wonamount = gameBean.getGameRoundBean().getWonAmount() - prBean.getTotalBetAmount();
           pBean.getAmounts().add(Float.valueOf(wonamount));
           pBean.setWonHands(pBean.getWonHands() + 1);
           Appmethods.showLog("Player " + pBean.getPlayerId() + " won amount " + gameBean.getGameRoundBean().getWonAmount() + " prBean.getTotalBetAmount()" + prBean.getTotalBetAmount() + " won amount " + wonamount);
         }
         else
         {
           pBean.getAmounts().add(Float.valueOf(-prBean.getTotalBetAmount()));
           Appmethods.showLog("Player " + pBean.getPlayerId() + " Lost amount " + prBean.getTotalBetAmount());
         }
       }
     }
     
 
 
     ISFSObject sfso = new SFSObject();
     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
     
     sfso = gameBean.getGameRoundBean().getSFSObject(sfso);
     sfso = gameBean.getGameRoundBean().getGameWonSFSObject(sfso);
     sfso.putSFSArray("playerBeans", gameBean.getPlayerBeansSFSArray());
     sfso.putUtfStringArray("players", gameBean.getPlayers());
     
     Commands.appInstance.send("GameCompleted", sfso, room.getUserList());
     
 
     Commands.appInstance.proxy.closeGameStatus(gameBean);
     
 
     gameBean.setGameGenerating(true);
     gameBean.startTimer(10, "NextGame");
   }
   
 
 
   public int getSameRankUsers(int rank, GameBean gameBean)
   {
     int count = 0;
     for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
     {
       PlayerRoundBean prBean = (PlayerRoundBean)e.nextElement();
       if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut()))
       {
         if (prBean.getRank() == rank)
         {
           count++;
           gameBean.getGameRoundBean().getHighRankUsers().add(prBean);
         }
       }
     }
     return count;
   }
   
   public void getPlayerRank(PlayerRoundBean prBean)
   {
     GameLogic gl = new GameLogic();
     if (gl.isTrail(prBean.getCards()))
     {
       prBean.setRank(Commands.TRAIL.intValue());
     }
     else if (gl.isStraightFlush(prBean.getCards()))
     {
       prBean.setRank(Commands.STRAIGHT_FLUSH.intValue());
     }
     else if (gl.isStraight(prBean.getCards()))
     {
       prBean.setRank(Commands.STRAIGHT.intValue());
     }
     else if (gl.isFlush(prBean.getCards()))
     {
       prBean.setRank(Commands.FLUSH.intValue());
     }
     else if (gl.isPair(prBean.getCards()))
     {
       prBean.setRank(Commands.TWO_PAIR.intValue());
     }
     else
     {
       prBean.setRank(Commands.HIGH_CARD.intValue());
     }
     gl = null;
   }
 }


