package com.zeal.peak.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeal.Vo.FollowerContactVo;
import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peekaboo.R;

public class ContactListViewAdapter extends BaseAdapter {

	private Context m_context;
	private ArrayList<FollowerContactVo> m_arrVo;
	private ImageLoader m_imgLoader;
	public ArrayList<FollowerContactVo> m_arrList;

	private class ViewHolder {
		private ImageView m_ivProfile;
		private TextView m_tvprofName;
		private CheckBox checkbox1;
	}

	public ContactListViewAdapter(Context m_context2,
			ArrayList<FollowerContactVo> m_arrList) {
		m_context = m_context2;
		m_arrVo = m_arrList;
		this.m_arrList = m_arrList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_arrVo.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return m_arrVo.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View m_view = arg1;
		ViewHolder m_holder;
		if (m_view == null) {
			m_holder = new ViewHolder();
			LayoutInflater m_inflater = (LayoutInflater) m_context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			m_view = m_inflater.inflate(R.layout.msgcontact_list, null);

			m_holder.m_tvprofName = (TextView) m_view
					.findViewById(R.id.msgtitle);
			m_holder.m_ivProfile = (ImageView) m_view
					.findViewById(R.id.msgicon);
			m_holder.checkbox1 = (CheckBox) m_view.findViewById(R.id.checkbox);
			m_holder.checkbox1.setTag(position);
			m_view.setTag(m_holder);
			m_holder.m_ivProfile.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					/*
					 * CheckBox cb = (CheckBox) v; int temp= (Integer)
					 * cb.getTag(); FollowerContactVo ctct=m_arrVo.get(temp);
					 * System
					 * .err.println("********************** "+ctct.getM_userId
					 * ()); ctct.setSelected(cb.isChecked());
					 * m_arrList.get(temp).setSelected(cb.isChecked());
					 */

				}
			});

		} else {
			m_holder = (ViewHolder) m_view.getTag();
		}

		m_imgLoader = new ImageLoader(m_context);

		m_imgLoader.DisplayImage(m_arrVo.get(position).getM_imageUrl()
				.toString(), R.drawable.no_image, m_holder.m_ivProfile);
		m_holder.m_tvprofName.setText(m_arrVo.get(position).getM_profileName()
				.toString());
		m_holder.checkbox1.setChecked(m_arrVo.get(position).isSelected());
		return m_view;
	}

	public ArrayList<FollowerContactVo> name() {
		return m_arrList;

	}

}