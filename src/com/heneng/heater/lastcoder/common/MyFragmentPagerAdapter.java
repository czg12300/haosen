package com.heneng.heater.lastcoder.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	Fragment[] fragmentArray;
	
	public MyFragmentPagerAdapter(FragmentManager fm,Fragment[] fragmentArray2) {
		super(fm);
		if (null == fragmentArray2) {
			this.fragmentArray = new Fragment[] {};
		} else {
			this.fragmentArray = fragmentArray2;
		}
	}

	
	@Override
	public Fragment getItem(int arg0) {
		return fragmentArray[arg0];
	}

	@Override
	public int getCount() {
		return fragmentArray.length;
	}

}
