package com.zeal.peak.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeal.Vo.FollowerContactVo;
import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peekaboo.R;

public class FollowerContactListAdapter extends BaseAdapter {

	private Context m_context;
	private ArrayList<FollowerContactVo> m_arrVo;
	private ImageLoader m_imgLoader;

	public FollowerContactListAdapter(Context p_context,
			ArrayList<FollowerContactVo> p_arrVo) {
		m_context = p_context;
		m_arrVo = p_arrVo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_arrVo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return m_arrVo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View m_view = convertView;
		ViewHolder m_holder;
		if (m_view == null) {
			m_holder = new ViewHolder();
			LayoutInflater m_inflater = (LayoutInflater) m_context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			m_view = m_inflater.inflate(R.layout.fcl_row_layout, null);

			m_holder.m_tvprofName = (TextView) m_view
					.findViewById(R.id.fcl_tvProfileName);
			m_holder.m_ivProfile = (ImageView) m_view
					.findViewById(R.id.fcl_ivprofImage);
			m_view.setTag(m_holder);
		} else {
			m_holder = (ViewHolder) m_view.getTag();
		}

		m_imgLoader = new ImageLoader(m_context);

		m_imgLoader.DisplayImage(m_arrVo.get(position).getM_imageUrl()
				.toString(), R.drawable.maskicon,
				m_holder.m_ivProfile);
		m_holder.m_tvprofName.setText(m_arrVo.get(position).getM_profileName()
				.toString());
		return m_view;
	}

	private class ViewHolder {
		private ImageView m_ivProfile;
		private TextView m_tvprofName;
	}
}
