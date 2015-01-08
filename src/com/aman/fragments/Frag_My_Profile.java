package com.aman.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aman.seeker.DashBoard;
import com.aman.seeker.R;
import com.aman.utils.Config;

public class Frag_My_Profile extends Fragment 
{
	FrameLayout fl_profile;
	ImageView iv_profile;
	TranslateAnimation anim;
	Context context;
	SharedPreferences pref;
	TextView tv_profile_pic;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.frag_my_profile, container,false);
		context=getActivity();
		pref=context.getSharedPreferences(Config.PREF_KEY, Context.MODE_PRIVATE);
		tv_profile_pic=(TextView)v.findViewById(R.id.tv_profile_name);
		tv_profile_pic.setText(pref.getString("name", ""));
		iv_profile=(ImageView)v.findViewById(R.id.iv_profile_image);
		iv_profile.setImageDrawable(null);
		//iv_profile.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,Config.getScreenSize(getActivity())[1]/2-200));
		
		Config.showImageFromUrl( pref.getString("photo_path", ""),iv_profile);	
		
		fl_profile=(FrameLayout)v.findViewById(R.id.fl_profile);
		
		fl_profile.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,Config.getScreenSize(getActivity())[1]/3));
		fl_profile.setOnClickListener(new OnClickListener()
		{		
			@Override
			public void onClick(View arg0) 
			{
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				getActivity().startActivityForResult(i, DashBoard.IMAGE_SELECT);
			}
		});
		return v;		
	}
}
