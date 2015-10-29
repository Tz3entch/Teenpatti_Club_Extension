/*     */ package su.sfs2x.extensions.games.teenpatticlub.bsn;
/*     */ 
/*     */ import com.smartfoxserver.v2.entities.Room;
/*     */ import com.smartfoxserver.v2.entities.User;
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DisconnectUserBsn
/*     */ {
/*     */   public void disconnectUser(User user, Room room)
/*     */   {
/*  31 */     Appmethods.showLog("DisconnectUserBsn >>USER " + user.getName());
/*     */     
/*  33 */     GameBean gameBean = Appmethods.getGameBean(room.getName());
/*  34 */     if (gameBean != null)
/*     */     {
/*  36 */       if (!gameBean.isStarted())
/*     */       {
/*     */ 
/*  39 */         boolean isSpectator = false;
/*     */         
/*  41 */         if (gameBean.getSpectatorsList().contains(user.getName()))
/*     */         {
/*     */ 
/*     */ 
/*  45 */           gameBean.removePlayer(user.getName());
/*  46 */           isSpectator = true;
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/*  52 */           gameBean.removePlayer(user.getName());
/*  53 */           gameBean.removePlayerBean(user.getName());
/*     */           
/*  55 */           Appmethods.updateGameBeanUpdateLobby(gameBean, room);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 141 */         ISFSObject sfso = new SFSObject();
/* 142 */         sfso = gameBean.getSFSObject();
/* 143 */         sfso.putUtfString("player", user.getName());
/* 144 */         sfso.putBool("isSpectator", isSpectator);
/* 145 */         Commands.appInstance.send("UserDisconnected", sfso, room.getUserList());
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 151 */         PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(user.getName());
/* 152 */         if (pBean != null) {
/* 153 */           pBean.setActive(false);
/*     */         }
/* 155 */         PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(user.getName());
/*     */         
/* 157 */         if ((!gameBean.getSpectatorsList().contains(user.getName())) && (prBean == null))
/*     */         {
/* 159 */           Appmethods.showLog("Disconnect join Player but not in game Remove Player" + user.getName());
/*     */           
/* 161 */           gameBean.getGameRoundBean().removePlayer(user.getName());
/* 162 */           gameBean.removePlayer(user.getName());
/*     */           
/* 164 */           ISFSObject sfso = new SFSObject();
/* 165 */           sfso = gameBean.getSFSObject();
/* 166 */           sfso = gameBean.getGameRoundBean().getSFSObject(sfso);
/* 167 */           sfso.putUtfString("player", user.getName());
/* 168 */           sfso.putBool("isSpectator", false);
/* 169 */           Commands.appInstance.send("LeaveTable", sfso, room.getUserList());
/*     */           
/*     */ 
/* 172 */           Appmethods.updateGameBeanUpdateLobby(gameBean, room);
/*     */         }
/*     */         
/* 175 */         if (gameBean.getSpectatorsList().contains(user.getName()))
/*     */         {
/* 177 */           Appmethods.showLog("Disconnect Spectator Remove Player" + user.getName());
/*     */           
/* 179 */           gameBean.getSpectatorsList().remove(user.getName());
/* 180 */           gameBean.removePlayerBean(user.getName());
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 187 */       System.out.println("DisconnectUserBsn : GameBean Not Found");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bsn/DisconnectUserBsn.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */