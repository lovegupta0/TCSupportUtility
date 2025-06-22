package com.teamcenter.lg;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.teamcenter.lg.operations.*;
import com.teamcenter.lg.session.SessionHandler;
import com.teamcenter.lg.utility.CleanUp;
import com.teamcenter.lg.utility.Logger;
import com.teamcenter.lg.utility.ParseCommandLineArgument;
import com.teamcenter.lg.utility.ReadConfigFile;

public class Main {

	public static void main(String[] args) {
		CleanUp clean=new CleanUp();
		StringWriter errors = new StringWriter();
		Logger log=Logger.getInstance();
		ReadConfigFile read=ReadConfigFile.getInstance();
		ParseCommandLineArgument cmd=new ParseCommandLineArgument(args);
		String outputFile=read.getOutput();
		String inputFile=read.getInput();
		SessionHandler session=new SessionHandler(read.getUrl());
		session.loginTC(read.getUsername(),read.getPassword(),read.getGroup());
		clean.cleanUpTask ();
		log.write(0, "Input: "+inputFile);
		log.write(0, "Output: "+outputFile);
		UIDOperations uidOp=new UIDOperations(session);
		try {
			if (read.getFind().equals("UID")) {
				uidOp.findUIDorITEMID();
			}
			else if (read.getFind().equals("ATTRIBUTES")) {
				AttributesOperation AO=new AttributesOperation (session); 
				AO.findAttributes();
			}
			else {
				uidOp.findUIDorITEMID();
			}
		} catch (Exception e) {
			log.write(-1, e.getMessage());
			e.printStackTrace (new PrintWriter(errors));
			log.write(-1, errors.toString());
		}
		finally {
			session.logout();
			System.out.println("Utility ran Successfully...");
			log.write(0, "Utility ran Successfully...");
			log.close();
			clean.cleanUpFolder();
		}

	}

}
