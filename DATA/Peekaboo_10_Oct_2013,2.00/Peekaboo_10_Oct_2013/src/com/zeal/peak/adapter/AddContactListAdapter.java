package com.zeal.peak.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zeal.Vo.ContactVo;
import com.zeal.peekaboo.R;

public class AddContactListAdapter extends BaseAdapter {

	private Context m_context;
	private ArrayList<ContactVo> m_arrList;

	public AddContactListAdapter(Context p_context,
			ArrayList<ContactVo> p_arrList) {
		m_context = p_context;
		m_arrList = p_arrList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_arrList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return m_arrList.get(position);
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
			LayoutInflater li = (LayoutInflater) m_context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			m_view = li
					.inflate(R.layout.add_contact_list_layout, parent, false);

			m_holder = new ViewHolder();
			m_holder.m_tvName = (TextView) m_view
					.findViewById(R.id.adc_tvNameValue);
			m_holder.m_cbCheckContact = (CheckBox) m_view
					.findViewById(R.id.adc_cbCheck);
			m_holder.m_cbCheckContact.setTag(position);
			// m_holder.m_tvEmail = (TextView) m_view
			// .findViewById(R.id.adc_tvEmailValue);
			m_view.setTag(m_holder);
		} else {
			m_holder = (ViewHolder) m_view.getTag();
		}

		m_holder.m_tvName.setText(m_arrList.get(position).getM_sName());
		// m_holder.m_tvEmail.setText(m_arrList.get(position).getM_sEmail());
		m_holder.m_cbCheckContact.setChecked(m_arrList.get(position)
				.isSelected());
		return m_view;

	}

	private class ViewHolder {
		private TextView m_tvName;
		private CheckBox m_cbCheckContact;
	}

	public ArrayList<ContactVo> getEmail() {
		return m_arrList;

	}
}
