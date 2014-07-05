/*
 * Copyright (C) 2012 The CyanogenMod Project (DvTonder)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.namelessrom.providers.preference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.namelessrom.providers.R;
import org.namelessrom.providers.misc.Constants;
import org.namelessrom.providers.misc.Preferences;
import org.namelessrom.providers.weather.WeatherUpdateService;

public class WeatherPreferences extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "WeatherPreferences";

    private CheckBoxPreference mUseCustomLoc;
    private EditTextPreference mCustomWeatherLoc;
    private CheckBoxPreference mUseMetric;

    private Context mContext;
    private Toast   mToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Constants.PREF_NAME);
        addPreferencesFromResource(R.xml.preferences_weather);
        setHasOptionsMenu(true);
        mContext = getActivity();

        // Load items that need custom summaries etc.
        mUseCustomLoc = (CheckBoxPreference) findPreference(Constants.WEATHER_USE_CUSTOM_LOCATION);
        mCustomWeatherLoc =
                (EditTextPreference) findPreference(Constants.WEATHER_CUSTOM_LOCATION_CITY);
        mUseMetric = (CheckBoxPreference) findPreference(Constants.WEATHER_USE_METRIC);

        // At first placement/start default the use of Metric units based on locale
        // If we had a previously set value already, this will just reset the same value
        Boolean defValue = Preferences.useMetricUnits(mContext);
        Preferences.setUseMetricUnits(mContext, defValue);
        mUseMetric.setChecked(defValue);

        if (canWeatherUpdate()) {
            updateWeather(true);
        } else {
            showDialog();
        }
    }

    private boolean canWeatherUpdate() {
        // Show a warning if location manager is disabled and there is no custom location set
        if (!mUseCustomLoc.isChecked()) {
            final LocationManager lm =
                    (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceManager()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        updateLocationSummary();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.weather, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                if (canWeatherUpdate()) {
                    updateWeather(true);
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(mContext, R.string.updating_weather,
                            Toast.LENGTH_SHORT);
                    mToast.show();
                } else {
                    showDialog();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (pref instanceof ListPreference) {
            pref.setSummary(((ListPreference) pref).getEntry());
        }

        boolean needWeatherUpdate = false;
        boolean forceWeatherUpdate = false;

        if (pref == mUseCustomLoc || pref == mCustomWeatherLoc) {
            updateLocationSummary();
        }

        if (pref == mUseMetric) {
            // The display format of the temperatures have changed
            // Force a weather update to refresh the display
            forceWeatherUpdate = true;
        }

        // If the weather source has changes, invalidate the custom location settings and change
        // back to GeoLocation to force the user to specify a new custom location if needed
        if (TextUtils.equals(key, Constants.WEATHER_SOURCE)) {
            Preferences.setCustomWeatherLocationId(mContext, null);
            Preferences.setCustomWeatherLocationCity(mContext, null);
            Preferences.setUseCustomWeatherLocation(mContext, false);
            mUseCustomLoc.setChecked(false);
            updateLocationSummary();
        }

        if (key.equals(Constants.WEATHER_USE_CUSTOM_LOCATION)
                || key.equals(Constants.WEATHER_CUSTOM_LOCATION_CITY)) {
            forceWeatherUpdate = true;
        }

        if (key.equals(Constants.WEATHER_REFRESH_INTERVAL)) {
            needWeatherUpdate = true;
        }

        if (Constants.DEBUG) {
            Log.v(TAG, "Preference " + key + " changed, need update " +
                    needWeatherUpdate + " force update " + forceWeatherUpdate);
        }

        if (needWeatherUpdate || forceWeatherUpdate) {
            updateWeather(forceWeatherUpdate);
        }

        //Intent updateIntent = new Intent(mContext, ClockWidgetProvider.class);
        //mContext.sendBroadcast(updateIntent);
    }

    //===============================================================================================
    // Utility classes and supporting methods
    //===============================================================================================

    private void updateWeather(final boolean forceWeatherUpdate) {
        final Intent updateIntent = new Intent(mContext, WeatherUpdateService.class);
        if (forceWeatherUpdate) {
            updateIntent.setAction(WeatherUpdateService.ACTION_FORCE_UPDATE);
        }
        mContext.startService(updateIntent);
    }

    private void updateLocationSummary() {
        if (mUseCustomLoc.isChecked()) {
            String location = Preferences.customWeatherLocationCity(mContext);
            if (location == null) {
                location = getResources().getString(R.string.unknown);
            }
            mCustomWeatherLoc.setSummary(location);
        } else {
            mCustomWeatherLoc.setSummary(R.string.weather_geolocated);
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final Dialog dialog;

        // Build and show the dialog
        builder.setTitle(R.string.weather_retrieve_location_dialog_title);
        builder.setMessage(R.string.weather_retrieve_location_dialog_message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.weather_retrieve_location_dialog_enable_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                }
        );
        builder.setNegativeButton(android.R.string.cancel, null);
        dialog = builder.create();
        dialog.show();
    }

}
