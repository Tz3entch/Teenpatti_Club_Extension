/*     */ package su.sfs2x.extensions.games.teenpatticlub.bsn;
/*     */ 
/*     */ import com.smartfoxserver.v2.entities.Room;
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.classes.CalculateWinningAmount;
import su.sfs2x.extensions.games.teenpatticlub.classes.GameLogic;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShowBsn
/*     */ {
/*     */   public String sideShowWonPlayer(PlayerRoundBean prBean1, PlayerRoundBean prBean2)
/*     */   {
/*  32 */     String player = null;
/*     */     
/*  34 */     getPlayerRank(prBean1);
/*  35 */     getPlayerRank(prBean2);
/*     */     
/*  37 */     if (prBean1.getRank() < prBean2.getRank()) {
/*  38 */       player = prBean1.getPlayerId();
/*     */     } else {
/*  40 */       player = prBean2.getPlayerId();
/*     */     }
/*  42 */     return player;
/*     */   }
/*     */   
/*     */   public void show(GameBean gameBean, String reason)
/*     */   {
/*  47 */     gameBean.getGameRoundBean().setShowCalled(true);
/*     */     
/*  49 */     for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*     */     {
/*  51 */       PlayerRoundBean prBean = (PlayerRoundBean)e.nextElement();
/*  52 */       if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut()))
/*     */       {
/*  54 */         getPlayerRank(prBean);
/*     */       }
/*     */     }
/*     */     
/*  58 */     String wonPlayer = null;
/*  59 */     String wonReason = null;
/*     */     
/*  61 */     int count = 0;
/*  62 */     for (int i = 1; i <= 6; i++)
/*     */     {
/*  64 */       count = getSameRankUsers(i, gameBean);
/*  65 */       if (count > 0)
/*     */       {
/*  67 */         if (count == 1)
/*     */         {
/*  69 */           wonPlayer = ((PlayerRoundBean)gameBean.getGameRoundBean().getHighRankUsers().get(0)).getPlayerId();
/*  70 */           if (i == 1) {
/*  71 */             wonReason = "three of a kind";
/*  72 */           } else if (i == 2) {
/*  73 */             wonReason = "pure sequence";
/*  74 */           } else if (i == 3) {
/*  75 */             wonReason = "sequence";
/*  76 */           } else if (i == 4) {
/*  77 */             wonReason = "color";
/*  78 */           } else if (i == 5) {
/*  79 */             wonReason = "pair";
/*  80 */           } else if (i == 6) {
/*  81 */             wonReason = "high card";
/*     */           }
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
/* 119 */           gl = null;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 124 */       if (wonPlayer != null)
/*     */       {
/*     */ 
/* 127 */         Appmethods.showLog("Won Player " + wonPlayer);
/* 128 */         Appmethods.showLog("Won Reson " + wonReason);
/*     */         
/* 130 */         gameBean.getGameRoundBean().setWonPlayer(wonPlayer);
/* 131 */         if ((reason == null) || (reason.equals(""))) {
/* 132 */           gameBean.getGameRoundBean().setWonReason(wonReason); break;
/*     */         }
/* 134 */         gameBean.getGameRoundBean().setWonReason(reason);
/* 135 */         break;
/*     */       }
/*     */     }
/*     */     
/* 139 */     if (wonPlayer != null)
/*     */     {
/*     */ 
/* 142 */       CalculateWinningAmount cwa = new CalculateWinningAmount();
/* 143 */       cwa.calculateWinningAmount(gameBean);
/* 144 */       cwa = null;
/*     */     }
/*     */     
/*     */ 
/* 148 */     for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*     */     {
/* 150 */       PlayerRoundBean prBean = (PlayerRoundBean)e.nextElement();
/* 151 */       if ((!prBean.isLeaveTable()) && (!prBean.isSitOut()))
/*     */       {
/* 153 */         PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(prBean.getPlayerId());
/* 154 */         pBean.setTotalHands(pBean.getTotalHands() + 1);
/* 155 */         if (gameBean.getGameRoundBean().getWonPlayer().equals(pBean.getPlayerId()))
/*     */         {
/* 157 */           float wonamount = gameBean.getGameRoundBean().getWonAmount() - prBean.getTotalBetAmount();
/* 158 */           pBean.getAmounts().add(Float.valueOf(wonamount));
/* 159 */           pBean.setWonHands(pBean.getWonHands() + 1);
/* 160 */           Appmethods.showLog("Player " + pBean.getPlayerId() + " won amount " + gameBean.getGameRoundBean().getWonAmount() + " prBean.getTotalBetAmount()" + prBean.getTotalBetAmount() + " won amount " + wonamount);
/*     */         }
/*     */         else
/*     */         {
/* 164 */           pBean.getAmounts().add(Float.valueOf(-prBean.getTotalBetAmount()));
/* 165 */           Appmethods.showLog("Player " + pBean.getPlayerId() + " Lost amount " + prBean.getTotalBetAmount());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 172 */     ISFSObject sfso = new SFSObject();
/* 173 */     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/*     */     
/* 175 */     sfso = gameBean.getGameRoundBean().getSFSObject(sfso);
/* 176 */     sfso = gameBean.getGameRoundBean().getGameWonSFSObject(sfso);
/* 177 */     sfso.putSFSArray("playerBeans", gameBean.getPlayerBeansSFSArray());
/* 178 */     sfso.putUtfStringArray("players", gameBean.getPlayers());
/*     */     
/* 180 */     Commands.appInstance.send("GameCompleted", sfso, room.getUserList());
/*     */     
/*     */ 
/* 183 */     Commands.appInstance.proxy.closeGameStatus(gameBean);
/*     */     
/*     */ 
/* 186 */     gameBean.setGameGenerating(true);
/* 187 */     gameBean.startTimer(10, "NextGame");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int getSameRankUsers(int rank, GameBean gameBean)
/*     */   {
/* 194 */     int count = 0;
/* 195 */     for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*     */     {
/* 197 */       PlayerRoundBean prBean = (PlayerRoundBean)e.nextElement();
/* 198 */       if ((!prBean.isLeaveTable()) && (!prBean.isPack()) && (!prBean.isSitOut()))
/*     */       {
/* 200 */         if (prBean.getRank() == rank)
/*     */         {
/* 202 */           count++;
/* 203 */           gameBean.getGameRoundBean().getHighRankUsers().add(prBean);
/*     */         }
/*     */       }
/*     */     }
/* 207 */     return count;
/*     */   }
/*     */   
/*     */   private void getPlayerRank(PlayerRoundBean prBean)
/*     */   {
/* 212 */     GameLogic gl = new GameLogic();
/* 213 */     if (gl.isTrail(prBean.getCards()))
/*     */     {
/* 215 */       prBean.setRank(Commands.TRAIL.intValue());
/*     */     }
/* 217 */     else if (gl.isStraightFlush(prBean.getCards()))
/*     */     {
/* 219 */       prBean.setRank(Commands.STRAIGHT_FLUSH.intValue());
/*     */     }
/* 221 */     else if (gl.isStraight(prBean.getCards()))
/*     */     {
/* 223 */       prBean.setRank(Commands.STRAIGHT.intValue());
/*     */     }
/* 225 */     else if (gl.isFlush(prBean.getCards()))
/*     */     {
/* 227 */       prBean.setRank(Commands.FLUSH.intValue());
/*     */     }
/* 229 */     else if (gl.isPair(prBean.getCards()))
/*     */     {
/* 231 */       prBean.setRank(Commands.TWO_PAIR.intValue());
/*     */     }
/*     */     else
/*     */     {
/* 235 */       prBean.setRank(Commands.HIGH_CARD.intValue());
/*     */     }
/* 237 */     gl = null;
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bsn/ShowBsn.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */