package com.zeal.peak.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeal.Vo.SearchImageVideoVo;
import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peak.obejects.RecyclingImageView;
import com.zeal.peekaboo.PeakAboo;
import com.zeal.peekaboo.R;
import com.zeal.peekaboo.SocialPhotoViewAcitivity;

public class SearchImageAdapter extends BaseAdapter {

	private Context m_context;
	private ArrayList<SearchImageVideoVo> m_arrDetails;
	private static LayoutInflater inflater = null;
	private ImageLoader m_Loader;
	final int width;
	private boolean m_isVideo = false, m_isUser = false;
	private ArrayList<String> m_arrImages;

	public SearchImageAdapter(Activity p_context,
			ArrayList<SearchImageVideoVo> p_arrVo, boolean p_isVideo,
			ArrayList<String> p_arrImage, boolean p_isUser) {
		m_context = p_context;
		m_arrDetails = p_arrVo;
		m_arrImages = p_arrImage;
		m_isUser = p_isUser;
		m_isVideo = p_isVideo;
		inflater = (LayoutInflater) m_context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_Loader = new ImageLoader(m_context);
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		p_context.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		width = displayMetrics.widthPixels;

	}

	@Override
	public int getCount() {
		return m_arrDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return m_arrDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		final ViewHolder vh;

		if (v == null) {
			vh = new ViewHolder();
			v = inflater.inflate(R.layout.search_list_row_layout, null);
			vh.m_tvProfileName = (TextView) v
					.findViewById(R.id.slr_tvProfilename);
			vh.m_llLayout = (LinearLayout) v.findViewById(R.id.slr_llLinear);
			vh.m_ivprofilepic = (RecyclingImageView) v
					.findViewById(R.id.slr_rivImage);
			vh.m_ivPlayVideo = (ImageView) v.findViewById(R.id.slr_ivImagePlay);
			v.setTag(vh);
		} else {
			vh = (ViewHolder) v.getTag();
		}

		if (m_isVideo) {
			vh.m_ivPlayVideo.setVisibility(View.VISIBLE);
			vh.m_ivprofilepic.setClickable(false);
		}
		vh.m_ivprofilepic.getLayoutParams().height = (width / 2) - 16;
		vh.m_llLayout.getLayoutParams().height = width / 8;

		vh.m_tvProfileName.setText(m_arrDetails.get(position).getM_sName());
		m_Loader.DisplayImage(m_arrDetails.get(position).getM_sMediaUrl(),
				R.drawable.no_image, vh.m_ivprofilepic);

		if (!m_isVideo) {
			vh.m_ivprofilepic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!m_isUser) {
						Intent in = new Intent(m_context,
								SocialPhotoViewAcitivity.class);
						in.putExtra("pos", position);
						in.putStringArrayListExtra("ImageArray", m_arrImages);
						m_context.startActivity(in);
					} else {
						PeakAboo.m_sFollowerId = m_arrDetails.get(position)
								.getM_sUserId();
						PeakAboo.m_isItemClicked = true;
						((Activity) m_context).finish();
					}
				}
			});
		}
		vh.m_ivPlayVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Video Url=="
						+ m_arrDetails.get(position).getM_sMediaUrl());
				Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(
						Uri.parse(m_arrDetails.get(position).getM_sMediaUrl()),
						"video/*");
				m_context.startActivity(intent);

			}
		});

		return v;
	}

	private static class ViewHolder {
		TextView m_tvProfileName;
		ImageView m_ivprofilepic, m_ivPlayVideo;
		LinearLayout m_llLayout;
	}
}
