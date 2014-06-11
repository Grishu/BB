package com.zeal.peekaboo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeal.peak.obejects.ImageCache;
import com.zeal.peak.obejects.ImageFetcher;
import com.zeal.peak.utils.CommonUtils;

public class Tabbedmaincontroller extends FragmentActivity {
	View selected;
	private static final String IMAGE_CACHE_DIR = "images";
	public static final String EXTRA_IMAGE = "extra_image";
	// private String m_userId;
	private ImageFetcher mImageFetcher;
	public static TextView m_tvHeader;
	private ImageView cameraicon, b2, Social;
	public static ImageView m_ivback, m_ivReport, m_ivsetting;
	public int curr_frag;
	View sel_frag;
	private String m_prefUserID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/*
		 * if (getIntent().getStringExtra("userId") != null) { m_userId =
		 * getIntent().getStringExtra("userId");
		 * System.out.println("TabmainControl UserId==>" + m_userId);
		 * PeakAboo.m_UserID=m_userId; }
		 */
		m_prefUserID = CommonUtils.getStringSharedPref(getApplicationContext(),
				"user_ID", "");
		PeakAboo.m_UserID = m_prefUserID;
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		final int height = displayMetrics.heightPixels;
		final int width = displayMetrics.widthPixels;
		final int longest = (height > width ? height : width) / 2;

		ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(
				this, IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory

		// The ImageFetcher takes care of loading images into our ImageView
		// children asynchronously
		m_tvHeader = (TextView) findViewById(R.id.textView1);
		m_tvHeader.setText(getResources().getString(R.string.lbl_peak_a_boo));
		m_ivsetting = (ImageView) findViewById(R.id.settingsView1);
		m_ivback = (ImageView) findViewById(R.id.imageView1);
		m_ivReport = (ImageView) findViewById(R.id.m_ivReport);
		cameraicon = (ImageView) findViewById(R.id.iv_camera);
		Social = (ImageView) findViewById(R.id.iv_social);
		b2 = (ImageView) findViewById(R.id.iv_suprise);

		m_ivback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (curr_frag == 3) {

					m_tvHeader.setText(getResources().getString(
							R.string.app_name));
					m_ivback.setVisibility(View.INVISIBLE);
					m_ivsetting.setVisibility(View.VISIBLE);

					Social.setBackgroundResource(R.drawable.buttonbg_hover);
					// onBackPressed();
					FragmentManager fm = getSupportFragmentManager();
					FragmentTransaction ft = fm.beginTransaction();
					fm.popBackStack();
					SocialFragment llf = new SocialFragment();
					Bundle m_bundle = new Bundle();
					m_bundle.putString("user_Id", PeakAboo.m_UserID);
					llf.setArguments(m_bundle);
//					ft.addToBackStack("Social");
					ft.replace(R.id.fragmentswitcherframe, llf);
					ft.commit();
				} else if (PeakAboo.m_isSurprise) {
					onBackPressed();
					m_ivback.setVisibility(View.INVISIBLE);
					m_ivsetting.setVisibility(View.VISIBLE);
				}

			}
		});

		m_ivsetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (curr_frag == 1) {
					Intent i = new Intent(Tabbedmaincontroller.this,SurpriseSetting.class);
					startActivity(i);
					
				} else {
					FragmentManager fm = getSupportFragmentManager();
					FragmentTransaction ft1 = fm.beginTransaction();
					Fragment m_fragSet = new Setting();
					Bundle m_bundle = new Bundle();
					m_bundle.putString("user_Id", PeakAboo.m_UserID);
					m_fragSet.setArguments(m_bundle);
					ft1.addToBackStack("Setting");
					ft1.replace(R.id.fragmentswitcherframe, m_fragSet);
					ft1.commit();

					curr_frag = 3;
				}
			}
		});
		mImageFetcher = new ImageFetcher(this, longest);
		mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
		mImageFetcher.setImageFadeIn(false);

		ImageView b = (ImageView) findViewById(R.id.iv_feed1);
		selected = b;
		ImageView m_ivNotif = (ImageView) findViewById(R.id.iv_msgs);

		feeddisplay(b);
		// final Drawable
		// hover=getResources().getDrawable(R.drawable.buttonbg_hover);

		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				curr_frag = 1;
				m_tvHeader.setText(getResources().getString(R.string.app_name));
				m_ivback.setVisibility(View.INVISIBLE);
				m_ivReport.setVisibility(View.VISIBLE);
				m_ivsetting.setVisibility(View.INVISIBLE);
				selected.setBackgroundResource(R.drawable.buttonbg);
				arg0.setBackgroundResource(R.drawable.buttonbg_hover);

				selected = arg0;
				// TODO Auto-generated method stub
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Fragment llf = new Vfsurprise();
				System.err.println("Surprise User Id-$$$$$$$$$$ "
						+ PeakAboo.m_UserID);
				Bundle m_bundle = new Bundle();
				m_bundle.putString("user_Id", PeakAboo.m_UserID);
				llf.setArguments(m_bundle);

				ft.replace(R.id.fragmentswitcherframe, llf);
				ft.commit();
			}
		});
		// ImageView b2=(ImageView)findViewById(R.id.iv_suprise);

		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				feeddisplay(arg0);

			}
		});

		cameraicon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//m_tvHeader.setText(getResources().getString(R.string.app_name));
				m_ivback.setVisibility(View.VISIBLE);
				m_ivReport.setVisibility(View.INVISIBLE);
				m_ivsetting.setVisibility(View.GONE);
				selected.setBackgroundResource(R.drawable.buttonbg);
				arg0.setBackgroundResource(R.drawable.buttonbg_hover);

				selected = arg0;
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Camerafilter llf = new Camerafilter();
				ft.replace(R.id.fragmentswitcherframe, llf);
				ft.commit();

			}
		});

		Social.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				curr_frag = 2;
				sel_frag = arg0;
				m_tvHeader.setText(getResources().getString(R.string.app_name));
				m_ivback.setVisibility(View.INVISIBLE);
				m_ivsetting.setVisibility(View.VISIBLE);
				m_ivReport.setVisibility(View.INVISIBLE);
				selected.setBackgroundResource(R.drawable.buttonbg);
				arg0.setBackgroundResource(R.drawable.buttonbg_hover);

				selected = arg0;
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				fm.popBackStack();
				SocialFragment llf = new SocialFragment();
				Bundle m_bundle = new Bundle();
