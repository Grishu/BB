package com.zeal.peekaboo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.penq.utils.LoadMoreListView;
import com.penq.utils.LoadMoreListView.OnLoadMoreListener;
import com.zeal.Vo.FollowerContactVo;
import com.zeal.peak.adapter.ContactListViewAdapter;
import com.zeal.peak.obejects.AsyncTask;
import com.zeal.peak.utils.CommonUtils;

public class MessageContactList extends Activity implements
		OnItemClickListener, OnClickListener {

	private Context m_context;
	private String m_userId, m_sListResult;
	private LoadMoreListView m_lvListItems;
	
	private ProgressDialog m_prgDialog;
	private ArrayList<FollowerContactVo> m_arrList;
	private ContactListViewAdapter m_ctadapter;
	private TextView m_tvTitle;
	private ImageView sendmsg;
	
	int current_page = 1;
	int loading=1;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactlist);
		m_context = MessageContactList.this;
		m_prgDialog = new ProgressDialog(m_context);
		m_userId = CommonUtils.getStringSharedPref(getApplicationContext(),
				"user_ID", "");
		Log.e("", m_userId);
		m_arrList = new ArrayList<FollowerContactVo>();
		m_lvListItems = (LoadMoreListView) findViewById(R.id.msglist);
		sendmsg = (ImageView) findViewById(R.id.sendmsg);

		sendmsg.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				StringBuffer responseText = new StringBuffer();
				// responseText.append("The following were selected...\n");

				ArrayList<FollowerContactVo> list = m_ctadapter.name();

				for (int i = 0; i < list.size(); i++) {
					FollowerContactVo state = list.get(i);

					if (state.isSelected()) {
						responseText.append(state.getM_userId() + ",");
					}
				}

				String s = responseText.toString();

				if (s == "") {
					AlertDialog.Builder ab = new AlertDialog.Builder(
							MessageContactList.this);
					ab.setTitle("'Peak'a'Boo'");
					ab.setMessage("Please Select Contacts");
					ab.setNeutralButton("ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
					ab.show();
				} else {
					int a = s.length();
					String contacts = s.substring(0, a - 1);
					Intent i = new Intent(MessageContactList.this,
							MainActivity1.class);
					i.putExtra("Upldmsg", 2);
					i.putExtra("Contact", contacts);

					startActivity(i);
					finish();
				}
//				Toast.makeText(getApplicationContext(), responseText,
//						Toast.LENGTH_SHORT).show();

			}

		});

		callContactsWS m_callWS = new callContactsWS();
		m_callWS.execute();

		m_lvListItems.setOnItemClickListener(new OnItemClickListener() {

			public void onListItemClick(ListView p_adpt, View p_view,
					int p_pos, long p_id) {
				/*
				 * Toast.makeText(m_context,
				 * String.valueOf(m_arrList.get(p_pos).getM_userId()),
				 * Toast.LENGTH_SHORT).show();
				 * 
				 * PeakAboo.m_sFollowerId = String.valueOf(m_arrList.get(p_pos)
				 * .getM_userId()); PeakAboo.m_isItemClicked = true; finish();
				 */

			}

			public void onItemClick(AdapterView<?> arg0, View p_view,
					int p_pos, long arg3) {
//				Toast.makeText(getApplicationContext(),
//						"You have selected item no." + (p_pos + 1) + "",
//						Toast.LENGTH_SHORT).show();

				if (p_view != null) {
					CheckBox checkBox = (CheckBox) p_view
							.findViewById(R.id.checkbox);
					checkBox.setChecked(!checkBox.isChecked());

					FollowerContactVo ctct = m_arrList.get(p_pos);
					System.err.println("********************** "
							+ ctct.getM_userId());
					// ctct.setSelected(cb.isChecked());
					m_arrList.get(p_pos).setSelected(checkBox.isChecked());
				}

			}
		});
		
		m_lvListItems.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				callContactsWS m_msgcontacts = new callContactsWS();
				m_msgcontacts.execute();
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
			if(loading!=0)
			{
			m_prgDialog.setMessage("Loading...");
			m_prgDialog.setCancelable(false);
			m_prgDialog.setTitle(getResources().getString(R.string.app_name));
			m_prgDialog.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			if(loading!=0)
			{
			m_sListResult = CommonUtils.parseJSON("listcontact.php?uid="
					+ m_userId+"&page="+current_page);
			Log.e("URL-->","listcontact.php?uid="
					+ m_userId+"&page="+current_page);
			}
			try {
				JSONObject jobj = new JSONObject(m_sListResult);
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
						m_fcVo.setSelected(false);

						m_arrList.add(m_fcVo);
						
						
					}

					
				} else {
                       loading=0;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return m_sListResult;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			
			m_ctadapter.notifyDataSetChanged();
			current_page++;
			m_lvListItems.onLoadMoreComplete();
			m_prgDialog.dismiss();
		}

	}
	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		m_ctadapter = new ContactListViewAdapter(m_context,m_arrList);
		m_lvListItems.setAdapter(m_ctadapter);
	
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}