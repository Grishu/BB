package com.zeal.peak.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.zeal.peekaboo.R;

public class GalleryImageAdapter extends BaseAdapter {
	private Context mContext;

	private Integer[] mImageIds = { R.color.spring_green, R.color.cyan,
			R.color.chartreuse, R.color.azure, R.color.blue,
			R.color.bright_pink, R.color.chartreuse, R.color.orange,
			R.color.violet, R.color.dark_green, R.color.grey, R.color.parrot };

	public GalleryImageAdapter(Context context) {
		mContext = context;

	}

	public int getCount() {
		return mImageIds.length;

	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
 
	// Override this method according to your need
	public View getView(int index, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		ImageView i = new ImageView(mContext);

		i.setImageResource(mImageIds[index]);
		i.setLayoutParams(new Gallery.LayoutParams(60, 70));

		i.setScaleType(ImageView.ScaleType.FIT_XY);

		return i;
	}
}
