package com.aman.seeker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aman.utils.Config;

public class NavigationDrawerFragment extends Fragment 
{

	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	private NavigationDrawerCallbacks mCallbacks;

	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;

	public int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;

	public static final int SHOW_PROFILE=101; 
	
	TextView tv_profile_pic;
	
	ArrayAdapter<String> adapter;

	
	ImageView iv_profile_picture;
	FrameLayout fl_profile;
	SharedPreferences pref;
	public NavigationDrawerFragment() 
	{
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) 
		{
			mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);

			mFromSavedInstanceState = true;
		}

		// Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		
		View v=inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
		
		pref=getActivity().getSharedPreferences(Config.PREF_KEY, Context.MODE_PRIVATE);
		
		tv_profile_pic=(TextView)v.findViewById(R.id.tv_profile_name);
		
		tv_profile_pic.setText(pref.getString("name", ""));
		
		iv_profile_picture=(ImageView)v.findViewById(R.id.iv_profile_image);			
		
		iv_profile_picture.setImageDrawable(null);
		
		Config.showImageFromUrl(pref.getString("photo_path", ""),iv_profile_picture);	
		
		fl_profile=(FrameLayout)v.findViewById(R.id.fl_profile);
		
		fl_profile.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,Config.getScreenSize(getActivity())[1]/4));
		
		fl_profile.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				if (mCallbacks != null) 
				{
					mDrawerLayout.closeDrawer(mFragmentContainerView);
					mDrawerLayout.postDelayed(new Runnable()
					{					
						@Override
						public void run() 
						{
							// TODO Auto-generated method stub
							selectItem(SHOW_PROFILE);
						}
					},300);
				}
			}
		});
		
		mDrawerListView = (ListView)v.findViewById(R.id.lv_navigation_drawer);
									
		mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,final int position, long id) 
			{
				if (mDrawerLayout != null) 
				{
					mDrawerLayout.closeDrawer(mFragmentContainerView);
					mDrawerLayout.postDelayed(new Runnable()
					{					
						@Override
						public void run() 
						{
							// TODO Auto-generated method stub
							selectItem(position);
						}
					},300);
				}				
			}
		});

		adapter=new ArrayAdapter<String>(getActivity(),R.layout.drawer_text_view, new String[] {getString(R.string.title_pins),getString(R.string.title_tagmyfav),getString(R.string.title_my_alert_me) });

		mDrawerListView.setAdapter(adapter);

		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		
		//selectItem(mCurrentSelectedPosition);

		return v;
	}

	public boolean isDrawerOpen() 
	{
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) 
	{
		mFragmentContainerView = getActivity().findViewById(fragmentId);

		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
				R.string.navigation_drawer_open, /*
				 * "open drawer" description for
				 * accessibility
				 */
				R.string.navigation_drawer_close /*
				 * "close drawer" description for
				 * accessibility
				 */
				)
		{
			@Override
			public void onDrawerClosed(View drawerView)
			{
				super.onDrawerClosed(drawerView);
				if (!isAdded()) 
				{
					return;
				}

				getActivity().supportInvalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) 
			{
				super.onDrawerOpened(drawerView);
				if (!isAdded())
				{
					return;
				}

				if (!mUserLearnedDrawer) 
				{
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
				}

				getActivity().supportInvalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) 
		{
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() 
		{
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(int position) 
	{
		mCurrentSelectedPosition = position;		
		if (mDrawerListView != null) 
		{
			mDrawerListView.setItemChecked(position, true);
		}	
		if (mCallbacks != null) 
		{
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try 
		{
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} 
		catch (ClassCastException e) 
		{
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() 
	{
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		if (mDrawerLayout != null && isDrawerOpen()) 
		{
			inflater.inflate(R.menu.dash_board, menu);
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mDrawerToggle.onOptionsItemSelected(item)) 
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() 
	{
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.app_name);
	}

	private ActionBar getActionBar() 
	{
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks
	{
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}
}
