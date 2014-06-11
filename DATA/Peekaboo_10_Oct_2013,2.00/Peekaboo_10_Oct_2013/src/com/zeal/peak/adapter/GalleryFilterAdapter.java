package com.zeal.peak.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.zeal.peekaboo.R;

public class GalleryFilterAdapter extends BaseAdapter {

	private Integer[] mFilterIds = { R.drawable.postarize,
			R.drawable.saturation, R.drawable.brightness, R.drawable.contrast,
			R.drawable.gamma, R.drawable.sepia };
	private Context mContext;

	public GalleryFilterAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		return mFilterIds.length;

	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ImageView i = new ImageView(mContext);

		i.setImageResource(mFilterIds[arg0]);
		i.setLayoutParams(new Gallery.LayoutParams(170, 170));

		i.setScaleType(ImageView.ScaleType.FIT_XY);

		return i;
	}

}
