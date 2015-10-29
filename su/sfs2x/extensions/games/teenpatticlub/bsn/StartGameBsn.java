/*    */ package su.sfs2x.extensions.games.teenpatticlub.bsn;
/*    */ 
/*    */ import com.smartfoxserver.v2.entities.Room;
/*    */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*    */ import com.smartfoxserver.v2.entities.data.SFSObject;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
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
/*    */ public class StartGameBsn
/*    */ {
/*    */   public void startGame(GameBean gameBean)
/*    */   {
/* 26 */     ISFSObject sfso = new SFSObject();
/* 27 */     Room room = Appmethods.getRoomByName(gameBean.getRoomId());
/*    */     
/* 29 */     sfso = gameBean.getSFSObject();
/* 30 */     sfso = gameBean.getGameRoundBean().getSFSObject(sfso);
/* 31 */     sfso.putInt("turnTime", 60);
/*    */     
/* 33 */     Commands.appInstance.send("StartGame", sfso, room.getUserList());
/*    */     
/*    */ 
/* 36 */     gameBean.startTimer(61, "Turn");
/*    */     
/*    */ 
/* 39 */     Commands.appInstance.proxy.startGameStatus(gameBean);
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/bsn/StartGameBsn.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */