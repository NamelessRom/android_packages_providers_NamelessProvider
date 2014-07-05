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

package org.namelessrom.providers.misc;

public class Constants {
    public static final boolean DEBUG = false;

    public static final String PREF_NAME = "Providers";

    public static final String WEATHER_SOURCE               = "weather_source";
    public static final String WEATHER_USE_CUSTOM_LOCATION  = "weather_use_custom_location";
    public static final String WEATHER_CUSTOM_LOCATION_ID   = "weather_custom_location_id";
    public static final String WEATHER_CUSTOM_LOCATION_CITY = "weather_custom_location_city";
    public static final String WEATHER_USE_METRIC           = "weather_use_metric";
    public static final String WEATHER_REFRESH_INTERVAL     = "weather_refresh_interval";

    // other shared pref entries
    public static final String WEATHER_LAST_UPDATE = "last_weather_update";
    public static final String WEATHER_DATA        = "weather_data";

    // First run is used to hide the initial no-weather message for a better OOBE
    public static final String WEATHER_FIRST_UPDATE = "weather_first_update";

}
