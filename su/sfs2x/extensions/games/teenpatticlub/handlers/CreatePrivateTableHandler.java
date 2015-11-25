 package su.sfs2x.extensions.games.teenpatticlub.handlers;
 
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
 import java.text.DateFormat;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.TimeZone;
 import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.JoinUserBsn;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class CreatePrivateTableHandler
   extends BaseClientRequestHandler
 {
   public void handleClientRequest(User sender, ISFSObject params)
   {
     Appmethods.showLog("********CreatePrivateTableHandler******");
     
 
     if (Commands.appInstance.proxy.isPrivateTableAvail(sender.getName()))
     {
       params.putUtfString("status", "Already Created");
       send("CreatePrivateTable", params, sender);
     }
     else
     {
       int boot = params.getInt("boot").intValue();
       int chaalLimit = boot;
       int potLimit = boot;
       
       for (int i = 0; i < 7; i++) {
         chaalLimit *= 2;
       }
       for (int i = 0; i < 11; i++) {
         potLimit *= 2;
       }
       
       TableBean tBean = new TableBean();
       
       tBean.set_boot(Integer.valueOf(boot));
       tBean.set_challLimit(Integer.valueOf(chaalLimit));
       tBean.set_potLimit(Integer.valueOf(potLimit));
       tBean.set_roomId(sender.getName());
       tBean.set_password(params.getUtfString("password"));
       tBean.set_type("Private");
       
       String currDate = null;
       Calendar currentTime = Calendar.getInstance();
       DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
       currDate = dateFormat.format(currentTime.getTime());
       tBean.set_startDate(currDate);
       
 
       tBean.startTimer(604800);
       
 
       Commands.appInstance.privateTables.add(tBean);
       
 
       Commands.appInstance.proxy.createPrivateTable(tBean);
       
 
       params.putUtfString("status", "Created");
       send("CreatePrivateTable", params, sender);
       
 
       UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
       ulBsn.updatePrivateTableLobby("Create", tBean);
       ulBsn = null;
       
 
 
       float inPlay = Commands.appInstance.proxy.getPlayerInpaly(sender.getName());
       
       JoinUserBsn juBsn = new JoinUserBsn();
       juBsn.joinUser(inPlay, sender.getName(), sender);
       juBsn = null;
     }
   }
 }


