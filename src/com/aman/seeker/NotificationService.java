package com.aman.seeker;

import com.aman.utils.Config;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationService extends Service
{
	IBinder myBinder=new NotiBinder();
	LocationManager locationManager;
	SharedPreferences pref;
	int count=1;
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
		registerLocationService();
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
		
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) 
		{		   
			//locationListener.onLocationChanged(location);
		}
		//else
		{
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,1000, locationListener);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1000, locationListener);
		}
		
	}
	
	public LocationListener locationListener=new LocationListener()
	{	
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) 
		{			
			Log.e("status",""+status);
		}
		
		@Override
		public void onProviderEnabled(String provider) 
		{
			
		}
		
		@Override
		public void onProviderDisabled(String provider) 
		{
			
		}
		
		@Override
		public void onLocationChanged(Location location) 
		{			
			sendNotification(count+" times",null);
			count++;
		}
	};
	
	private void sendNotification(String msg, Bundle extras)
	{
		NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(this, DashBoard.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(1, mBuilder.build());
	}

}
