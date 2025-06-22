package com.teamcenter.lg.operations;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import com.teamcenter.lg.IO.OutFoundResult;
import com.teamcenter.lg.IO.OutNotFoundResult;
import com.teamcenter.lg.utility.Logger;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.core.DataManagementService; 
import com.teamcenter.services.strong.query.SavedQueryService;
import com.teamcenter.services.strong.query._2006_03.SavedQuery.GetSavedQueriesResponse;
import com.teamcenter.services.strong.query._2007_09.SavedQuery.QueryResults;
import com.teamcenter.services.strong.query._2007_09.SavedQuery.SavedQueriesResponse;
import com.teamcenter.services.strong.query._2008_06.SavedQuery.QueryInput;
import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.model.ModelObject; 
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.client.model.strong.ImanQuery;

public class SearchItem {
	private Connection connect;
	private DataManagementService dmService;
	private SavedQueryService queryService;
	private ImanQuery query;
	private Logger log;
	private StringWriter errors= new StringWriter();
	private OutFoundResult output;
	private OutNotFoundResult pp;
	
	public SearchItem (Connection connect) {
		this.connect=connect;
		dmService=DataManagementService.getService(connect);
		queryService=SavedQueryService.getService (connect);
		this.log=Logger.getInstance();
		output=OutFoundResult.getInstance();
		pp=OutNotFoundResult.getInstance();
	}
	
	public void getSavedQueries(String queryName) {
		GetSavedQueriesResponse savedQueries;
		try {
			savedQueries=queryService.getSavedQueries();
			if (savedQueries.queries.length== 0){
				System.out.println("There are no saved queries in the system."); 
				log.write(-1, "There are no saved queries in the system."); 
				return;
			}
		
			for(int i=0;i<savedQueries.queries.length;i++) {
				if (savedQueries.queries[i].name.equals(queryName)) {
					query= savedQueries.queries[i].query; 
					log.write(0, "Saved Query Found: "+ queryName); 
					return;
				}
			}
		}catch (Exception e) { 
			System.out.println(e.getMessage());
			log.write(-1, e.getMessage()); 
			e.printStackTrace (new PrintWriter(errors)); 
			log.write(-1, errors.toString());
			
		}
		log.write(-1, "Saved Query Not Found: "+ queryName);
	}
	
	public void searchItem(List<String[]> lst, String[] header) {
		try {
			QueryInput[] savedQueryInput =new QueryInput[lst.size()]; 
			for(int i=0;i<lst.size();i++) {
				savedQueryInput[i]= new QueryInput();
				savedQueryInput[i].query=query;
				savedQueryInput[i].entries =new String [header.length]; 
				savedQueryInput[i].values= new String [header.length];
				savedQueryInput[i].maxNumToReturn= 25;
				for(int j=0;j<header.length;j++) {
					savedQueryInput[i].entries[j]=header[j];
					savedQueryInput[i].values[j]=lst.get(i)[j];
				}
		}
		SavedQueriesResponse savedQueryResult=queryService.executeSavedQueries(savedQueryInput);
		for(int i=0;i<lst.size();i++){ 
			String data=Arrays.stream(lst.get(i)).map(e->"\""+e+"\"").collect(Collectors.joining(","));
			if (savedQueryResult.arrayOfResults[i].objectUIDS.length>1) {
				String res=Arrays.stream(savedQueryResult.arrayOfResults[i].objectUIDS).map(e->e).collect (Collectors.joining(","));
				output.write(data+", \""+res+"\""); log.write(0,lst.get(i) [0]+": Found");
				System.out.println(lst.get(i) [0]+": Found");
			}
		else {
			if (savedQueryResult.arrayOfResults[i].objectUIDS.length==1) {
				output.write(data+", \""+savedQueryResult.arrayOfResults[i].objectUIDS [0]+"\"");
				log.write(0,lst.get(i) [0]+": Found");
				System.out.println(lst.get(i) [0]+": Found");
			}
		else {
			log.write(0,lst.get(i) [0]+": Not Found"); 
			System.out.println(lst.get(i) [0]+": Not Found"); 
			pp.write(data+", \"Not Found\"");
			}
			}
		}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace (new PrintWriter(errors)); 
			log.write(-1, errors.toString());
		}
	}
	
	public void getObject(List<String> lst){
		String[] uids=lst.toArray(new String[lst.size()]);
		ServiceData obj=dmService.loadObjects(uids);
		int k=0;
		
		for(int i=0;i<lst.size();i++) {
			try {
				if(k<obj.sizeOfPlainObjects() && obj.getPlainObject(k).getUid().equals(lst.get(i))){
					output.write(lst.get(i)+", \""+obj.getPlainObject(k++).getPropertyDisplayableValue("object_string")+"\"");
					log.write (0,lst.get(i)+": Found");
					System.out.println(lst.get(i)+": Found");
				}
				else {
					pp.write(lst.get(i)+", "+"Not Found"); 
					log.write(0,lst.get(i)+ ": Not Found");
					System.out.println(lst.get(i)+": Not Found");
				}
			}catch (Exception e) {
				pp.write(lst.get(i)+",\"Not Found\"");
				}
			}
	}
	
