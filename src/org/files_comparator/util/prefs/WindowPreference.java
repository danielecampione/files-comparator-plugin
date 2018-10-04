/*
 * Open Teradata Viewer ( files comparator plugin )
 * Copyright (C) 2011, D. Campione
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.files_comparator.util.prefs;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class WindowPreference extends Preference {

    // Class variables:
    private static String X = "X";
    private static String Y = "Y";
    private static String WIDTH = "WIDTH";
    private static String HEIGHT = "HEIGHT";

    // Instance variables:
    private Window target;

    public WindowPreference(String preferenceName, Window target) {
        super("Window-" + preferenceName);

        this.target = target;
        init();
    }

    private void init() {
        target.setLocation(getInt(X, 0), getInt(Y, 0));
        target.setSize(getInt(WIDTH, 500), getInt(HEIGHT, 400));

        target.addWindowListener(getWindowListener());
    }

    private void save() {
        putInt(X, target.getLocation().x);
        putInt(Y, target.getLocation().y);
        putInt(WIDTH, target.getSize().width);
        putInt(HEIGHT, target.getSize().height);
    }

    private WindowListener getWindowListener() {
        return new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                save();
            }
        };
    }
}
