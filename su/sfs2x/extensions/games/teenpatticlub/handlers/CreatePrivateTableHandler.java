/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Calendar;
/*    */ import java.util.TimeZone;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.bsn.JoinUserBsn;
import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CreatePrivateTableHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 32 */     Appmethods.showLog("********CreatePrivateTableHandler******");
/*    */     
/*    */ 
/* 35 */     if (Commands.appInstance.proxy.isPrivateTableAvail(sender.getName()))
/*    */     {
/* 37 */       params.putUtfString("status", "Already Created");
/* 38 */       send("CreatePrivateTable", params, sender);
/*    */     }
/*    */     else
/*    */     {
/* 42 */       int boot = params.getInt("boot").intValue();
/* 43 */       int chaalLimit = boot;
/* 44 */       int potLimit = boot;
/*    */       
/* 46 */       for (int i = 0; i < 7; i++) {
/* 47 */         chaalLimit *= 2;
/*    */       }
/* 49 */       for (int i = 0; i < 11; i++) {
/* 50 */         potLimit *= 2;
/*    */       }
/*    */       
/* 53 */       TableBean tBean = new TableBean();
/*    */       
/* 55 */       tBean.set_boot(Integer.valueOf(boot));
/* 56 */       tBean.set_challLimit(Integer.valueOf(chaalLimit));
/* 57 */       tBean.set_potLimit(Integer.valueOf(potLimit));
/* 58 */       tBean.set_roomId(sender.getName());
/* 59 */       tBean.set_password(params.getUtfString("password"));
/* 60 */       tBean.set_type("Private");
/*    */       
/* 62 */       String currDate = null;
/* 63 */       Calendar currentTime = Calendar.getInstance();
/* 64 */       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 65 */       dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
/* 66 */       currDate = dateFormat.format(currentTime.getTime());
/* 67 */       tBean.set_startDate(currDate);
/*    */       
/*    */ 
/* 70 */       tBean.startTimer(604800);
/*    */       
/*    */ 
/* 73 */       Commands.appInstance.privateTables.add(tBean);
/*    */       
/*    */ 
/* 76 */       Commands.appInstance.proxy.createPrivateTable(tBean);
/*    */       
/*    */ 
/* 79 */       params.putUtfString("status", "Created");
/* 80 */       send("CreatePrivateTable", params, sender);
/*    */       
/*    */ 
/* 83 */       UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
/* 84 */       ulBsn.updatePrivateTableLobby("Create", tBean);
/* 85 */       ulBsn = null;
/*    */       
/*    */ 
/*    */ 
/* 89 */       float inPlay = Commands.appInstance.proxy.getPlayerInpaly(sender.getName());
/*    */       
/* 91 */       JoinUserBsn juBsn = new JoinUserBsn();
/* 92 */       juBsn.joinUser(inPlay, sender.getName(), sender);
/* 93 */       juBsn = null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/CreatePrivateTableHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */