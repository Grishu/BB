package com.zeal.peekaboo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class UploadProfileImage extends Activity {

	private String picturePath;

	// private ImageView ivImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);
		// ivImage = (ImageView) findViewById(R.id.ivImage);
		selectImage();
	}

	public void selectImage() {
		final CharSequence[] items = { "Capture Photo", "Choose from Gallery",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
					// finish();
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
					// finish();
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
					finish();
				}
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
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
			File f = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"tt.jpg");
			// String path=f.getAbsolutePath();

			/*
			 * Uri selectedImage = data.getData(); String[] filePathColumn = {
			 * MediaStore.Images.Media.DATA };
			 * 
			 * Cursor cursor = getContentResolver().query(selectedImage,
			 * filePathColumn, null, null, null); cursor.moveToFirst();
			 * 
			 * int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			 */
			picturePath = f.getAbsolutePath();// cursor.getString(columnIndex);
			// cursor.close();
			Log.e("pic path", picturePath);
			PeakAboo.m_ImagePath = picturePath;
			PeakAboo.m_isImageUpload = true;
			// selecteduri = getRealPathFromURI(selectedImage);
			finish();
			// uploadphoto.setImageURI(selectedImage);
			// uploadphoto.setMinimumHeight(100);
			// uploadphoto.setMaxWidth(100);
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
				// uploadphoto.setImageBitmap(bm);

				String path = android.os.Environment
						.getExternalStorageDirectory().getAbsolutePath() + "";
				System.err.println("Image Path--->" + path);
				f.delete();
				OutputStream fOut = null;
				File file = new File(path, String.valueOf(System
						.currentTimeMillis()) + ".jpg");
				System.out.println("File is====>" + file.getAbsolutePath());
				PeakAboo.m_isImageUpload = true;
				PeakAboo.m_ImagePath = file.getAbsolutePath();
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
			finish();
		} else {
			finish();
		}

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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		finish();
	}
}
