/*    */ package su.sfs2x.extensions.games.teenpatticlub.handlers;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.User;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.entities.data.SFSArray;
/*    */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*    */ import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
/*    */ import java.util.ArrayList;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ public class GetLobbyHandler
/*    */   extends BaseClientRequestHandler
/*    */ {
/*    */   public void handleClientRequest(User sender, ISFSObject params)
/*    */   {
/* 20 */     Appmethods.showLog("*************** GetLobbyHandler *******************");
/*    */     
/*    */ 
/* 23 */     SFSArray public_tables = new SFSArray();
/* 24 */     SFSArray private_tables = new SFSArray();
/*    */     
/* 26 */     for (int i = 0; i < Commands.appInstance.publicTables.size(); i++)
/*    */     {
/* 28 */       ISFSObject obj = ((TableBean)Commands.appInstance.publicTables.get(i)).getSFSObject();
/* 29 */       public_tables.addSFSObject(obj);
/*    */     }
/*    */     
/* 32 */     for (int i = 0; i < Commands.appInstance.privateTables.size(); i++)
/*    */     {
/* 34 */       ISFSObject obj = ((TableBean)Commands.appInstance.privateTables.get(i)).getPrivateTableSFSObject();
/* 35 */       private_tables.addSFSObject(obj);
/*    */     }
/*    */     
/* 38 */     ISFSObject sfso = new SFSObject();
/* 39 */     sfso.putSFSArray("public_tables", public_tables);
/* 40 */     sfso.putSFSArray("private_tables", private_tables);
/* 41 */     send("GetLobby", sfso, sender);
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/handlers/GetLobbyHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */