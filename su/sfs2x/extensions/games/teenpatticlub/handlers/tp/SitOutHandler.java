 package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class SitOutHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("SitOutHandler");
     String player = sender.getName();
     Room room = sender.getLastJoinedRoom();
     GameBean gameBean = Appmethods.getGameBean(room.getName());
     if (gameBean != null)
     {
       params.putUtfString("player", sender.getName());
       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(sender.getName());
       PlayerRoundBean prBean = null;
       if (gameBean.getGameRoundBean() != null)
       {
         prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
         
         if (prBean != null)
         {
           ActionsBsn aBsn = new ActionsBsn();
           aBsn.sitOut(player, params, gameBean);
           aBsn = null;
         }
         
         if (params.getBool("isSitOut").booleanValue())
         {
 
           gameBean.removePlayerAndAddToSpectator(player);
           
 
           Commands.appInstance.proxy.updateUserChips(player, Float.valueOf(pBean.getInplay()));
           
           params.putUtfStringArray("players", gameBean.getGameRoundBean().getPlayers());
         }

       }
       else
       {
         gameBean.removePlayerAndAddToSpectator(player);
         params.putUtfStringArray("players", gameBean.getPlayers());
       }
       
       Appmethods.updateGameBeanUpdateLobby(gameBean, room);
       send("SitOut", params, room.getUserList());
     }
     else
     {
       Appmethods.showLog("GameBean Not Found");
     }
   }
 }


