package com.aman.fragments;

import java.util.ArrayList;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import asynctasks.NewPinTask;

import com.aman.ModelClasses.Pin;
import com.aman.adapter.NewPinAdapter;
import com.aman.seeker.Activity_Pin_Details;
import com.aman.seeker.R;
import com.aman.utils.Config;

public class Frag_NewsFeed extends Fragment implements OnItemClickListener 
{
	ListView lv_news_feeds;
	SwipeRefreshLayout swipeRefreshLayout;
	NewPinAdapter adapter;
	ArrayList<Pin> pinData=new ArrayList<>();
	NewPinTask task;
	int page=1;
	View v;
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		v=inflater.inflate(R.layout.fragment_newsfeed, container,false);		
		swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipe_layout);
		lv_news_feeds=(ListView)v.findViewById(R.id.lv_news_feeds);
		lv_news_feeds.setOnItemClickListener(this);
		swipeRefreshLayout.setColorScheme(android.R.color.black,android.R.color.holo_blue_bright,android.R.color.holo_red_dark,R.color.activated_listitem_color);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener()
		{			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
				onRefreshList();
			}
		});
		adapter=new NewPinAdapter(getActivity(), pinData);
		lv_news_feeds.setAdapter(adapter);
		task=new NewPinTask(getActivity(),v,new NewPinTask.onTaskCompleteListener()
		{		
			@Override
			public void ontaskComplete(ArrayList<Pin> pinData,String op_type) 
			{
				// TODO Auto-generated method stub
				Frag_NewsFeed.this.pinData.addAll(pinData);
				Config.pinData=pinData;
				adapter.notifyDataSetChanged();
				if(Frag_NewsFeed.this.pinData.size()>=10)
				{
					lv_news_feeds.setOnScrollListener(scrollListener);
				}
				swipeRefreshLayout.setRefreshing(false);
			}			
		},false);
		if(Config.pinData!=null)
		{
			pinData.addAll(Config.pinData);
			adapter.notifyDataSetChanged();
			onRefreshList();
		}
		else
		{
			swipeRefreshLayout.setRefreshing(true);
			task.execute("",String.valueOf(page),"load");
		}		
		return v;		
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
				task=new NewPinTask(getActivity(),v,new NewPinTask.onTaskCompleteListener()
				{		
					@Override
					public void ontaskComplete(ArrayList<Pin> pinData,String op_type) 
					{
						// TODO Auto-generated method stub
						Frag_NewsFeed.this.pinData.addAll(pinData);
						adapter.notifyDataSetChanged();
						swipeRefreshLayout.setRefreshing(false);
						if(pinData.size()>=10)
						lv_news_feeds.setOnScrollListener(scrollListener);
					}			
				},false);
				page++;
				task.execute("",String.valueOf(page),"load");
				lv_news_feeds.setOnScrollListener(null);
			}
		}
	};
	
	public void onRefreshList()
	{
		if(task!=null&&task.getStatus()==AsyncTask.Status.RUNNING)
		{
			return;
		}
		task=new NewPinTask(getActivity(),v,new NewPinTask.onTaskCompleteListener()
		{		
			@Override
			public void ontaskComplete(ArrayList<Pin> pinData,String op_type) 
			{
				swipeRefreshLayout.setRefreshing(false);
				if(op_type.equals("refresh"))
				{
					for(int i=0;i<pinData.size();i++)
					{
						Frag_NewsFeed.this.pinData.add(0,pinData.get(i));
					}
				}
				else
				{
					Frag_NewsFeed.this.pinData.addAll(pinData);
				}
				
				Config.pinData=Frag_NewsFeed.this.pinData;
				
				if(pinData.size()>0)
				{
					adapter.notifyDataSetChanged();
				}
				if(Frag_NewsFeed.this.pinData.size()>=10)
				{
					lv_news_feeds.setOnScrollListener(scrollListener);
				}
			}			
		},false);
		try
		{
			task.execute("",pinData.get(0).id,"refresh");
		}
		catch(Exception e)
		{
			task.execute("",String.valueOf(page),"load");	
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{		
		Intent intent=new Intent(getActivity(),Activity_Pin_Details.class);
		startActivity(intent);
	}
}
