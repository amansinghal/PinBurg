package com.aman.seeker;

import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

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
	private ImageView iv_drawer_btn,iv_navigation_drawer_profile_picture;
	private SharedPreferences pref;
	private ArrayAdapter<String> adapter;
	private ListView lv_navigation_drawer_items;
	private FragmentManager fragmentManager = getFragmentManager();
	DrawerStatusListener drawerStatusListener;
	private MyTabHost myTabHost;
	public MenuDrawer mDrawer;
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
		lv_navigation_drawer_items = (ListView)mDrawer.findViewById(R.id.lv_navigation_drawer);
		iv_navigation_drawer_profile_picture = (ImageView)mDrawer.findViewById(R.id.iv_profile_image);
		iv_navigation_drawer_profile_picture.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,(int) (Config.getScreenSize(this)[1]/2.5)));
		adapter=new ArrayAdapter<String>(this,R.layout.drawer_text_view, new String[] {getString(R.string.title_pins),getString(R.string.title_tagmyfav),getString(R.string.title_my_alert_me) });
		lv_navigation_drawer_items.setAdapter(adapter);
		lv_navigation_drawer_items.setItemChecked(0, true);
		//(ll_drawer_layout.findViewById(R.id.navigation_shader)).setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,Config.getScreenSize(this)[1]/2));
		//ll_drawer_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,Config.getScreenSize(this)[1]/3));
	}
	
	@Override
	public void onTabClick(View v, int position, ArrayList<FragInfo> tagItems)
	{		
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

}