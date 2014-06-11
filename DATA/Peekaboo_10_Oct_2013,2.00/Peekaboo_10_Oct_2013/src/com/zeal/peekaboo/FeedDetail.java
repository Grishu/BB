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
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.zeal.Vo.FeedsVo;
import com.zeal.peak.obejects.ImageCache;
import com.zeal.peak.obejects.ImageFetcher;

public class FeedDetail extends FragmentActivity {
	private static final String IMAGE_CACHE_DIR = "images";
	public static final String EXTRA_IMAGE = "extra_image";
	ImageFetcher mImageFetcher;
	private ArrayList<FeedsVo> m_arrDetails;
	private ImageView m_ivBack;
	private ViewPager vp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surprisefrag);

		int pos = getIntent().getIntExtra("pos", 0);
		System.err.println("Position is--->" + pos);
		getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);
		m_arrDetails = new ArrayList<FeedsVo>();
		m_arrDetails = getIntent().getParcelableArrayListExtra("parcel");

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
		m_arrDetails = GridFragment.abcd;
		m_ivBack = (ImageView) findViewById(R.id.sf_ivback);

		vp = (ViewPager) findViewById(R.id.pager);
		pgradptr pa = new pgradptr(getSupportFragmentManager());
		vp.setAdapter(pa);
		vp.setCurrentItem(pos);
		
		m_ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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

	class pgradptr extends FragmentStatePagerAdapter {
		List<FeedsVo> data = m_arrDetails;

		public pgradptr(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return DetailViewFragment.newInstance(data.get(arg0)
					.getM_imageUrl(), data.get(arg0).getM_profileName(), data
					.get(arg0).getM_hearts(), data.get(arg0).getM_likes(), data
					.get(arg0).getM_imageId(), data.get(arg0)
					.isM_isCheckedPosition(), data.get(arg0).getM_videoUrl());
			// return DetailViewFragment.newInstance(m_arrDetails.get(arg0));
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}
	}

}
