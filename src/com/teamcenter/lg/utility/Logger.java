package com.teamcenter.lg.utility;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.*;
public class Logger {
	private String filename;
	private FileWriter log=null;
	private static volatile Logger instance;
	private Logger() {
		Date d=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-YYYY");
		String date=sdf.format(d);
		filename="logs\\input_log_"+date+".log";
		File file=new File(filename);
		if(!file.exists()) {
		File folder=new File("logs\\");
		if(!folder.exists()) {
		folder.mkdir();
		}
		try {
		if (file.createNewFile()) {
			System.out.println("Log file sucessfully created.....");
		}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		}
		try {
			log=new FileWriter (filename, true);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	private String getDate() {
		Date d=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("dd-M-YYYY HH:mm:SS");
		String date=sdf.format(d);
		return date;
	}
	public void write(int n,String msg) {
		if (log==null) return;
		String date=getDate();
		switch(n) {
		case 0:{
			try {
				log.write("INFO : "+date+" : "+msg+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		case 1:{
			try {
				log.write("WARN : "+date+" : "+msg+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}break;
		}
		default:{
			try {
				log.write("ERROR: "+date+" : "+msg+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		}
	}
	public static Logger getInstance() {
		if(instance==null) {
			synchronized (Logger.class) {
				if(instance==null) {
					instance=new Logger();
				}
			}
		}
		return instance;
	}
	public void close() {
		try {
			log.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
