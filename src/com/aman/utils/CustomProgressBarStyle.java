package com.aman.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
	public CustomProgressBarStyle(View view) 
	{
		this.view=view;		
	}

	public void startProgress(Context context)
	{
		this.context=context;
		this.textView=new TextView(context);
		this.linearLayout2=new LinearLayout(context);		
		linearLayout2.setBackgroundColor(context.getResources().getColor(R.color.activated_listitem_color));		
		if(view instanceof LinearLayout)
		{			
			LinearLayout linearLayout=(LinearLayout)view;						
			textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			textView.setText("Loading...");			
			textView.setTextSize(intotpx(10));
			textView.setPadding(0, 5, 0, 5);
			textView.setGravity(Gravity.CENTER);
			textView.setAnimation(getBlinkAnimation());				
			textView.setTextColor(context.getResources().getColor(android.R.color.white));
			linearLayout2.addView(textView);			
		}
		if(view instanceof RelativeLayout)
		{			
			RelativeLayout linearLayout=(RelativeLayout)view;			
			textView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
			linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			textView.setText("Loading...");
			textView.setTextSize(intotpx(10));
			textView.setPadding(0, 5, 0, 5);
			textView.setGravity(Gravity.CENTER);
			textView.setAnimation(getBlinkAnimation());
			textView.setTextColor(context.getResources().getColor(android.R.color.white));			
			linearLayout2.addView(textView);			
			linearLayout.addView(linearLayout2);
		}

		if(view instanceof FrameLayout)
		{			
			FrameLayout linearLayout=(FrameLayout)view;			
			textView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT));
			linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
			textView.setText("Loading...");
			textView.setPadding(0, 5, 0, 5);
			textView.setTextSize(intotpx(10));
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(context.getResources().getColor(android.R.color.white));			
			textView.setAnimation(getBlinkAnimation());
			linearLayout2.addView(textView);
			linearLayout.addView(linearLayout2);
		}
		linearLayout2.startAnimation(getComeAnimation());
	}

	public void stopProgress()
	{
		if(linearLayout2!=null)
		{	
			linearLayout2.removeView(textView);
			linearLayout2.startAnimation(getOutAnimation());
		}		
	}

	public Animation getBlinkAnimation()
	{
		Animation animation = new AlphaAnimation(1, 0);         // Change alpha from fully visible to invisible
		animation.setDuration(400);                             // duration - half a second
		animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
		animation.setRepeatCount(-1);                            // Repeat animation infinitely
		animation.setRepeatMode(Animation.REVERSE);             // Reverse animation at the end so the button will fade back in

		return animation;
	}

	public Animation getComeAnimation()
	{
		Animation animation = new TranslateAnimation(0, 0,0,Animation.RELATIVE_TO_PARENT);       // Change alpha from fully visible to invisible
		animation.setDuration(1000);                             // duration - half a second        
		animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
		//animation.setRepeatCount(1);                            // Repeat animation infinitely
		return animation;
	}

	public Animation getOutAnimation()
	{
		Animation animation = new TranslateAnimation(0, 0,Animation.RELATIVE_TO_PARENT,0);       // Change alpha from fully visible to invisible
		animation.setDuration(1000);                             // duration - half a second
		//animation.setFillAfter(true);
		animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
		//animation.setRepeatCount(1);                            // Repeat animation infinitely
		return animation;
	}

	public float intotpx(int px_)
	{
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px_, r.getDisplayMetrics());
		return px;
	}
}
