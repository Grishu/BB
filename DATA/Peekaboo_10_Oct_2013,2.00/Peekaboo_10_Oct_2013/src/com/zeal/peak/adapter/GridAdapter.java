package com.zeal.peak.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeal.Vo.FeedsVo;
import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peak.obejects.ImageCache.ImageCacheParams;
import com.zeal.peak.obejects.ImageFetcher;
import com.zeal.peak.obejects.RecyclingImageView;
import com.zeal.peak.utils.CommonUtils;
import com.zeal.peekaboo.FeedDetail;
import com.zeal.peekaboo.PeakAboo;
import com.zeal.peekaboo.R;

public class GridAdapter extends BaseAdapter {
	ArrayList<FeedsVo> data;
	FragmentActivity ctx;
	ImageFetcher mImageFetcher;
	private static LayoutInflater inflater = null;
	final int width;
	boolean feed = true, m_status;
	private String m_sHeartUrl, m_heartCount, m_likeCount;
	ImageLoader il;
	int m_position, m_flagVideo;

	public GridAdapter(ArrayList<FeedsVo> p_data, FragmentActivity p_ctx) {
		feed = true;
		this.data = p_data;
		this.ctx = p_ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageCacheParams cacheParams = new ImageCacheParams(p_ctx, "hai");

		cacheParams.setMemCacheSizePercent(0.5f); // Set memory cache to 25% of
													// app memory

		// The ImageFetcher takes care of loading images into our ImageView
		// children asynchronously
		mImageFetcher = new ImageFetcher(ctx, 150);
		mImageFetcher.setLoadingImage(R.drawable.no_image);
		mImageFetcher.addImageCache(ctx.getSupportFragmentManager(),
				cacheParams);
		il = new ImageLoader(ctx);
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		p_ctx.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		width = displayMetrics.widthPixels;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return data.get(arg0).getM_imageId();
	}

