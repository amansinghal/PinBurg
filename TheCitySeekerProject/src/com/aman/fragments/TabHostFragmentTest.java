package com.aman.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aman.seeker.Activity_Dashboard;
import com.aman.seeker.DashBoard.PlaceholderFragment;
import com.aman.seeker.R;
import com.aman.utils.MyTabHost;
import com.aman.utils.MyTabHost.FragInfo;
import com.aman.utils.MyTabHost.MyTabHostException;
import com.aman.utils.MyTabHost.onTabClickListener;

public class TabHostFragmentTest extends Fragment implements onTabClickListener
{
	View v;
	MyTabHost myTabHost;
	int restrict;
	int location[] = new int[2];
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		v=inflater.inflate(R.layout.tabhostest,container,false);
		myTabHost=(MyTabHost)v.findViewById(R.id.myTabHost1);
		myTabHost.setCircleColor(getResources().getColor(R.color.list_item_background), Color.WHITE);
		try 
		{
			myTabHost.addTabs(new Frag_NewsFeed(), "New Pins",getResources().getDrawable(R.drawable.heart));
			myTabHost.addTabs(PlaceholderFragment.newInstance(2), "Title2",getResources().getDrawable(R.drawable.been_here));
			myTabHost.addTabs(PlaceholderFragment.newInstance(3), "Title3",getResources().getDrawable(R.drawable.review));
		} 
		catch (MyTabHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myTabHost.setTabClickListener(this);
		myTabHost.setDefaultFragment(0);
		return v;
	}
	@Override
	public void onTabClick(View v, int position, ArrayList<FragInfo> tagItems)
	{
		/*if(((Activity_Dashboard)getActivity()).isDrawerOpen())
		{
			((Activity_Dashboard)getActivity()).closeDrawer();
		}*/
		getFragmentManager().beginTransaction().replace(R.id.tabHostContainer,tagItems.get(position).fragment).setCustomAnimations(R.anim.enter_anim,R.anim.exit_anim).commit();		
	}	
}
