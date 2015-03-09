package com.aman.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aman.seeker.DashBoard.PlaceholderFragment;
import com.aman.seeker.R;
import com.aman.utils.MyTabHost;
import com.aman.utils.MyTabHost.MyTabHostException;

public class TabHostFragmentTest extends Fragment
{
	View v;
	MyTabHost myTabHost;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		v=inflater.inflate(R.layout.tabhostest,container,false);
		myTabHost=(MyTabHost)v.findViewById(R.id.myTabHost1);
		try 
		{
			myTabHost.addTabs(PlaceholderFragment.newInstance(1), "Title1");
			myTabHost.addTabs(PlaceholderFragment.newInstance(2), "Title2");
			myTabHost.addTabs(PlaceholderFragment.newInstance(3), "Title3");
		} 
		catch (MyTabHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return v;
	}
}
