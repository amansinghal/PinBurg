package com.aman.utils;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyTabHost extends LinearLayout implements OnClickListener
{
	ArrayList<FragInfo> fragmentlist=new ArrayList<>();
	onTabClickListener tabClickListener;
	int index=0;
	public MyTabHost(Context context)
	{
		super(context);
		setOrientation(LinearLayout.HORIZONTAL);
		setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getConvertedSP(60)));
	}
	
	public MyTabHost(Context context,AttributeSet attr)
	{
		super(context, attr);
	}
		
	public void addTabs(Fragment fragment,String title) throws MyTabHostException
	{
		if(fragment==null||title==null)
		{
			throw new MyTabHostException("Feild should not be null in constructor");
		}
		FragInfo info=new FragInfo();
		info.fragment=fragment;
		info.title=title;				
		if(fragmentlist.size()==0)			
		{
			info.textView=getTextView(title,true);
			info.isSelected=true;
			addView(getTextView(title,true));
		}
		else
		{
			info.textView=getTextView(title,false);
			info.isSelected=false;
			addView(getTextView(title,false));
		}
		fragmentlist.add(info);
	}
	
	public void addTabs(Fragment fragment,String title,Drawable iconDrawable) throws MyTabHostException
	{
		if(fragment==null||title==null||iconDrawable==null)
		{
			throw new MyTabHostException("Feild should not be null in constructor");
		}
		FragInfo info=new FragInfo();
		info.fragment=fragment;
		info.title=title;
		info.iconDrawable=iconDrawable;
		fragmentlist.add(info);
	}
	
	public void addTabs(Fragment fragment,String title,Bitmap iconBitmap) throws MyTabHostException
	{
		if(fragment==null||title==null||iconBitmap==null)
		{
			throw new MyTabHostException("Feild should not be null in constructor");
		}
		FragInfo info=new FragInfo();
		info.fragment=fragment;
		info.title=title;
		info.iconBitmap=iconBitmap;
		fragmentlist.add(info);
	}
	
	public void addTabs(Fragment fragment,Drawable iconDrawable) throws MyTabHostException
	{
		if(fragment==null||iconDrawable==null)
		{
			throw new MyTabHostException("Feild should not be null in constructor");
		}
		FragInfo info=new FragInfo();
		info.fragment=fragment;		
		info.iconDrawable=iconDrawable;
		fragmentlist.add(info);
	}
	
	public void addTabs(Fragment fragment,Bitmap iconBitmap) throws MyTabHostException
	{
		if(fragment==null||iconBitmap==null)
		{
			throw new MyTabHostException("Feild should not be null in constructor");
		}
		FragInfo info=new FragInfo();
		info.fragment=fragment;		
		info.iconBitmap=iconBitmap;
		fragmentlist.add(info);
	}
	class FragInfo
	{
		public Fragment fragment;
		public String title="";
		public Drawable iconDrawable;
		public Bitmap iconBitmap;
		public TextView textView;
		public boolean isSelected;
	}
	
	public class MyTabHostException extends Exception
	{		 

		private static final long serialVersionUID = 1L;
		
		private String message = null;
	 
	    public MyTabHostException()
	    {
	        super();
	    }
	 
	    public MyTabHostException(String message) {
	        super(message);
	        this.message = message;
	    }
	 
	    public MyTabHostException(Throwable cause) 
	    {
	        super(cause);
	    }
	 
	    @Override
	    public String toString() 
	    {
	        return message;
	    }
	 
	    @Override
	    public String getMessage() 
	    {
	        return message;
	    }
	}
	
	public void setTabClickListener(onTabClickListener tabClickListener) 
	{
		this.tabClickListener = tabClickListener;		
	}
	
	public Button getButton(String text)
	{
		Button button = new Button(getContext());
		button.setText(text);
		button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1));
		button.setGravity(Gravity.CENTER);
		button.setTextAppearance(getContext(), android.R.attr.textAppearanceSmall);
		button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
		return button;
	}
	
	public TextView getTextView(String title,boolean selected)
	{
		TextView textView = new TextView(getContext());
		textView.setText(title);
		textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1));
		textView.setGravity(Gravity.CENTER);
		textView.setTextAppearance(getContext(), android.R.attr.textAppearanceSmall);		
		textView.setTextAppearance(getContext(), android.R.attr.textAppearanceSmall);
		textView.setPadding(getConvertedSP(5),getConvertedSP(10),getConvertedSP(5),getConvertedSP(10));
		if(selected)
		{
			textView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
		}
		else
		{
			textView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
		}
		textView.setOnClickListener(this);
		textView.setTag(fragmentlist.size());		
		return textView;
	}
	
	public int getConvertedSP(int value)
	{
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,value, getResources().getDisplayMetrics());
		return px;
	}

	@Override
	public void onClick(View v)
	{
		this.tabClickListener.onTabClick(v, Integer.parseInt(String.valueOf(v.getTag())));
		Log.e("MyTabHost", String.valueOf(v.getTag()));
		changeSelectedBackGroundTextView( Integer.parseInt(String.valueOf(v.getTag())));
	}
	
	public static interface onTabClickListener
	{
		void onTabClick(View v,int position);
	}
	
	public void changeSelectedBackGroundTextView(int position)
	{
		for(int i= 0; i < fragmentlist.size() ; i++)
		{
			if(i==position)
			{				
				((TextView) getChildAt(i)).setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
				fragmentlist.get(i).textView=(TextView) getChildAt(i);
			}
			else
			{				
				((TextView) getChildAt(i)).setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
				fragmentlist.get(i).textView=(TextView) getChildAt(i);
			}
		}
	}
}
