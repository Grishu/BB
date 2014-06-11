package com.zeal.peak.adapter;

import java.util.ArrayList;
import java.util.List;

import com.zeal.Vo.FollowerContactVo;
import com.zeal.peekaboo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class Taglistadapter extends BaseAdapter {
	Context ctxt;
	List<FollowerContactVo> data;
	private ArrayList<FollowerContactVo> m_arrVo;

	LayoutInflater li;

	public Taglistadapter(Context ctxt, List<FollowerContactVo> data) {
		super();
		this.ctxt = ctxt;
		this.data = data;
		m_arrVo = (ArrayList<FollowerContactVo>) data;
		li = (LayoutInflater) this.ctxt
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		viewhoder holder;
		if (convertView == null) {
			vi = li.inflate(R.layout.taglist_layout, parent, false);
			holder = new viewhoder();
			holder.tv = (TextView) vi.findViewById(R.id.tl_tv_text);
			holder.cb = (CheckBox) vi.findViewById(R.id.tl_cb);
			vi.setTag(holder);
		} else {
			holder = (viewhoder) vi.getTag();
		}
		holder.tv.setText(data.get(position).getM_profileName());
		holder.cb.setChecked(data.get(position).isSelected());
		return vi;
	}

	private class viewhoder {
		TextView tv;
		CheckBox cb;
	}

	public ArrayList<FollowerContactVo> name() {
		return m_arrVo;

	}
}
