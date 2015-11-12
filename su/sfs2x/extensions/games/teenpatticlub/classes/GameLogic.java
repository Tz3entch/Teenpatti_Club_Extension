/*     */ package su.sfs2x.extensions.games.teenpatticlub.classes;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameLogic
/*     */ {
/*     */   public boolean isTrail(ArrayList<Integer> cards)
/*     */   {
/*  21 */     ArrayList<Integer> temp = new ArrayList();
/*  22 */     Collections.sort(cards);
/*  23 */     for (Iterator localIterator = cards.iterator(); localIterator.hasNext();) { int card = ((Integer)localIterator.next()).intValue();
/*     */       
/*  25 */       temp.add(Integer.valueOf(card % 13));
/*     */     }
/*     */     
/*  28 */     int count = 0;
/*  29 */     for (int i = 0; i < temp.size() - 1; i++)
/*     */     {
/*  31 */       if (temp.get(i) == temp.get(i + 1))
/*     */       {
/*  33 */         count++;
/*     */       }
/*     */     }
/*     */     
/*  37 */     if (count == 2)
/*     */     {
/*  39 */       return true;
/*     */     }
/*  41 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isStraightFlush(ArrayList<Integer> cards)
/*     */   {
/*  47 */     int spades = 0;
/*  48 */     int hearts = 0;
/*  49 */     int diamonds = 0;
/*  50 */     int clubs = 0;
/*  51 */     for (int i = 0; i < cards.size(); i++)
/*     */     {
/*  53 */       if ((((Integer)cards.get(i)).intValue() >= 1) && (((Integer)cards.get(i)).intValue() <= 13)) {
/*  54 */         spades++;
/*  55 */       } else if ((((Integer)cards.get(i)).intValue() >= 14) && (((Integer)cards.get(i)).intValue() <= 26)) {
/*  56 */         hearts++;
/*  57 */       } else if ((((Integer)cards.get(i)).intValue() >= 27) && (((Integer)cards.get(i)).intValue() <= 39)) {
/*  58 */         diamonds++;
/*  59 */       } else if ((((Integer)cards.get(i)).intValue() >= 40) && (((Integer)cards.get(i)).intValue() <= 52)) {
/*  60 */         clubs++;
/*     */       }
/*     */     }
/*  63 */     System.out.println(spades);
/*     */     
/*  65 */     if ((spades == 3) || (hearts == 3) || (diamonds == 3) || (clubs == 3))
/*     */     {
/*  67 */       if (isSequence(cards)) {
/*  68 */         return true;
/*     */       }
/*     */       
/*  71 */       ArrayList<Integer> temp = new ArrayList();
/*  72 */       for (Iterator localIterator = cards.iterator(); localIterator.hasNext();) { int card = ((Integer)localIterator.next()).intValue();
/*     */         
/*  74 */         temp.add(Integer.valueOf(card % 13));
/*     */       }
/*  76 */       return isSequence(temp);
/*     */     }
/*     */     
/*  79 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isSequence(ArrayList<Integer> cards)
/*     */   {
/*  84 */     Collections.sort(cards);
/*  85 */     for (int i = 0; i < cards.size() - 1; i++)
/*     */     {
/*  87 */       if (((Integer)cards.get(i)).intValue() + 1 != ((Integer)cards.get(i + 1)).intValue())
/*  88 */         return false;
/*     */     }
/*  90 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isStraight(ArrayList<Integer> cards)
/*     */   {
/*  96 */     ArrayList<Integer> temp = new ArrayList();
/*  97 */     for (Iterator localIterator = cards.iterator(); localIterator.hasNext();) { int card = ((Integer)localIterator.next()).intValue();
/*     */       
/*  99 */       temp.add(Integer.valueOf(card % 13));
/*     */     }
/*     */     
/* 102 */     Collections.sort(temp);
/* 103 */     if (isSequence(temp)) {
/* 104 */       return true;
/*     */     }
/*     */     
/* 107 */     if (((Integer)temp.get(0)).intValue() == 0)
/*     */     {
/* 109 */       temp.remove(0);
/* 110 */       temp.add(Integer.valueOf(13));
/* 111 */       System.out.println(temp.toString());
/* 112 */       return isSequence(temp);
/*     */     }
/*     */     
/* 115 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isFlush(ArrayList<Integer> cards)
/*     */   {
/* 121 */     int spades = 0;
/* 122 */     int hearts = 0;
/* 123 */     int diamonds = 0;
/* 124 */     int clubs = 0;
/* 125 */     for (int i = 0; i < cards.size(); i++)
/*     */     {
/* 127 */       if ((((Integer)cards.get(i)).intValue() >= 1) && (((Integer)cards.get(i)).intValue() <= 13)) {
/* 128 */         spades++;
/* 129 */       } else if ((((Integer)cards.get(i)).intValue() >= 14) && (((Integer)cards.get(i)).intValue() <= 26)) {
/* 130 */         hearts++;
/* 131 */       } else if ((((Integer)cards.get(i)).intValue() >= 27) && (((Integer)cards.get(i)).intValue() <= 39)) {
/* 132 */         diamonds++;
/* 133 */       } else if ((((Integer)cards.get(i)).intValue() >= 40) && (((Integer)cards.get(i)).intValue() <= 52))
/* 134 */         clubs++;
/*     */     }
/* 136 */     if ((spades == 3) || (hearts == 3) || (diamonds == 3) || (clubs == 3))
/* 137 */       return true;
/* 138 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isPair(ArrayList<Integer> cards)
/*     */   {
/* 144 */     ArrayList<Integer> temp = new ArrayList();
/* 145 */     for (Iterator localIterator = cards.iterator(); localIterator.hasNext();) { int card = ((Integer)localIterator.next()).intValue();
/*     */       
/* 147 */       temp.add(Integer.valueOf(card % 13));
/*     */     }
/* 149 */     Collections.sort(temp);
/*     */     
/* 151 */     for (int i = 0; i < temp.size() - 1; i++)
/*     */     {
/* 153 */       if (temp.get(i) == temp.get(i + 1))
/*     */       {
/* 155 */         return true;
/*     */       }
/*     */     }
/* 158 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public String compareTrails(ArrayList<PlayerRoundBean> players)
/*     */   {
/* 164 */     PlayerRoundBean prBean = (PlayerRoundBean)players.get(0);
/* 165 */     for (int i = 1; i < players.size(); i++)
/*     */     {
/* 167 */       prBean = checkHighestTrails(prBean, (PlayerRoundBean)players.get(i));
/*     */     }
/* 169 */     return prBean.getPlayerId();
/*     */   }
/*     */   
/*     */   private PlayerRoundBean checkHighestTrails(PlayerRoundBean prBean1, PlayerRoundBean prBean2)
/*     */   {
/* 174 */     ArrayList<Integer> arr1 = new ArrayList();
/* 175 */     ArrayList<Integer> arr2 = new ArrayList();
/*     */     
/* 177 */     for (int i = 0; i < prBean1.getCards().size(); i++)
/* 178 */       arr1.add(Integer.valueOf(((Integer)prBean1.getCards().get(i)).intValue() % 13));
/* 179 */     for (int j = 0; j < prBean2.getCards().size(); j++) {
/* 180 */       arr2.add(Integer.valueOf(((Integer)prBean2.getCards().get(j)).intValue() % 13));
/*     */     }
/* 182 */     if (((Integer)arr1.get(0)).intValue() < ((Integer)arr2.get(0)).intValue()) {
/* 183 */       return prBean1;
/*     */     }
/* 185 */     return prBean2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String compareStraightFlush(ArrayList<PlayerRoundBean> players)
/*     */   {
/* 192 */     PlayerRoundBean prBean = (PlayerRoundBean)players.get(0);
/* 193 */     for (int i = 1; i < players.size(); i++)
/*     */     {
/* 195 */       prBean = checkHighestStraightFlush(prBean, (PlayerRoundBean)players.get(i));
/*     */     }
/* 197 */     return prBean.getPlayerId();
/*     */   }
/*     */   
/*     */   private PlayerRoundBean checkHighestStraightFlush(PlayerRoundBean prBean1, PlayerRoundBean prBean2)
/*     */   {
/* 202 */     ArrayList<Integer> arr1 = new ArrayList();
/* 203 */     ArrayList<Integer> arr2 = new ArrayList();
/*     */     
/* 205 */     for (int i = 0; i < prBean1.getCards().size(); i++)
/* 206 */       arr1.add(Integer.valueOf(((Integer)prBean1.getCards().get(i)).intValue() % 13));
/* 207 */     for (int j = 0; j < prBean2.getCards().size(); j++) {
/* 208 */       arr2.add(Integer.valueOf(((Integer)prBean2.getCards().get(j)).intValue() % 13));
/*     */     }
/* 210 */     Collections.sort(arr1);
/* 211 */     Collections.sort(arr2);
/*     */     
/* 213 */     for (int i = 0; i < prBean1.getCards().size(); i++)
/*     */     {
/* 215 */       if (((Integer)arr1.get(i)).intValue() < ((Integer)arr2.get(i)).intValue())
/* 216 */         return prBean1;
/* 217 */       if (((Integer)arr1.get(i)).intValue() > ((Integer)arr2.get(i)).intValue()) {
/* 218 */         return prBean2;
/*     */       }
/*     */     }
/* 221 */     Collections.sort(prBean1.getCards());
/* 222 */     Collections.sort(prBean2.getCards());
/*     */     
/*     */ 
/*     */ 
/* 226 */     if (((Integer)prBean1.getCards().get(0)).intValue() < ((Integer)prBean2.getCards().get(0)).intValue()) {
/* 227 */       return prBean1;
/*     */     }
/* 229 */     return prBean2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String comparePair(ArrayList<PlayerRoundBean> players)
/*     */   {
/* 252 */     Appmethods.showLog("comparePair");
/* 253 */     PlayerRoundBean prBean = (PlayerRoundBean)players.get(0);
/* 254 */     for (int i = 1; i < players.size(); i++)
/*     */     {
/* 256 */       prBean = checkHighestPair(prBean, (PlayerRoundBean)players.get(i));
/*     */     }
/* 258 */     return prBean.getPlayerId();
/*     */   }
/*     */   
/*     */   private PlayerRoundBean checkHighestPair(PlayerRoundBean prBean1, PlayerRoundBean prBean2)
/*     */   {
/* 263 */     ArrayList<Integer> arr1 = new ArrayList();
/* 264 */     ArrayList<Integer> arr2 = new ArrayList();
/*     */     
/* 266 */     for (int i = 0; i < prBean1.getCards().size(); i++)
/* 267 */       arr1.add(Integer.valueOf(((Integer)prBean1.getCards().get(i)).intValue() % 13));
/* 268 */     for (int j = 0; j < prBean2.getCards().size(); j++) {
/* 269 */       arr2.add(Integer.valueOf(((Integer)prBean2.getCards().get(j)).intValue() % 13));
/*     */     }
/* 271 */     Collections.sort(arr1);
/* 272 */     Collections.sort(arr2);
/*     */     
/* 274 */     Appmethods.showLog("test1" + arr1.toString());
/* 275 */     Appmethods.showLog("test2" + arr2.toString());
/*     */     
/* 277 */     ArrayList<Integer> arr3 = new ArrayList();
/* 278 */     ArrayList<Integer> arr4 = new ArrayList();
/*     */     
/* 280 */     for (int i = 0; i < arr1.size() - 1; i++)
/*     */     {
/* 282 */       if (arr1.get(i) == arr1.get(i + 1))
/*     */       {
/* 284 */         arr3.add((Integer)arr1.get(i));
/*     */       }
/*     */       
/* 287 */       if (arr2.get(i) == arr2.get(i + 1))
/*     */       {
/* 289 */         arr4.add((Integer)arr2.get(i));
/*     */       }
/*     */     }
/*     */     
/* 293 */     if (((Integer)arr3.get(0)) < ((Integer)arr4.get(0))) {
/* 294 */       return prBean1;
/*     */     }
/* 296 */     return prBean2;
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/classes/GameLogic.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */