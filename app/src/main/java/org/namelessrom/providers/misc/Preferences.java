/*
 * Copyright (C) 2012 The CyanogenMod Project
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

package org.namelessrom.providers.misc;

import android.content.Context;
import android.content.SharedPreferences;

import org.namelessrom.providers.weather.OpenWeatherMapProvider;
import org.namelessrom.providers.weather.WeatherInfo;
import org.namelessrom.providers.weather.WeatherProvider;
import org.namelessrom.providers.weather.YahooWeatherProvider;

import java.util.Locale;

public class Preferences {
    private Preferences() {
    }

    public static boolean useMetricUnits(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        boolean defValue = !(locale.equals(Locale.US)
                || locale.toString().equals("ms_MY") // Malaysia
                || locale.toString().equals("si_LK") // Sri Lanka
        );
        return getPrefs(context).getBoolean(Constants.WEATHER_USE_METRIC, defValue);
    }

    public static void setUseMetricUnits(Context context, boolean value) {
        getPrefs(context).edit().putBoolean(Constants.WEATHER_USE_METRIC, value).apply();
    }

    public static long weatherRefreshIntervalInMs(Context context) {
        String value = getPrefs(context).getString(Constants.WEATHER_REFRESH_INTERVAL, "60");
        return Long.parseLong(value) * 60 * 1000;
    }

    public static boolean useCustomWeatherLocation(Context context) {
        return getPrefs(context).getBoolean(Constants.WEATHER_USE_CUSTOM_LOCATION, false);
    }

    public static void setUseCustomWeatherLocation(Context context, boolean value) {
        getPrefs(context).edit().putBoolean(Constants.WEATHER_USE_CUSTOM_LOCATION, value).apply();
    }

    public static String customWeatherLocationId(Context context) {
        return getPrefs(context).getString(Constants.WEATHER_CUSTOM_LOCATION_ID, null);
    }

    public static void setCustomWeatherLocationId(Context context, String id) {
        getPrefs(context).edit().putString(Constants.WEATHER_CUSTOM_LOCATION_ID, id).apply();
    }

    public static String customWeatherLocationCity(Context context) {
        return getPrefs(context).getString(Constants.WEATHER_CUSTOM_LOCATION_CITY, null);
    }

    public static void setCustomWeatherLocationCity(Context context, String city) {
        getPrefs(context).edit().putString(Constants.WEATHER_CUSTOM_LOCATION_CITY, city).apply();
    }

    public static WeatherProvider weatherProvider(Context context) {
        String name = getPrefs(context).getString(Constants.WEATHER_SOURCE, "yahoo");
        if (name.equals("openweathermap")) {
            return new OpenWeatherMapProvider(context);
        }
        return new YahooWeatherProvider(context);
    }

    public static void setCachedWeatherInfo(Context context, long timestamp, WeatherInfo data) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putLong(Constants.WEATHER_LAST_UPDATE, timestamp);
        if (data != null) {
            // We now have valid weather data to display
            editor.putBoolean(Constants.WEATHER_FIRST_UPDATE, false);
            editor.putString(Constants.WEATHER_DATA, data.toSerializedString());
        }
        editor.apply();
    }

    public static long lastWeatherUpdateTimestamp(Context context) {
        return getPrefs(context).getLong(Constants.WEATHER_LAST_UPDATE, 0);
    }

    public static WeatherInfo getCachedWeatherInfo(Context context) {
        return WeatherInfo.fromSerializedString(context,
                getPrefs(context).getString(Constants.WEATHER_DATA, null));
    }

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }
}
