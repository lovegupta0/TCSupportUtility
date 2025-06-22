package com.teamcenter.lg.operations;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.teamcenter.lg.session.SessionHandler;
import com.teamcenter.lg.utility.Logger;
import com.teamcenter.lg.utility.ReadConfigFile;
import com.teamcenter.lg.IO.*;

public class AttributesOperation {
	private SessionHandler session;
	private Logger log;
	private ReadConfigFile read;
	public AttributesOperation (SessionHandler session) {
		this.session = session;
		this.log =Logger.getInstance();
		this.read=ReadConfigFile.getInstance();
	}

	public void findAttributes() throws IOException {
		if (read.getAttributeFrom().toUpperCase().equals("UID")) UIDAttributes();
		else itemAttributes();
	}
	private void UIDAttributes() throws IOException {
		OutFoundResult output=OutFoundResult.getInstance();
		OutNotFoundResult pp=OutNotFoundResult.getInstance(); 
		BufferedReader reader=new BufferedReader(new FileReader (read.getInput()));
		SearchItem item=new SearchItem(session.getConnect());
		String[] attribute=read.getAttributes().split(",");
		output.write("UID,"+read.getAttributes());
		pp.write("UID"+",Remark");
		String str;
		int count=0;
		List<String> lst=new ArrayList<>();
		while((str=reader.readLine())!=null) {
			lst.add(str);
			if(lst.size()<read.getBulkSize())continue;
			else {
				item.getAttributeUid (lst, attribute); count++;
				lst.clear();
			}
			if (count==read.getBatchSize()) {
				count=0;
				reconnectServices (item);
			}
		}
			if (lst.size()>0) {
				item.getAttributeUid(lst, attribute); 
				lst.clear();
			}
			output.close();
			pp.close();
			reader.close();
	}
	
	private void itemAttributes() throws IOException {
		OutFoundResult output=OutFoundResult.getInstance();
		OutNotFoundResult pp=OutNotFoundResult.getInstance();
		BufferedReader reader=new BufferedReader(new FileReader (read.getInput()));
		String[] attribute=read.getAttributes().split(",");
		SearchItem item=new SearchItem(session.getConnect());
		item.getSavedQueries(read.getQueryName());
		String [] header=null; 
		String str=reader.readLine();
		if(str!=null) {
			header=str.split(",");
			output.write(str+","+read.getAttributes());
		}
		int count=0;
		pp.write(str+", Remark");
		List<String[]> lst=new ArrayList<>();
		while ((str=reader.readLine())!=null) {
			lst.add(str.split(","));
			if(lst.size()<read.getBulkSize()) continue;
			else {
				count++;
				item.getAttributeItem(lst, header, attribute); 
				lst.clear();
			}
			if (count==read.getBatchSize()) {
				count=0;
				reconnectServices (item);
			}
		}
		if(lst.size()>0) {
			item.getAttributeItem(lst, header, attribute);
			lst.clear();
		}
		output.close();
		pp.close();
		reader.close();
	}
	
	private void reconnectServices(SearchItem item) {
			session.logout();
			session.reconnect();
			item=new SearchItem(session.getConnect());
			if(!read.getAttributeFrom().toUpperCase().equals("UID")) item.getSavedQueries(read.getQueryName());
			}
}
