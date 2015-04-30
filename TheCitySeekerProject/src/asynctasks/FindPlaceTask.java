package asynctasks;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aman.ModelClasses.PlaceModel;
import com.aman.utils.CustomProgressDialog;
import com.aman.utils.RestClient;
import com.aman.utils.RestClient.RequestMethod;

import android.content.Context;
import android.os.AsyncTask;

public class FindPlaceTask extends AsyncTask<String, Void, Integer>
{
	Context context;
	CustomProgressDialog dialog;
	public static onTaskCompleteListener callbackListener;
	String response="";
	ArrayList<PlaceModel> listPlaces;
	public FindPlaceTask(Context context,onTaskCompleteListener listener) 
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
		listPlaces=new ArrayList<>();
		//dialog.show();
	}

	@Override
	protected Integer doInBackground(String... params) 
	{
		// TODO Auto-generated method stub
		 
        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyDGrAgt_GF5km6WlFEUjkZhd9Ucgi-U7Js";

        String input="";

        try 
        {
            input = "input=" + URLEncoder.encode(params[0], "utf-8");
        } 
        catch (UnsupportedEncodingException e1) 
        {
            e1.printStackTrace();
        }

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input+"&"+types+"&"+sensor+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

		RestClient client=new RestClient(url);		
		try 
		{
			client.Execute(RequestMethod.POST);
			response=client.getResponse();
			JSONArray jsonArray=new JSONObject(response).getJSONArray("predictions");			
			if(jsonArray.length()>0)
			{
					for(int i=0;i<jsonArray.length();i++)
					{
						PlaceModel model=new PlaceModel();
						model.desciption=jsonArray.getJSONObject(i).getString("description");
						model.Id=jsonArray.getJSONObject(i).getString("id");
						model.placeId=jsonArray.getJSONObject(i).getString("place_id");
						model.reference=jsonArray.getJSONObject(i).getString("reference");
						listPlaces.add(model);
					}
					return 1;
			}
			else
			{
					return 0;
			}
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
		//dialog.dismiss();
		callbackListener.ontaskComplete(listPlaces);
	}

	public static interface onTaskCompleteListener
	{
		void ontaskComplete(ArrayList<PlaceModel> listPlaces);
	}
}
