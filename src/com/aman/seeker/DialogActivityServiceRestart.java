package com.aman.seeker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogActivityServiceRestart extends Activity implements OnClickListener
{
	Button btn_restart,btn_cancel;
	TextView tv_message;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_activity_restart_service);		
		initComponents();
		tv_message.setText("The near by notification service has been disabled so now onwards you are not able to get your near by inforamtion.\n Do you want to start it again?");
	}
	
	private void initComponents()
	{
		btn_restart=(Button)findViewById(R.id.dialog_activity_btn_restart);
		btn_restart.setOnClickListener(this);
		btn_cancel=(Button)findViewById(R.id.dialog_activity_btn_cancel);
		btn_cancel.setOnClickListener(this);
		tv_message=(TextView)findViewById(R.id.dialog_activity_tv_message);
	}

	@Override
	public void onClick(View v) 
	{
			if(v.getId()==R.id.dialog_activity_btn_restart)
			{
				startNotificationService();
				this.finish();
			}
			if(v.getId()==R.id.dialog_activity_btn_cancel)
			{
				this.finish();
			}
	}
	public void startNotificationService() 
	{
		Intent notificationIntent=new Intent(this,NotificationService.class);	

		startService(notificationIntent);
	}	
}
