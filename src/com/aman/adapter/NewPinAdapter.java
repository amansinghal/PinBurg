package com.aman.adapter;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aman.ModelClasses.Pin;
import com.aman.seeker.R;
import com.aman.utils.Config;
import com.aman.utils.CustomTextView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewPinAdapter extends BaseAdapter
{
	LayoutInflater inflater;
	Context con;
	ArrayList<Pin> pinData;
	ImageLoader imageLoader;
	int screenSize[];
	private Random mRandom;
	private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
	public NewPinAdapter(Context con,ArrayList<Pin> pinData) 
	{
		this.con=con;
		this.pinData=pinData;
		imageLoader=ImageLoader.getInstance();
		screenSize=Config.getScreenSize(con);
		this.mRandom = new Random();
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

			v = inflater.inflate(R.layout.list_item_newsfeed_,arg2, false);	
			
			holder.iv_place_pic=(DynamicHeightImageView)v.findViewById(R.id.iv_place_pic);
			
			holder.tv_pin_cata_name=(CustomTextView)v.findViewById(R.id.tv_pin_catagory);
			
			holder.tv_pin_date=(CustomTextView)v.findViewById(R.id.tv_pin_date);
			
			holder.tv_pin_des=(CustomTextView)v.findViewById(R.id.tv_pin_description);
			
			holder.tv_pin_name=(CustomTextView)v.findViewById(R.id.tv_place_name);
			
			holder.tv_pin_country=(TextView)v.findViewById(R.id.tv_pin_country);
			
			holder.rt_pin_rate=(RatingBar)v.findViewById(R.id.rt_pin_rating);
			
			//holder.iv_place_pic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,Config.getScreenSize(con)[1]/3));

			v.setTag(holder);
		}
		else
		{
			holder=(viewHolder)v.getTag();
		}
		
		double positionHeight = getPositionRatio(position);
	
		holder.iv_place_pic.setHeightRatio(positionHeight);
								
		if(pinData.get(position).images.size()==0)
		{
			imageLoader.displayImage("https://maps.googleapis.com/maps/api/staticmap?zoom=15&size=600x300&maptype=normal&markers=icon:http://chart.apis.google.com/chart?chst=d_map_pin_icon%26chld=cafe%257C996600%7Clabel:F%7C"+pinData.get(position).pin_lat+","+pinData.get(position).pin_long+"&scale=2", holder.iv_place_pic);
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
		DynamicHeightImageView iv_place_pic;
		CustomTextView tv_pin_name,tv_pin_des,tv_pin_cata_name,tv_pin_date;
		RatingBar rt_pin_rate;
		TextView tv_pin_country;
	}
	
	private double getPositionRatio(final int position) {
		double ratio = sPositionHeightRatios.get(position, 0.0);
		// if not yet done generate and stash the columns height
		// in our real world scenario this will be determined by
		// some match based on the known height and width of the image
		// and maybe a helpful way to get the column height!
		if (ratio == 0) {
		ratio = getRandomHeightRatio();
		sPositionHeightRatios.append(position, ratio);
		Log.d("TAG", "getPositionRatio:" + position + " ratio:" + ratio);
		}
		return ratio;
		}
		private double getRandomHeightRatio() {
		return (mRandom.nextDouble() / 2.0) + 0.8; // height will be 1.0 - 1.5
		// the width
		}
}


