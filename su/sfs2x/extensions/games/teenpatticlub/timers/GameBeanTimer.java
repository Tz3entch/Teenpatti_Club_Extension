 package su.sfs2x.extensions.games.teenpatticlub.timers;

 import com.smartfoxserver.v2.entities.Room;
 import com.smartfoxserver.v2.entities.User;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.util.List;
 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.ActionsBsn;
 import su.sfs2x.extensions.games.teenpatticlub.bsn.UpdateLobbyBsn;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
 
 public class GameBeanTimer implements ActionListener
 {
   private String roomId;
   private String command;
   
   public GameBeanTimer(String roomId, String command)
   {
     this.roomId = roomId;
     this.command = command;
   }
   
   public void actionPerformed(ActionEvent arg0)
   {
     GameBean gameBean = Appmethods.getGameBean(this.roomId);
     
     if (gameBean != null)
     {
       gameBean.stopTimer();
       
 
 
       if (this.command.equals("StartGame"))
       {
 
         gameBean.startGame();
       }
       else if (this.command.equals("NextGame"))
       {
         Appmethods.showLog("NEXT_GAME");
         Room room = Appmethods.getRoomByName(gameBean.getRoomId());
         gameBean.setGameGenerating(true);
         gameBean.setGameID(Appmethods.generateGameID());
         ISFSObject obj = new SFSObject();
         obj.putInt("sec", 5);
         Commands.appInstance.send("QuickStart", obj, room.getUserList());
         gameBean.startTimer(5, "StartGame");
       }
       else if (this.command.equals("Turn"))
       {
         Appmethods.showLog("TURN");
         
 
         Room room = Appmethods.getRoomByName(gameBean.getRoomId());
         String player = gameBean.getGameRoundBean().getTurn();
         PlayerBean playerBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
         int remainigTime = playerBean.getTimerBank();

         Appmethods.showLog("Remaining Time " + remainigTime);

         if ((remainigTime > 0) && (!playerBean.isActive()))
         {
           Appmethods.showLog("Time Completed Start Extra Time");
           
           playerBean.setUsingExtraTime(true);
           ISFSObject sfso = new SFSObject();
           sfso.putUtfString("turn", gameBean.getGameRoundBean().getTurn());
           sfso.putInt("remainingTime", remainigTime);
           Commands.appInstance.send("ExtraTime", sfso, room.getUserList());
           
           gameBean.startTimer(remainigTime, "ExtraTime");
         }
         else
         {
           Appmethods.showLog("Time Completed Pack");
           playerBean.setTimeUpCount(playerBean.getTimeUpCount() + 1);
           ActionsBsn actionBsn = new ActionsBsn();
           actionBsn.pack(player, new SFSObject(), gameBean);
           actionBsn = null;}

       }
       else if (this.command.equals("ExtraTime"))
       {
         Appmethods.showLog("EXTRA_TIME");
         
         int remainigTime = gameBean.getRemainingSeconds();
         String player = gameBean.getGameRoundBean().getTurn();
         PlayerBean playerBean = (PlayerBean)gameBean.getPlayerBeenList().get(player);
         
         Appmethods.showLog("remaining Time >>" + remainigTime);
         
         if (remainigTime <= 0)
         {
           playerBean.setUsingExtraTime(false);
           playerBean.setTimerBank(Integer.valueOf(remainigTime));
           ActionsBsn actionBsn = new ActionsBsn();
           actionBsn.pack(player, new SFSObject(), gameBean);
           actionBsn = null;
         }
       }
       else if (this.command.equals("CloseGame"))
       {

         for (int i = 0; i < gameBean.getPlayers().size(); i++)
         {
           if (!((String)gameBean.getPlayers().get(i)).equals("null"))
           {
             gameBean.removePlayer((String)gameBean.getPlayers().get(i));
             gameBean.removePlayerBean((String)gameBean.getPlayers().get(i));
           }
         }
         
         if (gameBean.getGameType().equals("Private"))
         {
           TableBean tBean = Appmethods.getPrivateTableBean(gameBean.getRoomId());
           
           UpdateLobbyBsn ulBsn = new UpdateLobbyBsn();
           ulBsn.updatePrivateTableLobby("Delete", tBean);
           ulBsn = null;
           
           Commands.appInstance.proxy.updatePrivateTable(tBean.getPrivateTableId().intValue());
           
           Commands.appInstance.privateTables.remove(tBean);
         }
         else if (gameBean.getGameType().equals("Public"))
         {
           Appmethods.updateDynamicRoom(gameBean);
         }
         
         Room room = Appmethods.getRoomByName(gameBean.getRoomId());
         
         ISFSObject sfso = new SFSObject();
         sfso.putUtfString("status", "Game room closed try another table");
         Commands.appInstance.send("CloseGame", sfso, room.getUserList());
         
 
 
         for (int i = 0; i < room.getUserList().size(); i++)
         {
           Commands.appInstance.getApi().leaveRoom((User)room.getUserList().get(i), room);
         }
         
   int n = 0;
            List<User> players = room.getPlayersList();
            if (players.size() > 0) {
                for (User player : players) {
                    if (player.isNpc()) {
                        n++;
                    }
                }
            }
            if (n == 0) {

                System.out.println("================CLOSING GAME FROM GAMEBEANTIMER 157 ====================");

                Appmethods.removeGameBean(gameBean);
            }

        }
     }
   }
 }


