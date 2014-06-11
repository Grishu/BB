package com.zeal.peekaboo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.penq.utils.LoadMoreListView.OnLoadMoreListener;
import com.zeal.Vo.FollowerContactVo;
import com.zeal.peak.adapter.ContactListViewAdapter;
import com.zeal.peak.adapter.Taglistadapter;
import com.zeal.peak.obejects.AsyncTask;
import com.zeal.peak.utils.CommonUtils;
import com.zeal.peekaboo.MessageContactList.callContactsWS;

import android.net.ParseException;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TagList extends Activity {
ListView lv;

private List<FollowerContactVo> m_arrList;
private ProgressDialog m_prgDialog;
private String m_userId;
private Taglistadapter ta;

private Button remove;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_list);
		//String[] test={"hai","hello","hai","hello","hai","hello"};
		//data=new ArrayList<String>();
		m_prgDialog=new ProgressDialog(this);
		m_userId = CommonUtils.getStringSharedPref(getApplicationContext(),
				"user_ID", "");
		m_arrList=new ArrayList<FollowerContactVo>();
		lv=(ListView)findViewById(R.id.tl_lv_tl);
		
	   remove=(Button)findViewById(R.id.tl_btns);
	   
	   remove.setOnClickListener(new OnClickListener() {
		
		
		public void onClick(View v) {
			StringBuffer responseText = new StringBuffer();
			// responseText.append("The following were selected...\n");

		    ArrayList<FollowerContactVo> list = ta.name();

			for (int i = 0; i < list.size(); i++) {
				FollowerContactVo state = list.get(i);

				if (state.isSelected()) {
					responseText.append(state.getM_profileName() + ",");
				}
			}

			String s = responseText.toString();
			if (s.equals("")) {
				AlertDialog.Builder ab = new AlertDialog.Builder(
						TagList.this);
				ab.setTitle("'Peak'a'Boo'");
				ab.setMessage("Please Select Tags");
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
			}
			else
			{
			int a = s.length();
			String contacts = s.substring(0, a - 1);
			Log.e("TAGS",contacts);
			Removetags rt= new Removetags();
			rt.execute(contacts);
			}
			
		}
	});
		

	
		
		callContactsWS m_callWS = new callContactsWS();
		m_callWS.execute();
	}

	// Contact
	// List-->http://192.168.1.196/PeekaBoo_Webservice/listcontact.php?uid=76

	class callContactsWS extends AsyncTask<String, Integer, String> {

		private String m_sListResult;

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
			m_sListResult = CommonUtils.parseJSON("surprise_tag_list.php?uid="
					+ m_userId);
			
			
			try {
				JSONObject jobj = new JSONObject(m_sListResult);
				Boolean b = jobj.getBoolean("Status");
				if (b) {
					JSONObject joj=jobj.getJSONObject("result");

					JSONArray m_jDetail = joj.getJSONArray("TagName");
					
					
					for (int i = 0; i < m_jDetail.length(); i++) {
						FollowerContactVo m_fcVo = new FollowerContactVo();
						String jo = m_jDetail.getString(i);
						
						m_fcVo.setM_profileName(jo);
						m_fcVo.setSelected(false);

						m_arrList.add(m_fcVo);
						
						
						
					}

					
				} else {
				
					m_sListResult="No data";

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
			if(result.equals("No data"))
				
			{
				AlertDialog.Builder ab = new AlertDialog.Builder(
						TagList.this);
				ab.setTitle("'Peak'a'Boo'");
				ab.setMessage("No tags found!");
				ab.setNeutralButton("Ok",
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

			ta=new Taglistadapter(TagList.this, m_arrList);
			lv.setAdapter(ta);
			lv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View p_view,
						int p_pos, long arg3) {
//					Toast.makeText(getApplicationContext(),
//							"You have selected item no." + (p_pos + 1) + "",
//							Toast.LENGTH_SHORT).show();

					if (p_view != null) {
						CheckBox checkBox = (CheckBox) p_view
								.findViewById(R.id.tl_cb);
						checkBox.setChecked(!checkBox.isChecked());

						FollowerContactVo ctct = m_arrList.get(p_pos);
						System.err.println("********************** "
								+ ctct.getM_userId());
						// ctct.setSelected(cb.isChecked());
						m_arrList.get(p_pos).setSelected(checkBox.isChecked());
					}

				}
			});
			m_prgDialog.dismiss();
		}

	}

	
	

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag_list, menu);
		return true;
	}*/
	
	class Removetags extends AsyncTask<String, Integer, String> {

		@Override
		
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			m_prgDialog.setMessage("Loading...");
			m_prgDialog.setCancelable(false);
			m_prgDialog.setTitle(getResources().getString(R.string.app_name));
			m_prgDialog.show();
		}
		
		protected String doInBackground(String... params) {
		
			 String post=params[0];
			 String result = "";
				try {
			result= CommonUtils.parseJSON("surprise_delete.php?uid="+m_userId+"&tag="+post);

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return result;
		}
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			m_prgDialog.dismiss();
			
			Log.e("result:", result);
			if (result != null) {
				try {
					JSONObject jo = new JSONObject(result);
					Boolean b = jo.getBoolean("Status");

					if (b) {
						AlertDialog.Builder ab = new AlertDialog.Builder(
								TagList.this);
						ab.setTitle("'Peak'a'Boo'");
						ab.setMessage("Tags removed successfully");
						ab.setNeutralButton("Ok",
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
								TagList.this);
						ab.setTitle("'Peak'a'Boo'");
						ab.setMessage("Error in remove tags!Try Later.");
						ab.setNeutralButton("Ok",
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
		
	
	}

}
