package com.androlit.bookcloud.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by ${farhanarnob} on ${06-Oct-16}.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private FragmentManager fm;
    public HomePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void remove(int index){
            fm.getFragments().remove(0);
            fragments.remove(index);
            notifyDataSetChanged();
    }

    public void add(Fragment fragment){
        fragments.add(fragment);
        notifyDataSetChanged();
    }
}
