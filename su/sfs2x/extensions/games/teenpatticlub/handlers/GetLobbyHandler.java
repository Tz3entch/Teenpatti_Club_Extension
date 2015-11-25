 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSArray;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class GetLobbyHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("*************** GetLobbyHandler *******************");
     
 
     SFSArray public_tables = new SFSArray();
     SFSArray private_tables = new SFSArray();
     
     for (int i = 0; i < Commands.appInstance.publicTables.size(); i++)
     {
       ISFSObject obj = ((TableBean)Commands.appInstance.publicTables.get(i)).getSFSObject();
       public_tables.addSFSObject(obj);
     }
     
     for (int i = 0; i < Commands.appInstance.privateTables.size(); i++)
     {
       ISFSObject obj = ((TableBean)Commands.appInstance.privateTables.get(i)).getPrivateTableSFSObject();
       private_tables.addSFSObject(obj);
     }
     
     ISFSObject sfso = new SFSObject();
     sfso.putSFSArray("public_tables", public_tables);
     sfso.putSFSArray("private_tables", private_tables);
     send("GetLobby", sfso, sender);
   }
 }


