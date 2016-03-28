package com.zogamonline.laisiangthou.fragments.common.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.zogamonline.laisiangthou.MaterialPreferenceLib.custom_preferences.SlidingTabLayout;
import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.activities.BaseActivity;
import com.zogamonline.laisiangthou.activities.EmailActivity;
import com.zogamonline.laisiangthou.adapters.common.ViewPagerAdapter;

/**
 * Author Maktaduai on 10-09-2015.
 */
@SuppressWarnings("deprecation")
public class About extends BaseActivity {

    private final CharSequence[] Titles = {"About", "Libs", "Changelog"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        int numboftabs = 3;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, numboftabs);

        ViewPager pager = (ViewPager) findViewById(R.id.about_pager);
        pager.setAdapter(adapter);

        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.about_tab);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_rating:
                initRating();
                return true;
            case R.id.action_email:
                startActivity(new Intent(this, EmailActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRating() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }
}
