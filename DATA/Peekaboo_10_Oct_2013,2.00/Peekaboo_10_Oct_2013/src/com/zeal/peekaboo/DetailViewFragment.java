package com.zeal.peekaboo;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.zeal.peak.obejects.ImageFetcher;
import com.zeal.peak.obejects.RecyclingImageView;
import com.zeal.peak.utils.CommonUtils;

public class DetailViewFragment extends Fragment {
	private static final String IMAGE_DATA_EXTRA = "extra_image_data";
	private static final String PROFILE_DATA_EXTRA = "profile_name";
	private static final String LIKE_DATA = "like_count";
	private static final String HEART_DATA = "heart_count";
	private static final String IMAGE_ID_DATA = "image_id";
	private static final String VIDEO_URL_DATA = "video_url";
	private static final String FLAG_DATA = "flag";

	private String mImageUrl, mProfileName, mLikeCount, mHeartCount,
			m_sHeartUrl, m_heartCount, m_likeCount, m_videoUrl;
	private ImageView mImageView, m_ivPlay;
	private boolean m_status, m_isFlag;
	private int m_imageId, m_flagVideo;
	private ImageFetcher mImageFetcher;
	private TextView m_tvProfileName, m_tvHeart, m_tvLikes;
	private LinearLayout m_llHeart, m_llLike;
	private String m_prefUserID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString(
				IMAGE_DATA_EXTRA) : null;
		mProfileName = getArguments() != null ? getArguments().getString(
				PROFILE_DATA_EXTRA) : null;
		mLikeCount = getArguments() != null ? getArguments().getString(
				LIKE_DATA) : null;
		mHeartCount = getArguments() != null ? getArguments().getString(
				HEART_DATA) : null;
		m_imageId = getArguments() != null ? getArguments().getInt(
				IMAGE_ID_DATA) : null;
		m_videoUrl = getArguments() != null ? getArguments().getString(
				VIDEO_URL_DATA) : null;
		m_isFlag = getArguments() != null ? getArguments()
				.getBoolean(FLAG_DATA) : null;
	}

	public static DetailViewFragment newInstance(String imageUrl,
			String p_profName, String p_heartCount, String p_likeCount,
			int p_imageID, boolean p_flag, String p_videoUrl) {
		final DetailViewFragment f = new DetailViewFragment();

		final Bundle args = new Bundle();
		args.putString(IMAGE_DATA_EXTRA, imageUrl);
		args.putString(PROFILE_DATA_EXTRA, p_profName);
		args.putString(HEART_DATA, p_heartCount);
		args.putString(LIKE_DATA, p_likeCount);
		args.putInt(IMAGE_ID_DATA, p_imageID);
		args.putBoolean(FLAG_DATA, p_flag);
		args.putString(VIDEO_URL_DATA, p_videoUrl);

		f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_feed_detail, container,
				false);
		mImageView = (RecyclingImageView) v.findViewById(R.id.iv_detail_feed);
		m_tvProfileName = (TextView) v.findViewById(R.id.afd_tvProfileName);
		m_tvHeart = (TextView) v.findViewById(R.id.afd_tvHearts);
		m_tvLikes = (TextView) v.findViewById(R.id.afd_tvLikes);
		m_llHeart = (LinearLayout) v.findViewById(R.id.afd_llHeartsLikeLayout);
		m_llLike = (LinearLayout) v.findViewById(R.id.afd_llLikeLayout);
		m_ivPlay = (ImageView) v.findViewById(R.id.afd_ivplaybtn);
		m_prefUserID = CommonUtils.getStringSharedPref(
				DetailViewFragment.this.getActivity(), "user_ID", "");
		PeakAboo.m_UserID = m_prefUserID;
		m_llHeart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callHeartWS m_heartWS = new callHeartWS();
				TextView m_tvLike = (TextView) v
						.findViewById(R.id.afd_tvHearts);
				m_heartWS.execute(m_tvLike);
			}
		});

		m_llLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callLikeWS m_likeWS = new callLikeWS();
				TextView m_tvLike = (TextView) v.findViewById(R.id.afd_tvLikes);
				m_likeWS.execute(m_tvLike);
			}
		});

		m_ivPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(
						Uri.parse(m_videoUrl), "video/*");
				startActivity(intent);

			}
		});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (FeedDetail.class.isInstance(getActivity())) {
			mImageFetcher = ((FeedDetail) getActivity()).getImageFetcher();
			if (m_isFlag) {
				m_flagVideo = 1;
				m_ivPlay.setVisibility(View.VISIBLE);
				mImageFetcher.loadImage(mImageUrl, mImageView);
			} else {
				m_flagVideo = 0;
				m_ivPlay.setVisibility(View.INVISIBLE);
				mImageFetcher.loadImage(mImageUrl, mImageView);
			}

			m_tvProfileName.setText(mProfileName);
			m_tvHeart.setText(mHeartCount);
			m_tvLikes.setText(mLikeCount);
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
					+ PeakAboo.m_UserID + "&pid=" + m_imageId + "&flag="
					+ m_flagVideo);
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
					+ PeakAboo.m_UserID + "&pid=" + m_imageId + "&flag="
					+ m_flagVideo);
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
