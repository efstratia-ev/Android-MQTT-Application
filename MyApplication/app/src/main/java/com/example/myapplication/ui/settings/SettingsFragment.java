package com.example.myapplication.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.example.myapplication.MQTTConnection.MQTTInfo;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.util.Objects;
import java.util.regex.Pattern;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load the preferences from your XML resource (which I assume you already do anyway)
        addPreferencesFromResource(R.xml.settings);
        Preference preference = findPreference("IP");
        assert preference != null;
        ((EditTextPreference)preference).setText(MQTTInfo.getIP());
        preference = findPreference("Port");
        assert preference != null;
        ((EditTextPreference)preference).setText(String.valueOf(MQTTInfo.getPort()));
        preference = findPreference("Measurements");
        assert preference != null;
        ((EditTextPreference)preference).setText(String.valueOf(MainActivity.measurementsSend));
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
            String val= preference != null ? ((EditTextPreference) preference).getText() : null;
            if(validate(val)) {
                MQTTInfo.setIP(val);
                MainActivity.restart = true;
            }
            else{
                ((EditTextPreference)preference).setText(MQTTInfo.getIP());
                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), val+" is not a valid IP address", Toast.LENGTH_LONG).show();
            }
        }
        if (key.equals("Port")) {
            Preference preference = findPreference(key);
            MQTTInfo.setPort(Integer.parseInt(preference != null ? ((EditTextPreference) preference).getText() : null));
            MainActivity.restart=true;
        }
        if (key.equals("Measurements")) {
            Preference preference = findPreference(key);
            int val = Integer.parseInt(preference != null ? ((EditTextPreference) preference).getText() : null);
            if (val<1 || val>MainActivity.max) {
                // invalid you can show invalid message
                if (preference != null) {
                    ((EditTextPreference)preference).setText(String.valueOf(MainActivity.measurementsSend));
                }
                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Value mast be in range [0,"+MainActivity.max+"]", Toast.LENGTH_LONG).show();
            }
            else MainActivity.measurementsSend=val;
        }
    }

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
    }
}
