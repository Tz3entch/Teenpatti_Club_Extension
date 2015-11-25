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

 public class ChangeDealerHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("ChangeDealerHandler");
     
     String player = sender.getName();
     Room room = null;
     room = sender.getLastJoinedRoom();
     GameBean gameBean = Appmethods.getGameBean(room.getName());
     
     if (gameBean != null)
     {
 
       int dealerId = params.getInt("dealerId").intValue();
       float dealerCost = params.getFloat("dealerCost").floatValue();
       
       PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
       ISFSObject sfso = new SFSObject();
       sfso.putUtfString("player", player);
       if (gameBean.getGameRoundBean() != null)
       {
         PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(player);
         if ((prBean != null) && (gameBean.isStarted()) && (!gameBean.isGameGenerating()))
         {
           if (pBean.getInplay() > dealerCost)
           {
 
             Commands.appInstance.proxy.changeDealer(player, dealerId, dealerCost, gameBean.getGameID());
             
 
             pBean.setInplay(pBean.getInplay() - dealerCost);
             
             sfso.putUtfString("comment", "success");
             sfso.putInt("dealerId", dealerId);
             sfso.putFloat("dealerCost", dealerCost);
             send("ChangeDealer", sfso, room.getUserList());
           }
           else
           {
             sfso.putUtfString("comment", "no sufficient chips");
             send("ChangeDealer", sfso, sender);
           }
         }
         else
         {
           sfso.putUtfString("comment", "you can not change dealer at this time");
           send("ChangeDealer", sfso, sender);
         }
       }
       else
       {
         sfso.putUtfString("comment", "you can not change dealer at this time");
         send("ChangeDealer", sfso, sender);
       }
     }
   }
 }


