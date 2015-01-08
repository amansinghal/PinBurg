package asynctasks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aman.ModelClasses.CountryStateCity;
import com.aman.utils.Config;
import com.aman.utils.CustomProgressDialog;
import com.aman.utils.RestClient;
import com.aman.utils.RestClient.RequestMethod;

import android.content.Context;
import android.os.AsyncTask;

public class CatagoryTask extends AsyncTask<String, Void, Integer>
{
	Context context;
	CustomProgressDialog dialog;
	public static onTaskCompleteListener callbackListener;
	String response="";
	ArrayList<HashMap<String, String>> listCatagory;
	public CatagoryTask(Context context,onTaskCompleteListener listener) 
	{
		this.context=context;
		callbackListener=listener;
		listCatagory=new ArrayList<>();
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
		client.AddParam("tag","getCategory");
		if(params[0]!=null && !params[0].isEmpty())
		{
			client.AddParam("uid",params[0]);
		}
		try 
		{
			client.Execute(RequestMethod.POST);
			response=client.getResponse();
			JSONObject jsonObject=new JSONObject(response);
			if (jsonObject.getString("error").equals("0"))
			{				
				JSONArray array=jsonObject.getJSONArray("catagory");
				if(array.length()>0)
				{
					for(int i=0;i<array.length();i++)
					{
						HashMap<String, String> map=new HashMap<>();
						map.put("name", array.getJSONObject(i).getString("name"));
						map.put("id", array.getJSONObject(i).getString("id"));
						map.put("isSelected",array.getJSONObject(i).getString("selected"));
						listCatagory.add(map);
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
		callbackListener.ontaskComplete(listCatagory);
	}

	public static interface onTaskCompleteListener
	{
		void ontaskComplete(ArrayList<HashMap<String, String>> listCatagory );
	}
}
