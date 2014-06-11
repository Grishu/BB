package com.zeal.peekaboo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Camerafilter extends Fragment {

	// private ImageView ivImage;
	private Context m_context;
	public static Bitmap bm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.cameraiconpage, container, false);
		m_context = Camerafilter.this.getActivity();
		Tabbedmaincontroller.m_tvHeader.setText("Send or Upload");
		ImageView msg = (ImageView) v.findViewById(R.id.cf_iv_upld);
		ImageView upld = (ImageView) v.findViewById(R.id.cf_iv_sndmsg);

		upld.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), MessageContactList.class);

				startActivity(i);

			}
		});
		msg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getActivity(), MainActivity1.class);
				in.putExtra("Upldmsg", 1);
				startActivity(in);
			}
		});

		return v;
	}

}
