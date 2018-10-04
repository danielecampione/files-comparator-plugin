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

package org.files_comparator.ui.bar;

import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.files_comparator.ui.AbstractBarDialog;
import org.files_comparator.ui.FilesComparatorPanel;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class LineNumberBarDialog extends AbstractBarDialog {

    private static final long serialVersionUID = 6252598727063500080L;

    // Instance variables:
    private JTextField lineNumberField;

    public LineNumberBarDialog(FilesComparatorPanel filesComparatorPanel) {
        super(filesComparatorPanel);
    }

    protected void init() {
        setLayout(new FlowLayout(FlowLayout.LEADING));

        // Incremental search:
        lineNumberField = new JTextField(15);
        lineNumberField.addKeyListener(getSearchKeyAction());

        add(Box.createHorizontalStrut(5));
        add(new JLabel("Linenumber:"));
        add(lineNumberField);
    }

    public void _activate() {
        lineNumberField.setText("");
        lineNumberField.requestFocus();
    }

    public void _deactivate() {
    }

    private KeyListener getSearchKeyAction() {
        return new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    getFilesComparatorPanel().doGoToLine(
                            Integer.valueOf(lineNumberField.getText()));
                }
            }
        };
    }
}
