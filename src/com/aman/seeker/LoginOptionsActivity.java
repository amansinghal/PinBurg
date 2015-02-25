package com.aman.seeker;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.aman.fragments.Frag_Login_Options;

public class LoginOptionsActivity extends ActionBarActivity
{
	FragmentTransaction fragmentTransaction;
	ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		actionBar=getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		fragmentTransaction=getSupportFragmentManager().beginTransaction();
		fragmentTransaction.setCustomAnimations(R.anim.enter_anim,R.anim.exit_anim,R.anim.pop_enter_anim,R.anim.pop_exit_anim);
		fragmentTransaction.replace(R.id.fl_login_container,new Frag_Login_Options()).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// TODO Auto-generated method stub
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			if(getSupportFragmentManager().getBackStackEntryCount()>0)
			{
				getSupportFragmentManager().popBackStack();
			}
			else
			{				
				finish();
			}
			break;
		default:
			
			break;
		}	
		return super.onOptionsItemSelected(item);
	}
	
}
