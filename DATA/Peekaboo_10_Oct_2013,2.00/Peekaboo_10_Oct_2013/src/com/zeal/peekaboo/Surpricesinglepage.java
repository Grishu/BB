package com.zeal.peekaboo;

import com.zeal.peak.obejects.ImageFetcher;
import com.zeal.peak.obejects.ImageWorker;
import com.zeal.peak.obejects.RecyclingImageView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Surpricesinglepage extends Fragment {
	String mImageUrl;
	ImageFetcher mImageFetcher;
	ImageView mImageView;
	static Activity actv;

	public static Surpricesinglepage newInstance(String imageUrl, Activity a) {
		final Surpricesinglepage f = new Surpricesinglepage();

		final Bundle args = new Bundle();
		args.putString("URL", imageUrl);
		f.setArguments(args);
		actv = a;

		return f;
	}

	public Surpricesinglepage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (Tabbedmaincontroller.class.isInstance(getActivity())) {
			mImageFetcher = ((Tabbedmaincontroller) getActivity())
					.getImageFetcher();
			mImageFetcher.loadImage(mImageUrl, mImageView);
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("URL")
				: null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View v = inflater.inflate(R.layout.gvitem, container, false);
		mImageView = (RecyclingImageView) v.findViewById(R.id.gridimage);
		/*
		 * mImageFetcher=new ImageFetcher(actv , 200);
		 * mImageFetcher.loadImage(mImageUrl, mImageView);
		 */
		return v;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mImageView != null) {
			// Cancel any pending image work
			ImageWorker.cancelWork(mImageView);
			mImageView.setImageDrawable(null);
		}
	}

}
