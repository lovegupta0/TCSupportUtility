package com.teamcenter.lg.utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
public class CleanUp {
	private String filename="logs\\briefcase_log.txt";
	private String folder="";
	public CleanUp() {
		Date d=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM");
		String date=sdf.format(d);
		folder="logs\\"+date;
	}
	public void cleanUpTask() {
		File file=new File(filename);
		file.deleteOnExit();
	}
	public void cleanUpFolder() {
		try {
			FileUtils.forceDeleteOnExit (new File(folder)); 
			new File(folder).deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
