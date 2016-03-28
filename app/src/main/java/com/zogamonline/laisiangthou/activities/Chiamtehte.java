package com.zogamonline.laisiangthou.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.zogamonline.laisiangthou.MaterialPreferenceLib.custom_preferences.SlidingTabLayout;
import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.adapters.common.ChiamtehAdapter;

/**
 * Author Maktaduai created on 24-10-2015.
 */
@SuppressWarnings("deprecation")
public class Chiamtehte extends BaseActivity{

    private final CharSequence[] Titles = {"Bului Chiamtehsate", "ESV Bookmarks"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        checkPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmarks);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        drawerToggle.setDrawerIndicatorEnabled(false);

        int numboftabs = 2;
        ChiamtehAdapter adapter = new ChiamtehAdapter(this.getSupportFragmentManager(), Titles, numboftabs);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
        navigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkPreferences() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (sharedPrefs.getBoolean("notifications_new_message", false)) {
            getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}