	@Override
	public View getView(final int pos, View convertview, ViewGroup arg2) {
		View v = convertview;
		final VH vh;
		if (v == null) {
			vh = new VH();
			v = inflater.inflate(R.layout.gvitem, null);
			vh.profile = (TextView) v.findViewById(R.id.tv_profilename);
			vh.likes = (TextView) v.findViewById(R.id.gv_tvlikes);
			vh.hearts = (TextView) v.findViewById(R.id.gv_tvhearts);

			vh.profilepic = (RecyclingImageView) v.findViewById(R.id.gridimage);
			vh.ll = (LinearLayout) v.findViewById(R.id.llv1234);
			vh.m_llHeartLayout = (LinearLayout) v
					.findViewById(R.id.gv_llheart_layout);
			vh.m_llLikeLayout = (LinearLayout) v
					.findViewById(R.id.gv_llike_layout);
			vh.m_ivPlayVideo = (ImageView) v.findViewById(R.id.gv_item_playbtn);
			v.setTag(vh);
		} else {
			vh = (VH) v.getTag();
		}
		// vh.profile.setText(data.get(pos)+" "+String.valueOf(pos));
		// image_view.getLayoutParams().height = 20;
		if (feed) {
			vh.profilepic.getLayoutParams().height = (width / 2) - 16;
			vh.ll.getLayoutParams().height = width / 8;
		} else {
			vh.profilepic.getLayoutParams().height = LayoutParams.FILL_PARENT;
			vh.ll.setVisibility(View.INVISIBLE);
		}

		/*
		 * System.err.println("GridAdapter-&&&&&&&&&&&-- Name==>" +
		 * data.get(pos).getM_profileName().toString() + "Image URl===>" +
		 * data.get(pos).getM_imageId());
		 */
		vh.profile.setText(data.get(pos).getM_profileName().toString());
		vh.likes.setText(data.get(pos).getM_likes().toString());
		vh.hearts.setText(data.get(pos).getM_hearts().toString());

		/*
		 * if (data.get(pos).getM_imageUrl().contains(".jpg") ||
		 * data.get(pos).getM_imageUrl().contains(".png")) { // m_bitmap = //
		 * BitmapFactory.decodeFile(data.get(pos).getM_imageUrl());
		 * 
		 * mImageFetcher.loadImage(data.get(pos).getM_imageUrl(),
		 * vh.profilepic);
		 * 
		 * vh.m_ivPlayVideo.setVisibility(View.GONE);
		 * il.DisplayImage(data.get(pos).getM_imageUrl(), R.drawable.arrow,
		 * vh.profilepic); System.out.println("Image Got===>" +
		 * data.get(pos).getM_imageUrl()); } else if
		 * (data.get(pos).getM_imageUrl().contains(".mp4")) { m_bitmap =
		 * ThumbnailUtils.extractThumbnail(ThumbnailUtils
		 * .createVideoThumbnail(data.get(pos).getM_imageUrl() .toString(),
		 * MediaStore.Video.Thumbnails.FULL_SCREEN_KIND), 200, 200); //
		 * mImageFetcher.setLoadingImage(m_bitmap);
		 * System.out.println("Video Got===>" + data.get(pos).getM_imageUrl());
		 * vh.m_ivPlayVideo.setVisibility(View.VISIBLE); //
		 * mImageFetcher.loadImage(m_bitmap, vh.profilepic);
		 * il.DisplayImage(data.get(pos).getM_imageUrl(), R.drawable.arrow,
		 * vh.profilepic); }
		 */

		if (!data.get(pos).isM_isCheckedPosition()) {

			vh.m_ivPlayVideo.setVisibility(View.GONE);
			il.DisplayImage(data.get(pos).getM_imageUrl(), R.drawable.no_image,
					vh.profilepic);
		} else if (data.get(pos).isM_isCheckedPosition()) {
			vh.m_ivPlayVideo.setVisibility(View.VISIBLE);
			il.DisplayImage(data.get(pos).getM_imageUrl(), R.drawable.no_image,
					vh.profilepic);
		}
		// System.out.println("00000 Image ID==" +
		// data.get(pos).getM_imageId());
		m_position = data.get(pos).getM_imageId();
		vh.m_llHeartLayout.setTag(m_position);
		vh.m_llHeartLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!data.get(pos).isM_isCheckedPosition()) {
					m_flagVideo = 0;
				} else if (data.get(pos).isM_isCheckedPosition()) {
					m_flagVideo = 1;
				}
				callHeartWS m_WS = new callHeartWS();
				TextView m_tvHeart = (TextView) v
						.findViewById(R.id.gv_tvhearts);
				m_WS.execute(m_tvHeart, pos, (Integer) v.getTag());

			}
		});
		vh.m_llLikeLayout.setTag(m_position);

		vh.m_llLikeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!data.get(pos).isM_isCheckedPosition()) {
					m_flagVideo = 0;
				} else if (data.get(pos).isM_isCheckedPosition()) {
					m_flagVideo = 1;
				}
				callLikeWS m_likeWS = new callLikeWS();
				TextView m_tvLike = (TextView) v.findViewById(R.id.gv_tvlikes);
				m_likeWS.execute(m_tvLike, pos, (Integer) v.getTag());

			}
		});

		vh.profilepic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent in = new Intent(ctx, FeedDetail.class);
				in.putExtra("pos", pos);
				in.putParcelableArrayListExtra("parcel", data);
				ctx.startActivity(in);

			}
		});

		vh.m_ivPlayVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Video Url=="
						+ data.get(pos).getM_videoUrl());
				Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(
						Uri.parse(data.get(pos).getM_videoUrl()), "video/*");
				ctx.startActivity(intent);
			}
		});
		return v;
	}

	private static class VH {
		TextView profile, likes, hearts;
		ImageView profilepic, m_ivPlayVideo;
		LinearLayout ll, m_llHeartLayout, m_llLikeLayout;
	}

	public void pause() {
		mImageFetcher.setPauseWork(false);
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	public void resume() {
		mImageFetcher.setExitTasksEarly(false);
	}

	public void distroy() {
		mImageFetcher.closeCache();
	}

	/**
	 * Call the webservice of heart
	 */
	class callHeartWS extends AsyncTask<String, Integer, String> {
		TextView tv;
		int position, imid;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		public void execute(TextView tvv, int p_position, Integer integer) {
			// TODO Auto-generated method stub
			tv = tvv;
			position = p_position;
			imid = integer;
			this.execute();
		}

		@Override
		protected String doInBackground(String... params) {
			m_sHeartUrl = CommonUtils.parseJSON("heart.php?uid="
					+ PeakAboo.m_UserID + "&pid=" + imid + "&flag="
					+ m_flagVideo);
			try {
				JSONObject m_jObj = new JSONObject(m_sHeartUrl);
				m_status = m_jObj.getBoolean("Status");
				m_heartCount = m_jObj.getString("Hearts");
				data.get(position).setM_hearts(m_heartCount);
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
			notifyDataSetChanged();
			/*
			 * if (result != null) { tv.setText(result); }
			 */
		}
	}

	/**
	 * Call the webservice of Like
	 */
	class callLikeWS extends AsyncTask<String, Integer, String> {
		TextView tv;
		int position, imid;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		public void execute(TextView p_tvv, int pos, Integer integer) {
			// TODO Auto-generated method stub
			tv = p_tvv;
			position = pos;
			imid = integer;
			this.execute();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.193/PeekaBoo_Webservice/like.php?uid=98&pid=38&flag=0
			m_sHeartUrl = CommonUtils.parseJSON("like.php?uid="
					+ PeakAboo.m_UserID + "&pid=" + imid + "&flag="
					+ m_flagVideo);
			try {
				JSONObject m_jObj = new JSONObject(m_sHeartUrl);
				m_status = m_jObj.getBoolean("Status");
				m_likeCount = m_jObj.getString("Likes");
				data.get(position).setM_likes(m_likeCount);
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
			/*
			 * if (result != null) { tv.setText(result); }
			 */
			notifyDataSetChanged();
		}
	}
}
