package com.zeal.peekaboo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.penq.utils.BitmapUtils;
import com.penq.utils.VideoUtils;
import com.zeal.peak.utils.CommonUtils;

public class AddCommentActivity extends Activity {

	private EditText comment;
	private Button photoupld;
	String tag;
	private Bitmap bmp;
	String urlTo = PeakAboo.BaseUrl + "/insertimage.php";
	String videourl = PeakAboo.BaseUrl + "/videoinsert.php";
	String selecteduri = "";
	ProgressDialog pDlg;
	String m_prefUserID;
	private ImageView backcmnt;
	private String video;
	private String media;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_tag);
		comment = (EditText) findViewById(R.id.comment);

		Bundle extras = getIntent().getExtras();

		media = extras.getString("Media");
		Log.e("", media);
		if (media.equals("V")) {
			video = extras.getString("Video");
			Log.e("", video);
			comment.setVisibility(View.VISIBLE);
		}
		if (media.equals("I")) {
			comment.setVisibility(View.VISIBLE);
			byte[] byteArray = extras.getByteArray("picture");
			bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		}
		backcmnt = (ImageView) findViewById(R.id.tagphoto);
		backcmnt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		photoupld = (Button) findViewById(R.id.photo_upld);

		pDlg = new ProgressDialog(this);
		// iv.setImageBitmap(bmp);
		m_prefUserID = CommonUtils.getStringSharedPref(getApplicationContext(),
				"user_ID", "");
		photoupld.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (comment.getText().toString().equals("")) {
					tag = "Default";
				} else {
					String s = comment.getText().toString();
					tag = s.replaceAll("#", ",");
					tag = tag.replaceFirst(",", "");

				}
				if (media.equals("I")) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					String pic = "uid=" + m_prefUserID + "&amp;tag=" + tag;
					bmp.compress(CompressFormat.PNG, 0, bos);
					byte[] bitmapdata = bos.toByteArray();
					ByteArrayInputStream bs = new ByteArrayInputStream(
							bitmapdata);
					asyncphotoupload apup = new asyncphotoupload();
					apup.execute(pic, bs);
				} else if (media.equals("V")) {
					String pic = "uid=" + m_prefUserID + "&amp;tag=" + tag;
					asyncphotoupload apup = new asyncphotoupload();
					apup.execute(pic, video);
				}
			}
		});

	}

	class asyncphotoupload extends AsyncTask<String, Integer, String> {
		ByteArrayInputStream bitmapdata;
		String vid;

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDlg.setTitle("please Wait");
			pDlg.setCancelable(false);
			pDlg.show();
		}

		public void execute(String pic, String video) {
			vid = video;
			this.execute(pic);
		}

		public void execute(String pic, ByteArrayInputStream bs) {
			// TODO Auto-generated method stub
			bitmapdata = bs;
			this.execute(pic);
		}

		protected String doInBackground(String... params) {
			String post = params[0];
			String result = "";
			try {
				if (media.equals("I")) {
					BitmapUtils bu = new BitmapUtils();
					Log.e("hai", selecteduri);
					result = bu.multipartRequest(urlTo, post, bitmapdata,
							"file", AddCommentActivity.this);
				} else if (media.equals("V")) {
					VideoUtils pu = new VideoUtils();
					Log.e("hai", video);
					result = pu.multipartRequest(videourl, post, vid, "file",
							AddCommentActivity.this);
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
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
								AddCommentActivity.this);
						ab.setTitle("'Peak'a'Boo'");
						ab.setMessage("Media Uploaded Successfully");
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
								AddCommentActivity.this);
						ab.setTitle("'Peak'a'Boo'");
						ab.setMessage("Error in Uploading Media!Try Later.");
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
