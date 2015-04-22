package com.aman.fragments;

import com.aman.seeker.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Frag_Choose_Category extends Fragment
{
	View v;
	ListView lv_category;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		v= inflater.inflate(R.layout.frag_choose_category, container, false);
		initView(v);
		return v;
	}
	
	private void initView(View v)
	{
		lv_category = (ListView)v.findViewById(R.id.frag_choose_category_lv_category);
	}
}
