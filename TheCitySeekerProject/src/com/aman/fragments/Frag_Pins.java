package com.aman.fragments;

import com.aman.seeker.DashBoard.PlaceholderFragment;
import com.aman.seeker.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Frag_Pins extends Fragment
{
	FragmentTabHost tabHost;
	View v;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{	
		v=inflater.inflate(R.layout.frag_pins, container, false);
		initViews(v);
		initTabs();
		return v;
	}

	public void initViews(View v)
	{
		tabHost=(FragmentTabHost)v.findViewById(android.R.id.tabhost);		
	}

	public void initTabs()
	{
		//((FrameLayout)v.findViewById(R.id.tabHostContent))
		tabHost.setup(getActivity(), getFragmentManager(), R.id.tabHostContent);
		tabHost.addTab(tabHost.newTabSpec("newPins").setIndicator("New Pins"),Frag_NewsFeed.class,null);
		tabHost.addTab(tabHost.newTabSpec("arroundPins").setIndicator("Pin Arround"),PlaceholderFragment.class,null);
		tabHost.addTab(tabHost.newTabSpec("categoraizedPins").setIndicator("Catagorized"),PlaceholderFragment.class,null);

	}
}
