package com.zeal.peekaboo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.zeal.Vo.DiceRollVo;
import com.zeal.peak.utils.CommonUtils;

public class DiceRollActivity extends Activity implements OnGestureListener {

	private GestureDetector m_gestureDetect;
	private Context m_context;
	// private TextView m_tvCount;
	int count = 0;
	private String m_request, m_userVal, m_userId;
	private ProgressDialog m_prgDialog;
	public static ArrayList<DiceRollVo> m_diceArryList;
	private double m_latitude, m_longitude;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	private ImageView m_ivBack, m_ivDice1, m_ivDice2;
	private Handler m_handler;
	Location mylocation = null;
	boolean execution = true, got = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dice_roll_layout);
		m_context = DiceRollActivity.this;
		m_gestureDetect = new GestureDetector(this);
		m_ivBack = (ImageView) findViewById(R.id.dr_ivBack);
		m_ivDice1 = (ImageView) findViewById(R.id.dr_ivdice1);
		m_ivDice2 = (ImageView) findViewById(R.id.dr_ivdice2);
		m_userId = getIntent().getStringExtra("user_id");
		System.out.println("User Id-----" + m_userId);

		m_diceArryList = new ArrayList<DiceRollVo>();
		m_prgDialog = new ProgressDialog(m_context);

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
				super.handleMessage(msg);
				handled();
			}
		};
		m_prgDialog.setMessage("Getting location...");
		m_prgDialog.setCancelable(false);
		m_prgDialog.setTitle(getResources().getString(R.string.app_name));
		m_prgDialog.show();
		getCurrentLocationLatLong();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return m_gestureDetect.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {
		if (start.getRawY() < end.getRawY()) {
			getCurrentLocationLatLong();
			if (execution) {
				CallDiceRollWS m_rollWs = new CallDiceRollWS();
				m_rollWs.execute();
				execution = false;
			}

		} else {

			if (execution) {

				CallDiceRollWS m_rollWs = new CallDiceRollWS();
				m_rollWs.execute();
				execution = false;
			}
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
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

	/*
	 * Call the Dice Roll Webservice.
	 */
	class CallDiceRollWS extends AsyncTask<String, Integer, String> {
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
			// TODO Auto-generated method stub
			/*
			 * m_request = CommonUtils.parseJSON("diceroll.php?uid=" + m_userId
			 * + "&latitude=73.181098&longitude=22.307309");
			 */
			m_request = CommonUtils.parseJSON("diceroll.php?uid=" + m_userId
					+ "&latitude=" + m_latitude + "&longitude=" + m_longitude);
			if (m_request != null) {
				try {
					JSONObject jo = new JSONObject(m_request);
					Boolean b = jo.getBoolean("Status");
					// m_userVal = jo.getString("Users");
					System.out.println("Status========>" + b + "   Result -->"
							+ "User Are:==" + jo.getString("Users"));

					if (b) {
						m_userVal = jo.getString("Users");
						JSONArray resultarr = jo.getJSONArray("result");
						DiceRollVo m_lookVo;
						for (int i = 0; i < resultarr.length(); i++) {
							JSONObject jObj = resultarr.getJSONObject(i);
							// m_profileName = jObj.getString("ProfileName");
							// dist = jObj.getString("Distance");
							// m_imagUrl = jObj.getString("ImageUrl");

							m_lookVo = new DiceRollVo();
							m_lookVo.setM_userId(Integer.valueOf(jObj
									.getInt("ID")));
							m_lookVo.setM_profileName(jObj
									.getString("ProfileName"));
							m_lookVo.setM_ImageUrl(jObj.getString("ImageUrl"));
							m_lookVo.setM_distance(jObj.getString("Distance"));

							m_diceArryList.add(m_lookVo);
							// System.err.println("Profile Name -->"
							// + m_profileName + "\n Distance-->" + dist
							// + "----Image URL==" + m_imagUrl);
							System.out
									.println("Size of LookAround Arraylist-->"
											+ m_diceArryList.size());
						}

					} else {
						Toast.makeText(getApplicationContext(),
								jo.getString("result"), Toast.LENGTH_LONG)
								.show();
						m_userVal = null;
						// m_userVal=jo.getString("result");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return m_userVal;
		}

		@Override
		protected void onPostExecute(final String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			System.err.println("onPostExecute===>" + result);
			if (result != null) {
				setDiceImages(Integer.valueOf(result));

				startActivity(new Intent(m_context,
						DiceRollDetailsActivity.class));
				// finish();
			} else {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						m_context);

				// Setting Dialog Title
				alertDialog.setTitle(getResources()
						.getString(R.string.app_name));

				// Setting Dialog Message
				alertDialog.setMessage("No users found.");
				alertDialog.setNeutralButton("OK", null);
				alertDialog.show();
			}
			m_prgDialog.dismiss();
		}
	}

	private void setDiceImages(int p_val) {
		switch (p_val) {
		case 1:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice01));

			break;
		case 2:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice01));
			m_ivDice2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice01));
			break;
		case 3:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice01));
			m_ivDice2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice02));
			break;
		case 4:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice02));
			m_ivDice2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice02));
			break;
		case 5:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice03));
			m_ivDice2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice02));
			break;
		case 6:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice03));
			m_ivDice2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice03));

			break;
		case 7:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice04));
			m_ivDice2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice03));

			break;
		case 8:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice04));
			m_ivDice2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice04));

			break;
		case 9:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice05));
			m_ivDice2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice04));

			break;
		case 10:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice05));
			m_ivDice2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice05));

			break;
		case 11:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice06));
			m_ivDice2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice05));

			break;
		case 12:
			m_ivDice1.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice06));
			m_ivDice2.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.dice06));

			break;
		default:
			break;
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
			m_prgDialog.dismiss();
			execution = true;
			got = true;
			/*
			 * Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT)
			 * .show();
			 */
			System.out.println("Location Is-------->" + Text);
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

	/**
	 * Call the Location services to get the user's current loacation.
	 */
	private void getCurrentLocationLatLong() {
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
//		m_diceArryList.clear();
		m_handler.removeMessages(1);
		if (locationManager != null) {
			m_handler.removeMessages(1);
			locationManager.removeUpdates(locationListener);
		}

	}
}
