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

package org.files_comparator.ui.settings;

import javax.swing.JPanel;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public enum Settings {

    Editor("Editor", "stock_edit", new EditorSettingsPanel()), Filter("Filter",
            "stock_standard-filter", new FilterSettingsPanel()), Folder(
            "Folder", "stock_folder", new FolderSettingsPanel());

    // Instance variables:
    private String name;
    private String iconName;
    private JPanel panel;

    Settings(String name, String iconName, JPanel panel) {
        this.name = name;
        this.iconName = iconName;
        this.panel = panel;
    }

    String getName() {
        return name;
    }

    String getIconName() {
        return iconName;
    }

    JPanel getPanel() {
        return panel;
    }

    public String toString() {
        return name;
    }
}
