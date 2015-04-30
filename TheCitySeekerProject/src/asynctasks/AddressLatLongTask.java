package asynctasks;

import java.util.HashMap;

import org.json.JSONObject;

import com.aman.utils.CustomProgressDialog;
import com.aman.utils.RestClient;
import com.aman.utils.RestClient.RequestMethod;

import android.content.Context;
import android.os.AsyncTask;

public class AddressLatLongTask extends AsyncTask<String, Void, Integer>
{
	Context context;
	CustomProgressDialog dialog;
	public static onTaskCompleteListener callbackListener;
	String response="";
	HashMap<String, String> placeDetails;
	public AddressLatLongTask(Context context,onTaskCompleteListener listener) 
	{
		this.context=context;
		callbackListener=listener;
	}

	@Override
	protected void onPreExecute() 
	{
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog=new CustomProgressDialog(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		placeDetails=new HashMap<>();
		dialog.show();
	}

	@Override
	protected Integer doInBackground(String... params) 
	{
		// TODO Auto-generated method stub
		 
        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyDGrAgt_GF5km6WlFEUjkZhd9Ucgi-U7Js";

        // reference of place
        String reference = "latlng="+params[0];
 
        // Sensor enabled
        String sensor = "sensor=false";
 
        // Building the parameters to the web service
        String parameters = reference+"&"+sensor+"&"+key;
 
        // Output format
        String output = "json";
 
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/geocode/"+output+"?"+parameters;
        
		RestClient client=new RestClient(url);		
		try 
		{
			client.Execute(RequestMethod.POST);
			response=client.getResponse();
			JSONObject j=new JSONObject(response);
			j=j.getJSONArray("results").getJSONObject(0);
			placeDetails.put("name", j.getString("formatted_address"));						
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
		callbackListener.ontaskComplete(placeDetails);
	}

	public static interface onTaskCompleteListener
	{
		void ontaskComplete(HashMap<String, String> placeDetails);
	}
}
