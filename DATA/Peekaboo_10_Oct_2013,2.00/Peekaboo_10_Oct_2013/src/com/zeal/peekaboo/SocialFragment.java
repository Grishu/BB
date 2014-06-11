package com.zeal.peekaboo;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.penq.utils.Photoutils;
import com.zeal.peak.adapter.PhotoGridAdapter;
import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peak.obejects.AsyncTask;
import com.zeal.peak.utils.CommonUtils;
import com.zeal.peak.utils.CustomValidator;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class SocialFragment extends Fragment {

	// private GridAdapter ga;
	private GridView gv;
	private TextView tv_profilename, tv_profiledisc, tv_no_of_followers,
			tv_no_of_phts, tv_dice_roll, tv_shake, tv_look_around,
			tv_total_contact;
	private ImageView iv_prof_pic;
	private Context m_context;
	private Bundle m_buBundle;
	private String m_userIdVal, m_imageURL, res, m_message;
	private ProgressDialog m_prgDialog;
	private ArrayList<String> m_imageUrls;
	private PhotoGridAdapter m_photoGridAdpter;
	private ImageLoader m_imgLoader;
	private LinearLayout m_llFollowerLayout, m_llContactLayout, m_llTop;
	private String profDescription, m_sUploadImge;
	private EditText m_etMessg;
	private Dialog dialog;
	private byte[] byteArray;
	private InputMethodManager imm;
	private boolean m_isLocationOn = true;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.socialfragmentlayout, container,
				false);

		m_context = SocialFragment.this.getActivity();
		m_prgDialog = new ProgressDialog(m_context);
		m_imageUrls = new ArrayList<String>();
		m_imgLoader = new ImageLoader(m_context);

		imm = (InputMethodManager) m_context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		tv_shake = (TextView) v.findViewById(R.id.tv_sp_shake_device);
		tv_dice_roll = (TextView) v.findViewById(R.id.tv_sp_dice_roll);
		tv_profilename = (TextView) v.findViewById(R.id.sp_profilename);
		tv_no_of_followers = (TextView) v
				.findViewById(R.id.tv_sp_followers_count);
		tv_no_of_phts = (TextView) v.findViewById(R.id.tv_sp_phto_count);
		tv_profiledisc = (TextView) v.findViewById(R.id.sp_profiledisc);
		tv_look_around = (TextView) v.findViewById(R.id.tv_sp_look_around);
		tv_total_contact = (TextView) v.findViewById(R.id.tv_sp_cntcts_count);
		iv_prof_pic = (ImageView) v.findViewById(R.id.sp_iv_profpic);
		gv = (GridView) v.findViewById(R.id.gv_sp_photogrid);
		m_llFollowerLayout = (LinearLayout) v.findViewById(R.id.followersno);
		m_llContactLayout = (LinearLayout) v.findViewById(R.id.contactsno);
		m_llTop = (LinearLayout) v.findViewById(R.id.socialtop);
		Tabbedmaincontroller.m_tvHeader.setText(getResources().getString(
				R.string.lbl_peak_a_boo));
		m_llTop.setBackgroundColor(getResources()
				.getColor(R.color.social_green));
		m_buBundle = this.getArguments();
		m_userIdVal = m_buBundle.getString("user_Id");
		m_isLocationOn = CommonUtils.getBooleanSharedPref(m_context,
				"Location", true);
		tv_shake.setOnClickListener(m_OnClickListener);
		tv_dice_roll.setOnClickListener(m_OnClickListener);
		tv_look_around.setOnClickListener(m_OnClickListener);
		m_llFollowerLayout.setOnClickListener(m_OnClickListener);
		m_llContactLayout.setOnClickListener(m_OnClickListener);

		iv_prof_pic.setOnClickListener(m_OnClickListener);
		tv_profiledisc.setOnClickListener(m_OnClickListener);

		// ga = new GridAdapter(abcd, getActivity(), false);
		// gv.setAdapter(ga);

		callSocialWS ld = new callSocialWS();
		ld.execute();
		
		return v;
	}

	/**
	 * Common click listener
	 */
	OnClickListener m_OnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.sp_profiledisc:
				showStatusDialog();
				break;

			case R.id.sp_iv_profpic:
				startActivity(new Intent(m_context, UploadProfileImage.class)
						.putExtra("user_id", m_userIdVal));
				break;

			case R.id.tv_sp_shake_device:
				if (m_isLocationOn) {
					startActivity(new Intent(m_context, ShakeActivity.class)
							.putExtra("user_id", m_userIdVal));
				} else {

					Toast.makeText(
							m_context,
							"Location services disabled.\n Please enable location services from privacy.",
							Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.tv_sp_dice_roll:
				if (m_isLocationOn) {
					startActivity(new Intent(m_context, DiceRollActivity.class)
							.putExtra("user_id", m_userIdVal));
				} else {

					Toast.makeText(
							m_context,
							"Location services disabled.\n Please enable location services from privacy.",
							Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.tv_sp_look_around:
				if (m_isLocationOn) {
					startActivity(new Intent(m_context,
							LookAroundActivity.class).putExtra("user_id",
							m_userIdVal));
				} else {

					Toast.makeText(
							m_context,
							"Location services disabled.\n Please enable location services from privacy.",
							Toast.LENGTH_LONG).show();
				}
				break;

			case R.id.followersno:
				if (!tv_no_of_followers.getText().toString()
						.equalsIgnoreCase("0")) {
					startActivity(new Intent(m_context,
							FollowersListActivity.class).putExtra("user_id",
							m_userIdVal));
				} else {
					Toast.makeText(m_context, "You do not have any follower.",
							Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.contactsno:
				if (!tv_total_contact.getText().toString()
						.equalsIgnoreCase("0")) {
					startActivity(new Intent(m_context,
							ContactListActivity.class).putExtra("user_id",
							m_userIdVal));
				} else {
					Toast.makeText(m_context, "You do not have any contact.",
							Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}
	};

	/*
	 * Call the social webservice and inflate the data.
	 */
	class callSocialWS extends AsyncTask<String, Integer, String> {

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
			res = CommonUtils.parseJSON("social.php?uid=" + m_userIdVal);
			return res;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			try {
				JSONObject jobj = new JSONObject(result);
				Boolean b = jobj.getBoolean("Status");
				if (b) {
					JSONArray ja = jobj.getJSONArray("result");
					JSONObject jo = ja.getJSONObject(0);
					tv_total_contact.setText(jo.getString("NumOfContact"));
					tv_profilename.setText(jo.getString("ProfileName"));
					tv_no_of_phts.setText(jo.getString("NumOfImages"));
					tv_no_of_followers.setText(jo.getString("NumOfFollowers"));
					if (jo.getString("Description").equalsIgnoreCase(""))
						tv_profiledisc.setText("Tap to enter your status.");
					else
						tv_profiledisc.setText(jo.getString("Description"));
					m_imageURL = jo.getString("ProfileImage");
					System.out
							.println("SocialFragment.onPostExecute()Profile Image===>"
									+ m_imageURL);
					if (!m_imageURL.equalsIgnoreCase("NoImage")) {
						m_imgLoader.DisplayImage(m_imageURL,
								R.drawable.no_image, iv_prof_pic);
					} else {
						// iv_prof_pic.setBackgroundDrawable(getResources()
						// .getDrawable(R.drawable.no_image));
					}
					JSONArray m_imageArray = jo.getJSONArray("ImageUrl");
					if (m_imageArray.length() > 0) {
						System.out.println("ImageArray Is-------->"
								+ m_imageArray.toString() + "\n Image URL"
								+ m_imageURL);
						for (int i = 0; i < m_imageArray.length(); i++) {
							m_imageUrls.add(m_imageArray.getString(i));
						}

						m_photoGridAdpter = new PhotoGridAdapter(SocialFragment.this.getActivity(),
								m_imageUrls);
						gv.setAdapter(m_photoGridAdpter);
					}
				} else {
					Log.e("Error", "No Data Found.");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			m_prgDialog.dismiss();
		}
	}

	private void showStatusDialog() {
		// custom dialog
		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (!Character.isLetterOrDigit(source.charAt(i))
							&& !Character.isSpaceChar(source.charAt(i))) {
						return "";
					}
				}
				return null;
			}
		};

		dialog = new Dialog(m_context, R.style.Theme_Dialog);
		dialog.setContentView(R.layout.update_status_layout);
		dialog.setCancelable(false);

		Button m_btndialogCancel = (Button) dialog
				.findViewById(R.id.usl_btnCancel);
		Button m_btndialogUpdate = (Button) dialog
				.findViewById(R.id.usl_btnDone);
		m_etMessg = (EditText) dialog.findViewById(R.id.usl_etStatus);
		m_etMessg.setFilters(new InputFilter[] { filter });
		m_etMessg.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				50) });
		// if button is clicked, close the custom dialog
		m_btndialogCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		m_btndialogUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				profDescription = m_etMessg.getText().toString();
				CustomValidator.m_isError = false;
				System.err.println("Message -=----------" + profDescription);
				CustomValidator.validateForEmptyValue(m_etMessg,
						"Please enter Status");

				if (!CustomValidator.m_isError) {
					callUpdateMessageWS m_callMesg = new callUpdateMessageWS();
					m_callMesg.execute(profDescription);
				}
				imm.hideSoftInputFromWindow(m_etMessg.getWindowToken(), 0);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		m_imgLoader.clearCache();
	}

	/**
	 * Call a webservice to update the status message of user profile.
	 */
	class callUpdateMessageWS extends AsyncTask<String, Integer, String> {
		String temp;

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

			// http://192.168.1.196/PeekaBoo_Webservice/update_user_desc.php?uid=74&desc=Hello
			m_message = params[0];
			temp = m_message.replaceAll(" ", "%20");
			res = CommonUtils.parseJSON("update_user_desc.php?uid="
					+ m_userIdVal + "&desc=" + temp);

			return res;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {

				Boolean b;
				try {
					JSONObject jobj = new JSONObject(result);
					b = jobj.getBoolean("status");
					if (b) {
						tv_profiledisc.setText(m_message);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			m_prgDialog.dismiss();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Social OnResume()", "Social Fragment onResume called.....");
		if (PeakAboo.m_isImageUpload && PeakAboo.m_ImagePath != null) {
			// byteArray = getArguments().getByteArray("picture");
			m_sUploadImge = "uid=" + m_userIdVal;
			ImageUpload m_upload = new ImageUpload();
			m_upload.execute(m_sUploadImge);
			PeakAboo.m_isImageUpload = false;
		}
	}

	/**
	 * Upload image webservice to upload profile picture.
	 */
	class ImageUpload extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			m_prgDialog.setTitle("Uploading Image");
			m_prgDialog.setCancelable(false);
			m_prgDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String post = arg0[0];
			String result = "";
			// http://192.168.1.196/PeekaBoo_Webservice/update_prof_pic.php?uid=74

			try {
				Photoutils pu = new Photoutils();
				Log.e("", post);

				Log.e("Image Background", PeakAboo.m_ImagePath);
				result = pu.multipartRequest(PeakAboo.BaseUrl
						+ "update_prof_pic.php", post, PeakAboo.m_ImagePath,
						"file", SocialFragment.this.getActivity());

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

			Log.e("result:", result);
			if (result != null) {
				try {
					JSONObject jo = new JSONObject(result);
					Boolean b = jo.getBoolean("Status");

					if (b) {
						Toast.makeText(m_context, jo.getString("result"),
								Toast.LENGTH_LONG).show();
						Bitmap bmp = BitmapFactory
								.decodeFile(PeakAboo.m_ImagePath);
						iv_prof_pic.setImageBitmap(bmp);
						// showwelcomeDialog();
					} else {
						Toast.makeText(m_context, jo.getString("result"),
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				m_prgDialog.dismiss();
			}
		}
	}
}
