package com.zeal.peekaboo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.zeal.peak.utils.CommonUtils;

@SuppressLint("NewApi")
public class Setting extends Fragment {

	private Button m_btnLogout, m_btnFindFriends, 
			m_btnPreference, m_btnMyAccount, m_btnPrivacy;
	private Context context;
	private String m_userId;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.settings, container, false);
		context = Setting.this.getActivity();
		Tabbedmaincontroller.m_ivback.setVisibility(View.VISIBLE);
		Tabbedmaincontroller.m_ivsetting.setVisibility(View.GONE);
		Tabbedmaincontroller.m_tvHeader.setText("Settings");
		m_btnLogout = (Button) v.findViewById(R.id.logout);
		m_btnFindFriends = (Button) v.findViewById(R.id.findfriends);
		m_btnPreference = (Button) v.findViewById(R.id.preferences);
		m_btnMyAccount = (Button) v.findViewById(R.id.myaccount);
		m_btnPrivacy = (Button) v.findViewById(R.id.privacy);

		m_userId = getArguments().getString("user_Id");
		m_btnLogout.setOnClickListener(m_ClickListener);
		m_btnFindFriends.setOnClickListener(m_ClickListener);
		m_btnPreference.setOnClickListener(m_ClickListener);
		m_btnMyAccount.setOnClickListener(m_ClickListener);
		m_btnPrivacy.setOnClickListener(m_ClickListener);
		return v;
	}

	/**
	 * Common click listener
	 */
	OnClickListener m_ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.myaccount:
				startActivity(new Intent(context, MyAccountActivity.class)
						.putExtra("user_Id", m_userId));
				break;
			case R.id.findfriends:
				startActivity(new Intent(context,
						FindFriendsLinkAccountAcitivity.class));
				break;
			case R.id.preferences:

				break;
			case R.id.logout:
				CommonUtils.setStringSharedPref(getActivity(), "user_ID", "");

				Intent i = new Intent(context, MainActivity.class);
				startActivity(i);
				getActivity().finish();
				break;
			case R.id.privacy:
				startActivity(new Intent(context,
						PrivacyStatementActivity.class));
				break;
			default:
				break;
			}

		}
	};
}
