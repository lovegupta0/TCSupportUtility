package com.teamcenter.lg.utility;

public class ParseCommandLineArgument {
	private String tInput=null;
	private String toutput=null;
	private String password="";
	private Mode mode;
	private int batchSize=1;
	private EncryptionAndDecryption ed=new EncryptionAndDecryption();
	private ReadConfigFile config;
	public ParseCommandLineArgument (String[] args) {
		config=ReadConfigFile.getInstance();
		for (String str:args) {
			String[] val=str.split("=");
			switch (val[0].toLowerCase()) {
			case"-input":config.setInput (val [1]); break;
			case "-output": config.setOutput (val[1]); break;
			case "-password": setPassword(val[1]); break;
			case "-encrypt": setMode(Mode.ENCRYPT); break; 
			case "-decrypt": setMode(Mode.DECRYPT); break;
			case "-batch_size":config.setBatchSize (Integer.parseInt(val[1]));
			default:{ help(); System.exit(1); }
			}
		}
		if (Mode.ENCRYPT==getMode()) {
			try {
				System.out.println(ed.encrypt(getPassword())); 

			}
			catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
		else if (Mode.DECRYPT==getMode()) {
			try {
				System.out.println(ed.decrypt(getPassword()));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
		
	}

	public String getPassword() {
		return password;
	}
	public Mode getMode() {
		return mode;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	public void help() {
		String str="\n-password=<enter password>\r\n"
		+ "-encrypt -> to encrypt above password\r\n"
		+ "-decrypt -> to decrypt above password\r\n"
		+ "-help -> for help option\r\n"
		+ "\r\n"
		+ "eg: -password=\"something\" -encrypt";
		System.out.println(str);
		}
	
}
	