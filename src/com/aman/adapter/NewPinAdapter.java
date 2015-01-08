package com.aman.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aman.ModelClasses.Pin;
import com.aman.seeker.R;
import com.aman.utils.Config;
import com.aman.utils.CustomTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewPinAdapter extends BaseAdapter
{
	LayoutInflater inflater;
	Context con;
	ArrayList<Pin> pinData;
	ImageLoader imageLoader;
	public NewPinAdapter(Context con,ArrayList<Pin> pinData) 
	{
		this.con=con;
		this.pinData=pinData;
		imageLoader=ImageLoader.getInstance();		
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return pinData.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return pinData.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2)
	{
		// TODO Auto-generated method stub
		// Declare Variables
		viewHolder holder;
		if(v==null)
		{
			inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			holder=new viewHolder();

			v = inflater.inflate(R.layout.list_item_newsfeed,arg2, false);	
			
			holder.iv_place_pic=(ImageView)v.findViewById(R.id.iv_place_pic);
			
			holder.tv_pin_cata_name=(CustomTextView)v.findViewById(R.id.tv_pin_catagory);
			
			holder.tv_pin_date=(CustomTextView)v.findViewById(R.id.tv_pin_date);
			
			holder.tv_pin_des=(CustomTextView)v.findViewById(R.id.tv_pin_description);
			
			holder.tv_pin_name=(CustomTextView)v.findViewById(R.id.tv_place_name);
			
			holder.tv_pin_country=(TextView)v.findViewById(R.id.tv_pin_country);
			
			holder.rt_pin_rate=(RatingBar)v.findViewById(R.id.rt_pin_rating);
			
			holder.iv_place_pic.setLayoutParams(new LinearLayout.LayoutParams(Config.getScreenSize(con)[0]/3,LinearLayout.LayoutParams.MATCH_PARENT));

			v.setTag(holder);
		}
		else
		{
			holder=(viewHolder)v.getTag();
		}
								
		if(pinData.get(position).images.size()==0)
		{
			holder.iv_place_pic.setImageDrawable(con.getResources().getDrawable(R.drawable.img_not_available));
		}
		else
		{
			imageLoader.displayImage("http://www.pinburg.com"+pinData.get(position).images.get(0).get("path"), holder.iv_place_pic);
		}
		
		holder.tv_pin_cata_name.setText(pinData.get(position).cat_name);
		
		holder.tv_pin_country.setText((pinData.get(position).formatted_add.substring(pinData.get(position).formatted_add.lastIndexOf(",")+1)).trim());
		
		holder.tv_pin_date.setText(pinData.get(position).date);
		
		holder.tv_pin_des.setText(pinData.get(position).description);
		
		holder.tv_pin_name.setText(pinData.get(position).name);
		
		return v;
	}
	class viewHolder
	{
		ImageView iv_place_pic;
		CustomTextView tv_pin_name,tv_pin_des,tv_pin_cata_name,tv_pin_date;
		RatingBar rt_pin_rate;
		TextView tv_pin_country;
	}
}


