/*     */ package su.sfs2x.extensions.games.teenpatticlub.events;
/*     */ 
/*     */ import com.smartfoxserver.bitswarm.sessions.ISession;
/*     */ import com.smartfoxserver.v2.api.ISFSApi;
/*     */ import com.smartfoxserver.v2.core.ISFSEvent;
/*     */ import com.smartfoxserver.v2.core.SFSEventParam;
/*     */ import com.smartfoxserver.v2.entities.data.ISFSObject;
/*     */ import com.smartfoxserver.v2.exceptions.SFSErrorCode;
/*     */ import com.smartfoxserver.v2.exceptions.SFSErrorData;
/*     */ import com.smartfoxserver.v2.exceptions.SFSException;
/*     */ import com.smartfoxserver.v2.exceptions.SFSLoginException;
/*     */ import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.security.DefaultPermissionProfile;

/*     */ import java.io.PrintStream;
import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.main.Main;
import su.sfs2x.extensions.games.teenpatticlub.proxy.SQLProxy;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoginEventHandler extends BaseServerEventHandler
/*     */ {
/*     */   public void handleServerEvent(ISFSEvent event) throws SFSException
/*     */   {
/*  27 */     Appmethods.showLog("**************LoginEventHandler*************");
/*     */     
/*  29 */     String userName = (String)event.getParameter(SFSEventParam.LOGIN_NAME);
/*  30 */     String cryptedPass = (String)event.getParameter(SFSEventParam.LOGIN_PASSWORD);
/*  31 */     ISession session = (ISession)event.getParameter(SFSEventParam.SESSION);
/*     */     
/*     */ 
/*  34 */     Appmethods.showLog("User Name " + userName);
/*  35 */     Appmethods.showLog("User Currrent IP:" + session.getFullIpAddress());
/*  36 */     Appmethods.showLog(session.getAddress());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  41 */     if (!userName.startsWith("##"))
/*     */     {
/*     */ 
/*  44 */       ISFSObject sfso = Commands.appInstance.proxy.getUserDetails(userName);
/*     */       
/*     */ 
/*  47 */       String dbPassword = sfso.getUtfString("password");
/*  48 */       String dbUserName = sfso.getUtfString("username");
/*  49 */       String status = sfso.getUtfString("status");
/*     */       
/*  51 */       System.out.println("password  is : " + dbPassword);
/*  52 */       System.out.println("password client : " + cryptedPass);
/*  53 */       System.out.println("emailActivationStatus : " + status);
/*     */       
/*     */ 
/*     */ 
/*  57 */       if (dbUserName == null)
/*     */       {
/*  59 */         SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
/*  60 */         data.addParameter(userName + " not found");
/*  61 */         trace(new Object[] { " ## USER NOT REGISTERED ##" });
/*  62 */         Appmethods.showLog("Login Exception Login with Invalid Username");
/*     */         
/*  64 */         throw new SFSLoginException("Please Register ", data);
/*     */       }
/*  66 */       if (dbPassword == null)
/*     */       {
/*  68 */         SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_GUEST_NOT_ALLOWED);
/*  69 */         data.addParameter(userName);
/*  70 */         trace(new Object[] { " ## USER NOT REGISTERED ##" });
/*  71 */         Appmethods.showLog("Login Exception Login with Invalid Password");
/*     */         
/*  73 */         throw new SFSLoginException("Please Register ", data);
/*     */       }
/*     */       
/*  76 */       if (status.equals("N"))
/*     */       {
/*  78 */         SFSErrorData data = new SFSErrorData(SFSErrorCode.INVITATION_NOT_VALID);
/*  79 */         data.addParameter(userName);
/*  80 */         trace(new Object[] { " ## Your account is not acitivated yet or blocked ##" });
/*  81 */         Appmethods.showLog("Login Exception account is not acitivated yet or blocked");
/*  82 */         throw new SFSLoginException("Your account is not acitivated yet or blocked! ", data);
/*     */       }
/*  84 */       if ((dbPassword.equals("")) || (dbPassword == null))
/*     */       {
/*  86 */         SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
/*  87 */         data.addParameter(userName);
/*  88 */         trace(new Object[] { " ## You must enter a password. ##" });
/*  89 */         Appmethods.showLog("Login Exception failed password");
/*  90 */         throw new SFSLoginException(" You must enter a password.", data);
/*     */       }
/*  92 */       if (!getApi().checkSecurePassword(session, dbPassword, cryptedPass))
/*     */       {
/*  94 */         SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
/*  95 */         data.addParameter(userName);
/*  96 */         trace(new Object[] { "Login failed for user     : " + userName });
/*  97 */         Appmethods.showLog("Login Exception Login with wrong password");
/*  98 */         throw new SFSLoginException("Login failed for user: " + userName, data);
/*     */       }
/*     */       
/*     */ 
/* 102 */       Appmethods.showLog("Login Success");
/* 103 */       trace(new Object[] { "############  CUSTOM_LOGIN_SUCCESSFUL  #############  : " + userName });
/*     */       
/*     */ 
/* 106 */       Commands.appInstance.proxy.insertLoginSession(userName, session);
                session.setProperty("$permission", DefaultPermissionProfile.STANDARD);
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 113 */       trace(new Object[] { "############  Guest Login Success  #############  : " + userName });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/yuggupta/Desktop/teenpathiExtension.jar!/su/sfs2x/extensions/games/teenpathi/events/LoginEventHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */