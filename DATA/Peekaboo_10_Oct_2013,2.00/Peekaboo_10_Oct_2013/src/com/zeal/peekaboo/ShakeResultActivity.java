package com.zeal.peekaboo;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zeal.Vo.LookAroundVo;
import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peak.utils.CommonUtils;

public class ShakeResultActivity extends Activity {

	private Context m_context;
	private String m_userId, m_request, m_resultData, m_slistItemId;
	private ProgressDialog m_prgDialog;
	private ArrayList<LookAroundVo> m_lookArryList;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	private ImageView m_ivImage, m_ivBack;
	private TextView m_tvProfName, m_tvDistance, m_tvNoData;
	private ImageLoader imgLoader;
	private double m_latitude, m_longitude, m_value;
	private RelativeLayout m_rlShakeResult;
	boolean execution = true, got = false;
	Location mylocation = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake_details_layout);
		m_context = ShakeResultActivity.this;
		m_prgDialog = new ProgressDialog(m_context);
		m_lookArryList = new ArrayList<LookAroundVo>();
		m_userId = getIntent().getStringExtra("user_id");
		m_latitude = getIntent().getDoubleExtra("Latitude", 0);
		m_longitude = getIntent().getDoubleExtra("Longitude", 0);
		imgLoader = new ImageLoader(m_context);

		m_tvProfName = (TextView) findViewById(R.id.sdl_tvProfileName);
		m_tvDistance = (TextView) findViewById(R.id.sdl_tvDistance);
		m_tvNoData = (TextView) findViewById(R.id.sdl_tvNoData);
		m_ivImage = (ImageView) findViewById(R.id.sdl_ivprofImage);
		m_ivBack = (ImageView) findViewById(R.id.sdl_ivBack);
		m_rlShakeResult = (RelativeLayout) findViewById(R.id.sdl_rlLayoutShake);

		/*m_handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				handled();
			}
		};
		getLocation();*/
		 CallShakeWS m_shakeWS = new CallShakeWS();
		 m_shakeWS.execute();

		m_rlShakeResult.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				for (int i = 0; i < m_lookArryList.size(); i++) {
					m_slistItemId = String.valueOf(m_lookArryList.get(i)
							.getM_userId());
					Log.e("ID",
							String.valueOf(m_lookArryList.get(i).getM_userId()));
					PeakAboo.m_sFollowerId = m_slistItemId;
					PeakAboo.m_isItemClicked = true;
					finish();
				}

			}
		});
		m_ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * Get the user's last location if not getting current location else fetch
	 * current location.
	 */
	/*private void getLocation() {
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
	}*/

	/*
	 * Call the shake webservice and parse the json response.
	 */
	class CallShakeWS extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			m_prgDialog.setMessage("Loading...");
			m_prgDialog.setCancelable(false);
			m_prgDialog.setTitle(getResources().getString(R.string.app_name));
			m_prgDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://zealousys.com/PeekaBoo_Webservice/shake.php?uid=56&latitude=73.181098&longitude=22.307309
			m_request = CommonUtils.parseJSON("shake.php?uid=" + m_userId
					+ "&latitude=" + m_latitude + "&longitude=" + m_longitude);
			// m_request = CommonUtils
			// .parseJSON("shake.php?uid=76&latitude=73.181098&longitude=22.307309");
			if (m_request != null) {
				try {
					JSONObject jo = new JSONObject(m_request);
					Boolean b = jo.getBoolean("Status");

					m_resultData = jo.getString("result");

					System.out.println("Status========>" + b + "   Result -->"
							+ m_resultData);

					if (b) {
						JSONArray resultarr = jo.getJSONArray("result");
						LookAroundVo m_lookVo;
						for (int i = 0; i < resultarr.length(); i++) {
							JSONObject jObj = resultarr.getJSONObject(i);

							m_lookVo = new LookAroundVo();
							m_lookVo.setM_userId(Integer.valueOf(jObj
									.getString("ID")));
							m_lookVo.setM_profileName(jObj
									.getString("ProfileName"));
							m_lookVo.setM_ImageUrl(jObj.getString("ImageUrl"));
							m_lookVo.setM_distance(jObj.getString("Distance"));

							m_lookArryList.add(m_lookVo);
							System.err.println("Profile Name -->"
									+ jObj.getString("ProfileName")
									+ "\n Distance-->"
									+ jObj.getString("Distance")
									+ "----Image URL=="
									+ jObj.getString("ImageUrl"));
							// System.out
							// .println("Size of LookAround Arraylist-->"+
							// m_lookArryList.size());
						}

					} else {
						m_resultData = jo.getString("result");

						Log.e("Error", jo.getString("result"));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return m_resultData;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null) {
				if (m_lookArryList.size() > 0) {
					m_rlShakeResult.setVisibility(View.VISIBLE);
					m_tvNoData.setVisibility(View.INVISIBLE);
					for (int i = 0; i < m_lookArryList.size(); i++) {

						m_value = Double
								.parseDouble(new DecimalFormat("000.00")
										.format(Double.valueOf(m_lookArryList
												.get(i).getM_distance().trim()
												.toString())));

						m_tvProfName.setText(m_lookArryList.get(i)
								.getM_profileName());
						m_tvDistance.setText(String.valueOf(m_value) + "KM");
						imgLoader.DisplayImage(m_lookArryList.get(i)
								.getM_ImageUrl().toString(),
								R.drawable.no_image, m_ivImage);
					}
				} else {
					m_rlShakeResult.setVisibility(View.INVISIBLE);
					m_tvNoData.setVisibility(View.VISIBLE);
					m_tvNoData.setText(result);
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							m_context);

					// Setting Dialog Title
					alertDialog.setTitle(getResources().getString(
							R.string.app_name));

					// Setting Dialog Message
					alertDialog.setMessage("No users currently Shaking");
					alertDialog.setNeutralButton("OK", null);
					alertDialog.show();
					// finish();
					// Toast.makeText(m_context, "No users currently Shaking",
					// Toast.LENGTH_LONG).show();
				}

			}
			// Toast.makeText(m_context, result, Toast.LENGTH_LONG).show();
			// m_adapter = new LookAroundAdapter(m_context, m_lookArryList);
			// m_lvList.setAdapter(m_adapter);

			m_prgDialog.dismiss();
		}

	}

	/* Class My Location Listener */
	/*public class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location loc) {
			mylocation = loc;
			m_latitude = loc.getLatitude();
			m_longitude = loc.getLongitude();
			String Text = "My current location is: " + "Latitud = "
					+ loc.getLatitude() + "Longitud = " + loc.getLongitude();
			got = true;
			
			 * Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT)
			 * .show();
			 
			System.out.println("Location Is-------->" + Text);
			if (execution) {
				CallShakeWS m_shakeWS = new CallShakeWS();
				m_shakeWS.execute();
				execution = false;
			}
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
	}*/

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/*m_handler.removeMessages(1);
		if (locationManager != null) {
			m_handler.removeMessages(1);
			locationManager.removeUpdates(locationListener);
		}*/

	}
}
