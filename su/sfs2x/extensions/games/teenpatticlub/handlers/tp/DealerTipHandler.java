 package su.sfs2x.extensions.games.teenpatticlub.handlers.tp;
 
 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class DealerTipHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("DealerTipHandler");
     Room room = sender.getLastJoinedRoom();
     float tip = params.getFloat("tip").floatValue();
     String player = sender.getName();
     
     GameBean gameBean = Appmethods.getGameBean(room.getName());
     
     if (gameBean != null)
     {
       ISFSObject sfso = new SFSObject();
       sfso.putUtfString("player", sender.getName());
       sfso.putFloat("tip", tip);
       
       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
       
       if (gameBean.getGameRoundBean() != null)
       {
         PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
         
         if ((prBean != null) && (gameBean.isStarted()) && (!gameBean.isGameGenerating()))
         {
           if (pBean.getInplay() > tip)
           {
             Commands.appInstance.proxy.insertDealerTip(player, tip, gameBean.getGameID());
             pBean.setInplay(pBean.getInplay() - tip);
             
             sfso.putUtfString("comment", "success");
             send("DealerTip", sfso, room.getUserList());
           }
           else
           {
             sfso.putUtfString("comment", "no sufficient chips");
             send("DealerTip", sfso, sender);
           }
         }
         else
         {
           sfso.putUtfString("comment", "you can not give tip at this time");
           send("DealerTip", sfso, sender);
         }
       }
       else
       {
         sfso.putUtfString("comment", "you can not give tip at this time");
         send("DealerTip", sfso, sender);
       }
       
     }
     else
     {
       Appmethods.showLog("GameBean not found");
     }
   }
 }


