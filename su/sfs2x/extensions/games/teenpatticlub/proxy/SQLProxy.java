/*      */ package su.sfs2x.extensions.games.teenpatticlub.proxy;
/*      */ 
/*      */ import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.db.IDBManager;
/*      */ import com.smartfoxserver.v2.entities.Zone;
/*      */ import com.smartfoxserver.v2.entities.data.ISFSArray;
/*      */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*      */ import com.smartfoxserver.v2.entities.data.SFSArray;
/*      */ import com.smartfoxserver.v2.entities.data.SFSObject;
/*      */ import java.io.PrintStream;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.*;
/*      */
/*      */
/*      */
/*      */
/*      */ import java.util.concurrent.ConcurrentHashMap;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.GameRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.PlayerRoundBean;
import su.sfs2x.extensions.games.teenpatticlub.bean.TableBean;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SQLProxy
/*      */ {
/*   46 */   Connection con = null;
/*      */   
/*      */   public IDBManager dbManager;
/*      */   
/*      */   public SQLProxy(Zone zone)
/*      */   {
/*   52 */     this.dbManager = zone.getDBManager();
/*      */     try
/*      */     {
/*   55 */       this.con = this.dbManager.getConnection();
/*   56 */       Appmethods.showSQLLog("SQL Connection Success ");
/*      */     }
/*      */     catch (Exception e) {
/*   59 */       Appmethods.showSQLLog("SQL Connection Failed :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public ISFSObject getUserDetails(String nickname)
/*      */   {
/*   66 */     String sqlSelect = "select * from user_master where userid=? and id_type='player';";
/*   67 */     Appmethods.showSQLLog(" getUserPassword >> : " + sqlSelect);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*   72 */     ISFSObject sfso = new SFSObject();
/*      */     
/*      */     try
/*      */     {
/*   76 */       PreparedStatement pstmt = this.con.prepareStatement(sqlSelect);
/*   77 */       pstmt.setString(1, nickname);
/*      */       
/*   79 */       ResultSet rs = pstmt.executeQuery();
/*   80 */       while (rs.next())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*   85 */         sfso.putUtfString("username", rs.getString("userid"));
/*   86 */         sfso.putUtfString("password", rs.getString("password"));
/*   87 */         sfso.putUtfString("status", rs.getString("status"));
/*      */       }
/*   89 */       rs.close();
/*   90 */       pstmt.close();
/*      */     }
/*      */     catch (Exception e) {
/*   93 */       Appmethods.showSQLLog(" getUserPassword >> : " + e.toString());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  101 */     return sfso;
/*      */   }
/*      */   
/*      */   public void getPublicTables() {
/*  105 */     String sql = "Select * from public_tables;";
/*  106 */     Appmethods.showSQLLog("Sql " + sql);
/*      */     try {
/*  108 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  109 */       ResultSet res = stmt.executeQuery();
/*      */       
/*  111 */       if (res.next()) {
/*      */         do {
/*  113 */           TableBean tBean = new TableBean();
/*  114 */           tBean.set_id(Integer.valueOf(res.getInt("id")));
/*  115 */           tBean.set_boot(Integer.valueOf(res.getInt("boot")));
/*  116 */           tBean.set_challLimit(Integer.valueOf(res.getInt("chall_limit")));
/*  117 */           tBean.set_potLimit(Integer.valueOf(res.getInt("pot_limit")));
/*  118 */           Commands.appInstance.publicTables.add(tBean);
/*  119 */         } while (res.next());
/*      */       }
/*  121 */       res.close();
/*  122 */       stmt.close();
/*      */     }
/*      */     catch (SQLException exception)
/*      */     {
/*  126 */       Appmethods.showSQLLog("getPublicTables SQL Connection Failed :" + exception.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   public ISFSObject getPlayerProfile(String player) {
/*  131 */     String sql = "select * from user_master where userid =?;";
/*      */     
/*  133 */     ISFSObject sfso = new SFSObject();
/*      */     try
/*      */     {
/*  136 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  137 */       stmt.setString(1, player);
/*  138 */       ResultSet res = stmt.executeQuery();
/*  139 */       while (res.next())
/*      */       {
/*  141 */         sfso.putUtfString("userId", res.getString("userid"));
/*  142 */         Appmethods.showSQLLog("getPlayerProfile >> Chips" + res.getFloat("chips"));
/*  143 */         sfso.putFloat("chips", Appmethods.setFloatFormat(res.getFloat("chips")));
/*  144 */         Appmethods.showSQLLog("getPlayerProfile >> Chips" + sfso.getFloat("chips"));
/*  145 */         sfso.putUtfString("idType", res.getString("id_type"));
/*  146 */         sfso.putUtfString("avatar", res.getString("avatar"));
/*  147 */         sfso.putUtfString("fullName", res.getString("full_name"));
/*  148 */         sfso.putUtfString("mobile", res.getString("mobile"));
/*  149 */         sfso.putUtfString("email", res.getString("email"));
/*  150 */         sfso.putUtfString("nationality", res.getString("nationality"));
/*  151 */         sfso.putUtfString("gender", res.getString("gender"));
/*      */       }
/*  153 */       res.close();
/*  154 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  158 */       System.out.println(" SQLProxyError >>  loginDate  >> :" + e.toString());
/*      */     }
/*      */     
/*  161 */     return sfso;
/*      */   }
/*      */   
/*      */   public String getPlayerAvatar(String player)
/*      */   {
/*  166 */     String sql = "select avatar from user_master where userid =?;";
/*  167 */     String avatar = "";
/*      */     try
/*      */     {
/*  170 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  171 */       stmt.setString(1, player);
/*  172 */       ResultSet res = stmt.executeQuery();
/*  173 */       while (res.next())
/*      */       {
/*  175 */         avatar = res.getString("avatar");
/*      */       }
/*  177 */       res.close();
/*  178 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  182 */       System.out.println(" SQLProxyError >>  loginDate  >> :" + e.toString());
/*      */     }
/*  184 */     return avatar;
/*      */   }
/*      */   
/*      */   public float getPlayerCommission(String player)
/*      */   {
/*  189 */     String sql = "select commission from user_master where userid =?;";
/*  190 */     float commission = 0.0F;
/*      */     try
/*      */     {
/*  193 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  194 */       stmt.setString(1, player);
/*  195 */       ResultSet res = stmt.executeQuery();
/*  196 */       while (res.next())
/*      */       {
/*  198 */         commission = res.getFloat("commission");
/*      */       }
/*  200 */       res.close();
/*  201 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  205 */       System.out.println(" SQLProxyError >>  loginDate  >> :" + e.toString());
/*      */     }
/*  207 */     return commission;
/*      */   }
/*      */   //TODO getNpcAvatar
/*      */ 
/*      */   public float getPlayerInpaly(String player)
/*      */   {
/*  213 */     String sql = "select chips from user_master where userid =?;";
/*  214 */     float chips = 0.0F;
/*      */     try
/*      */     {
/*  217 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  218 */       stmt.setString(1, player);
/*  219 */       ResultSet res = stmt.executeQuery();
/*  220 */       while (res.next())
/*      */       {
/*  222 */         chips = res.getFloat("chips");
/*      */       }
/*  224 */       res.close();
/*  225 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  229 */       System.out.println(" SQLProxyError >>  loginDate  >> :" + e.toString());
/*      */     }
/*  231 */     return chips;
/*      */   }
/*      */   //TODO getNpcInplay
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPrivateTableAvail(String player)
/*      */   {
/*  241 */     String sql = "select * from private_tables where status=0 and owner=?;";
/*      */     try
/*      */     {
/*  244 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  245 */       stmt.setString(1, player);
/*  246 */       ResultSet res = stmt.executeQuery();
/*  247 */       if (res.next())
/*      */       {
/*  249 */         return true;
/*      */       }
/*  251 */       res.close();
/*  252 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  256 */       System.out.println(" SQLProxyError >>  isPrivateTableAvail  >> :" + e.toString());
/*      */     }
/*      */     
/*  259 */     return false;
/*      */   }
/*      */   
/*      */   public void getPrivateTables() {
/*  263 */     String sql = "Select * from private_tables where status=0;";
/*  264 */     Appmethods.showSQLLog("Sql " + sql);
/*      */     try {
/*  266 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  267 */       ResultSet res = stmt.executeQuery();
/*      */       
/*  269 */       if (res.next()) {
/*      */         do {
/*  271 */           TableBean tBean = new TableBean();
/*  272 */           tBean.set_id(Integer.valueOf(res.getInt("id")));
/*  273 */           tBean.set_boot(Integer.valueOf(res.getInt("boot")));
/*  274 */           tBean.set_challLimit(Integer.valueOf(res.getInt("chall_limit")));
/*  275 */           tBean.set_potLimit(Integer.valueOf(res.getInt("pot_limit")));
/*  276 */           tBean.set_roomId(res.getString("owner"));
/*  277 */           tBean.set_password(res.getString("password"));
/*  278 */           tBean.set_type("Private");
/*  279 */           tBean.set_startDate(res.getString("created_datetime"));
/*  280 */           String createdDate = res.getString("created_datetime");
/*      */           
/*      */ 
/*  283 */           Date d1 = null;
/*  284 */           Date d2 = null;
/*  285 */           String currDate = null;
/*  286 */           Calendar currentTime = Calendar.getInstance();
/*  287 */           DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*  288 */           dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
/*  289 */           currDate = dateFormat.format(currentTime.getTime());
/*  290 */           try { d2 = dateFormat.parse(currDate); } catch (ParseException e) { Appmethods.showSQLLog("Time sheduler >> ParseException"); }
/*  291 */           try { d1 = dateFormat.parse(createdDate); } catch (Exception e) { e.printStackTrace();
/*      */           }
/*      */           
/*  294 */           long totalsec = d2.getTime() - d1.getTime();
/*  295 */           long seconds = totalsec / 1000L;
/*  296 */           int sec = (int)(604800L - seconds);
/*      */           
/*  298 */           if (sec > 0)
/*      */           {
/*      */ 
/*  301 */             tBean.startTimer(sec);
/*  302 */             Commands.appInstance.privateTables.add(tBean);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  307 */             updatePrivateTable(res.getInt("id"));
/*      */           }
/*  309 */         } while (res.next());
/*      */       }
/*  311 */       res.close();
/*  312 */       stmt.close();
/*      */     }
/*      */     catch (SQLException exception)
/*      */     {
/*  316 */       Appmethods.showSQLLog("getPrivateTables SQL Connection Failed :" + exception.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void createPrivateTable(TableBean tBean)
/*      */   {
/*  325 */     String sqlInsert = "insert into private_tables(boot,chall_limit,pot_limit,owner,password,created_datetime,status) values(?,?,?,?,?,?,?)";
/*  326 */     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
/*      */     
/*  328 */     String sql = "select id from private_tables where owner =? and password=? and status=?;";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  339 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/*  340 */       pstmt.setInt(1, tBean.get_boot().intValue());
/*  341 */       pstmt.setInt(2, tBean.get_challLimit().intValue());
/*  342 */       pstmt.setInt(3, tBean.get_potLimit().intValue());
/*  343 */       pstmt.setString(4, tBean.get_roomId());
/*  344 */       pstmt.setString(5, tBean.get_password());
/*  345 */       pstmt.setString(6, tBean.get_startDate());
/*  346 */       pstmt.setInt(7, 0);
/*      */       
/*  348 */       pstmt.executeUpdate();
/*  349 */       pstmt.close();
/*      */       
/*      */ 
/*  352 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  353 */       stmt.setString(1, tBean.get_roomId());
/*  354 */       stmt.setString(2, tBean.get_password());
/*  355 */       stmt.setInt(3, 0);
/*      */       
/*  357 */       int id = 0;
/*  358 */       ResultSet res = stmt.executeQuery();
/*  359 */       while (res.next())
/*      */       {
/*  361 */         id = res.getInt("id");
/*      */       }
/*  363 */       tBean.setPrivateTableId(Integer.valueOf(id));
/*  364 */       res.close();
/*  365 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e) {
/*  368 */       Appmethods.showSQLLog(" SQLProxyError >> createPrivateTable  >> :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   private String getId(String sql) {
/*  373 */     String t_id = Appmethods.getRandomNumberString();
/*  374 */     int id = 0;
/*      */     try {
/*  376 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  377 */       ResultSet res = stmt.executeQuery();
/*  378 */       while (res.next())
/*      */       {
/*  380 */         id = res.getInt("id");
/*      */       }
/*  382 */       res.close();
/*  383 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e) {
/*  386 */       System.out.println("SQLProxyError >> getId >> :" + e.toString());
/*      */     }
/*      */     
/*  389 */     t_id = t_id + (id + 1);
/*      */     
/*  391 */     return t_id;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void insertPrivateTableHistory(GameBean gameBean, String player, String type, int privateTableId)
/*      */   {
/*  398 */     String sql = "Select * from private_tbl_history where private_table_Id=? and userid=?;";
/*  399 */     Appmethods.showSQLLog("Sql " + sql);
/*  400 */     boolean bool = false;
/*      */     
/*      */     try
/*      */     {
/*  404 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  405 */       stmt.setInt(1, privateTableId);
/*  406 */       stmt.setString(2, player);
/*      */       
/*  408 */       ResultSet res = stmt.executeQuery();
/*  409 */       while (res.next())
/*      */       {
/*  411 */         bool = true;
/*      */       }
/*  413 */       res.close();
/*  414 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e) {
/*  417 */       System.out.println("SQLProxyError >> insertPrivateTableHistory >> :" + e.toString());
/*      */     }
/*      */     
/*  420 */     if (!bool)
/*      */     {
/*      */ 
/*      */ 
/*  424 */       String sqlUpdate = "update user_master set user_master.chips = (user_master.chips - ?)  where user_master.userid = ?;";
/*  425 */       Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
/*      */       
/*      */ 
/*      */ 
/*  429 */       sql = "Select * from private_tbl_history ORDER BY id DESC LIMIT 1";
/*  430 */       Appmethods.showSQLLog("Sql " + sql);
/*  431 */       String t_id = getId(sql);
/*      */       
/*  433 */       String sqlInsert = "Insert INTO private_tbl_history(transaction_id,private_table_Id,gameid,roomid,userid,deducted_amount,tran_type,date_time) VALUES (?,?,?,?,?,?,?,?);";
/*      */       
/*      */ 
/*  436 */       Appmethods.showSQLLog(" SQLProxy >> startGameStatus >> sqlInsert >> :" + sqlInsert);
/*      */       
/*      */       try
/*      */       {
/*  440 */         PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/*  441 */         stmtWin.setFloat(1, 30.0F);
/*  442 */         stmtWin.setString(2, player);
/*      */         
/*  444 */         stmtWin.executeUpdate();
/*  445 */         stmtWin.close();
/*      */         
/*      */ 
/*  448 */         PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/*  449 */         pstmt.setString(1, "T" + t_id);
/*  450 */         pstmt.setInt(2, privateTableId);
/*  451 */         pstmt.setString(3, gameBean.getGameID());
/*  452 */         pstmt.setString(4, gameBean.getRoomId());
/*  453 */         pstmt.setString(5, player);
/*  454 */         pstmt.setFloat(6, 30.0F);
/*  455 */         pstmt.setString(7, type);
/*  456 */         pstmt.setString(8, Appmethods.getDateTime());
/*      */         
/*  458 */         pstmt.executeUpdate();
/*  459 */         pstmt.close();
/*      */       }
/*      */       catch (SQLException e) {
/*  462 */         System.out.println("SQLProxyError >> startGameStatus game_history>> :" + e.toString());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void updatePrivateTable(int id)
/*      */   {
/*  470 */     String sqlUpdate = "update private_tables set status=1 where id=?;";
/*  471 */     Appmethods.showSQLLog(" SQLProxy >> updatePrivateTable >> sqlUpdate >> :" + sqlUpdate);
/*      */     try
/*      */     {
/*  474 */       PreparedStatement pstmt = this.con.prepareStatement(sqlUpdate);
/*      */       
/*  476 */       pstmt.setInt(1, id);
/*  477 */       pstmt.executeUpdate();
/*  478 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e) {
/*  481 */       Appmethods.showSQLLog(" SQLProxy >> updatePrivateTable >> :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void startGameStatus(GameBean gameBean)
/*      */   {
/*  489 */     String sqlInsert = "Insert INTO game_history(game_id,room_id,players_count,game_type,player_names,player_cards,start_time,updated_by,status) VALUES (?,?,?,?,?,?,?,?,?);";
/*      */     
/*      */ 
/*  492 */     Appmethods.showSQLLog(" SQLProxy >> startGameStatus >> sqlInsert >> :" + sqlInsert);
/*      */     
/*      */     try
/*      */     {
/*  496 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/*  497 */       pstmt.setString(1, gameBean.getGameID());
/*  498 */       pstmt.setString(2, gameBean.getRoomId());
/*  499 */       pstmt.setInt(3, gameBean.getGameRoundBean().getActivePlayersCount());
/*  500 */       pstmt.setString(4, gameBean.getGameType());
/*  501 */       pstmt.setString(5, gameBean.getGameRoundBean().getPlayers().toString());
/*  502 */       pstmt.setString(6, gameBean.getGameRoundBean().getPlayerCardsString());
/*  503 */       pstmt.setString(7, gameBean.getGameStartDate());
/*  504 */       pstmt.setString(8, "Admin");
/*  505 */       pstmt.setInt(9, 0);
/*  506 */       pstmt.executeUpdate();
/*  507 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e) {
/*  510 */       System.out.println("SQLProxyError >> startGameStatus game_history>> :" + e.toString());
/*      */     }
/*      */     
/*      */ 
/*  514 */     sqlInsert = "Insert INTO game_status(gameid,status,entry_date,updated_by) VALUES (?,?,?,?);";
/*      */     try
/*      */     {
/*  517 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/*  518 */       pstmt.setString(1, gameBean.getGameID());
/*  519 */       pstmt.setInt(2, 0);
/*  520 */       pstmt.setString(3, gameBean.getGameStartDate());
/*  521 */       pstmt.setString(4, "Admin");
/*  522 */       pstmt.executeUpdate();
/*  523 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e) {
/*  526 */       System.out.println("SQLProxyError >> startGameStatus >> game_status>> :" + e.toString());
/*      */     }
/*      */     
/*      */ 
/*  530 */     for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*      */     {
/*  532 */       PlayerRoundBean bean = (PlayerRoundBean)e.nextElement();
/*  533 */       insertUserPlaying(bean.getPlayerId(), gameBean.getGameID(), gameBean.getRoomId());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void closeGameStatus(GameBean gameBean)
/*      */   {
/*  541 */     String currDate = Appmethods.getDateTime();
/*      */     
/*      */ 
/*  544 */     String sqlUpdate = "update game_history set won_amount=?, won_player=?, end_time=?, status=? where game_id=?;";
/*      */     try
/*      */     {
/*  547 */       PreparedStatement pstmt = this.con.prepareStatement(sqlUpdate);
/*  548 */       pstmt.setFloat(1, gameBean.getGameRoundBean().getWonAmount());
/*  549 */       pstmt.setString(2, gameBean.getGameRoundBean().getWonPlayer());
/*  550 */       pstmt.setString(3, currDate);
/*  551 */       pstmt.setInt(4, 1);
/*  552 */       pstmt.setString(5, gameBean.getGameID());
/*      */       
/*  554 */       pstmt.executeUpdate();
/*  555 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e) {
/*  558 */       System.out.println("SQLProxyError >> closeGameStatus game_status>> :" + e.toString());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  563 */     String sql = "update game_status set status=? where gameid= ?;";
/*      */     try
/*      */     {
/*  566 */       PreparedStatement pstmt = this.con.prepareStatement(sql);
/*  567 */       pstmt.setInt(1, 1);
/*  568 */       pstmt.setString(2, gameBean.getGameID());
/*  569 */       pstmt.executeUpdate();
/*  570 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  574 */       System.out.println("SQLProxyError  >>  closeGameStatus >> :" + e.toString());
/*      */     }
/*      */     
/*      */ 
/*  578 */     for (Enumeration<PlayerRoundBean> e = gameBean.getGameRoundBean().getPlayerRoundBeans().elements(); e.hasMoreElements();)
/*      */     {
/*  580 */       PlayerRoundBean bean = (PlayerRoundBean)e.nextElement();
/*  581 */       deleteUserPlaying(bean.getPlayerId(), gameBean.getGameID());
/*      */       
/*  583 */       if ((!bean.isLeaveTable()) && (!bean.isSitOut()))
/*      */       {
/*  585 */         PlayerBean pbean = (PlayerBean)gameBean.getPlayerBeenList().get(bean.getPlayerId());
/*  586 */         updateUserChips(bean.getPlayerId(), Float.valueOf(pbean.getInplay()));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  591 */     sqlUpdate = "update commission_master set status=? where gameid=?;";
/*      */     try
/*      */     {
/*  594 */       PreparedStatement pstmt = this.con.prepareStatement(sqlUpdate);
/*  595 */       pstmt.setInt(1, 1);
/*  596 */       pstmt.setString(2, gameBean.getGameID());
/*      */       
/*  598 */       pstmt.executeUpdate();
/*  599 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e) {
/*  602 */       System.out.println("SQLProxyError >> closeGameStatus commission_master>> :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   private void insertUserPlaying(String player, String gameid, String roomId)
/*      */   {
/*  608 */     String sqlInsert = "Insert INTO users_playing VALUES (?,?,?);";
/*      */     try
/*      */     {
/*  611 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/*  612 */       pstmt.setString(1, player);
/*  613 */       pstmt.setString(2, gameid);
/*  614 */       pstmt.setString(3, roomId);
/*  615 */       pstmt.executeUpdate();
/*  616 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  620 */       System.out.println("SQLProxyError >>  insertUserPlaying  :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   public void deleteUserPlaying(String player, String gameid) {
/*  625 */     String sqlDelete = "delete from users_playing where username= ? and game_id= ?;";
/*      */     try
/*      */     {
/*  628 */       PreparedStatement pstmt = this.con.prepareStatement(sqlDelete);
/*  629 */       pstmt.setString(1, player);
/*  630 */       pstmt.setString(2, gameid);
/*  631 */       pstmt.executeUpdate();
/*  632 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  636 */       System.out.println("SQLProxyError >> deleteUserPlaying >> :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void insertUserAction(String roomid, String player, int amount, String move_type, float playerRake)
/*      */   {
/*  643 */     String sql = "select distributor_id  from user_master where userid=?;";
/*  644 */     Appmethods.showSQLLog(" SQLProxy >>  sql >> :" + sql);
/*      */     
/*  646 */     String dis_id = "";
/*      */     try
/*      */     {
/*  649 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  650 */       stmt.setString(1, player);
/*  651 */       ResultSet res = stmt.executeQuery();
/*  652 */       while (res.next())
/*      */       {
/*  654 */         dis_id = res.getString("distributor_id");
/*      */       }
/*  656 */       res.close();
/*  657 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  661 */       System.out.println("SQLProxyError >>  insertUserAction user_master>> :" + e.toString());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  666 */     GameBean gameBean = Appmethods.getGameBean(roomid);
/*  667 */     float commission = 0.0F;
/*      */     
/*  669 */     Appmethods.showSQLLog("insertUserAction>> Type " + move_type);
/*      */     
/*      */ 
/*  672 */     if (move_type.equals("Pack"))
/*      */     {
/*  674 */       commission = 0.0F;
/*      */     }
/*      */     else
/*      */     {
/*  678 */       commission = amount * playerRake / 100.0F;
/*  679 */       commission = Appmethods.setFloatFormat(commission);
/*      */     }
/*      */     
/*  682 */     sql = "Select * from commission_master ORDER BY id DESC LIMIT 1";
/*  683 */     Appmethods.showSQLLog("Sql " + sql);
/*  684 */     String t_id = getId(sql);
/*      */     
/*  686 */     String sqlInsert = "Insert INTO commission_master(gameid,transaction_id,roomid,handid,userid,distributor_id,amount,move_type,game_date,commission,type,updated_by,tempflag) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
/*      */     
/*  688 */     Appmethods.showSQLLog("Date " + gameBean.getGameStartDate());
/*  689 */     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
/*      */     try
/*      */     {
/*  692 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/*  693 */       pstmt.setString(1, gameBean.getGameID());
/*  694 */       pstmt.setString(2, "T" + t_id);
/*  695 */       pstmt.setString(3, gameBean.getRoomId());
/*  696 */       pstmt.setString(4, "handid_" + gameBean.getGameRoundBean().getHandId());
/*  697 */       pstmt.setString(5, player);
/*  698 */       pstmt.setString(6, dis_id);
/*  699 */       pstmt.setFloat(7, Appmethods.setFloatFormat(amount));
/*  700 */       pstmt.setString(8, move_type);
/*  701 */       pstmt.setString(9, gameBean.getGameStartDate());
/*  702 */       pstmt.setFloat(10, commission);
/*  703 */       pstmt.setString(11, "DR");
/*  704 */       pstmt.setString(12, "Admin");
/*  705 */       pstmt.setInt(13, 0);
/*  706 */       pstmt.executeUpdate();
/*  707 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  711 */       System.out.println("SQLProxyError >>  insertUserAction  :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateUserChips(String player, Float chips)
/*      */   {
/*  718 */     String sqlUpdate = "update user_master set user_master.chips = ?  where user_master.userid = ?;";
/*  719 */     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
/*      */     
/*      */     try
/*      */     {
/*  723 */       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/*  724 */       stmtWin.setFloat(1, Appmethods.setFloatFormat(chips.floatValue()));
/*  725 */       stmtWin.setString(2, player);
/*      */       
/*  727 */       stmtWin.executeUpdate();
/*  728 */       stmtWin.close();
/*      */ 
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  733 */       Appmethods.showSQLLog(" SQLProxyError >> updateUserChips  >> :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   public ArrayList<String> getCurrentRunningGames(String player)
/*      */   {
/*  739 */     String sql = "select users_playing.room_id from users_playing where users_playing.username=?";
/*  740 */     ArrayList<String> games = new ArrayList();
/*      */     try
/*      */     {
/*  743 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  744 */       stmt.setString(1, player);
/*  745 */       ResultSet res = stmt.executeQuery();
/*  746 */       while (res.next())
/*      */       {
/*  748 */         games.add(res.getString("room_id"));
/*      */       }
/*  750 */       res.close();
/*  751 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  755 */       System.out.println("SQLProxyError >>  getCurrentRunningGames  >> :" + e.toString());
/*      */     }
/*  757 */     return games;
/*      */   }

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
/*      */   
/*      */ 
/*      */   public void redeemChips(String player, float amount)
/*      */   {
/*  763 */     String sqlUpdate = "update user_master set user_master.chips = (user_master.chips - ?)  where user_master.userid = ?;";
/*  764 */     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
/*      */     
/*      */     try
/*      */     {
/*  768 */       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/*  769 */       stmtWin.setFloat(1, Appmethods.setFloatFormat(amount));
/*  770 */       stmtWin.setString(2, player);
/*      */       
/*  772 */       stmtWin.executeUpdate();
/*  773 */       stmtWin.close();
/*      */ 
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  778 */       Appmethods.showSQLLog(" SQLProxyError >> redeemChips  user_master1>> :" + e.toString());
/*      */     }
/*      */     
/*  781 */     String sql = "select chips,distributor_id  from user_master where userid=?;";
/*  782 */     Appmethods.showSQLLog(" SQLProxy >>  sql >> :" + sql);
/*      */     
/*  784 */     float balanceChips = 0.0F;
/*  785 */     String dis_id = "";
/*      */     try
/*      */     {
/*  788 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  789 */       stmt.setString(1, player);
/*  790 */       ResultSet res = stmt.executeQuery();
/*  791 */       while (res.next())
/*      */       {
/*  793 */         balanceChips = res.getFloat("chips");
/*  794 */         balanceChips = Appmethods.setFloatFormat(balanceChips);
/*  795 */         dis_id = res.getString("distributor_id");
/*      */       }
/*      */       
/*  798 */       res.close();
/*  799 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  803 */       System.out.println("SQLProxyError >>  redeemChips user_master2>> :" + e.toString());
/*      */     }
/*      */     
/*  806 */     sqlUpdate = "update user_master set user_master.chips = (user_master.chips + ?)  where user_master.userid=?;";
/*  807 */     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
/*      */     
/*      */     try
/*      */     {
/*  811 */       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/*  812 */       stmtWin.setFloat(1, Appmethods.setFloatFormat(amount));
/*  813 */       stmtWin.setString(2, dis_id);
/*      */       
/*  815 */       stmtWin.executeUpdate();
/*  816 */       stmtWin.close();
/*      */ 
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  821 */       Appmethods.showSQLLog(" SQLProxyError >> redeemChips  user_master3>> :" + e.toString());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  826 */     String sqlInsert = "Insert INTO transaction_history(userid,transaction_id,from_to,points,crdr,trn_type,balance_upd,update_date) values (?,?,?,?,?,?,?,?);";
/*      */     
/*  828 */     Appmethods.showSQLLog("Balance Chips " + balanceChips);
/*      */     
/*  830 */     sql = "Select * from transaction_history ORDER BY id DESC LIMIT 1";
/*  831 */     Appmethods.showSQLLog("Sql " + sql);
/*  832 */     String t_id = getId(sql);
/*      */     
/*      */     try
/*      */     {
/*  836 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/*  837 */       pstmt.setString(1, player);
/*  838 */       pstmt.setString(2, "TR"+t_id);
/*  839 */       pstmt.setString(3, dis_id);
/*  840 */       pstmt.setFloat(4, Appmethods.setFloatFormat(amount));
/*  841 */       pstmt.setString(5, "Dr");
/*  842 */       pstmt.setString(6, "Transfer");
/*  843 */       pstmt.setFloat(7, Appmethods.setFloatFormat(balanceChips));
/*  844 */       pstmt.setString(8, Appmethods.getDateTime());
/*  845 */       pstmt.executeUpdate();
/*  846 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  850 */       System.out.println("SQLProxyError >>  redeemChips  transaction_history:" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ISFSArray getTransactionHistory(String player)
/*      */   {
/*  858 */     ISFSArray sfsArray = new SFSArray();
/*      */     
/*  860 */     String sql = "select * from transaction_history where userid=? or from_to=? order by id desc";
/*      */     try
/*      */     {
/*  863 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  864 */       stmt.setString(1, player);
/*  865 */       stmt.setString(2, player);
/*  866 */       ResultSet res = stmt.executeQuery();
/*  867 */       while (res.next())
/*      */       {
/*  869 */         ISFSObject sfso = new SFSObject();
/*  870 */         sfso.putInt("id", res.getInt("id"));
/*  871 */         sfso.putUtfString("from/to", res.getString("from_to"));
/*  872 */         sfso.putFloat("points", Appmethods.setFloatFormat(res.getFloat("points")));
/*  873 */         sfso.putUtfString("crdr", res.getString("crdr"));
/*  874 */         sfso.putUtfString("transactionType", res.getString("trn_type"));
/*  875 */         float balance = res.getFloat("balance_upd");
/*      */         
/*  877 */         Appmethods.showSQLLog("Balance before " + Appmethods.setFloatFormat(balance));
/*  878 */         balance = Appmethods.setFloatFormat(balance);
/*  879 */         Appmethods.showSQLLog("Balance after " + balance);
/*  880 */         sfso.putFloat("balanceUpdate", balance);
/*  881 */         sfso.putUtfString("updateDate", res.getString("update_date"));
/*  882 */         sfsArray.addSFSObject(sfso);
/*      */       }
/*  884 */       res.close();
/*  885 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  889 */       System.out.println("SQLProxyError >>  getTransactionHistory  >> :" + e.toString());
/*      */     }
/*  891 */     return sfsArray;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateProfile(String player, String mobile, String email, String password)
/*      */   {
/*  899 */     if ((password == null) || (password.equals("")) || (password.length() == 0))
/*      */     {
/*  901 */       String sqlUpdate = "update user_master set user_master.mobile=?, user_master.email=?  where user_master.userid=?;";
/*  902 */       Appmethods.showSQLLog(" SQLProxy >>  updateProfile >> :" + sqlUpdate);
/*      */       
/*      */       try
/*      */       {
/*  906 */         PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/*  907 */         stmtWin.setString(1, mobile);
/*  908 */         stmtWin.setString(2, email);
/*  909 */         stmtWin.setString(3, player);
/*  910 */         stmtWin.executeUpdate();
/*  911 */         stmtWin.close();
/*      */       }
/*      */       catch (SQLException e)
/*      */       {
/*  915 */         Appmethods.showSQLLog(" SQLProxyError >> updateProfile  >> :" + e.toString());
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  920 */       String sqlUpdate = "update user_master set user_master.password = ?, user_master.mobile=?, user_master.email=?  where user_master.userid=?;";
/*  921 */       Appmethods.showSQLLog(" SQLProxy >>  updateProfile >> :" + sqlUpdate);
/*      */       
/*      */       try
/*      */       {
/*  925 */         PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/*  926 */         stmtWin.setString(1, password);
/*  927 */         stmtWin.setString(2, mobile);
/*  928 */         stmtWin.setString(3, email);
/*  929 */         stmtWin.setString(4, player);
/*  930 */         stmtWin.executeUpdate();
/*  931 */         stmtWin.close();
/*      */       }
/*      */       catch (SQLException e)
/*      */       {
/*  935 */         Appmethods.showSQLLog(" SQLProxyError >> updateProfile  >> :" + e.toString());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public ISFSArray getDealers()
/*      */   {
/*  942 */     ISFSArray sfsArray = new SFSArray();
/*      */     
/*  944 */     String sql = "select * from dealer_master";
/*      */     try
/*      */     {
/*  947 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  948 */       ResultSet res = stmt.executeQuery();
/*  949 */       while (res.next())
/*      */       {
/*  951 */         ISFSObject sfso = new SFSObject();
/*      */         
/*  953 */         sfso.putInt("id", res.getInt("dealerid"));
/*  954 */         sfso.putUtfString("dealerName", res.getString("dealer_name"));
/*  955 */         sfso.putDouble("cost", res.getDouble("dealer_cost"));
/*  956 */         sfso.putUtfString("imageUrl", res.getString("image_url"));
/*  957 */         sfso.putUtfString("status", res.getString("status"));
/*      */         
/*  959 */         sfsArray.addSFSObject(sfso);
/*      */       }
/*  961 */       res.close();
/*  962 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  966 */       System.out.println("SQLProxyError >>  getDealers  >> :" + e.toString());
/*      */     }
/*  968 */     return sfsArray;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void changeDealer(String player, int dealerId, float dealerCost, String gameid)
/*      */   {
/*  975 */     String sql = "Select * from dealer_data ORDER BY id DESC LIMIT 1";
/*  976 */     Appmethods.showSQLLog("Sql " + sql);
/*  977 */     String t_id = getId(sql);
/*      */     
/*  979 */     String sqlInsert = "Insert INTO dealer_data(userid,transaction_id,dealerid,cost,gameid,update_date) values (?,?,?,?,?,?);";
/*      */     
/*  981 */     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
/*      */     try
/*      */     {
/*  984 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/*      */       
/*  986 */       pstmt.setString(1, player);
/*  987 */       pstmt.setString(2, "T" + t_id);
/*  988 */       pstmt.setInt(3, dealerId);
/*  989 */       pstmt.setFloat(4, Appmethods.setFloatFormat(dealerCost));
/*  990 */       pstmt.setString(5, gameid);
/*  991 */       pstmt.setString(6, Appmethods.getDateTime());
/*      */       
/*  993 */       pstmt.executeUpdate();
/*  994 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  998 */       System.out.println("SQLProxyError >>  changeDealer >> dealer_data:" + e.toString());
/*      */     }
/*      */     
/*      */ 
/* 1002 */     cutUserChips(player, dealerCost);
/*      */   }
/*      */   
/*      */ 
/*      */   public void cutUserChips(String player, float amount)
/*      */   {
/* 1008 */     String sqlUpdate = "update user_master set user_master.chips = (user_master.chips - ?)  where user_master.userid = ?;";
/* 1009 */     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
/*      */     try
/*      */     {
/* 1012 */       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/* 1013 */       stmtWin.setFloat(1, Appmethods.setFloatFormat(amount));
/* 1014 */       stmtWin.setString(2, player);
/* 1015 */       stmtWin.executeUpdate();
/* 1016 */       stmtWin.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1020 */       Appmethods.showSQLLog(" SQLProxyError >> changeDealer  >> :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   public void insertDealerTip(String player, float amount, String gameId)
/*      */   {
/* 1026 */     String sql = "Select * from user_tip ORDER BY id DESC LIMIT 1";
/* 1027 */     Appmethods.showSQLLog("Sql " + sql);
/* 1028 */     String t_id = getId(sql);
/*      */     
/* 1030 */     String sqlInsert = "Insert INTO user_tip(userid,transaction_id,gameid,tip,created_datetime) values (?,?,?,?,?);";
/*      */     
/* 1032 */     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
/*      */     try
/*      */     {
/* 1035 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/* 1036 */       pstmt.setString(1, player);
/* 1037 */       pstmt.setString(2, "T" + t_id);
/* 1038 */       pstmt.setString(3, gameId);
/* 1039 */       pstmt.setFloat(4, Appmethods.setFloatFormat(amount));
/* 1040 */       pstmt.setString(5, Appmethods.getDateTime());
/*      */       
/* 1042 */       pstmt.executeUpdate();
/* 1043 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1047 */       System.out.println("SQLProxyError >>  insertDealerTip  user_tip:" + e.toString());
/*      */     }
/*      */     
/*      */ 
/* 1051 */     cutUserChips(player, amount);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void insertUserLastSession(PlayerBean pBean, int status)
/*      */   {
/* 1058 */     String sqlInsert = "Insert INTO user_last_session(userid,previous_balance,won_or_lost,totalHands,wonHands,status,created_datetime) values (?,?,?,?,?,?,?);";
/*      */     
/* 1060 */     float amount = 0.0F;
/* 1061 */     for (int i = 0; i < pBean.getAmounts().size(); i++)
/*      */     {
/* 1063 */       Appmethods.showLog("Amount " + pBean.getPlayerId() + " " + pBean.getAmounts().get(i));
/* 1064 */       amount += ((Float)pBean.getAmounts().get(i)).floatValue();
/*      */     }
/*      */     
/* 1067 */     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
/*      */     
/* 1069 */     Appmethods.showSQLLog(" insertUserLastSession >>  Amount >> :" + amount);
/*      */     
/*      */     try
/*      */     {
/* 1073 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/* 1074 */       pstmt.setString(1, pBean.getPlayerId());
/* 1075 */       pstmt.setFloat(2, pBean.getStartInplay());
/* 1076 */       pstmt.setFloat(3, Appmethods.setFloatFormat(amount));
/* 1077 */       pstmt.setInt(4, pBean.getTotalHands());
/* 1078 */       pstmt.setInt(5, pBean.getWonHands());
/* 1079 */       pstmt.setInt(6, status);
/* 1080 */       pstmt.setString(7, Appmethods.getDateTime());
/*      */       
/* 1082 */       pstmt.executeUpdate();
/* 1083 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1087 */       System.out.println("SQLProxyError >>  insertUserLastSession  user_last_session:" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   public void updateUserLastSession(Integer id)
/*      */   {
/* 1093 */     String sqlUpdate = "update user_last_session set status = ? where id=?;";
/* 1094 */     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
/*      */     try
/*      */     {
/* 1097 */       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/* 1098 */       stmtWin.setInt(1, 0);
/* 1099 */       stmtWin.setInt(2, id.intValue());
/* 1100 */       stmtWin.executeUpdate();
/* 1101 */       stmtWin.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1105 */       Appmethods.showSQLLog(" SQLProxyError >> updateUserLastSession  >> :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   public ISFSObject getUserLastSessionInfo(String player, int send)
/*      */   {
/* 1111 */     Integer id = Integer.valueOf(0);
/* 1112 */     ISFSObject sfso = null;
/* 1113 */     String sql = "select * from user_last_session where userid=? and status=? order by id desc limit 1";
/*      */     try
/*      */     {
/* 1116 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/* 1117 */       stmt.setString(1, player);
/* 1118 */       stmt.setInt(2, send);
/* 1119 */       ResultSet res = stmt.executeQuery();
/*      */       
/* 1121 */       while (res.next())
/*      */       {
/* 1123 */         sfso = new SFSObject();
/* 1124 */         id = Integer.valueOf(res.getInt("id"));
/* 1125 */         sfso.putInt("id", res.getInt("id"));
/* 1126 */         sfso.putUtfString("player", player);
/* 1127 */         sfso.putFloat("won_or_lost", Appmethods.setFloatFormat(res.getFloat("won_or_lost")));
/* 1128 */         sfso.putInt("totalHands", res.getInt("totalHands"));
/* 1129 */         sfso.putInt("wonHands", res.getInt("wonHands"));
/* 1130 */         sfso.putUtfString("createdDate", res.getString("created_datetime"));
/*      */       }
/*      */       
/* 1133 */       if (sfso != null)
/*      */       {
/* 1135 */         sfso.putUtfString("status", "datafound");
/* 1136 */         updateUserLastSession(id);
/*      */       }
/*      */       else
/*      */       {
/* 1140 */         sfso = new SFSObject();
/* 1141 */         sfso.putUtfString("status", "nodatafound");
/*      */       }
/*      */       
/* 1144 */       res.close();
/* 1145 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1149 */       System.out.println("SQLProxyError >>  getUserLastSessionInfo  >> :" + e.toString());
/*      */     }
/*      */     
/*      */ 
/* 1153 */     return sfso;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void getGifts()
/*      */   {
/* 1161 */     String sql = "select * from gift_master";
/*      */     try
/*      */     {
/* 1164 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/* 1165 */       ResultSet res = stmt.executeQuery();
/*      */       
/* 1167 */       while (res.next())
/*      */       {
/* 1169 */         ISFSObject sfso = new SFSObject();
/* 1170 */         sfso.putInt("id", res.getInt("id"));
/* 1171 */         sfso.putInt("giftid", res.getInt("giftid"));
/* 1172 */         sfso.putUtfString("giftName", res.getString("gift_name"));
/* 1173 */         sfso.putDouble("giftCost", res.getDouble("gift_cost"));
/* 1174 */         sfso.putUtfString("imgaeUrl", res.getString("imgae_url"));
/* 1175 */         sfso.putUtfString("status", res.getString("status"));
/* 1176 */         Commands.appInstance.gifts.addSFSObject(sfso);
/*      */       }
/* 1178 */       res.close();
/* 1179 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1183 */       System.out.println("SQLProxyError >>  getGifts  >> :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   public SFSArray getUserGifts(String player)
/*      */   {
/* 1189 */     SFSArray sfsArray = new SFSArray();
/* 1190 */     String sql = "select * from gift_data where userid=? order by update_date desc";
/*      */     try
/*      */     {
/* 1193 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/* 1194 */       stmt.setString(1, player);
/* 1195 */       ResultSet res = stmt.executeQuery();
/*      */       
/* 1197 */       while (res.next())
/*      */       {
/* 1199 */         ISFSObject sfso = new SFSObject();
/*      */         
/* 1201 */         sfso.putInt("id", res.getInt("id"));
/* 1202 */         sfso.putInt("giftid", res.getInt("giftid"));
/* 1203 */         sfso.putDouble("unitprice", res.getDouble("unitprice"));
/* 1204 */         sfso.putInt("qty", res.getInt("qty"));
/* 1205 */         sfso.putUtfString("update_date", res.getString("update_date"));
/*      */         
/* 1207 */         sfsArray.addSFSObject(sfso);
/*      */       }
/* 1209 */       res.close();
/* 1210 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1214 */       System.out.println("SQLProxyError >>  getUserGifts  >> :" + e.toString());
/*      */     }
/*      */     
/* 1217 */     return sfsArray;
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateGiftItem(String player, int giftId, int qnt, String receiver, double unitPrice, String type, String gameid)
/*      */   {
/* 1223 */     double cost = qnt * unitPrice;
/* 1224 */     int quantity = 0;
/*      */     
/* 1226 */     String sql = "select * from gift_data where userid=? and giftid=?";
/* 1227 */     Appmethods.showSQLLog(" SQLProxy >>  sql >> :" + sql);
/* 1228 */     boolean bool = false;
/*      */     try
/*      */     {
/* 1231 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/* 1232 */       stmt.setString(1, player);
/* 1233 */       stmt.setInt(2, giftId);
/* 1234 */       ResultSet res = stmt.executeQuery();
/*      */       
/* 1236 */       while (res.next())
/*      */       {
/* 1238 */         bool = true;
/* 1239 */         quantity = res.getInt("qty");
/*      */       }
/* 1241 */       res.close();
/* 1242 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1246 */       System.out.println("SQLProxyError >>  updateGiftItem  >> :" + e.toString());
/*      */     }
/*      */     
/* 1249 */     if (bool)
/*      */     {
/*      */ 
/* 1252 */       String sqlUpdate = "update gift_data set qty = ?, cost = ?, update_date=?, gameid=?  where userid=? and giftid=?;";
/* 1253 */       Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
/* 1254 */       if (type.equals("Buy"))
/*      */       {
/*      */ 
/* 1257 */         quantity += qnt;
/* 1258 */         cost = quantity * unitPrice;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1263 */         quantity -= qnt;
/* 1264 */         cost = quantity * unitPrice;
/*      */       }
/*      */       
/*      */       try
/*      */       {
/* 1269 */         PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/* 1270 */         stmtWin.setInt(1, quantity);
/* 1271 */         stmtWin.setDouble(2, cost);
/* 1272 */         stmtWin.setString(3, Appmethods.getDateTime());
/* 1273 */         stmtWin.setString(4, gameid);
/* 1274 */         stmtWin.setString(5, player);
/* 1275 */         stmtWin.setInt(6, giftId);
/* 1276 */         stmtWin.executeUpdate();
/* 1277 */         stmtWin.close();
/*      */       }
/*      */       catch (SQLException e)
/*      */       {
/* 1281 */         Appmethods.showSQLLog(" SQLProxyError >> updateGiftItem  gift_data>> :" + e.toString());
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 1287 */       String sqlInsert = "Insert INTO gift_data(userid,giftid,unitprice,qty,cost,gameid,update_date) values (?,?,?,?,?,?,?);";
/* 1288 */       Appmethods.showSQLLog(" SQLProxy >>  sqlInsert >> :" + sqlInsert);
/*      */       try
/*      */       {
/* 1291 */         PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/* 1292 */         pstmt.setString(1, player);
/* 1293 */         pstmt.setInt(2, giftId);
/* 1294 */         pstmt.setDouble(3, unitPrice);
/* 1295 */         pstmt.setInt(4, qnt);
/* 1296 */         pstmt.setDouble(5, cost);
/* 1297 */         pstmt.setString(6, gameid);
/* 1298 */         pstmt.setString(7, Appmethods.getDateTime());
/*      */         
/* 1300 */         pstmt.executeUpdate();
/* 1301 */         pstmt.close();
/*      */       }
/*      */       catch (SQLException e)
/*      */       {
/* 1305 */         System.out.println("SQLProxyError >>  updateGiftItem  gift_data:" + e.toString());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1310 */     sql = "Select * from gift_history ORDER BY id DESC LIMIT 1";
/* 1311 */     Appmethods.showSQLLog("Sql " + sql);
/* 1312 */     String t_id = getId(sql);
/*      */     
/* 1314 */     String sqlInsert = "Insert INTO gift_history(transaction_id, userid, gift_id, unitprice, gameid, receiver, datetime) values (?,?,?,?,?,?,?);";
/* 1315 */     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert gift_history>> :" + sqlInsert);
/*      */     
/*      */     try
/*      */     {
/* 1319 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/*      */       
/* 1321 */       pstmt.setString(1, "T" + t_id);
/* 1322 */       pstmt.setString(2, player);
/* 1323 */       pstmt.setInt(3, giftId);
/* 1324 */       pstmt.setDouble(4, unitPrice);
/* 1325 */       pstmt.setString(5, gameid);
/* 1326 */       pstmt.setString(6, receiver);
/* 1327 */       pstmt.setString(7, Appmethods.getDateTime());
/*      */       
/* 1329 */       pstmt.executeUpdate();
/* 1330 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1334 */       System.out.println("SQLProxyError >>  insertChatHistory  chat_history:" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void insertChatHistory(String sender, String msg, String msgType, String receiver)
/*      */   {
/* 1343 */     String sqlInsert = "Insert INTO chat_history(sender,message,msg_type,receiver,datetime) values (?,?,?,?,?);";
/* 1344 */     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert insertChatHistory>> :" + sqlInsert);
/*      */     
/*      */     try
/*      */     {
/* 1348 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/* 1349 */       pstmt.setString(1, sender);
/* 1350 */       pstmt.setString(2, msg);
/* 1351 */       pstmt.setString(3, msgType);
/* 1352 */       pstmt.setString(4, receiver);
/* 1353 */       pstmt.setString(5, Appmethods.getDateTime());
/*      */       
/* 1355 */       pstmt.executeUpdate();
/* 1356 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1360 */       System.out.println("SQLProxyError >>  insertChatHistory  chat_history:" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void insertLoginSession(String player, ISession session)
/*      */   {
/* 1368 */     updateUserLogin(1, player);
/*      */     
/* 1370 */     String sqlInsert = "Insert INTO login_data_player(userid,id_type,subid_type,logintime,ip) values (?,?,?,?,?);";
/* 1371 */     Appmethods.showSQLLog(" SQLProxy >>  sqlInsert insertLoginSession>> :" + sqlInsert);
/*      */     
/*      */     try
/*      */     {
/* 1375 */       PreparedStatement pstmt = this.con.prepareStatement(sqlInsert);
/* 1376 */       pstmt.setString(1, player);
/* 1377 */       pstmt.setString(2, "player");
/* 1378 */       pstmt.setString(3, "player");
/* 1379 */       pstmt.setString(4, Appmethods.getDateTime());
/*      */       pstmt.setString(5, session.getAddress());
                 Appmethods.showSQLLog(" IP address :" + session.getAddress());
/* 1381 */       pstmt.executeUpdate();
/* 1382 */       pstmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1386 */       System.out.println("SQLProxyError >>  insertLoginSession  login_data_player:" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateLogoutSession(String player)
/*      */   {
/* 1393 */     updateUserLogin(0, player);
/*      */     
/* 1395 */     String sql = "select id,logintime  from login_data_player where userid=? order by id desc limit 1;";
/* 1396 */     Appmethods.showSQLLog(" SQLProxy >>  sql >> :" + sql);
/* 1397 */     int id = 0;
/* 1398 */     String createdDate = "";
/*      */     try
/*      */     {
/* 1401 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/* 1402 */       stmt.setString(1, player);
/* 1403 */       ResultSet res = stmt.executeQuery();
/* 1404 */       while (res.next())
/*      */       {
/* 1406 */         id = res.getInt("id");
/* 1407 */         createdDate = res.getString("logintime");
/*      */       }
/* 1409 */       res.close();
/* 1410 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1414 */       System.out.println("SQLProxyError >>  updateLogoutSession >> :" + e.toString());
/*      */     }
/*      */     
/*      */ 
/* 1418 */     Date d1 = null;
/* 1419 */     Date d2 = null;
/* 1420 */     String currDate = null;
/* 1421 */     Calendar currentTime = Calendar.getInstance();
/* 1422 */     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 1423 */     dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
/* 1424 */     currDate = dateFormat.format(currentTime.getTime());
/*      */     
/* 1426 */     if (createdDate == "")
/* 1427 */       createdDate = currDate;
/*      */     try {
/* 1429 */       d2 = dateFormat.parse(currDate); } catch (ParseException e) { Appmethods.showSQLLog("Time sheduler >> ParseException"); }
/* 1430 */     try { d1 = dateFormat.parse(createdDate); } catch (Exception e) { e.printStackTrace();
/*      */     }
/*      */     
/* 1433 */     long totalsec = d2.getTime() - d1.getTime();
/* 1434 */     int seconds = (int)totalsec / 1000;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1439 */     String sqlUpdate = "update login_data_player set logouttime = ?, total_seconds =? where id=?;";
/* 1440 */     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
/*      */     try
/*      */     {
/* 1443 */       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/* 1444 */       stmtWin.setString(1, Appmethods.getDateTime());
/* 1445 */       stmtWin.setInt(2, seconds);
/* 1446 */       stmtWin.setInt(3, id);
/* 1447 */       stmtWin.executeUpdate();
/* 1448 */       stmtWin.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1452 */       Appmethods.showSQLLog(" SQLProxyError >> updateLogoutSession  >> :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   public void updateUserLogin(int value, String player)
/*      */   {
/* 1458 */     String sqlUpdate = "update user_master set islogin = ? where userid=?;";
/* 1459 */     Appmethods.showSQLLog(" SQLProxy >>  sqlUpdate >> :" + sqlUpdate);
/*      */     try
/*      */     {
/* 1462 */       PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/* 1463 */       stmtWin.setInt(1, value);
/* 1464 */       stmtWin.setString(2, player);
/* 1465 */       stmtWin.executeUpdate();
/* 1466 */       stmtWin.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1470 */       Appmethods.showSQLLog(" SQLProxyError >> updateLogoutSession  >> :" + e.toString());
/*      */     }
/*      */   }
/*      */   
/*      */   public ISFSObject checkDeviceId(String player, String deviceId)
/*      */   {
/* 1476 */     ISFSObject sfso = new SFSObject();
/* 1477 */     String device_id = "";
/* 1478 */     String version_id = "";
/*      */     
/* 1480 */     String sqlSelect = "select device_id,version_id from user_master where userid=? and id_type='player';";
/* 1481 */     Appmethods.showSQLLog(" checkDeviceId >> : " + sqlSelect);
/*      */     
/*      */     try
/*      */     {
/* 1485 */       PreparedStatement pstmt = this.con.prepareStatement(sqlSelect);
/* 1486 */       pstmt.setString(1, player);
/*      */       
/* 1488 */       ResultSet rs = pstmt.executeQuery();
/* 1489 */       while (rs.next()) {
/* 1490 */         device_id = rs.getString("device_id");
/* 1491 */         version_id = rs.getString("version_id");
/*      */       }
/* 1493 */       rs.close();
/* 1494 */       pstmt.close();
/*      */     }
/*      */     catch (Exception e) {
/* 1497 */       Appmethods.showSQLLog(" getUserPassword >> : " + e.toString());
/*      */     }
/*      */     
/* 1500 */     if (!device_id.equals(""))
/*      */     {
/* 1502 */       if (device_id.equals("unlock"))
/*      */       {
/*      */ 
/* 1505 */         String sqlUpdate = "update user_master set user_master.isDeviceCaputre=?, user_master.device_id=?  where user_master.userid=?;";
/* 1506 */         Appmethods.showSQLLog(" SQLProxy >>  checkDeviceId >> :" + sqlUpdate);
/*      */         
/*      */         try
/*      */         {
/* 1510 */           PreparedStatement stmtWin = this.con.prepareStatement(sqlUpdate);
/* 1511 */           stmtWin.setInt(1, 1);
/* 1512 */           stmtWin.setString(2, deviceId);
/* 1513 */           stmtWin.setString(3, player);
/* 1514 */           stmtWin.executeUpdate();
/* 1515 */           stmtWin.close();
/*      */         }
/*      */         catch (SQLException e)
/*      */         {
/* 1519 */           Appmethods.showSQLLog(" SQLProxyError >> checkDeviceId Update Profile  >> :" + e.toString());
/*      */         }
/* 1521 */         sfso.putBool("deviceIdStatus", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/* 1527 */       else if (device_id.equals(deviceId))
/*      */       {
/*      */ 
/* 1530 */         sfso.putBool("deviceIdStatus", true);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1535 */         sfso.putBool("deviceIdStatus", false);
/*      */       }
/*      */       
/*      */     }
/*      */     else {
/* 1540 */       sfso.putBool("deviceIdStatus", false);
/*      */     }
/* 1542 */     sfso.putUtfString("versionId", version_id);
/* 1543 */     return sfso;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void serverRestartedCloseGames()
/*      */   {
/* 1550 */     String sql = "select users_playing.username,users_playing.game_id from users_playing INNER JOIN game_status on users_playing.game_id = game_status.gameid and game_status.status=0";
/*      */     
/* 1552 */     Appmethods.showSQLLog(" serverRestartedCloseGames >> Sql >> :" + sql);
/*      */     try
/*      */     {
/* 1555 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/* 1556 */       ResultSet res = stmt.executeQuery();
/*      */       
/* 1558 */       while (res.next())
/*      */       {
/* 1560 */         String name = new String(res.getString("username"));
/* 1561 */         String gameid = res.getString("game_id");
/*      */         
/*      */ 
/* 1564 */         String sqldelete = "delete from users_playing where username = ? and game_id = ?";
/* 1565 */         Appmethods.showSQLLog(" serverRestartedCloseGames >> sqldelete >> :" + sqldelete);
/* 1566 */         PreparedStatement stmt1 = this.con.prepareStatement(sqldelete);
/*      */         
/* 1568 */         stmt1.setString(1, name);
/* 1569 */         stmt1.setString(2, gameid);
/*      */         
/* 1571 */         stmt1.executeUpdate();
/* 1572 */         stmt1.close();
/*      */         
/*      */ 
/* 1575 */         String sqlGameStatus = "update game_status set status=2 where gameid=?;";
/* 1576 */         Appmethods.showSQLLog(" serverRestartedCloseGames >> sqlGameStatus >>:" + sqlGameStatus);
/*      */         
/* 1578 */         PreparedStatement sqlstatus = this.con.prepareStatement(sqlGameStatus);
/* 1579 */         sqlstatus.setString(1, gameid);
/* 1580 */         sqlstatus.executeUpdate();
/* 1581 */         sqlstatus.close();
/*      */         
/*      */ 
/* 1584 */         String sqlUpdate = "update game_history set status=2 where game_id = ?";
/* 1585 */         Appmethods.showSQLLog(" serverRestartedCloseGames >> sqlUpdate  >> :" + sqlUpdate);
/* 1586 */         PreparedStatement stmt2 = this.con.prepareStatement(sqlUpdate);
/*      */         
/* 1588 */         stmt2.setString(1, gameid);
/*      */         
/* 1590 */         stmt2.executeUpdate();
/* 1591 */         stmt2.close();
/*      */       }
/*      */       
/* 1594 */       res.close();
/* 1595 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/* 1599 */       Appmethods.showSQLLog("SQLProxyError >>  serverRestartedCloseGames >> :" + e.toString());
/*      */     }
/*      */   }

    public LinkedList<String> getNpcNames()
/*      */   {
/*  739 */     String sql = "select npc_users.npc_name from npc_users";
/*  740 */     LinkedList<String> games = new LinkedList<>();
/*      */     try
/*      */     {
/*  743 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  744 */
/*  745 */       ResultSet res = stmt.executeQuery();
/*  746 */       while (res.next())
/*      */       {
/*  748 */         games.add(res.getString("npc_name"));
/*      */       }
/*  750 */       res.close();
/*  751 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  755 */       System.out.println("SQLProxyError >>  getCurrentRunningGames  >> :" + e.toString());
/*      */     }
/*  757 */     return games;
/*      */   }

    public ArrayList<Integer> getNpcforRoom()
/*      */   {
/*  739 */     String sql = "select npc_for_room.room from npc_for_room";
/*  740 */     ArrayList<Integer> result = new ArrayList<>();
/*      */     try
/*      */     {
/*  743 */       PreparedStatement stmt = this.con.prepareStatement(sql);
/*  744 */
/*  745 */       ResultSet res = stmt.executeQuery();
/*  746 */       while (res.next())
/*      */       {
/*  748 */         result.add(res.getInt("room"));
/*      */       }
/*  750 */       res.close();
/*  751 */       stmt.close();
/*      */     }
/*      */     catch (SQLException e)
/*      */     {
/*  755 */       System.out.println("SQLProxyError >>  getCurrentRunningGames  >> :" + e.toString());
/*      */     }
/*  757 */     return result;
/*      */   }
/*      */ }
/*      */


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/proxy/SQLProxy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */