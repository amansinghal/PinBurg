package asynctasks;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;

import com.aman.utils.CustomProgressDialog;

public class GetAddressFromLocationTask extends AsyncTask<Double, Void, Integer>
{
	Context context;
	CustomProgressDialog dialog;
	public static onTaskCompleteListener callbackListener;
	String response="";
	List<Address> listAddress;
	public GetAddressFromLocationTask(Context context,onTaskCompleteListener listener) 
	{
		this.context=context;
		callbackListener=listener;
		listAddress=new ArrayList<>();
	}

	@Override
	protected void onPreExecute() 
	{
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog=new CustomProgressDialog(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected Integer doInBackground(Double... params) 
	{
		// TODO Auto-generated method stub		
		try 
		{
			listAddress = getStringFromLocation(params[0],params[1]);
			return 1;
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
	}

	protected void onPostExecute(Integer result) 
	{
		dialog.dismiss();
		callbackListener.ontaskComplete(listAddress);
	}

	public static interface onTaskCompleteListener
	{
		void ontaskComplete(List<Address> listAddress);
	}
	
	public List<Address> getStringFromLocation(double lat, double lng)throws ClientProtocolException, IOException, JSONException
	{

		String address = String.format(Locale.ENGLISH,"http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="+Locale.getDefault().getCountry(), lat, lng);
		Log.e("Requesting URL----->>>", address);
		HttpGet httpGet = new HttpGet(address);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		List<Address> retList = null;

		response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream stream = entity.getContent();
		int b;
		while ((b = stream.read()) != -1)
		{
			stringBuilder.append((char) b);
		}

		Log.e(getClass().getName(), stringBuilder.toString());
		
		JSONObject jsonObject = new JSONObject(stringBuilder.toString());

		retList = new ArrayList<Address>();

		if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) 
		{
			JSONArray results = jsonObject.getJSONArray("results");
			for (int i = 0; i < results.length(); i++) 
			{
				JSONObject result = results.getJSONObject(i);
				String indiStr = result.getString("formatted_address");
				Address addr = new Address(Locale.getDefault());
				addr.setAddressLine(0, indiStr);
				retList.add(addr);
			}    	    
		}
		return retList;    	    
	}
}
