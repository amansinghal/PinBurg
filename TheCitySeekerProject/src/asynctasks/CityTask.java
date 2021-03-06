package asynctasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aman.ModelClasses.CountryStateCity;
import com.aman.utils.Config;
import com.aman.utils.CustomProgressDialog;
import com.aman.utils.RestClient;
import com.aman.utils.RestClient.RequestMethod;

import android.content.Context;
import android.os.AsyncTask;

public class CityTask extends AsyncTask<String, Void, Integer>
{
	Context context;
	CustomProgressDialog dialog;
	public static onTaskCompleteListener callbackListener;
	String response="";
	ArrayList<CountryStateCity> listCity;
	public CityTask(Context context,onTaskCompleteListener listener) 
	{
		this.context=context;
		callbackListener=listener;
		listCity=new ArrayList<>();
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
	protected Integer doInBackground(String... params) 
	{
		// TODO Auto-generated method stub
		RestClient client=new RestClient(Config.APILink);
		client.AddParam("tag","getCity");	
		client.AddParam("stateid",params[0]);
		try 
		{
			client.Execute(RequestMethod.POST);
			response=client.getResponse();
			JSONObject jsonObject=new JSONObject(response);
			if (jsonObject.getString("error").equals("0"))
			{				
				JSONArray array=jsonObject.getJSONArray("City");
				if(array.length()>0)
				{
					for(int i=0;i<array.length();i++)
					{
						CountryStateCity obj=new CountryStateCity();
						obj.Id=array.getJSONObject(i).getString("id");
						obj.Name=array.getJSONObject(i).getString("name");
						listCity.add(obj);
					}
					return 1;
				}
				else
				{
					return 0;
				}
			}
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
		callbackListener.ontaskComplete(listCity);
	}

	public static interface onTaskCompleteListener
	{
		void ontaskComplete(ArrayList<CountryStateCity> citylist);
	}
}
