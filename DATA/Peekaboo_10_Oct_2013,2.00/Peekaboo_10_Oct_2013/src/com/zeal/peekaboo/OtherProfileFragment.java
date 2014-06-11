package com.zeal.peekaboo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zeal.peak.adapter.PhotoGridAdapter;
import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peak.utils.CommonUtils;

public class OtherProfileFragment extends Fragment {
	private String m_sUserID, m_sFollowerId, m_result, res, m_imageURL,
			m_sRecordResult;
	private ProgressDialog m_prgDialog;
	private Context m_context;
	private TextView m_tvFollower, m_tvContact, m_tvPhotoCount,
			m_tvFollowerCount, m_tvContactCount, m_tvProfileName, m_tvProfDesc;
	private LinearLayout m_llFollowerLayout, m_llContactLayout,
			m_llFollowCountLayout, m_llContactCountLayout, m_llTop;
	private PhotoGridAdapter m_photoGridAdpter;
	private ArrayList<String> m_imageUrls;
	private ImageLoader m_imgLoader;
	private ImageView m_ivProfilePic;
	private GridView m_gvGrid;
	private Boolean m_isFollowerAdded = false, m_isContactAdded = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.otherprofile, container, false);
		m_context = OtherProfileFragment.this.getActivity();
		Tabbedmaincontroller.m_ivback.setVisibility(View.GONE);
		Tabbedmaincontroller.m_ivsetting.setVisibility(View.GONE);
		m_sUserID = getArguments().getString("user_Id");
		m_sFollowerId = getArguments().getString("Follower_Id");
		m_imageUrls = new ArrayList<String>();
		m_imgLoader = new ImageLoader(m_context);
		m_prgDialog = new ProgressDialog(m_context);
		m_tvFollower = (TextView) v.findViewById(R.id.tv_sp_start_following);
		m_tvContact = (TextView) v.findViewById(R.id.tv_sp_add_to_contacts);
		m_tvProfDesc = (TextView) v.findViewById(R.id.op_tvprofiledisc);
		PeakAboo.m_sFollowerId = m_sFollowerId;
		m_tvPhotoCount = (TextView) v.findViewById(R.id.op_tvPhotoCount);
		m_tvFollowerCount = (TextView) v.findViewById(R.id.op_tvFollowersCount);
		m_tvContactCount = (TextView) v.findViewById(R.id.op_tvCntctsCount);
		m_tvProfileName = (TextView) v.findViewById(R.id.op_tvProfilename);

		m_ivProfilePic = (ImageView) v.findViewById(R.id.op_iv_profpic);
		m_gvGrid = (GridView) v.findViewById(R.id.op_gvImageList);
		m_llFollowerLayout = (LinearLayout) v
				.findViewById(R.id.op_llFollowerLayout);
		m_llContactLayout = (LinearLayout) v
				.findViewById(R.id.op_llContactLayout);
		m_llFollowCountLayout = (LinearLayout) v.findViewById(R.id.followersno);
		m_llContactCountLayout = (LinearLayout) v.findViewById(R.id.contactsno);
		m_llTop = (LinearLayout) v.findViewById(R.id.op_socialtop);
		m_llContactLayout.setOnClickListener(m_onClickListener);
		m_llFollowerLayout.setOnClickListener(m_onClickListener);
		m_tvContact.setOnClickListener(m_onClickListener);
		m_tvFollower.setOnClickListener(m_onClickListener);
		m_llFollowCountLayout.setOnClickListener(m_onClickListener);
		m_llContactCountLayout.setOnClickListener(m_onClickListener);

		m_llTop.setBackgroundColor(getResources()
				.getColor(R.color.social_green));
		callOtherUserWS m_callUser = new callOtherUserWS();
		m_callUser.execute();

		ischeckFollower_Contact m_checkFollwer = new ischeckFollower_Contact();
		m_checkFollwer.execute();
		return v;
	}

	/**
	 * Common click listener
	 */
	OnClickListener m_onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.op_llContactLayout:
			case R.id.tv_sp_add_to_contacts:
				if (m_isContactAdded) {
					callRemoveContactWS m_removeCont = new callRemoveContactWS();
					m_removeCont.execute();
					Log.e("Contact Remove WS", "Removed Contact");
				} else {
					callAddContactWS m_addCont = new callAddContactWS();
					m_addCont.execute();
				}
				break;

			case R.id.op_llFollowerLayout:
			case R.id.tv_sp_start_following:
				if (m_isFollowerAdded) {
					callRemoveFollowerWS m_follow = new callRemoveFollowerWS();
					m_follow.execute();
					Log.e("Follower Remove WS", "Removed Follower");
				} else {
					callAddFollowerWS m_add = new callAddFollowerWS();
					m_add.execute();
				}
				break;
			case R.id.followersno:
				if (!m_tvFollowerCount.getText().toString()
						.equalsIgnoreCase("0")) {
					startActivity(new Intent(m_context,
							FollowersListActivity.class).putExtra("user_id",
							m_sUserID));
				} else {
					Toast.makeText(m_context, "You do not have any follower.",
							Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.contactsno:
				if (!m_tvContactCount.getText().toString()
						.equalsIgnoreCase("0")) {
					startActivity(new Intent(m_context,
							ContactListActivity.class).putExtra("user_id",
							m_sUserID));
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
	 * Webservice to check that whether a follower or contact is already added
	 * or not?
	 */
	class ischeckFollower_Contact extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			m_prgDialog.setMessage("Loading...");
			m_prgDialog.setCancelable(false);
			m_prgDialog.setTitle(getResources().getString(R.string.app_name));
			m_prgDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/chk_follow_contact.php?uid=107&fcid=100
			if (m_sUserID != null && m_sFollowerId != null) {
				m_result = CommonUtils.parseJSON("chk_follow_contact.php?uid="
						+ m_sUserID + "&fcid=" + m_sFollowerId);
			}
			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject m_jObj = new JSONObject(result);
				Boolean b = m_jObj.getBoolean("Status");
				if (b) {
					JSONObject m_jArray = m_jObj.getJSONObject("result");
					System.out.println("Follower==="
							+ m_jArray.getString("Followers") + "Contact===="
							+ m_jArray.getString("Contact"));
					if (m_jArray.getString("Followers").toString()
							.equalsIgnoreCase("0")) {
						m_tvFollower.setText("FOLLOW");
						m_isFollowerAdded = false;
					} else if (m_jArray.getString("Followers").toString()
							.equalsIgnoreCase("1")) {
						m_tvFollower.setText("UNFOLLOW");
						m_isFollowerAdded = true;
					}

					if (m_jArray.getString("Contact").toString()
							.equalsIgnoreCase("2")) {
						m_tvContact.setText("ADD TO CONTACTS");
						m_isContactAdded = false;
					} else if (m_jArray.getString("Contact").toString()
							.equalsIgnoreCase("1")) {
						m_tvContact.setText("REMOVE FROM CONTACTS");
						m_isContactAdded = true;
					} else if (m_jArray.getString("Contact").toString()
							.equalsIgnoreCase("0")) {
						m_tvContact.setText("REQUEST SEND");
					}

				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			m_prgDialog.dismiss();
		}
	}

	/*
	 * Call the social webservice and inflate the data.
	 */
	class callOtherUserWS extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			m_prgDialog.setMessage("Loading...");
			m_prgDialog.setCancelable(false);
			m_prgDialog.setTitle(getResources().getString(R.string.app_name));
			m_prgDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			res = CommonUtils.parseJSON("social.php?uid="
					+ PeakAboo.m_sFollowerId);
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

					m_tvContactCount.setText(jo.getString("NumOfContact"));
					m_tvProfileName.setText(jo.getString("ProfileName"));
					m_tvPhotoCount.setText(jo.getString("NumOfImages"));
					m_tvFollowerCount.setText(jo.getString("NumOfFollowers"));
					m_tvProfDesc.setText(jo.getString("Description"));
					m_imageURL = jo.getString("ProfileImage");
					System.out
							.println("OtherUserProfile.onPostExecute()Profile Image===>"
									+ m_imageURL
									+ "USer Name==="
									+ jo.getString("ProfileName"));
					m_imgLoader.DisplayImage(m_imageURL, R.drawable.no_image,
							m_ivProfilePic);

					JSONArray m_imageArray = jo.getJSONArray("ImageUrl");

					if (m_imageArray.length() > 0) {
						System.out.println("ImageArray Is-------->"
								+ m_imageArray.toString() + "\n Image URL"
								+ m_imageURL);
						for (int i = 0; i < m_imageArray.length(); i++) {
							m_imageUrls.add(m_imageArray.getString(i));
						}

						m_photoGridAdpter = new PhotoGridAdapter(OtherProfileFragment.this.getActivity(),
								m_imageUrls);
						m_gvGrid.setAdapter(m_photoGridAdpter);
					}

				} else {
					Log.e("Error", "No Data Found.");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			m_prgDialog.dismiss();
		}
	}

	/**
	 * Webservice to remove the follower which is already added.
	 */
	class callRemoveFollowerWS extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/rm_follower.php?uid=107&fwid=85
			m_result = CommonUtils.parseJSON("rm_follower.php?uid=" + m_sUserID
					+ "&fwid=" + m_sFollowerId);
			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					JSONObject m_jObj = new JSONObject(result);
					Boolean m_b = m_jObj.getBoolean("Status");
					if (m_b) {
						m_sRecordResult = m_jObj.getString("result");
						m_tvFollower.setText("FOLLOW");
						m_isFollowerAdded = true;
					} else {
						m_sRecordResult = m_jObj.getString("result");
						m_tvFollower.setText("UNFOLLOW");
						m_isFollowerAdded = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Webservice to remove the contact which is already added.
	 */
	class callRemoveContactWS extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/rm_contact.php?uid=107&cid=98
			m_result = CommonUtils.parseJSON("rm_contact.php?uid=" + m_sUserID
					+ "&cid=" + m_sFollowerId);
			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					JSONObject m_jObj = new JSONObject(result);
					Boolean m_b = m_jObj.getBoolean("Status");
					if (m_b) {
						m_sRecordResult = m_jObj.getString("result");
						m_tvContact.setText("ADD TO CONTACTS");
						m_isContactAdded = true;
					} else {
						m_sRecordResult = m_jObj.getString("result");
						m_tvContact.setText("REMOVE FROM CONTACTS");
						m_isContactAdded = false;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Call Webservice to Add Follower.
	 */
	class callAddFollowerWS extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {

			// http://192.168.1.196/PeekaBoo_Webservice/addfollower.php?uid=76&fwid=80
			m_result = CommonUtils.parseJSON("addfollower.php?uid=" + m_sUserID
					+ "&fwid=" + m_sFollowerId);

			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					JSONObject m_jObj = new JSONObject(result);
					Boolean m_b = m_jObj.getBoolean("Status");
					if (m_b) {
						m_sRecordResult = m_jObj.getString("result");
						m_tvFollower.setText("UNFOLLOW");
						m_isFollowerAdded = true;
					} else {
						m_sRecordResult = m_jObj.getString("result");
						m_tvFollower.setText("FOLLOW");
						m_isFollowerAdded = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Call Webservice to Add Contact.
	 */
	class callAddContactWS extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// http://192.168.1.196/PeekaBoo_Webservice/addcontact.php?uid=105&cid=77
			m_result = CommonUtils.parseJSON("addcontact.php?uid=" + m_sUserID
					+ "&cid=" + m_sFollowerId);

			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				try {
					JSONObject m_jObj = new JSONObject(result);
					Boolean m_b = m_jObj.getBoolean("Status");
					if (m_b) {
						m_sRecordResult = m_jObj.getString("result");
						// m_tvContact.setText("REMOVE FROM CONTACTS");
						m_tvContact.setText("REQUEST SEND");
						m_isContactAdded = true;
					} else {
						m_sRecordResult = m_jObj.getString("result");
						m_tvContact.setText("ADD TO CONTACTS");
						m_isContactAdded = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
