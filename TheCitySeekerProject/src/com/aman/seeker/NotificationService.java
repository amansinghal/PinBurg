package com.aman.seeker;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.aman.utils.Config;

public class NotificationService extends Service implements LocationListener
{
	IBinder myBinder=new NotiBinder();
	LocationManager locationManager=null;
	SharedPreferences pref;
	public static String ACTION="com.aman.seeker.STOP";
	int count=1;
	public static boolean isFromApp=false;
	//StopServiceReciever stopServiceReciever;
	@Override
	public IBinder onBind(Intent arg0)
	{		
		return myBinder;
	}

	@Override
	public void onCreate() 
	{		
		super.onCreate();	
		pref=getSharedPreferences(Config.PREF_KEY,MODE_PRIVATE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{				
		{
			registerLocationService();
			//stopServiceReciever=new StopServiceReciever();
			//registerReceiver(stopServiceReciever, new IntentFilter(ACTION));
		}		
		return START_STICKY;
	}
	public class NotiBinder extends Binder
	{
		NotificationService getService()
		{
			return NotificationService.this;
		}
	}

	public void registerLocationService() 
	{
		locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,Integer.parseInt(pref.getString("range","1"))*1000, this);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,Integer.parseInt(pref.getString("range","1"))*1000, this);

	}

	public void stopLocationService()
	{
		if(locationManager!=null)
		{
			locationManager.removeUpdates(this);			
		}
	}


	@SuppressLint("NewApi")
	private void sendNotification(String msg, Bundle extras)
	{
		NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(this, DashBoard.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("fromNotification", "true");
		// If this is a notification type message include the data from the message 
		// with the intent
		if (extras != null)
		{
			intent.putExtras(extras);
			intent.setAction("");
		}
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("PinBurg")
		.setContentText(msg)
		.setTicker(msg)
		.setAutoCancel(true)		
		.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
	
		
	/*	Notification noti = new Notification.Builder(this)
		.setStyle(new Notification.InboxStyle()
		.addLine("Soandso likes your post")
		.addLine("Soandso reblogged your post"))		
		.setContentTitle("PinBurg")
		.setAutoCancel(true)		
		.addAction(android.R.drawable.ic_menu_info_details,"view", contentIntent)
		.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
		.build();*/		
		mBuilder.setContentIntent(contentIntent);	
		mNotificationManager.notify(1, mBuilder.build());
	}
	
	
	
	@Override
	public void onDestroy() 
	{			
		super.onDestroy();		
		if(!isFromApp)
		{
			Intent dialogIntent = new Intent(getBaseContext(), DialogActivityServiceRestart.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplication().startActivity(dialogIntent);
			startActivity(dialogIntent);		
		}
		stopLocationService();
	}
	
/*	class StopServiceReciever extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			if(arg1.getAction().equalsIgnoreCase(ACTION))
			{
				stopLocationService();
				if(!arg1.hasExtra("isFromSelf"))
				{
					Intent dialogIntent = new Intent(getBaseContext(), DialogActivityServiceRestart.class);
					dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getApplication().startActivity(dialogIntent);
					arg0.startActivity(dialogIntent);					
				}
				else
				{
					stopSelf();					
				}		
				try
				{
				unregisterReceiver(this);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}						
		}
		
	}
*/
	@Override
	public void onLocationChanged(Location location) 
	{
		sendNotification(count+" times",null);
		count++;
	}

	@Override
	public void onProviderDisabled(String provider) 
	{
		
	}

	@Override
	public void onProviderEnabled(String provider) 
	{
		
	}

	@Override
	public void onStatusChanged(String provider, int pStatus, Bundle pExtras) 
	{

	}
	
}
