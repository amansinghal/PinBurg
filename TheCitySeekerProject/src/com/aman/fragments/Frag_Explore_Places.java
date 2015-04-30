package com.aman.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.seeker.Activity_Explore_Category;
import com.aman.seeker.R;

public class Frag_Explore_Places extends Fragment implements OnClickListener
{
	View v;
	TextView tv_near,tv_category,tv_highly_rated,tv_taste;
	@Override
	public View onCreateView(LayoutInflater inflater,	ViewGroup container, Bundle savedInstanceState) 
	{
		v=inflater.inflate(R.layout.frag_search, container, false);
		initViews(v);
		return v;
	}

	private void initViews(View v)
	{
		tv_near = (TextView)v.findViewById(R.id.frag_search_tv_near);
		tv_near.setOnClickListener(this);
		tv_category = (TextView)v.findViewById(R.id.frag_search_tv_categories);
		tv_category.setOnClickListener(this);
		tv_highly_rated = (TextView)v.findViewById(R.id.frag_search_tv_highly_rated);
		tv_highly_rated.setOnClickListener(this);
		tv_taste = (TextView)v.findViewById(R.id.frag_search_tv_taste);
		tv_taste.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.frag_search_tv_categories:
			
			callForCategorisedPlaces();
			
			break;

		case R.id.frag_search_tv_highly_rated:

			break;

		case R.id.frag_search_tv_near:

			break;

		case R.id.frag_search_tv_taste:

			break;
		}
	}
	
	private void callForCategorisedPlaces()
	{
		Intent intent = new Intent(getActivity(),Activity_Explore_Category.class);
		startActivity(intent);
	}
}
