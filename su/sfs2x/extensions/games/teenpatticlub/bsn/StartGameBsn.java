 package su.sfs2x.extensions.games.teenpatticlub.bsn;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class StartGameBsn
 {
   public void startGame(GameBean gameBean)
   {
     ISFSObject sfso = new SFSObject();
     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
     
     sfso = gameBean.getSFSObject();
     sfso = gameBean.getGameRoundBean().getSFSObject(sfso);
     sfso.putInt("turnTime", 60);
     
     Commands.appInstance.send("StartGame", sfso, room.getUserList());
     
 
     gameBean.startTimer(61, "Turn");
     
 
     Commands.appInstance.proxy.startGameStatus(gameBean);
   }
 }


