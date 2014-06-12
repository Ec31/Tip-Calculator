package com.example.tipcalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
 
public class TipSettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.settings);
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        EditTextPreference tip1 = (EditTextPreference) findPreference("tip1");
        tip1
                .setSummary(sp.getString("tip1", ""));
        
        EditTextPreference tip2 = (EditTextPreference) findPreference("tip2");
        tip2
                .setSummary(sp.getString("tip2", ""));
        
        EditTextPreference tip3 = (EditTextPreference) findPreference("tip3");
        tip3
                .setSummary(sp.getString("tip3", ""));
    }

    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            EditTextPreference etp = (EditTextPreference) pref;
            pref.setSummary(etp.getText());
        }
    }
}