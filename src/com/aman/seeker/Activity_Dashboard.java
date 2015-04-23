package com.aman.seeker;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aman.fragments.Frag_NewsFeed;
import com.aman.fragments.Frag_Explore_Places;
import com.aman.fragments.TabHostFragmentTest;
import com.aman.seeker.DashBoard.PlaceholderFragment;
import com.aman.utils.Config;
import com.aman.utils.MyTabHost;
import com.aman.utils.MyTabHost.FragInfo;
import com.aman.utils.MyTabHost.MyTabHostException;
import com.aman.utils.MyTabHost.onTabClickListener;

public class Activity_Dashboard extends Activity implements OnClickListener, onTabClickListener
{
	private ImageView iv_drawer_btn;
	private LinearLayout ll_drawer_layout;
	private SharedPreferences pref;
	private ListView lv_navigation_drawer;
	private ArrayAdapter<String> adapter;
	private FragmentManager fragmentManager = getFragmentManager();
	DrawerStatusListener drawerStatusListener;
	private MyTabHost myTabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		
		if(!checkAlreadyLogin())
		{
			return;		
		}
		
		setContentView(R.layout.activity_dashboard);
		
		initViews();
		
		try 
		{
			myTabHost.addTabs(new Frag_NewsFeed(), "New Pins",getResources().getDrawable(R.drawable.heart));
			myTabHost.addTabs(new Frag_Explore_Places(), "Explore",getResources().getDrawable(R.drawable.been_here));
			myTabHost.addTabs(PlaceholderFragment.newInstance(3), "Title3",getResources().getDrawable(R.drawable.review));
		} 
		catch (MyTabHostException e)
		{
			e.printStackTrace();
		}
		myTabHost.setTabClickListener(this);
		myTabHost.setDefaultFragment(1);
		adapter=new ArrayAdapter<String>(this,R.layout.drawer_text_view, new String[] {getString(R.string.title_pins),getString(R.string.title_tagmyfav),getString(R.string.title_my_alert_me) });
		lv_navigation_drawer.setAdapter(adapter);
		lv_navigation_drawer.setItemChecked(0, true);
	}

	public void setDrawerStatusChangeListesner(DrawerStatusListener drawerStatusListener)
	{
		this.drawerStatusListener = drawerStatusListener;
	}

	private void initViews()
	{
		myTabHost=(MyTabHost)findViewById(R.id.myTabHost1);
		myTabHost.setCircleColor(getResources().getColor(R.color.list_item_background), Color.WHITE);
		iv_drawer_btn = (ImageView)findViewById(R.id.activity_dashboard_iv_drawer_btn);
		iv_drawer_btn.setOnClickListener(this);
		ll_drawer_layout = (LinearLayout)findViewById(R.id.navigation_drawer);
		ll_drawer_layout.setOnClickListener(this);
		ll_drawer_layout.setTag(5);
		((ImageView)ll_drawer_layout.findViewById(R.id.iv_profile_image)).setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,Config.getScreenSize(this)[1]/3));
		//(ll_drawer_layout.findViewById(R.id.navigation_shader)).setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,Config.getScreenSize(this)[1]/2));
		//ll_drawer_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,Config.getScreenSize(this)[1]/3));
		lv_navigation_drawer = (ListView)findViewById(R.id.lv_navigation_drawer);		
		Config.collapse(ll_drawer_layout);
	}
	
	@Override
	public void onTabClick(View v, int position, ArrayList<FragInfo> tagItems)
	{
		if(isDrawerOpen())
		{
			closeDrawer();
		}
		getFragmentManager().beginTransaction().replace(R.id.activity_dashboard_container,tagItems.get(position).fragment).setCustomAnimations(R.anim.enter_anim,R.anim.exit_anim).commit();		
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
			manageDrawer();
			break;
		}
	}

	public void manageDrawer()
	{
		if(!isDrawerOpen())
		{
			openDrawer();
		}
		else
		{
			closeDrawer();
		}
	}

	public boolean isDrawerOpen()
	{
		if(ll_drawer_layout.getTag() != null)
		{
			if(drawerStatusListener != null)
				drawerStatusListener.onDrawerStatusChangeListener(true);
			return false;
		}
		else
		{
			if(drawerStatusListener != null)
				drawerStatusListener.onDrawerStatusChangeListener(false);
			return true;			
		}
	}

	public void openDrawer() 
	{
		//rotateAnimationClock();
		iv_drawer_btn.startAnimation(getPopAnimation());
		Config.expand(ll_drawer_layout);
		ll_drawer_layout.setTag(null);		
		findViewById(R.id.navigation_shader).setOnTouchListener(onTouchListener);
		//iv_drawer_btn.setImageDrawable(getResources().getDrawable(R.drawable.drawer_open));
	}

	OnTouchListener onTouchListener = new OnTouchListener()
	{		
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			manageDrawer();
			return false;
		}
	};

	public void closeDrawer()
	{
		iv_drawer_btn.startAnimation(getPopAnimation());
		Config.collapse(ll_drawer_layout);
		ll_drawer_layout.setTag(5);
		findViewById(R.id.navigation_shader).setOnTouchListener(null);
		//iv_drawer_btn.setImageDrawable(getResources().getDrawable(R.drawable.drawer_close));
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

}
