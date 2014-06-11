package com.zeal.peekaboo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.LoggingBehaviors;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.zeal.peak.utils.CommonUtils;
import com.zeal.peak.utils.CustomValidator;

public class MainActivity extends Activity {

	private String username, password, m_userId, m_urlResponse, m_response,
			m_EmailUname, m_ID, m_url, m_sName, m_sprofUrl;
	private ProgressDialog pd;
	private Button m_btnLogin, m_btnRegister, m_btnFBLogin;
	private Context m_context;
	private EditText etuname, etpass;
	private TextView m_tvforgtPass;
	private InputMethodManager imm;
	private SessionStatusCallback statusCallback = new SessionStatusCallback();
	private static final String URL_PREFIX_LOGIN = "https://graph.facebook.com/me?fields=id,name,picture&access_token=";
	JSONObject jsonObject = null;
	Bundle m_savedinstance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		m_context = MainActivity.this;
		m_savedinstance = savedInstanceState;
		pd = new ProgressDialog(m_context);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		m_btnLogin = (Button) findViewById(R.id.btn_login);
		m_btnRegister = (Button) findViewById(R.id.btn_register);
		m_btnFBLogin = (Button) findViewById(R.id.btn_fblogin);
		etuname = (EditText) findViewById(R.id.et_uname);
		etpass = (EditText) findViewById(R.id.et_pass);
		m_tvforgtPass = (TextView) findViewById(R.id.tv_frgtpassword);

