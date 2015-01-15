package com.aman.seeker;

import android.app.Application;
import android.content.SharedPreferences;

import com.aman.utils.Config;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ApplicationContextClass extends Application 
{	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.displayer(new FadeInBitmapDisplayer(300)).resetViewBeforeLoading(true)
		.showImageForEmptyUri(R.drawable.img_not_available)
		.showImageOnFail(R.drawable.error)
		.showImageOnLoading(R.drawable.loadingimg).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
		.defaultDisplayImageOptions(defaultOptions)
		.memoryCache(new WeakMemoryCache())
		.discCacheSize(100 * 1024 * 1024).build();

		ImageLoader.getInstance().init(config);			
				
	}
}