package com.aman.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import asynctasks.AddPinTask;
import asynctasks.AddressLatLongTask;
import asynctasks.CatagoryTask;
import asynctasks.FindPlaceTask;
import asynctasks.PlaceDetailsTask;

import com.aman.ModelClasses.PlaceModel;
import com.aman.seeker.DashBoard;
import com.aman.seeker.R;
import com.aman.utils.Config;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Frag_Tag_My_Burg extends Fragment implements OnClickListener 
{
	GoogleMap googleMap = null;

	ProgressDialog dialog;

	private static View view;

	AutoCompleteTextView autocomptv_search;

	ArrayList<PlaceModel> listPlaceModels=new ArrayList<>();

	ArrayList<String> dropdownContainer=new ArrayList<>();

	LinearLayout ll_tag_my_pin_confirm,ll_pin_detail_up,ll_pin_detail_down;

	ImageView iv_ok,iv_cancel,iv_add_pin_confirm,iv_add_pin_cancel,iv_pin_details_img1,iv_pin_details_img2,iv_pin_details_img3,iv_search;

	TextView tv_tag_my_pin;

	Spinner spnr_catagory;

	EditText et_pin_detail_address,et_pin_detail_name,et_pin_detail_description;

	String target_pin_lat="",target_pin_long="";

	SharedPreferences pref;	
	
	Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 	

	{
		// TODO Auto-generated method stub	
 		pref=getActivity().getSharedPreferences(Config.PREF_KEY, Context.MODE_PRIVATE);

		//view = inflater.inflate(R.layout.map_layout_tag_my_pins, container, false);
		
		if (view != null) 
		{			
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}

		try 
		{				
			view = inflater.inflate(R.layout.map_layout_tag_my_pins, container, false);
		}
		catch (InflateException e)
		{
			 //map is already there, just return view as it is 
		}
		context = getActivity();

		ll_tag_my_pin_confirm=(LinearLayout)view.findViewById(R.id.ll_tag_my_pin_confirm);

		ll_pin_detail_up=(LinearLayout)view.findViewById(R.id.ll_pin_detail_up);

		ll_pin_detail_down=(LinearLayout)view.findViewById(R.id.ll_pin_detail_down);

		spnr_catagory=(Spinner)view.findViewById(R.id.spnr_pin_detail_catagory);

		tv_tag_my_pin=(TextView)view.findViewById(R.id.tv_tag_my_pin_address);

		iv_ok=(ImageView)view.findViewById(R.id.iv_tag_my_pin_ok);

		iv_add_pin_confirm=(ImageView)view.findViewById(R.id.iv_add_pin_confirm);

		iv_add_pin_cancel=(ImageView)view.findViewById(R.id.iv_add_pin_cancel);

		iv_pin_details_img1=(ImageView)view.findViewById(R.id.iv_pin_detail_image1);

		iv_pin_details_img2=(ImageView)view.findViewById(R.id.iv_pin_detail_image2);

		iv_pin_details_img3=(ImageView)view.findViewById(R.id.iv_pin_detail_image3);

		iv_pin_details_img1.setLayoutParams(new LinearLayout.LayoutParams(Config.getScreenSize(getActivity())[0]/3,LinearLayout.LayoutParams.MATCH_PARENT));

		iv_pin_details_img2.setLayoutParams(new LinearLayout.LayoutParams(Config.getScreenSize(getActivity())[0]/3,LinearLayout.LayoutParams.MATCH_PARENT));

		iv_pin_details_img3.setLayoutParams(new LinearLayout.LayoutParams(Config.getScreenSize(getActivity())[0]/3,LinearLayout.LayoutParams.MATCH_PARENT));

		et_pin_detail_address=(EditText)view.findViewById(R.id.et_pin_detail_address);

		et_pin_detail_name=(EditText)view.findViewById(R.id.et_pin_detail_name);

		et_pin_detail_description=(EditText)view.findViewById(R.id.et_pin_detail_description);

		iv_cancel=(ImageView)view.findViewById(R.id.iv_tag_my_pin_cancel);

		autocomptv_search=(AutoCompleteTextView)view.findViewById(R.id.autocomptv_tag_burg);					

		autocomptv_search.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				for(String tempstr:dropdownContainer)
				{
					if(tempstr.equals(s.toString())||s.toString().isEmpty())
					{
						return;
					}
				}
				FindPlaceTask task=new FindPlaceTask(context, new FindPlaceTask.onTaskCompleteListener()
				{							
					@Override
					public void ontaskComplete(ArrayList<PlaceModel> listPlaces) 
					{
						listPlaceModels=listPlaces;
						if(listPlaces.size()>0)
						{
							dropdownContainer=new ArrayList<>();
							for(int i=0;i<listPlaces.size();i++)
							{
								dropdownContainer.add(listPlaces.get(i).desciption);
							}
							ArrayAdapter<String> adapter=new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, dropdownContainer);
							autocomptv_search.setAdapter(adapter);
							autocomptv_search.showDropDown();
						}								
					}
				});
				task.execute(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

		autocomptv_search.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) 
			{
				autocomptv_search.setVisibility(View.GONE);
				autocomptv_search.setText("");
				PlaceDetailsTask task=new PlaceDetailsTask(getActivity(), new PlaceDetailsTask.onTaskCompleteListener()
				{

					@Override
					public void ontaskComplete(HashMap<String, String> placeDetails) 
					{
						// TODO Auto-generated method stub
						Log.e("placeDetails",placeDetails.get("name"));
						target_pin_lat=placeDetails.get("lat");
						target_pin_long=placeDetails.get("lng");
						LatLng latLng=new LatLng(Double.parseDouble(placeDetails.get("lat")), Double.parseDouble(placeDetails.get("lng")));
						googleMap.clear();
						googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pin))));
						CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(18).build();
						googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
						ll_tag_my_pin_confirm.setVisibility(View.VISIBLE);
						tv_tag_my_pin.setText(placeDetails.get("name"));
						et_pin_detail_address.setText(placeDetails.get("name"));
					}
				});
				task.execute(listPlaceModels.get(arg2).reference);
			}
		});

		init();

		googleMap.setOnMapClickListener(new OnMapClickListener() 
		{			
			@Override
			public void onMapClick(final LatLng arg0) 
			{
				googleMap.clear();
				ll_tag_my_pin_confirm.setVisibility(View.INVISIBLE);					
				AddressLatLongTask adLatLongTask=new AddressLatLongTask(getActivity(),new  AddressLatLongTask.onTaskCompleteListener()
				{				
					@Override
					public void ontaskComplete(HashMap<String, String> placeDetails) 
					{				
						//googleMap.addMarker(new MarkerOptions().position(arg0).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pin))));
						autocomptv_search.setVisibility(View.GONE);
						ll_pin_detail_down.setVisibility(View.GONE);
						ll_pin_detail_up.setVisibility(View.GONE);
						target_pin_lat=""+arg0.latitude;
						target_pin_long=""+arg0.longitude;
						googleMap.addMarker(new MarkerOptions().position(arg0).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pin))));
						CameraPosition cameraPosition = new CameraPosition.Builder().target(arg0).zoom(googleMap.getCameraPosition().zoom).build();
						googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));							
						ll_tag_my_pin_confirm.setVisibility(View.VISIBLE);
						tv_tag_my_pin.setText(placeDetails.get("name"));
						et_pin_detail_address.setText(placeDetails.get("name"));
						/*for(int i=1;i<=360;i++)
						{
							LatLng latLng=Config.getSecondPoints(arg0.latitude, arg0.longitude, i);
							googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pin))));
						}*/

					}
				});
				adLatLongTask.execute(arg0.latitude+","+arg0.longitude);
			}
		});

		iv_ok.setOnClickListener(new OnClickListener()
		{				
			@Override
			public void onClick(View arg0) 
			{							
				getCatagory();
			}
		});

		iv_cancel.setOnClickListener(new OnClickListener()
		{				
			@Override
			public void onClick(View arg0) 
			{			
				ll_tag_my_pin_confirm.setVisibility(View.INVISIBLE);
				googleMap.clear();
				target_pin_lat="";
				target_pin_long="";
			}
		});

		iv_add_pin_confirm.setOnClickListener(new OnClickListener()
		{			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View arg0) 
			{
				AddPinTask task=new AddPinTask(getActivity(), new AddPinTask.onTaskCompleteListener()
				{					
					@Override
					public void ontaskComplete(boolean success) 
					{
						// TODO Auto-generated method stub
						if(success)
						{
							et_pin_detail_address.setText("");								
							ll_pin_detail_down.setVisibility(View.GONE);
							ll_pin_detail_up.setVisibility(View.GONE);
							et_pin_detail_name.setText("");
							et_pin_detail_description.setText("");
							et_pin_detail_address.setText("");
							target_pin_lat="";
							target_pin_long="";
							iv_pin_details_img1.setTag(null);
							iv_pin_details_img1.setImageDrawable(getResources().getDrawable(R.drawable.add_photo));
							iv_pin_details_img2.setTag(null);
							iv_pin_details_img2.setImageDrawable(getResources().getDrawable(R.drawable.add_photo));
							iv_pin_details_img3.setTag(null);
							iv_pin_details_img3.setImageDrawable(getResources().getDrawable(R.drawable.add_photo));
							googleMap.clear();
							Toast.makeText(getActivity(), "Successfully Pinned", Toast.LENGTH_LONG).show();
						}
						else
						{
							Config.alertDialogBox(getActivity(), (ActionBarActivity)getActivity(),null,"There is a problem while pin your location.", false);
						}
					}
				});
				if(Config.getText(et_pin_detail_address).isEmpty()&&Config.getText(et_pin_detail_description).isEmpty()&&Config.getText(et_pin_detail_name).isEmpty())
				{
					Config.alertDialogBox(getActivity(), (ActionBarActivity)getActivity(),null,"The entries must not be empty.", false);
				}
				else
				{						
					String tempPath1="",tempPath2="",tempPath3="";
					tempPath1=iv_pin_details_img1.getTag()!=null?((String)iv_pin_details_img1.getTag()):"";
					tempPath2=iv_pin_details_img2.getTag()!=null?((String)iv_pin_details_img1.getTag()):"";
					tempPath3=iv_pin_details_img3.getTag()!=null?((String)iv_pin_details_img1.getTag()):"";
					task.execute(target_pin_lat,target_pin_long,pref.getString("uid", ""),Config.getText(et_pin_detail_name),Config.getText(et_pin_detail_description)
							,Config.getText(et_pin_detail_address),((HashMap<String,String>)spnr_catagory.getSelectedItem()).get("id"),tempPath1
							,tempPath2,tempPath3);
				}
			}
		});

		iv_add_pin_cancel.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				googleMap.clear();
				ll_pin_detail_down.setVisibility(View.INVISIBLE);
				ll_pin_detail_up.setVisibility(View.INVISIBLE);
				et_pin_detail_name.setText("");
				et_pin_detail_description.setText("");
				et_pin_detail_address.setText("");
				target_pin_lat="";
				target_pin_long="";
				iv_pin_details_img1.setTag(null);
				iv_pin_details_img1.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_input_add));
				iv_pin_details_img2.setTag(null);
				iv_pin_details_img2.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_input_add));
				iv_pin_details_img3.setTag(null);
				iv_pin_details_img3.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_input_add));
			}
		});

		iv_pin_details_img1.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v) 
			{				
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				getActivity().startActivityForResult(i, DashBoard.IMAGE_SELECT_ADD_IMG1);	
			}
		});

		iv_pin_details_img2.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v) 
			{

				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				getActivity().startActivityForResult(i, DashBoard.IMAGE_SELECT_ADD_IMG2);	
			}
		});

		iv_pin_details_img3.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				getActivity().startActivityForResult(i, DashBoard.IMAGE_SELECT_ADD_IMG3);	

			}
		});

		googleMap.getUiSettings().setMyLocationButtonEnabled(false);

		googleMap.getUiSettings().setZoomControlsEnabled(false);

		googleMap.setMyLocationEnabled(true);

		if(googleMap.getMyLocation()==null)
		{
			dialog=ProgressDialog.show(getActivity(), null, "Pointing you on map.....");
		}

		googleMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener()
		{				
			@Override
			public void onMyLocationChange(Location arg0) 
			{
				// TODO Auto-generated method stub
				if(dialog!=null && dialog.isShowing())
				{
					dialog.dismiss();
					CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(arg0.getLatitude(),arg0.getLongitude())).zoom(10).build();
					googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				}
			}
		});

		googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
		{
			@Override
			public void onInfoWindowClick(Marker marker) 
			{
				// TODO Auto-generated method stub

			}
		});
		
		
		initForSearch();
		
		iv_search.setOnClickListener(this);

		return view;
	}

	private void initForSearch()
	{
		iv_search = (ImageView)getActivity().findViewById(R.id.activity_dashboard_iv_search);
		iv_search.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onDetach() 
	{
		super.onDetach();
		iv_search.setVisibility(View.GONE);
	}
	
	public void init()
	{		
		if (googleMap == null)
		{
			googleMap = ((MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map_tag_my_pins)).getMap();
		}
	}
	private void getCatagory()	
	{
		CatagoryTask task=new CatagoryTask(getActivity(), new CatagoryTask.onTaskCompleteListener()
		{		
			@Override
			public void ontaskComplete(ArrayList<HashMap<String, String>> listCatagory) 
			{
				if(!listCatagory.isEmpty())
				{
					SimpleAdapter adapter=new SimpleAdapter(getActivity(), listCatagory,R.layout.simple_text_2,new String[]{"name"},new int[]{R.id.text2});
					spnr_catagory.setAdapter(adapter);
					ll_tag_my_pin_confirm.setVisibility(View.INVISIBLE);
					ll_pin_detail_down.setVisibility(View.VISIBLE);
					ll_pin_detail_up.setVisibility(View.VISIBLE);
				}
				else
				{
					Config.alertDialogBox(getActivity(), (ActionBarActivity)getActivity(),null,"Problem while getting catagories.",true);
				}
			}
		});
		task.execute();
	}
	
	@Override
	public void onDestroy() 
	{		
		super.onDestroy();		
	}

	@Override
	public void onClick(View v) 
	{
		if(R.id.activity_dashboard_iv_search == v.getId())
		{
			if(iv_search.getTag() == null)
			{
				autocomptv_search.setVisibility(View.VISIBLE);
				iv_search.setTag(10);
			}
			else
			{
				autocomptv_search.setVisibility(View.GONE);
				iv_search.setTag(null);
			}
		}
		
	}
}
