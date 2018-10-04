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

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ComboBoxPreference extends Preference {

    // Class variables:
    private static String ITEMS = "ITEMS";

    // Instance variables:
    private JComboBox<String> target;
    private int maxItems = 10;

    public ComboBoxPreference(String preferenceName, JComboBox<String> target) {
        super("ComboBox-" + preferenceName);

        this.target = target;

        init();
    }

    private void init() {
        DefaultComboBoxModel<String> model;

        model = new DefaultComboBoxModel<String>();
        for (String item : getListOfString(ITEMS, maxItems)) {
            model.addElement(item);
        }

        target.setModel(model);
        model.addListDataListener(getListDataListener());
        if (target.getItemCount() > 0) {
            target.setSelectedIndex(0);
        }
    }

    private void save() {
        List<String> list;
        ComboBoxModel<String> model;
        String item;

        list = new ArrayList<String>();

        model = target.getModel();

        // Put the selectedItem on top.
        item = (String) model.getSelectedItem();
        if (item != null) {
            list.add(item);
        }

        for (int i = 0; i < model.getSize(); i++) {
            item = (String) model.getElementAt(i);

            // Don't save items twice.
            if (list.contains(item)) {
                continue;
            }

            list.add(item);
        }

        putListOfString(ITEMS, maxItems, list);
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