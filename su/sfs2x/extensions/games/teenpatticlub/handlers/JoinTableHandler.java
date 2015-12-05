 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class JoinTableHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("JoinTableHandler");
     Room room = null;
     room = sender.getLastJoinedRoom();
     String player = sender.getName();
     GameBean gameBean = Appmethods.getGameBean(room.getName());
     ISFSObject sfso = new SFSObject();
     
     if (gameBean != null)
     {
 
       int pos = params.getInt("pos").intValue();
       
 
       if ((((String)gameBean.getPlayers().get(pos)).equals("null")) && (!Appmethods.isUserExist(gameBean.getPlayers(), player)))
       {
 
         gameBean.addPlayer(pos, sender.getName());
         
         if (gameBean.isGameGenerating())
         {
           sfso.putInt("remainingSeconds", gameBean.getRemainingSeconds());
         }

         if (!gameBean.isStarted())
         {
 
           if (gameBean.getJoinedPlayers() == 2)
           {
 
             gameBean.setGameGenerating(true);
             ISFSObject obj = new SFSObject();
             obj.putInt("sec", 30);
             send("QuickStart", obj, room.getUserList());
             gameBean.startTimer(31, "StartGame");
           }
         }

         if (gameBean.getGameType().equals("Public"))
         {
 
           Appmethods.updateDynamicRoom(gameBean);
 
         }
         else
         {
           TableBean tBean = Appmethods.getPrivateTableBean(gameBean.getRoomId());
           UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
           ulBsn.updatePrivateTableLobby("Update", tBean);
           ulBsn = null;
           
           PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
           float commission = Commands.appInstance.proxy.getPrivateCommission();
           
           pBean.setInplay(pBean.getInplay() - commission);
           
 
           String type = "Join";
           if (room.getName().equals(player))
             type = "Create";
           Commands.appInstance.proxy.insertPrivateTableHistory(gameBean, player, type, tBean.getPrivateTableId().intValue(), commission);
         }
         
         sfso.putInt("pos", pos);
         sfso.putUtfString("joinedPlayer", sender.getName());
         sfso.putUtfString("avatar", Commands.appInstance.proxy.getPlayerAvatar(sender.getName()));
         sfso.putBool("isGameStarted", gameBean.isStarted());
         sfso.putUtfStringArray("players", gameBean.getPlayers());
         sfso.putBool("isGameGenerating", gameBean.isGameGenerating());
         sfso.putSFSArray("playerBeans", gameBean.getPlayerBeansSFSArray());
         send("JoinTable", sfso, room.getUserList());
 
       }
       else
       {
         send("SeatOccupied", params, sender);
       }
     }
     else
     {
       Appmethods.showLog("JoinTableHandler:  GameBean is Null");
     }
   }
 }


