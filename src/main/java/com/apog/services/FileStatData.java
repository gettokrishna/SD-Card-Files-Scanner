package com.apog.services;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.apog.model.FileStatModel;

/**
 * A helper class with static methods to readily process and return required data 
 * @author Krishna Chaitanya Pavuluri
 *
 */
public  class FileStatData {
	
	public static  final String scan = "Data_Service";
    private static String fullSetData;
	public static List<FileStatModel> fileStatList = new ArrayList<FileStatModel>();

	/**
	 * Method to facilitate list sorting and return Sorted List
	 * @return Sorted List
	 */
	public static List<FileStatModel> fileStatistics(){
		// sort the Array List File Size wise
		Collections.sort(fileStatList, FileStatModel.FileSizeComparator);
		return fileStatList;
    }
	
    /**
     * Method to facilitate Memory calculation in terms of equivalent units of KB,MB,GB and TB
     * @param File size in bytes
     * @return String representation of Equivalent Unit of memory
     */
	public static String memoryCalc(long fileSize){
		String mem = "0 KB";
		long GB_size = (1024*1024*1024*999L);
		if( fileSize < (1024*999)){
			float kb  = (float) Math.ceil(fileSize /1024);
			mem = new DecimalFormat("##.##").format(kb).toString()+ " KB";
		}else if(fileSize < (1024*1024*999)){
			float mb  = (int) Math.ceil(fileSize /(1024*1024));
			mem = new DecimalFormat("##.##").format(mb).toString()+ " MB";
		}else if(fileSize < GB_size){
			double gb  = (float) Math.ceil(fileSize /(1024*1024*1024));
			mem = new DecimalFormat("##.##").format(gb).toString()+ " GB";
		}else{
			double tb  = (float) Math.ceil(fileSize /(1024*1024*1024*1024));
			mem = new DecimalFormat("##.##").format(tb).toString()+ " TB";
		}
		
		return mem;
	}
	
	/**
	 * Method to quickly calculate Average File size from the List of files read from Memory
	 * @return String representation of Equivalent Unit of memory
	 */
	public static String avgFileSize(){
		
		long fileMem = 0l;
		for(FileStatModel fm : fileStatList){
			 fileMem += fm.getSize();
		 }		
		long avgMem = fileMem / fileStatList.size();
		
		return memoryCalc(avgMem);
	}
	
	/**
	 * Method to Group the Files with same extensions and calculate their frequencies.
	 * @return Sorted List of File data with Key value pair. Key being File Extension and Value as List of Grouped files
	 */
	public static  List<Entry<String,ArrayList<FileStatModel>>> freqFileExtns(){
		Map<String,ArrayList<FileStatModel>> freqFiles = new HashMap<String,ArrayList<FileStatModel>>();
		 for(FileStatModel fm : fileStatList){
			 String key = fm.getExtn();
			if( freqFiles.containsKey(key)){
				freqFiles.get(key).add(fm);
			}else{
				ArrayList<FileStatModel> fileList = new  ArrayList<FileStatModel>();
				fileList.add(fm);
				freqFiles.put(key,fileList);
			}
		 }
         Set<Entry<String,ArrayList<FileStatModel>>> setMap = freqFiles.entrySet();
         List<Entry<String,ArrayList<FileStatModel>>> list = new ArrayList<Entry<String,ArrayList<FileStatModel>>>(setMap);
         Collections.sort(list, FreqComparator);
                 
         return list;
	}
	
    
	private static final Comparator<Entry<String, ArrayList<FileStatModel>>> FreqComparator = new Comparator<Map.Entry<String,ArrayList<FileStatModel>>>(){

		@Override
		public int compare(Entry<String, ArrayList<FileStatModel>> arg0,
				Entry<String, ArrayList<FileStatModel>> arg1) {
			long size1 = arg0.getValue().size();
			long size2 = arg1.getValue().size();
			return (size2<size1? -1 : (size2 ==size1? 0 : 1));
		}
	};
	
	public static String getFullSetData() {
		return fullSetData;
	}


	public static void setFullSetData(String data) {
		fullSetData = data;
	}
	
 }
