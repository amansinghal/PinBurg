package com.aman.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.aman.seeker.R;


/**
 * Sub-Class of {@link TextView} used to set a custom Font for the text
 * 
 * @author kipl108
 * 
 */
public class CustomTextView extends TextView {

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public CustomTextView(Context context) {
		super(context);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs,R.styleable.CustomTextView);
		String customFont = a.getString(R.styleable.CustomTextView_customTextFont);
		setCustomFont(ctx, customFont);		
		a.recycle();
	}

	public boolean setCustomFont(Context ctx, String asset) 
	{
		Typeface tf = null;
		if (asset == null || asset.equals(""))
			return true;
		try {
			tf = Typeface.createFromAsset(ctx.getAssets(), asset);
		}
		catch (Exception e) {
			Log.e("CustomTextView", "Could not get typeface \"" + asset+ "\" : " + e.getMessage());
			return false;
		}
		setTypeface(tf, Typeface.NORMAL);
		return true;
	}

}
