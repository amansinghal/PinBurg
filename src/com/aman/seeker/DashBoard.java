package com.aman.seeker;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import asynctasks.UploadFileTask;

import com.aman.fragments.Frag_Alert_Me;
import com.aman.fragments.Frag_My_Profile;
import com.aman.fragments.Frag_My_Tagged_Pins;
import com.aman.fragments.Frag_NewsFeed;
import com.aman.fragments.Frag_Tag_My_Burg;
import com.aman.utils.Config;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DashBoard extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks 
{

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private CharSequence mTitle;
	TextView tv_title;
	final int SEARCH_MENU_ID_FIND_PIN=100,SEARCH_MENU_ID_TAG_PIN=101,CHANGE_MAPTYPE_ADD_PIN=102;
	SearchView SearchBox;
	SharedPreferences pref;
	NotificationService notificationService;
	public static final int IMAGE_SELECT=111,IMAGE_SELECT_ADD_IMG1=112,IMAGE_SELECT_ADD_IMG2=113,IMAGE_SELECT_ADD_IMG3=114;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		pref=getSharedPreferences(Config.PREF_KEY, MODE_PRIVATE);

		if(!pref.getBoolean("isuserlogin", false))
		{
			if(!pref.getBoolean("rememberme", false))
			{
				pref.edit().putBoolean("isuserlogin", false).commit();
			}
			startActivity(new Intent(this,LoginOptionsActivity.class));

			return;
		}
		setContentView(R.layout.activity_dash_board);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

		mTitle = getString(R.string.title_newsfeed);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));

		int titleId = getResources().getIdentifier("action_bar_title", "id","android");

		tv_title=(TextView) findViewById(titleId);

		tv_title.setPadding(5, 5, 5, 5);

		tv_title.setGravity(Gravity.CENTER);	

		//getSupportFragmentManager().beginTransaction().add(R.id.container,new Frag_My_Profile()).commit();
		startNotificationService();
	}

	public void startNotificationService() 
	{
		Intent notificationIntent=new Intent(this,NotificationService.class);

		startService(notificationIntent);

		bindService(notificationIntent, serviceConnection,Service.BIND_AUTO_CREATE);
		
		
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) 
	{
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();		
		onSectionAttached(position);
		switch (position)
		{
		case NavigationDrawerFragment.SHOW_PROFILE:
			fragmentManager.beginTransaction().replace(R.id.container,new Frag_My_Profile()).commit();
			break;
		case 0:
			fragmentManager.beginTransaction().replace(R.id.container,new Frag_NewsFeed()).commit();
			break;
		case 1:
			fragmentManager.beginTransaction().replace(R.id.container,new Frag_Tag_My_Burg()).commit();
			break;
		case 3:
			fragmentManager.beginTransaction().replace(R.id.container,new Frag_My_Tagged_Pins()).commit();
			break;
		case 4:
			fragmentManager.beginTransaction().replace(R.id.container,new Frag_Alert_Me()).commit();
			break;
		default:
			fragmentManager.beginTransaction().replace(R.id.container,PlaceholderFragment.newInstance(position + 1)).commit();
			break;
		}		
	}

	public void onSectionAttached(int number) 
	{
		switch (number) 
		{
		case NavigationDrawerFragment.SHOW_PROFILE:
			mTitle = getString(R.string.title_my_profile);
			break;
		case 0:
			mTitle = getString(R.string.title_newsfeed);
			break;
		case 1:
			mTitle = getString(R.string.title_tagmyfav);
			break;
		case 2:
			mTitle = getString(R.string.title_arroundme);
			break;
		case 3:
			mTitle = getString(R.string.title_my_tagged_pins);
			break;
		case 4:
			mTitle = getString(R.string.title_my_alert_me);
			break;

		}
	}

	public void restoreActionBar() 
	{
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{	
		if(mNavigationDrawerFragment!=null)
			if (!mNavigationDrawerFragment.isDrawerOpen()) 
			{
				// Only show items in the action bar relevant to this screen
				// if the drawer is not showing. Otherwise, let the drawer
				// decide what to show in the action bar
				if(mTitle.equals(getString(R.string.title_arroundme)))
				{
					menu.add(0,SEARCH_MENU_ID_FIND_PIN, 1, android.R.string.search_go).setIcon(android.R.drawable.ic_menu_search).setActionView(R.layout.action_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);				
				}
				else if(mTitle.equals(getString(R.string.title_tagmyfav)))
				{
					menu.add(0,SEARCH_MENU_ID_TAG_PIN, 1, android.R.string.search_go).setIcon(android.R.drawable.ic_menu_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
					menu.add(0,CHANGE_MAPTYPE_ADD_PIN, 1,"MAP-TYPE").setIcon(android.R.drawable.ic_menu_mapmode).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
				}
				else
				{
					getMenuInflater().inflate(R.menu.dash_board, menu);	
				}
				restoreActionBar();
				return true;
			}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) 
		{
		case SEARCH_MENU_ID_FIND_PIN:
			SearchBox = (SearchView) item.getActionView();
			SearchBox.requestFocus();
			int SearchBoxId = SearchBox.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
			TextView textView = (TextView) SearchBox.findViewById(SearchBoxId);
			textView.setTextColor(Color.WHITE);
			textView.setHintTextColor(Color.WHITE);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			break;
		case R.id.logout:	
			pref.edit().putBoolean("isuserlogin",false).commit();
			startActivity(new Intent(this,LoginOptionsActivity.class));
			Config.pinData=null;
			Config.myPinData=null;
			finish();
			break;
		case SEARCH_MENU_ID_TAG_PIN:
			AutoCompleteTextView temp=(AutoCompleteTextView)findViewById(R.id.autocomptv_tag_burg);
			if(temp.getVisibility()==View.VISIBLE)
			{
				temp.setVisibility(View.GONE);				
				Config.hideKeyboard(DashBoard.this, DashBoard.this);
			}
			else
			{				
				temp.setVisibility(View.VISIBLE);
				((LinearLayout)findViewById(R.id.ll_tag_my_pin_confirm)).setVisibility(View.GONE);
				temp.requestFocus();
			}
			break;
		case CHANGE_MAPTYPE_ADD_PIN:
			GoogleMap googleMap=((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_tag_my_pins)).getMap();
			switch (googleMap.getMapType())
			{
			case GoogleMap.MAP_TYPE_HYBRID:
				googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
				break;
			case GoogleMap.MAP_TYPE_NONE:
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			case GoogleMap.MAP_TYPE_NORMAL:
				googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case GoogleMap.MAP_TYPE_SATELLITE:
				googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				break;
			case GoogleMap.MAP_TYPE_TERRAIN:
				googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	public static class PlaceholderFragment extends Fragment 
	{
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) 
		{
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() 
		{

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
		{
			View rootView = inflater.inflate(R.layout.fragment_dash_board,container, false);

			return rootView;
		}

		@Override
		public void onAttach(Activity activity) 
		{
			super.onAttach(activity);

			//((DashBoard) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		switch (requestCode) 
		{
		case IMAGE_SELECT:
			if (resultCode == RESULT_OK)
			{
				if(data.getData()!=null)
				{
					Uri uri=data.getData();
					String[] filePathColumn = {MediaColumns.DATA};
					Cursor cursor = getContentResolver().query(data.getData(),filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();					
					if(picturePath!=null)
					{
						Log.e("URI", picturePath);	

						try
						{						
							picturePath=Config.compressImage(picturePath);
						}
						catch(Exception e)
						{
							Config.alertDialogBox(DashBoard.this, DashBoard.this, null,"This is not a valid image.", false);
							return;
						}
						UploadFileTask task=new UploadFileTask(DashBoard.this, new UploadFileTask.onTaskCompleteListener()
						{			
							@Override
							public void ontaskComplete(String response) 
							{		
								try
								{									
									JSONObject object=new JSONObject(response);
									if(object.getString("error").equals("0"))
									{
										final ImageView img_profile=((ImageView)findViewById(R.id.iv_profile_image));

										img_profile.setImageDrawable(null);

										pref.edit().putString("photo_path","http://www.pinburg.com"+object.getString("path")).commit();

										Config.showImageFromUrl( pref.getString("photo_path", ""),img_profile);																											
									}
									else
									{
										switch (object.getString("error_code"))
										{
										case "101":

											Config.alertDialogBox(DashBoard.this, DashBoard.this, null, object.getString("error_msg"), false);

											break;
										case "102":

											Config.alertDialogBox(DashBoard.this, DashBoard.this, null, object.getString("error_msg"), false);

											break;

										case "103":

											Config.alertDialogBox(DashBoard.this, DashBoard.this, null, object.getString("error_msg"), false);

											break;
										case "104":

											Config.alertDialogBox(DashBoard.this, DashBoard.this, null, object.getString("error_msg"), false);

											break;
										}
									}
								}
								catch(Exception e)
								{
									e.printStackTrace();
									Config.alertDialogBox(DashBoard.this, DashBoard.this, null,"Internet connection error.", false);
								}
							}
						});
						task.execute(picturePath,pref.getString("uid", ""));
					}
					else
					{
						Config.alertDialogBox(DashBoard.this, DashBoard.this, null,"seleted option is not suporrted please choose another option to select.",true);
					}
				}								
			} 
			break;
		case IMAGE_SELECT_ADD_IMG1:
			if (resultCode == RESULT_OK)
			{
				if(data.getData()!=null)
				{
					Uri uri=data.getData();
					String[] filePathColumn = {MediaColumns.DATA};
					Cursor cursor = getContentResolver().query(data.getData(),filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();					
					if(picturePath!=null)
					{
						Log.e("URI", picturePath);	

						try
						{						
							picturePath=Config.compressImage(picturePath);	
							ImageView imageView=((ImageView)findViewById(R.id.iv_pin_detail_image1));
							imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
							imageView.setTag(picturePath);
						}
						catch(Exception e)
						{
							Config.alertDialogBox(DashBoard.this, DashBoard.this, null,"This is not a valid image.", false);
							return;
						}

					}
					else
					{
						Config.alertDialogBox(DashBoard.this, DashBoard.this, null,"seleted option is not suporrted please choose another option to select.",true);
					}
				}								
			} 
			break;
		case IMAGE_SELECT_ADD_IMG2:
			if (resultCode == RESULT_OK)
			{
				if(data.getData()!=null)
				{
					Uri uri=data.getData();
					String[] filePathColumn = {MediaColumns.DATA};
					Cursor cursor = getContentResolver().query(data.getData(),filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();					
					if(picturePath!=null)
					{
						Log.e("URI", picturePath);	

						try
						{						
							picturePath=Config.compressImage(picturePath);	
							ImageView imageView=((ImageView)findViewById(R.id.iv_pin_detail_image2));
							imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
							imageView.setTag(picturePath);
						}
						catch(Exception e)
						{
							Config.alertDialogBox(DashBoard.this, DashBoard.this, null,"This is not a valid image.", false);
							return;
						}

					}
					else
					{
						Config.alertDialogBox(DashBoard.this, DashBoard.this, null,"seleted option is not suporrted please choose another option to select.",true);
					}
				}								
			} 
			break;
		case IMAGE_SELECT_ADD_IMG3:
			if (resultCode == RESULT_OK)
			{
				if(data.getData()!=null)
				{
					Uri uri=data.getData();
					String[] filePathColumn = {MediaColumns.DATA};
					Cursor cursor = getContentResolver().query(data.getData(),filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();					
					if(picturePath!=null)
					{
						Log.e("URI", picturePath);	

						try
						{						
							picturePath=Config.compressImage(picturePath);	
							ImageView imageView=((ImageView)findViewById(R.id.iv_pin_detail_image3));
							imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
							imageView.setTag(picturePath);
						}
						catch(Exception e)
						{
							Config.alertDialogBox(DashBoard.this, DashBoard.this, null,"This is not a valid image.", false);
							return;
						}

					}
					else
					{
						Config.alertDialogBox(DashBoard.this, DashBoard.this, null,"seleted option is not suporrted please choose another option to select.",true);
					}
				}								
			} 
			break;

		default:
			break;
		}
	}

	private ServiceConnection serviceConnection=new ServiceConnection()
	{

		@Override
		public void onServiceDisconnected(ComponentName arg0) 
		{
			System.out.println("onServiceDisconnected:"+arg0);
			notificationService=null;			
		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder) 
		{
			System.out.println("onServiceConnected:"+arg0);
			notificationService=((NotificationService.NotiBinder)binder).getService();
			notificationService.registerLocationService();
		}
	};
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
	}
}
