package com.zeal.peak.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeal.Vo.FeedsVo;
import com.zeal.peak.obejects.ImageCache.ImageCacheParams;
import com.zeal.peak.obejects.ImageFetcher;
import com.zeal.peak.obejects.RecyclingImageView;
import com.zeal.peekaboo.R;

public class FeedsDetailAdapter extends BaseAdapter {

	private FragmentActivity m_context;
	private ArrayList<FeedsVo> m_arrFeedList;
	private int width, height;
	private ImageFetcher mImageFetcher;
	private Bitmap m_bitmap;
	private ArrayList<Integer> m_isChecked = new ArrayList<Integer>();
	private ArrayList<Boolean> m_checkBoolean = new ArrayList<Boolean>();
	private boolean m_Checked = false;

	public FeedsDetailAdapter(FragmentActivity p_context,
			ArrayList<FeedsVo> p_arrFeed) {
		m_context = p_context;
		m_arrFeedList = p_arrFeed;

		ImageCacheParams cacheParams = new ImageCacheParams(m_context, "hai");

		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory

		// The ImageFetcher takes care of loading images into our ImageView
		// children asynchronously
		mImageFetcher = new ImageFetcher(m_context, 150);
		mImageFetcher.setLoadingImage(R.drawable.no_image);
		mImageFetcher.addImageCache(m_context.getSupportFragmentManager(),
				cacheParams);

		final DisplayMetrics displayMetrics = new DisplayMetrics();
		m_context.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		height = displayMetrics.heightPixels;
		width = displayMetrics.widthPixels;

		if (m_isChecked.size() <= 0) {
			for (int i = 0; i < m_arrFeedList.size(); i++) {
				m_checkBoolean.add(i, false);
				m_isChecked.add(i);
				// m_arrFeedList.get(i).setM_isCheckedPosition(true);
			}
		} else {

			for (int i = 0; i < m_arrFeedList.size(); i++) {
				m_checkBoolean.set(i, false);
				m_isChecked.remove(i);
				m_arrFeedList.get(i).setM_isCheckedPosition(false);
			}
		}

	}

	@Override
	public int getCount() {
		return m_arrFeedList.size();
	}

	@Override
	public Object getItem(int position) {
		return m_arrFeedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View m_view = convertView;
		ViewHolder m_holder;
		Bitmap m_bitmap;
		if (m_view == null) {
			m_holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) m_context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			m_view = inflater.inflate(R.layout.gvitem, null);

			m_holder.m_tvName = (TextView) m_view
					.findViewById(R.id.tv_profilename);
			m_holder.m_tvLikes = (TextView) m_view
					.findViewById(R.id.gv_tvlikes);
			m_holder.m_tvHearts = (TextView) m_view
					.findViewById(R.id.gv_tvhearts);

			m_holder.m_ivProfImage = (RecyclingImageView) m_view
					.findViewById(R.id.gridimage);
			m_holder.m_llLayout = (LinearLayout) m_view
					.findViewById(R.id.llv1234);
			m_view.setTag(m_holder);

		} else {
			m_holder = (ViewHolder) m_view.getTag();
		}

		if (true) {
			m_holder.m_ivProfImage.getLayoutParams().height = (width / 2) - 16;
			m_holder.m_llLayout.getLayoutParams().height = width / 8;
		} else {
			m_holder.m_ivProfImage.getLayoutParams().height = LayoutParams.FILL_PARENT;
			m_holder.m_llLayout.setVisibility(View.INVISIBLE);

		}

		m_holder.m_tvName.setText(m_arrFeedList.get(position)
				.getM_profileName().toString());
		m_holder.m_tvHearts.setText(m_arrFeedList.get(position).getM_hearts()
				.toString());
		m_holder.m_tvLikes.setText(m_arrFeedList.get(position).getM_likes()
				.toString());

		System.out.println("FeedsDetailAdapter.getView()&&&&&--"
				+ m_arrFeedList.get(position).getM_imageUrl().toString());

		// if (m_isChecked.contains(position)) {
		if (m_checkBoolean.get(position) == false) {
			if (m_arrFeedList.get(position).isM_isCheckedPosition()) {
				if (m_arrFeedList.get(position).getM_imageUrl()
						.contains(".jpg")
						|| m_arrFeedList.get(position).getM_imageUrl()
								.contains(".png")) {

					m_bitmap = BitmapFactory.decodeFile(m_arrFeedList.get(
							position).getM_imageUrl());

					mImageFetcher.loadImage(m_arrFeedList.get(position)
							.getM_imageUrl(), m_holder.m_ivProfImage);
					System.out.println("Image Got===>"
							+ m_arrFeedList.get(position).getM_imageUrl());
					m_checkBoolean.set(position, true);
					m_arrFeedList.get(position).setM_isCheckedPosition(false);
				} else if (m_arrFeedList.get(position).getM_imageUrl()
						.contains(".mp4")) {
					m_bitmap = ThumbnailUtils.extractThumbnail(ThumbnailUtils
							.createVideoThumbnail(m_arrFeedList.get(position)
									.getM_imageUrl().toString(),
									MediaStore.Video.Thumbnails.MICRO_KIND),
							50, 50); // mImageFetcher.setLoadingImage(m_bitmap);
					System.out.println("Video Got===>"
							+ m_arrFeedList.get(position).getM_imageUrl());
					mImageFetcher.loadImage(m_bitmap, m_holder.m_ivProfImage);
					m_checkBoolean.set(position, true);
					m_checkBoolean.add(position, true);
					m_arrFeedList.get(position).setM_isCheckedPosition(false);
				}

			}
			m_arrFeedList.get(position).setM_isCheckedPosition(
					m_checkBoolean.set(position, false));
			// m_isChecked.set(position, m_isChecked.get(position));

		}
		/*
		 * } else if(m_arrFeedList.get(position).isM_isCheckedPosition() ){
		 * m_isChecked.add(position); m_checkBoolean.set(position, false);
		 * m_arrFeedList.get(position).setM_isCheckedPosition(false); }
		 */

		/*
		 * if (m_arrFeedList.get(position).getM_imageUrl().contains(".jpg") ||
		 * m_arrFeedList.get(position).getM_imageUrl().contains(".png")) {
		 * 
		 * m_bitmap = BitmapFactory.decodeFile(m_arrFeedList.get(position)
		 * .getM_imageUrl());
		 * 
		 * mImageFetcher.loadImage( m_arrFeedList.get(position).getM_imageUrl(),
		 * m_holder.m_ivProfImage); System.out.println("Image Got===>" +
		 * m_arrFeedList.get(position).getM_imageUrl());
		 * 
		 * }
		 *//*
			 * else if
			 * (m_arrFeedList.get(position).getM_imageUrl().contains(".mp4")) {
			 * m_bitmap = ThumbnailUtils.extractThumbnail(ThumbnailUtils
			 * .createVideoThumbnail(m_arrFeedList.get(position)
			 * .getM_imageUrl().toString(),
			 * MediaStore.Video.Thumbnails.MICRO_KIND), 50, 50); //
			 * mImageFetcher.setLoadingImage(m_bitmap);
			 * System.out.println("Video Got===>" +
			 * m_arrFeedList.get(position).getM_imageUrl());
			 * mImageFetcher.loadImage(m_bitmap, m_holder.m_ivProfImage); }
			 */
		return m_view;
	}

	private static class ViewHolder {
		ImageView m_ivProfImage;
		TextView m_tvName, m_tvHearts, m_tvLikes;
		LinearLayout m_llLayout;
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

}