	public void getAttributeItem(List<String[]> input, String[] header, String[] attribute) {
		try {
			QueryInput [] savedQueryInput= new QueryInput [input.size()];
			for(int i=0;i<input.size();i++) {
				savedQueryInput[i]= new QueryInput();
				savedQueryInput[i].query =query;
				savedQueryInput[i].entries= new String [header.length]; 
				savedQueryInput [i].values =new String [header.length];
				savedQueryInput [i].maxNumToReturn = 25;
				for(int j=0;j<header.length;j++) {
					savedQueryInput[i].entries[j]=header[j];
					savedQueryInput [i].values[j]=input.get(i)[j];
				}
			}
				Map<String, String> map=new HashMap<>();
				SavedQueriesResponse savedQueryResult= queryService.executeSavedQueries(savedQueryInput);
				
				for(int i=0;i<input.size();i++) {
					String data=Arrays.stream(input.get(i)).map(e->"\""+e+"\"").collect (Collectors.joining(","));
					if (savedQueryResult.arrayOfResults[i].objectUIDS.length>1) {
							for(int j=0;j<savedQueryResult.arrayOfResults[i].objectUIDS.length;j++) { 
								map.put(savedQueryResult.arrayOfResults[i].objectUIDS[j], data);
							}
							log.write(0,input.get(i) [0]+": Found");
							System.out.println(input.get(i) [0]+": Found");
					}
					else {
						if (savedQueryResult.arrayOfResults[i].objectUIDS.length==1) {
								map.put(savedQueryResult.arrayOfResults[i].objectUIDS [0], data);
								log.write(0,input.get(i)[0]+": Found");
								System.out.println(input.get(i)[0]+": Found");
						}
						else {
							log.write(0,input.get(i) [0]+": Not Found");
							System.out.println(input.get(i)[0]+": Not Found");
							pp.write(data+", \"Not Found\"");
						}
					}
				}
				String[] uids=map.keySet().toArray(new String[map.size()]);
				ServiceData obj=dmService.loadObjects(uids);
				ModelObject[] mo= new ModelObject[obj.sizeOfPlainObjects()];
				for(int i=0;i<obj.sizeOfPlainObjects();i++) {
					mo[i]=obj.getPlainObject(i);
				}
				ServiceData data=dmService.getProperties (mo, attribute);
				for(int i=0;i<mo.length;i++) {
					ArrayList<String> lst=new ArrayList<>();
					for (String str:attribute) {
						if (data.getPlainObject(i).getPropertyDisplayableValues(str).size()>1) {
								lst.add(data.getPlainObject(i).getPropertyDisplayableValues(str).stream().collect (Collectors.joining(",")));
						}
						else lst.add(data.getPlainObject(i).getPropertyDisplayableValue(str));
					}
					String res=lst.stream().map(e->"\"" +e+"\"").collect(Collectors.joining (",")); 
					output.write(map.get(data.getPlainObject(i).getUid())+", "+res);
				}
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace (new PrintWriter(errors));
			log.write(-1, errors.toString());
		}
	}
	
	public void getAttributeUid(List<String> input, String[] attribute) {
			String[] uids= input.toArray(new String[input.size()]);
			try {
				ServiceData obj=dmService.loadObjects(uids);
				ModelObject[] mo= new ModelObject[obj.sizeOfPlainObjects()];
				int k=0;
				for(int i=0;i<input.size();i++) {
					if (k<obj.sizeOfPlainObjects()  && obj.getPlainObject(k).getUid().equals(input.get(i))) { 
						mo[k]=obj.getPlainObject(k);
						k++;
						log.write(0,input.get(i)+": Found");
						System.out.println(input.get(i)+": Found");
					}
					else {
						log.write(0,input.get(i)+": Not Found");
						System.out.println(input.get(i)+": Not Found");
						pp.write(input.get(i)+", \"Not Found\"");
					}
				}
				ServiceData data=dmService.getProperties (mo, attribute);
				for(int i=0;i<mo.length;i++) {
					ArrayList<String> lst=new ArrayList<>();
					lst.add(data.getPlainObject(i).getUid()); 
					for (String str:attribute) {
						if (data.getPlainObject(i).getPropertyDisplayableValues(str).size()>1) {
							lst.add(data.getPlainObject(i).getPropertyDisplayableValues(str).stream().collect(Collectors.joining(",")));
						}
						else lst.add(data.getPlainObject(i).getPropertyDisplayableValue(str));
					}
					String res=lst.stream().map(e->"\""+e+"\"").collect(Collectors.joining(","));
					output.write(res);
				}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace (new PrintWriter(errors)); 
					log.write(-1, errors.toString());
			}
	}

		
}
