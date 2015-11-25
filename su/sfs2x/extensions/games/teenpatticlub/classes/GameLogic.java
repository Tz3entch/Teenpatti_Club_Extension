 package su.sfs2x.extensions.games.teenpatticlub.classes;

 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.Iterator;
 import java.util.Objects;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class GameLogic
 {
   public boolean isTrail(ArrayList<Integer> cards)
   {
     ArrayList<Integer> temp = new ArrayList();
     Collections.sort(cards);
     for (Iterator localIterator = cards.iterator(); localIterator.hasNext();) { int card = ((Integer)localIterator.next()).intValue();
       
       temp.add(Integer.valueOf(card % 13));
     }
     
     int count = 0;
     for (int i = 0; i < temp.size() - 1; i++)
     {
       if (temp.get(i) == temp.get(i + 1))
       {
         count++;
       }
     }
     
     if (count == 2)
     {
       return true;
     }
     return false;
   }
   
 
   public boolean isStraightFlush(ArrayList<Integer> cards)
   {
     int spades = 0;
     int hearts = 0;
     int diamonds = 0;
     int clubs = 0;
     for (int i = 0; i < cards.size(); i++)
     {
       if ((((Integer)cards.get(i)).intValue() >= 1) && (((Integer)cards.get(i)).intValue() <= 13)) {
         spades++;
       } else if ((((Integer)cards.get(i)).intValue() >= 14) && (((Integer)cards.get(i)).intValue() <= 26)) {
         hearts++;
       } else if ((((Integer)cards.get(i)).intValue() >= 27) && (((Integer)cards.get(i)).intValue() <= 39)) {
         diamonds++;
       } else if ((((Integer)cards.get(i)).intValue() >= 40) && (((Integer)cards.get(i)).intValue() <= 52)) {
         clubs++;
       }
     }
     System.out.println(spades);
     
     if ((spades == 3) || (hearts == 3) || (diamonds == 3) || (clubs == 3))
     {
       if (isSequence(cards)) {
         return true;
       }
       
       ArrayList<Integer> temp = new ArrayList();
       for (Iterator localIterator = cards.iterator(); localIterator.hasNext();) { int card = ((Integer)localIterator.next()).intValue();
         
         temp.add(Integer.valueOf(card % 13));
       }
       return isSequence(temp);
     }
     
     return false;
   }
   
   private boolean isSequence(ArrayList<Integer> cards)
   {
     Collections.sort(cards);
     for (int i = 0; i < cards.size() - 1; i++)
     {
       if (((Integer)cards.get(i)).intValue() + 1 != ((Integer)cards.get(i + 1)).intValue())
         return false;
     }
     return true;
   }
   
 
   public boolean isStraight(ArrayList<Integer> cards)
   {
     ArrayList<Integer> temp = new ArrayList();
     for (Iterator localIterator = cards.iterator(); localIterator.hasNext();) { int card = ((Integer)localIterator.next()).intValue();
       
       temp.add(Integer.valueOf(card % 13));
     }
     
     Collections.sort(temp);
     if (isSequence(temp)) {
       return true;
     }
     
     if (((Integer)temp.get(0)).intValue() == 0)
     {
       temp.remove(0);
       temp.add(Integer.valueOf(13));
       System.out.println(temp.toString());
       return isSequence(temp);
     }
     
     return false;
   }
   
 
   public boolean isFlush(ArrayList<Integer> cards)
   {
     int spades = 0;
     int hearts = 0;
     int diamonds = 0;
     int clubs = 0;
     for (int i = 0; i < cards.size(); i++)
     {
       if ((((Integer)cards.get(i)).intValue() >= 1) && (((Integer)cards.get(i)).intValue() <= 13)) {
         spades++;
       } else if ((((Integer)cards.get(i)).intValue() >= 14) && (((Integer)cards.get(i)).intValue() <= 26)) {
         hearts++;
       } else if ((((Integer)cards.get(i)).intValue() >= 27) && (((Integer)cards.get(i)).intValue() <= 39)) {
         diamonds++;
       } else if ((((Integer)cards.get(i)).intValue() >= 40) && (((Integer)cards.get(i)).intValue() <= 52))
         clubs++;
     }
     if ((spades == 3) || (hearts == 3) || (diamonds == 3) || (clubs == 3))
       return true;
     return false;
   }
   
 
   public boolean isPair(ArrayList<Integer> cards)
   {
     ArrayList<Integer> temp = new ArrayList();
     for (Iterator localIterator = cards.iterator(); localIterator.hasNext();) { int card = ((Integer)localIterator.next()).intValue();
       
       temp.add(Integer.valueOf(card % 13));
     }
     Collections.sort(temp);
     
     for (int i = 0; i < temp.size() - 1; i++)
     {
       if (temp.get(i) == temp.get(i + 1))
       {
         return true;
       }
     }
     return false;
   }
   
 
   public String compareTrails(ArrayList<PlayerRoundBean> players)
   {
     PlayerRoundBean prBean = (PlayerRoundBean)players.get(0);
     for (int i = 1; i < players.size(); i++)
     {
       prBean = checkHighestTrails(prBean, (PlayerRoundBean)players.get(i));
     }
     return prBean.getPlayerId();
   }
   
   private PlayerRoundBean checkHighestTrails(PlayerRoundBean prBean1, PlayerRoundBean prBean2)
   {
     ArrayList<Integer> arr1 = new ArrayList();
     ArrayList<Integer> arr2 = new ArrayList();
     
     for (int i = 0; i < prBean1.getCards().size(); i++)
       arr1.add(Integer.valueOf(((Integer)prBean1.getCards().get(i)).intValue() % 13));
     for (int j = 0; j < prBean2.getCards().size(); j++) {
       arr2.add(Integer.valueOf(((Integer)prBean2.getCards().get(j)).intValue() % 13));
     }
     if (((Integer)arr1.get(0)).intValue() < ((Integer)arr2.get(0)).intValue()) {
       return prBean1;
     }
     return prBean2;
   }
   
 
 
   public String compareStraightFlush(ArrayList<PlayerRoundBean> players)
   {
     PlayerRoundBean prBean = (PlayerRoundBean)players.get(0);
     for (int i = 1; i < players.size(); i++)
     {
       prBean = checkHighestStraightFlush(prBean, (PlayerRoundBean)players.get(i));
     }
     return prBean.getPlayerId();
   }
   
   private PlayerRoundBean checkHighestStraightFlush(PlayerRoundBean prBean1, PlayerRoundBean prBean2)
   {
     ArrayList<Integer> arr1 = new ArrayList();
     ArrayList<Integer> arr2 = new ArrayList();
     
     for (int i = 0; i < prBean1.getCards().size(); i++)
       arr1.add(Integer.valueOf(((Integer)prBean1.getCards().get(i)).intValue() % 13));
     for (int j = 0; j < prBean2.getCards().size(); j++) {
       arr2.add(Integer.valueOf(((Integer)prBean2.getCards().get(j)).intValue() % 13));
     }
     Collections.sort(arr1);
     Collections.sort(arr2);
     
     for (int i = 0; i < prBean1.getCards().size(); i++)
     {
       if (((Integer)arr1.get(i)).intValue() < ((Integer)arr2.get(i)).intValue())
         return prBean1;
       if (((Integer)arr1.get(i)).intValue() > ((Integer)arr2.get(i)).intValue()) {
         return prBean2;
       }
     }
     Collections.sort(prBean1.getCards());
     Collections.sort(prBean2.getCards());
     
 
 
     if (((Integer)prBean1.getCards().get(0)).intValue() < ((Integer)prBean2.getCards().get(0)).intValue()) {
       return prBean1;
     }
     return prBean2;
   }

   public String comparePair(ArrayList<PlayerRoundBean> players)
   {
     Appmethods.showLog("comparePair");
     PlayerRoundBean prBean = (PlayerRoundBean)players.get(0);
     for (int i = 1; i < players.size(); i++)
     {
       prBean = checkHighestPair(prBean, (PlayerRoundBean)players.get(i));
     }
     return prBean.getPlayerId();
   }
   
   private PlayerRoundBean checkHighestPair(PlayerRoundBean prBean1, PlayerRoundBean prBean2)
   {
     ArrayList<Integer> arr1 = new ArrayList();
     ArrayList<Integer> arr2 = new ArrayList();
     
     for (int i = 0; i < prBean1.getCards().size(); i++)
       arr1.add((prBean1.getCards().get(i)) % 13);
     for (int j = 0; j < prBean2.getCards().size(); j++) {
       arr2.add((prBean2.getCards().get(j)) % 13);
     }
     Collections.sort(arr1);
     Collections.sort(arr2);
     
     Appmethods.showLog("test1" + arr1.toString());
     Appmethods.showLog("test2" + arr2.toString());
     
     ArrayList<Integer> arr3 = new ArrayList();
     ArrayList<Integer> arr4 = new ArrayList();
     
     for (int i = 0; i < arr1.size() - 1; i++)
     {
       if (Objects.equals(arr1.get(i), arr1.get(i + 1)))
       {
         arr3.add(arr1.get(i));
       }
       
       if (Objects.equals(arr2.get(i), arr2.get(i + 1)))
       {
         arr4.add(arr2.get(i));
       }
     }
     
     if ((arr3.get(0)) < (arr4.get(0))) {
       return prBean1;
     }
     return prBean2;
   }
 }


