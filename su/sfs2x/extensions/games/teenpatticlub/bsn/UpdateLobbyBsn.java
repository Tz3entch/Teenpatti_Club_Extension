 package su.sfs2x.extensions.games.teenpatticlub.bsn;
 
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.List;
 import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;

 public class UpdateLobbyBsn
 {
   public void updatePrivateTableLobby(String command, TableBean tBean)
   {
     ISFSObject sfso = new SFSObject();
     sfso = tBean.getPrivateTableSFSObject();
     sfso.putUtfString("command", command);
     
     Collection<User> users = Commands.appInstance.getParentZone().getUserList();
     List<User> listusers = new ArrayList();
     Object[] arr = users.toArray();
     for (int i = 0; i < arr.length; i++)
     {
       User no = (User)arr[i];
       listusers.add(no);
     }
     
     Commands.appInstance.send("UpdatePrivateTableLobby", sfso, listusers);
   }
 }


