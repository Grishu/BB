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
import android.widget.ListView;
import android.widget.TextView;

import com.zeal.Vo.FollowerContactVo;
import com.zeal.peak.adapter.FollowerContactListAdapter;
import com.zeal.peak.obejects.AsyncTask;
import com.zeal.peak.utils.CommonUtils;

public class FollowersListActivity extends Activity {
	private Context m_context;
	private String m_userId, m_sListResult;
	private ListView m_lvListItems;
	private ProgressDialog m_prgDialog;
	private ArrayList<FollowerContactVo> m_arrList;
	private FollowerContactListAdapter m_adapter;
	private TextView m_tvTitle;
	private ImageView m_ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.follower_contact_list_layout);
		m_context = FollowersListActivity.this;
		m_prgDialog = new ProgressDialog(m_context);
		m_userId = getIntent().getStringExtra("user_id");
		System.err.println("User Id--------" + m_userId);
		Tabbedmaincontroller.m_ivsetting.setVisibility(View.INVISIBLE);
		Tabbedmaincontroller.m_ivback.setVisibility(View.VISIBLE);
		PeakAboo.m_isSurprise=true;
		m_arrList = new ArrayList<FollowerContactVo>();
		m_lvListItems = (ListView) findViewById(R.id.fcl_lvList);
		m_tvTitle = (TextView) findViewById(R.id.fcl_tvTitle);
		m_ivBack = (ImageView) findViewById(R.id.fcl_ivBack);
		m_tvTitle.setText("Followers");

		callFollowersWS m_callWS = new callFollowersWS();
		m_callWS.execute();

		m_lvListItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adptr, View view, int pos,
					long id) {
				// Toast.makeText(m_context,
				// String.valueOf(m_arrList.get(pos).getM_userId()),
				// Toast.LENGTH_SHORT).show();

				PeakAboo.m_sFollowerId = String.valueOf(m_arrList.get(pos)
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
	}

	/*
	 * Call the Followers List webservice and parse the response in JSON.
	 */
	class callFollowersWS extends AsyncTask<String, Integer, String> {

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
			m_sListResult = CommonUtils.parseJSON("listfollow.php?uid="
					+ m_userId);// m_userId
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

					m_adapter = new FollowerContactListAdapter(m_context,
							m_arrList);
					m_lvListItems.setAdapter(m_adapter);
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			m_prgDialog.dismiss();
		}
	}
}
