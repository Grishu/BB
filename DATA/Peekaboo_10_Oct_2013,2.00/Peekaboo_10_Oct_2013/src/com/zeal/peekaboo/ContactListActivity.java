package com.zeal.peekaboo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.penq.utils.LoadMoreListView;
import com.penq.utils.LoadMoreListView.OnLoadMoreListener;
import com.zeal.Vo.FollowerContactVo;
import com.zeal.peak.adapter.FollowerContactListAdapter;
import com.zeal.peak.obejects.AsyncTask;
import com.zeal.peak.utils.CommonUtils;

public class ContactListActivity extends Activity {
	private Context m_context;
	private String m_userId, m_sListResult;
	private LoadMoreListView m_lvListItems;
	private ProgressDialog m_prgDialog;
	private ArrayList<FollowerContactVo> m_arrList;
	private FollowerContactListAdapter m_adapter;
	private TextView m_tvTitle;
	private ImageView m_ivBack;
	private int m_pageVal = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.follower_contact_list_layout);
		m_context = ContactListActivity.this;
		m_prgDialog = new ProgressDialog(m_context);
		m_userId = getIntent().getStringExtra("user_id");
		m_arrList = new ArrayList<FollowerContactVo>();
		m_lvListItems = (LoadMoreListView) findViewById(R.id.fcl_lvList);
		m_tvTitle = (TextView) findViewById(R.id.fcl_tvTitle);
		m_ivBack = (ImageView) findViewById(R.id.fcl_ivBack);
		m_tvTitle.setText("Contacts");

		callContactsWS m_callWS = new callContactsWS();
		m_callWS.execute();

		m_lvListItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> p_adpt, View p_view,
					int p_pos, long p_id) {
				/*
				 * Toast.makeText(m_context,
				 * String.valueOf(m_arrList.get(p_pos).getM_userId()),
				 * Toast.LENGTH_SHORT).show();
				 */

				PeakAboo.m_sFollowerId = String.valueOf(m_arrList.get(p_pos)
						.getM_userId());
				PeakAboo.m_isItemClicked = true;
				finish();

			}
		});

		m_ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		m_lvListItems.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				callContactsWS m_callWS = new callContactsWS();
				m_callWS.execute();
			}
		});
	}

	// Contact
	// List-->http://192.168.1.196/PeekaBoo_Webservice/listcontact.php?uid=76

	class callContactsWS extends AsyncTask<String, Integer, String> {

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
			m_sListResult = CommonUtils.parseJSON("listcontact.php?uid="
					+ m_userId + "&page=" + m_pageVal);// m_userId
			return m_sListResult;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				JSONObject jobj = new JSONObject(result);
				Boolean b = jobj.getBoolean("Status");
				if (b) {

					JSONArray m_jDetail = jobj.getJSONArray("result");

					for (int i = 0; i < m_jDetail.length(); i++) {
						FollowerContactVo m_fcVo;
						JSONObject jo = m_jDetail.getJSONObject(i);

						System.out.println("Name===="
								+ jo.getString("ProfileName") + "URL  "
								+ jo.getString("ImageUrl") + "ID--"
								+ jo.getInt("ID"));
						m_fcVo = new FollowerContactVo();
						m_fcVo.setM_userId(Integer.valueOf(jo.getInt("ID")));
						m_fcVo.setM_profileName(jo.getString("ProfileName"));
						m_fcVo.setM_imageUrl(jo.getString("ImageUrl"));

						m_arrList.add(m_fcVo);
					}

					// m_adapter = new FollowerContactListAdapter(m_context,
					// m_arrList);
					// m_lvListItems.setAdapter(m_adapter);
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			m_pageVal++;
			m_adapter.notifyDataSetChanged();
			m_lvListItems.onLoadMoreComplete();
			m_prgDialog.dismiss();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		m_adapter = new FollowerContactListAdapter(m_context, m_arrList);
		m_lvListItems.setAdapter(m_adapter);
	}
}
