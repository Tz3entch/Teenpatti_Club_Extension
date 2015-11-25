 package su.sfs2x.extensions.games.teenpatticlub.bsn;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class DisconnectUserBsn
 {
   public void disconnectUser(User user, Room room)
   {
     Appmethods.showLog("DisconnectUserBsn >>USER " + user.getName());
     
     GameBean gameBean = Appmethods.getGameBean(room.getName());
     if (gameBean != null)
     {
       if (!gameBean.isStarted())
       {
 
         boolean isSpectator = false;
         
         if (gameBean.getSpectatorsList().contains(user.getName()))
         {
 
 
           gameBean.removePlayer(user.getName());
           isSpectator = true;
 
         }
         else
         {
 
           gameBean.removePlayer(user.getName());
           gameBean.removePlayerBean(user.getName());
           
           Appmethods.updateGameBeanUpdateLobby(gameBean, room);
         }

         ISFSObject sfso = new SFSObject();
         sfso = gameBean.getSFSObject();
         sfso.putUtfString("player", user.getName());
         sfso.putBool("isSpectator", isSpectator);
         Commands.appInstance.send("UserDisconnected", sfso, room.getUserList());
 
       }
       else
       {
 
         PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(user.getName());
         if (pBean != null) {
           pBean.setActive(false);
         }
         PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(user.getName());
         
         if ((!gameBean.getSpectatorsList().contains(user.getName())) && (prBean == null))
         {
           Appmethods.showLog("Disconnect join Player but not in game Remove Player" + user.getName());
           
           gameBean.getGameRoundBean().removePlayer(user.getName());
           gameBean.removePlayer(user.getName());
           
           ISFSObject sfso = new SFSObject();
           sfso = gameBean.getSFSObject();
           sfso = gameBean.getGameRoundBean().getSFSObject(sfso);
           sfso.putUtfString("player", user.getName());
           sfso.putBool("isSpectator", false);
           Commands.appInstance.send("LeaveTable", sfso, room.getUserList());
           
 
           Appmethods.updateGameBeanUpdateLobby(gameBean, room);
         }
         
         if (gameBean.getSpectatorsList().contains(user.getName()))
         {
           Appmethods.showLog("Disconnect Spectator Remove Player" + user.getName());
           
           gameBean.getSpectatorsList().remove(user.getName());
           gameBean.removePlayerBean(user.getName());
         }
         
       }
       
     }
     else {
       System.out.println("DisconnectUserBsn : GameBean Not Found");
     }
   }
 }


