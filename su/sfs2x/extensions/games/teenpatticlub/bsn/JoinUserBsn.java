/*     */ package su.sfs2x.extensions.games.teenpatticlub.bsn;
/*     */ 
/*     */ import com.smartfoxserver.v2.entities.Room;
/*     */ import com.smartfoxserver.v2.entities.User;
/*     */ import com.smartfoxserver.v2.entities.Zone;
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JoinUserBsn
/*     */ {
/*     */   public void joinUser(float inPlay, String roomId, User sender)
/*     */   {
/*  27 */     String player = sender.getName();
/*     */     
/*  29 */     GameBean gameBean = null;
/*  30 */     Room room = null;
/*  31 */     TableBean tableBean = null;
/*     */     
/*  33 */     ISFSObject sfso = new SFSObject();
/*     */     
/*     */ 
/*     */ 
/*  37 */     tableBean = Appmethods.getPrivateTableBean(roomId);
/*     */     
/*  39 */     if (tableBean != null)
/*     */     {
/*  41 */       boolean isGameStarted = false;
/*  42 */       room = Commands.appInstance.getParentZone().getRoomByName(roomId);
/*  43 */       if (room == null) {
/*     */         try
/*     */         {
/*  46 */           room = Appmethods.createPrivateRoom(roomId);
/*     */         } catch (Exception localException) {}
/*     */       }
/*  49 */       gameBean = Appmethods.getGameBean(roomId);
/*     */       
/*  51 */       if (gameBean == null)
/*     */       {
/*  53 */         gameBean = new GameBean();
/*  54 */         gameBean.setGameID(Appmethods.generateGameID());
/*  55 */         gameBean.setRoomId(room.getName());
/*  56 */         gameBean.setGameType("Private");
/*     */         
/*  58 */         Commands.appInstance.getGames().put(room.getName(), gameBean);
/*     */ 
/*     */ 
/*     */       }
/*  62 */       else if (gameBean.isStarted())
/*     */       {
/*     */ 
/*  65 */         isGameStarted = true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  70 */       Appmethods.joinRoom(sender, room);
/*     */       
/*     */ 
/*  73 */       PlayerBean playerBean = new PlayerBean(player);
/*  74 */       playerBean.setInplay(inPlay);
/*  75 */       gameBean.getPlayerBeenList().put(player, playerBean);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */       gameBean.getSpectatorsList().add(player);
/*     */       
/*     */ 
/*  87 */       if (!isGameStarted)
/*     */       {
/*  89 */         sfso = gameBean.getSFSObject();
/*  90 */         Commands.appInstance.send("JoinUser", sfso, sender);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*  95 */         sfso = gameBean.getSFSObject();
/*  96 */         Commands.appInstance.send("JoinUser", sfso, sender);
/*  97 */         sfso = new SFSObject();
/*     */         
/*     */ 
/* 100 */         sfso = Appmethods.getSinkDataSFSObject(gameBean);
/* 101 */         Commands.appInstance.send("SinkData", sfso, sender);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 106 */       Appmethods.showLog("JoinUserBsn: Table Bean is Null");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bsn/JoinUserBsn.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */