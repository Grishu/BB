package com.zeal.peekaboo;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeal.peak.obejects.ImageFetcher;
import com.zeal.peak.obejects.RecyclingImageView;
import com.zeal.peak.utils.CommonUtils;

public class SurpriseSingleview extends Fragment {

	private static final String IMAGE_DATA_EXTRA = "extra_image_data";
	private static final String PROFILE_DATA_EXTRA = "profile_name";
	private static final String LIKE_DATA = "like_count";
	private static final String HEART_DATA = "heart_count";
	private static final String IMAGE_ID_DATA = "image_id";
	private String mImageUrl, mProfileName, mHeartCount, mLikeCount,
			m_sHeartUrl, m_heartCount, m_likeCount;
	private boolean m_status;
	private int m_imageId;
	private ImageView mImageView;
	private ImageFetcher mImageFetcher;
	private TextView m_tvHeartCount, m_tvLikeCount, m_tvProfName;
	private LinearLayout m_llHeartLayout, m_llLikeLayout;
	private String m_prefUserID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString(
				IMAGE_DATA_EXTRA) : null;
		mProfileName = getArguments() != null ? getArguments().getString(
				PROFILE_DATA_EXTRA) : null;
		mHeartCount = getArguments() != null ? getArguments().getString(
				HEART_DATA) : null;
		mLikeCount = getArguments() != null ? getArguments().getString(
				LIKE_DATA) : null;
		m_imageId = getArguments() != null ? getArguments().getInt(
				IMAGE_ID_DATA) : null;

	}

	public static SurpriseSingleview newInstance(String imageUrl,
			String p_profName, String p_heartCount, String p_likeCount,
			int p_imageID) {

		final SurpriseSingleview f = new SurpriseSingleview();

		final Bundle args = new Bundle();
		args.putString(IMAGE_DATA_EXTRA, imageUrl);
		args.putString(PROFILE_DATA_EXTRA, p_profName);
		args.putString(HEART_DATA, p_heartCount);
		args.putString(LIKE_DATA, p_likeCount);
		args.putInt(IMAGE_ID_DATA, p_imageID);
		f.setArguments(args);

		return f;
	}

	public SurpriseSingleview() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_feed_detail, container,
				false);
		mImageView = (RecyclingImageView) v.findViewById(R.id.iv_detail_feed);
		m_tvHeartCount = (TextView) v.findViewById(R.id.afd_tvHearts);
		m_tvLikeCount = (TextView) v.findViewById(R.id.afd_tvLikes);
		m_tvProfName = (TextView) v.findViewById(R.id.afd_tvProfileName);
		m_llHeartLayout = (LinearLayout) v.findViewById(R.id.afd_llHeartLayout);
		m_llLikeLayout = (LinearLayout) v.findViewById(R.id.afd_llLikeLayout);
		m_prefUserID = CommonUtils.getStringSharedPref(
				SurpriseSingleview.this.getActivity(), "user_ID", "");
		PeakAboo.m_UserID = m_prefUserID;
		m_llHeartLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callHeartWS m_heartWS = new callHeartWS();
				TextView m_tvLike = (TextView) v
						.findViewById(R.id.afd_tvHearts);
				m_heartWS.execute(m_tvLike);
			}
		});

		m_llLikeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callLikeWS m_likeWS = new callLikeWS();
				TextView m_tvLike = (TextView) v.findViewById(R.id.afd_tvLikes);
				m_likeWS.execute(m_tvLike);
			}
		});
		m_tvProfName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				/*
				 * Intent m_intent = new Intent(getActivity(),
				 * Tabbedmaincontroller.class); m_intent.putExtra("user_Id",
				 * PeakAboo.m_UserID); startActivity(m_intent);
				 */

			}
		});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (SurpriseFullscreen.class.isInstance(getActivity())) {
			mImageFetcher = ((SurpriseFullscreen) getActivity())
					.getImageFetcher();
			mImageFetcher.loadImage(mImageUrl, mImageView);
			m_tvHeartCount.setText(mHeartCount);
			m_tvLikeCount.setText(mLikeCount);
			m_tvProfName.setText(mProfileName);
		}
	}

	/**
	 * Call the webservice of heart
	 */
	class callHeartWS extends AsyncTask<String, Integer, String> {
		TextView tv;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		public void execute(TextView tvv) {
			// TODO Auto-generated method stub
			tv = tvv;
			this.execute();
		}

		@Override
		protected String doInBackground(String... params) {
			m_sHeartUrl = CommonUtils.parseJSON("heart.php?uid="
					+ PeakAboo.m_UserID + "&pid=" + m_imageId + "&flag=0");
			try {
				JSONObject m_jObj = new JSONObject(m_sHeartUrl);
				m_status = m_jObj.getBoolean("Status");
				m_heartCount = m_jObj.getString("Hearts");
				System.out.println("HEart Count------>" + m_heartCount);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return m_heartCount;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// pDlg.dismiss();
			if (result != null) {
				tv.setText(result);
			}
		}
	}

	/**
	 * Call the webservice of Like
	 */
	class callLikeWS extends AsyncTask<String, Integer, String> {
		TextView tv;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		public void execute(TextView p_tvv) {
			// TODO Auto-generated method stub
			tv = p_tvv;
			this.execute();
		}

		@Override
		protected String doInBackground(String... params) {
			m_sHeartUrl = CommonUtils.parseJSON("like.php?uid="
					+ PeakAboo.m_UserID + "&pid=" + m_imageId + "&flag=0");
			try {
				JSONObject m_jObj = new JSONObject(m_sHeartUrl);
				m_status = m_jObj.getBoolean("Status");
				m_likeCount = m_jObj.getString("Likes");
				System.out.println("Like Count------>" + m_likeCount);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return m_likeCount;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// pDlg.dismiss();
			if (result != null) {
				tv.setText(result);
			}
		}
	}
}
