package com.zeal.peak.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeal.Vo.DiceRollVo;
import com.zeal.peak.lazyload.ImageLoader;
import com.zeal.peekaboo.R;

public class DiceRollAdapter extends BaseAdapter {

	ArrayList<DiceRollVo> m_diceArry;
	private Context m_context;
	ImageLoader imgLoader;

	public DiceRollAdapter(Context p_context, ArrayList<DiceRollVo> p_diceArry) {
		m_context = p_context;
		m_diceArry = p_diceArry;
		imgLoader = new ImageLoader(m_context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_diceArry.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return m_diceArry.get(position);
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
			v = li.inflate(R.layout.lookaround_list_layout, null);

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
			
			/*m_value = Double.parseDouble(new DecimalFormat("000.00")
					.format(Double.valueOf(m_diceArry.get(position)
							.getM_distance().trim().toString())));
			System.out.println("LookAroundAdapter.getView() Double value===>"
					+ m_value);*/
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		// whenever you want to load an image from url
		// call DisplayImage function
		// url - image url to load
		// loader - loader image, will be displayed before getting image
		// image - ImageView
		imgLoader.DisplayImage(m_diceArry.get(position).getM_ImageUrl()
				.toString(), R.drawable.round_corner_image,
				m_holder.m_ivProfile);

		m_holder.m_profName
				.setText(m_diceArry.get(position).getM_profileName());
		System.out.println("$$$$$$$$$$$===="
				+ String.format(
						"%.2f",                                                                             
						Double.valueOf(m_diceArry.get(position).getM_distance()
								.trim().toString())));
		m_holder.m_distance.setText(String.format(
				"%.2f",
				Double.valueOf(m_diceArry.get(position).getM_distance().trim()
						.toString()))
				+ " KM");

		return v;
	}

	private class ViewHolder {
		private ImageView m_ivProfile;
		private TextView m_distance, m_profName, m_statusMesg;
	}

}
