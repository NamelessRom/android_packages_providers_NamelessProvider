/*
* <!--
*    Copyright (C) 2014 The NamelessROM Project
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
* -->
*/
package org.namelessrom.providers.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.namelessrom.providers.misc.Logger;
import org.namelessrom.providers.weather.WeatherUpdateService;

public class WeatherReceiver extends BroadcastReceiver {

    @Override public void onReceive(final Context context, final Intent intent) {
        if (intent == null) {
            return;
        }

        final String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }

        Logger.d(this, String.format("received action: %s", action));

        if (TextUtils.equals(WeatherUpdateService.ACTION_FORCE_UPDATE, action)
                || TextUtils.equals(WeatherUpdateService.ACTION_REQUEST_UPDATE, action)) {
            final Intent i = new Intent(context, WeatherUpdateService.class);
            i.setAction(action);
            context.startService(i);
        }
    }

}
