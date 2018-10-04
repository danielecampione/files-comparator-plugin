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

package org.files_comparator.ui;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import open_teradata_viewer.plugin.FilesComparatorException;

import org.files_comparator.ui.text.BufferDocumentIF;
import org.files_comparator.util.ObjectUtil;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SavePanelDialog {

    // Instance variables:
    private FilesComparatorPanel filesComparatorPanel;
    private boolean ok;
    private List<BufferDocumentIF> documents;
    private JCheckBox[] checkBoxes;

    public SavePanelDialog(FilesComparatorPanel filesComparatorPanel) {
        this.filesComparatorPanel = filesComparatorPanel;

        documents = new ArrayList<BufferDocumentIF>();
    }

    public void add(BufferDocumentIF document) {
        documents.add(document);
    }

    @SuppressWarnings("deprecation")
    public void show() {
        JOptionPane pane;
        JDialog dialog;

        pane = new JOptionPane(getSavePanel(), JOptionPane.WARNING_MESSAGE);
        pane.setOptionType(JOptionPane.OK_CANCEL_OPTION);

        dialog = pane.createDialog(filesComparatorPanel, "Save files");
        dialog.setResizable(true);
        try {
            dialog.show();

            if (ObjectUtil.equals(pane.getValue(), JOptionPane.OK_OPTION)) {
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
        BufferDocumentIF document;

        if (checkBoxes == null) {
            return;
        }

        for (int i = 0; i < checkBoxes.length; i++) {
            if (!checkBoxes[i].isSelected()) {
                continue;
            }

            document = documents.get(i);
            if (document == null) {
                continue;
            }

            try {
                document.write();
            } catch (FilesComparatorException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(filesComparatorPanel,
                        "Can't write file" + document.getName(),
                        "Problem writing file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JComponent getSavePanel() {
        JPanel panel;
        String columns;
        String rows;
        FormLayout layout;
        CellConstraints cc;
        JLabel label;
        JCheckBox checkBox;
        BufferDocumentIF document;
        Font font;

        columns = "10px, fill:pref, 10px";
        rows = "10px, fill:pref, 5px, fill:pref, 10px,";
        for (int i = 0; i < documents.size(); i++) {
            rows += " fill:pref, ";
        }

        rows += " 10px";

        layout = new FormLayout(columns, rows);
        cc = new CellConstraints();

        panel = new JPanel(layout);
        label = new JLabel("Some files have been changed");
        font = label.getFont().deriveFont(Font.BOLD);
        label.setFont(font);
        label.setHorizontalAlignment(JLabel.LEFT);
        panel.add(label, cc.xy(2, 2));
        label = new JLabel("Which ones would you like to save?");
        label.setFont(font);
        label.setHorizontalAlignment(JLabel.LEFT);
        panel.add(label, cc.xy(2, 4));

        checkBoxes = new JCheckBox[documents.size()];
        for (int i = 0; i < documents.size(); i++) {
            document = documents.get(i);
            if (document == null) {
                continue;
            }

            checkBox = new JCheckBox(document.getName());
            checkBoxes[i] = checkBox;
            if (!document.isChanged()) {
                checkBox.setEnabled(false);
            } else {
                checkBox.setSelected(true);
            }

            panel.add(checkBox, cc.xy(2, 6 + i));
        }

        return panel;
    }
}
