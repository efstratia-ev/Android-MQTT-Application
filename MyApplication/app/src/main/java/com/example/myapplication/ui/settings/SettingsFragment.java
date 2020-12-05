package com.example.myapplication.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import com.example.myapplication.MQTTConnection.MQTTInfo;
import com.example.myapplication.MQTTConnection.MQTTPublisher;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Map;
import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load the preferences from your XML resource (which I assume you already do anyway)
        addPreferencesFromResource(R.xml.settings);
        Preference preference = findPreference("IP");
        assert preference != null;
        ((EditTextPreference)preference).setText(MQTTInfo.getIP());
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("IP")) {
            Preference preference = findPreference(key);
            assert preference != null;
            MQTTInfo.setIP(((EditTextPreference)preference).getText());
            MainActivity.restart=true;
        }
    }
}
