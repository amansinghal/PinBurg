package com.aman.seeker;

import com.aman.fragments.Frag_My_Profile;
import com.aman.fragments.Frag_Pin_Details;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

public class Activity_Pin_Details extends ActionBarActivity
{
	FragmentManager fragmentManager ;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin_details);
		fragmentManager = getSupportFragmentManager();		
		fragmentManager.beginTransaction().replace(R.id.activity_pin_detail_container,new Frag_Pin_Details()).commit();
	}
}
