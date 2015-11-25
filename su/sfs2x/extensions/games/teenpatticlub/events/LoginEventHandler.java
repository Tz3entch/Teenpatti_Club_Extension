 package su.sfs2x.extensions.games.teenpatticlub.events;
 
 import com.smartfoxserver.bitswarm.sessions.ISession;
 import com.smartfoxserver.v2.core.ISFSEvent;
 import com.smartfoxserver.v2.core.SFSEventParam;
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.exceptions.SFSErrorCode;
 import com.smartfoxserver.v2.exceptions.SFSErrorData;
 import com.smartfoxserver.v2.exceptions.SFSException;
 import com.smartfoxserver.v2.exceptions.SFSLoginException;
 import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
 import com.smartfoxserver.v2.security.DefaultPermissionProfile;
 import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class LoginEventHandler extends BaseServerEventHandler
 {
   public void handleServerEvent(ISFSEvent event) throws SFSException
   {
     Appmethods.showLog("**************LoginEventHandler*************");
     
     String userName = (String)event.getParameter(SFSEventParam.LOGIN_NAME);
     String cryptedPass = (String)event.getParameter(SFSEventParam.LOGIN_PASSWORD);
     ISession session = (ISession)event.getParameter(SFSEventParam.SESSION);
     
 
     Appmethods.showLog("User Name " + userName);
     Appmethods.showLog("User Currrent IP:" + session.getFullIpAddress());
     Appmethods.showLog(session.getAddress());

     if (!userName.startsWith("##"))
     {
 
       ISFSObject sfso = Commands.appInstance.proxy.getUserDetails(userName);
       
 
       String dbPassword = sfso.getUtfString("password");
       String dbUserName = sfso.getUtfString("username");
       String status = sfso.getUtfString("status");
       
       System.out.println("password  is : " + dbPassword);
       System.out.println("password client : " + cryptedPass);
       System.out.println("emailActivationStatus : " + status);
       
 
 
       if (dbUserName == null)
       {
         SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
         data.addParameter(userName + " not found");
         trace(new Object[] { " ## USER NOT REGISTERED ##" });
         Appmethods.showLog("Login Exception Login with Invalid Username");
         
         throw new SFSLoginException("Please Register ", data);
       }
       if (dbPassword == null)
       {
         SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_GUEST_NOT_ALLOWED);
         data.addParameter(userName);
         trace(new Object[] { " ## USER NOT REGISTERED ##" });
         Appmethods.showLog("Login Exception Login with Invalid Password");
         
         throw new SFSLoginException("Please Register ", data);
       }
       
       if (status.equals("N"))
       {
         SFSErrorData data = new SFSErrorData(SFSErrorCode.INVITATION_NOT_VALID);
         data.addParameter(userName);
         trace(new Object[] { " ## Your account is not acitivated yet or blocked ##" });
         Appmethods.showLog("Login Exception account is not acitivated yet or blocked");
         throw new SFSLoginException("Your account is not acitivated yet or blocked! ", data);
       }
       if ((dbPassword.equals("")) || (dbPassword == null))
       {
         SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
         data.addParameter(userName);
         trace(new Object[] { " ## You must enter a password. ##" });
         Appmethods.showLog("Login Exception failed password");
         throw new SFSLoginException(" You must enter a password.", data);
       }
       if (!getApi().checkSecurePassword(session, dbPassword, cryptedPass))
       {
         SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
         data.addParameter(userName);
         trace(new Object[] { "Login failed for user     : " + userName });
         Appmethods.showLog("Login Exception Login with wrong password");
         throw new SFSLoginException("Login failed for user: " + userName, data);
       }
       
 
       Appmethods.showLog("Login Success");
       trace(new Object[] { "############  CUSTOM_LOGIN_SUCCESSFUL  #############  : " + userName });
       
 
       Commands.appInstance.proxy.insertLoginSession(userName, session);
                session.setProperty("$permission", DefaultPermissionProfile.STANDARD);
 
 
     }
     else
     {
 
       trace(new Object[] { "############  Guest Login Success  #############  : " + userName });
     }
   }
 }


