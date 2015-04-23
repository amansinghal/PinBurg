package com.aman.fragments;

import java.util.List;

import android.app.Fragment;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import asynctasks.GetAddressFromLocationTask;

import com.aman.seeker.Activity_Explore_Category;
import com.aman.seeker.R;
import com.aman.utils.Config;
import com.aman.utils.GPSTracker;

public class Frag_Explore_Places_List_View extends Fragment implements OnClickListener, OnCloseListener
{
	private View v;
	private LinearLayout ll_place_box;
	private SearchView sv_search;
	private ImageView iv_refresh_current_location,iv_search_for_pin,iv_show_map_view;
	private final int HIDE_TAG=0x102;
	private ProgressBar pb_progress;
	private AutoCompleteTextView ac_tv_search_pin;
	private GPSTracker gps;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		v = inflater.inflate(R.layout.frag_explore_category_list_view, container, false);
		gps = ((Activity_Explore_Category)getActivity()).gps;		
		initViews(v);		
		setAddressFromLocation(gps.location);
		return v;
	}		
	
	private void setAddressFromLocation(Location location)
	{
		GetAddressFromLocationTask task = new GetAddressFromLocationTask(getActivity(), new GetAddressFromLocationTask.onTaskCompleteListener() 
		{		
			@Override
			public void ontaskComplete(List<Address> listAddress) 
			{
				if(!listAddress.isEmpty())
				ac_tv_search_pin.setText(listAddress.get(0).getAddressLine(0));		
			}
		});
		task.execute(location.getLatitude(),location.getLongitude());
	}

	private void initViews(View v)
	{
		ll_place_box = (LinearLayout)v.findViewById(R.id.frag_explore_category_list_view_place_box);
		sv_search = (SearchView)v.findViewById(R.id.frag_explore_category_list_view_search_box);
		sv_search.setVisibility(View.GONE);		
		sv_search.setOnCloseListener(this);
		iv_refresh_current_location = (ImageView)v.findViewById(R.id.frag_explore_category_list_view_place_box_iv_refresh);
		iv_refresh_current_location.setOnClickListener(this);
		iv_search_for_pin = (ImageView)v.findViewById(R.id.frag_explore_category_list_view_search_icon);
		iv_search_for_pin.setOnClickListener(this);
		iv_show_map_view = (ImageView)v.findViewById(R.id.frag_explore_category_list_view_map_icon);
		pb_progress = ((Activity_Explore_Category)getActivity()).pb_progress;
		pb_progress.setVisibility(View.INVISIBLE);
		ac_tv_search_pin = (AutoCompleteTextView)v.findViewById(R.id.frag_explore_category_list_view_place_box_ac_place_name);
		ac_tv_search_pin.setFocusableInTouchMode(false);
		ac_tv_search_pin.setFocusable(false);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.frag_explore_category_list_view_place_box_iv_refresh:
			handleDrawableForChangeCurrentLocation();
			break;
		case R.id.frag_explore_category_list_view_search_icon:
			handleDrawableToSearchPin();
		break;
		}
	}

	private void handleDrawableForChangeCurrentLocation()
	{
		if(iv_refresh_current_location.getTag() == null)
		{
			iv_refresh_current_location.setTag(HIDE_TAG);
			iv_refresh_current_location.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
			iv_search_for_pin.setVisibility(View.GONE);
			iv_show_map_view.setVisibility(View.GONE);
			ac_tv_search_pin.setFocusableInTouchMode(true);
			ac_tv_search_pin.setFocusable(true);			
		}
		else
		{
			iv_refresh_current_location.setTag(null);
			iv_refresh_current_location.setImageDrawable(getResources().getDrawable(R.drawable.refresh_marker));
			iv_search_for_pin.setVisibility(View.VISIBLE);
			iv_show_map_view.setVisibility(View.VISIBLE);
			ac_tv_search_pin.setFocusableInTouchMode(false);
			ac_tv_search_pin.setFocusable(false);
			Config.hideKeyboard(getActivity(), getActivity());
		}
	}
	
	private void handleDrawableToSearchPin()
	{
		if(iv_search_for_pin.getTag() == null)
		{
			iv_search_for_pin.setTag(HIDE_TAG);
			ll_place_box.setVisibility(View.GONE);
			sv_search.setVisibility(View.VISIBLE);
			sv_search.setIconified(false);
		}
		else
		{
			iv_search_for_pin.setTag(null);
			ll_place_box.setVisibility(View.VISIBLE);
			sv_search.setVisibility(View.GONE);					
		}
	}

	@Override
	public boolean onClose() 
	{
		handleDrawableToSearchPin();
		return false;
	}
}
