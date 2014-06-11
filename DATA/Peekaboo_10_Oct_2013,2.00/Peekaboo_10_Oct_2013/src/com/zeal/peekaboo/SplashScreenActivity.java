package com.zeal.peekaboo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gcm.GCMRegistrar;
import com.zeal.peak.utils.CommonUtils;

public class SplashScreenActivity extends Activity {

	// used to know if the back button was pressed in the splash screen activity
	// and avoid opening the next activity
	private boolean mIsBackButtonPressed = false;
	private static final int SPLASH_DURATION = 2000; // 2 seconds
	private String m_prefUserID;
	private Context m_context;

	String rid;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.actvity_splashscreen);

		m_context = SplashScreenActivity.this;
		m_prefUserID = CommonUtils.getStringSharedPref(getApplicationContext(),
				"user_ID", "");
		if (CommonUtils.checkConnection(m_context)) {

			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			rid = GCMRegistrar.getRegistrationId(getApplicationContext());
			if (rid.equals("")) {
				GCMRegistrar.register(getApplicationContext(), "883601372525");
				while (!GCMRegistrar.getRegistrationId(getApplicationContext())
						.equals("")) {

				}

			} else {

				/* Toast.makeText(m_context, rid, Toast.LENGTH_LONG).show(); */
			}

			System.out.println("User ID --------->" + m_prefUserID);
			Handler handler = new Handler();

			// run a thread after 2 seconds to start the home screen
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {

					// make sure we close the splash screen so the user won't
					// come
					// back when it presses back key

					if (!mIsBackButtonPressed) {
						// start the home screen if the back button wasn't
						// pressed
						// already
						if (m_prefUserID.equalsIgnoreCase("")) {
							Intent intent = new Intent(m_context,
									MainActivity.class);
							startActivity(intent);
							finish();
						} else {
							PeakAboo.m_UserID = m_prefUserID;
							Intent intent = new Intent(m_context,
									Tabbedmaincontroller.class);
							startActivity(intent);
							finish();
						}

					}

				}

			}, SPLASH_DURATION); // time in milliseconds (1 second = 1000
									// milliseconds) until the run() method will
									// be
									// called
		} else {
			showAlert("No Network Available.");
		}
	}

	private void showAlert(String p_mesg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(m_context);

		// Dialog Title
		alertDialog.setTitle(getResources().getString(R.string.app_name));
		// Dialog Message
		alertDialog.setMessage(p_mesg);
		alertDialog.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				});
		alertDialog.show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		mIsBackButtonPressed = true;
	}

}
