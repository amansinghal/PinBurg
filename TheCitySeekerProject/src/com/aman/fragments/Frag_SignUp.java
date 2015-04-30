package com.aman.fragments;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import asynctasks.CityTask;
import asynctasks.CountryTask;
import asynctasks.CountryTask.onTaskCompleteListener;
import asynctasks.SignUpTask;
import asynctasks.StateTask;

import com.aman.ModelClasses.CountryStateCity;
import com.aman.seeker.DashBoard;
import com.aman.seeker.R;
import com.aman.utils.Config;
import com.aman.utils.Config.WhatToClose;

public class Frag_SignUp extends Fragment
{
	EditText et_name,et_email,et_password,et_dob,et_addressline;
	Spinner spnr_country,spnr_state,spnr_city;
	ArrayList<CountryStateCity> countryList,stateList,cityList;
	Button btn_clear,btn_signup;
	Calendar calendar=Calendar.getInstance();
	String CountryId="",StateId="",CityId="";
	SharedPreferences pref;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.frag_signup, container, false);
		pref=getActivity().getSharedPreferences(Config.PREF_KEY, Context.MODE_PRIVATE);
		et_name=(EditText)view.findViewById(R.id.et_signup_name);
		et_email=(EditText)view.findViewById(R.id.et_signup_email);
		et_password=(EditText)view.findViewById(R.id.et_signup_password);
		et_dob=(EditText)view.findViewById(R.id.et_signup_dob);
		et_addressline=(EditText)view.findViewById(R.id.et_signup_addressline);
		btn_signup=(Button)view.findViewById(R.id.btn_signup_signup);
		btn_clear=(Button)view.findViewById(R.id.btn_signup_clear);
		spnr_city=(Spinner)view.findViewById(R.id.spnr_city);
		spnr_country=(Spinner)view.findViewById(R.id.spnr_country);
		spnr_state=(Spinner)view.findViewById(R.id.spnr_state);
		CountryTask task=new CountryTask(getActivity(), new onTaskCompleteListener()
		{			
			@Override
			public void ontaskComplete(ArrayList<CountryStateCity> countrylist) 
			{
				// TODO Auto-generated method stub
				countryList=countrylist;
				ArrayList<String> tempList=new ArrayList<>();
				for(int i=0;i<countrylist.size();i++)
				{
					tempList.add(countrylist.get(i).Name);
				}
				if(tempList.size()>0)
				{
					spnr_country.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.simple_text_1, tempList));
				}
				else
				{
					Config.alertDialogBox(getActivity(), (ActionBarActivity)getActivity(),WhatToClose.Fragment, "Country not found please signup later.",false);
				}
			}
		});

		spnr_country.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				CountryId=countryList.get(arg2).Id;
				StateId="";
				CityId="";
				StateTask stateTask=new StateTask(getActivity(),new StateTask.onTaskCompleteListener()
				{			
					@Override
					public void ontaskComplete(ArrayList<CountryStateCity> statelist) 
					{
						stateList=statelist;

						ArrayList<String> tempList=new ArrayList<>();
						for(int i=0;i<statelist.size();i++)
						{
							tempList.add(statelist.get(i).Name);
						}
						if(tempList.size()>0)
						{
							spnr_state.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.simple_text_1, tempList));
						}
						else
						{
							spnr_state.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.simple_text_1, tempList));
							Config.alertDialogBox(getActivity(), (ActionBarActivity)getActivity(),null, "State not found.",false);
						}
					}
				});
				stateTask.execute(countryList.get(spnr_country.getSelectedItemPosition()).Id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{

			}

		});
		spnr_state.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,final int arg2, long arg3) 
			{
				StateId=stateList.get(arg2).Id;
				CityId="";
				CityTask citytask=new CityTask(getActivity(), new CityTask.onTaskCompleteListener()
				{

					@Override
					public void ontaskComplete(ArrayList<CountryStateCity> citylist) 
					{
						// TODO Auto-generated method stub
						cityList=citylist;

						ArrayList<String> tempList=new ArrayList<>();
						for(int i=0;i<citylist.size();i++)
						{
							tempList.add(citylist.get(i).Name);
						}
						if(tempList.size()>0)
						{
							spnr_city.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.simple_text_1, tempList));
						}
						else
						{
							spnr_city.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.simple_text_1, tempList));
							Config.alertDialogBox(getActivity(), (ActionBarActivity)getActivity(),null, "City not found.",false);
						}
					}
				});
				citytask.execute(stateList.get(spnr_state.getSelectedItemPosition()).Id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				// TODO Auto-generated method stub

			}

		});
		spnr_city.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				// TODO Auto-generated method stub
				CityId=cityList.get(arg2).Id;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub

			}
		});
		task.execute();		
		final DatePickerDialog dialog=new DatePickerDialog(getActivity(),new OnDateSetListener()
		{

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) 
			{
				// TODO Auto-generated method stub
				et_dob.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
		et_dob.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				dialog.show();
			}
		});
		btn_clear.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub

			}
		});
		btn_signup.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				SignUpTask signUpTask=new SignUpTask(getActivity(), new SignUpTask.onTaskCompleteListener()
				{					
					@Override					
					public void ontaskComplete(String response) 
					{
						// TODO Auto-generated method stub
						try 
						{
							Log.e("Response", response);
							if(!response.isEmpty())
							{
								JSONObject jsonObject=new JSONObject(response);
								if(jsonObject.getString("error").equals("0"))
								{
									pref.edit().putString("uid",jsonObject.getString("uid"))
									.putString("name",jsonObject.getJSONObject("user").getString("name"))
									.putString("email",jsonObject.getJSONObject("user").getString("email"))
									.putString("photo_path",jsonObject.getJSONObject("user").getString("photo_path"))
									.putBoolean("isuserlogin", true)
									.commit();
									startActivity(new Intent(getActivity(),DashBoard.class));
									getActivity().finish();
								}
								if(jsonObject.getString("error").equals("1"))
								{
									Config.alertDialogBox(getActivity(), (ActionBarActivity)getActivity(),null, "Server error try again.",false);
								}
								if(jsonObject.getString("error").equals("2"))
								{
									Config.alertDialogBox(getActivity(), (ActionBarActivity)getActivity(),null, "Email already registered sign up with a diffrent email.",false);
								}
							}
							else
							{
								Config.alertDialogBox(getActivity(), (ActionBarActivity)getActivity(),null, "No response from server try again later.",false);
							}
						}
						catch (Exception e) 
						{
							e.printStackTrace();
						}


					}
				});
				if(!Config.getText(et_name).isEmpty())
				{
					if(!Config.getText(et_email).isEmpty())
					{
						if(!Config.getText(et_password).isEmpty())
						{
							if(!Config.getText(et_dob).isEmpty())
							{
								if(!CountryId.isEmpty())
								{
									if(!StateId.isEmpty())
									{
										if(!CityId.isEmpty())
										{
											if(!Config.getText(et_addressline).isEmpty())
											{
												signUpTask.execute(Config.getText(et_name),Config.getText(et_email)
														,Config.getText(et_password),Config.getText(et_dob)
														,Config.getText(et_addressline),CountryId,StateId,CityId);
											}
											else
											{
												onShake(et_addressline);
											}
										}
										else
										{
											onShakeSpnr(spnr_city);
										}
									}
									else
									{
										onShakeSpnr(spnr_state);
									}
								}
								else
								{
									onShakeSpnr(spnr_country);
								}
							}
							else
							{
								onShake(et_dob);
							}
						}
						else
						{
							onShake(et_password);
						}
					}
					else
					{
						onShake(et_email);
					}
				}
				else
				{
					onShake(et_name);
				}
			}
		});
		return view;
	}
	@SuppressLint("NewApi")
	public void onShake(final View v) 
	{
		final Drawable drawable=v.getBackground();
		Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
		v.startAnimation(shake);
		new Handler().postDelayed(new Runnable()
		{			

			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				v.setBackground(drawable);
			}
		},800);
		v.setBackground(getActivity().getResources().getDrawable(R.drawable.alert_red_textbox));
		v.requestFocus();
	}

	@SuppressLint("NewApi")
	public void onShakeSpnr(final Spinner v) 
	{
		Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
		v.startAnimation(shake);
		/*  new Handler().postDelayed(new Runnable()
	    {			

			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				v.setBackground(drawable);
			}
		},800);
	    v.setBackground(getActivity().getResources().getDrawable(R.drawable.alert_red_textbox));*/
		v.requestFocus();
	}
}
