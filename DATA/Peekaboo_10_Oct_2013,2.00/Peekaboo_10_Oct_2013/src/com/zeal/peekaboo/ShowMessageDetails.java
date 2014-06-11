package com.zeal.peekaboo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peak.obejects.RecyclingImageView;

public class ShowMessageDetails extends Activity {
	private Context m_context;
	private ImageView m_ivBack;
	private RecyclingImageView m_ivImage;
	private String m_Url;
	private ImageLoader m_loader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_details);
		m_context = ShowMessageDetails.this;

		m_Url = getIntent().getStringExtra("MediaUrl");
		System.out.println("Url===" + m_Url);
		m_ivBack = (ImageView) findViewById(R.id.md_ivBack);
		m_ivImage = (RecyclingImageView) findViewById(R.id.md_ivImage);
		m_loader = new ImageLoader(m_context, 512);

		/*
		 * if (m_Url.contains(".mp4")) { Intent intent = new
		 * Intent(Intent.ACTION_VIEW).setDataAndType( Uri.parse(m_Url),
		 * "video/*"); startActivity(intent); } else {
		 */
		m_loader.DisplayImage(m_Url, R.drawable.home, m_ivImage);
		// }
		m_ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
}
