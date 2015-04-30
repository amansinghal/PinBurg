package com.aman.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.aman.seeker.R;

public class GPSTracker implements LocationListener
{
	private final Context mContext;

	//flag for GPS Status
	boolean isGPSEnabled = false;

	//flag for network status
	boolean isNetworkEnabled = false;

	boolean canGetLocation = false;

	public Location location;
	double latitude;
	double longitude;

	//The minimum distance to change updates in metters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; //10 metters

	//The minimum time beetwen updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

	//Declaring a Location Manager
	protected LocationManager locationManager;

	public ProgressDialog dialog;

	public GPSTracker(Context context) 
	{
		this.mContext = context;
		getLocation();
	}

	public Location getLocation()
	{
		dialog = ProgressDialog.show(mContext, null, "Getting your current location..",false,false);
		try
		{
			locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

			//getting GPS status
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			//getting network status
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled)
			{
				// no network provider is enabled
			}
			else
			{
				this.canGetLocation = true;

				//First get location from Network Provider
				if (isNetworkEnabled)
				{
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

					Log.d("Network", "Network");

					if (locationManager != null)
					{
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						updateGPSCoordinates();
					}
					
					//return location;
					
				}

				//if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled)
				{
					if (location == null)
					{
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

						Log.d("GPS Enabled", "GPS Enabled");

						if (locationManager != null)
						{
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							updateGPSCoordinates();
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			Log.e("Error : Location", "Impossible to connect to LocationManager", e);
		}
		return location;
	}

	public void updateGPSCoordinates()
	{
		if (location != null)
		{
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			dialog.dismiss();
			Log.e("Latitude", ""+latitude);
			Log.e("Longitude", ""+longitude);
		}
	}
	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 */

	public void stopUsingGPS()
	{
		if (locationManager != null) 
		{
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	/**
	 * Function to get latitude
	 */
	public double getLatitude()
	{
		if (location != null)
		{
			latitude = location.getLatitude();
		}

		return latitude;
	}

	/**
	 * Function to get longitude
	 */
	public double getLongitude()
	{
		if (location != null)
		{
			longitude = location.getLongitude();
		}

		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 */
	public boolean canGetLocation()
	{
		getLocation();
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog
	 */
	public void showSettingsAlert()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		//Setting Dialog Title
		alertDialog.setTitle("GPS Alert");

		//Setting Dialog Message
		alertDialog.setMessage("Please turn on the location services.");

		//On Pressing Setting button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() 
		{   
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});

		//On pressing cancel button
		alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() 
		{   
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});

		alertDialog.show();
	}
	
	

	/**
	 * Get list of address by latitude and longitude
	 * @return null or List<Address>
	 */
	public List<Address> getGeocoderAddress(Context context,Location location)
	{
		if (location != null)
		{
			Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
			try 
			{
				List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				return addresses;
			} 
			catch (IOException e) 
			{
				//e.printStackTrace();
				Log.e("Error : Geocoder", "Impossible to connect to Geocoder", e);
			}
		}

		return null;
	}

	
	/**
	 * Try to get AddressLine
	 * @return null or addressLine
	 */
	public String getAddressLine(Context context)
	{
		List<Address> addresses = getGeocoderAddress(context,this.location);
		if (addresses != null && addresses.size() > 0)
		{
			Address address = addresses.get(0);
			String addressLine = address.getAddressLine(0);

			return addressLine;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Try to get Locality
	 * @return null or locality
	 */
	public String getLocality(Context context)
	{
		List<Address> addresses = getGeocoderAddress(context,this.location);
		if (addresses != null && addresses.size() > 0)
		{
			Address address = addresses.get(0);
			String locality = address.getLocality();

			return locality;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Try to get Postal Code
	 * @return null or postalCode
	 */
	public String getPostalCode(Context context)
	{
		List<Address> addresses = getGeocoderAddress(context,this.location);
		if (addresses != null && addresses.size() > 0)
		{
			Address address = addresses.get(0);
			String postalCode = address.getPostalCode();

			return postalCode;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Try to get CountryName
	 * @return null or postalCode
	 */
	public String getCountryName(Context context)
	{
		List<Address> addresses = getGeocoderAddress(context,this.location);
		if (addresses != null && addresses.size() > 0)
		{
			Address address = addresses.get(0);
			String countryName = address.getCountryName();

			return countryName;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void onLocationChanged(Location location) 
	{   
		this.location = location;
		updateGPSCoordinates();
	}

	@Override
	public void onProviderDisabled(String provider) 
	{   
	}

	@Override
	public void onProviderEnabled(String provider) 
	{   
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{   
	}
}