//				ft.addToBackStack("Social");
				m_bundle.putString("user_Id", PeakAboo.m_UserID);
				llf.setArguments(m_bundle);
				ft.replace(R.id.fragmentswitcherframe, llf);
				ft.commit();
			}
		});

		m_ivNotif.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				m_tvHeader.setText(getResources().getString(R.string.app_name));
				m_ivback.setVisibility(View.INVISIBLE);
				m_ivReport.setVisibility(View.INVISIBLE);
				m_ivsetting.setVisibility(View.GONE);
				selected.setBackgroundResource(R.drawable.buttonbg);
				v.setBackgroundResource(R.drawable.buttonbg_hover);

				selected = v;
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				NotificationFragment m_notif = new NotificationFragment();
				Bundle m_bundle = new Bundle();
				m_bundle.putString("user_Id", PeakAboo.m_UserID);
				m_notif.setArguments(m_bundle);
				ft.replace(R.id.fragmentswitcherframe, m_notif);
				ft.commit();
			}
		});
	}

	/*
	 * public void name(String uid, String fid) { FragmentManager fm =
	 * getSupportFragmentManager(); FragmentTransaction ft =
	 * fm.beginTransaction(); SocialFragment llf = new SocialFragment(); Bundle
	 * m_bundle = new Bundle(); m_bundle.putString("user_Id",
	 * PeakAboo.m_UserID); llf.setArguments(m_bundle);
	 * 
	 * ft.replace(R.id.fragmentswitcherframe, llf); ft.commit();
	 * 
	 * }
	 */
	public ImageFetcher getImageFetcher() {
		return mImageFetcher;
	}

	protected void feeddisplay(View arg0) {
		// TODO Auto-generated method stub
		selected.setBackgroundResource(R.drawable.buttonbg);
		m_ivback.setVisibility(View.INVISIBLE);
		m_ivReport.setVisibility(View.INVISIBLE);
		m_ivsetting.setVisibility(View.GONE);
		arg0.setBackgroundResource(R.drawable.buttonbg_hover);
		m_tvHeader.setText("Peak'a'Boo");
		selected = arg0;

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		Fragment llf = new GridFragment();
		Bundle bundle = new Bundle();
		bundle.putString("userId", PeakAboo.m_UserID);
		llf.setArguments(bundle);

		ft.replace(R.id.fragmentswitcherframe, llf);
		ft.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (PeakAboo.m_isItemClicked) {
			PeakAboo.m_isItemClicked = false;
			Log.e("onResume", "TabbedmainController Resume Called.");

			FragmentTransaction fm = getSupportFragmentManager()
					.beginTransaction();
			Fragment llf = new OtherProfileFragment();
			System.err.println("Surprise User Id-$$$$$$$$$$ "
					+ PeakAboo.m_UserID);
			Bundle m_bundle = new Bundle();
			m_bundle.putString("Follower_Id", PeakAboo.m_sFollowerId);
			m_bundle.putString("user_Id", PeakAboo.m_UserID);
			llf.setArguments(m_bundle);
			// fm.addToBackStack("OtherProfile");
			fm.replace(R.id.fragmentswitcherframe, llf);
			fm.commit();
		}
		

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (PeakAboo.m_isSurprise) {
			m_ivback.setVisibility(View.INVISIBLE);
			m_ivsetting.setVisibility(View.VISIBLE);
		}
		else if(!PeakAboo.m_isItemClicked)
		{
			m_ivback.setVisibility(View.INVISIBLE);
			m_ivsetting.setVisibility(View.VISIBLE);
		}
	}
}
