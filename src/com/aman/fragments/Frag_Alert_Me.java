package com.aman.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import asynctasks.CatagoryTask;
import asynctasks.GetNotifyInfoTask;
import asynctasks.UpdateNotificationServiceTask;
import com.aman.adapter.CategoryAdapter;
import com.aman.seeker.DashBoard;
import com.aman.seeker.NotificationService;
import com.aman.seeker.R;
import com.aman.utils.Config;

public class Frag_Alert_Me extends Fragment implements OnClickListener
{
	SeekBar sb;
	Switch sw;
	TextView tv_km,tv_select_all;
	ListView lv_category;
	ArrayList<HashMap<String, String>> listCatagory_old=new ArrayList<>();
	CatagoryTask task;
	Button btn_save;
	SharedPreferences pref;
	CategoryAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) 
	{	
		View v=inflater.inflate(R.layout.fragment_alert_me, container, false);
		
		pref=getActivity().getSharedPreferences(Config.PREF_KEY, Context.MODE_PRIVATE);
		initComponents(v);		
		setFromPrefrence();
		sb.setMax(15);		
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{		
			@Override
			public void onStopTrackingTouch(SeekBar arg0) 
			{
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) 
			{

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) 
			{				
				if(arg1==0)
				{
					sw.setChecked(false);					
				}	
				else
				{
					sw.setChecked(true);
				}
				tv_km.setText(String.valueOf(arg1)+" Km");
			}
		});		
		sw.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{		
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
			{
				if(arg1)
				{
					//((DashBoard)getActivity()).notificationService.registerLocationService();
					getActivity().startService(((DashBoard)getActivity()).notificationIntent);
				}
				else
				{
					//((DashBoard)getActivity()).notificationService.stopLocationService();
					NotificationService.isFromApp=true;
					getActivity().stopService(((DashBoard)getActivity()).notificationIntent);					
				}
			}
		});
		getCatagory(getActivity());
	
		return v;
	}
	
	private void setFromPrefrence()
	{
		sw.setChecked(pref.getString("is_notify_service_enable", "1").equals("1")?true:false);
		sb.setProgress(Integer.parseInt(pref.getString("range", "")));
		tv_km.setText(pref.getString("range","")+" Km");	
	}

	private void setToPrefrence() 
	{
		
	}
	private void initComponents(View v)
	{
		sb=(SeekBar)v.findViewById(R.id.sb_alert_me_range_setter);
		sb.setProgress(Integer.parseInt(pref.getString("reange", "15")));
		sw=(Switch)v.findViewById(R.id.sw_alert_me_switch);
		sw.setChecked(pref.getString("is_notify_service_enable", "1").equals("1")?true:false);
		tv_km=(TextView)v.findViewById(R.id.tv_alert_me_km);
		tv_select_all=(TextView)v.findViewById(R.id.tv_alert_me_select_all);
		tv_select_all.setOnClickListener(this);
		btn_save=(Button)v.findViewById(R.id.btn_alert_me_save);
		btn_save.setOnClickListener(this);
		lv_category=(ListView)v.findViewById(R.id.lv_alert_me_category);
		lv_category.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv_category.setOnItemClickListener(onItemClickListener);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{		
		case R.id.tv_alert_me_select_all:

			break;
		case R.id.btn_alert_me_save:
			
			StringBuilder category = new StringBuilder();
			for(int i=0; i < adapter.getCount();i++)
			{
				HashMap<String, String> map=(HashMap<String, String>) adapter.getItem(i);
				if(map.get("isSelected").equals("1"))
				{
					category.append(map.get("id")+",");
				}
			}				
			if(category!=null)
			{
				String temp=category.toString();
				if(temp.endsWith(","))
				{
					temp=temp.substring(0, temp.length()-1);
				}
				getNotificationServiceTask().execute(pref.getString("uid", ""),String.valueOf(sb.getProgress()),sw.isChecked()?"1":"0",temp);	
			}			
			
			break;
		}

	}

	private void getCatagory(final Context con)	
	{
		GetNotifyInfoTask task=new GetNotifyInfoTask(con, new GetNotifyInfoTask.onTaskCompleteListener()
		{			
			@Override
			public void ontaskComplete(ArrayList<HashMap<String, String>> listCatagory,int range, boolean isnotifyenable) 
			{
				if(listCatagory!=null)
				{
					listCatagory_old.addAll(listCatagory);
					adapter=new CategoryAdapter(getActivity(), listCatagory);
					lv_category.setAdapter(adapter);
					sw.setChecked(isnotifyenable);					
					sb.setProgress(range);
				}
				else
				{
					
				}
			}
		});
		task.execute(pref.getString("uid", ""));
	}	
	OnItemClickListener onItemClickListener=new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
		{
			HashMap<String, String> map=(HashMap<String, String>) adapter.getItem(arg2);
			if(map.get("isSelected").equals("1"))
			{
				lv_category.setItemChecked(arg2, false);
				map.put("isSelected", "0");
			}
			else
			{
				lv_category.setItemChecked(arg2,true);
				map.put("isSelected", "1");
			}
			adapter.notifyDataSetChanged();
		}
		
	};
	
	private UpdateNotificationServiceTask getNotificationServiceTask()
	{
		UpdateNotificationServiceTask task=new UpdateNotificationServiceTask(getActivity(), new UpdateNotificationServiceTask.onTaskCompleteListener()
		{
			
			@Override
			public void ontaskComplete(String response) 
			{
				try 
				{
					JSONObject object=new JSONObject(response);
					if(object.getString("success").equals("1"))
					{
						pref.edit().putString("range",String.valueOf(sb.getProgress()))
						.putString("is_notify_service_enable",sw.isChecked()?"1":"0").commit();
						Intent intent=new Intent(NotificationService.ACTION);
						intent.putExtra("isFromSelf", "true");
						getActivity().sendBroadcast(intent);
						if(sw.isChecked())
						getActivity().startService(((DashBoard)getActivity()).notificationIntent);
					}
					else
					{						
						getCatagory(getActivity());
					}
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
			}
		});
		return task;
	}
}
