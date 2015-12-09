 package su.sfs2x.extensions.games.teenpatticlub.proxy;

 import com.smartfoxserver.bitswarm.sessions.ISession;
 import com.smartfoxserver.v2.db.IDBManager;
 import com.smartfoxserver.v2.entities.Zone;
 import com.smartfoxserver.v2.entities.data.ISFSArray;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSArray;
 import com.smartfoxserver.v2.entities.data.SFSObject;

 import java.sql.*;
 import java.text.DateFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.*;
 import java.util.Date;

 import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
 import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class SQLProxy
 {
   Connection con = null;
   
   public IDBManager dbManager;
   
   public SQLProxy(Zone zone)
   {
     this.dbManager = zone.getDBManager();
     try
     {
       this.con = this.dbManager.getConnection();
       Appmethods.showSQLLog("SQL Connection Success ");
     }
     catch (Exception e) {
       Appmethods.showSQLLog("SQL Connection Failed :" + e.toString());
     }
   }
   
 
   public ISFSObject getUserDetails(String nickname)
   {
     String sqlSelect = "select * from user_master where userid=? and id_type='player';";
     Appmethods.showSQLLog(" getUserPassword >> : " + sqlSelect);
     
 
 
 
     ISFSObject sfso = new SFSObject();
     
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlSelect);
       pstmt.setString(1, nickname);
       
       ResultSet rs = pstmt.executeQuery();
       while (rs.next())
       {
 
 
 
         sfso.putUtfString("username", rs.getString("userid"));
         sfso.putUtfString("password", rs.getString("password"));
         sfso.putUtfString("status", rs.getString("status"));
       }
       rs.close();
       pstmt.close();
     }
     catch (Exception e) {
       Appmethods.showSQLLog(" getUserPassword >> : " + e.toString());
     }
     
 
 
 
 
 
     return sfso;
   }
   
   public void getPublicTables() {
     String sql = "Select * from public_tables;";
     Appmethods.showSQLLog("Sql " + sql);
     try {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       ResultSet res = stmt.executeQuery();
       
       if (res.next()) {
         do {
           TableBean tBean = new TableBean();
           tBean.set_id(Integer.valueOf(res.getInt("id")));
           tBean.set_boot(Integer.valueOf(res.getInt("boot")));
           tBean.set_challLimit(Integer.valueOf(res.getInt("chall_limit")));
           tBean.set_potLimit(Integer.valueOf(res.getInt("pot_limit")));
           Commands.appInstance.publicTables.add(tBean);
         } while (res.next());
       }
       res.close();
       stmt.close();
     }
     catch (SQLException exception)
     {
       Appmethods.showSQLLog("getPublicTables SQL Connection Failed :" + exception.toString());
     }
   }
   
   public ISFSObject getPlayerProfile(String player) {
     String sql = "select * from user_master where userid =?;";
     
     ISFSObject sfso = new SFSObject();
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         sfso.putUtfString("userId", res.getString("userid"));
         Appmethods.showSQLLog("getPlayerProfile >> Chips" + res.getFloat("chips"));
         sfso.putFloat("chips", Appmethods.setFloatFormat(res.getFloat("chips")));
         Appmethods.showSQLLog("getPlayerProfile >> Chips" + sfso.getFloat("chips"));
         sfso.putUtfString("idType", res.getString("id_type"));
         sfso.putUtfString("avatar", res.getString("avatar"));
         sfso.putUtfString("fullName", res.getString("full_name"));
         sfso.putUtfString("mobile", res.getString("mobile"));
         sfso.putUtfString("email", res.getString("email"));
         sfso.putUtfString("nationality", res.getString("nationality"));
         sfso.putUtfString("gender", res.getString("gender"));
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println(" SQLProxyError >>  loginDate  >> :" + e.toString());
     }
     
     return sfso;
   }
   
   public String getPlayerAvatar(String player)
   {
     String sql = "select avatar from user_master where userid =?;";
     String avatar = "";
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         avatar = res.getString("avatar");
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println(" SQLProxyError >>  loginDate  >> :" + e.toString());
     }
     return avatar;
   }
   
   public float getPlayerCommission(String player)
   {
     String sql = "select game_comm from game_master_settings where id =1;";
     float commission = 0.0F;
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       //stmt.setString(1, player);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         commission = res.getFloat("game_comm");
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println(" SQLProxyError >>  loginDate  >> :" + e.toString());
     }
     return commission;
   }

   public float getPrivateCommission()
   {
     String sql = "select pvttable_cut_point from game_master_settings where id =1;";
     float commission = 0.0F;
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       //stmt.setString(1, player);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         commission = res.getFloat("pvttable_cut_point");
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println(" SQLProxyError >>  loginDate  >> :" + e.toString());
     }
     return commission;
   }

 
   public float getPlayerInpaly(String player)
   {
     String sql = "select chips from user_master where userid =?;";
     float chips = 0.0F;
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         chips = res.getFloat("chips");
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println(" SQLProxyError >>  loginDate  >> :" + e.toString());
     }
     return chips;
   }

 
 
 
 
 
   public boolean isPrivateTableAvail(String player)
   {
     String sql = "select * from private_tables where status=0 and owner=?;";
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       ResultSet res = stmt.executeQuery();
       if (res.next())
       {
         return true;
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println(" SQLProxyError >>  isPrivateTableAvail  >> :" + e.toString());
     }
     
     return false;
   }
   
   public void getPrivateTables() {
     String sql = "Select * from private_tables where status=0;";
     Appmethods.showSQLLog("Sql " + sql);
     try {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       ResultSet res = stmt.executeQuery();
       
       if (res.next()) {
         do {
           TableBean tBean = new TableBean();
           tBean.set_id(Integer.valueOf(res.getInt("id")));
           tBean.set_boot(Integer.valueOf(res.getInt("boot")));
           tBean.set_challLimit(Integer.valueOf(res.getInt("chall_limit")));
           tBean.set_potLimit(Integer.valueOf(res.getInt("pot_limit")));
           tBean.set_roomId(res.getString("owner"));
           tBean.set_password(res.getString("password"));
           tBean.set_type("Private");
           tBean.set_startDate(res.getString("created_datetime"));
           String createdDate = res.getString("created_datetime");
           
 
           Date d1 = null;
           Date d2 = null;
           String currDate = null;
           Calendar currentTime = Calendar.getInstance();
           DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
           currDate = dateFormat.format(currentTime.getTime());
           try { d2 = dateFormat.parse(currDate); } catch (ParseException e) { Appmethods.showSQLLog("Time sheduler >> ParseException"); }
           try { d1 = dateFormat.parse(createdDate); } catch (Exception e) { e.printStackTrace();
           }
           
           long totalsec = d2.getTime() - d1.getTime();
           long seconds = totalsec / 1000L;
           int sec = (int)(604800L - seconds);
           
           if (sec > 0)
           {
 
             tBean.startTimer(sec);
             Commands.appInstance.privateTables.add(tBean);
 
           }
           else
           {
             updatePrivateTable(res.getInt("id"));
           }
         } while (res.next());
       }
       res.close();
       stmt.close();
     }
     catch (SQLException exception)
     {
       Appmethods.showSQLLog("getPrivateTables SQL Connection Failed :" + exception.toString());
     }
   }
   
 
 
 
   public void createPrivateTable(TableBean tBean)
   {
     String sqlInsert = "insert into private_tables(boot,chall_limit,pot_limit,owner,password,created_datetime,status) values(?,?,?,?,?,?,?)";
     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
     
     String sql = "select id from private_tables where owner =? and password=? and status=?;";
     
 
 
 
 
 
 
 
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setInt(1, tBean.get_boot().intValue());
       pstmt.setInt(2, tBean.get_challLimit().intValue());
       pstmt.setInt(3, tBean.get_potLimit().intValue());
       pstmt.setString(4, tBean.get_roomId());
       pstmt.setString(5, tBean.get_password());
       pstmt.setString(6, tBean.get_startDate());
       pstmt.setInt(7, 0);
       
       pstmt.executeUpdate();
       pstmt.close();
       
 
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, tBean.get_roomId());
       stmt.setString(2, tBean.get_password());
       stmt.setInt(3, 0);
       
       int id = 0;
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         id = res.getInt("id");
       }
       tBean.setPrivateTableId(Integer.valueOf(id));
       res.close();
       stmt.close();
     }
     catch (SQLException e) {
       Appmethods.showSQLLog(" SQLProxyError >> createPrivateTable  >> :" + e.toString());
     }
   }
   
   private String getId(String sql) {
     String t_id = Appmethods.getRandomNumberString();
     int id = 0;
     try {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         id = res.getInt("id");
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e) {
       System.out.println("SQLProxyError >> getId >> :" + e.toString());
     }
     
     t_id = t_id + (id + 1);
     
     return t_id;
   }
   
 
 
   public void insertPrivateTableHistory(GameBean gameBean, String player, String type, int privateTableId, float commission)
   {
     String sql = "Select * from private_tbl_history where private_table_Id=? and userid=?;";
     Appmethods.showSQLLog("Sql " + sql);
     boolean bool = false;
     //float commission = getPrivateCommission();
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setInt(1, privateTableId);
       stmt.setString(2, player);
       
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         bool = true;
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e) {
       System.out.println("SQLProxyError >> insertPrivateTableHistory >> :" + e.toString());
     }
     
     if (!bool)
     {
 
 
       String sqlUpdate = "update user_master set user_master.chips = (user_master.chips - ?)  where user_master.userid = ?;";
       Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
       
 
 
       sql = "Select * from private_tbl_history ORDER BY id DESC LIMIT 1";
       Appmethods.showSQLLog("Sql " + sql);
       String t_id = getId(sql);
       
       String sqlInsert = "Insert INTO private_tbl_history(transaction_id,private_table_Id,gameid,roomid,userid,deducted_amount,tran_type,date_time) VALUES (?,?,?,?,?,?,?,?);";
       
 
       Appmethods.showSQLLog(" SQLProxy >> startGameStatus >> sqlInsert >> :" + sqlInsert);
       
       try
       {
         PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
         stmtWin.setFloat(1, commission);
         stmtWin.setString(2, player);
         
         stmtWin.executeUpdate();
         stmtWin.close();
         
 
         PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
         pstmt.setString(1, "T" + t_id);
         pstmt.setInt(2, privateTableId);
         pstmt.setString(3, gameBean.getGameID());
         pstmt.setString(4, gameBean.getRoomId());
         pstmt.setString(5, player);
         pstmt.setFloat(6, commission);
         pstmt.setString(7, type);
         pstmt.setString(8, Appmethods.getDateTime());
         
         pstmt.executeUpdate();
         pstmt.close();
       }
       catch (SQLException e) {
         System.out.println("SQLProxyError >> startGameStatus game_history>> :" + e.toString());
       }
     }
   }
   
 
   public void updatePrivateTable(int id)
   {
     String sqlUpdate = "update private_tables set status=1 where id=?;";
     Appmethods.showSQLLog(" SQLProxy >> updatePrivateTable >> sqlUpdate >> :" + sqlUpdate);
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlUpdate);
       
       pstmt.setInt(1, id);
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e) {
       Appmethods.showSQLLog(" SQLProxy >> updatePrivateTable >> :" + e.toString());
     }
   }
   
 
 
   public void startGameStatus(GameBean gameBean)
   {
     String sqlInsert = "Insert INTO game_history(game_id,room_id,players_count,game_type,player_names,player_cards,start_time,updated_by,status) VALUES (?,?,?,?,?,?,?,?,?);";
     
 
     Appmethods.showSQLLog(" SQLProxy >> startGameStatus >> sqlInsert >> :" + sqlInsert);
     
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setString(1, gameBean.getGameID());
       pstmt.setString(2, gameBean.getRoomId());
       pstmt.setInt(3, gameBean.getGameRoundBean().getActivePlayersCount());
       pstmt.setString(4, gameBean.getGameType());
       pstmt.setString(5, gameBean.getGameRoundBean().getPlayers().toString());
       pstmt.setString(6, gameBean.getGameRoundBean().getPlayerCardsString());
       pstmt.setString(7, gameBean.getGameStartDate());
       pstmt.setString(8, "Admin");
       pstmt.setInt(9, 0);
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e) {
       System.out.println("SQLProxyError >> startGameStatus game_history>> :" + e.toString());
     }
     
 
     sqlInsert = "Insert INTO game_status(gameid,status,entry_date,updated_by) VALUES (?,?,?,?);";
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setString(1, gameBean.getGameID());
       pstmt.setInt(2, 0);
       pstmt.setString(3, gameBean.getGameStartDate());
       pstmt.setString(4, "Admin");
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e) {
       System.out.println("SQLProxyError >> startGameStatus >> game_status>> :" + e.toString());
     }
     
 
     for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
     {
       PlayerRoundBean bean = (PlayerRoundBean)e.nextElement();
       insertUserPlaying(bean.getPlayerId(), gameBean.getGameID(), gameBean.getRoomId());
     }
   }
   
 
 
   public void closeGameStatus(GameBean gameBean)
   {
     String currDate = Appmethods.getDateTime();
     
 
     String sqlUpdate = "update game_history set won_amount=?, won_player=?, end_time=?, status=? where game_id=?;";
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlUpdate);
       pstmt.setFloat(1, gameBean.getGameRoundBean().getWonAmount());
       pstmt.setString(2, gameBean.getGameRoundBean().getWonPlayer());
       pstmt.setString(3, currDate);
       pstmt.setInt(4, 1);
       pstmt.setString(5, gameBean.getGameID());
       
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e) {
       System.out.println("SQLProxyError >> closeGameStatus game_status>> :" + e.toString());
     }
     
 
 
     String sql = "update game_status set status=? where gameid= ?;";
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sql);
       pstmt.setInt(1, 1);
       pstmt.setString(2, gameBean.getGameID());
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError  >>  closeGameStatus >> :" + e.toString());
     }
     
 
     for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
     {
       PlayerRoundBean bean = (PlayerRoundBean)e.nextElement();
       deleteUserPlaying(bean.getPlayerId(), gameBean.getGameID());
       
       if ((!bean.isLeaveTable()) && (!bean.isSitOut()))
       {
         PlayerBean pbean = (PlayerBean)gameBean.getPlayerBeenList().get(bean.getPlayerId());
         updateUserChips(bean.getPlayerId(), Float.valueOf(pbean.getInplay()));
       }
     }
     
 
     sqlUpdate = "update commission_master set status=? where gameid=?;";
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlUpdate);
       pstmt.setInt(1, 1);
       pstmt.setString(2, gameBean.getGameID());
       
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e) {
       System.out.println("SQLProxyError >> closeGameStatus commission_master>> :" + e.toString());
     }
   }
   
   private void insertUserPlaying(String player, String gameid, String roomId)
   {
     String sqlInsert = "Insert INTO users_playing VALUES (?,?,?);";
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setString(1, player);
       pstmt.setString(2, gameid);
       pstmt.setString(3, roomId);
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  insertUserPlaying  :" + e.toString());
     }
   }
   
   public void deleteUserPlaying(String player, String gameid) {
     String sqlDelete = "delete from users_playing where username= ? and game_id= ?;";
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlDelete);
       pstmt.setString(1, player);
       pstmt.setString(2, gameid);
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >> deleteUserPlaying >> :" + e.toString());
     }
   }
   
 
   public void insertUserAction(String roomid, String player, int amount, String move_type, float playerRake)
   {
     String sql = "select distributor_id  from user_master where userid=?;";
     Appmethods.showSQLLog(" SQLProxy >>  sql >> :" + sql);
     
     String dis_id = "TD3021785";
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         dis_id = res.getString("distributor_id");
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  insertUserAction user_master>> :" + e.toString());
     }
     
 
 
     GameBean gameBean = Appmethods.getGameBean(roomid);
     float commission = 0.0F;
     
     Appmethods.showSQLLog("insertUserAction>> Type " + move_type);
     
 
     if (move_type.equals("Pack"))
     {
       commission = 0.0F;
     }
     else
     {
       commission = amount * playerRake / 100.0F;
       commission = Appmethods.setFloatFormat(commission);
     }
     
     sql = "Select * from commission_master ORDER BY id DESC LIMIT 1";
     Appmethods.showSQLLog("Sql " + sql);
     String t_id = getId(sql);
     
     String sqlInsert = "Insert INTO commission_master(gameid,transaction_id,roomid,handid,userid,distributor_id,amount,move_type,game_date,commission,type,updated_by,tempflag) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
     
     Appmethods.showSQLLog("Date " + gameBean.getGameStartDate());
     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setString(1, gameBean.getGameID());
       pstmt.setString(2, "T" + t_id);
       pstmt.setString(3, gameBean.getRoomId());
       pstmt.setString(4, "handid_" + gameBean.getGameRoundBean().getHandId());
       pstmt.setString(5, player);
       pstmt.setString(6, dis_id);
       pstmt.setFloat(7, Appmethods.setFloatFormat(amount));
       pstmt.setString(8, move_type);
       pstmt.setString(9, gameBean.getGameStartDate());
       pstmt.setFloat(10, commission);
       pstmt.setString(11, "DR");
       pstmt.setString(12, "Admin");
       pstmt.setInt(13, 0);
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  insertUserAction  :" + e.toString());
     }
   }
   
 
   public void updateUserChips(String player, Float chips)
   {
     String sqlUpdate = "update user_master set user_master.chips = ?  where user_master.userid = ?;";
     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
     
     try
     {
       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
       stmtWin.setFloat(1, Appmethods.setFloatFormat(chips.floatValue()));
       stmtWin.setString(2, player);
       
       stmtWin.executeUpdate();
       stmtWin.close();
 
     }
     catch (SQLException e)
     {
       Appmethods.showSQLLog(" SQLProxyError >> updateUserChips  >> :" + e.toString());
     }
   }
   
   public ArrayList<String> getCurrentRunningGames(String player)
   {
     String sql = "select users_playing.room_id from users_playing where users_playing.username=?";
     ArrayList<String> games = new ArrayList();
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         games.add(res.getString("room_id"));
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  getCurrentRunningGames  >> :" + e.toString());
     }
     return games;
   }

             public void registerUser(String username, String password, String dob, String email, String mobile)
             {
            	 String sqlInsert = "Insert INTO user_master(userid,password,id_type,subid_type,avatar,mobile,email,dob,created,signup,distributor_id,reference_id,commission,pwd_stat,status,version_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            	 
            	 try
            	 {
            	 PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
            	 pstmt.setString(1, username);
            	 pstmt.setString(2, password);
            	 pstmt.setString(3, "player");
            	 pstmt.setString(4, "player");
            	 pstmt.setString(5, "avatar-3.png");
            	 pstmt.setString(6, mobile);
            	 pstmt.setString(7, email);
            	 pstmt.setString(8, dob);
            	 pstmt.setString(9, "BYAPP");
            	 pstmt.setString(10, Appmethods.getDateTime());
            	 pstmt.setString(11, "TD5886671");
            	 pstmt.setString(12, "SD8464091");
            	 pstmt.setString(13, "5");
            	 pstmt.setString(14, "1");
            	 pstmt.setString(15, "Y");
            	 pstmt.setString(16, "3.0");
            	 pstmt.executeUpdate();
            	 pstmt.close();
            	 }
            	 catch (SQLException e)
            	 {
            	 System.out.println("SQLProxyError >>  registerUser  user_master:" + e.toString());
            	 }
            	 
             }
   
 
   public void redeemChips(String player, float amount)
   {
     String sqlUpdate = "update user_master set user_master.chips = (user_master.chips - ?)  where user_master.userid = ?;";
     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
     
     try
     {
       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
       stmtWin.setFloat(1, Appmethods.setFloatFormat(amount));
       stmtWin.setString(2, player);
       
       stmtWin.executeUpdate();
       stmtWin.close();
 
     }
     catch (SQLException e)
     {
       Appmethods.showSQLLog(" SQLProxyError >> redeemChips  user_master1>> :" + e.toString());
     }
     
     String sql = "select chips,distributor_id  from user_master where userid=?;";
     Appmethods.showSQLLog(" SQLProxy >>  sql >> :" + sql);
     
     float balanceChips = 0.0F;
     String dis_id = "TD3021785";
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         balanceChips = res.getFloat("chips");
         balanceChips = Appmethods.setFloatFormat(balanceChips);
         dis_id = res.getString("distributor_id");
       }
       
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  redeemChips user_master2>> :" + e.toString());
     }
     
     sqlUpdate = "update user_master set user_master.chips = (user_master.chips + ?)  where user_master.userid=?;";
     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
     
     try
     {
       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
       stmtWin.setFloat(1, Appmethods.setFloatFormat(amount));
       stmtWin.setString(2, dis_id);
       
       stmtWin.executeUpdate();
       stmtWin.close();
 
     }
     catch (SQLException e)
     {
       Appmethods.showSQLLog(" SQLProxyError >> redeemChips  user_master3>> :" + e.toString());
     }
     
 
 
     String sqlInsert = "Insert INTO transaction_history(userid,transaction_id,from_to,points,crdr,trn_type,balance_upd,update_date) values (?,?,?,?,?,?,?,?);";
     
     Appmethods.showSQLLog("Balance Chips " + balanceChips);
     
     sql = "Select * from transaction_history ORDER BY id DESC LIMIT 1";
     Appmethods.showSQLLog("Sql " + sql);
     String t_id = getId(sql);
     
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setString(1, player);
       pstmt.setString(2, "TR"+t_id);
       pstmt.setString(3, dis_id);
       pstmt.setFloat(4, Appmethods.setFloatFormat(amount));
       pstmt.setString(5, "Dr");
       pstmt.setString(6, "Transfer");
       pstmt.setFloat(7, Appmethods.setFloatFormat(balanceChips));
       pstmt.setString(8, Appmethods.getDateTime());
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  redeemChips  transaction_history:" + e.toString());
     }
   }
   
 
 
   public ISFSArray getTransactionHistory(String player)
   {
     ISFSArray sfsArray = new SFSArray();
     
     String sql = "select * from transaction_history where userid=? or from_to=? order by id desc";
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       stmt.setString(2, player);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         ISFSObject sfso = new SFSObject();
         sfso.putInt("id", res.getInt("id"));
         sfso.putUtfString("from/to", res.getString("from_to"));
         sfso.putFloat("points", Appmethods.setFloatFormat(res.getFloat("points")));
         sfso.putUtfString("crdr", res.getString("crdr"));
         sfso.putUtfString("transactionType", res.getString("trn_type"));
         float balance = res.getFloat("balance_upd");
         
         Appmethods.showSQLLog("Balance before " + Appmethods.setFloatFormat(balance));
         balance = Appmethods.setFloatFormat(balance);
         Appmethods.showSQLLog("Balance after " + balance);
         sfso.putFloat("balanceUpdate", balance);
         sfso.putUtfString("updateDate", res.getString("update_date"));
         sfsArray.addSFSObject(sfso);
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  getTransactionHistory  >> :" + e.toString());
     }
     return sfsArray;
   }
   
 
 
 
   public void updateProfile(String player, String mobile, String email, String password)
   {
     if ((password == null) || (password.equals("")) || (password.length() == 0))
     {
       String sqlUpdate = "update user_master set user_master.mobile=?, user_master.email=?  where user_master.userid=?;";
       Appmethods.showSQLLog(" SQLProxy >>  updateProfile >> :" + sqlUpdate);
       
       try
       {
         PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
         stmtWin.setString(1, mobile);
         stmtWin.setString(2, email);
         stmtWin.setString(3, player);
         stmtWin.executeUpdate();
         stmtWin.close();
       }
       catch (SQLException e)
       {
         Appmethods.showSQLLog(" SQLProxyError >> updateProfile  >> :" + e.toString());
       }
     }
     else
     {
       String sqlUpdate = "update user_master set user_master.password = ?, user_master.mobile=?, user_master.email=?  where user_master.userid=?;";
       Appmethods.showSQLLog(" SQLProxy >>  updateProfile >> :" + sqlUpdate);
       
       try
       {
         PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
         stmtWin.setString(1, password);
         stmtWin.setString(2, mobile);
         stmtWin.setString(3, email);
         stmtWin.setString(4, player);
         stmtWin.executeUpdate();
         stmtWin.close();
       }
       catch (SQLException e)
       {
         Appmethods.showSQLLog(" SQLProxyError >> updateProfile  >> :" + e.toString());
       }
     }
   }
   
   public ISFSArray getDealers()
   {
     ISFSArray sfsArray = new SFSArray();
     
     String sql = "select * from dealer_master";
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         ISFSObject sfso = new SFSObject();
         
         sfso.putInt("id", res.getInt("dealerid"));
         sfso.putUtfString("dealerName", res.getString("dealer_name"));
         sfso.putDouble("cost", res.getDouble("dealer_cost"));
         sfso.putUtfString("imageUrl", res.getString("image_url"));
         sfso.putUtfString("status", res.getString("status"));
         
         sfsArray.addSFSObject(sfso);
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  getDealers  >> :" + e.toString());
     }
     return sfsArray;
   }
   
 
 
   public void changeDealer(String player, int dealerId, float dealerCost, String gameid)
   {
     String sql = "Select * from dealer_data ORDER BY id DESC LIMIT 1";
     Appmethods.showSQLLog("Sql " + sql);
     String t_id = getId(sql);
     
     String sqlInsert = "Insert INTO dealer_data(userid,transaction_id,dealerid,cost,gameid,update_date) values (?,?,?,?,?,?);";
     
     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       
       pstmt.setString(1, player);
       pstmt.setString(2, "T" + t_id);
       pstmt.setInt(3, dealerId);
       pstmt.setFloat(4, Appmethods.setFloatFormat(dealerCost));
       pstmt.setString(5, gameid);
       pstmt.setString(6, Appmethods.getDateTime());
       
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  changeDealer >> dealer_data:" + e.toString());
     }
     
 
     cutUserChips(player, dealerCost);
   }
   
 
   public void cutUserChips(String player, float amount)
   {
     String sqlUpdate = "update user_master set user_master.chips = (user_master.chips - ?)  where user_master.userid = ?;";
     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
     try
     {
       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
       stmtWin.setFloat(1, Appmethods.setFloatFormat(amount));
       stmtWin.setString(2, player);
       stmtWin.executeUpdate();
       stmtWin.close();
     }
     catch (SQLException e)
     {
       Appmethods.showSQLLog(" SQLProxyError >> changeDealer  >> :" + e.toString());
     }
   }
   
   public void insertDealerTip(String player, float amount, String gameId)
   {
     String sql = "Select * from user_tip ORDER BY id DESC LIMIT 1";
     Appmethods.showSQLLog("Sql " + sql);
     String t_id = getId(sql);
     
     String sqlInsert = "Insert INTO user_tip(userid,transaction_id,gameid,tip,created_datetime) values (?,?,?,?,?);";
     
     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setString(1, player);
       pstmt.setString(2, "T" + t_id);
       pstmt.setString(3, gameId);
       pstmt.setFloat(4, Appmethods.setFloatFormat(amount));
       pstmt.setString(5, Appmethods.getDateTime());
       
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  insertDealerTip  user_tip:" + e.toString());
     }
     
 
     cutUserChips(player, amount);
   }
   
 
 
   public void insertUserLastSession(PlayerBean pBean, int status)
   {
     String sqlInsert = "Insert INTO user_last_session(userid,previous_balance,won_or_lost,totalHands,wonHands,status,created_datetime) values (?,?,?,?,?,?,?);";
     
     float amount = 0.0F;
     for (int i = 0; i < pBean.getAmounts().size(); i++)
     {
       Appmethods.showLog("Amount " + pBean.getPlayerId() + " " + pBean.getAmounts().get(i));
       amount += ((Float)pBean.getAmounts().get(i)).floatValue();
     }
     
     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
     
     Appmethods.showSQLLog(" insertUserLastSession >>  Amount >> :" + amount);
     
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setString(1, pBean.getPlayerId());
       pstmt.setFloat(2, pBean.getStartInplay());
       pstmt.setFloat(3, Appmethods.setFloatFormat(amount));
       pstmt.setInt(4, pBean.getTotalHands());
       pstmt.setInt(5, pBean.getWonHands());
       pstmt.setInt(6, status);
       pstmt.setString(7, Appmethods.getDateTime());
       
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  insertUserLastSession  user_last_session:" + e.toString());
     }
   }
   
   public void updateUserLastSession(Integer id)
   {
     String sqlUpdate = "update user_last_session set status = ? where id=?;";
     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
     try
     {
       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
       stmtWin.setInt(1, 0);
       stmtWin.setInt(2, id.intValue());
       stmtWin.executeUpdate();
       stmtWin.close();
     }
     catch (SQLException e)
     {
       Appmethods.showSQLLog(" SQLProxyError >> updateUserLastSession  >> :" + e.toString());
     }
   }
   
   public ISFSObject getUserLastSessionInfo(String player, int send)
   {
     Integer id = Integer.valueOf(0);
     ISFSObject sfso = null;
     String sql = "select * from user_last_session where userid=? and status=? order by id desc limit 1";
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       stmt.setInt(2, send);
       ResultSet res = stmt.executeQuery();
       
       while (res.next())
       {
         sfso = new SFSObject();
         id = Integer.valueOf(res.getInt("id"));
         sfso.putInt("id", res.getInt("id"));
         sfso.putUtfString("player", player);
         sfso.putFloat("won_or_lost", Appmethods.setFloatFormat(res.getFloat("won_or_lost")));
         sfso.putInt("totalHands", res.getInt("totalHands"));
         sfso.putInt("wonHands", res.getInt("wonHands"));
         sfso.putUtfString("createdDate", res.getString("created_datetime"));
       }
       
       if (sfso != null)
       {
         sfso.putUtfString("status", "datafound");
         updateUserLastSession(id);
       }
       else
       {
         sfso = new SFSObject();
         sfso.putUtfString("status", "nodatafound");
       }
       
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  getUserLastSessionInfo  >> :" + e.toString());
     }
     
 
     return sfso;
   }
   
 
 
 
   public void getGifts()
   {
     String sql = "select * from gift_master";
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       ResultSet res = stmt.executeQuery();
       
       while (res.next())
       {
         ISFSObject sfso = new SFSObject();
         sfso.putInt("id", res.getInt("id"));
         sfso.putInt("giftid", res.getInt("giftid"));
         sfso.putUtfString("giftName", res.getString("gift_name"));
         sfso.putDouble("giftCost", res.getDouble("gift_cost"));
         sfso.putUtfString("imgaeUrl", res.getString("imgae_url"));
         sfso.putUtfString("status", res.getString("status"));
         Commands.appInstance.gifts.addSFSObject(sfso);
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  getGifts  >> :" + e.toString());
     }
   }
   
   public SFSArray getUserGifts(String player)
   {
     SFSArray sfsArray = new SFSArray();
     String sql = "select * from gift_data where userid=? order by update_date desc";
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       ResultSet res = stmt.executeQuery();
       
       while (res.next())
       {
         ISFSObject sfso = new SFSObject();
         
         sfso.putInt("id", res.getInt("id"));
         sfso.putInt("giftid", res.getInt("giftid"));
         sfso.putDouble("unitprice", res.getDouble("unitprice"));
         sfso.putInt("qty", res.getInt("qty"));
         sfso.putUtfString("update_date", res.getString("update_date"));
         
         sfsArray.addSFSObject(sfso);
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  getUserGifts  >> :" + e.toString());
     }
     
     return sfsArray;
   }
   
 
   public void updateGiftItem(String player, int giftId, int qnt, String receiver, double unitPrice, String type, String gameid)
   {
     double cost = qnt * unitPrice;
     int quantity = 0;
     
     String sql = "select * from gift_data where userid=? and giftid=?";
     Appmethods.showSQLLog(" SQLProxy >>  sql >> :" + sql);
     boolean bool = false;
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       stmt.setInt(2, giftId);
       ResultSet res = stmt.executeQuery();
       
       while (res.next())
       {
         bool = true;
         quantity = res.getInt("qty");
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  updateGiftItem  >> :" + e.toString());
     }
     
     if (bool)
     {
 
       String sqlUpdate = "update gift_data set qty = ?, cost = ?, update_date=?, gameid=?  where userid=? and giftid=?;";
       Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
       if (type.equals("Buy"))
       {
 
         quantity += qnt;
         cost = quantity * unitPrice;
 
       }
       else
       {
         quantity -= qnt;
         cost = quantity * unitPrice;
       }
       
       try
       {
         PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
         stmtWin.setInt(1, quantity);
         stmtWin.setDouble(2, cost);
         stmtWin.setString(3, Appmethods.getDateTime());
         stmtWin.setString(4, gameid);
         stmtWin.setString(5, player);
         stmtWin.setInt(6, giftId);
         stmtWin.executeUpdate();
         stmtWin.close();
       }
       catch (SQLException e)
       {
         Appmethods.showSQLLog(" SQLProxyError >> updateGiftItem  gift_data>> :" + e.toString());
       }
       
     }
     else
     {
       String sqlInsert = "Insert INTO gift_data(userid,giftid,unitprice,qty,cost,gameid,update_date) values (?,?,?,?,?,?,?);";
       Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
       try
       {
         PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
         pstmt.setString(1, player);
         pstmt.setInt(2, giftId);
         pstmt.setDouble(3, unitPrice);
         pstmt.setInt(4, qnt);
         pstmt.setDouble(5, cost);
         pstmt.setString(6, gameid);
         pstmt.setString(7, Appmethods.getDateTime());
         
         pstmt.executeUpdate();
         pstmt.close();
       }
       catch (SQLException e)
       {
         System.out.println("SQLProxyError >>  updateGiftItem  gift_data:" + e.toString());
       }
     }
     
 
     sql = "Select * from gift_history ORDER BY id DESC LIMIT 1";
     Appmethods.showSQLLog("Sql " + sql);
     String t_id = getId(sql);
     
     String sqlInsert = "Insert INTO gift_history(transaction_id, userid, gift_id, unitprice, gameid, receiver, datetime) values (?,?,?,?,?,?,?);";
     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert gift_history>> :" + sqlInsert);
     
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       
       pstmt.setString(1, "T" + t_id);
       pstmt.setString(2, player);
       pstmt.setInt(3, giftId);
       pstmt.setDouble(4, unitPrice);
       pstmt.setString(5, gameid);
       pstmt.setString(6, receiver);
       pstmt.setString(7, Appmethods.getDateTime());
       
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  insertChatHistory  chat_history:" + e.toString());
     }
   }
   
 
 
 
   public void insertChatHistory(String sender, String msg, String msgType, String receiver)
   {
     String sqlInsert = "Insert INTO chat_history(sender,message,msg_type,receiver,datetime) values (?,?,?,?,?);";
     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert insertChatHistory>> :" + sqlInsert);
     
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setString(1, sender);
       pstmt.setString(2, msg);
       pstmt.setString(3, msgType);
       pstmt.setString(4, receiver);
       pstmt.setString(5, Appmethods.getDateTime());
       
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  insertChatHistory  chat_history:" + e.toString());
     }
   }
   
 
 
   public void insertLoginSession(String player, ISession session)
   {
     updateUserLogin(1, player);
     
     String sqlInsert = "Insert INTO login_data_player(userid,id_type,subid_type,logintime,ip) values (?,?,?,?,?);";
     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert insertLoginSession>> :" + sqlInsert);
     
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setString(1, player);
       pstmt.setString(2, "player");
       pstmt.setString(3, "player");
       pstmt.setString(4, Appmethods.getDateTime());
       pstmt.setString(5, session.getAddress());
                 Appmethods.showSQLLog(" IP address :" + session.getAddress());
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  insertLoginSession  login_data_player:" + e.toString());
     }
   }

   public void insertRoomcheckerStartTime()
   {

     String sqlInsert = "insert INTO room_checker(checkrooms, start_time)  values (?,?)";
     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert insertRoomcheckerStart>> :" + sqlInsert);

     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setString(1, "run");
       pstmt.setObject(2, new Date());
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >> insertRoomcheckerStartTime :" + e.toString());
     }
   }

   public void insertRoomcheckerEndTime()
   {

     String sqlInsert = "update room_checker set end_time = ? order by id desc limit 1";
     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert insertRoomcheckerStart>> :" + sqlInsert);

     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
       pstmt.setObject(1, new Date());
       pstmt.executeUpdate();
       pstmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >> insertRoomcheckerEndTime :" + e.toString());
     }
   }



   public void updateLogoutSession(String player)
   {
     updateUserLogin(0, player);
     
     String sql = "select id,logintime  from login_data_player where userid=? order by id desc limit 1;";
     Appmethods.showSQLLog(" SQLProxy >>  sql >> :" + sql);
     int id = 0;
     String createdDate = "";
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       stmt.setString(1, player);
       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         id = res.getInt("id");
         createdDate = res.getString("logintime");
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  updateLogoutSession >> :" + e.toString());
     }
     
 
     Date d1 = null;
     Date d2 = null;
     String currDate = null;
     Calendar currentTime = Calendar.getInstance();
     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
     currDate = dateFormat.format(currentTime.getTime());
     
     if (createdDate == "")
       createdDate = currDate;
     try {
       d2 = dateFormat.parse(currDate); } catch (ParseException e) { Appmethods.showSQLLog("Time sheduler >> ParseException"); }
     try { d1 = dateFormat.parse(createdDate); } catch (Exception e) { e.printStackTrace();
     }
     
     long totalsec = d2.getTime() - d1.getTime();
     int seconds = (int)totalsec / 1000;
     
 
 
 
     String sqlUpdate = "update login_data_player set logouttime = ?, total_seconds =? where id=?;";
     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
     try
     {
       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
       stmtWin.setString(1, Appmethods.getDateTime());
       stmtWin.setInt(2, seconds);
       stmtWin.setInt(3, id);
       stmtWin.executeUpdate();
       stmtWin.close();
     }
     catch (SQLException e)
     {
       Appmethods.showSQLLog(" SQLProxyError >> updateLogoutSession  >> :" + e.toString());
     }
   }
   
   public void updateUserLogin(int value, String player)
   {
     String sqlUpdate = "update user_master set islogin = ? where userid=?;";
     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
     try
     {
       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
       stmtWin.setInt(1, value);
       stmtWin.setString(2, player);
       stmtWin.executeUpdate();
       stmtWin.close();
     }
     catch (SQLException e)
     {
       Appmethods.showSQLLog(" SQLProxyError >> updateLogoutSession  >> :" + e.toString());
     }
   }
   
   public ISFSObject checkDeviceId(String player, String deviceId)
   {
     ISFSObject sfso = new SFSObject();
     String device_id = "";
     String version_id = "";
     
     String sqlSelect = "select device_id,version_id from user_master where userid=? and id_type='player';";
     Appmethods.showSQLLog(" checkDeviceId >> : " + sqlSelect);
     
     try
     {
       PreparedStatement pstmt = this.con.prepareStatement(sqlSelect);
       pstmt.setString(1, player);
       
       ResultSet rs = pstmt.executeQuery();
       while (rs.next()) {
         device_id = rs.getString("device_id");
         version_id = rs.getString("version_id");
       }
       rs.close();
       pstmt.close();
     }
     catch (Exception e) {
       Appmethods.showSQLLog(" getUserPassword >> : " + e.toString());
     }
     
     if (!device_id.equals(""))
     {
       if (device_id.equals("unlock"))
       {
 
         String sqlUpdate = "update user_master set user_master.isDeviceCaputre=?, user_master.device_id=?  where user_master.userid=?;";
         Appmethods.showSQLLog(" SQLProxy >>  checkDeviceId >> :" + sqlUpdate);
         
         try
         {
           PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
           stmtWin.setInt(1, 1);
           stmtWin.setString(2, deviceId);
           stmtWin.setString(3, player);
           stmtWin.executeUpdate();
           stmtWin.close();
         }
         catch (SQLException e)
         {
           Appmethods.showSQLLog(" SQLProxyError >> checkDeviceId Update Profile  >> :" + e.toString());
         }
         sfso.putBool("deviceIdStatus", true);
 
 
 
 
       }
       else if (device_id.equals(deviceId))
       {
 
         sfso.putBool("deviceIdStatus", true);
 
       }
       else
       {
         sfso.putBool("deviceIdStatus", false);
       }
       
     }
     else {
       sfso.putBool("deviceIdStatus", false);
     }
     sfso.putUtfString("versionId", version_id);
     return sfso;
   }
   
 
 
   public void serverRestartedCloseGames()
   {
     String sql = "select users_playing.username,users_playing.game_id from users_playing INNER JOIN game_status on users_playing.game_id = game_status.gameid and game_status.status=0";
     
     Appmethods.showSQLLog(" serverRestartedCloseGames >> Sql >> :" + sql);
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);
       ResultSet res = stmt.executeQuery();
       
       while (res.next())
       {
         String name = new String(res.getString("username"));
         String gameid = res.getString("game_id");
         
 
         String sqldelete = "delete from users_playing where username = ? and game_id = ?";
         Appmethods.showSQLLog(" serverRestartedCloseGames >> sqldelete >> :" + sqldelete);
         PreparedStatement stmt1 = this.con.prepareStatement(sqldelete);
         
         stmt1.setString(1, name);
         stmt1.setString(2, gameid);
         
         stmt1.executeUpdate();
         stmt1.close();
         
 
         String sqlGameStatus = "update game_status set status=2 where gameid=?;";
         Appmethods.showSQLLog(" serverRestartedCloseGames >> sqlGameStatus >>:" + sqlGameStatus);
         
         PreparedStatement sqlstatus = this.con.prepareStatement(sqlGameStatus);
         sqlstatus.setString(1, gameid);
         sqlstatus.executeUpdate();
         sqlstatus.close();
         
 
         String sqlUpdate = "update game_history set status=2 where game_id = ?";
         Appmethods.showSQLLog(" serverRestartedCloseGames >> sqlUpdate  >> :" + sqlUpdate);
         PreparedStatement stmt2 = this.con.prepareStatement(sqlUpdate);
         
         stmt2.setString(1, gameid);
         
         stmt2.executeUpdate();
         stmt2.close();
       }
       
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       Appmethods.showSQLLog("SQLProxyError >>  serverRestartedCloseGames >> :" + e.toString());
     }
   }

    public LinkedList<String> getNpcNames()
   {
     String sql = "select npc_users.npc_name from npc_users";
     LinkedList<String> games = new LinkedList<>();
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);

       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         games.add(res.getString("npc_name").toUpperCase());
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  getCurrentRunningGames  >> :" + e.toString());
     }
     return games;
   }

    public ArrayList<Integer> getNpcforRoom()
   {
     String sql = "select npc_for_room.room from npc_for_room";
     ArrayList<Integer> result = new ArrayList<>();
     try
     {
       PreparedStatement stmt = this.con.prepareStatement(sql);

       ResultSet res = stmt.executeQuery();
       while (res.next())
       {
         result.add(res.getInt("room"));
       }
       res.close();
       stmt.close();
     }
     catch (SQLException e)
     {
       System.out.println("SQLProxyError >>  getCurrentRunningGames  >> :" + e.toString());
     }
     return result;
   }

     public HashMap<String, Integer> getNpcSettings()
     {
         String sql = "select npc_settings.* from npc_settings where id = 1";
         HashMap<String, Integer> result = new HashMap<>();
         try
         {
             PreparedStatement stmt = this.con.prepareStatement(sql);

             ResultSet res = stmt.executeQuery();
             while (res.next()) {
                 result.put("delay", res.getInt("roomchecker_delay"));
                 result.put("period", res.getInt("roomchecker_period"));
                 result.put("minHands", res.getInt("min_npc_hands"));
                 result.put("maxHands", res.getInt("max_npc_hands"));
             }
             res.close();
             stmt.close();
         }
         catch (SQLException e)
         {
             System.out.println("SQLProxyError >>  getCurrentRunningGames  >> :" + e.toString());
         }
         return result;
     }

 }



