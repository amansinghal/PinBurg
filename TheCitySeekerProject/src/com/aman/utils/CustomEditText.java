package com.aman.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.aman.seeker.R;

/**
 * Sub-Class of {@link EditText} used to set a custom Font for the text
 * 
 * @author kipl108
 * 
 */
public class CustomEditText extends EditText {

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public CustomEditText(Context context) {
		super(context);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs,
				R.styleable.CustomEditText);
		String customFont = a
				.getString(R.styleable.CustomEditText_customEditTextFont);
		setCustomFont(ctx, customFont);
		a.recycle();
	}

	public boolean setCustomFont(Context ctx, String asset) {
		Typeface tf = null;
		if (asset == null || asset.equals(""))
			return true;
		try {
			tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + asset);
		} catch (Exception e) {
			Log.e("CustomTextView", "Could not get typeface \"" + asset
					+ "\" : " + e.getMessage());
			return false;
		}

		setTypeface(tf);
		return true;
	}

}
