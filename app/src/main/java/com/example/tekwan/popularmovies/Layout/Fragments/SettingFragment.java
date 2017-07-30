package com.example.tekwan.popularmovies.Layout.Fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.tekwan.popularmovies.R;

/**
 * Created by tekwan on 7/30/2017.
 */

public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting);
    }
}
