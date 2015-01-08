package com.aman.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.aman.seeker.R;

public class CustomProgressDialog extends Dialog
{

	public CustomProgressDialog(Context context) 
	{
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custon_progress_bar_view);
	}
}
