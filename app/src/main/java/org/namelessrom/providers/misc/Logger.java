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
package org.namelessrom.providers.misc;

import android.util.Log;

/**
 * Utility class for logging
 */
public class Logger {

    private static final boolean DEBUG = false;

    public static void d(final Object tag, final String msg) {
        if (DEBUG) Log.d(getTag(tag), String.format("--> %s", msg));
    }

    public static void v(final Object tag, final String msg) {
        if (DEBUG) Log.v(getTag(tag), String.format("--> %s", msg));
    }

    private static String getTag(final Object tag) {
        if (tag == null) return "Logger";

        if (tag instanceof String) {
            return (String) tag;
        }

        return tag.getClass().getSimpleName();
    }

}
