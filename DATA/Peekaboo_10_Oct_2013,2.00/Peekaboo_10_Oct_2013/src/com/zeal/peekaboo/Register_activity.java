package com.zeal.peekaboo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.penq.utils.Photoutils;
import com.zeal.peak.utils.CustomValidator;

public class Register_activity extends Activity {
	private EditText name, email, password, mobile;
//	private ImageView OK;
	private ImageView uploadphoto;
	private String urlTo = PeakAboo.BaseUrl + "register.php";
	private String picturePath;
	private Boolean picselected = false;
	private String selecteduri = "";
	private ProgressDialog pDlg;
	private ImageView reg_backbtn;
	private InputMethodManager imm;
	private Context m_context;
	private Button m_btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_activity);
		m_context = Register_activity.this;
		name = (EditText) findViewById(R.id.reg_et_uname);
		email = (EditText) findViewById(R.id.reg_et_email);
		password = (EditText) findViewById(R.id.reg_et_pass);
		mobile = (EditText) findViewById(R.id.reg_et_phn);
		m_btnRegister = (Button) findViewById(R.id.reg_iv_next1);
		uploadphoto = (ImageView) findViewById(R.id.reg_iv_upload);

//		OK = (ImageView) findViewById(R.id.reg_iv_next);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		/*
		 * confirm_email=(EditText)findViewById(R.id.et_reg_confirm_email);
		 * confirm_pass=(EditText)findViewById(R.id.et_reg_cnfrm_pass);
		 */

		pDlg = new ProgressDialog(this);
		reg_backbtn = (ImageView) findViewById(R.id.reg_iv_back);

		reg_backbtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();

			}
		});

		/*OK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				imm.hideSoftInputFromWindow(OK.getWindowToken(), 0);
				String post = "uname=" + name.getText().toString()
						+ "&amp;email=" + email.getText().toString()
						+ "&amp;pass=" + password.getText().toString();
				CustomValidator.m_isError = false;

				CustomValidator.validateForEmptyValue(password,
						"Please enter Password");
				CustomValidator.validateForEmptyValue(name,
						"Please enter username");
				CustomValidator.validateEmail(email,
						"Please enter email address",
						"Please enter valid email address.");
				if (!CustomValidator.m_isError) {
					asyncupload aup = new asyncupload();
					aup.execute(post);
				}
				
				 * ab.setTitle("Alert");
				 * ab.setMessage("Incorect Username or Password");
				 * ab.setNeutralButton("OK", null); ab.show(); }
				 

			}
		});*/
		m_btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imm.hideSoftInputFromWindow(m_btnRegister.getWindowToken(), 0);
				String post = "uname=" + name.getText().toString()
						+ "&amp;email=" + email.getText().toString()
						+ "&amp;pass=" + password.getText().toString();
				CustomValidator.m_isError = false;

				CustomValidator.validateForEmptyValue(password,
						"Please enter Password");
				CustomValidator.validateForEmptyValue(name,
						"Please enter username");
				CustomValidator.validateEmail(email,
						"Please enter email address",
						"Please enter valid email address.");
				if (!CustomValidator.m_isError) {
					asyncupload aup = new asyncupload();
					aup.execute(post);
				}

			}
		});
		uploadphoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectImage();
			}
		});

	}

	private void selectImage() {
		final CharSequence[] items = { "Capture Photo", "Choose from Gallery",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(
				Register_activity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Capture Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");
					System.err.println("Path$$$$$$$$$ " + f.getAbsolutePath());

					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, PeakAboo.RESULT_CAMERA);
				} else if (items[item].equals("Choose from Gallery")) {

					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					i.putExtra("crop", "true");
					i.putExtra("outputX", 512);
					i.putExtra("outputY", 512);
					i.putExtra("aspectX", 1);
					i.putExtra("aspectY", 1);
					i.putExtra("scale", true);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "tt.jpg");
					// imgpath=f.getAbsolutePath();
					System.err.println("Path$$$$$$$$$ " + f.getAbsolutePath());
					i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					i.putExtra("outputFormat",
							Bitmap.CompressFormat.PNG.toString());
					startActivityForResult(i, PeakAboo.RESULT_LOAD_IMAGE);

				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PeakAboo.RESULT_LOAD_IMAGE
				&& resultCode == RESULT_OK && null != data) {
			/*
			 * Uri selectedImage = data.getData(); String[] filePathColumn = {
			 * MediaStore.Images.Media.DATA };
			 * 
			 * Cursor cursor = getContentResolver().query(selectedImage,
			 * filePathColumn, null, null, null); cursor.moveToFirst(); int
			 * columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			 */

			File f = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"tt.jpg");

			picturePath = f.getAbsolutePath();// cursor.getString(columnIndex);
			// cursor.close();
			picselected = true;
			Log.e("pic path", picturePath);
			Bitmap m_bitmap = BitmapFactory.decodeFile(picturePath);
			// selecteduri = getRealPathFromURI(selectedImage);
			uploadphoto.setImageBitmap(decodeFile(f));
			uploadphoto.setMinimumHeight(100);
			uploadphoto.setMaxWidth(100);
		} else if (requestCode == PeakAboo.RESULT_CAMERA
				&& resultCode == Activity.RESULT_OK) {

			File f = new File(Environment.getExternalStorageDirectory()
					.toString());
			for (File temp : f.listFiles()) {
				if (temp.getName().equals("temp.jpg")) {
					f = temp;
					break;
				}
			}
			try {
				Bitmap bm;
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

				bm = BitmapFactory
						.decodeFile(f.getAbsolutePath(), btmapOptions);

				// bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
				uploadphoto.setImageBitmap(bm);

				String path = android.os.Environment
						.getExternalStorageDirectory().getAbsolutePath() + "";
				System.err.println("Image Path--->" + path);
				f.delete();
				OutputStream fOut = null;
				File file = new File(path, String.valueOf(System
						.currentTimeMillis()) + ".jpg");
				System.out.println("File is====>" + file.getAbsolutePath());
				selecteduri = file.getAbsolutePath();
				try {
					fOut = new FileOutputStream(file);
					bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
					fOut.flush();
					fOut.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 512;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
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

	class asyncupload extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDlg.setTitle("please Wait");
			pDlg.setCancelable(false);
			pDlg.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String post = arg0[0];
			String result = "";
			try {
				Photoutils pu = new Photoutils();
				Log.e("hai", selecteduri);
				result = pu.multipartRequest(urlTo, post, selecteduri, "file",
						Register_activity.this);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}

		@Override
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
						/*
						 * Toast.makeText(Register_activity.this,
						 * jo.getString("result"), Toast.LENGTH_LONG) .show();
						 */
						showwelcomeDialog(false, "");
					} else {
						showwelcomeDialog(true, jo.getString("result"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		}
	}

	private void showwelcomeDialog(boolean p_isRegister, String p_mesg) {
		// custom dialog
		final Dialog dialog = new Dialog(Register_activity.this,
				R.style.Theme_Dialog);
		dialog.setContentView(R.layout.welcome_dialog_layout);
		dialog.setCancelable(false);
		TextView m_tvMesg = (TextView) dialog.findViewById(R.id.wdl_tvMesg);
		Button m_btndialogCancel = (Button) dialog
				.findViewById(R.id.welcomebtn);
		if (p_isRegister) {
			m_tvMesg.setText(p_mesg);
			m_btndialogCancel.setText("OK");
		}
		// if button is clicked, close the custom dialog
		m_btndialogCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearText();
				startActivity(new Intent(m_context, PromoteScreenActivity.class));
				dialog.dismiss();
				finish();
			}
		});
		dialog.show();
	}

	private void clearText() {
		name.setText("");
		email.setText("");
		password.setText("");
		mobile.setText("");
	}
}
