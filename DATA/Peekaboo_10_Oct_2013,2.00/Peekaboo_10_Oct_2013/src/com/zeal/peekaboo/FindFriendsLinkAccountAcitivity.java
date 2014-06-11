package com.zeal.peekaboo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class FindFriendsLinkAccountAcitivity extends Activity {
	private Context m_context;
	private ImageView m_ivBack;
	private Button m_btnAddressMatch, m_btnFacebook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findfriendsandlinkaccounts);
		m_context = FindFriendsLinkAccountAcitivity.this;
		m_ivBack = (ImageView) findViewById(R.id.ff_ivBack);
		m_btnAddressMatch = (Button) findViewById(R.id.ff_btnAddressBookmatching);
		m_btnFacebook = (Button) findViewById(R.id.ff_btnFacebook);

		m_ivBack.setOnClickListener(m_ClickListener);
		m_btnAddressMatch.setOnClickListener(m_ClickListener);
		m_btnFacebook.setOnClickListener(m_ClickListener);

	}

	/**
	 * Common Click listener.
	 */
	OnClickListener m_ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ff_btnAddressBookmatching:
				startActivity(new Intent(m_context,
						AddressBookMatchingContactActivity.class));
				break;
			case R.id.ff_ivBack:
				finish();
				break;
			case R.id.ff_btnFacebook:
				
				break;
			default:
				break;
			}
		}
	};
}
