package com.zogamonline.laisiangthou.adapters.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zogamonline.laisiangthou.fragments.common.about.AboutFragment;
import com.zogamonline.laisiangthou.fragments.common.about.ThirdPartyLibFragment;

/**
 * Creator Maktaduai on 10-09-2015.
 */
@SuppressWarnings("UnnecessaryLocalVariable")
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsum) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsum;

    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            AboutFragment tab1 = new AboutFragment();
            return tab1;
        }
        if (position == 1) {
            ThirdPartyLibFragment tab2 = new ThirdPartyLibFragment();
            return tab2;
        }
        if (position == 2){
            ChangeLog tab3 = new ChangeLog();
            return tab3;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
