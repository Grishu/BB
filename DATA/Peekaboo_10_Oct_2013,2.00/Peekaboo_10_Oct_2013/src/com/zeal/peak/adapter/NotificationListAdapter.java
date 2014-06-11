package com.zeal.peak.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zeal.Vo.NotificationVo;
import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peekaboo.R;

public class NotificationListAdapter extends BaseAdapter {

	private Context m_context;
	private ImageLoader m_imgLoader;
	private ArrayList<NotificationVo> m_notifArry;

	public NotificationListAdapter(Context p_context,
			ArrayList<NotificationVo> p_notifArry) {
		m_context = p_context;
		m_notifArry = p_notifArry;
		m_imgLoader = new ImageLoader(m_context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_notifArry.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return m_notifArry.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder m_holder;
		if (v == null) {
			LayoutInflater li = (LayoutInflater) m_context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.mesg_notif_layout, parent, false);

			m_holder = new ViewHolder();

			m_holder.m_ivprofImage = (ImageView) v
					.findViewById(R.id.mnl_ivprofImage);
			m_holder.m_tvName = (TextView) v.findViewById(R.id.mnl_tvMessage);
			v.setTag(m_holder);
		} else {
			m_holder = (ViewHolder) v.getTag();
		}

		m_imgLoader.DisplayImage(m_notifArry.get(position).getM_imgUrl()
				.toString(), R.drawable.home, m_holder.m_ivprofImage);
		m_holder.m_tvName.setText(m_notifArry.get(position).getM_profName()
				+ "  want to add you as a contact");
		return v;
	}

	private class ViewHolder {
		private ImageView m_ivprofImage;
		private TextView m_tvName;

	}
}
