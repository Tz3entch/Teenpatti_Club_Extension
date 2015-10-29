/*     */ package su.sfs2x.extensions.games.teenpatticlub.handlers;
/*     */ 
/*     */ import com.smartfoxserver.v2.entities.Room;
/*     */ import com.smartfoxserver.v2.entities.User;
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*     */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LeaveTableHandler
/*     */   extends BaseClientRequestHandler
/*     */ {
/*     */   public void handleClientRequest(User sender, ISFSObject params)
/*     */   {
/*  27 */     Appmethods.showLog("LeaveTableHandler");
/*     */     
/*  29 */     Room room = sender.getLastJoinedRoom();
/*  30 */     Appmethods.showLog("LeaveTableHandler " + room.getName());
/*  31 */     GameBean gameBean = Appmethods.getGameBean(room.getName());
/*  32 */     ISFSObject sfso = new SFSObject();
/*     */     
/*  34 */     if ((gameBean != null) && (!room.getGroupId().equals("LobbyGroup")))
/*     */     {
/*  36 */       Appmethods.leaveRoom(sender, room);
/*  37 */       boolean isSpectator = false;
/*  38 */       if (!gameBean.isStarted())
/*     */       {
/*     */ 
/*  41 */         PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(sender.getName());
/*  42 */         Commands.appInstance.proxy.updateUserChips(sender.getName(), Float.valueOf(pBean.getInplay()));
/*     */         
/*     */ 
/*  45 */         if (gameBean.getSpectatorsList().contains(sender.getName()))
/*     */         {
/*     */ 
/*     */ 
/*  49 */           gameBean.removePlayer(sender.getName());
/*  50 */           isSpectator = true;
/*     */         }
/*     */         else
/*     */         {
/*  54 */           if (pBean.getAmounts().size() != 0)
/*     */           {
/*     */ 
/*  57 */             Commands.appInstance.proxy.insertUserLastSession(pBean, 0);
/*  58 */             send("GetUserLastSession", Commands.appInstance.proxy.getUserLastSessionInfo(sender.getName(), 0), sender);
/*     */           }
/*     */           
/*     */ 
/*  62 */           gameBean.removePlayer(sender.getName());
/*  63 */           gameBean.removePlayerBean(sender.getName());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*  68 */         sfso = gameBean.getSFSObject();
/*  69 */         sfso.putUtfString("player", sender.getName());
/*  70 */         sfso.putBool("isSpectator", isSpectator);
/*     */         
/*  72 */         send("LeaveTable", sfso, sender);
/*  73 */         send("LeaveTable", sfso, room.getUserList());
/*     */         
/*     */ 
/*  76 */         Appmethods.updateGameBeanUpdateLobby(gameBean, room);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*  83 */       else if (gameBean.getSpectatorsList().contains(sender.getName()))
/*     */       {
/*  85 */         gameBean.removePlayer(sender.getName());
/*  86 */         isSpectator = true;
/*  87 */         sfso = gameBean.getSFSObject();
/*  88 */         sfso.putUtfString("player", sender.getName());
/*  89 */         sfso.putBool("isSpectator", isSpectator);
/*  90 */         send("LeaveTable", sfso, sender);
/*  91 */         send("LeaveTable", sfso, room.getUserList());
/*     */       }
/*     */       else
/*     */       {
/*  95 */         PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(sender.getName());
/*  96 */         PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(sender.getName());
/*     */         
/*  98 */         if (!gameBean.isGameGenerating())
/*     */         {
/* 100 */           if (prBean != null)
/*     */           {
/* 102 */             pBean.getAmounts().add(Float.valueOf(-prBean.getTotalBetAmount()));
/* 103 */             pBean.setTotalHands(pBean.getTotalHands() + 1);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 109 */         ActionsBsn actionBsn = new ActionsBsn();
/* 110 */         actionBsn.leaveTable(sender.getName(), params, gameBean);
/* 111 */         actionBsn = null;
/*     */         
/* 113 */         if (prBean != null)
/*     */         {
/* 115 */           if (!prBean.isSendSession())
/*     */           {
/* 117 */             prBean.setSendSession(true);
/*     */             
/* 119 */             Commands.appInstance.proxy.insertUserLastSession(pBean, 0);
/*     */             
/* 121 */             send("GetUserLastSession", Commands.appInstance.proxy.getUserLastSessionInfo(sender.getName(), 0), sender);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 129 */       if (!room.getGroupId().equals("LobbyGroup"))
/* 130 */         Appmethods.leaveRoom(sender, room);
/* 131 */       send("LeaveTable", sfso, sender);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/LeaveTableHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */