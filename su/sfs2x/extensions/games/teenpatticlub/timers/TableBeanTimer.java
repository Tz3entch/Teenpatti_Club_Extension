/*    */ package su.sfs2x.extensions.games.teenpatticlub.timers;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.util.ArrayList;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
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
/*    */ public class TableBeanTimer
/*    */   implements ActionListener
/*    */ {
/* 23 */   private String roomId = "";
/*    */   
/* 25 */   public TableBeanTimer(String roomid) { this.roomId = roomid; }
/*    */   
/*    */ 
/*    */   public void actionPerformed(ActionEvent arg0)
/*    */   {
/* 30 */     TableBean tBean = Appmethods.getPrivateTableBean(this.roomId);
/* 31 */     tBean.stopTimer();
/*    */     
/* 33 */     UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
/* 34 */     ulBsn.updatePrivateTableLobby("Delete", tBean);
/* 35 */     ulBsn = null;
/*    */     
/* 37 */     Commands.appInstance.proxy.updatePrivateTable(tBean.getPrivateTableId().intValue());
/*    */     
/* 39 */     Commands.appInstance.privateTables.remove(tBean);
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/timers/TableBeanTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */