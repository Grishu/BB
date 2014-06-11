package com.zeal.peekaboo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.penq.utils.LoadMoreListView;
import com.zeal.Vo.MessageVo;
import com.zeal.Vo.NotificationVo;
import com.zeal.peak.adapter.MessageListAdapter;
import com.zeal.peak.adapter.NotificationListAdapter;
import com.zeal.peak.obejects.AsyncTask;
import com.zeal.peak.utils.CommonUtils;

public class NotificationFragment extends Fragment {

	private Context m_context;
	private Button m_btnNotification, m_btnMessg;
	private LoadMoreListView m_lvList;
	private ProgressDialog m_progDialog;
	private String m_userID, m_response, m_profName, m_mediaUrl, m_result,
			m_imgUrl, m_sLastLoad, Id, m_videoUrl;
	private Bundle m_buBundle;
	private ArrayList<MessageVo> m_mesgArry;
	private ArrayList<NotificationVo> m_notifArray;
	private MessageListAdapter m_adapter;
	private NotificationListAdapter m_notifAdapter;
	private boolean m_isMessageRecord = false;
	private TextView m_tvNoData;
	private ImageView m_ivNotifArrow, m_ivMesgArrow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.notification_layout, container,
				false);
		Tabbedmaincontroller.m_tvHeader.setText(getResources().getString(
				R.string.lbl_peak_a_boo));
		m_context = NotificationFragment.this.getActivity();
		m_progDialog = new ProgressDialog(m_context);
		m_mesgArry = new ArrayList<MessageVo>();
		m_notifArray = new ArrayList<NotificationVo>();
		m_btnNotification = (Button) v.findViewById(R.id.nl_btnNotif);
		m_btnMessg = (Button) v.findViewById(R.id.nl_btnMessg);
		m_tvNoData = (TextView) v.findViewById(R.id.nl_tvnoData);
		m_lvList = (LoadMoreListView) v.findViewById(R.id.nl_lvList);
		m_ivNotifArrow = (ImageView) v.findViewById(R.id.nl_ivNotifArrow);
		m_ivMesgArrow = (ImageView) v.findViewById(R.id.nl_ivMesgArrow);

		m_buBundle = this.getArguments();
		m_userID = m_buBundle.getString("user_Id");

		m_btnNotification.setOnClickListener(m_onClickListener);
		m_btnMessg.setOnClickListener(m_onClickListener);

		m_sLastLoad = CommonUtils
				.getStringSharedPref(m_context, "LastLoad", "");
		if (m_sLastLoad.equalsIgnoreCase("Messages")) {
			MessageDetails();
		} else if (m_sLastLoad.equalsIgnoreCase("Notifications")) {
			NofiticationDetails();
		} else {
			NofiticationDetails();
		}
		m_lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> p_adptr, View p_view,
					int p_position, long p_id) {
				if (m_isMessageRecord) {
					System.out.println("Size of Messg is---"
							+ m_mesgArry.size());
					// Toast.makeText(
					// m_context,
					// "Follower Name URL:=="
					// + m_mesgArry.get(p_position)
					// .getM_mediaUrl(), Toast.LENGTH_LONG)
					// .show();

					if (m_mesgArry.get(p_position).getM_videoUrl() != null) {

						Intent intent = new Intent(Intent.ACTION_VIEW)
								.setDataAndType(Uri.parse(m_mesgArry.get(
										p_position).getM_videoUrl()), "video/*");
						startActivity(intent);
						// m_intent.putExtra("MediaUrl", m_mesgArry
						// .get(p_position).getM_videoUrl());

					} else if (m_mesgArry.get(p_position).getM_videoUrl() == null) {
						Intent m_intent = new Intent(m_context,
								ShowMessageDetails.class);
						m_intent.putExtra("MediaUrl", m_mesgArry
								.get(p_position).getM_mediaUrl());
						startActivity(m_intent);
					}

				} else {

					/*
					 * Toast.makeText( m_context, " Image URL:==" +
					 * m_notifArray.get(p_position).getM_id(),
					 * Toast.LENGTH_LONG).show();
					 */
					Id = m_notifArray.get(p_position).getM_id();
					showNotificationAlert(Id);
				}
			}
		});

		/*
		 * m_lvList.setOnLoadMoreListener(new OnLoadMoreListener() {
		 * 
		 * @Override public void onLoadMore() { if (m_isMessageRecord) {
		 * callMessageWS m_mesgWS = new callMessageWS(); m_mesgWS.execute(); }
		 * else { callNotificationWS m_notifWS = new callNotificationWS();
		 * m_notifWS.execute(); } } });
		 */
		return v;
	}

	/**
	 * Common Click Listener.
	 */
	OnClickListener m_onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.nl_btnMessg:
				CommonUtils.setStringSharedPref(m_context, "LastLoad",
						"Messages");
				MessageDetails();
				break;
			case R.id.nl_btnNotif:
				CommonUtils.setStringSharedPref(m_context, "LastLoad",
						"Notifications");
				NofiticationDetails();
				break;

			default:
				break;
			}

		}
	};

	private void showNotificationAlert(final String p_Id) {

		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				m_context);
		alertDialog.setCancelable(false);
		alertDialog.setTitle(getResources().getString(R.string.app_name));
		alertDialog.setMessage("Add to Contact.");
		alertDialog.setNegativeButton("ACCEPT",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						callContactAddWS m_call = new callContactAddWS();
						m_call.execute(p_Id);
						NofiticationDetails();
					}
				});
		alertDialog.setPositiveButton("REJECT", null);
		alertDialog.show();
	}

	private void MessageDetails() {
		m_isMessageRecord = true;
		m_ivMesgArrow.setVisibility(View.VISIBLE);
		m_ivNotifArrow.setVisibility(View.INVISIBLE);
		m_btnMessg.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.strip_arrow));
		m_btnNotification.setBackgroundColor(getResources().getColor(
				R.color.orange1));
		callMessageWS m_mesgWS = new callMessageWS();
		m_mesgWS.execute();
		m_mesgArry.clear();
	}

	private void NofiticationDetails() {
		m_isMessageRecord = false;
		m_ivMesgArrow.setVisibility(View.INVISIBLE);
		m_ivNotifArrow.setVisibility(View.VISIBLE);
		m_btnMessg.setBackgroundColor(getResources().getColor(R.color.orange1));
		m_btnNotification.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.strip_arrow));
		callNotificationWS m_notifWS = new callNotificationWS();
		m_notifWS.execute();
		m_notifArray.clear();
	}

	/**
	 * Accept the request of Notification webservice.
	 */
	class callContactAddWS extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			m_progDialog.setMessage("Loading...");
			m_progDialog.setTitle(getResources().getString(R.string.app_name));
			m_progDialog.setCancelable(false);
			m_progDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/contact_selection.php?uid=74&cid=77
			m_response = CommonUtils.parseJSON("contact_selection.php?uid="
					+ m_userID + "&cid=" + params[0]);
			return m_response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				JSONObject m_obj;
				try {
					m_obj = new JSONObject(result);
					Boolean b = m_obj.getBoolean("status");
					if (b) {
						Toast.makeText(m_context, m_obj.getString("result"),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(m_context, m_obj.getString("result"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			m_progDialog.dismiss();
			m_notifAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Call the Message webservice to show the Messages notification.
	 */
	class callMessageWS extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			m_progDialog.setMessage("Loading...");
			m_progDialog.setTitle(getResources().getString(R.string.app_name));
			m_progDialog.setCancelable(false);
			m_progDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/message_receive.php?uid=76
			m_response = CommonUtils.parseJSON("message_receive.php?uid="
					+ m_userID);

			if (m_response != null) {
				try {
					MessageVo m_msgVo;
					JSONObject m_jObj = new JSONObject(m_response);
					Boolean m_status = m_jObj.getBoolean("status");
					JSONArray m_arrResult = m_jObj.getJSONArray("result");
					m_result = m_jObj.getString("result");
					if (m_status) {

						for (int i = 0; i < m_arrResult.length(); i++) {
							JSONObject jObj = m_arrResult.getJSONObject(i);

							m_profName = jObj.getString("ProfileName");
							m_mediaUrl = jObj.getString("MediaUrl");
							m_imgUrl = jObj.getString("ProfilePic");

							m_msgVo = new MessageVo();
							m_msgVo.setM_profileName(m_profName);
							m_msgVo.setM_mediaUrl(m_mediaUrl);
							m_msgVo.setM_profImgUrl(m_imgUrl);

							if (jObj.has("VideoUrl")) {
								m_videoUrl = jObj.getString("VideoUrl");
								m_msgVo.setM_videoUrl(m_videoUrl);
							}

							m_mesgArry.add(m_msgVo);

						}
					} else {
						m_result = m_jObj.getString("result");

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// m_adapter.notifyDataSetChanged();
			if (m_mesgArry.size() > 0) {
				m_tvNoData.setVisibility(View.INVISIBLE);
				m_lvList.setVisibility(View.VISIBLE);
				m_adapter = new MessageListAdapter(m_context, m_mesgArry);
				m_lvList.setAdapter(m_adapter);
			} else {
				m_tvNoData.setVisibility(View.VISIBLE);
				m_lvList.setVisibility(View.INVISIBLE);
				m_tvNoData.setText(m_result);
			}
			m_progDialog.dismiss();
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// m_mesgArry = new ArrayList<MessageVo>();
		// m_notifArray = new ArrayList<NotificationVo>();
	}

	/**
	 * Call the Notification webservice and parse the JSON response.
	 */
	class callNotificationWS extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			m_progDialog.setMessage("Loading...");
			m_progDialog.setTitle(getResources().getString(R.string.app_name));
			m_progDialog.setCancelable(false);
			m_progDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/list_all_contacts.php?uid=74
			m_response = CommonUtils.parseJSON("list_all_contacts.php?uid="
					+ m_userID);
			if (m_response != null) {
				NotificationVo m_notifVo;
				JSONObject m_jNotif;
				try {
					m_jNotif = new JSONObject(m_response);
					Boolean m_staut = m_jNotif.getBoolean("Status");
					JSONArray m_arrNotifResult = m_jNotif
							.getJSONArray("result");
					m_result = m_jNotif.getString("result");
					if (m_staut) {
						for (int i = 0; i < m_arrNotifResult.length(); i++) {
							JSONObject jObj = m_arrNotifResult.getJSONObject(i);

							m_notifVo = new NotificationVo();
							m_notifVo.setM_id(jObj.getString("ID"));
							m_notifVo.setM_profName(jObj
									.getString("ProfileName"));
							m_notifVo.setM_imgUrl(jObj.getString("ImageUrl"));

							m_notifArray.add(m_notifVo);
							/*
							 * m_tvNoData.setVisibility(View.INVISIBLE);
							 * m_lvList.setVisibility(View.VISIBLE);
							 */
						}
					} else {
						m_result = m_jNotif.getString("result");
						/*
						 * m_tvNoData.setVisibility(View.VISIBLE);
						 * m_lvList.setVisibility(View.INVISIBLE);
						 * m_tvNoData.setText(m_result);
						 */
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (m_notifArray.size() > 0 && m_notifArray != null) {
				m_tvNoData.setVisibility(View.INVISIBLE);
				m_lvList.setVisibility(View.VISIBLE);
				m_notifAdapter = new NotificationListAdapter(m_context,
						m_notifArray);
				m_lvList.setAdapter(m_notifAdapter);
			} else {

				m_lvList.setVisibility(View.INVISIBLE);
				m_tvNoData.setText(result);
				m_tvNoData.setVisibility(View.VISIBLE);

			}
			m_progDialog.dismiss();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		m_mesgArry.clear();
		m_mesgArry = null;
		m_notifArray.clear();
		m_notifArray = null;
	}
}
