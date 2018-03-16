package com.damai.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.citywithincity.utils.ReflectUtil;
import com.damai.dl.HostFragment;
import com.damai.dl.IPluginFragment;

public class TabFragmentAdapter extends FragmentPagerAdapter {
	private String[] fragmentClasses;
	private ClassLoader classLoader;
	public TabFragmentAdapter(FragmentManager fm,ClassLoader classLoaders,String[] fragmentClasses) {
		super(fm);
		this.classLoader = classLoaders;
		this.fragmentClasses = fragmentClasses;
	}

	@Override
	public Fragment getItem(int position) {
		try {
			Object fragment = ReflectUtil.newInstance(fragmentClasses[position],classLoader);
			if(fragment instanceof IPluginFragment){
				HostFragment result = new HostFragment();
				result.setFragment((IPluginFragment)fragment);
				return result;
			}
			return (Fragment) fragment;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getCount() {
		return fragmentClasses.length;
	}

}
