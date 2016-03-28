package com.zogamonline.laisiangthou.setting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.zogamonline.laisiangthou.MaterialPreferenceLib.PreferenceActivity;
import com.zogamonline.laisiangthou.MaterialPreferenceLib.custom_preferences.ListPreference;
import com.zogamonline.laisiangthou.R;

/**
 * Creator: Tuangoulal Phualte @Maktaduai on 10-08-2015.
 */
@SuppressWarnings("deprecation")
public class LSTPreferenceActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private Preference mPreferenceCallback;
    private Preference preference_font_face;
    //public static final String PREFS_THEME = "ThemePrefs";
    public static boolean sChanged = false;

    private static void restartActivity(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) activity.recreate();
        else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    activity.overridePendingTransition(0, 0);
                    activity.startActivity(activity.getIntent());
                }
            });
            activity.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ThemeUtils.onActivityCreateSetTheme(this);
        final String themPrefKey = getString(R.string.pref_theme), defaultTheme = getResources().getString(R.string.pref_theme_default);
        final String theme = PreferenceManager.getDefaultSharedPreferences(this).getString(themPrefKey, defaultTheme);
        switch (theme) {
            case "blue":
                setTheme(R.style.AppTheme_Blue);
                break;
            case "orange":
                setTheme(R.style.AppTheme_Orange);
                break;
            case "dark":
                setTheme(R.style.AppTheme_Dark);
                break;
            case "green":
                setTheme(R.style.AppTheme_Green);
                break;
            case "red":
                setTheme(R.style.AppTheme_Red);
                break;
            case "blue_grey":
                setTheme(R.style.AppTheme_BlueGrey);
                break;
            case "indigo":
                setTheme(R.style.AppTheme_Indigo);
                break;
        }
        sChanged = true;
        //checkPreferences();
        super.onCreate(savedInstanceState);
        ListPreference lisPreferences = (ListPreference) findPreference(getString(R.string.pref_theme));
        lisPreferences.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                restartActivity(LSTPreferenceActivity.this);
                return true;
            }
        });

        mPreferenceCallback = this.findPreference("preference_font_face");
        mPreferenceCallback.setOnPreferenceChangeListener(this);

        preference_font_face = this.findPreference("preference_font_face");
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /*private void checkPreferences() {
        SharedPreferences setting = getSharedPreferences(PREFS_THEME, 0);
        int theme = setting.getInt("theme", 0);
        ThemeUtils.setTheme(theme);
    }*/

    @Override
    protected int getPreferencesXmlId() {
        return R.xml.preferences;
    }

    @Override
    public boolean onPreferenceChange(final Preference preference, final Object newValue) {
        if (mPreferenceCallback == preference) {
            final String value = (String) newValue;
            Toast.makeText(this, "Na font teel " + value + " ahi", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (key.equals(preference_font_face.getKey())) {
            final String value = sharedPreferences.getString(key, "");
            preference_font_face.setSummary("Font thak " + value + " ahi");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}
