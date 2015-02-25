package com.aman.seeker;

import org.json.JSONObject;

import com.aman.database.DataBaseHelper;
import com.aman.utils.Config;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService
{
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	SharedPreferences pref;
	DataBaseHelper dbHelper;
	public GcmIntentService()
	{
		super("GcmIntentService");	
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		pref=getSharedPreferences(Config.PREF_KEY, Context.MODE_PRIVATE);
		dbHelper=new DataBaseHelper(getApplicationContext());
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		System.out.print(extras);

		System.out.print(messageType);

		if (!extras.isEmpty()) // has effect of unparcelling Bundle
		{
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
			{
				sendNotification("Send error: " + extras.toString(), null);
			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
			{
				sendNotification("Deleted messages on server: " + extras.toString(), null);
			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
			{
				// Post notification of received message.
				String message = ((intent.getExtras() == null) ? "Empty Bundle" : intent.getExtras().getString("message"));
				try 
				{
					JSONObject jObject=new JSONObject(message);

					if(jObject.getString("error").equals("0"))
					{
						if(jObject.getString("notification_type").equals("addPin"))
						{
							int response =dbHelper.insertPin(message);
							Log.e("Inserted ??",response==-1?"false":"true");
							String msg="Mr."+jObject.getJSONObject("user").getString("name")+" has pinned a location related to "+jObject.getJSONObject("pin").getString("cat_name");
							if(!jObject.getJSONObject("user").getString("id").equals(pref.getString("uid", "")))
							{
								sendNotification(msg, extras);
							}
						}
					}
				}
				catch (Exception e) 
				{
					sendNotification(message, extras);
					e.printStackTrace();
				}

				Log.i("GCMUNTENTSREIVCE", "Received: " + message);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	/**
	 * Put the message into a notification and post it.
	 * This is just one simple example of what you might choose to do with 
	 * a GCM message.
	 * @param msg
	 * @param extras
	 */
	private void sendNotification(String msg, Bundle extras)
	{
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(this, DashBoard.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// If this is a notification type message include the data from the message 
		// with the intent
		if (extras != null)
		{
			intent.putExtras(extras);
			intent.setAction("com.antoinecampbell.gcmdemo.NOTIFICATION");
		}
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("PinBurg")
		//.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
		.setContentText(msg)
		.setTicker(msg)
		.setAutoCancel(true)
		.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	/**
	 * Remove the app's notification
	 */
	@SuppressWarnings("unused")
	private void clearNotification()
	{
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(NOTIFICATION_ID);
	}

}
