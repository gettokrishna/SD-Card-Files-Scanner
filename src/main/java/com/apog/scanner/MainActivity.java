package com.apog.scanner;

import com.apog.scanner.StartScanFragment.Task;
import com.apog.services.FileStatData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Main Activity Class to serve as base container for all other Fragments across the application.
 * Implements DataExchange interface to facilitate data communication between Activity and StartScanFragment
 * @author Krishna Chaitanya Pavuluri
 */

public class MainActivity extends Activity implements StartScanFragment.DataExchange{

	
public final static String  scan= "Main_Activity";
private StartScanFragment.Task progressTask = null;
private Intent progServIntent = null;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
				getFragmentManager().beginTransaction()
				.add(R.id.container, new StartScanFragment()).addToBackStack(null).commit();
		}
				
	}
    
   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
		public boolean onMenuOpened(int featureId, Menu menu) {
			// TODO Auto-generated method stub
		    if(getActionBar().getTitle().equals(StartScanFragment.actionTitle))
                closeOptionsMenu();    		   		    
			return super.onMenuOpened(featureId, menu);
		}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.export) {
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			String data =FileStatData.getFullSetData();
			sendIntent.putExtra(Intent.EXTRA_TEXT, data);
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
    public void onBackPressed() {
    		// TODO Auto-generated method stub
    		super.onBackPressed();
    		if(getActionBar().getTitle().equals(StartScanFragment.actionTitle)){
                 if(progServIntent !=null)
         		 stopService(progServIntent);
         		if(progressTask != null)
         		 progressTask.cancel(true);
         		finish();
    		}else{
    		    getFragmentManager().beginTransaction().replace(R.id.container,new StartScanFragment()).addToBackStack(null).commit();	
    		}
    }

	@Override
	public void updateTaskObj(Task t) {
		// TODO Auto-generated method stub
		progressTask = t;
	}

	@Override
	public void updateIntentObj(Intent i) {
		// TODO Auto-generated method stub
		progServIntent = i;
	}     
	
    @Override
    protected void onDestroy() {
    		// TODO Auto-generated method stub
    		super.onDestroy();
    }
}
