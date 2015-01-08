package com.aman.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.aman.seeker.R;

public class CategoryAdapter extends BaseAdapter
{
	LayoutInflater inflater;
	Context con;
	ArrayList<HashMap<String, String>> list;
	public CategoryAdapter(Context con,ArrayList<HashMap<String, String>> list) 
	{
		this.con=con;
		this.list=list;
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return list.get(arg0);
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

			convertView = inflater.inflate(R.layout.list_item_1,arg2, false);	
			
			holder.cb=(CheckBox)convertView.findViewById(R.id.checkBox1);
			
			convertView.setTag(holder);
		}
		else
		{
			holder=(viewHolder)convertView.getTag();
		}
		
		holder.cb.setText(list.get(position).get("name"));
		
		if(list.get(position).get("isSelected").equalsIgnoreCase("1"))
		{
			holder.cb.setChecked(true);			
		}	
		else
		{
			holder.cb.setChecked(true);
		}
		
		final int pos=position;
		
		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{		
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
			{
				if(arg1)
				{
					list.get(pos).put("isSelected", "1");
				}
				else
				{
					list.get(pos).put("isSelected", "0");
				}
			}
		});
		return convertView;
	}
	class viewHolder
	{
		CheckBox cb;
	}
}


