/*    */ package su.sfs2x.extensions.games.teenpatticlub.bsn;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.Zone;
/*    */ import java.util.List;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LobbyBsn
/*    */ {
/*    */   public String getLobbyRoomName()
/*    */   {
/* 25 */     List<Room> lobbyrooms = Commands.appInstance.getParentZone().getRoomListFromGroup("LobbyGroup");
/*    */     
/* 27 */     String roomName = "";
/* 28 */     Room lobbyRoom = null;
/*    */     
/*    */ 
/* 31 */     for (int i = 0; i < lobbyrooms.size(); i++)
/*    */     {
/* 33 */       roomName = "Lobby" + (i + 1);
/* 34 */       lobbyRoom = Commands.appInstance.getParentZone().getRoomByName(roomName);
/*    */       
/* 36 */       if (lobbyRoom != null)
/*    */       {
/*    */ 
/* 39 */         if (lobbyRoom.getUserList().size() >= 98)
/*    */         {
/*    */ 
/* 42 */           Appmethods.showLog("Room is filled");
/*    */         }
/*    */         else
/*    */         {
/* 46 */           Appmethods.showLog("Room is not filled send user to that room");
/* 47 */           break;
/*    */         }
/*    */         
/*    */       }
/*    */       else
/*    */       {
/* 53 */         Appmethods.showLog("Room created");
/*    */         try {
/* 55 */           Commands.appInstance.makeLobbyRoom(roomName);
/*    */         }
/*    */         catch (Exception localException) {}
/*    */       }
/*    */     }
/* 60 */     return roomName;
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bsn/LobbyBsn.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */