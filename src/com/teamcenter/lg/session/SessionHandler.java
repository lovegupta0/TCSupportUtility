package com.teamcenter.lg.session;
import com.teamcenter.lg.utility.Logger;
import com.teamcenter.schemas.soa._2006_03.exceptions.InvalidCredentialsException;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.core.SessionService;
import com.teamcenter.services.strong.core._2006_03.Session.LoginResponse;
import com.teamcenter.soa.SoaConstants;
import com.teamcenter.soa.client.Connection; 
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.services.strong.core._2007_01.Session.GetTCSessionInfoResponse;
public class SessionHandler {
	private SessionService session;
	private Connection connect;
	private CredentialManagerImp credentialManager;
	private Logger log;
	public SessionHandler (String host) {
		log=Logger.getInstance();
		credentialManager=new CredentialManagerImp();
		connect=new Connection (host, credentialManager, SoaConstants.REST, SoaConstants.HTTP);
	}
	public Boolean loginTC(String user, String password, String group) {
			session=SessionService.getService(connect);
			GetTCSessionInfoResponse tcSessionInfo=null;
			LoginResponse res=null;
			try {
			
				res=session.loginSSO(user, password, group,"" , "TC");
			} catch (InvalidCredentialsException e) {
				log.write(-1, "Unable to Login...");
				log.write(-1, e.getMessage());
				System.out.println(e.getMessage());
				return false;
			}
			if (res!=null) {
				System.out.println("Login Sucessfull");
				try {
					tcSessionInfo=session.getTCSessionInfo();
					ModelObject loginUser= tcSessionInfo.user;
					if (!loginUser.getPropertyDisplayableValue ("user name").isEmpty())
						log.write(0, "user" +loginUser.getPropertyDisplayableValue("user_name")+" Sucessfully Logged in");
				}catch (Exception e) {
					log.write(-1, e.getMessage()); 
					System.out.println(e.getMessage());
					return false;
				}
				return true;
			}
			else {
				log.write(-1, "Unable to Login..."); 
				System.out.println("Login unsucessfull");
				return false;
			}
	}
	public void logout() {
		try {
			if(session!=null) {
				session.logout();
				log.write(0, "Sucessfully Logout");
				System.out.println("Sucessfully Logout");
			}
		} catch (ServiceException e) {
			log.write(-1, e.getMessage());
			System.out.println(e.getMessage());
		}
	}
	public Connection getConnect() {
		return connect;
	}
	public SessionService getsession() {
		return session;
	}
	public void reconnect() {
		loginTC (credentialManager.getName(), credentialManager.getPassword(), credentialManager.getGroup());
	}

}
