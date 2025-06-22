package com.teamcenter.lg.utility;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
public class ReadConfigFile {
	private String tUrl="";
	private String tUsername="";
	private String tPassword="";
	private String tGroup="";
	private String filename="config\\config.properties";
	private Logger log;
	private String tFind="";
	private String tQueryName="";
	private String tInput="";
	private String toutput="output.csv";
	private String tAttributes="";
	private EncryptionAndDecryption decyper=new EncryptionAndDecryption();
	private String tAttributeFrom="";
	private int tbatchSize=3000;
	private int tBulkSize=1;
	private static volatile ReadConfigFile instance;
	
	private ReadConfigFile() {
		this.log=Logger.getInstance(); 
		Properties prop;
		InputStream input = null;
		try {
			prop=new Properties();
			input=new FileInputStream(filename);
			if (input!=null) {
				prop.load(input);
			}
			else {
				log.write(-1, "File not Found...");
				System.out.println("File not found"); System.exit(-1);
				return;
			}
		tUrl=prop.getProperty("url");
		tUsername=prop.getProperty("user");
		tPassword=prop.getProperty("password");
		tGroup=prop.getProperty("group");
		tQueryName=prop.getProperty("query_name");
		tFind=prop.getProperty("find");
		tInput=prop.getProperty("input");
		tAttributes=prop.getProperty("attributes");
		tAttributeFrom=prop.getProperty("find_attributes_from");
		log.write(0, "URL: "+ tUrl);
		log.write(0, "USER: "+ tUsername);
		tInput=prop.getProperty("input");
		tAttributes=prop.getProperty("attributes");
		tAttributeFrom=prop.getProperty("find_attributes_from");
		log.write(0, "URL: "+tUrl);
		log.write(0, "USER: "+ tUsername);
		log.write(0, "Password: ***********");
		log.write(0, "GROUP: "+ tGroup);
		log.write(0, "Query Name: "+tQueryName);
		log.write(0, "Find: "+tFind);
		log.write(0, "Find Attributes From: "+tAttributeFrom);
		log.write(0, "Attributes: "+tAttributes);
		if (prop.getProperty("output")!=null && prop.getProperty("output").length()>0) { 
			toutput=prop.getProperty("output");
		}
		if (prop.getProperty("batch_size") != null) {
			tbatchSize=Integer.parseInt (prop.getProperty("batch_size"));
		} 
		if (prop.getProperty("bulk_size") != null) {
			tBulkSize=Integer.parseInt(prop.getProperty("bulk_size"));
		}
			log.write(0, "Bulk Size: "+tBulkSize);
		} 
		catch (Exception e) {
			log.write(-1, e.getMessage());
			System.out.println(e.getMessage());
		}
		finally {
			try {
				if (input!=null)
					input.close();
			} catch (IOException e) {
				log.write(-1, e.getMessage());
				System.out.println(e.getMessage());;
			}
		}
	}
	public static ReadConfigFile getInstance() {
		if (instance==null) {
			synchronized (ReadConfigFile.class) {
				if (instance==null) {
					instance=new ReadConfigFile();
				}
			}
		}
		return instance;
	}
	public String getUrl() {
		return tUrl;
	}
	public String getUsername() {
		return tUsername;
	}
	public String getPassword() {
		try {
			return decyper.decrypt(tPassword);
		}
		catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
		return "";
	}
	public String getGroup() {
		return tGroup;
	}
	public String getFilename() {
		return filename;
	}
	public String getFind() {
		return tFind.toUpperCase();
	}
	public String getQueryName() {
		return tQueryName;
	}
	public String getInput() {
		return tInput;
	}
	public String getOutput() {
		return toutput;
	}
	public String getAttributes() {
		return tAttributes;
	}
	public String getAttributeFrom() {
		return tAttributeFrom.toLowerCase();
	}
	public int getBatchSize() {
		return tbatchSize;
	}
	public int getBulkSize() {
		return tBulkSize;
	}
	public void setUrl(String tUrl) {
		this.tUrl = tUrl;
	}
	public void setUsername(String tUsername) {
		this.tUsername = tUsername;
	}
	public void setPassword(String tPassword) {
		this.tPassword = tPassword;
	}
	public void setGroup(String tGroup) {
		this.tGroup = tGroup;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public void setFind(String tFind) {
		this.tFind = tFind;
	}
	public void setQueryName(String tQueryName) {
		this.tQueryName = tQueryName;
	}
	public void setInput(String tInput) {
		this.tInput = tInput;
	}
	public void setOutput(String toutput) {
		this.toutput = toutput;
	}
	public void setAttributes(String tAttributes) {
		this.tAttributes = tAttributes;
	}
	public void setAttributeFrom(String tAttributeFrom) {
		this.tAttributeFrom = tAttributeFrom;
	}
	public void setBatchSize(int tbatchSize) {
		log.write(0, "Batch Size: "+tbatchSize);
		this.tbatchSize = tbatchSize;
	}
	public void setBulkSize(int tBulkSize) {
		this.tBulkSize = tBulkSize;
	}
	
}
