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

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ComboBoxSelectionPreference extends Preference {

    // Class variables:
    private static String SELECTED_ITEM = "SELECTED_ITEM";

    // Instance variables:
    private JComboBox target;
    @SuppressWarnings("unused")
    private int maxItems = 10;

    public ComboBoxSelectionPreference(String preferenceName, JComboBox target) {
        super("ComboBox-" + preferenceName);

        this.target = target;

        init();
    }

    private void init() {
        Object object;
        String selectedItem;
        int selectedIndex;

        selectedItem = getString(SELECTED_ITEM, null);

        target.getModel().addListDataListener(getListDataListener());

        if (target.getItemCount() > 0) {
            selectedIndex = 0;
            if (selectedItem != null) {
                for (int i = 0; i < target.getItemCount(); i++) {
                    object = target.getItemAt(i);
                    if (object == null) {
                        continue;
                    }

                    if (object.toString().equals(selectedItem)) {
                        selectedIndex = i;
                        break;
                    }
                }
            }

            target.setSelectedIndex(selectedIndex);
        }
    }

    private void save() {
        ComboBoxModel model;
        Object item;

        model = target.getModel();

        // Save the selectedItem
        item = model.getSelectedItem();
        if (item != null) {
            putString(SELECTED_ITEM, item.toString());
        }
    }

    private ListDataListener getListDataListener() {
        return new ListDataListener() {
            public void contentsChanged(ListDataEvent e) {
                save();
            }

            public void intervalAdded(ListDataEvent e) {
                save();
            }

            public void intervalRemoved(ListDataEvent e) {
                save();
            }
        };
    }
}
