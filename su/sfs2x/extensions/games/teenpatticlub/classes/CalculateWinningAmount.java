package su.sfs2x.extensions.games.teenpatticlub.classes;

import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class CalculateWinningAmount
 {
   public void calculateWinningAmount(GameBean gameBean)
   {
     String wonPlayer = gameBean.getGameRoundBean().getWonPlayer();
     PlayerBean pBean = (PlayerBean)gameBean.getPlayerBeenList().get(wonPlayer);
     PlayerRoundBean prBean = (PlayerRoundBean)gameBean.getGameRoundBean().getPlayerRoundBeans().get(wonPlayer);
     float wonAmount = 0.0F;
     
     float potAmount = gameBean.getGameRoundBean().getPotAmount().intValue();

     float playerRake = 0.0F;
     if (pBean != null) {
       playerRake = pBean.getCommission();
     }
     else
     {
       playerRake = 5.0F;
     }
     
     float rake = potAmount * playerRake / 100.0F;
     wonAmount = potAmount - rake;
     
     gameBean.getGameRoundBean().setWonAmount(wonAmount);
     
     if (pBean != null) {
       pBean.setInplay(pBean.getInplay() + wonAmount);
       prBean.setWonAmount(wonAmount);
     }
     else
     {
       Appmethods.showLog("***************");
       Appmethods.showLog("CWL PlayerBean is null" + wonPlayer);
       Appmethods.showLog("***************");
     }
   }
 }


