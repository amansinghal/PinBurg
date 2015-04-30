package com.aman.seeker;

import com.aman.fragments.Frag_Tag_My_Burg;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class Activity_Add_Pins extends ActionBarActivity
{
	private ActionBar actionBar;
	private FragmentManager fragmentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_pin);
		setActionBar("Add pin");
		fragmentManager = getFragmentManager();		
		setupDefaultFragment();
	}
	
	private void setupDefaultFragment()
	{
		fragmentManager.beginTransaction().replace(R.id.activity_add_pin_container, new Frag_Tag_My_Burg()).commit();
	}
	
	private void setActionBar(String category)
	{
		actionBar = getActionBar();
		actionBar.setTitle(category);		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	 {
	        switch (item.getItemId()) 
	        {	       
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	          return super.onOptionsItemSelected(item);
	        } 
	    } 
}
