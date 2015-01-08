package com.aman.fragments;

import java.util.ArrayList;
import com.aman.ModelClasses.Pin;
import com.aman.adapter.NewPinAdapter;
import com.aman.seeker.R;
import com.aman.utils.Config;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.AbsListView.OnScrollListener;
import asynctasks.NewPinTask;

public class Frag_My_Tagged_Pins extends Fragment implements OnClickListener
{
	GoogleMap googleMap = null;

	ProgressDialog dialog;

	private static View view;

	private ListView lv_my_pins;

	private ViewSwitcher vSwitcher;

	NewPinAdapter adapter;

	SharedPreferences pref;

	NewPinTask task;

	ArrayList<Pin> pinData=new ArrayList<>();

	TextView tv_mapview,tv_listview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 	
	{
		pref=(getActivity().getSharedPreferences(Config.PREF_KEY, Context.MODE_PRIVATE));
		if (view != null) 
		{			
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}

		try 
		{				
			view = inflater.inflate(R.layout.map_layout_my_tagged_pins, container, false);					
		}
		catch (InflateException e)
		{
		}
		
	
		init();
		
		initComponent(view);

		adapter=new NewPinAdapter(getActivity(), pinData);

		lv_my_pins.setAdapter(adapter);

		task=new NewPinTask(getActivity(),new NewPinTask.onTaskCompleteListener()
		{		
			@Override
			public void ontaskComplete(ArrayList<Pin> pinData,String op_type) 
			{
				Frag_My_Tagged_Pins.this.pinData.addAll(pinData);
				Config.myPinData=pinData;
				adapter.notifyDataSetChanged();
				if(Frag_My_Tagged_Pins.this.pinData.size()>=10)
				{
					lv_my_pins.setOnScrollListener(scrollListener);
				}
				if(pinData.size()>0)
					showMarkerOnPin(pinData);
				//swipeRefreshLayout.setRefreshing(false);
			}			
		},true);
		if(Config.myPinData!=null)
		{
			pinData.addAll(Config.myPinData);
			showMarkerOnPin(pinData);
			adapter.notifyDataSetChanged();
			onRefreshList();
		}
		else
		{
			//swipeRefreshLayout.setRefreshing(true);
			task.execute(pref.getString("uid", ""),"0","load");
		}

		return view;
	}
	public void init()
	{		
		if (googleMap == null)
		{
			googleMap = ((SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map_my_tagged_pins)).getMap();
			
			googleMap.getUiSettings().setMyLocationButtonEnabled(false);

			googleMap.getUiSettings().setZoomControlsEnabled(false);

			googleMap.setOnMapClickListener(null);
		}
	}

	public void initComponent(View v)
	{
		lv_my_pins=(ListView)v.findViewById(R.id.lv_my_pins);
		vSwitcher=(ViewSwitcher)v.findViewById(R.id.viewswithcer_my_pins);
		tv_listview=(TextView)v.findViewById(R.id.tv_listview_mytagged_pin);
		tv_listview.setOnClickListener(this);
		tv_mapview=(TextView)v.findViewById(R.id.tv_mapview_mytagged_pin);
		tv_mapview.setOnClickListener(this);
	}


	OnScrollListener scrollListener=new OnScrollListener()
	{

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) 
		{


		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) 
		{			
			if ((firstVisibleItem + visibleItemCount) == totalItemCount)
			{
				if(task!=null&&task.getStatus()==AsyncTask.Status.RUNNING)
				{
					return;
				}
				task=new NewPinTask(getActivity(),new NewPinTask.onTaskCompleteListener()
				{		
					@Override
					public void ontaskComplete(ArrayList<Pin> pinData,String op_type) 
					{
						Frag_My_Tagged_Pins.this.pinData.addAll(pinData);
						adapter.notifyDataSetChanged();
						if(pinData.size()>=10)
							lv_my_pins.setOnScrollListener(scrollListener);
						if(pinData.size()>0)
							showMarkerOnPin(pinData);	
					}			
				},false);
				task.execute("",pinData.get(pinData.size()-1).id,"load");
				lv_my_pins.setOnScrollListener(null);
			}
		}
	};

	public void onRefreshList()
	{
		if(task!=null&&task.getStatus()==AsyncTask.Status.RUNNING)
		{
			return;
		}
		task=new NewPinTask(getActivity(),new NewPinTask.onTaskCompleteListener()
		{		
			@Override
			public void ontaskComplete(ArrayList<Pin> pinData,String op_type) 
			{
				//swipeRefreshLayout.setRefreshing(false);
				if(op_type.equals("refresh"))
				{
					for(int i=0;i<pinData.size();i++)
					{
						Frag_My_Tagged_Pins.this.pinData.add(0,pinData.get(i));
					}
				}
				else
				{
					Frag_My_Tagged_Pins.this.pinData.addAll(pinData);
				}

				Config.myPinData=Frag_My_Tagged_Pins.this.pinData;

				if(pinData.size()>0)
				{
					adapter.notifyDataSetChanged();
					showMarkerOnPin(pinData);
				}
				if(Frag_My_Tagged_Pins.this.pinData.size()>=10)
				{
					lv_my_pins.setOnScrollListener(scrollListener);
				}
			}			
		},false);
		try
		{
			task.execute(pref.getString("uid", ""),pinData.get(0).id,"refresh");
		}
		catch(Exception e)
		{
			task.execute(pref.getString("uid", ""),"0","load");	
		}
	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.tv_listview_mytagged_pin:
			vSwitcher.showNext();
			break;
		case R.id.tv_mapview_mytagged_pin:
			vSwitcher.showPrevious();
			break;
		}		
	}

	public void showMarkerOnPin(ArrayList<Pin> pinData)
	{
		for(int i=0;i<pinData.size();i++)
		{
			googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(pinData.get(i).pin_lat),Double.parseDouble(pinData.get(i).pin_long))).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pin))));		 	
		}
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(Double.parseDouble(pinData.get(pinData.size()-1).pin_lat),Double.parseDouble(pinData.get(pinData.size()-1).pin_long))).zoom(10).build()));
	}
}
