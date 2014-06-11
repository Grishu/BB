package com.zeal.peekaboo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.penq.utils.LoadMoreListView;
import com.penq.utils.LoadMoreListView.OnLoadMoreListener;
import com.zeal.Vo.LookAroundVo;
import com.zeal.peak.adapter.LookAroundAdapter;
import com.zeal.peak.utils.CommonUtils;

public class LookAroundActivity extends FragmentActivity {

	private LoadMoreListView m_lvList;
	private Context m_context;
	private ProgressDialog m_prgDialog;
	private String m_request, m_profileName, dist, m_imagUrl, m_userId,
			m_slistItemId, m_userStatus;
	private ArrayList<LookAroundVo> m_lookArryList;
	private LookAroundAdapter m_adapter;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	private double m_latitude, m_longitude;
	private ImageView m_ivBack;
	boolean execution = true, got = false;
	private Handler m_handler;
	Location mylocation = null;
	private int m_pageVal = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.look_around_layout);

		m_context = LookAroundActivity.this;

		m_lookArryList = new ArrayList<LookAroundVo>();
		m_prgDialog = new ProgressDialog(m_context);
		m_lvList = (LoadMoreListView) findViewById(R.id.la_lvList);

		m_userId = getIntent().getStringExtra("user_id");
		System.out.println("User Id-----" + m_userId);

		m_prgDialog.setMessage("Loading...");
		m_prgDialog.setTitle(getResources().getString(R.string.app_name));
		m_prgDialog.setCancelable(false);
		m_prgDialog.show();

		m_ivBack = (ImageView) findViewById(R.id.larn_ivBack);
		m_ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
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

		m_lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adptr, View view, int pos,
					long id) {
				/*
				 * Toast.makeText(m_context,
				 * String.valueOf(m_lookArryList.get(pos).getM_userId()),
				 * Toast.LENGTH_SHORT).show();
				 */
				m_slistItemId = String.valueOf(m_lookArryList.get(pos)
						.getM_userId());
				Log.e("ID",
						String.valueOf(m_lookArryList.get(pos).getM_userId()));
				PeakAboo.m_sFollowerId = m_slistItemId;
				PeakAboo.m_isItemClicked = true;
				/*
				 * Intent m_intent = new Intent(m_context,
				 * Tabbedmaincontroller.class); m_intent.putExtra("LookAround",
				 * true); m_intent.putExtra("Follower_Id", m_slistItemId);
				 * m_intent.putExtra("user_id", m_userId);
				 * startActivity(m_intent);
				 */
				/*
				 * FragmentTransaction fm = getSupportFragmentManager()
				 * .beginTransaction(); Fragment llf = new
				 * OtherProfileFragment();
				 * System.err.println("Surprise User Id-$$$$$$$$$$ " +
				 * m_userId); // Bundle m_bundle = new Bundle(); //
				 * m_bundle.putString("user_Id", m_slistItemId); //
				 * llf.setArguments(m_bundle);
				 * 
				 * fm.replace(R.id.fragmentswitcherframe, llf); fm.commit();
				 */

				finish();
			}

		});

		m_lvList.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				CallLookAroundWS m_lookAround = new CallLookAroundWS();
				m_lookAround.execute();
			}
		});
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

	/**
	 * Calls the Webservice of Look Around and
	 */
	class CallLookAroundWS extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			m_prgDialog.setMessage("Loading...");
			m_prgDialog.setTitle(getResources().getString(R.string.app_name));
			m_prgDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			m_request = CommonUtils.parseJSON("looksarround.php?uid="
					+ m_userId + "&latitude=" + m_latitude + "&longitude="
					+ m_longitude + "&page=" + m_pageVal);
			if (m_request != null) {
				try {
					JSONObject jo = new JSONObject(m_request);
					Boolean b = jo.getBoolean("Status");

					JSONArray resultarr = jo.getJSONArray("result");
					System.out.println("Status========>" + b + "   Result -->"
							+ resultarr);

					if (b) {
						LookAroundVo m_lookVo;
						for (int i = 0; i < resultarr.length(); i++) {
							JSONObject jObj = resultarr.getJSONObject(i);
							m_profileName = jObj.getString("ProfileName");
							dist = jObj.getString("Distance");
							m_imagUrl = jObj.getString("ImageUrl");
							m_userStatus = jObj.getString("UserDescription");

							m_lookVo = new LookAroundVo();
							m_lookVo.setM_userId(Integer.valueOf(jObj
									.getInt("ID")));
							m_lookVo.setM_profileName(jObj
									.getString("ProfileName"));
							m_lookVo.setM_ImageUrl(jObj.getString("ImageUrl"));
							m_lookVo.setM_distance(jObj.getString("Distance"));
							m_lookVo.setM_statusMesg(jObj
									.getString("UserDescription"));

							m_lookArryList.add(m_lookVo);
							// System.err.println("Profile Name -->"
							// + m_profileName + "\n Distance-->" + dist
							// + "----Image URL==" + m_imagUrl);
							// System.out
							// .println("Size of LookAround Arraylist-->"
							// + m_lookArryList.size());
						}

					} else {
						Toast.makeText(getApplicationContext(),
								jo.getString("result"), Toast.LENGTH_LONG)
								.show();

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return m_request;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			m_adapter.notifyDataSetChanged();

			// m_adapter = new LookAroundAdapter(m_context, m_lookArryList);
			// m_lvList.setAdapter(m_adapter);
			m_pageVal++;
			m_lvList.onLoadMoreComplete();
			m_prgDialog.dismiss();

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		m_adapter = new LookAroundAdapter(m_context, m_lookArryList);
		m_lvList.setAdapter(m_adapter);

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
			got = true;
			String Text = "My current location is: " + "Latitud = "
					+ loc.getLatitude() + "Longitud = " + loc.getLongitude();

			/*
			 * Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT)
			 * .show();
			 */
			System.out.println("Location Is-------->" + Text);
			if (execution) {
				CallLookAroundWS m_lookAround = new CallLookAroundWS();
				m_lookAround.execute();
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
			m_prgDialog.setMessage("Loading...");
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		m_handler.removeMessages(1);
		if (locationManager != null) {
			m_handler.removeMessages(1);
			locationManager.removeUpdates(locationListener);
		}
	}
}
