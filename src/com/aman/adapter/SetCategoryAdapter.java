package com.aman.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aman.seeker.R;
import com.aman.utils.Config;

public class SetCategoryAdapter extends BaseAdapter
{
	LayoutInflater inflater;
	Context con;
	public SetCategoryAdapter(Context con) 
	{
		this.con=con;
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2)
	{
		// TODO Auto-generated method stub
		// Declare Variables
		viewHolder holder;
		if(convertView==null)
		{
			inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			holder=new viewHolder();

			convertView = inflater.inflate(R.layout.list_item_newsfeed,arg2, false);	
			
			holder.iv_place_pic=(ImageView)convertView.findViewById(R.id.iv_place_pic);
			
			holder.iv_place_pic.setLayoutParams(new LinearLayout.LayoutParams(Config.getScreenSize(con)[0]/3,LinearLayout.LayoutParams.MATCH_PARENT));

			convertView.setTag(holder);
		}
		else
		{
			holder=(viewHolder)convertView.getTag();
		}

		return convertView;
	}
	class viewHolder
	{
		ImageView iv_place_pic;
	}
}


