 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class LeaveTableHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("LeaveTableHandler");
     
     Room room = sender.getLastJoinedRoom();
     Appmethods.showLog("LeaveTableHandler " + room.getName());
     GameBean gameBean = Appmethods.getGameBean(room.getName());
     ISFSObject sfso = new SFSObject();
     
     if ((gameBean != null) && (!room.getGroupId().equals("LobbyGroup")))
     {
       Appmethods.leaveRoom(sender, room);
       boolean isSpectator = false;
       if (!gameBean.isStarted())
       {
 
         PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(sender.getName());
         Commands.appInstance.proxy.updateUserChips(sender.getName(), Float.valueOf(pBean.getInplay()));
         
 
         if (gameBean.getSpectatorsList().contains(sender.getName()))
         {
 
 
           gameBean.removePlayer(sender.getName());
           isSpectator = true;
         }
         else
         {
           if (pBean.getAmounts().size() != 0)
           {
 
             Commands.appInstance.proxy.insertUserLastSession(pBean, 0);
             send("GetUserLastSession", Commands.appInstance.proxy.getUserLastSessionInfo(sender.getName(), 0), sender);
           }
           
 
           gameBean.removePlayer(sender.getName());
           gameBean.removePlayerBean(sender.getName());
         }
         
 
 
         sfso = gameBean.getSFSObject();
         sfso.putUtfString("player", sender.getName());
         sfso.putBool("isSpectator", isSpectator);
         
         send("LeaveTable", sfso, sender);
         send("LeaveTable", sfso, room.getUserList());
         
 
         Appmethods.updateGameBeanUpdateLobby(gameBean, room);
 
       }
       else if (gameBean.getSpectatorsList().contains(sender.getName()))
       {
         gameBean.removePlayer(sender.getName());
         isSpectator = true;
         sfso = gameBean.getSFSObject();
         sfso.putUtfString("player", sender.getName());
         sfso.putBool("isSpectator", isSpectator);
         send("LeaveTable", sfso, sender);
         send("LeaveTable", sfso, room.getUserList());
       }
       else
       {
         PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(sender.getName());
         PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(sender.getName());
         
         if (!gameBean.isGameGenerating())
         {
           if (prBean != null)
           {
             pBean.getAmounts().add(Float.valueOf(-prBean.getTotalBetAmount()));
             pBean.setTotalHands(pBean.getTotalHands() + 1);
           }
         }
         
 
 
         ActionsBsn actionBsn = new ActionsBsn();
         actionBsn.leaveTable(sender.getName(), params, gameBean);
         actionBsn = null;
         
         if (prBean != null)
         {
           if (!prBean.isSendSession())
           {
             prBean.setSendSession(true);
             
             Commands.appInstance.proxy.insertUserLastSession(pBean, 0);
             
             send("GetUserLastSession", Commands.appInstance.proxy.getUserLastSessionInfo(sender.getName(), 0), sender);
           }
           
         }
       }
     }
     else
     {
       if (!room.getGroupId().equals("LobbyGroup"))
         Appmethods.leaveRoom(sender, room);
       send("LeaveTable", sfso, sender);
     }
   }
 }


