package com.aman.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources; 
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aman.seeker.R;

public class CustomProgressBarStyle 
{
	View view;
	Context context;
	TextView textView;
	LinearLayout linearLayout2;
	String msg;
	int stripColor;
	boolean blinkNeeded=true;
	public CustomProgressBarStyle(View view,Context context) 
	{
		this.view=view;
		this.context=context;
	}

	public CustomProgressBarStyle(int rootLayoutId,Context context) 
	{
		this.view=((Activity)context).findViewById(rootLayoutId);
		this.context=context;		
	}

	public void startProgress(Context context,String msg,int stripColor)
	{		
		removeDrawanView(view);
		this.context=context;
		this.textView=new TextView(context);
		this.msg=msg;
		this.linearLayout2=new LinearLayout(context);	
		linearLayout2.setTag("fromView");
		this.stripColor=stripColor;
		linearLayout2.setBackgroundColor(stripColor);
		textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
		textView.setText(msg);
		textView.setPadding(0, (int)intotpx(5), 0,(int)intotpx(5));
		textView.setTextSize(intotpx(8));
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(context.getResources().getColor(android.R.color.white));			
		linearLayout2.addView(textView);
		if(view instanceof LinearLayout)
		{			
			LinearLayout linearLayout=(LinearLayout)view;
			linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));			
			linearLayout.addView(linearLayout2);			
		}
		if(view instanceof RelativeLayout)
		{			
			RelativeLayout linearLayout=(RelativeLayout)view;	
			linearLayout2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
			linearLayout.addView(linearLayout2);
		}

		if(view instanceof FrameLayout)
		{			
			FrameLayout linearLayout=(FrameLayout)view;	
			linearLayout2.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT));			
			linearLayout.addView(linearLayout2);
		}	
		linearLayout2.startAnimation(getComeAnimation());
	}



	public void stopProgress()
	{
		if(linearLayout2!=null)
		{				
			blinkNeeded=true;
			linearLayout2.startAnimation(getOutAnimation());
			if(view instanceof LinearLayout)
			{						
				((LinearLayout)view).removeView(linearLayout2);
			}
			if(view instanceof RelativeLayout)
			{			
				((RelativeLayout)view).removeView(linearLayout2);
			}

			if(view instanceof FrameLayout)
			{			
				((FrameLayout)view).removeView(linearLayout2);
			}				
		}		
	}

	public Animation getBlinkAnimation()
	{
		Animation animation = new AlphaAnimation(1, 0);     
		animation.setDuration(400);                       
		animation.setInterpolator(new LinearInterpolator());   
		animation.setRepeatCount(-1);                          
		animation.setRepeatMode(Animation.REVERSE);          
		return animation;
	}

	public Animation getComeAnimation()
	{
		Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setAnimationListener(new AnimationListener()
		{		
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if(blinkNeeded)
					textView.startAnimation(getBlinkAnimation());
			}
		});
		animation.setFillAfter(true);
		animation.setDuration(500);                            
		return animation;
	}

	public Animation getOutAnimation()
	{
		Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);  	
		animation.setAnimationListener(new AnimationListener()
		{		
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

				textView.setAnimation(null);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub				
			}
		});
		animation.setFillAfter(true);
		animation.setDuration(500);                      
		return animation;
	}

	public void setError(final String errorMsg)
	{
		new Handler().post(new Runnable()
		{		
			@Override
			public void run() 
			{
				blinkNeeded=false;
				startProgress(context, errorMsg, context.getResources().getColor(android.R.color.holo_red_light));					
			}
		});		
	}

	public void setError(final String errorMsg,final int stripColor)
	{
		new Handler().post(new Runnable()
		{		
			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				blinkNeeded=false;
				startProgress(context, errorMsg, stripColor);			
			}
		});		
	}

	public void removeError(final String errorMsg)
	{
		new Handler().post(new Runnable()
		{		
			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				textView.setText(errorMsg);
				linearLayout2.setBackgroundColor(stripColor);		
			}
		});		
	}

	public void removeError(final String errorMsg,final int stripColor)
	{
		new Handler().postDelayed(new Runnable()
		{		
			@Override
			public void run() 
			{
				stopProgress();
			}
		},10000);		
	}

	public void setMessage(final String message)
	{
		new Handler().post(new Runnable()
		{		
			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				textView.setText(message);
				linearLayout2.setBackgroundColor(context.getResources().getColor(R.color.activated_listitem_color));		
			}
		});
	}

	public void hideableMessage(String message,int timeDilayToHide)
	{
		blinkNeeded=false;
		startProgress(context, message,stripColor);		
		new Handler().postDelayed(new Runnable()
		{		
			@Override
			public void run() 
			{			
				stopProgress();				
			}
		},timeDilayToHide);
	}

	public float intotpx(int px_)
	{
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px_, r.getDisplayMetrics());
		return px;
	}

	private void removeDrawanView(View view)
	{
		if(view instanceof LinearLayout)
		{			
			LinearLayout linearLayout=(LinearLayout)view;			
			View getRemovableView=getViewsByTag(linearLayout, "fromView");
			getRemovableView.startAnimation(getOutAnimation());
			if(getRemovableView!=null)
			{	
				getRemovableView.startAnimation(getOutAnimation());
				linearLayout.removeView(getRemovableView);
			}
		}
		if(view instanceof RelativeLayout)
		{			
			RelativeLayout linearLayout=(RelativeLayout)view;	
			View getRemovableView=getViewsByTag(linearLayout, "fromView");
			getRemovableView.startAnimation(getOutAnimation());
			if(getRemovableView!=null)
			{	
				getRemovableView.startAnimation(getOutAnimation());
				linearLayout.removeView(getRemovableView);
			}
		}

		if(view instanceof FrameLayout)
		{			
			FrameLayout linearLayout=(FrameLayout)view;
			View getRemovableView=getViewsByTag(linearLayout, "fromView");
			if(getRemovableView!=null)
			{	
				getRemovableView.startAnimation(getOutAnimation());
				linearLayout.removeView(getRemovableView);
			}
		}	
	}

	private View getViewsByTag(ViewGroup root, String tag)
	{	    
		final int childCount = root.getChildCount();
		for (int i = 0; i < childCount; i++) 
		{
			final View child = root.getChildAt(i);
			final Object tagObj = child.getTag();
			if (tagObj != null && tagObj.equals(tag)) 
			{
				return child; 
			}	        
		}
		return null;
	}

}
