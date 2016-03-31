package com.zogamonline.laisiangthou.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.activities.esv.Bible;
import com.zogamonline.laisiangthou.activities.lst.Laisiangthou;
import com.zogamonline.laisiangthou.fragments.common.about.About;
import com.zogamonline.laisiangthou.notepad.NoteActivity;
import com.zogamonline.laisiangthou.setting.SettingsActivity;

//This code is created by Tuangoulal Phualte @Maktaduai

@SuppressWarnings({"deprecation", "ConstantConditions"})
public abstract class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    protected NavigationView navigationView;
    private DrawerLayout fullLayout;
    private Toolbar toolbar;
    protected ActionBarDrawerToggle drawerToggle;
    private static final long DRAWER_SLIDE_DURATION_MS = 250;

    @Override
    public void setTheme(int resid) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String key = getString(R.string.pref_theme_key);
        String defaultValue = getString(R.string.theme_list_default);
        int theme = Integer.valueOf(sharedPreferences.getString(key, defaultValue));

        switch (theme){
            case 0:
                super.setTheme(R.style.AppTheme_Blue);
                break;
            case 1:
                super.setTheme(R.style.AppTheme_Orange);
                break;
            case 2:
                super.setTheme(R.style.AppTheme_Dark);
                break;
            case 3:
                super.setTheme(R.style.AppTheme_Green);
                break;
            case 4:
                super.setTheme(R.style.AppTheme_Red);
                break;
            case 5:
                super.setTheme(R.style.AppTheme_BlueGrey);
                break;
            case 6:
                super.setTheme(R.style.AppTheme_Indigo);
                break;
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        checkPreferences();
        //ThemeUtils.onActivityCreateSetTheme(this);
        //setLstTheme();

        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_home, null);

        FrameLayout activityContainer = (FrameLayout) fullLayout.findViewById(R.id.container_body);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        super.setContentView(fullLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        setSupportActionBar(toolbar);
        setUpNavView();
    }

    private boolean useToolbar() {
        return true;
    }

    @SuppressLint("PrivateResource")
    protected void setUpNavView() {
        navigationView.setNavigationItemSelectedListener(this);

        if (useDrawerToggle()) { // use the hamburger menu
            drawerToggle = new ActionBarDrawerToggle(this, fullLayout, toolbar,
                    R.string.drawer_open,
                    R.string.drawer_close);

            fullLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
        } else if (useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources()
                    .getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        }
    }

    private boolean useDrawerToggle() {
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {

        fullLayout.closeDrawer(GravityCompat.START);
        //selectedNavItemId = menuItem.getItemId();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //itemSelection(selectedNavItemId);
                onOptionsItemSelected(menuItem);
            }
        }, DRAWER_SLIDE_DURATION_MS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.laisiangthou_bului:
                startActivity(new Intent(this, Laisiangthou.class));
                return true;
            case R.id.bible_esv:
                startActivity(new Intent(this, Bible.class));
                return true;
            case R.id.chiamtehte:
                startActivity(new Intent(this, Chiamtehte.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                return true;
            case R.id.notepad:
                startActivity(new Intent(this, NoteActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                return true;

            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                return true;

            case R.id.restart_home:
                restartActivity(this);
                overridePendingTransition(0, 0);
                return true;

            case R.id.nav_about:
                startActivity(new Intent(this, About.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                return true;

            case R.id.nav_rss:
                startActivity(new Intent(this, Commentary.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*private void themeDialog() {
        String[] themeArray = new String[]{"Blue", "Orange", "Dark", "Green", "Red", "Blue Grey", "Indigo"};
        Integer[] mImgid = {R.drawable.blue, R.drawable.orange, R.drawable.dark, R.drawable.green,
                R.drawable.red, R.drawable.bluegrey, R.drawable.indigo};

        @SuppressLint("InflateParams") View themeView = LayoutInflater.from(BaseActivity.this)
                .inflate(R.layout.theme_dialog, null);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
        dialog.setView(themeView);
        @SuppressLint("InflateParams") View title = getLayoutInflater().inflate(R.layout.dialog_title, null);
        TextView dialog_title = (TextView) title.findViewById(R.id.dialog_title);
        dialog_title.setText(R.string.theme_teelna);
        dialog_title.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.theme, 0, 0);
        dialog.setCustomTitle(title);
        ListView listView = (ListView) themeView
                .findViewById(R.id.themeList);
        ThemeAdapter themeAdapter = new ThemeAdapter(BaseActivity.this, themeArray, mImgid);
        listView.setAdapter(themeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                SharedPreferences settings = BaseActivity.this.getSharedPreferences(PREFS_THEME, 0);
                SharedPreferences.Editor editor = settings.edit();

                switch (position) {
                    case 0:
                        ThemeUtils.changeToTheme(BaseActivity.this, ThemeUtils.BLUE);
                        editor.putInt("theme", ThemeUtils.BLUE);
                        editor.apply();
                        break;
                    case 1:
                        ThemeUtils.changeToTheme(BaseActivity.this, ThemeUtils.ORANGE);
                        editor.putInt("theme", ThemeUtils.ORANGE);
                        editor.apply();
                        break;
                    case 2:
                        ThemeUtils.changeToTheme(BaseActivity.this, ThemeUtils.DARK);
                        editor.putInt("theme", ThemeUtils.DARK);
                        editor.apply();
                        break;
                    case 3:
                        ThemeUtils.changeToTheme(BaseActivity.this, ThemeUtils.GREEN);
                        editor.putInt("theme", ThemeUtils.GREEN);
                        editor.apply();
                        break;
                    case 4:
                        ThemeUtils.changeToTheme(BaseActivity.this, ThemeUtils.RED);
                        editor.putInt("theme", ThemeUtils.RED);
                        editor.apply();
                        break;
                    case 5:
                        ThemeUtils.changeToTheme(BaseActivity.this, ThemeUtils.BLUEGREY);
                        editor.putInt("theme", ThemeUtils.BLUEGREY);
                        editor.apply();
                        break;
                    case 6:
                        ThemeUtils.changeToTheme(BaseActivity.this, ThemeUtils.INDIGO);
                        editor.putInt("theme", ThemeUtils.INDIGO);
                        editor.apply();
                        break;
                }
                dialog.setCancelable(true);
            }
        });
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.setCancelable(true);
            }
        });
        dialog.show();
    }*/

    private void checkPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("notifications_new_message", false)) {
            getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SettingsActivity.sChanged) {
            BaseActivity.this.recreate();
            overridePendingTransition(0, 0);
            SettingsActivity.sChanged = false;
        }
        checkPreferences();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private static void restartActivity(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) activity.recreate();
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    activity.overridePendingTransition(0, 0);
                    activity.startActivity(activity.getIntent());
                    activity.overridePendingTransition(0, 0);
                }
            });
        }
    }
 }
