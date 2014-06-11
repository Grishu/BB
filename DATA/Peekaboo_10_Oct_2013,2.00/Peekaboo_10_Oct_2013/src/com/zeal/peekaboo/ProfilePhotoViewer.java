package com.zeal.peekaboo;

import com.zeal.peak.obejects.ImageFetcher;
import com.zeal.peak.obejects.RecyclingImageView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ProfilePhotoViewer extends Fragment {
	public static final String IMAGE_DATA_EXTRA = "image_url";
	private String m_imageUrl;
	private ImageView m_ivPhoto;
	private ImageFetcher mImageFetcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		m_imageUrl = getArguments() != null ? getArguments().getString(
				IMAGE_DATA_EXTRA) : null;

	}

	public static ProfilePhotoViewer newInstance(String p_imagUrl) {
		final ProfilePhotoViewer m_prof = new ProfilePhotoViewer();

		final Bundle args = new Bundle();
		args.putString(IMAGE_DATA_EXTRA, p_imagUrl);

		m_prof.setArguments(args);
		return m_prof;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.profile_photo_layout, container,
				false);
		m_ivPhoto = (RecyclingImageView) v.findViewById(R.id.pp_rciImageView);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		if (SocialPhotoViewAcitivity.class.isInstance(getActivity())) {
			mImageFetcher = ((SocialPhotoViewAcitivity) getActivity())
					.getImageFetcher();
			mImageFetcher.loadImage(m_imageUrl, m_ivPhoto);
		}
	}
}
