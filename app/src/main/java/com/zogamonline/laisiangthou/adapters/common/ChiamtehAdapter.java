package com.zogamonline.laisiangthou.adapters.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zogamonline.laisiangthou.fragments.esv.ESVBookmark;
import com.zogamonline.laisiangthou.fragments.lst.BuluiBookmarks;

/**
 * Creator: Maktaduai on 10-09-2015.
 */
@SuppressWarnings("UnnecessaryLocalVariable")
public class ChiamtehAdapter extends FragmentStatePagerAdapter {

    private final CharSequence[] Titles; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    final int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ChiamtehAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0) // if the position is 0 we are returning the First tab
        {
            BuluiBookmarks tab1 = new BuluiBookmarks();
            return tab1;
        }
        if (position == 1){
            ESVBookmark tab2 = new ESVBookmark();
            return tab2;
        }
        return null;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
