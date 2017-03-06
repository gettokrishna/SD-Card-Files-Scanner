package com.apog.scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import com.apog.model.FileStatModel;
import com.apog.services.FileStatData;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Fragment that helps in attaching data to the base view , to facilitate Dynamic view addition to base view and display the full set of
 * File statistics data 
 * @author Krishna Chaitanya Pavuluri
 */

public class ScanStatsFragment extends Fragment {
	
    public static String actionTitle;
    public static final String scan = "Scan_Stats";
    private String eol = System.getProperty("line.separator");
    private String fullData;
    private StringBuffer data = new StringBuffer();
	public ScanStatsFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

	   View rootView = inflater.inflate(R.layout.fragment_scan_stats, container,false);
	   
	   
	   if(FileStatData.fileStatList.size() > 0){
		   
		   LinearLayout topTenLayout = (LinearLayout) rootView.findViewById(R.id.top_ten_Layout);
		   LinearLayout freqFiveLayout = (LinearLayout) rootView.findViewById(R.id.freq_five_Layout);
		   
		   data.append(getResources().getString(R.string.stats_label));
		   data.append(eol).append(eol);
		   data.append(getResources().getString(R.string.top_files_label)).append(eol);

		   List<FileStatModel> fileSizeStats = FileStatData.fileStatistics();
		   
		   int topSizeFileCount = Integer.parseInt(getResources().getString(R.string.no_of_topSizeFiles));
		   int i = 0;
		   for(FileStatModel fm : fileSizeStats){
			      View dataView = inflater.inflate(R.layout.fragment_data, container,false);
			      TextView tv1 = (TextView) dataView.findViewById(R.id.dataText1);
			      TextView tv2= (TextView) dataView.findViewById(R.id.dataText2);
			      tv1.setText(fm.getFileName());		  
			      tv2.setText(fm.getDisplaySize());
			      String fileStat = fm.getFileName() + " -- " +fm.getDisplaySize();
			      data.append(fileStat).append(eol);
			      topTenLayout.addView(dataView,i);
			      i++;
			      if(i==topSizeFileCount) break;
			}
		   
		   data.append(eol);
		   TextView topTen = (TextView) rootView.findViewById(R.id.average_file_size);
		   String avarageFileSize = "Average File Size : "+FileStatData.avgFileSize();
		   topTen.setText(avarageFileSize);
		   data.append(avarageFileSize).append(eol).append(eol);
		   Log.d(scan,"File Frequency");
		   List<Entry<String,ArrayList<FileStatModel>>> list = FileStatData.freqFileExtns();
	       data.append(getResources().getString(R.string.freq_files_label)).append(eol).append(eol);
		   int freqFilesCount = Integer.parseInt(getResources().getString(R.string.no_of_freqFiles));
		   int j = 0;
		   for(Entry<String,ArrayList<FileStatModel>> entry : list){
			      View dataView = inflater.inflate(R.layout.fragment_data, container,false);
			      TextView tv1 = (TextView) dataView.findViewById(R.id.dataText1);
			      TextView tv2= (TextView) dataView.findViewById(R.id.dataText2);
			      tv1.setText(entry.getKey());		  
			      tv2.setText(Integer.toString(entry.getValue().size()));
			      String freqFiles = "Extension : "+entry.getKey()+" and Frequency : "+entry.getValue().size();
			      data.append(freqFiles).append(eol);
			      freqFiveLayout.addView(dataView,j);
			      j++;
			      if(j==freqFilesCount) break;
			}
		   fullData = data.toString();
		   FileStatData.setFullSetData(fullData);
   
	   }
	   else{
		     TextView tv = (TextView)rootView.findViewById(R.id.error_msg);
		     tv.setText(R.string.scanErrMsg1);
		     tv.setVisibility(View.VISIBLE);
		     rootView.findViewById(R.id.file_stats_label).setVisibility(View.INVISIBLE);
		     rootView.findViewById(R.id.top_files_label).setVisibility(View.INVISIBLE);
		     rootView.findViewById(R.id.top_ten_Layout).setVisibility(View.INVISIBLE);
		     rootView.findViewById(R.id.average_file_size).setVisibility(View.INVISIBLE);
		     rootView.findViewById(R.id.freq_label).setVisibility(View.INVISIBLE);
		     rootView.findViewById(R.id.freq_five_Layout).setVisibility(View.INVISIBLE);
		     data.append(getResources().getString(R.string.scanErrMsg1));
		     fullData = data.toString();
			 FileStatData.setFullSetData(fullData);
	   }
		   return rootView;
	}
	
	@Override
    public void onAttach(Activity activity) {
    	// TODO Auto-generated method stub
    	super.onAttach(activity);
    	actionTitle = getResources().getString(R.string.stats_action_title);
		activity.getActionBar().setTitle(actionTitle);
    }	
	
}
