package com.zeal.peekaboo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.penq.utils.LoadMoreGrid;
import com.penq.utils.LoadMoreGrid.OnLoadMoreListener;
import com.zeal.Vo.FeedsVo;
import com.zeal.peak.adapter.GridAdapter;
import com.zeal.peak.utils.CommonUtils;

public class GridFragment extends Fragment {
	public static ArrayList<FeedsVo> abcd;
	private LoadMoreGrid gv;
	private GridAdapter ga;
	private String m_urlResp, m_userIdVal;
	private Bundle m_bundle;
	private String profileName, imageUrl, m_hearts, m_likes, m_ImageID,
			m_videoUrl;
	private ProgressDialog pDlg;
	private Context m_context;
	private Boolean m_resultStatus;
	private TextView m_tvNoData;
	int pageval = 1;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.feedpage, container, false);
		m_context = GridFragment.this.getActivity();
		abcd = new ArrayList<FeedsVo>();
		pDlg = new ProgressDialog(m_context);
		Tabbedmaincontroller.m_tvHeader.setText(getResources().getString(
				R.string.lbl_peak_a_boo));
		m_bundle = this.getArguments();
		m_userIdVal = m_bundle.getString("userId");
		gv = (LoadMoreGrid) v.findViewById(R.id.feedgrid);
		m_tvNoData = (TextView) v.findViewById(R.id.tv_nodata);

		ga = new GridAdapter(abcd, getActivity());

		/*
		 * gv.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
		 * arg2, long arg3) { // Toast.makeText(getActivity(),
		 * String.valueOf(arg2), // Toast.LENGTH_LONG).show();
		 * 
		 * Intent in = new Intent(getActivity(), FeedDetail.class);
		 * in.putExtra("pos", arg2); in.putParcelableArrayListExtra("parcel",
		 * abcd); startActivity(in);
		 * 
		 * System.out.println("position======" + abcd.get(arg2).getM_imageId());
		 * } });
		 */

		gv.setOnLoadMoreListener(new OnLoadMoreListener() {

			public void onLoadMore() { // TODO Auto-generated method stub
				feedtask Feed = new feedtask();
				Feed.execute();

			}
		});

		return v;
	}

	class feedtask extends AsyncTask<String, Integer, String> {

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
			m_urlResp = CommonUtils.parseJSON("feeds.php?uid=" + m_userIdVal
					+ "&page=" + pageval);// +
			// m_userIdVal);
			// System.out.println("GridFragment Response--->"
			// + m_urlResp);
			try {
				// result = getmethod.Getrequest("feeds.php?uid=76");//
				// "+m_userIdVal);
				// System.out.println("Feeds Result--->" + result);
				if (m_urlResp != null) {
					JSONObject jobj = new JSONObject(m_urlResp);
					m_resultStatus = jobj.getBoolean("Status");
					FeedsVo m_feedVo = null;
					if (m_resultStatus) {
						JSONArray ja = jobj.getJSONArray("result");
						m_tvNoData.setVisibility(View.INVISIBLE);
						gv.setVisibility(View.VISIBLE);

						for (int i = 0; i < ja.length(); i++) {
							// for (int j = 0; j <= i; j++) {
							// JSONObject jo = ja.getJSONObject(j);
							// JSONArray resultJA = jo.getJSONArray("result");
							boolean isvideo = false;

							// for (int k = 0; k < resultJA.length(); k++) {
							JSONObject jo_inside = ja.getJSONObject(i);
							// for (int k = 0; k < jo_inside.length(); k++) {
							// JSONArray resultJA = jo_inside
							// .getJSONArray("result");
							m_videoUrl = "";

							// JSONObject m_resultObj = jo_inside
							// .getJSONObject("result");

							System.out.println("Result Arreay------->" + ja);
							profileName = jo_inside.getString("ProfileName");
							if (jo_inside.has("ImageID")) {
								m_ImageID = jo_inside.getString("ImageID");
								isvideo = false;
							}
							if (jo_inside.has("VideoID")) {
								m_ImageID = jo_inside.getString("VideoID");
								isvideo = true;
							}

							System.out.println("Profile Name===" + profileName);
							if (jo_inside.has("ImageUrl")) {
								imageUrl = jo_inside.getString("ImageUrl");
							}
							// video url
							if (jo_inside.has("VideoThumb")) {
								imageUrl = jo_inside.getString("VideoThumb");
								m_videoUrl = jo_inside.getString("VideoUrl");
							}

							m_hearts = jo_inside.getString("Hearts");
							m_likes = jo_inside.getString("Likes");

							m_feedVo = new FeedsVo();
							m_feedVo.setM_profileName(profileName);
							m_feedVo.setM_imageUrl(imageUrl);
							m_feedVo.setM_likes(m_likes);
							m_feedVo.setM_hearts(m_hearts);
							m_feedVo.setM_imageId(Integer.valueOf(m_ImageID));
							m_feedVo.setM_isCheckedPosition(isvideo);
							m_feedVo.setM_videoUrl(m_videoUrl);

							System.out.println("GridFragment.feedtask.====>"
									+ profileName + " Image URL--->" + imageUrl
									+ "Hearts====" + m_hearts + "Likes ===="
									+ m_likes);
							abcd.add(m_feedVo);
							System.out.println("Size of Arraylist-->"
									+ abcd.size());
							m_feedVo = null;
							// }

						}

					} else {
						m_urlResp = jobj.getString("result");
						/*
						 * Toast.makeText(m_context, m_failResult,
						 * Toast.LENGTH_LONG) .show();
						 */
						Log.d("Result", m_urlResp);
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return m_urlResp;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			System.err.println("onPostExecute=====" + abcd.size());

			if (result != null) {
				ga.notifyDataSetChanged();
				if (m_resultStatus != null) {
					if (m_resultStatus == false && abcd.size() < 0) {
						m_tvNoData.setVisibility(View.VISIBLE);
						m_tvNoData.setText(result);
						gv.setVisibility(View.INVISIBLE);
						Toast.makeText(m_context, result, Toast.LENGTH_LONG)
								.show();

					} else {

						m_tvNoData.setVisibility(View.INVISIBLE);
						gv.setVisibility(View.VISIBLE);

						ga.notifyDataSetChanged();
						pageval++;
						// m_adapter = new FeedsDetailAdapter(getActivity(),
						// abcd);
						// gv.setAdapter(m_adapter);
					}
				}
				gv.onLoadMoreComplete();

			}
			pDlg.dismiss();

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ga.distroy();
		// m_adapter.distroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		ga.pause();
		// m_adapter.pause();
	}

	@Override
	public void onResume() {
		super.onResume();
		pageval = 1;
		feedtask Feed = new feedtask();
		Feed.execute();
		gv.setAdapter(ga);
		ga.resume();
		abcd.clear();

		// m_adapter.resume();
		// m_adapter.notifyDataSetChanged();
	}

}
