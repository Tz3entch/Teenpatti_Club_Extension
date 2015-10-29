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
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JoinTableHandler
/*     */   extends BaseClientRequestHandler
/*     */ {
/*     */   public void handleClientRequest(User sender, ISFSObject params)
/*     */   {
/*  28 */     Appmethods.showLog("JoinTableHandler");
/*  29 */     Room room = null;
/*  30 */     room = sender.getLastJoinedRoom();
/*  31 */     String player = sender.getName();
/*  32 */     GameBean gameBean = Appmethods.getGameBean(room.getName());
/*  33 */     ISFSObject sfso = new SFSObject();
/*     */     
/*  35 */     if (gameBean != null)
/*     */     {
/*     */ 
/*  38 */       int pos = params.getInt("pos").intValue();
/*     */       
/*     */ 
/*  41 */       if ((((String)gameBean.getPlayers().get(pos)).equals("null")) && (!Appmethods.isUserExist(gameBean.getPlayers(), player)))
/*     */       {
/*     */ 
/*  44 */         gameBean.addPlayer(pos, sender.getName());
/*     */         
/*  46 */         if (gameBean.isGameGenerating())
/*     */         {
/*  48 */           sfso.putInt("remainingSeconds", gameBean.getRemainingSeconds());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */         if (!gameBean.isStarted())
/*     */         {
/*     */ 
/*  61 */           if (gameBean.getJoinedPlayers() == 2)
/*     */           {
/*     */ 
/*  64 */             gameBean.setGameGenerating(true);
/*  65 */             ISFSObject obj = new SFSObject();
/*  66 */             obj.putInt("sec", 30);
/*  67 */             send("QuickStart", obj, room.getUserList());
/*  68 */             gameBean.startTimer(31, "StartGame");
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*  73 */         if (gameBean.getGameType().equals("Public"))
/*     */         {
/*     */ 
/*  76 */           Appmethods.updateDynamicRoom(gameBean);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*  81 */           TableBean tBean = Appmethods.getPrivateTableBean(gameBean.getRoomId());
/*  82 */           UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
/*  83 */           ulBsn.updatePrivateTableLobby("Update", tBean);
/*  84 */           ulBsn = null;
/*     */           
/*  86 */           PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
/*     */           
/*  88 */           pBean.setInplay(pBean.getInplay() - 30.0F);
/*     */           
/*     */ 
/*  91 */           String type = "Join";
/*  92 */           if (room.getName().equals(player))
/*  93 */             type = "Create";
/*  94 */           Commands.appInstance.proxy.insertPrivateTableHistory(gameBean, player, type, tBean.getPrivateTableId().intValue());
/*     */         }
/*     */         
/*  97 */         sfso.putInt("pos", pos);
/*  98 */         sfso.putUtfString("joinedPlayer", sender.getName());
/*  99 */         sfso.putUtfString("avatar", Commands.appInstance.proxy.getPlayerAvatar(sender.getName()));
/* 100 */         sfso.putBool("isGameStarted", gameBean.isStarted());
/* 101 */         sfso.putUtfStringArray("players", gameBean.getPlayers());
/* 102 */         sfso.putBool("isGameGenerating", gameBean.isGameGenerating());
/* 103 */         sfso.putSFSArray("playerBeans", gameBean.getPlayerBeansSFSArray());
/* 104 */         send("JoinTable", sfso, room.getUserList());
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 109 */         send("SeatOccupied", params, sender);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 114 */       Appmethods.showLog("JoinTableHandler:  GameBean is Null");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/JoinTableHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */