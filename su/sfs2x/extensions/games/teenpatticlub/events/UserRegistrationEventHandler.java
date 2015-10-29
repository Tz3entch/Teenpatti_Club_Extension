package su.sfs2x.extensions.games.teenpatticlub.events;

import su.sfs2x.extensions.games.teenpatticlub.constants.Commands;
import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class UserRegistrationEventHandler extends BaseClientRequestHandler {
	
	public void handleClientRequest(User sender, ISFSObject params){
		Appmethods.showLog("RegistrationEventHandler");
		//Gets user sent parameters
		String username = params.getUtfString("username");
		String password = params.getUtfString("password");
		String email = params.getUtfString("email");
		String mobile = params.getUtfString("mobile");
		String dob = params.getUtfString("dob");
		Commands.appInstance.proxy.registerUser(username, password, dob, email, mobile);
		
		params.putUtfString("success", "User successfully registered");
		send("register", params, sender);
	}

}
