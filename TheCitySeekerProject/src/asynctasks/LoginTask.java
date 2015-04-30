package asynctasks;

import com.aman.utils.Config;
import com.aman.utils.CustomProgressDialog;
import com.aman.utils.RestClient;
import com.aman.utils.RestClient.RequestMethod;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoginTask extends AsyncTask<String, Void, Integer>
{
	Context context;
	CustomProgressDialog dialog;
	GoogleCloudMessaging gcm;
	public static onTaskCompleteListener callbackListener;
	String response="";
	public LoginTask(Context context,onTaskCompleteListener listener) 
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
		String regid;
		if (gcm == null)
		{
			gcm = GoogleCloudMessaging.getInstance(context);
		}
		try 
		{
			regid = gcm.register(Config.GCM_SENDER_ID);
			Log.e("RegId", regid);	
			RestClient client=new RestClient(Config.APILink);
			client.AddParam("tag","login");
			client.AddParam("email", params[0]);
			client.AddParam("password", params[1]);	
			client.AddParam("gcm_regid", regid);
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
