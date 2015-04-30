package com.aman.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.aman.seeker.R;
import com.aman.utils.Config;

public class Frag_Pin_Details extends Fragment
{
	FrameLayout fm_header;
	int screenSize[];
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) 
	{
		View v=inflater.inflate(R.layout.frag_pin_details, container, false);
		screenSize=Config.getScreenSize(getActivity());
		initView(v);
		return v;
	}
	
	private void initView(View v)
	{
		fm_header=(FrameLayout)v.findViewById(R.id.frag_pin_details_fl_header);
		fm_header.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenSize[1]/2.5)));
	}
}
