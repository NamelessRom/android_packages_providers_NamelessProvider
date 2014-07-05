/*
 * Copyright (C) 2012 The Android Open Source Project
 * Portions Copyright (C) 2012 The CyanogenMod Project
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class WidgetUtils {
    //===============================================================================================
    // Widget display and resizing related functionality
    //===============================================================================================

    private static final String  TAG = "WidgetUtils";
    private static final boolean D   = Constants.DEBUG;

    /**
     * Networking available check
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected() || !info.isAvailable()) {
            if (D) Log.d(TAG, "No network connection is available for weather update");
            return false;
        }
        return true;
    }
}
