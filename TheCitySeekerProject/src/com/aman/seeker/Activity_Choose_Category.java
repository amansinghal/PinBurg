package com.aman.seeker;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import asynctasks.CatagoryTask;

import com.aman.utils.Config;

public class Activity_Choose_Category extends ActionBarActivity implements OnItemClickListener
{
	private ListView lv_category;
	private ArrayList<HashMap<String, String>> listCategory=new ArrayList<>();
	private SimpleAdapter adapter;
	private ActionBar actionBar;
	public static final int REQ_CODE_CHOOSE_CATEGORY=0x1001;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_category);
		setActionBar("Choose one..");
		initView();
		adapter = new SimpleAdapter(this, listCategory, android.R.layout.simple_list_item_1, new String[]{"name"},new int[]{android.R.id.text1});
		lv_category.setAdapter(adapter);
		lv_category.setOnItemClickListener(this);
		getCategoryTask();
	}
	
	private void setActionBar(String category)
	{
		actionBar = getActionBar();
		actionBar.setTitle(category);		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	private void initView()
	{
		lv_category = (ListView)findViewById(R.id.activity_choose_category_lv_category);
	}

	
	 public boolean onOptionsItemSelected(MenuItem item) 
	 {
	        switch (item.getItemId()) 
	        {	       
	        case android.R.id.home:
	            onBackPressed();
	            setResult(RESULT_CANCELED);
	            return true;
	        default:
	          return super.onOptionsItemSelected(item);
	        } 
	    } 
	 
	 private void getCategoryTask()
	 {
		 CatagoryTask task = new CatagoryTask(this, new CatagoryTask.onTaskCompleteListener()
		 {			
			@Override
			public void ontaskComplete(ArrayList<HashMap<String, String>> listCatagory) 
			{
				if(!listCatagory.isEmpty())
				{
					listCategory.addAll(listCatagory);
					adapter.notifyDataSetChanged();
				}
				else
				{
					Config.alertDialogBox(Activity_Choose_Category.this, null, null, "Unable to get categories right now try later", false);
				}
			}
		});
		 task.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		Intent intent = new Intent();
		intent.putExtra("category", listCategory.get(arg2));
		setResult(RESULT_OK, intent);
		finish();
	}
}
