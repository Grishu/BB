package com.zeal.peekaboo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.zeal.Vo.FeedsVo;
import com.zeal.peak.obejects.ImageCache;
import com.zeal.peak.obejects.ImageFetcher;

public class SurpriseFullscreen extends FragmentActivity {
	ViewPager vp;

	private static final String IMAGE_CACHE_DIR = "images";
	public static final String EXTRA_IMAGE = "extra_image";
	ImageFetcher mImageFetcher;
	private ArrayList<FeedsVo> m_arrSurpriseDetail;
	private ImageView m_ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surprisefrag);

		int pos = getIntent().getIntExtra("pos", 0);
		System.out.println("Full Screen Image ID===" + pos);
		getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);
		m_arrSurpriseDetail = new ArrayList<FeedsVo>();
		m_arrSurpriseDetail = getIntent().getParcelableArrayListExtra("parcel");
		m_ivBack = (ImageView) findViewById(R.id.sf_ivback);
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		final int height = displayMetrics.heightPixels;
		final int width = displayMetrics.widthPixels;
		final int longest = (height > width ? height : width) / 2;
		mImageFetcher = new ImageFetcher(this, longest);
		ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(
				this, IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f);

		mImageFetcher = new ImageFetcher(this, longest);
		mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
		mImageFetcher.setImageFadeIn(false);

		m_ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});
		vp = (ViewPager) findViewById(R.id.pager);
		if (m_arrSurpriseDetail.size() > 0) {
			pgradptr pa = new pgradptr(getSupportFragmentManager());
			vp.setAdapter(pa);
			vp.setCurrentItem(pos);
		}
		pageSwitcher(6);
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				Log.e("pageselectrd", "selected:" + String.valueOf(arg0));
				timer.cancel();
				pageSwitcher(5);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	Timer timer = new Timer();

	public void pageSwitcher(int seconds) {
		timer = new Timer(); // At this line a new Thread will be created
		timer.schedule(new RemindTask(), 6000, seconds * 1000); // delay
																// in
		// milliseconds
	}

	// this is an inner class...
	class RemindTask extends TimerTask {

		@Override
		public void run() {

			// As the TimerTask run on a seprate thread from UI thread we have
			// to call runOnUiThread to do work on UI thread.
			runOnUiThread(new Runnable() {
				public void run() {

					/*
					 * if (page > 4) { // In my case the number of pages are 5
					 * timer.cancel(); // Showing a toast for just testing
					 * purpose Toast.makeText(getApplicationContext(),
					 * "Timer stoped", Toast.LENGTH_LONG).show(); } else {
					 */
					vp.setCurrentItem(vp.getCurrentItem() + 1);

					// }
				}
			});

		}
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

	class pgradptr extends FragmentStatePagerAdapter {
		// List<String> data = Arrays.asList(Images.imageThumbUrls);
		List<FeedsVo> data = m_arrSurpriseDetail;

		public pgradptr(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {

			return SurpriseSingleview.newInstance(data.get(arg0)
					.getM_imageUrl(), data.get(arg0).getM_profileName(), data
					.get(arg0).getM_hearts(), data.get(arg0).getM_likes(), data
					.get(arg0).getM_imageId());
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}
	}

}
