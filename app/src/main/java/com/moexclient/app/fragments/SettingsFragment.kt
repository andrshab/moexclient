package com.moexclient.app.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.moexclient.app.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}