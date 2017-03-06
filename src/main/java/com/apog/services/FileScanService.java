package com.apog.services;

import com.apog.scanner.MainActivity;
import com.apog.scanner.R;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

/**
 * Service to trigger Local Notification to represent the File Scan while the scanning activity is progressing at the background 
 * @author Krishna Chaitanya Pavuluri
 */
public class FileScanService extends IntentService{
	
	public static final String scan = "Scan_Notify";
	public FileScanService(){
		super("FileScanService");
	}

	NotificationManager notificationManager;
	
	public FileScanService(String name) {
		super(name);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	protected void onHandleIntent(Intent intent123) {
		
 		Log.d(scan, "Into On Handle Intent of Service");

 		Intent intent = new Intent(this, MainActivity.class);
 		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

     	PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

 	 	Notification notify  = new Notification.Builder(this)
 	        .setContentTitle("External Memory Scanning")
 	        .setContentText("Your SD Card slot is under scan")
 	        .setSmallIcon(R.drawable.ic_launcher)
 	        .setContentIntent(pIntent)
 	        .setAutoCancel(true)
 	        .build();
 	    
 	  
 	 notificationManager = 
 	  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

 	notificationManager.notify(0, notify);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
}

