package com.zeal.peekaboo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zeal.peak.utils.CommonUtils;

public class SurpriseSetting extends Activity {

	private EditText stag;
	private Button addstag;
	private String m_prefUserID;
	String urlTo = PeakAboo.BaseUrl ;
	private ProgressDialog pDlg;
	private Button remtag;
	private ImageView ssback;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surprise_setting_layout);
		stag=(EditText)findViewById(R.id.stag);
		addstag=(Button)findViewById(R.id.addtag);
		remtag=(Button)findViewById(R.id.removetag);
		ssback=(ImageView)findViewById(R.id.ss_btn_bk);
		pDlg= new ProgressDialog(this);
		
		ssback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		addstag.setOnClickListener(new OnClickListener() {
			
			
			String s;
			String url;

			public void onClick(View v) {
				
				String tag=stag.getText().toString();
				
				if(tag.equals(""))
				{
					AlertDialog.Builder ab = new AlertDialog.Builder(
							SurpriseSetting.this);
					ab.setTitle("'Peak'a'Boo'");
					ab.setMessage("Please enter the tag!");
					ab.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
					ab.show();
				}
				else
				{
				 s = tag.replaceAll("#", ",");
				 s = s.replaceFirst(",", "");
				 Log.e("TAGS",s);
				 m_prefUserID = CommonUtils.getStringSharedPref(getApplicationContext(),
						"user_ID", "");
				 url="surprise_add.php?uid="+m_prefUserID+"&tag="+s;
				Log.e("URL",url);
				asyncsurprise apup = new asyncsurprise();
				apup.execute(url);
				}
				
				
		       
				
			}
		});
		
		remtag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SurpriseSetting.this,TagList.class);
				startActivity(i);
				
			}
		});
	}
	class asyncsurprise extends AsyncTask<String, Integer, String> {
		
		

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDlg.setTitle("please Wait");
			pDlg.setCancelable(false);
			pDlg.show();
		}
/*
		public void executeed(String pic) {
			
			this.execute(pic);
		}
*/
		

		protected String doInBackground(String... params) {
			String post = params[0];
			String result = "";
			try {
				 result= CommonUtils.parseJSON(post);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}

		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDlg.dismiss();
			
			Log.e("result:", result);
			if (result != null) {
				try {
					JSONObject jo = new JSONObject(result);
					Boolean b = jo.getBoolean("Status");

					if (b) {
						AlertDialog.Builder ab = new AlertDialog.Builder(
								SurpriseSetting.this);
						ab.setTitle("'Peak'a'Boo'");
						ab.setMessage("Tag added successfully");
						ab.setNeutralButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										finish();
									}
								});
						ab.show();

					} else {
						AlertDialog.Builder ab = new AlertDialog.Builder(
								SurpriseSetting.this);
						ab.setTitle("'Peak'a'Boo'");
						ab.setMessage("Error in adding tag!Try Later.");
						ab.setNeutralButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										finish();
									}
								});
						ab.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		}
	}
	
}
