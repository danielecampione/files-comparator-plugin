/*
 * Open Teradata Viewer ( files comparator plugin )
 * Copyright (C) 2014, D. Campione
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

package org.files_comparator.ui.util;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.files_comparator.ui.action.FilesComparatorAction;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SwingUtil {

    private SwingUtil() {
    }

    public static void installKey(JComponent component, String key,
            FilesComparatorAction action) {
        InputMap inputMap;
        ActionMap actionMap;
        KeyStroke stroke;

        stroke = KeyStroke.getKeyStroke(key);

        inputMap = component
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        if (inputMap.get(stroke) != action.getName()) {
            inputMap.put(stroke, action.getName());
        }

        actionMap = component.getActionMap();
        if (actionMap.get(action.getName()) != action) {
            actionMap.put(action.getName(), action);
        }
    }

    public static void deInstallKey(JComponent component, String key,
            FilesComparatorAction action) {
        InputMap inputMap;
        KeyStroke stroke;

        stroke = KeyStroke.getKeyStroke(key);
        inputMap = component
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.remove(stroke);

        // Do not deinstall the action because I don't know how many other
        // inputmap residents will call the action.
    }
}