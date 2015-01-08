package asynctasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import asynctasks.CityTask.onTaskCompleteListener;

import com.aman.ModelClasses.CountryStateCity;
import com.aman.utils.Config;
import com.aman.utils.CustomMultiPartEntity;
import com.aman.utils.CustomMultiPartEntity.ProgressListener;

public class UploadFileTask extends AsyncTask<String, Integer, Integer> 
{
	ProgressDialog pd;
	String res;
	long totalSize=0;
	Context context;
	public static onTaskCompleteListener callbackListener;
	public UploadFileTask(Context context,onTaskCompleteListener listener) 
	{
		this.context=context;
		callbackListener=listener;
	}
	@Override
	protected void onPreExecute() 
	{
		// TODO Auto-generated method stub
		super.onPreExecute();
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("Uploading Picture...");
		pd.setCancelable(false);
		pd.show();
	}
	@Override
	protected Integer doInBackground(String... params) 
	{
		StringBuilder total = null;
		try 
		{
			// int i = Config.uploadFile(pic);
			Log.e("PATH", params[0]);
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Config.UploadAPILink);
			

			//MultipartEntity entity = new MultipartEntity();
			CustomMultiPartEntity entity = new CustomMultiPartEntity(new ProgressListener()
			{
				@Override
				public void transferred(long num)
				{
					publishProgress((int) ((num / (float) totalSize) * 100));
				}
			});

			entity.addPart("type", new StringBody("image/jpeg"));

			entity.addPart("fileToUpload", new FileBody(new File(params[0])));
			
			entity.addPart("uid", new StringBody(params[1]));

			totalSize = entity.getContentLength();

			httppost.setEntity(entity);

			HttpResponse response = httpclient.execute(httppost);

			BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			total = new StringBuilder();

			String line = null;

			while ((line = r.readLine()) != null) 
			{
				total.append(line);
			}

			r.close();

			Log.e("Response", total.toString());				

			res=total.toString();
			
			return 1;

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return -1;
		}			
	}

	@Override
	protected void onProgressUpdate(Integer... values) 
	{
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		if(((int) (values[0]))!=100)
		{
			pd.setProgress((int) (values[0]));
		}
		else
		{
			pd.dismiss();
			pd=ProgressDialog.show(context,null,"Please wait....");
		}
	}
	@Override
	protected void onPostExecute(Integer result) 
	{
		// TODO Auto-generated method stub
		super.onPostExecute(result);			
		pd.dismiss();
		switch (result) 
		{
		case -1:
			callbackListener.ontaskComplete("");
			break;
		case 1:
			callbackListener.ontaskComplete(res);
			break;
		}
	}
	public static interface onTaskCompleteListener
	{
		void ontaskComplete(String response);
	}
}
