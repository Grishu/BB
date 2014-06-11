package com.zeal.peekaboo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.LoggingBehaviors;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.test.clasess.Twitt_Sharing;

public class PromoteScreenActivity extends Activity {

	private Context m_context;
	private Button m_btnFBshare, m_btnTwitterShare;
	private StatusCallback statusCallback = new SessionStatusCallback();
	private Bundle m_savedinstance;
	public final String consumer_key = "trWwomp0b09ER2A8H1cQg";
	public final String secret_key = "PAC3E3CtcPcTuPl9VpCuzY6eDD8hPZPwp6gRDCviLs";
	private File casted_image, myImage;
	private InputStream m_imageStream;
	private String string_img_url = null, string_msg = null;
	private ImageView m_ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.promote_layout);
		m_context = PromoteScreenActivity.this;
		m_savedinstance = savedInstanceState;
		m_btnFBshare = (Button) findViewById(R.id.pl_btnFacebook);
		m_btnTwitterShare = (Button) findViewById(R.id.pl_btnTwitter);
		m_ivBack = (ImageView) findViewById(R.id.pl_ivBack);

		m_btnFBshare.setOnClickListener(m_onClickListener);
		m_btnTwitterShare.setOnClickListener(m_onClickListener);
		m_ivBack.setOnClickListener(m_onClickListener);
	}

	OnClickListener m_onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.pl_btnFacebook:

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
								PromoteScreenActivity.this)
								.setCallback(statusCallback));
					}
				}
				updateView();
				break;
			case R.id.pl_btnTwitter:
				onClickTwitt();
				break;
			case R.id.pl_ivBack:
				finish();
				break;

			default:
				break;
			}

		}
	};

	// Here you can pass the string message & image path which you want to share
	// in Twitter.
	public void onClickTwitt() {
		if (isNetworkAvailable()) {
			Twitt_Sharing twitt = new Twitt_Sharing(PromoteScreenActivity.this,
					consumer_key, secret_key);
			// string_img_url =
			// "http://www.wikihow.com/images/9/91/Get-the-URL-for-Pictures-Step-1.jpg";
			// "http://3.bp.blogspot.com/_Y8u09A7q7DU/S-o0pf4EqwI/AAAAAAAAFHI/PdRKv8iaq70/s1600/id-do-anything-logo.jpg";
			string_msg = "PeekaBoo Application."
					+ "https://play.google.com/store/apps/details?id=com.penqgames.tk.brtsway";
			// here we have web url image so we have to make it as file to
			// upload
			Bitmap m_bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_launcher, null);

			try {
				myImage = new File(Environment.getExternalStorageDirectory()
						+ "/share.jpg");
				System.err.println("Path====" + myImage);
				InputStream inputStream = getResources().openRawResource(
						R.drawable.ic_logo);
				OutputStream out = new FileOutputStream(myImage);
				byte buf[] = new byte[1024];
				int len;
				while ((len = inputStream.read(buf)) > 0)
					out.write(buf, 0, len);
				out.close();
				inputStream.close();
			} catch (IOException e) {
			}

			m_imageStream = getResources().openRawResource(R.drawable.ic_logo);
			// m_imageStream = bitmapToInputStream(m_bitmap);
			// myImage = String_to_File(string_img_url);
			// Now share both message & image to sharing activity
			twitt.shareToTwitter(string_msg, myImage, m_imageStream);

		} else {
			Toast.makeText(m_context, "No Network Connection Available !!!",
					Toast.LENGTH_SHORT).show();
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			updateView();
		}
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

	private void updateView() {
		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			publishFeedDialog();
		} else {
			m_btnFBshare.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					onClickLogin();
				}
			});
		}
	}

	// when user will click on twitte then first that will check that is
	// internet exist or not
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
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

	private void publishFeedDialog() {
		Bundle params = new Bundle();
		// params.putString("name", "PeekaBoo Android Application.");
		// params.putString("caption",
		// "play.google.com");
		// params.putString(
		// "description",
		// "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
		// params.putString("link", "https://developers.facebook.com/android");
		// params.putString("picture",getResources().getDrawable(R.drawable.ic_launcher));
		// "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");
		params.putString("link",
				"https://play.google.com/store/apps/details?id=com.penqgames.tk.brtsway&hl=en");
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(m_context,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(m_context,
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
								finish();
							} else {
								// User clicked the Cancel button
								Toast.makeText(m_context, "Publish cancelled",
										Toast.LENGTH_SHORT).show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(m_context, "Publish cancelled",
									Toast.LENGTH_SHORT).show();
						} else {
							// Generic, ex: network error
							Toast.makeText(getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		feedDialog.show();
	}

}
