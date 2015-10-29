/*    */ package su.sfs2x.extensions.games.teenpatticlub.bsn;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.Zone;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UpdateLobbyBsn
/*    */ {
/*    */   public void updatePrivateTableLobby(String command, TableBean tBean)
/*    */   {
/* 31 */     ISFSObject sfso = new SFSObject();
/* 32 */     sfso = tBean.getPrivateTableSFSObject();
/* 33 */     sfso.putUtfString("command", command);
/*    */     
/* 35 */     Collection<User> users = Commands.appInstance.getParentZone().getUserList();
/* 36 */     List<User> listusers = new ArrayList();
/* 37 */     Object[] arr = users.toArray();
/* 38 */     for (int i = 0; i < arr.length; i++)
/*    */     {
/* 40 */       User no = (User)arr[i];
/* 41 */       listusers.add(no);
/*    */     }
/*    */     
/* 44 */     Commands.appInstance.send("UpdatePrivateTableLobby", sfso, listusers);
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bsn/UpdateLobbyBsn.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */