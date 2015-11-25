 package su.sfs2x.extensions.games.teenpatticlub.timers;
 
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class TableBeanTimer
   implements ActionListener
 {
   private String roomId = "";
   
   public TableBeanTimer(String roomid) { this.roomId = roomid; }
   
 
   public void actionPerformed(ActionEvent arg0)
   {
     TableBean tBean = Appmethods.getPrivateTableBean(this.roomId);
     tBean.stopTimer();
     
     UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
     ulBsn.updatePrivateTableLobby("Delete", tBean);
     ulBsn = null;
     
     Commands.appInstance.proxy.updatePrivateTable(tBean.getPrivateTableId().intValue());
     
     Commands.appInstance.privateTables.remove(tBean);
   }
 }


