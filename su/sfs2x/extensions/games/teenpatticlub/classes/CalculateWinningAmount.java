/*    */ package su.sfs2x.extensions.games.teenpatticlub.classes;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CalculateWinningAmount
/*    */ {
/*    */   public void calculateWinningAmount(GameBean gameBean)
/*    */   {
/* 20 */     String wonPlayer = gameBean.getGameRoundBean().getWonPlayer();
/* 21 */     PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(wonPlayer);
/* 22 */     PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(wonPlayer);
/* 23 */     float wonAmount = 0.0F;
/*    */     
/* 25 */     float potAmount = gameBean.getGameRoundBean().getPotAmount().intValue();
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
/*    */ 
/*    */ 
/* 40 */     float playerRake = 0.0F;
/* 41 */     if (pBean != null) {
/* 42 */       playerRake = pBean.getCommission();
/*    */     }
/*    */     else
/*    */     {
/* 46 */       playerRake = 5.0F;
/*    */     }
/*    */     
/* 49 */     float rake = potAmount * playerRake / 100.0F;
/* 50 */     wonAmount = potAmount - rake;
/*    */     
/* 52 */     gameBean.getGameRoundBean().setWonAmount(wonAmount);
/*    */     
/* 54 */     if (pBean != null) {
/* 55 */       pBean.setInplay(pBean.getInplay() + wonAmount);
/* 56 */       prBean.setWonAmount(wonAmount);
/*    */     }
/*    */     else
/*    */     {
/* 60 */       Appmethods.showLog("***************");
/* 61 */       Appmethods.showLog("CWL PlayerBean is null" + wonPlayer);
/* 62 */       Appmethods.showLog("***************");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/classes/CalculateWinningAmount.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */