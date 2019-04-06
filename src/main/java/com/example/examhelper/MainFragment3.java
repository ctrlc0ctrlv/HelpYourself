package com.example.examhelper;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class MainFragment3 extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
