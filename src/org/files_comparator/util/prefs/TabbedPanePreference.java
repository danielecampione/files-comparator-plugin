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

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.files_comparator.util.StringUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TabbedPanePreference extends Preference {

    // Class variables:
    private static String TITLE = "TITLE";

    // Instance variables:
    private JTabbedPane target;

    public TabbedPanePreference(String preferenceName, JTabbedPane target) {
        super("TabbedPane-" + preferenceName);

        this.target = target;

        init();
    }

    private void init() {
        String title;

        title = getString(TITLE, "");

        if (!StringUtil.isEmpty(title)) {
            for (int index = 0; index < target.getTabCount(); index++) {
                if (title.equals(target.getTitleAt(index))) {
                    target.setSelectedIndex(index);
                    break;
                }
            }
        }

        target.getModel().addChangeListener(getChangeListener());
    }

    private void save() {
        int index;
        String title;

        index = target.getSelectedIndex();
        title = index == -1 ? null : target.getTitleAt(index);
        putString(TITLE, title);
    }

    private ChangeListener getChangeListener() {
        return new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                save();
            }
        };
    }
}
