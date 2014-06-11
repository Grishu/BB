package com.zeal.peekaboo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.zeal.peak.obejects.ShakeListener;

public class ShakeActivity extends Activity {
	private ShakeListener mShaker;
	private Context m_context;
	private String m_userId;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	private ImageView m_ivBack;
	Vibrator vibe;
	private Handler m_handler;
	Location mylocation = null;
	private ProgressDialog m_prgDialog;
	private double m_latitude, m_longitude;
	boolean execution = true, got = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		m_context = ShakeActivity.this;
		m_prgDialog = new ProgressDialog(m_context);
		m_ivBack = (ImageView) findViewById(R.id.sd_ivBack);
		vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		m_ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish();
				// shakeaction();
				/*if (execution) {
					shakeaction();
					execution = false;
				}*/
			}
		});
		m_handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				handled();
			}
		};
		getLocation();
		m_userId = getIntent().getStringExtra("user_id");

		mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
			public void onShake() {
				if (execution) {
					shakeaction();
					execution = false;
				}
				// getLocation();
			}
		});

	}

	protected void shakeaction() {
		// TODO Auto-generated method stub
		vibe.vibrate(100);
		/*
		 * new AlertDialog.Builder(ShakeActivity.this)
		 * .setPositiveButton(android.R.string.ok, null)
		 * .setMessage("Shooken!").show();
		 */

		Intent m_intentShake = new Intent(m_context, ShakeResultActivity.class);
		m_intentShake.putExtra("user_id", m_userId);
		m_intentShake.putExtra("Latitude", m_latitude);
		m_intentShake.putExtra("Longitude", m_longitude);
		startActivity(m_intentShake);
		finish();
	}

	/**
	 * Get the user's last location if not getting current location else fetch
	 * current location.
	 */
	private void getLocation() {
		m_handler.sendEmptyMessageDelayed(1, 1000 * 60);
		locationManager = (LocationManager) m_context
				.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		mylocation = locationManager.getLastKnownLocation(provider);
		locationManager.requestLocationUpdates(provider, 400, 1,
				locationListener);
		if (mylocation != null) {
			locationListener.onLocationChanged(mylocation);
		}
	}

	protected void handled() {
		// TODO Auto-generated method stub
		if (!got) {
			m_prgDialog.dismiss();
			execution = false;
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(m_context);

			// Setting Dialog Title
			alertDialog.setTitle("Unable to get exact location");

			// Setting Dialog Message
			alertDialog.setMessage("Unable to get exact location try later");
			alertDialog.setNeutralButton("OK", null);
			alertDialog.show();
		}
	}

	/* Class My Location Listener */
	public class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location loc) {
			mylocation = loc;
			m_latitude = loc.getLatitude();
			m_longitude = loc.getLongitude();
			String Text = "My current location is: " + "Latitud = "
					+ loc.getLatitude() + "Longitud = " + loc.getLongitude();
			got = true;
			/*
			 * Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT)
			 * .show();
			 */
			System.out.println("Location Is-------->" + Text);
			// if (execution) {
			// CallShakeWS m_shakeWS = new CallShakeWS();
			// m_shakeWS.execute();
			// shakeaction();
			// execution = false;
			// }
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Disabled",
					Toast.LENGTH_SHORT).show();
			m_prgDialog.dismiss();
			execution = false;
			showSettingsAlert();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Enabled",
					Toast.LENGTH_SHORT).show();
			m_prgDialog.setMessage("Getting Location...");
			m_prgDialog.setTitle(getResources().getString(R.string.app_name));
			m_prgDialog.show();
			execution = true;
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	public void showSettingsAlert() {
		got = true;
		m_handler.removeMessages(1);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(m_context);

		// Setting Dialog Title
		alertDialog.setTitle("GPS  settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						m_context.startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						execution = false;

						dialog.cancel();

					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	@Override
	public void onResume() {
		mShaker.resume();
		super.onResume();
	}

	@Override
	public void onPause() {
		mShaker.pause();
		super.onPause();
		m_handler.removeMessages(1);
		if (locationManager != null) {
			m_handler.removeMessages(1);
			locationManager.removeUpdates(locationListener);
		}
	}

}
