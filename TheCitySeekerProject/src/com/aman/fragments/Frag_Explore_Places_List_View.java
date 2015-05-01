package com.aman.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;

import android.app.Fragment;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import asynctasks.FindPlaceTask;
import asynctasks.GetAddressFromLocationTask;
import asynctasks.GetPinTask;
import asynctasks.PlaceDetailsTask;

import com.aman.ModelClasses.Pin;
import com.aman.ModelClasses.PlaceModel;
import com.aman.adapter.NewPinAdapter;
import com.aman.seeker.Activity_Explore_Category;
import com.aman.seeker.R;
import com.aman.utils.Config;
import com.aman.utils.Config.WhatToClose;
import com.aman.utils.GPSTracker;

public class Frag_Explore_Places_List_View extends Fragment implements OnClickListener, OnCloseListener, TextWatcher
{
	private View v;
	private LinearLayout ll_place_box,ll_search,ll_mapview,ll_change_location;
	private SearchView sv_search;
	private ImageView iv_options;
	private final int HIDE_TAG=0x102;
	private ProgressBar pb_progress;
	private AutoCompleteTextView ac_tv_search_pin;
	private GPSTracker gps;
	private ArrayList<PlaceModel> listPlaceModels=new ArrayList<>();
	private ArrayList<String> dropdownContainer=new ArrayList<>();
	private HashMap<String, String> placeDetails;
	private String lastFilledText = "",selectedCategory="";
	private ListView lv_placeList;
	private NewPinAdapter adapter;
	private ArrayList<Pin> pinData=new ArrayList<>();
	private Location locationForSearch=null;
	private Activity_Explore_Category baseActivityContext;
	private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		v = inflater.inflate(R.layout.frag_explore_category_list_view, container, false);
		baseActivityContext =((Activity_Explore_Category)getActivity()); 
		gps = baseActivityContext.gps;		
		selectedCategory = baseActivityContext.selectedCategory;	
		locationForSearch = baseActivityContext.locationForSearch;
		initViews(v);		
		adapter=new NewPinAdapter(getActivity(), pinData);
		lv_placeList.setAdapter(adapter);
		if(locationForSearch == null)
		{
			setAddressFromLocation(gps.location);
		}
		else
		{
			//setAddressFromLocation(locationForSearch);
			ac_tv_search_pin.setText(locationForSearch.getExtras().getString("address"));	
			getPinAccoringToCatgory(""+locationForSearch.getLatitude(),""+locationForSearch.getLongitude(),"10",selectedCategory);
		}
		return v;
	}		

	private void setAddressFromLocation(final Location location)
	{
		GetAddressFromLocationTask task = new GetAddressFromLocationTask(getActivity(), new GetAddressFromLocationTask.onTaskCompleteListener() 
		{		
			@Override
			public void ontaskComplete(List<Address> listAddress) 
			{
				if(!listAddress.isEmpty())
				{
					ac_tv_search_pin.setText(listAddress.get(0).getAddressLine(0));	
					getPinAccoringToCatgory(""+location.getLatitude(),""+location.getLongitude(),"10",selectedCategory);
				}
				else
				{
					Config.alertDialogBox(getActivity(), baseActivityContext, WhatToClose.Activity,"Sorry !! we are unable to get your current location info please try later.", false);
				}
			}
		});
		task.execute(location.getLatitude(),location.getLongitude());
	}

	private void initViews(View v)
	{
		ll_change_location = (LinearLayout)baseActivityContext.findViewById(R.id.activity_explore_categroy_drawer_layout_ll_change_current_location);
		ll_change_location.setOnClickListener(this);
		ll_search = (LinearLayout)baseActivityContext.findViewById(R.id.activity_explore_categroy_drawer_layout_ll_search);
		ll_search.setOnClickListener(this);
		ll_mapview = (LinearLayout)baseActivityContext.findViewById(R.id.activity_explore_categroy_drawer_layout_ll_mapview);
		ll_mapview.setOnClickListener(this);
		lv_placeList = (ListView)v.findViewById(R.id.frag_explore_category_list_view_lv_places_list);
		ll_place_box = (LinearLayout)v.findViewById(R.id.frag_explore_category_list_view_place_box);
		sv_search = (SearchView)v.findViewById(R.id.frag_explore_category_list_view_search_box);
		sv_search.setVisibility(View.GONE);		
		sv_search.setOnCloseListener(this);
		iv_options = (ImageView)v.findViewById(R.id.frag_explore_category_list_view_iv_options);
		iv_options.setOnClickListener(this);
		pb_progress = baseActivityContext.pb_progress;
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
		case R.id.frag_explore_category_list_view_iv_options:
			if(iv_options.getTag() == null)
				baseActivityContext.mDrawer.toggleMenu();
			else
				handleDrawableForChangeCurrentLocation();
			break;
		case R.id.activity_explore_categroy_drawer_layout_ll_search:			
			baseActivityContext.mDrawer.toggleMenu();
			handler = new Handler();
			handler.postDelayed(new Runnable()
			{				
				@Override
				public void run() 
				{
					handleDrawableToSearchPin();					
				}
			}, MenuDrawer.ANIMATION_DELAY+800);
			break;
		case R.id.activity_explore_categroy_drawer_layout_ll_mapview:

			break;
		case R.id.activity_explore_categroy_drawer_layout_ll_change_current_location:
			handleDrawableForChangeCurrentLocation();
			baseActivityContext.mDrawer.toggleMenu();
			break;
		}
	}

	private void handleDrawableForChangeCurrentLocation()
	{
		if(iv_options.getTag() == null)
		{
			iv_options.setTag(HIDE_TAG);
			iv_options.setImageDrawable(getResources().getDrawable(R.drawable.cancel_marker));
			ac_tv_search_pin.setFocusableInTouchMode(true);
			ac_tv_search_pin.setFocusable(true);
			ac_tv_search_pin.requestFocus();
			lastFilledText = ac_tv_search_pin.getText().toString();
			ac_tv_search_pin.setText("");
			ac_tv_search_pin.addTextChangedListener(this);	
			ac_tv_search_pin.setOnItemClickListener(onItemClickListenerForAutoComplete);
		}
		else
		{
			iv_options.setTag(null);
			iv_options.setImageDrawable(getResources().getDrawable(R.drawable.menu_option));
			ac_tv_search_pin.setFocusableInTouchMode(false);
			ac_tv_search_pin.setFocusable(false);
			Config.hideKeyboard(getActivity(), getActivity());
			ac_tv_search_pin.removeTextChangedListener(this);
			ac_tv_search_pin.setOnItemClickListener(null);
			ac_tv_search_pin.setText(lastFilledText);
		}
	}

	OnItemClickListener onItemClickListenerForAutoComplete = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
		{
			handleDrawableForChangeCurrentLocation();
			ac_tv_search_pin.setText(listPlaceModels.get(arg2).desciption);
			PlaceDetailsTask task=new PlaceDetailsTask(getActivity(), new PlaceDetailsTask.onTaskCompleteListener()
			{
				@Override
				public void ontaskComplete(HashMap<String, String> placeDetails) 
				{					
					Log.e("placeDetails",placeDetails.get("name"));
					locationForSearch = new Location("");
					locationForSearch.setLatitude(Double.parseDouble(placeDetails.get("lat")));
					locationForSearch.setLongitude(Double.parseDouble( placeDetails.get("lng")));
					Bundle bundle = new Bundle();
					bundle.putString("address",placeDetails.get("address"));
					locationForSearch.setExtras(bundle);
					baseActivityContext.locationForSearch = locationForSearch;
					Frag_Explore_Places_List_View.this.placeDetails = placeDetails;
					getPinAccoringToCatgory(placeDetails.get("lat"), placeDetails.get("lng"), "10", selectedCategory);
				}
			});
			task.execute(listPlaceModels.get(arg2).reference);			
		}

	};

	private void getPinAccoringToCatgory(String lat,String long_,String range,String category) 
	{
		GetPinTask task = new GetPinTask(getActivity(), v, new GetPinTask.onTaskCompleteListener()
		{			
			@Override
			public void ontaskComplete(ArrayList<Pin> pinData) 
			{
				Frag_Explore_Places_List_View.this.pinData.clear();
				adapter.notifyDataSetChanged();
				if(!pinData.isEmpty())
				{					
					Frag_Explore_Places_List_View.this.pinData.addAll(pinData);
					adapter.notifyDataSetChanged();
				}
				else
				{
					Config.alertDialogBox(getActivity(), null, null, "Opps!! \nPinburg don't have any places for the category near by you.", false);
				}
			}
		}, true);
		task.execute(lat,long_,range,category);
	}

	private void handleDrawableToSearchPin()
	{
		if(iv_options.getTag() == null)
		{
			iv_options.setTag(HIDE_TAG);
			ll_place_box.setVisibility(View.GONE);
			sv_search.setVisibility(View.VISIBLE);			
			sv_search.setIconified(false);
		}
		else
		{
			iv_options.setTag(null);
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

	@Override
	public void afterTextChanged(Editable s) 
	{
		for(String tempstr:dropdownContainer)
		{
			if(tempstr.equals(s.toString())||s.toString().isEmpty())
			{
				return;
			}
		}	

		findPlaceFromGoogleAutoComplete(s.toString());
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) 
	{


	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{

	}

	private void findPlaceFromGoogleAutoComplete(String keywords)
	{
		FindPlaceTask task=new FindPlaceTask(getActivity(), new FindPlaceTask.onTaskCompleteListener()
		{							
			@Override
			public void ontaskComplete(ArrayList<PlaceModel> listPlaces) 
			{				
				listPlaceModels.clear();				
				listPlaceModels.addAll(listPlaces);
				if(listPlaces.size()>0)
				{
					dropdownContainer=new ArrayList<>();
					for(int i=0;i<listPlaces.size();i++)
					{
						dropdownContainer.add(listPlaces.get(i).desciption);
					}
					ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dropdownContainer);
					ac_tv_search_pin.setAdapter(adapter);
					ac_tv_search_pin.showDropDown();
				}								
			}
		});
		task.execute(keywords);
	}
}
