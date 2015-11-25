 package su.sfs2x.extensions.games.teenpatticlub.bsn;
 
 import com.smartfoxserver.v2.entities.Room;
 import java.util.List;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class LobbyBsn
 {
   public String getLobbyRoomName()
   {
     List<Room> lobbyrooms = Commands.appInstance.getParentZone().getRoomListFromGroup("LobbyGroup");
     
     String roomName = "";
     Room lobbyRoom = null;
     
 
     for (int i = 0; i < lobbyrooms.size(); i++)
     {
       roomName = "Lobby" + (i + 1);
       lobbyRoom = Commands.appInstance.getParentZone().getRoomByName(roomName);
       
       if (lobbyRoom != null)
       {
 
         if (lobbyRoom.getUserList().size() >= 98)
         {
 
           Appmethods.showLog("Room is filled");
         }
         else
         {
           Appmethods.showLog("Room is not filled send user to that room");
           break;
         }
         
       }
       else
       {
         Appmethods.showLog("Room created");
         try {
           Commands.appInstance.makeLobbyRoom(roomName);
         }
         catch (Exception localException) {}
       }
     }
     return roomName;
   }
 }


