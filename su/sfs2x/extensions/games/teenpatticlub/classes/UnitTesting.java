/*     */ package su.sfs2x.extensions.games.teenpatticlub.classes;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
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
/*     */ public class UnitTesting
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  61 */     System.out.println(Appmethods.getRandomNumberString());
/*     */     
/*     */ 
/*  64 */     String str = "India";
/*  65 */     if (str.contains("In")) {
/*  66 */       System.out.println("true");
/*     */     } else {
/*  68 */       System.out.println("false");
/*     */     }
/*     */     
/*  71 */     int am = 10;
/*  72 */     float am1 = am;
/*  73 */     String result1 = String.format("%.2f", new Object[] { Float.valueOf(am1) });
/*  74 */     System.out.println("result1 " + result1);
/*     */     
/*  76 */     float rack = 0.0F;
/*  77 */     rack = 21.857143F;
/*  78 */     String result = String.format("%.2f", new Object[] { Float.valueOf(rack) });
/*  79 */     System.out.println(rack);
/*  80 */     System.out.println(result);
/*  81 */     rack = Float.parseFloat(result);
/*  82 */     System.out.println(rack);
/*     */     
/*  84 */     ArrayList<Integer> cards = new ArrayList();
/*  85 */     cards.add(Integer.valueOf(14));
/*  86 */     cards.add(Integer.valueOf(15));
/*  87 */     cards.add(Integer.valueOf(16));
/*  88 */     GameLogic gl = new GameLogic();
/*     */     
/*     */ 
/*  91 */     cards = new ArrayList();
/*  92 */     cards.add(Integer.valueOf(14));
/*  93 */     cards.add(Integer.valueOf(15));
/*  94 */     cards.add(Integer.valueOf(26));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */     cards = new ArrayList();
/* 102 */     cards.add(Integer.valueOf(5));
/* 103 */     cards.add(Integer.valueOf(13));
/* 104 */     cards.add(Integer.valueOf(26));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */     ArrayList<PlayerRoundBean> players = new ArrayList();
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
/* 160 */     PlayerRoundBean prBean1 = new PlayerRoundBean("test1");
/* 161 */     prBean1.getCards().add(Integer.valueOf(10));
/* 162 */     prBean1.getCards().add(Integer.valueOf(5));
/* 163 */     prBean1.getCards().add(Integer.valueOf(8));
/* 164 */     players.add(prBean1);
/*     */     
/* 166 */     PlayerRoundBean prBean2 = new PlayerRoundBean("test2");
/* 167 */     prBean2.getCards().add(Integer.valueOf(7));
/* 168 */     prBean2.getCards().add(Integer.valueOf(9));
/* 169 */     prBean2.getCards().add(Integer.valueOf(2));
/* 170 */     players.add(prBean2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void dateFunction()
/*     */   {
/* 178 */     String dateCur = null;
/* 179 */     Calendar currentTime = Calendar.getInstance();
/* 180 */     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 181 */     dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
/* 182 */     dateCur = dateFormat.format(currentTime.getTime());
/* 183 */     Date d1 = null;
/* 184 */     Date d2 = null;
/* 185 */     try { d2 = dateFormat.parse(dateCur); } catch (ParseException e) { Appmethods.showLog("Time sheduler >> ParseException"); }
/* 186 */     System.out.println("Current Time " + dateCur);
/* 187 */     String createdDate = "2015-03-1 12:44:49";
/* 188 */     int sec = 604800;
/* 189 */     System.out.println("Current Time Sec " + sec);
/*     */     try {
/* 191 */       d1 = dateFormat.parse(createdDate); } catch (Exception e) { e.printStackTrace();
/*     */     }
/* 193 */     long totalsec = d2.getTime() - d1.getTime();
/* 194 */     System.out.println(" Time MilliSec " + totalsec);
/* 195 */     long seconds = totalsec / 1000L;
/* 196 */     System.out.println(" Time Sec " + seconds);
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/classes/UnitTesting.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */