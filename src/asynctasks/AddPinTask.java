package asynctasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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

import com.aman.utils.Config;
import com.aman.utils.CustomMultiPartEntity;
import com.aman.utils.CustomMultiPartEntity.ProgressListener;
import com.aman.utils.CustomProgressDialog;
import com.aman.utils.RestClient;
import com.aman.utils.RestClient.RequestMethod;

public class AddPinTask extends AsyncTask<String, Integer, Integer>
{
	Context context;
	CustomProgressDialog dialog;
	public static onTaskCompleteListener callbackListener;
	ProgressDialog pd;
	String response="";
	long totalSize=0;
	ArrayList<HashMap<String, String>> listCatagory;
	public AddPinTask(Context context,onTaskCompleteListener listener) 
	{
		this.context=context;
		callbackListener=listener;
		listCatagory=new ArrayList<>();
	}

	@Override
	protected void onPreExecute() 
	{
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
		StringBuilder total;
		try 
		{


			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(Config.APILink);

			CustomMultiPartEntity entity = new CustomMultiPartEntity(new ProgressListener()
			{
				@Override
				public void transferred(long num)
				{
					publishProgress((int) ((num / (float) totalSize) * 100));
				}
			});

			entity.addPart("tag", new StringBody("addPin"));

			entity.addPart("pin_lat", new StringBody(params[0]));

			entity.addPart("pin_long", new StringBody(params[1]));

			entity.addPart("user_id", new StringBody(params[2]));

			entity.addPart("name", new StringBody(params[3]));

			entity.addPart("description", new StringBody(params[4]));

			entity.addPart("formatted_add", new StringBody(params[5]));

			entity.addPart("cat_id", new StringBody(params[6]));

			entity.addPart("sub_cat_id", new StringBody("0"));


			if(!params[7].isEmpty()||!params[8].isEmpty()||!params[9].isEmpty())
			{

				entity.addPart("type", new StringBody("image/jpeg"));

				if(!params[7].isEmpty())
					entity.addPart("img1", new FileBody(new File(params[7])));
				if(!params[8].isEmpty())
					entity.addPart("img2", new FileBody(new File(params[8])));
				if(!params[9].isEmpty())
					entity.addPart("img3", new FileBody(new File(params[9])));

				totalSize = entity.getContentLength();

			}


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
			
			if(new JSONObject(total.toString()).getString("error").equals("0"))				
			{				
				if(!params[7].isEmpty())
					new File(params[7]).delete();
				if(!params[8].isEmpty())
					new File(params[8]).delete();
				if(!params[9].isEmpty())
					new File(params[9]).delete();
				return 1;
			}

			return 0;
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
			return 0;
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
	protected void onPostExecute(Integer result) 
	{
		pd.dismiss();
		callbackListener.ontaskComplete(result==0?false:true);
	}

	public static interface onTaskCompleteListener
	{
		void ontaskComplete(boolean success);
	}
}
