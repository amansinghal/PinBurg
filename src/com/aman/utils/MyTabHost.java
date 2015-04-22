package com.aman.utils;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aman.seeker.R;

public class MyTabHost extends LinearLayout implements OnClickListener
{
	ArrayList<FragInfo> fragmentlist=new ArrayList<>();
	onTabClickListener tabClickListener;
	int index=0;
	int colorChecked,colorUnchecked;
	public MyTabHost(Context context)
	{
		super(context);
		setOrientation(LinearLayout.HORIZONTAL);
		setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getConvertedSP(60)));
		colorChecked = Color.parseColor("#fff000");
		colorUnchecked = Color.parseColor("#ffffff");
	}

	public MyTabHost(Context context,AttributeSet attr)
	{
		super(context, attr);
		colorChecked = Color.parseColor("#fff000");
		colorUnchecked = Color.parseColor("#ffffff");
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
			info.linearLayout=getRoundedIconsWithText(title,true,getResources().getDrawable(R.drawable.ic_launcher));
			info.isSelected=true;
			addView(info.linearLayout);
		}
		else
		{
			info.linearLayout=getRoundedIconsWithText(title,false,getResources().getDrawable(R.drawable.ic_launcher));
			info.isSelected=false;
			addView(info.linearLayout);			
		}	
		fragmentlist.add(info);
	}

	public void setDefaultFragment(int position)
	{
		fragmentlist.get(position).linearLayout.performClick();
		changeSelectedBackGroundRoundedIconsWithText(position);
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
		if(fragmentlist.size()==0)			
		{
			info.linearLayout=getRoundedIconsWithText(title,true,iconDrawable);
			info.isSelected=true;
			addView(info.linearLayout);
		}
		else
		{
			info.linearLayout=getRoundedIconsWithText(title,false,iconDrawable);
			info.isSelected=false;
			addView(info.linearLayout);
		}
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
	public class FragInfo
	{
		public Fragment fragment;
		public String title="";
		public Drawable iconDrawable;
		public Bitmap iconBitmap;
		public TextView textView;
		public boolean isSelected;
		public LinearLayout linearLayout;
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
		textView.setTextColor(Color.WHITE);
		textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1));
		textView.setGravity(Gravity.CENTER);
		textView.setTextAppearance(getContext(), android.R.attr.textAppearanceSmall);		
		textView.setTextAppearance(getContext(), android.R.attr.textAppearanceSmall);
		textView.setPadding(getConvertedSP(5),getConvertedSP(10),getConvertedSP(5),getConvertedSP(10));
		if(selected)
		{
			textView.setBackgroundColor(getResources().getColor(R.color.activated_listitem_color));
		}
		else
		{
			textView.setBackgroundColor(getResources().getColor(R.color.list_item_background));
		}
		textView.setOnClickListener(this);
		textView.setTag(fragmentlist.size());		
		return textView;
	}
	
	@SuppressWarnings("deprecation")
	public LinearLayout getRoundedIconsWithText(String title,boolean selected,Drawable icon)
	{
		LinearLayout linearLayout = new LinearLayout(getContext());
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		TextView textView = new TextView(getContext());
		textView.setId(R.id.textView1);
		textView.setText(title);
		textView.setTextColor(Color.WHITE);
		textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1));
		textView.setTextAppearance(getContext(), android.R.attr.textAppearanceSmall);		
		textView.setTextAppearance(getContext(), android.R.attr.textAppearanceSmall);
		textView.setPadding(getConvertedSP(5),0,0,0);
		
		ImageView imageView = new ImageView(getContext());		
		imageView.setId(R.id.imageView1);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(getConvertedSP(40),getConvertedSP(40),1));
		imageView.setPadding(getConvertedSP(5),getConvertedSP(5),getConvertedSP(5),getConvertedSP(5));
		imageView.setImageDrawable(icon);
		linearLayout.addView(imageView);
		linearLayout.addView(textView);
		if(selected)
		{
			textView.setTypeface(null, Typeface.BOLD);
			//imageView.setBackground(getResources().getDrawable(R.drawable.circle_shape_checked));
			imageView.setBackgroundDrawable(getCircle(colorChecked));
		}
		else
		{
			textView.setTypeface(null, Typeface.NORMAL);
			//imageView.setBackground(getResources().getDrawable(R.drawable.circle_shape_unchecked));
			imageView.setBackgroundDrawable(getCircle(colorUnchecked));
		}
		linearLayout.setOnClickListener(this);
		linearLayout.setTag(fragmentlist.size());
		return linearLayout;
	}

	public int getConvertedSP(int value)
	{
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,value, getResources().getDisplayMetrics());
		return px;
	}

	@Override
	public void onClick(View v)
	{
		this.tabClickListener.onTabClick(v, Integer.parseInt(String.valueOf(v.getTag())),fragmentlist);
		Log.e("MyTabHost", String.valueOf(v.getTag()));
		changeSelectedBackGroundRoundedIconsWithText( Integer.parseInt(String.valueOf(v.getTag())));
	}

	public static interface onTabClickListener
	{
		void onTabClick(View v,int position,ArrayList<FragInfo> arrayList);
	}

	public void changeSelectedBackGroundTextView(int position)
	{
		for(int i= 0; i < fragmentlist.size() ; i++)
		{
			if(i==position)
			{				
				((TextView) getChildAt(i)).setBackgroundColor(getResources().getColor(R.color.activated_listitem_color));
				fragmentlist.get(i).textView=(TextView) getChildAt(i);
			}
			else
			{				
				((TextView) getChildAt(i)).setBackgroundColor(getResources().getColor(R.color.list_item_background));
				fragmentlist.get(i).textView=(TextView) getChildAt(i);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void changeSelectedBackGroundRoundedIconsWithText(int position)
	{
		for(int i= 0; i < fragmentlist.size() ; i++)
		{
			if(i==position)
			{				
				((TextView) getChildAt(i).findViewById(R.id.textView1)).setTypeface(null, Typeface.BOLD);
				fragmentlist.get(i).linearLayout=(LinearLayout) getChildAt(i);
				fragmentlist.get(i).linearLayout.findViewById(R.id.imageView1).startAnimation(getPopAnimation());
				//((ImageView)fragmentlist.get(i).linearLayout.findViewById(R.id.imageView1)).setBackground(getResources().getDrawable(R.drawable.circle_shape_checked));
				((ImageView)fragmentlist.get(i).linearLayout.findViewById(R.id.imageView1)).setBackgroundDrawable(getCircle(colorChecked));
			}
			else
			{				
				((TextView) getChildAt(i).findViewById(R.id.textView1)).setTypeface(null, Typeface.NORMAL);
				fragmentlist.get(i).linearLayout=(LinearLayout) getChildAt(i);
				//((ImageView)fragmentlist.get(i).linearLayout.findViewById(R.id.imageView1)).setBackground(getResources().getDrawable(R.drawable.circle_shape_unchecked));
				((ImageView)fragmentlist.get(i).linearLayout.findViewById(R.id.imageView1)).setBackgroundDrawable(getCircle(colorUnchecked));
			}
		}
	}
	
	public int[] getScreenSize(Context context) 
	{
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

		int screenWidthInPix = displayMetrics.widthPixels;

		int screenheightInPix = displayMetrics.heightPixels;

		return (new int[] {  screenWidthInPix,screenheightInPix });
	}
	
	public Animation getPopAnimation()
	{
		Animation animation = new ScaleAnimation(0.6f,1.0f,0.6f,1.0f,Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);  	

		animation.setFillAfter(true);
		
		animation.setDuration(200);                      
		return animation;
	}
	
	public ShapeDrawable getCircle(int color)
	{
		 ShapeDrawable biggerCircle= new ShapeDrawable( new OvalShape());
	        biggerCircle.setIntrinsicHeight( 60 );
	        biggerCircle.setIntrinsicWidth( 60);
	        biggerCircle.setBounds(new Rect(0, 0, 60, 60));
	        biggerCircle.getPaint().setColor(color);
	        
	        return biggerCircle;
	}
	
	
	
	public void setCircleColor(int itemIsChecked,int itemIsUnchecked)
	{
		this.colorChecked = itemIsChecked;
		this.colorUnchecked = itemIsUnchecked;
	}
}
