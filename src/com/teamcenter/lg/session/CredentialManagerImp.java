package com.teamcenter.lg.session;
import com.teamcenter.schemas.soa. _2006_03.exceptions. InvalidCredentialsException;
import com.teamcenter.schemas.soa._2006_03.exceptions. InvalidUserException;
import com.teamcenter.soa.client.CredentialManager;
import com.teamcenter.soa.exceptions.CanceledOperationException;
public class CredentialManagerImp implements CredentialManager {
	private String name= null;
	private String password = null;
	private String group= ""; 
	private String role = "";
	private String discriminator ="TC Login";

	@Override
	public int getCredentialType() {
		return CredentialManager.CLIENT_CREDENTIAL_TYPE_STD;
	}

	@Override
	public String[] getCredentials(InvalidCredentialsException arg0) throws CanceledOperationException {
		String[] tokens= { name, password, group, role, discriminator }; 
		return tokens;
		
	}

	@Override
	public String[] getCredentials(InvalidUserException arg0) throws CanceledOperationException {
		String[] tokens= { name, password, group, role, discriminator }; 
		return tokens;
	}

	@Override
	public void setGroupRole(String group, String role) {
		this.group=group;
		this.role=role;
		
	}

	@Override
	public void setUserPassword(String user, String password, String discriminator) {
		this.name=user;
		this.password=password;
		this.discriminator=discriminator;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getGroup() {
		return group;
	}

	public String getRole() {
		return role;
	}

	

}
