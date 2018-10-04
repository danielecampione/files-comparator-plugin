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

package org.files_comparator.ui.settings;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import net.sourceforge.open_teradata_viewer.UISupport;

import org.files_comparator.settings.FilesComparatorSettings;
import org.files_comparator.ui.FilesComparatorPanel;
import org.files_comparator.ui.SaveSettingsPanel;
import org.files_comparator.util.ObjectUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SaveSettingsDialog {

    // Instance variables:
    private FilesComparatorPanel filesComparatorPanel;
    private boolean ok;

    public SaveSettingsDialog(FilesComparatorPanel filesComparatorPanel) {
        this.filesComparatorPanel = filesComparatorPanel;
    }

    public void show() {
        JOptionPane pane;
        JDialog dialog;

        pane = new JOptionPane(getSaveSettings(), JOptionPane.WARNING_MESSAGE);
        pane.setOptionType(JOptionPane.YES_NO_OPTION);

        dialog = pane.createDialog(filesComparatorPanel, "Save settings");
        dialog.setResizable(true);
        try {
            UISupport.showDialog(dialog);

            if (ObjectUtil.equals(pane.getValue(), JOptionPane.YES_OPTION)) {
                ok = true;
            }
        } finally {
            // Don't allow memoryleaks!
            dialog.dispose();
        }
    }

    public boolean isOK() {
        return ok;
    }

    public void doSave() {
        FilesComparatorSettings.getInstance().save();
    }

    private JComponent getSaveSettings() {
        return new SaveSettingsPanel();
    }
}