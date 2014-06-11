package com.zeal.peekaboo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zeal.peak.obejects.AsyncTask;
import com.zeal.peak.utils.CommonUtils;
import com.zeal.peak.utils.CustomValidator;

public class ChangePasswordActivity extends Activity {

	private Context m_context;
	private Button m_btnSubmit, m_btnCancel;
	private EditText m_etOldPass, m_etNewPass, m_etConfirmPass;
	private ImageView m_ivBack;
	private String m_result, m_userId;
	private ProgressDialog m_prgDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password_layout);
		m_context = ChangePasswordActivity.this;
		m_prgDialog = new ProgressDialog(m_context);
		m_btnSubmit = (Button) findViewById(R.id.cpl_btnSubmit);
		m_btnCancel = (Button) findViewById(R.id.cpl_btnCancel);
		m_etOldPass = (EditText) findViewById(R.id.cpl_etOldPass);
		m_etNewPass = (EditText) findViewById(R.id.cpl_etNewPass);
		m_etConfirmPass = (EditText) findViewById(R.id.cpl_etConfirmPass);
		m_ivBack = (ImageView) findViewById(R.id.cpl_ivBack);

		m_userId = getIntent().getStringExtra("userId");
		m_btnSubmit.setOnClickListener(m_onClickListener);
		m_btnCancel.setOnClickListener(m_onClickListener);
		m_ivBack.setOnClickListener(m_onClickListener);
	}

	/**
	 * Common click listener
	 */
	OnClickListener m_onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.cpl_btnCancel:
			case R.id.cpl_ivBack:
				finish();

				break;
			case R.id.cpl_btnSubmit:

				CustomValidator.m_isError = false;

				CustomValidator.validateForEmptyValue(m_etOldPass,
						"Please enter old password");
				CustomValidator.validateForEmptyValue(m_etNewPass,
						"Please enter new password");
				CustomValidator.validateForEmptyValue(m_etConfirmPass,
						"Please cofirm password");
				if (!CustomValidator.m_isError) {

					if (m_etNewPass
							.getText()
							.toString()
							.trim()
							.equalsIgnoreCase(
									m_etConfirmPass.getText().toString().trim())) {
						callChangePasswordWS m_changePass = new callChangePasswordWS();
						m_changePass.execute(m_etNewPass.getText().toString());

					} else {
						m_etConfirmPass
								.setError("Password and confirm password are not same.");
					}
				}
				break;
			default:
				break;
			}

		}
	};

	/**
	 * Change password webservice implementation.
	 */
	class callChangePasswordWS extends AsyncTask<String, Integer, String> {

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
			// http://192.168.1.196/PeekaBoo_Webservice/change_password.php?uid=105&password=test123
			m_result = CommonUtils.parseJSON("change_password.php?uid="
					+ m_userId + "&password=" + params[0]);

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

						// Toast.makeText(m_context, jo.getString("result"),
						// Toast.LENGTH_LONG).show();
						showAlert(jo.getString("result"));
					} else {
						// Toast.makeText(m_context, jo.getString("result"),
						// Toast.LENGTH_LONG).show();
						showAlert(jo.getString("result"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			m_prgDialog.dismiss();
			// finish();
		}
	}

	private void showAlert(String p_mesg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(m_context);

		// Setting Dialog Title
		alertDialog.setTitle(getResources().getString(R.string.app_name));

		// Setting Dialog Message
		alertDialog.setMessage(p_mesg);
		alertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				});
		alertDialog.show();
	}
}
