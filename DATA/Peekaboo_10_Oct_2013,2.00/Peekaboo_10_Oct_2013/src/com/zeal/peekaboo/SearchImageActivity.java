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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.penq.utils.LoadMoreGrid;
import com.zeal.Vo.SearchImageVideoVo;
import com.zeal.peak.adapter.SearchImageAdapter;
import com.zeal.peak.utils.CommonUtils;

public class SearchImageActivity extends Activity {

	private ProgressDialog pDlg;
	private Context m_context;
	private String m_sSearchTag, m_result;
	private LoadMoreGrid m_lmgImageGrid;
	private TextView m_tvNodata, m_tvTitle;
	private ArrayList<SearchImageVideoVo> m_arrVo;
	private ArrayList<String> m_arrImage;
	private SearchImageAdapter m_adapter;
	private ImageView m_ivBack;
	private boolean m_isVideo = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_image_layout);
		m_context = SearchImageActivity.this;
		pDlg = new ProgressDialog(m_context);
		m_arrVo = new ArrayList<SearchImageVideoVo>();
		m_arrImage = new ArrayList<String>();
		m_sSearchTag = getIntent().getStringExtra("Search_Tag");

		m_isVideo = getIntent().getBooleanExtra("PV", false);
		m_lmgImageGrid = (LoadMoreGrid) findViewById(R.id.sil_lmdImageGrid);
		m_tvNodata = (TextView) findViewById(R.id.sil_tvNoData);
		m_tvTitle = (TextView) findViewById(R.id.sil_tvTitle);
		m_ivBack = (ImageView) findViewById(R.id.sil_ivBack);

		if (!getIntent().getBooleanExtra("User", false)) {
			if (m_sSearchTag != null && !m_isVideo) {
				m_tvTitle.setText("Photos");
				callSearchPhotoWS m_searchImage = new callSearchPhotoWS();
				m_searchImage.execute(m_sSearchTag);
			} else if (m_sSearchTag != null && m_isVideo) {
				m_tvTitle.setText("Videos");
				callSearchVideoWS m_searchVideo = new callSearchVideoWS();
				m_searchVideo.execute(m_sSearchTag);
			}
		} else {
			m_tvTitle.setText("Users");
			callSearchUserWS m_searchUser = new callSearchUserWS();
			m_searchUser.execute(m_sSearchTag);
		}
		m_ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

	/**
	 * Method for Search Image webservice calling and parse the response.
	 */
	class callSearchPhotoWS extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDlg.setMessage("Loading...");
			pDlg.setCancelable(false);
			pDlg.setTitle(getResources().getString(R.string.app_name));
			pDlg.setCancelable(false);
			pDlg.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/search_image.php?q=snv
			m_result = CommonUtils.parseJSON("search_image.php?q=" + params[0]);

			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDlg.dismiss();
			if (result != null) {
				try {
					JSONObject m_jOb = new JSONObject(result);
					boolean m_status = m_jOb.getBoolean("Status");

					if (m_status) {
						JSONArray m_resultArr = m_jOb.getJSONArray("result");
						m_lmgImageGrid.setVisibility(View.VISIBLE);
						m_tvNodata.setVisibility(View.INVISIBLE);
						SearchImageVideoVo m_Vo;

						for (int i = 0; i < m_resultArr.length(); i++) {
							JSONObject jo_inside = m_resultArr.getJSONObject(i);
							System.out.println("Details Are===>"
									+ jo_inside.getString("Name") + "Url=="
									+ jo_inside.getString("MediaUrl"));
							m_Vo = new SearchImageVideoVo();
							m_Vo.setM_sName(jo_inside.getString("Name"));
							m_Vo.setM_sMediaUrl(jo_inside.getString("MediaUrl"));

							m_arrImage.add(jo_inside.getString("MediaUrl"));
							m_arrVo.add(m_Vo);
							m_Vo = null;

							m_adapter = new SearchImageAdapter(
									SearchImageActivity.this, m_arrVo, false,
									m_arrImage, false);
							m_lmgImageGrid.setAdapter(m_adapter);
						}
					} else {
						m_lmgImageGrid.setVisibility(View.INVISIBLE);
						m_tvNodata.setVisibility(View.VISIBLE);
						m_tvNodata.setText(m_jOb.getString("result"));
						showAlert(m_jOb.getString("result"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			m_arrVo = null;

		}
	}

	/**
	 * Method for Search Video webservice calling and parse the response.
	 */
	class callSearchVideoWS extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDlg.setMessage("Loading...");
			pDlg.setCancelable(false);
			pDlg.setTitle(getResources().getString(R.string.app_name));
			pDlg.setCancelable(false);
			pDlg.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/search_video.php?q=az
			m_result = CommonUtils.parseJSON("search_video.php?q=" + params[0]);

			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDlg.dismiss();
			if (result != null) {
				try {
					JSONObject m_jOb = new JSONObject(result);
					boolean m_status = m_jOb.getBoolean("Status");

					if (m_status) {
						JSONArray m_resultArr = m_jOb.getJSONArray("result");
						m_lmgImageGrid.setVisibility(View.VISIBLE);
						m_tvNodata.setVisibility(View.INVISIBLE);
						SearchImageVideoVo m_Vo;

						for (int i = 0; i < m_resultArr.length(); i++) {
							JSONObject jo_inside = m_resultArr.getJSONObject(i);
							System.out.println("Details Are===>"
									+ jo_inside.getString("Name") + "Url=="
									+ jo_inside.getString("MediaUrl"));
							m_Vo = new SearchImageVideoVo();
							m_Vo.setM_sName(jo_inside.getString("Name"));
							m_Vo.setM_sMediaUrl(jo_inside.getString("MediaUrl"));
							m_arrImage.add(jo_inside.getString("MediaUrl"));
							m_arrVo.add(m_Vo);
							m_Vo = null;

							m_adapter = new SearchImageAdapter(
									SearchImageActivity.this, m_arrVo, true,
									m_arrImage, false);
							m_lmgImageGrid.setAdapter(m_adapter);
						}
					} else {
						m_lmgImageGrid.setVisibility(View.INVISIBLE);
						m_tvNodata.setVisibility(View.VISIBLE);
						m_tvNodata.setText(m_jOb.getString("result"));
						showAlert(m_jOb.getString("result"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			m_arrVo = null;

		}
	}

	/**
	 * Method for Search User webservice calling and parse the response.
	 */
	class callSearchUserWS extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDlg.setMessage("Loading...");
			pDlg.setCancelable(false);
			pDlg.setTitle(getResources().getString(R.string.app_name));
			pDlg.setCancelable(false);
			pDlg.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// http://192.168.1.196/PeekaBoo_Webservice/search_user.php?q=test
			m_result = CommonUtils.parseJSON("search_user.php?q=" + params[0]);

			return m_result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDlg.dismiss();
			if (result != null) {
				try {
					JSONObject m_jOb = new JSONObject(result);
					boolean m_status = m_jOb.getBoolean("Status");

					if (m_status) {
						JSONArray m_resultArr = m_jOb.getJSONArray("result");
						m_lmgImageGrid.setVisibility(View.VISIBLE);
						m_tvNodata.setVisibility(View.INVISIBLE);
						SearchImageVideoVo m_Vo;

						for (int i = 0; i < m_resultArr.length(); i++) {
							JSONObject jo_inside = m_resultArr.getJSONObject(i);
							System.out.println("Details Are===>User-->"
									+ jo_inside.getString("UserId")
									+ jo_inside.getString("Name") + "Url=="
									+ jo_inside.getString("MediaUrl"));
							m_Vo = new SearchImageVideoVo();
							m_Vo.setM_sUserId(jo_inside.getString("UserId"));
							m_Vo.setM_sName(jo_inside.getString("Name"));
							m_Vo.setM_sMediaUrl(jo_inside.getString("MediaUrl"));
							m_arrImage.add(jo_inside.getString("MediaUrl"));
							m_arrVo.add(m_Vo);
							m_Vo = null;

							m_adapter = new SearchImageAdapter(
									SearchImageActivity.this, m_arrVo, false,
									m_arrImage, true);
							m_lmgImageGrid.setAdapter(m_adapter);
						}
					} else {
						m_lmgImageGrid.setVisibility(View.INVISIBLE);
						m_tvNodata.setVisibility(View.VISIBLE);
						m_tvNodata.setText(m_jOb.getString("result"));
						showAlert(m_jOb.getString("result"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			m_arrVo = null;

		}
	}

	private void showAlert(String p_mesg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(m_context);

		// Dialog Title
		alertDialog.setTitle(getResources().getString(R.string.app_name));
		// Dialog Message
		alertDialog.setMessage(p_mesg);
		alertDialog.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				});
		alertDialog.show();
	}
}
