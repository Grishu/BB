package com.zeal.peekaboo;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.penq.utils.VideoUtils;
import com.zeal.peak.utils.CommonUtils;

public class MainActivity1 extends Activity {

	private final int SELECT_FILE = 1;
	private final int REQUEST_CAMERA = 0;
	private static final int VIDEO_CAPTURE = 101;
	private ImageView ivImage;

	String videomsgurl = PeakAboo.BaseUrl
			+ "/message_video_send.php?receiver_uid=";

	private String c;
	private int i;
	private String m_prefUserID;
	private ProgressDialog pDlg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);
		pDlg = new ProgressDialog(this);
		Bundle extras = getIntent().getExtras();
		i = extras.getInt("Upldmsg");
		if (i == 2) {
			c = extras.getString("Contact");
		}
		ivImage = (ImageView) findViewById(R.id.ivImage);

		if (i == 1) {
			final CharSequence[] items = { "Capture Photo",
					"Choose from Gallery", "Record A Video", "Cancel" };
			selectImage(items);
		} else if (i == 2) {
			final CharSequence[] items1 = { "Capture Photo", "Record A Video",
					"Cancel" };
			selectImage(items1);
		}

	}

	private void selectImage(CharSequence[] item) {

		final CharSequence[] items = item;

		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity1.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			private Uri fileUri;

			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Capture Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");

					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Gallery")) {
					// (MediaStore.ACTION_IMAGE_CAPTURE);
					Intent photoPickerIntent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					// photoPickerIntent.setType("image/*");
					photoPickerIntent.putExtra("crop", "true");
					photoPickerIntent.putExtra("outputX", 512);
					photoPickerIntent.putExtra("outputY", 512);
					photoPickerIntent.putExtra("aspectX", 1);
					photoPickerIntent.putExtra("aspectY", 1);
					photoPickerIntent.putExtra("scale", true);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "tt.jpg");
					// imgpath=f.getAbsolutePath();
					System.err.println("Path$$$$$$$$$ " + f.getAbsolutePath());
					photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(f));
					photoPickerIntent.putExtra("outputFormat",
							Bitmap.CompressFormat.PNG.toString());
					startActivityForResult(photoPickerIntent, SELECT_FILE);

				} else if (items[item].equals("Record A Video")) {
					/*
					 * File mediaFile = new
					 * File(android.os.Environment.getExternalStorageDirectory
					 * ().getAbsolutePath() + "/myvideo.mp4");
					 */
					/*
					 * Intent intent = new
					 * Intent("android.media.action.VIDEO_CAPTURE"); // fileUri
					 * = Uri.fromFile(mediaFile);
					 * 
					 * intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
					 * Log.e("", "in video intent"); //
					 * intent.putExtra("android.intent.extra.durationLimit",5);
					 * startActivityForResult(intent, 101);
					 */
					Log.e("", "in video intent");
					Intent takeVideoIntent = new Intent(
							MediaStore.ACTION_VIDEO_CAPTURE);
					takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
					takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,
							10);

					takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,
							13179648L);
					startActivityForResult(takeVideoIntent, 101);

				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
					finish();
				}
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

				Log.e("", "cancelled");
				finish();
			}
		});
		builder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {

				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				try {

					String path = f.getAbsolutePath();
					Intent intent = new Intent(this, NextActivity.class);
					intent.putExtra("Upldmsg", i);
					intent.putExtra("Contact", c);
					intent.putExtra("picture", path);
					intent.putExtra("Act", true);
					startActivity(intent);
					finish();

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == SELECT_FILE) {
				File f = new File(
						android.os.Environment.getExternalStorageDirectory(),
						"tt.jpg");

				String path = f.getAbsolutePath();
				Intent intent = new Intent(this, NextActivity.class);

				intent.putExtra("picture", path);
				intent.putExtra("Contact", c);
				intent.putExtra("Upldmsg", i);
				intent.putExtra("Act", true);
				startActivity(intent);
				finish();

			}

			else if (requestCode == VIDEO_CAPTURE) {

//				Toast.makeText(this,
//						"Video has been saved to:\n" + data.getData(),
//						Toast.LENGTH_LONG).show();
				if (i == 1) {
					String selecteduri = getRealPathFromURI(data.getData());
					Intent i = new Intent(MainActivity1.this,
							AddCommentActivity.class);
					i.putExtra("Video", selecteduri);
					i.putExtra("Media", "V");
					startActivity(i);
					finish();
				} else if (i == 2) {
					String vid = getRealPathFromURI(data.getData());
					/*
					 * VideoUtils pu = new VideoUtils(); m_prefUserID =
					 * CommonUtils
					 * .getStringSharedPref(getApplicationContext(),"user_ID",
					 * ""); String post="uid="+m_prefUserID; try { String result
					 * = pu.multipartRequest(videomsgurl, post,vid, "file",
					 * MainActivity1.this); Log.e("RESULT--->",result); } catch
					 * (ParseException e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); } catch (IOException e) { // TODO
					 * Auto-generated catch block e.printStackTrace(); }
					 */

					messageupload mu = new messageupload();
					mu.exe(vid);
				}

			}

		} else
			finish();
	}

	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		if (cursor == null) {
			return null;
		}
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	class messageupload extends AsyncTask<String, Integer, String> {
		String bytestreams;
		private String m_prefUserID;
		private String result;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDlg.setTitle("please Wait");
			pDlg.setCancelable(false);
			pDlg.show();
		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.e("TAG", "Selected Contacts are" + c);
			videomsgurl = videomsgurl + c;
			m_prefUserID = CommonUtils.getStringSharedPref(
					getApplicationContext(), "user_ID", "");
			String pic = "uid=" + m_prefUserID;

			VideoUtils pu = new VideoUtils();
			m_prefUserID = CommonUtils.getStringSharedPref(
					getApplicationContext(), "user_ID", "");
			String post = "uid=" + m_prefUserID;

			try {

				result = pu.multipartRequest(videomsgurl, post, bytestreams,
						"file", MainActivity1.this);
				Log.e("result:", result);
			} catch (ParseException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}

			return result;
		}

		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// pDlg.dismiss();
			Log.e("result:", result);
			if (result != null) {
				try {
					JSONObject jo = new JSONObject(result);
					Boolean b = jo.getBoolean("status");

					if (b) {
						AlertDialog.Builder ab = new AlertDialog.Builder(
								MainActivity1.this);

						ab.setTitle("'Peak'a'Boo'");
						ab.setMessage("Message Sent Successfully");
						ab.setNeutralButton("ok",
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
								MainActivity1.this);
						ab.setTitle("'Peak'a'Boo'");
						ab.setMessage("Error in Sending Picture!Try Later.");
						ab.setNeutralButton("ok",
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

		public void exe(String vid) {
			// TODO Auto-generated method stub
			bytestreams = vid;
			this.execute();
		}
	}
}
