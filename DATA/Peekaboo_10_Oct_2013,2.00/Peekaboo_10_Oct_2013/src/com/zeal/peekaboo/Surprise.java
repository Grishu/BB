package com.zeal.peekaboo;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.clasess.Images;

public class Surprise extends Fragment {
	surpricepager sp;
	ViewPager vp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		sp = new surpricepager(getActivity().getSupportFragmentManager());

		View v = inflater.inflate(R.layout.surprisefrag, container, false);
		vp = (ViewPager) v.findViewById(R.id.pager);
		vp.setAdapter(sp);

		return v;

	}

	public void flip() {
		vp.setCurrentItem(vp.getCurrentItem() + 1);
	}

	private class surpricepager extends FragmentStatePagerAdapter {

		public surpricepager(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		List<String> data = Arrays.asList(Images.imageThumbUrls);

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		/*
		 * @Override public boolean isViewFromObject(View arg0, Object arg1) {
		 * // TODO Auto-generated method stub return false; }
		 */

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub

			return Surpricesinglepage
					.newInstance(data.get(arg0), getActivity());
		}
	}

}
