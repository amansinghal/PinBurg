package com.aman.database;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aman.ModelClasses.Pin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper
{
	 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "pinburg_db";
 
    // Table Names
    private static final String TABLE_PINS = "pins_table";    
 
    private static final String CREATE_TABLE_PINS = "CREATE TABLE pins_table (id varchar(20),pin_lat varchar(20)" +
    		",pin_long varchar(20)," +
    		"likes int(10),shares int(10),name varchar(100),description varchar(200),address varchar(200)," +
    		"img1_path varchar(100),img1_id varchar(100),catagery_name varchar(50),catagery_id varchar(50),rating int(50)," +
    		"created_at datetime,user_id varchar(20),user_photo varchar(100),user_email varchar(50),user_name varchar(100))";
 
    public DataBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db)
    {
          db.execSQL(CREATE_TABLE_PINS);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PINS);         
        // create new tables
        onCreate(db);
    }
    
    public int insertPin(String jsnResponse)
    {
    	Pin pin=getParsePin(jsnResponse);
    	SQLiteDatabase db=this.getWritableDatabase();
    	ContentValues values=new ContentValues();
    	values.put("id", pin.id);
    	values.put("pin_lat", pin.pin_lat);
    	values.put("pin_long", pin.pin_long);
    	values.put("name", pin.name);
    	values.put("description", pin.description);
    	values.put("address", pin.formatted_add);
    	values.put("likes", pin.likes);
    	values.put("shares", pin.shares);
    	values.put("rating", pin.rating);
    	values.put("catagery_name", pin.cat_name);
    	values.put("catagery_id", pin.cat_id);
    	values.put("created_at", pin.date);
    	values.put("img1_path",pin.images.size()>0?pin.images.get(0).get("path"):"");
    	values.put("img1_id",pin.images.size()>0?pin.images.get(0).get("id"):"");
    	values.put("user_id", pin.userId);
    	values.put("user_photo", pin.userPhoto);
    	values.put("user_name", pin.userName);
    	values.put("user_email", pin.userEmail);
    	return (int) db.insert(TABLE_PINS, null, values);
    }
  
    
    public Pin fetchPinById(String id)
    {
    	SQLiteDatabase db=this.getReadableDatabase();
    	Cursor c=db.rawQuery("SELECT * FROM "+TABLE_PINS+"WHERE id=?",new String[]{id});
    	Pin pin=new Pin();
		pin.id=c.getString(c.getColumnIndex("id"));
		pin.name=c.getString(c.getColumnIndex("mane"));
		pin.description=c.getString(c.getColumnIndex("description"));
		pin.pin_lat=c.getString(c.getColumnIndex("pin_lat"));
		pin.pin_long=c.getString(c.getColumnIndex("pin_long"));
		pin.formatted_add=c.getString(c.getColumnIndex("address"));
		pin.likes=c.getString(c.getColumnIndex("likes"));
		pin.shares=c.getString(c.getColumnIndex("shares"));
		pin.rating=c.getString(c.getColumnIndex("rating"));
		pin.cat_id=c.getString(c.getColumnIndex("catagery_id"));
		pin.date=c.getString(c.getColumnIndex("created_at"));
		pin.cat_name=c.getString(c.getColumnIndex("catagery_name"));
		pin.userId=c.getString(c.getColumnIndex("user_id"));
		pin.userName=c.getString(c.getColumnIndex("user_name"));
		pin.userPhoto=c.getString(c.getColumnIndex("user_photo"));
		pin.userEmail=c.getString(c.getColumnIndex("user_email"));
		HashMap<String, String> map=new HashMap<>();
		map.put("id", c.getString(c.getColumnIndex("img1_id")));
		map.put("path", c.getString(c.getColumnIndex("img1_path")));
		pin.images.add(map);
		return pin;
    }
    
    public Pin getParsePin(String jsonResponse)
    {
    	try
    	{
    	JSONObject temp=new JSONObject(jsonResponse);
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
		JSONArray temparr=temp.getJSONObject("pin").getJSONArray("image");						
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
		return pin;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }
}