package com.aman.fragments;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import asynctasks.LoginTask;
import asynctasks.LoginTask.onTaskCompleteListener;

import com.aman.seeker.Activity_Dashboard;
import com.aman.seeker.DashBoard;
import com.aman.seeker.R;
import com.aman.utils.Config;

public class Frag_Login extends Fragment
{
	Button btn_login,btn_go_back;
	EditText et_email,et_password;
	CheckBox cb_rememberme;
	SharedPreferences pref;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View view=inflater.inflate(R.layout.frag_login_page, container, false);
		pref=getActivity().getSharedPreferences(Config.PREF_KEY, Context.MODE_PRIVATE);
		btn_login=(Button)view.findViewById(R.id.btn_login);
		cb_rememberme=(CheckBox)view.findViewById(R.id.cb_login_remember_me);
		btn_go_back=(Button)view.findViewById(R.id.btn_go_back);
		et_email=(EditText)view.findViewById(R.id.et_login_email);
		et_email.setText(pref.getString("email", ""));
		et_password=(EditText)view.findViewById(R.id.et_login_password);
		btn_login.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View arg0) 
			{
				//getActivity().startActivity(new Intent(getActivity(),DashBoard.class));
				LoginTask task=new LoginTask(getActivity(),new onTaskCompleteListener() 
				{
					@Override
					public void ontaskComplete(String response) 
					{
						// TODO Auto-generated method stub
						//Log.e("Response", response);
						try 
						{
							JSONObject jObject=new JSONObject(response);	
							if(jObject.getString("error").equals("0"))
							{
 								pref.edit().putString("uid",jObject.getJSONObject("user").getString("uid"))
								.putString("name",jObject.getJSONObject("user").getString("name"))
								.putString("email",jObject.getJSONObject("user").getString("email"))
								.putString("share_count",jObject.getJSONObject("user").getString("share_count"))
								.putString("like_count",jObject.getJSONObject("user").getString("like_count"))
								.putString("comment_count",jObject.getJSONObject("user").getString("comment_count"))
								.putString("pin_count",jObject.getJSONObject("user").getString("pin_count"))
								.putString("country",jObject.getJSONObject("user").getJSONObject("country").toString())
								.putString("state",jObject.getJSONObject("user").getJSONObject("state").toString())
								.putString("city",jObject.getJSONObject("user").getJSONObject("city").toString())
								.putString("pin_count",jObject.getJSONObject("user").getString("pin_count"))
								.putBoolean("isuserlogin",true).putBoolean("rememberme",cb_rememberme.isChecked())
								.putString("range", jObject.getJSONObject("user").getString("range"))
 								.putString("is_notify_service_enable", jObject.getJSONObject("user").getString("is_notify_service_enable"))
								.putString("photo_path","http://www.pinburg.com"+jObject.getJSONObject("user").getString("photo_path"))
								.commit();
								startActivity(new Intent(getActivity(),Activity_Dashboard.class));
								getActivity().finish();
							}
							else
							{
								Config.alertDialogBox(getActivity(),(ActionBarActivity)getActivity(), null,jObject.getString("error_msg"),true);
							}
						}
						catch(Exception e) 
						{
							e.printStackTrace();
						}
						
					}
				});
				task.execute(Config.getText(et_email),Config.getText(et_password));
			}
		});
		
		btn_go_back.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View arg0) 
			{
				getFragmentManager().popBackStack();				
			}
		});
		return view;
	}
	
}
