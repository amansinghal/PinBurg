package asynctasks;

import com.aman.utils.Config;
import com.aman.utils.CustomProgressDialog;
import com.aman.utils.RestClient;
import com.aman.utils.RestClient.RequestMethod;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateNotificationServiceTask extends AsyncTask<String, Void, Integer>
{
	Context context;
	CustomProgressDialog dialog;
	GoogleCloudMessaging gcm;
	public static onTaskCompleteListener callbackListener;
	String response="";
	public UpdateNotificationServiceTask(Context context,onTaskCompleteListener listener) 
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
		dialog.show();
	}

	@Override
	protected Integer doInBackground(String... params) 
	{
		// TODO Auto-generated method stub		
		try 
		{
			RestClient client=new RestClient(Config.APILink);
			client.AddParam("tag","setNotifyMeInfo");
			client.AddParam("uid", params[0]);
			client.AddParam("range", params[1]);	
			client.AddParam("flag", params[2]);
			client.AddParam("category", params[3]);
			client.Execute(RequestMethod.POST);
			response=client.getResponse();
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
		callbackListener.ontaskComplete(response);
	}

	public static interface onTaskCompleteListener
	{
		void ontaskComplete(String response);
	}
}
