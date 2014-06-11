package com.zeal.peekaboo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zeal.peak.obejects.ImageCache;
import com.zeal.peak.obejects.ImageFetcher;

public class SocialPhotoViewAcitivity extends FragmentActivity {
	private ViewPager m_vpPager;
	private ArrayList<String> m_imgArray;
	private ImageFetcher mImageFetcher;
	private int m_position;
	private ImageView m_ivBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_layout);
		m_imgArray = new ArrayList<String>();
		m_position = getIntent().getIntExtra("pos", 0);
		m_imgArray = getIntent().getStringArrayListExtra("ImageArray");

		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		final int height = displayMetrics.heightPixels;
		final int width = displayMetrics.widthPixels;
		final int longest = (height > width ? height : width) / 2;
		mImageFetcher = new ImageFetcher(this, longest);
		ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(
				this, "IMAGE_CACHE_DIR");
		cacheParams.setMemCacheSizePercent(0.25f);

		mImageFetcher = new ImageFetcher(this, longest);
		mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
		mImageFetcher.setImageFadeIn(false);

		System.err.println("size of image Array---->" + m_imgArray.size());
		m_vpPager = (ViewPager) findViewById(R.id.phl_vpPager);
		m_ivBack=(ImageView)findViewById(R.id.phl_ivback);
		
		ImageAdapter m_adapter = new ImageAdapter(getSupportFragmentManager());
		m_vpPager.setAdapter(m_adapter);
		m_vpPager.setCurrentItem(m_position);
		
		m_ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
	}

	public ImageFetcher getImageFetcher() {
		return mImageFetcher;
	}

	class ImageAdapter extends FragmentStatePagerAdapter {
		List<String> m_arrList = m_imgArray;

		public ImageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {

			return ProfilePhotoViewer.newInstance(m_arrList.get(arg0)
					.toString());
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return m_arrList.size();
		}

	}
}
