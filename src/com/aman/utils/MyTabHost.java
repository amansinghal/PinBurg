package com.aman.utils;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MyTabHost extends LinearLayout
{
	ArrayList<FragInfo> fragmentlist=new ArrayList<>();
	public MyTabHost(Context context)
	{
		super(context);
		setOrientation(LinearLayout.HORIZONTAL);
		setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
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
	}
	
	class MyTabHostException extends Exception
	{		 

		private static final long serialVersionUID = 1L;
		
		private String message = null;
	 
	    public MyTabHostException() {
	        super();
	    }
	 
	    public MyTabHostException(String message) {
	        super(message);
	        this.message = message;
	    }
	 
	    public MyTabHostException(Throwable cause) {
	        super(cause);
	    }
	 
	    @Override
	    public String toString() {
	        return message;
	    }
	 
	    @Override
	    public String getMessage() {
	        return message;
	    }
	}
}
