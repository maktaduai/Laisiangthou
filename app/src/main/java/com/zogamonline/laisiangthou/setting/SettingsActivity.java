package com.zogamonline.laisiangthou.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.zogamonline.laisiangthou.MaterialPreferenceLib.PreferenceActivity;
import com.zogamonline.laisiangthou.MaterialPreferenceLib.custom_preferences.ListPreference;
import com.zogamonline.laisiangthou.R;
import com.zogamonline.laisiangthou.activities.lst.Laisiangthou;

/**
 * Creator Maktaduai on 3/4/2016.
 */
@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener{

    private Preference mPreferenceCallback;
    private Preference preference_font_face;
    public static boolean sChanged = false;

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
        sChanged = true;
    }

    private static void restartActivity(final Activity activity) {
        activity.recreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListPreference lisPreferences = (ListPreference) findPreference(getString(R.string.pref_theme_key));
        lisPreferences.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                restartActivity(SettingsActivity.this);
                return true;
            }
        });

        mPreferenceCallback = this.findPreference("preference_font_face");
        mPreferenceCallback.setOnPreferenceChangeListener(this);

        preference_font_face = this.findPreference("preference_font_face");
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

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
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
