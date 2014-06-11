package com.zeal.peekaboo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeal.Vo.ContactVo;
import com.zeal.peak.adapter.AddContactListAdapter;
import com.zeal.peak.obejects.AsyncTask;
import com.zeal.peak.utils.CommonUtils;

public class AddressBookMatchingContactActivity extends Activity {
	private Context m_context;
	private ListView m_lvContact;
	private ArrayList<ContactVo> m_arrContact;
	private TextView m_tvNoData;
	private AddContactListAdapter m_adapter;
	private ImageView m_ivBack, m_ivDone;
	private String m_result, m_userId;
	private ProgressDialog m_prgDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_book_layout);
		m_context = AddressBookMatchingContactActivity.this;
		m_arrContact = new ArrayList<ContactVo>();
		m_prgDialog = new ProgressDialog(m_context);
		m_lvContact = (ListView) findViewById(R.id.abl_lvAddressList);
		m_tvNoData = (TextView) findViewById(R.id.abl_tvNodata);
		m_ivBack = (ImageView) findViewById(R.id.abl_ivBack);
		m_ivDone = (ImageView) findViewById(R.id.abl_ivDone);
		m_arrContact = getNameEmailDetails();
		m_userId = CommonUtils.getStringSharedPref(getApplicationContext(),
				"user_ID", "");
		if (m_arrContact.size() > 0) {
			m_adapter = new AddContactListAdapter(m_context, m_arrContact);
			m_lvContact.setAdapter(m_adapter);
			m_tvNoData.setVisibility(View.INVISIBLE);
			m_lvContact.setVisibility(View.VISIBLE);
		} else {
			m_tvNoData.setVisibility(View.VISIBLE);
			m_lvContact.setVisibility(View.INVISIBLE);
			m_tvNoData.setText("No Contact Exists.");
		}

		m_lvContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View p_view,
					int p_position, long p_id) {
				if (p_view != null) {
//					Toast.makeText(
//							getApplicationContext(),
//							"You have selected item no." + (p_position + 1)
//									+ "", Toast.LENGTH_SHORT).show();
					CheckBox checkBox = (CheckBox) p_view
							.findViewById(R.id.adc_cbCheck);
					checkBox.setChecked(!checkBox.isChecked());

					ContactVo m_contactVo = m_arrContact.get(p_position);
					System.err.println("********************** "
							+ m_contactVo.getM_sName() + " Email"
							+ m_contactVo.getM_sEmail());
					m_arrContact.get(p_position).setSelected(
							checkBox.isChecked());
				}
			}

		});
		m_ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		m_ivDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StringBuffer responseText = new StringBuffer();
				// responseText.append("The following were selected...\n");

				ArrayList<ContactVo> list = m_adapter.getEmail();

				for (int i = 0; i < list.size(); i++) {
					ContactVo state = list.get(i);

					if (state.isSelected()) {
						responseText.append(state.getM_sEmail() + ",");
					}
				}
				String m_sSelectedEmail = responseText.toString();
				System.out.println("Email Selected Are----" + m_sSelectedEmail);
				if (m_sSelectedEmail != null && m_sSelectedEmail != "") {
					callEmailContactWS m_emailWS = new callEmailContactWS();
					m_emailWS.execute(m_sSelectedEmail);
				} else {
					AlertDialog.Builder ab = new AlertDialog.Builder(m_context);
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
				}
			}
		});
	}

	class callEmailContactWS extends AsyncTask<String, Integer, String> {

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
			// http://192.168.1.196/PeekaBoo_Webservice/email_list.php?email=
			// snv.jan@gmail.com,test@testing.com,snv_3@rediffmail.com&uid=178
			m_result = CommonUtils.parseJSON("email_list.php?email="
					+ params[0] + "&uid=" + m_userId);
			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			m_prgDialog.dismiss();
		}
	}

	/**
	 * super fast query to pull email addresses. It is much faster than pulling
	 * all contact columns as suggested by other answers...
	 * 
	 * @return
	 */
	public ArrayList<ContactVo> getNameEmailDetails() {
		ArrayList<ContactVo> m_arrList = new ArrayList<ContactVo>();
		// ArrayList<String> emlRecs = new ArrayList<String>();
		// HashSet<String> emlRecsHS = new HashSet<String>();
		ContentResolver cr = m_context.getContentResolver();
		String[] PROJECTION = new String[] { ContactsContract.RawContacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.PHOTO_ID,
				ContactsContract.CommonDataKinds.Email.DATA,
				ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
		String order = "CASE WHEN " + ContactsContract.Contacts.DISPLAY_NAME
				+ " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
				+ ContactsContract.Contacts.DISPLAY_NAME + ", "
				+ ContactsContract.CommonDataKinds.Email.DATA
				+ " COLLATE NOCASE";
		String filter = ContactsContract.CommonDataKinds.Email.DATA
				+ " NOT LIKE ''";
		Cursor cur = cr.query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION,
				filter, null, order);
		ContactVo m_vo;
		if (cur.moveToFirst()) {
			do {
				m_vo = new ContactVo();
				// names comes in hand sometimes
				String name = cur.getString(1);
				String emlAddr = cur.getString(3);

				m_vo.setM_sName(name);
				m_vo.setM_sEmail(emlAddr);
				// keep unique only
				/*
				 * if (emlRecsHS.add(emlAddr.toLowerCase())) {
				 * emlRecs.add(emlAddr); }
				 */
				m_arrList.add(m_vo);
			} while (cur.moveToNext());
		}

		cur.close();
		return m_arrList;
	}
}
