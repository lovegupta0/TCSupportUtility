package com.teamcenter.lg.IO;

import java.io.FileWriter;
import java.io.IOException;

import com.teamcenter.lg.utility.ReadConfigFile;

public class OutNotFoundResult {
	private ReadConfigFile config;
	private static volatile OutNotFoundResult instance;
	private FileWriter output;
	private OutNotFoundResult() {
		config=ReadConfigFile.getInstance();
		String ex=config.getOutput().substring(config.getOutput().length()-4);
		try {
			output=new FileWriter (config.getOutput().replace(ex, "_problem_Item"+ex), false);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	public static OutNotFoundResult getInstance() {
		if (instance==null) {
			synchronized (OutNotFoundResult.class) {
				if (instance==null) {
					instance=new OutNotFoundResult();
				}
			}
		}
			return instance;
	}
	public void write(String msg) {
		try {
			output.write(msg+"\n");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void close() {
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			}
	}
	
}