		m_btnLogin.setOnClickListener(m_ClickListener);
		m_btnRegister.setOnClickListener(m_ClickListener);
		m_tvforgtPass.setOnClickListener(m_ClickListener);
		m_btnFBLogin.setOnClickListener(m_ClickListener);
	}

	/**
	 * Common Click Listener
	 */
	OnClickListener m_ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_login:
				imm.hideSoftInputFromWindow(m_btnLogin.getWindowToken(), 0);
				username = etuname.getText().toString();
				password = etpass.getText().toString();
				CustomValidator.m_isError = false;

				CustomValidator.validateForEmptyValue(etpass,
						"Please enter password");
				// CustomValidator.validateEmail(etuname,
				// "Please enter email address",
				// "Please enter valid email address.");
				if (!CustomValidator.m_isError) {
					Logintask lt = new Logintask();
					lt.execute();
				}
				// if (CommonUtils.IsNetConnected()) {

				// }
				// else{
				// Toast.makeText(m_context, "No Network Connection.",
				// Toast.LENGTH_LONG).show();
				// }

				break;
			case R.id.btn_register:
				startActivity(new Intent(m_context, Register_activity.class));
				break;

			case R.id.tv_frgtpassword:
				showForgetPasswordDialog();
				break;
			case R.id.btn_fblogin:
				Settings.addLoggingBehavior(LoggingBehaviors.INCLUDE_ACCESS_TOKENS);

				Session session = Session.getActiveSession();
				if (session == null) {
					if (m_savedinstance != null) {
						session = Session.restoreSession(m_context, null,
								statusCallback, m_savedinstance);
					}
					if (session == null) {
						session = new Session(m_context);
					}
					Session.setActiveSession(session);
					if (session.getState().equals(
							SessionState.CREATED_TOKEN_LOADED)) {
						session.openForRead(new Session.OpenRequest(
								MainActivity.this).setCallback(statusCallback));
					}
				}
				updateView();
				break;
			default:
				break;
			}

		}
	};

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			updateView();
		}
	}

	private void updateView() {
		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			// textInstructionsOrLink.setText(m_getUrl +
			// session.getAccessToken());
			// m_url = parseJSON(m_getUrl + session.getAccessToken());
			callRequest m_rs = new callRequest();
			m_rs.execute(session.getAccessToken());

		} else {
			m_btnFBLogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					onClickLogin();
				}
			});
		}
	}

	private void onClickLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this)
					.setCallback(statusCallback));
		} else {
			Session.openActiveSession(this, true, statusCallback);
		}
	}

	class callRequest extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// showprogress();
			pd.setMessage("Please wait");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			m_url = parseJSON(URL_PREFIX_LOGIN + params[0]);
			return m_url;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			try {
				jsonObject = new JSONObject(m_url);

				System.out.println("ID=" + jsonObject.getString("id")
						+ "NAME==>" + jsonObject.getString("name") + "Pic=="
						+ jsonObject.getString("picture"));
				m_sName = jsonObject.getString("name");

				JSONObject jobj = new JSONObject(
						jsonObject.getString("picture"));
				JSONObject m_dataobj = new JSONObject(jobj.getString("data"));
				System.out.println("^&&&&&&&&&&&&&&&&&&  "
						+ m_dataobj.getString("url"));
				m_sprofUrl = m_dataobj.getString("url");
				pd.dismiss();

				FacebookLoginWS m_fbWS = new FacebookLoginWS();
				m_fbWS.execute(jsonObject.getString("id"));

				/*
				 * startActivity(new Intent(m_context, Register_activity.class)
				 * .putExtra("Name", m_sName).putExtra("profPic", m_sprofUrl));
				 */
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Call the Webservice read the Json response and return the response in
	 * string.
	 */
	public static String parseJSON(String p_url) {
		JSONObject jsonObject = null;
		String json = null;
		try {
			// Create a new HTTP Client
			DefaultHttpClient defaultClient = new DefaultHttpClient();
			// Setup the get request
			HttpGet httpGetRequest = new HttpGet(p_url);
			System.out.println("Request URL--->" + p_url);
			// Execute the request in the client
			HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
			// Grab the response
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent(), "UTF-8"));
			json = reader.readLine();
			System.err.println("JSON Response--->" + json);
			// Instantiate a JSON object from the request response
			jsonObject = new JSONObject(json);

		} catch (Exception e) {
			// In your production code handle any errors and catch the
			// individual exceptions
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * Facebook Login Webservice calling and parsing response.
	 */
	public class FacebookLoginWS extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// showprogress();
			pd.setMessage("Please wait");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/login_fb.php?fbuid=100006607508262
			String abcd = "", url = "";
			m_ID = params[0];
			url = "login_fb.php?fbuid=" + params[0];
			abcd = CommonUtils.parseJSON(url);
			System.out.println("Response------->" + abcd);
			return abcd;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result != null) {
				try {
					JSONObject jo = new JSONObject(result);
					Boolean b = jo.getBoolean("Status");
					if (b) {
						JSONObject resultarr = jo.getJSONObject("result");
						System.err.println("Result -->" + resultarr);
						m_userId = resultarr.getString("user_id");

						CommonUtils.setStringSharedPref(m_context, "user_ID",
								m_userId);
						PeakAboo.m_UserID = m_userId;
						System.err.println("User id -->" + m_userId);
						// Toast.makeText(getApplicationContext() ,"success",
						// Toast.LENGTH_LONG).show();

						startActivity(new Intent(MainActivity.this,
								Tabbedmaincontroller.class).putExtra("userId",
								PeakAboo.m_UserID));
						finish();

					} else {
						Toast.makeText(m_context, "Oops, try again!",
								Toast.LENGTH_LONG).show();
						callRegisterFBWS m_fbRegis = new callRegisterFBWS();
						m_fbRegis.execute(m_ID);

						/*
						 * startActivity(new Intent(m_context,
						 * Register_activity.class).putExtra("Name",
						 * m_sName).putExtra("profPic", m_sprofUrl));
						 */
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			pd.dismiss();
		}
	}

	class callRegisterFBWS extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// showprogress();
			pd.setMessage("Please wait");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/register_fb.php?uname=Sanjay&fbuid=100006607508262
			String m_fbUsername = m_sName.replaceAll(" ", "%20");
			String m_result = CommonUtils.parseJSON("register_fb.php?uname="
					+ m_fbUsername + "&fbuid=" + params[0]);

			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				try {
					JSONObject jo = new JSONObject(result);
					Boolean b = jo.getBoolean("Status");
					if (b) {
						Toast.makeText(m_context, jo.getString("result"),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(m_context, jo.getString("result"),
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			pd.dismiss();
		}
	}

	/**
	 * Login Webservice calling and parsing response.
	 */
	public class Logintask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// showprogress();
			pd.setMessage("Logging in...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String abcd = "", url = "";
			url = "login.php?userlogin=" + username + "&password=" + password;
			abcd = CommonUtils.parseJSON(url);
			System.out.println("Response------->" + abcd);

			return abcd;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pd.dismiss();
			// Log.e("result", result);
			if (result != null) {
				try {
					JSONObject jo = new JSONObject(result);
					Boolean b = jo.getBoolean("Status");
					if (b) {
						JSONObject resultarr = jo.getJSONObject("result");
						System.err.println("Result -->" + resultarr);
						m_userId = resultarr.getString("user_id");
						CommonUtils.setStringSharedPref(m_context, "user_ID",
								m_userId);
						PeakAboo.m_UserID = m_userId;
						System.err.println("User id -->" + m_userId);
						// Toast.makeText(getApplicationContext() ,"success",
						// Toast.LENGTH_LONG).show();
						startActivity(new Intent(MainActivity.this,
								Tabbedmaincontroller.class).putExtra("userId",
								PeakAboo.m_UserID));
						finish();
					} else {
						Toast.makeText(m_context, "Oops, try again!",
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			} else {

			}
		}
	}

	/**
	 * Shows a Forget Password dialog.
	 */
	private void showForgetPasswordDialog() {
		// custom dialog
		final Dialog dialog = new Dialog(m_context, R.style.Theme_Dialog);
		dialog.setContentView(R.layout.forgetpassword_dialog_layout);
		dialog.setCancelable(false);

		final EditText m_etUsernameEmail = (EditText) dialog
				.findViewById(R.id.fdl_etUname);

		Button m_btndialogCancel = (Button) dialog
				.findViewById(R.id.fdl_btnCancel);
		Button m_btnSubmit = (Button) dialog.findViewById(R.id.fdl_btnSubmit);
		// if button is clicked, close the custom dialog
		m_btndialogCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		m_btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				CustomValidator.m_isError = false;
				CustomValidator.validateForEmptyValue(m_etUsernameEmail,
						"Please enter username or Email");
				if (!CustomValidator.m_isError) {
					m_EmailUname = m_etUsernameEmail.getText().toString();
					callForgetPasswordWS m_callWS = new callForgetPasswordWS();
					m_callWS.execute(m_EmailUname);
					dialog.dismiss();
				}

			}
		});
		dialog.show();
	}

	@Override
	public void onStart() {
		super.onStart();
		// Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	public void onStop() {
		super.onStop();
		// Session.getActiveSession().removeCallback(statusCallback);
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().closeAndClearTokenInformation();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	/**
	 * Call Forget password webservice and parse its response and send the
	 * Password in email.
	 * 
	 */
	public class callForgetPasswordWS extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd.setMessage("Sending mail...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://zealousys.com/PeekaBoo_Webservice/forgetpass.php?username=iron@gmail.com
			System.out.println("Params===" + params[0]);
			m_urlResponse = "forgetpass.php?username=" + params[0];
			m_response = CommonUtils.parseJSON(m_urlResponse);
			System.out.println("Response------->" + m_response);

			return m_response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result != null) {
				try {
					JSONObject jo = new JSONObject(result);
					Boolean b = jo.getBoolean("Status");
					if (b) {
						// Toast.makeText(m_context, jo.getString("result"),
						// Toast.LENGTH_SHORT).show();
						Toast.makeText(m_context,
								"You will be mailed your password shortly.",
								Toast.LENGTH_SHORT).show();

						Intent shareIntent = new Intent(
								android.content.Intent.ACTION_SEND);
						shareIntent.setType("text/plain");
						shareIntent.putExtra(
								android.content.Intent.EXTRA_SUBJECT,
								"Peak'a'Boo");
						shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
								m_EmailUname);
						PackageManager pm = m_context.getPackageManager();
						List<ResolveInfo> activityList = pm
								.queryIntentActivities(shareIntent, 0);
						for (final ResolveInfo app : activityList) {
							if ((app.activityInfo.name).contains("gmail")) {
								final ActivityInfo activity = app.activityInfo;
								final ComponentName name = new ComponentName(
										activity.applicationInfo.packageName,
										activity.name);
								shareIntent
										.addCategory(Intent.CATEGORY_LAUNCHER);
								shareIntent
										.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
												| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								shareIntent.setComponent(name);
								m_context.startActivity(shareIntent);
								break;
							}
						}
					} else {
						Toast.makeText(m_context, jo.getString("result"),
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			pd.dismiss();

		}
	}
}