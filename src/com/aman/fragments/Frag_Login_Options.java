package com.aman.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.aman.seeker.R;

public class Frag_Login_Options extends Fragment
{
	Button btn_login,btn_signup,btn_as_guest,btn_fb_login;
	FragmentTransaction fragmentTransaction;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View view=inflater.inflate(R.layout.frag_login_option, container, false);
		btn_login=(Button)view.findViewById(R.id.btn_login);
		btn_as_guest=(Button)view.findViewById(R.id.btn_as_guest);
		btn_fb_login=(Button)view.findViewById(R.id.btn_fb);
		btn_signup=(Button)view.findViewById(R.id.btn_signup);
		
		btn_login.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View arg0) 
			{
				fragmentTransaction=getFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(R.anim.enter_anim,R.anim.exit_anim,R.anim.pop_enter_anim,R.anim.pop_exit_anim);
				fragmentTransaction.replace(R.id.fl_login_container, new Frag_Login()).addToBackStack(null).commit();
			}
		});
		
		btn_as_guest.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		btn_fb_login.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		btn_signup.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				fragmentTransaction=getFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(R.anim.enter_anim,R.anim.exit_anim,R.anim.pop_enter_anim,R.anim.pop_exit_anim);
				fragmentTransaction.replace(R.id.fl_login_container, new Frag_SignUp()).addToBackStack(null).commit();
			}
		});
		return view;
	}
}
