package com.zeal.peekaboo;

import com.zeal.peak.utils.CommonUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class PrivacyStatementActivity extends Activity {

	private Context m_context;
	private ImageView m_ivBack;
	private ToggleButton m_cbGPS;
	private boolean m_isLocOn=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privacy_layout);
		m_context = PrivacyStatementActivity.this;
		m_ivBack = (ImageView) findViewById(R.id.prl_ivBack);
		m_cbGPS = (ToggleButton) findViewById(R.id.prl_tbCheck);

		m_ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});

		m_isLocOn = CommonUtils.getBooleanSharedPref(m_context, "Location",
				true);
		System.out.println("Location service is======"+m_isLocOn);
		if (m_isLocOn) {
			m_cbGPS.setChecked(true);
		} else {
			m_cbGPS.setChecked(false);
		}
		m_cbGPS.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					CommonUtils.setBooleanSharedPref(m_context, "Location",
							true);
				} else {
					CommonUtils.setBooleanSharedPref(m_context, "Location",
							false);
				}
			}
		});
	}

}
