package com.zeal.peak.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeal.Vo.LookAroundVo;
import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peekaboo.R;

public class LookAroundAdapter extends BaseAdapter {

	ArrayList<LookAroundVo> m_lookArry;
	private Context m_context;
	private double m_value;
	ImageLoader imgLoader;

	public LookAroundAdapter(Context p_context,
			ArrayList<LookAroundVo> p_lookArry) {
		m_context = p_context;
		m_lookArry = p_lookArry;
		imgLoader = new ImageLoader(m_context);
	}

	@Override
	public int getCount() {
		return m_lookArry.size();
	}

	@Override
	public Object getItem(int arg0) {
		return m_lookArry.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		ViewHolder m_holder;
		if (v == null) {
			LayoutInflater li = (LayoutInflater) m_context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.lookaround_list_layout, parent, false);

			m_holder = new ViewHolder();

			m_holder.m_ivProfile = (ImageView) v
					.findViewById(R.id.ll_ivprofImage);
			m_holder.m_distance = (TextView) v.findViewById(R.id.ll_tvDistance);
			m_holder.m_profName = (TextView) v
					.findViewById(R.id.ll_tvProfileName);
			m_holder.m_statusMesg = (TextView) v
					.findViewById(R.id.ll_tvMessage);
			v.setTag(m_holder);
		} else {
			m_holder = (ViewHolder) v.getTag();
		}

		try {

			m_value = Double.parseDouble(new DecimalFormat("00.0")
					.format(Double.valueOf(m_lookArry.get(position)
							.getM_distance().trim().toString())));
			System.out.println("LookAroundAdapter.getView() Double value===>"
					+ m_value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		// whenever you want to load an image from url
		// call DisplayImage function
		// url - image url to load
		// loader - loader image, will be displayed before getting image
		// image - ImageView
		imgLoader.DisplayImage(m_lookArry.get(position).getM_ImageUrl()
				.toString(), R.drawable.home, m_holder.m_ivProfile);
		// m_holder.m_ivProfile.setImageBitmap(DownloadFullFromUrl(m_lookArry
		// .get(position).getM_ImageUrl().toString()));
		m_holder.m_profName
				.setText(m_lookArry.get(position).getM_profileName());
		m_holder.m_distance.setText(String.valueOf(m_value) + " KM");
		m_holder.m_statusMesg.setText(m_lookArry.get(position)
				.getM_statusMesg());

		return v;
	}

	private class ViewHolder {
		private ImageView m_ivProfile;
		private TextView m_distance, m_profName, m_statusMesg;
	}

}
