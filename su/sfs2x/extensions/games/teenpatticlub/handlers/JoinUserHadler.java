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
import su.sfs2x.extensions.games.teenpatticlub.bsn.JoinUserBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JoinUserHadler
/*     */   extends BaseClientRequestHandler
/*     */ {
/*     */   public void handleClientRequest(User sender, ISFSObject params)
/*     */   {
/*  28 */     Appmethods.showLog("**********JoinUserHadler*******");
/*     */     
/*  30 */     String player = sender.getName();
/*     */     
/*  32 */     String type = params.getUtfString("type");
/*     */     
/*  34 */     float inPlay = Commands.appInstance.proxy.getPlayerInpaly(sender.getName());
/*     */     
/*  36 */     GameBean gameBean = null;
/*  37 */     Room room = null;
/*  38 */     TableBean tableBean = null;
/*     */     
/*  40 */     ISFSObject sfso = new SFSObject();
/*     */     
/*  42 */     if (type.equals("Public"))
/*     */     {
/*     */ 
/*     */ 
/*  46 */       int tableId = params.getInt("tableId").intValue();
/*  47 */       tableBean = Appmethods.getTableBean(tableId);
/*     */       
/*  49 */       if (tableBean != null)
/*     */       {
/*     */ 
/*     */ 
/*  53 */         boolean isGameStarted = false;
/*  54 */         if (tableBean.get_roomId().equals("null"))
/*     */         {
/*     */           try {
/*  57 */             room = Appmethods.createRoom();
/*     */           } catch (Exception localException) {}
/*  59 */           gameBean = new GameBean();
/*  60 */           gameBean.setGameID(Appmethods.generateGameID());
/*  61 */           gameBean.setRoomId(room.getName());
/*  62 */           gameBean.setGameType("Public");
/*  63 */           gameBean.setTableBeanId(tableBean.get_id().intValue());
/*     */           
/*  65 */           tableBean.set_roomId(room.getName());
/*     */           
/*  67 */           Commands.appInstance.getGames().put(room.getName(), gameBean);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*  72 */           gameBean = Appmethods.getGameBean(tableBean.get_roomId());
/*  73 */           room = Appmethods.getRoomByName(tableBean.get_roomId());
/*     */           
/*  75 */           if (gameBean.isStarted())
/*     */           {
/*     */ 
/*  78 */             isGameStarted = true;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*  83 */         Appmethods.joinRoom(sender, room);
/*     */         
/*     */ 
/*     */ 
/*  87 */         PlayerBean playerBean = new PlayerBean(player);
/*  88 */         playerBean.setInplay(inPlay);
/*  89 */         playerBean.setStartInplay(inPlay);
/*  90 */         gameBean.getPlayerBeenList().put(player, playerBean);
/*     */         
/*     */ 
/*  93 */         gameBean.getSpectatorsList().add(player);
/*     */         
/*     */ 
/*  96 */         if (!isGameStarted)
/*     */         {
/*  98 */           sfso = gameBean.getSFSObject();
/*  99 */           send("JoinUser", sfso, sender);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 104 */           sfso = gameBean.getSFSObject();
/* 105 */           send("JoinUser", sfso, sender);
/* 106 */           sfso = new SFSObject();
/*     */           
/*     */ 
/* 109 */           sfso = Appmethods.getSinkDataSFSObject(gameBean);
/* 110 */           send("SinkData", sfso, sender);
/*     */         }
/*     */       }
/*     */     }
/* 114 */     else if (type.equals("Private"))
/*     */     {
/*     */ 
/* 117 */       String roomId = params.getUtfString("roomId");
/*     */       
/* 119 */       JoinUserBsn juBsn = new JoinUserBsn();
/* 120 */       juBsn.joinUser(inPlay, roomId, sender);
/* 121 */       juBsn = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/JoinUserHadler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */