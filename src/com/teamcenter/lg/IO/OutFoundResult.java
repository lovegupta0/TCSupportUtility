package com.teamcenter.lg.IO;

import java.io.FileWriter;
import java.io.IOException;

import com.teamcenter.lg.utility.ReadConfigFile;

public class OutFoundResult {
	private ReadConfigFile config; 
	private static volatile OutFoundResult instance;
	private FileWriter output; 
	
	private OutFoundResult() { 
		config=ReadConfigFile.getInstance();
		try { 
		output=new FileWriter (config.getOutput(), false); 
		} catch (IOException e) { 
		e.printStackTrace(); 
		System.exit(-1); 
		}
	}
	public static OutFoundResult getInstance() { 
		if (instance==null) { 
			synchronized (OutFoundResult.class) { 
				if (instance==null) { 
					instance=new OutFoundResult(); 
				} 
			} 
		}
		return instance;
	}
	public void write (String msg) { 
		try { 
			output.write(msg+"\n"); 
		} catch (IOException e) { 
			e.printStackTrace();
			System.exit(-1); 
		} 
	} 
	public void close () { 
		try { 
			output.close(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
			System.exit(-1); 
		}
	}
	
	
}
