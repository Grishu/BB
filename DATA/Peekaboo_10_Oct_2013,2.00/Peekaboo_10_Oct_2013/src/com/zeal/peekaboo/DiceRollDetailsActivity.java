package com.zeal.peekaboo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zeal.Vo.DiceRollVo;
import com.zeal.peak.adapter.DiceRollAdapter;

public class DiceRollDetailsActivity extends Activity {
	private ListView m_lvList;
	DiceRollActivity m_diceActivity;
	ArrayList<DiceRollVo> m_arrayList;
	private Context m_context;
	DiceRollAdapter m_adapter;
	private TextView m_tvNoRecord;
	ImageView m_ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dice_details_layout);
		m_context = DiceRollDetailsActivity.this;

		m_lvList = (ListView) findViewById(R.id.dd_lvList);
		m_tvNoRecord = (TextView) findViewById(R.id.dd_tvNoRecord);
		m_ivBack = (ImageView) findViewById(R.id.dd_ivBack);
		m_diceActivity = new DiceRollActivity();
		m_arrayList = new ArrayList<DiceRollVo>();

		m_arrayList = m_diceActivity.m_diceArryList;
		System.out.println("Size of Details are--->" + m_arrayList.size());
		if (m_arrayList.size() > 0) {
			m_adapter = new DiceRollAdapter(m_context, m_arrayList);
			m_lvList.setAdapter(m_adapter);
			m_tvNoRecord.setVisibility(View.INVISIBLE);
			m_lvList.setVisibility(View.VISIBLE);
		} else {
			m_tvNoRecord.setVisibility(View.VISIBLE);
			m_lvList.setVisibility(View.INVISIBLE);
		}

		m_lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adptr, View view, int pos,
					long id) {
				/*
				 * Toast.makeText(m_context,
				 * String.valueOf(m_arrayList.get(pos).getM_userId()),
				 * Toast.LENGTH_SHORT).show();
				 */
				PeakAboo.m_sFollowerId = String.valueOf(m_arrayList.get(pos)
						.getM_userId());
				PeakAboo.m_isItemClicked = true;
				finish();
			}

		});

		m_ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				m_arrayList.clear();
				m_diceActivity.m_diceArryList.clear();
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		m_arrayList.clear();
		m_diceActivity.m_diceArryList.clear();
		finish();
	}
}
