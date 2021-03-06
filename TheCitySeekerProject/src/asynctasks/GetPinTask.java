package asynctasks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.aman.ModelClasses.Pin;
import com.aman.seeker.R;
import com.aman.utils.Config;
import com.aman.utils.CustomProgressBarStyle;
import com.aman.utils.CustomProgressDialog;
import com.aman.utils.RestClient;
import com.aman.utils.RestClient.RequestMethod;

public class GetPinTask extends AsyncTask<String, Void, Integer>
{
	Context context;
	CustomProgressDialog dialog;
	public static onTaskCompleteListener callbackListener;
	String response="";
	ArrayList<Pin> pinData;
	boolean needProgressDialog;
	CustomProgressBarStyle barStyle;
	private View view;
	public GetPinTask(Context context,View view,onTaskCompleteListener listener,boolean needProgressDialog) 
	{
		this.context=context;
		callbackListener=listener;
		pinData=new ArrayList<>();
		this.needProgressDialog=needProgressDialog;	
		barStyle=new CustomProgressBarStyle(view,context);
		this.view = view;
	}

	@Override
	protected void onPreExecute() 
	{
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog=new CustomProgressDialog(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		//view.setVisibility(View.GONE);
		//barStyle.startProgress(context,"Getting pins please wait...",context.getResources().getColor(R.color.activated_listitem_color));
		if(needProgressDialog)
		dialog.show();
	}

	@Override
	protected Integer doInBackground(String... params) 
	{
		// TODO Auto-generated method stub
		RestClient client=new RestClient(Config.APILink);
		client.AddParam("tag","getNearByPinsWithFilter");	
		client.AddParam("lat",params[0]);
		client.AddParam("long",params[1]);
		client.AddParam("range", params[2]);
		client.AddParam("category", params[3]);		
		try 
		{
			client.Execute(RequestMethod.POST);
			response=client.getResponse();
			JSONObject jsonObject=new JSONObject(response);
			if (jsonObject.getString("success").equals("1"))
			{				
				JSONArray array=jsonObject.getJSONArray("result");
				if(array.length()>0)
				{
					for(int i=0;i<array.length();i++)
					{
						JSONObject temp=array.getJSONObject(i);
						Pin pin=new Pin();
						pin.id=temp.getJSONObject("pin").getString("id");
						pin.name=temp.getJSONObject("pin").getString("name");
						pin.description=temp.getJSONObject("pin").getString("description");
						pin.pin_lat=temp.getJSONObject("pin").getString("pin_lat");
						pin.pin_long=temp.getJSONObject("pin").getString("pin_long");
						pin.formatted_add=temp.getJSONObject("pin").getString("formatted_add");
						pin.likes=temp.getJSONObject("pin").getString("likes");
						pin.shares=temp.getJSONObject("pin").getString("shares");
						pin.rating=temp.getJSONObject("pin").getString("rating");
						pin.cat_id=temp.getJSONObject("pin").getString("cat_id");
						pin.date=temp.getJSONObject("pin").getString("created_at");
						pin.cat_name=temp.getJSONObject("pin").getString("cat_name");
						pin.userId=temp.getJSONObject("user").getString("id");
						pin.userName=temp.getJSONObject("user").getString("name");
						pin.userPhoto=temp.getJSONObject("user").getString("photo_path");
						pin.userEmail=temp.getJSONObject("user").getString("email");
						JSONArray temparr=temp.getJSONObject("pin").isNull("image")?new JSONArray():temp.getJSONObject("pin").getJSONArray("image");						
						if(temparr.length()>0)
						{
							for(int j=0;j<temparr.length();j++)
							{
								HashMap<String, String> map=new HashMap<>();
								map.put("id", temparr.getJSONObject(j).getString("id"));
								map.put("path", temparr.getJSONObject(j).getString("path"));
								pin.images.add(map);
							}
						}	
						pinData.add(pin);
					}
					return 1;
				}
				else
				{
					return 0;
				}
			}	
			else if(jsonObject.getString("error").equals("1"))
			{
				return -1;
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
			return -1;
		}
	}

	protected void onPostExecute(Integer result) 
	{
		if(needProgressDialog)
		dialog.dismiss();
		if(result==-1)
			barStyle.setError("Error while loading the pins...");
		else if(result==1)
			barStyle.stopProgress();
		else if(result==0)		
			barStyle.hideableMessage("No more pins now.", 2000);
		
		callbackListener.ontaskComplete(pinData);
	}

	public static interface onTaskCompleteListener
	{
		void ontaskComplete(ArrayList<Pin> pinData);
	}
}
