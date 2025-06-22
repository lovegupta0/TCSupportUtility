package com.teamcenter.lg.operations;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.teamcenter.lg.session.SessionHandler;
import com.teamcenter.lg.utility.Logger;
import com.teamcenter.lg.utility.ReadConfigFile;
import com.teamcenter.lg.IO.*;

public class UIDOperations {
	private Logger log;
	private ReadConfigFile read;
	private SessionHandler session;
	
	public UIDOperations(SessionHandler session) {
		this.session= session;
		this.log=Logger.getInstance();
		this.read=ReadConfigFile.getInstance();
	} 
	public void findUIDorITEMID() throws IOException {
		if (read.getFind().equals("UID")) ItemToUID();
		else UIDToItem();
		}

	private void ItemToUID() throws IOException {
		OutFoundResult output=OutFoundResult.getInstance(); 
		OutNotFoundResult pp=OutNotFoundResult.getInstance();
		BufferedReader reader=new BufferedReader (new FileReader(read.getInput()));
		SearchItem item=new SearchItem(session.getConnect());
		String str=reader.readLine();
		item.getSavedQueries(read.getQueryName());
		String[] header=null;
		output.write (str+",UID");
		pp.write(str+",Remark");
		if(str!=null) {
			header=str.split(",");
		}
		int count=0;
		List<String[]> lst=new ArrayList<>();
		while ((str=reader.readLine()) !=null) {
			lst.add(str.split(","));
			if(lst.size()<read.getBulkSize()) continue;
			else {
				count++;
				item.searchItem(lst, header);
				lst.clear();
			}
			if (count==read.getBatchSize()) {
				count=0;
				reconnectServices(item);
			}
		}
		if(lst.size()>0) {
			item.searchItem(lst, header);
			lst.clear();
		}
		output.close();
		pp.close();
		reader.close();
	}


	private void UIDToItem() throws IOException {
		OutFoundResult output=OutFoundResult.getInstance();
		OutNotFoundResult pp=OutNotFoundResult.getInstance(); 
		BufferedReader reader=new BufferedReader(new FileReader (read.getInput()));
		SearchItem item=new SearchItem(session.getConnect());
		String line;
		int count=0;
		List<String> lst=new ArrayList<>(); 
		while((line=reader.readLine())!=null) {
			lst.add(line);
			if(lst.size()<read.getBulkSize()) continue;
			else {
				count++;
				item.getObject(lst);
				lst.clear();
			} 
			if (count==read.getBatchSize()) {
				count=0;
				reconnectServices(item);
			}
		}
		if(lst.size()>0) {
			item.getObject (lst); 
			lst.clear();
		}
		output.close(); 
		pp.close(); 
		reader.close();
	}
	private void reconnectServices (SearchItem item) {
		session.logout();
		session.reconnect();
		item=new SearchItem(session.getConnect());
		if(!read.getFind().equals("UID")) item.getSavedQueries(read.getQueryName());
	}
		
}
