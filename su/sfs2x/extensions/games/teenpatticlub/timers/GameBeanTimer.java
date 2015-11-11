/*     */ package su.sfs2x.extensions.games.teenpatticlub.timers;
/*     */ 
/*     */ import com.smartfoxserver.v2.api.ISFSApi;
/*     */ import com.smartfoxserver.v2.api.SFSApi;
import com.smartfoxserver.v2.entities.Room;
/*     */ import com.smartfoxserver.v2.entities.User;
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*     */ 
/*     */ public class GameBeanTimer implements ActionListener
/*     */ {
/*     */   private String roomId;
/*     */   private String command;
/*     */   
/*     */   public GameBeanTimer(String roomId, String command)
/*     */   {
/*  31 */     this.roomId = roomId;
/*  32 */     this.command = command;
/*     */   }
/*     */   
/*     */   public void actionPerformed(ActionEvent arg0)
/*     */   {
/*  37 */     GameBean gameBean = Appmethods.getGameBean(this.roomId);
/*     */     
/*  39 */     if (gameBean != null)
/*     */     {
/*  41 */       gameBean.stopTimer();
/*     */       
/*     */ 
/*     */ 
/*  45 */       if (this.command.equals("StartGame"))
/*     */       {
/*     */ 
/*  48 */         gameBean.startGame();
/*     */       }
/*  50 */       else if (this.command.equals("NextGame"))
/*     */       {
/*  52 */         Appmethods.showLog("NEXT_GAME");
/*  53 */         Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/*  54 */         gameBean.setGameGenerating(true);
/*  55 */         gameBean.setGameID(Appmethods.generateGameID());
/*  56 */         ISFSObject obj = new SFSObject();
/*  57 */         obj.putInt("sec", 5);
/*  58 */         Commands.appInstance.send("QuickStart", obj, room.getUserList());
/*  59 */         gameBean.startTimer(5, "StartGame");
/*     */       }
/*  61 */       else if (this.command.equals("Turn"))
/*     */       {
/*  63 */         Appmethods.showLog("TURN");
/*     */         
/*     */ 
/*  66 */         Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/*  67 */         String player = gameBean.getGameRoundBean().getTurn();
/*  68 */         PlayerBean playerBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/*  69 */         int remainigTime = playerBean.getTimerBank();
/*     */
/*  71 */         Appmethods.showLog("Remaining Time " + remainigTime);

//
//                  User user = room.getUserByName(player);
//                  ActionsBsn absn = new ActionsBsn();
//                  if (user.isNpc()) {
//                      ISFSObject sfso = new SFSObject();
//
//                     sfso.putInt("amount", gameBean.getGameRoundBean().getBlindBet());
//                      absn.show(player, sfso, gameBean);
//                  }
/*     */         
/*     */ 
/*  74 */         if ((remainigTime > 0) && (!playerBean.isActive()))
/*     */         {
/*  76 */           Appmethods.showLog("Time Completed Start Extra Time");
/*     */           
/*  78 */           playerBean.setUsingExtraTime(true);
/*  79 */           ISFSObject sfso = new SFSObject();
/*  80 */           sfso.putUtfString("turn", gameBean.getGameRoundBean().getTurn());
/*  81 */           sfso.putInt("remainingTime", remainigTime);
/*  82 */           Commands.appInstance.send("ExtraTime", sfso, room.getUserList());
/*     */           
/*  84 */           gameBean.startTimer(remainigTime, "ExtraTime");
/*     */         }
/*     */         else
/*     */         {
/*  88 */           Appmethods.showLog("Time Completed Pack");
/*  89 */           playerBean.setTimeUpCount(playerBean.getTimeUpCount() + 1);
/*  90 */           ActionsBsn actionBsn = new ActionsBsn();
/*  91 */           actionBsn.pack(player, new SFSObject(), gameBean);
/*  92 */           actionBsn = null;}
/*     */
/*     */       }
/*  95 */       else if (this.command.equals("ExtraTime"))
/*     */       {
/*  97 */         Appmethods.showLog("EXTRA_TIME");
/*     */         
/*  99 */         int remainigTime = gameBean.getRemainingSeconds();
/* 100 */         String player = gameBean.getGameRoundBean().getTurn();
/* 101 */         PlayerBean playerBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/*     */         
/* 103 */         Appmethods.showLog("remaining Time >>" + remainigTime);
/*     */         
/* 105 */         if (remainigTime <= 0)
/*     */         {
/* 107 */           playerBean.setUsingExtraTime(false);
/* 108 */           playerBean.setTimerBank(Integer.valueOf(remainigTime));
/* 109 */           ActionsBsn actionBsn = new ActionsBsn();
/* 110 */           actionBsn.pack(player, new SFSObject(), gameBean);
/* 111 */           actionBsn = null;
/*     */         }
/*     */       }
/* 114 */       else if (this.command.equals("CloseGame"))
/*     */       {
/*     */
/* 117 */         for (int i = 0; i < gameBean.getPlayers().size(); i++)
/*     */         {
/* 119 */           if (!((String)gameBean.getPlayers().get(i)).equals("null"))
/*     */           {
/* 121 */             gameBean.removePlayer((String)gameBean.getPlayers().get(i));
/* 122 */             gameBean.removePlayerBean((String)gameBean.getPlayers().get(i));
/*     */           }
/*     */         }
/*     */         
/* 126 */         if (gameBean.getGameType().equals("Private"))
/*     */         {
/* 128 */           TableBean tBean = Appmethods.getPrivateTableBean(gameBean.getRoomId());
/*     */           
/* 130 */           UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
/* 131 */           ulBsn.updatePrivateTableLobby("Delete", tBean);
/* 132 */           ulBsn = null;
/*     */           
/* 134 */           Commands.appInstance.proxy.updatePrivateTable(tBean.getPrivateTableId().intValue());
/*     */           
/* 136 */           Commands.appInstance.privateTables.remove(tBean);
/*     */         }
/* 138 */         else if (gameBean.getGameType().equals("Public"))
/*     */         {
/* 140 */           Appmethods.updateDynamicRoom(gameBean);
/*     */         }
/*     */         
/* 143 */         Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/*     */         
/* 145 */         ISFSObject sfso = new SFSObject();
/* 146 */         sfso.putUtfString("status", "Game room closed try another table");
/* 147 */         Commands.appInstance.send("CloseGame", sfso, room.getUserList());
/*     */         
/*     */ 
/*     */ 
/* 151 */         for (int i = 0; i < room.getUserList().size(); i++)
/*     */         {
/* 153 */           Commands.appInstance.getApi().leaveRoom((User)room.getUserList().get(i), room);
/*     */         }
/*     */         
/*     */   int n = 0;
            List<User> players = room.getPlayersList();
            if (players.size() > 0) {
                for (User player : players) {
                    if (player.isNpc()) {
                        n++;
                    }
                }
            }
            if (n == 0) {
/*     */
                System.out.println("================CLOSING GAME FROM GAMEBEANTIMER 157 ====================");
/* 158 */
                Appmethods.removeGameBean(gameBean);
            }
/*     */
        }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/timers/GameBeanTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */