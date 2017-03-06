package com.apog.scanner;

import java.io.File;
import java.util.List;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.apog.model.FileStatModel;
import com.apog.services.FileScanService;
import com.apog.services.FileStatData;

/**
 * Fragment to hold the Start Scan Button and to show the Progress of File Scanning. 
 * Also facilitates the core logic of file scanning and Local Notification building.
 * @author Krishna Chaitanya Pavuluri
 */

public class StartScanFragment extends Fragment{

	public final static String  scan= "Start_Scan";
	public static String actionTitle = null;
  
    private  Intent intent = null;
    private  Task fileScanner =  null; 
    private static ProgressBar pb = null;
    private LinearLayout lm2 = null;
    
	public StartScanFragment() {
	}
    
	public interface DataExchange{
		
		public void updateTaskObj(Task t);
		
		public void updateIntentObj(Intent i);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = null;
		
        rootView= inflater.inflate(R.layout.fragment_main, container,false);
		
		lm2 = (LinearLayout) rootView.findViewById(R.id.baseLauout2);
        lm2.setVisibility(View.GONE);
    	pb = (ProgressBar) rootView.findViewById(R.id.progress);

		ImageButton btnScanNow = (ImageButton)rootView.findViewById(R.id.button1);
		
		btnScanNow.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!(lm2.getVisibility() == View.VISIBLE)){
					intent = new Intent(getActivity(),FileScanService.class);
					getActivity().startService(intent);
					fileScanner = new Task();
					fileScanner.execute();
			        lm2.setVisibility(View.VISIBLE);
				}
				                					 
			 }
		 });
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		actionTitle = getResources().getString(R.string.scan_action_title);
		activity.getActionBar().setTitle(actionTitle);

	}
	
	public class Task extends AsyncTask<String, Integer, String>{

		public List<FileStatModel> fileList = FileStatData.fileStatList;

		@Override
		protected String doInBackground(String... params) {
			
			  File mainDir = new File("/storage");
			  
			  File[] list = mainDir.listFiles();
			  String externalSD = "";
			  for(File f : list){
				  String fname = f.getName();
				  Log.d(scan,"File Name : "+fname);
				  if(fname.toLowerCase().contains("sd")){
					  if(!fname.equalsIgnoreCase("sdcard0")){
						  externalSD = fname;
						  Log.d(scan,"External SD Card Folder : "+fname);
					  }
			     }
			  }
			  
			  String mainDirAbsPath = mainDir.getAbsolutePath()+"/"+externalSD;
			  Log.d(scan,"External Scan folder path : "+mainDirAbsPath);
			  
			  File sdCardDir = new File(mainDirAbsPath);
			  
			  Log.d(scan, "Sdcard Dir Name : "+sdCardDir.getName());
			  fileList.clear();
	       
			  dirFilesScanner(sdCardDir,true);
			  if(FileStatData.fileStatList.size()>0){
			     FileStatData.fileStatistics();
			     Log.d(scan, "Average file Size : "+FileStatData.avgFileSize());
			     FileStatData.freqFileExtns();
			  }
			  else{
				  getFragmentManager().beginTransaction()
					.replace(R.id.container, new ScanStatsFragment()).addToBackStack(null).commit();
			  }
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			// ProgressBar view status update
			int progressValue = values[0];
			progressValue++;
			pb.setProgress(progressValue);
  			if(progressValue >= pb.getMax()) {
  				getFragmentManager().beginTransaction()
				.replace(R.id.container, new ScanStatsFragment()).addToBackStack(null).commit();
  			}
			Log.d(scan,"Progress bar updated "+values[0]);
		}
		
		
		private void dirFilesScanner(File directory,boolean baseDirs){
			  File[] files =  directory.listFiles();
			  if(files != null){
				  int filesCount = files.length;
		          if(baseDirs){
		        	  if(filesCount > 0){
		        		  pb.setMax(filesCount);
		        	  }else{
		        		  pb.setMax(1);
		        		  pb.setProgress(0);
		        		  publishProgress(0);
		        	  }
		          }
			     for(int i=0;i<filesCount;i++){
				  	  if(files[i].isDirectory()){
						  dirFilesScanner(files[i],false);
					  }else{
						  String fileName = files[i].getName();
						  String[] fileParts   = fileName.split("\\.");
						  String  extn  = fileParts[fileParts.length - 1];
						
						  FileStatModel fm = new FileStatModel();
						  fm.setFileName(fileName);
						  fm.setExtn(extn);
						  fm.setLastModified(files[i].lastModified());
						  fm.setSize(files[i].length());
						  fm.setDisplaySize(FileStatData.memoryCalc(fm.getSize()));
						  
						  fileList.add(fm);
					  }
				  	  if(baseDirs){
				  		  publishProgress(i);
				  	  }
				  }  
			   }else{
			     String msg = getResources().getString(R.string.scanErrMsg1);
			     Log.d(scan,"Error Msg : "+msg);
			   }
		}		
		
	}
	
}

