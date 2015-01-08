package com.aman.seeker;

import com.aman.utils.Config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AutoStartBroadcastReciever extends BroadcastReceiver
{
	SharedPreferences pref;
	@Override
	public void onReceive(Context arg0, Intent arg1) 
	{	
		pref=arg0.getSharedPreferences(Config.PREF_KEY,Context.MODE_PRIVATE);
		if((pref.getBoolean("isuserlogin", false)))
		{
			if(pref.getBoolean("isalertenable", true))
			{
				arg0.startService(new Intent(arg0,NotificationService.class));
			}
		}
	}

}
