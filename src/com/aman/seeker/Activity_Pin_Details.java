package com.aman.seeker;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.aman.fragments.Frag_Pin_Details;

public class Activity_Pin_Details extends Activity
{
	FragmentManager fragmentManager ;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin_details);
		fragmentManager = getFragmentManager();		
		fragmentManager.beginTransaction().replace(R.id.activity_pin_detail_container,new Frag_Pin_Details()).commit();
	}
}
