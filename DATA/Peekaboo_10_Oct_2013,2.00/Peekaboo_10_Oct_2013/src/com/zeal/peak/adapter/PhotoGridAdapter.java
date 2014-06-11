package com.zeal.peak.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;



import android.widget.ImageView.ScaleType;

import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peak.obejects.ImageFetcher;
import com.zeal.peak.obejects.RecyclingImageView;
import com.zeal.peekaboo.R;
import com.zeal.peekaboo.SocialPhotoViewAcitivity;

public class PhotoGridAdapter extends BaseAdapter {

	private Context m_context;
	private ArrayList<String> m_imagUrl;
	ImageFetcher mImageFetcher;
	ImageLoader m_imgLoad;
	final int width;

	public PhotoGridAdapter(Activity p_context, ArrayList<String> p_imagArry) {
		m_context = p_context;
		m_imagUrl = p_imagArry;
		m_imgLoad = new ImageLoader(m_context);
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		p_context.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		width = displayMetrics.widthPixels;
	}

	@Override
	public int getCount() {
		return m_imagUrl.size();
	}

	@Override
	public Object getItem(int position) {
		return m_imagUrl.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View m_view;
		m_view = convertView;
		RecyclingImageView m_ivImage = new RecyclingImageView(m_context);
		m_imgLoad.DisplayImage(m_imagUrl.get(position).toString(),
				R.drawable.home, m_ivImage);

		// System.err.println("Height is===>" + height);
		/*
		 * if (height > 900) m_ivImage.setLayoutParams(new
		 * GridView.LayoutParams(200, 200)); else if (height > 720)
		 * m_ivImage.setLayoutParams(new GridView.LayoutParams(150, 150)); else
		 * if (height > 480) m_ivImage.setLayoutParams(new
		 * GridView.LayoutParams(90, 90));
		 */

		/*
		 * m_ivImage.setLayoutParams(new GridView.LayoutParams((int) m_context
		 * .getResources().getDimension(R.dimen.width), (int) m_context
		 * .getResources().getDimension(R.dimen.height)));
		 * m_ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
		 */
		//m_ivImage.setPadding(2, 2, 2, 2);
		
		m_ivImage.setLayoutParams(new LayoutParams(width/3, (width/3)) );
		m_ivImage.setAdjustViewBounds(true);
		
		m_ivImage.setScaleType(ScaleType.CENTER_CROP);
	
//		m_ivImage.getLayoutParams().height = (width / 3) - 16;
		m_view = m_ivImage;
		m_ivImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Image In Fullscreen==="
						+ m_imagUrl.get(position).toString());
				Intent in = new Intent(m_context,
						SocialPhotoViewAcitivity.class);
				in.putExtra("pos", position);
				in.putStringArrayListExtra("ImageArray", m_imagUrl);
				m_context.startActivity(in);
			}
		});
		return m_view;
	}
}
