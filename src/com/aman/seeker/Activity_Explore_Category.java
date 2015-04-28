package com.aman.seeker;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aman.fragments.Frag_Explore_Places_List_View;
import com.aman.utils.GPSTracker;

public class Activity_Explore_Category extends ActionBarActivity
{
	public String selectedCategory="";
	private ActionBar actionBar;
	private TextView tv_alert_no_category;
	public ProgressBar pb_progress;
	private FragmentManager fragManager;
	private Location location;
	public Location locationForSearch;
	private boolean shouldCheckForChange = false;
	public GPSTracker gps;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		fragManager = getFragmentManager();
		location = ((ApplicationContextClass)getApplicationContext()).location;
		setContentView(R.layout.activity_explore_category);
		initViews();
		setActionBar("Explore pin according category");

		gps= new GPSTracker(this);
		
		gps.dialog.setOnCancelListener(new OnCancelListener()
		{			
			@Override
			public void onCancel(DialogInterface dialog) 
			{			
				callForChooseCategory();	
			}
		});
				
	}



	private void initViews() 
	{
		tv_alert_no_category = (TextView)findViewById(R.id.activity_explore_category_tv_no_category_alert);
		pb_progress = (ProgressBar)findViewById(R.id.activity_explore_category_pb_progress);		
	}

	private void setActionBar(String category)
	{
		actionBar = getActionBar();
		actionBar.setTitle(category);		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{		 
		getMenuInflater().inflate(R.menu.activity_explore_category_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.activity_explore_category_menu_action_select:
			callForChooseCategory();
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		} 
	} 

	private void callForChooseCategory() 
	{
		if(location == null)
		{
			if(gps.canGetLocation())
			{
				location = ((ApplicationContextClass)getApplicationContext()).location = gps.getLocation();
				Intent intent = new Intent(this,Activity_Choose_Category.class);
				startActivityForResult(intent,Activity_Choose_Category.REQ_CODE_CHOOSE_CATEGORY);				
				shouldCheckForChange = false;				
			}
			else
			{				
				showSettingsAlert();
			}
		}
		else
		{
			Intent intent = new Intent(this,Activity_Choose_Category.class);
			startActivityForResult(intent,Activity_Choose_Category.REQ_CODE_CHOOSE_CATEGORY);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{	
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != RESULT_OK)
		{
			//hiding progressing bar  and showing category not select alert 
			pb_progress.setVisibility(View.GONE);
			if(selectedCategory.isEmpty())
			{
				tv_alert_no_category.setVisibility(View.VISIBLE);
			}
			else
			{
				tv_alert_no_category.setVisibility(View.GONE);
			}
			return;
		}
		if(requestCode == Activity_Choose_Category.REQ_CODE_CHOOSE_CATEGORY)
		{
			//show progress bar, hide category not selected alert & set selected category to title bar 
			pb_progress.setVisibility(View.VISIBLE);
			pb_progress.bringToFront();
			tv_alert_no_category.setVisibility(View.GONE);
			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>) data.getExtras().get("category");
			setActionBar(map.get("name"));
			selectedCategory = map.get("id");
			fragManager.beginTransaction().replace(R.id.activity_explore_category_container, new Frag_Explore_Places_List_View()).commit();
		}
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		if(shouldCheckForChange)
		{
			callForChooseCategory();			
		}
	}
	
	 public void showSettingsAlert()
	    {
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

	        //Setting Dialog Title
	        alertDialog.setTitle("GPS Alert");

	        //Setting Dialog Message
	        alertDialog.setMessage("Please turn on the location services.");

	        //On Pressing Setting button
	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() 
	        {   
	            @Override
	            public void onClick(DialogInterface dialog, int which) 
	            {
	                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                startActivity(intent);
	                shouldCheckForChange = true;
	            }
	        });

	        //On pressing cancel button
	        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
	        {   
	            @Override
	            public void onClick(DialogInterface dialog, int which) 
	            {
	                dialog.cancel();
	            }
	        });

	        alertDialog.show();
	    }

}
