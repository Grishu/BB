package com.zeal.peekaboo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeal.peak.obejects.AsyncTask;
import com.zeal.peak.utils.CommonUtils;
import com.zeal.peak.utils.CustomValidator;

public class MyAccountActivity extends Activity {

	private Context m_context;
	private String m_userValue, m_result, m_updateResult;
	private ProgressDialog m_prgDialog;
	private EditText m_etUsername, m_etEmail, m_etAlterEmail, m_etPhone;
	private ImageView m_ivBack, m_ivUpdate;
	private TextView m_tvPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myaccount_layout);
		m_context = MyAccountActivity.this;
		m_prgDialog = new ProgressDialog(m_context);
		m_userValue = getIntent().getStringExtra("user_Id");

		m_etEmail = (EditText) findViewById(R.id.ml_etEmail);
		m_etUsername = (EditText) findViewById(R.id.ml_etUsername);
		m_etAlterEmail = (EditText) findViewById(R.id.ml_etAlternateEmail);
		m_etPhone = (EditText) findViewById(R.id.ml_etPhone);
		m_tvPassword = (TextView) findViewById(R.id.ml_tvPassword);
		m_ivUpdate = (ImageView) findViewById(R.id.ml_ivSubmit);
		m_ivBack = (ImageView) findViewById(R.id.ml_ivBack);

		callMyAccountWS m_account = new callMyAccountWS();
		m_account.execute();

		m_ivUpdate.setOnClickListener(m_onClickListener);
		m_ivBack.setOnClickListener(m_onClickListener);
		m_tvPassword.setOnClickListener(m_onClickListener);

	}

	/**
	 * Common click listener
	 */
	OnClickListener m_onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ml_ivSubmit:
				CustomValidator.m_isError = false;

				CustomValidator.validateForEmptyValue(m_etUsername,
						"Please enter Username");
				CustomValidator.validateForEmptyValue(m_etPhone,
						"Please enter Phone number");
				CustomValidator.validateEmail(m_etEmail,
						"Please enter email address",
						"Please enter valid email address.");
				if (!CustomValidator.m_isError) {
					callUpdateWS m_update = new callUpdateWS();
					m_update.execute();
				}

				break;
			case R.id.ml_ivBack:
				finish();
				break;
			case R.id.ml_tvPassword:
				startActivity(new Intent(m_context,
						ChangePasswordActivity.class).putExtra("userId",
						m_userValue));
				break;

			default:
				break;
			}

		}
	};

	class callMyAccountWS extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			m_prgDialog.setTitle(getResources().getString(R.string.app_name));
			m_prgDialog.setMessage("Loading...");
			m_prgDialog.setCancelable(false);
			m_prgDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/myaccount.php?uid=76
			m_result = CommonUtils
					.parseJSON("myaccount.php?uid=" + m_userValue);
			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (m_result != null) {
				try {
					JSONObject m_jOb = new JSONObject(result);
					boolean m_status = m_jOb.getBoolean("Status");
					if (m_status) {
						JSONArray m_arry = m_jOb.getJSONArray("result");
						for (int i = 0; i < m_arry.length(); i++) {
							JSONObject m_mydetail = m_arry.getJSONObject(i);
							System.out.println("Account Detail=="
									+ m_mydetail.getString("UserName")
									+ "Email==="
									+ m_mydetail.getString("UserEmail"));
							m_etEmail
									.setText(m_mydetail.getString("UserEmail"));
							m_etUsername.setText(m_mydetail
									.getString("UserName"));
							if (!m_mydetail.getString("Phone").toString()
									.equalsIgnoreCase("NoPhone")) {
								m_etPhone
										.setText(m_mydetail.getString("Phone"));

							} else if (m_mydetail.getString("Phone").toString()
									.equalsIgnoreCase("NoPhone")) {
								m_etPhone.setHint("Enter your phone number");
							}
							if (!m_mydetail.getString("AlternateEmail")
									.toString().equalsIgnoreCase("NoEmail")) {
								m_etAlterEmail.setText(m_mydetail
										.getString("AlternateEmail"));
							} else if (m_mydetail.getString("AlternateEmail")
									.toString().equalsIgnoreCase("NoEmail")) {
								m_etAlterEmail
										.setHint("Enter your alternate email");
							}
						}
					} else {

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			m_prgDialog.dismiss();
		}
	}

	class callUpdateWS extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			m_prgDialog.setTitle(getResources().getString(R.string.app_name));
			m_prgDialog.setMessage("Loading...");
			m_prgDialog.setCancelable(false);
			m_prgDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/myaccount_edit.php?uid=105&uname=Sanjay%20Vaghela&phone=9428165978
			// &user_desc=this%20is%20testing&alternate_email=snv_3@gmail.com
			m_updateResult = CommonUtils
					.parseJSON("myaccount_edit.php?uid=" + m_userValue
							+ "&uname=" + m_etUsername.getText().toString()
							+ "&phone=" + m_etPhone.getText().toString()
							+ "&user_desc=None" + "&alternate_email="
							+ m_etAlterEmail.getText().toString());
			return m_updateResult;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				JSONObject m_obj;
				try {
					m_obj = new JSONObject(result);
					boolean m_stb = m_obj.getBoolean("Status");
					if (m_stb) {
						Toast.makeText(m_context, m_obj.getString("result"),
								Toast.LENGTH_LONG).show();
						showAlert(m_obj.getString("result"));
					} else {
						Toast.makeText(m_context, m_obj.getString("result"),
								Toast.LENGTH_LONG).show();
						showAlert(m_obj.getString("result"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			m_prgDialog.dismiss();
			finish();
		}
	}

	/**
	 * Simple alert dialog
	 * @param p_mesg - Message to show in alert.
	 */
	private void showAlert(String p_mesg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(m_context);

		// Setting Dialog Title
		alertDialog.setTitle(getResources().getString(R.string.app_name));

		// Setting Dialog Message
		alertDialog.setMessage(p_mesg);
		alertDialog.setNeutralButton("OK", null);
		alertDialog.show();
	}
}
