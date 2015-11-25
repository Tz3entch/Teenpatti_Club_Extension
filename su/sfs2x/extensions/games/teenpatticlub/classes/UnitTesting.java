 package su.sfs2x.extensions.games.teenpatticlub.classes;

 import java.text.DateFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.TimeZone;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class UnitTesting
 {
   public static void main(String[] args)
   {
     System.out.println(Appmethods.getRandomNumberString());
     
 
     String str = "India";
     if (str.contains("In")) {
       System.out.println("true");
     } else {
       System.out.println("false");
     }
     
     int am = 10;
     float am1 = am;
     String result1 = String.format("%.2f", new Object[] { Float.valueOf(am1) });
     System.out.println("result1 " + result1);
     
     float rack = 0.0F;
     rack = 21.857143F;
     String result = String.format("%.2f", new Object[] { Float.valueOf(rack) });
     System.out.println(rack);
     System.out.println(result);
     rack = Float.parseFloat(result);
     System.out.println(rack);
     
     ArrayList<Integer> cards = new ArrayList();
     cards.add(Integer.valueOf(14));
     cards.add(Integer.valueOf(15));
     cards.add(Integer.valueOf(16));
     GameLogic gl = new GameLogic();

     cards = new ArrayList();
     cards.add(Integer.valueOf(14));
     cards.add(Integer.valueOf(15));
     cards.add(Integer.valueOf(26));

     cards = new ArrayList();
     cards.add(Integer.valueOf(5));
     cards.add(Integer.valueOf(13));
     cards.add(Integer.valueOf(26));
 
     ArrayList<PlayerRoundBean> players = new ArrayList();

     PlayerRoundBean prBean1 = new PlayerRoundBean("test1");
     prBean1.getCards().add(Integer.valueOf(10));
     prBean1.getCards().add(Integer.valueOf(5));
     prBean1.getCards().add(Integer.valueOf(8));
     players.add(prBean1);
     
     PlayerRoundBean prBean2 = new PlayerRoundBean("test2");
     prBean2.getCards().add(Integer.valueOf(7));
     prBean2.getCards().add(Integer.valueOf(9));
     prBean2.getCards().add(Integer.valueOf(2));
     players.add(prBean2);
   }

   public static void dateFunction()
   {
     String dateCur = null;
     Calendar currentTime = Calendar.getInstance();
     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
     dateCur = dateFormat.format(currentTime.getTime());
     Date d1 = null;
     Date d2 = null;
     try { d2 = dateFormat.parse(dateCur); } catch (ParseException e) { Appmethods.showLog("Time sheduler >> ParseException"); }
     System.out.println("Current Time " + dateCur);
     String createdDate = "2015-03-1 12:44:49";
     int sec = 604800;
     System.out.println("Current Time Sec " + sec);
     try {
       d1 = dateFormat.parse(createdDate); } catch (Exception e) { e.printStackTrace();
     }
     long totalsec = d2.getTime() - d1.getTime();
     System.out.println(" Time MilliSec " + totalsec);
     long seconds = totalsec / 1000L;
     System.out.println(" Time Sec " + seconds);
   }
 }


