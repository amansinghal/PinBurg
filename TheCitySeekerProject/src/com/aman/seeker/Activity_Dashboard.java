package com.aman.seeker;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.aman.fragments.Frag_Explore_Places;
import com.aman.fragments.TabHostFragmentTest;
import com.aman.utils.Config;
import com.aman.utils.MyTabHost;

public class Activity_Dashboard extends Activity implements OnClickListener, OnItemClickListener
{
	private ImageView iv_drawer_btn,iv_navigation_drawer_profile_picture;
	private SharedPreferences pref;
	private ArrayAdapter<String> adapter;
	private ListView lv_navigation_drawer_items;
	private FragmentManager fragmentManager = getFragmentManager();	
	public MenuDrawer mDrawer;
	private Handler handler;
	public MyTabHost myTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		
		if(!checkAlreadyLogin())
		{
			return;		
		}
		
		mDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW, Position.LEFT);
		
		mDrawer.setContentView(R.layout.activity_dashboard);
				
		mDrawer.setMenuView(R.layout.fragment_navigation_drawer);
						
		initViews();								
				
		adapter=new ArrayAdapter<String>(this,R.layout.drawer_text_view, new String[] {getString(R.string.title_pins),getString(R.string.title_explore),getString(R.string.title_my_alert_me) });
	}


	private void initViews()
	{	
		iv_drawer_btn = (ImageView)findViewById(R.id.activity_dashboard_iv_drawer_btn);
		iv_drawer_btn.setOnClickListener(this);
		lv_navigation_drawer_items = (ListView)mDrawer.findViewById(R.id.lv_navigation_drawer);
		iv_navigation_drawer_profile_picture = (ImageView)mDrawer.findViewById(R.id.iv_profile_image);
		iv_navigation_drawer_profile_picture.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,(int) (Config.getScreenSize(this)[1]/2.5)));
		adapter=new ArrayAdapter<String>(this,R.layout.drawer_text_view, new String[] {getString(R.string.title_pins),getString(R.string.title_tagmyfav),getString(R.string.title_my_alert_me) });
		lv_navigation_drawer_items.setAdapter(adapter);
		lv_navigation_drawer_items.setItemChecked(1, true);
		lv_navigation_drawer_items.setOnItemClickListener(this);
		getFragmentManager().beginTransaction().replace(R.id.activity_dashboard_container,new Frag_Explore_Places()).setCustomAnimations(R.anim.enter_anim,R.anim.exit_anim).commit();
		//(ll_drawer_layout.findViewById(R.id.navigation_shader)).setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,Config.getScreenSize(this)[1]/2));
		//ll_drawer_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,Config.getScreenSize(this)[1]/3));
	}

	private boolean checkAlreadyLogin()
	{
		pref=getSharedPreferences(Config.PREF_KEY, MODE_PRIVATE);

		if(!pref.getBoolean("isuserlogin", false))
		{
			if(!pref.getBoolean("rememberme", false))
			{
				pref.edit().putBoolean("isuserlogin", false).commit();
			}
			
			finish();
			
			startActivity(new Intent(this,LoginOptionsActivity.class));
			
			return false;
		}
		else
		{
			return true;
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.activity_dashboard_iv_drawer_btn:
			mDrawer.toggleMenu();
			iv_drawer_btn.startAnimation(getPopAnimation());
			break;
		}
	}

	public Animation getPopAnimation()
	{		

		Animation animation = new ScaleAnimation(0.6f,1.0f,0.6f,1.0f,Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);  	

		animation.setFillAfter(true);

		animation.setDuration(200);                      
		return animation;
	}

	public Animation rotateAnimationClock()
	{
		Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(500); 
		rotateAnimation.setInterpolator(new LinearInterpolator()); 
		rotateAnimation.setFillAfter(true); 
		return rotateAnimation;
	}

	public interface DrawerStatusListener
	{
		public void onDrawerStatusChangeListener(boolean isOpen); 
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1,final int arg2, long arg3) 
	{
		mDrawer.toggleMenu();
		handler = new Handler();		
		handler.postDelayed(new Runnable() 
		{			
			@Override
			public void run() 
			{			
				if(arg2 == 0)
				{
					getFragmentManager().beginTransaction().replace(R.id.activity_dashboard_container,new TabHostFragmentTest()).setCustomAnimations(R.anim.enter_anim,R.anim.exit_anim).commit();
				}
				if(arg2 == 1)
				{
					getFragmentManager().beginTransaction().replace(R.id.activity_dashboard_container,new Frag_Explore_Places()).setCustomAnimations(R.anim.enter_anim,R.anim.exit_anim).commit();
				}
			}
		},MenuDrawer.ANIMATION_DELAY+500);	
	}

}
