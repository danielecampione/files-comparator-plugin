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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.files_comparator.ui.util.ImageUtil;
import org.files_comparator.ui.util.ToolBarBuilder;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilePanelBar extends JPanel {

    private static final long serialVersionUID = 735149620849001953L;

    private FilePanel filePanel;
    private JLabel selected;
    private JLabel lineNumber;
    private JLabel columnNumber;
    private ImageIcon iconSelected;
    private ImageIcon iconNotSelected;

    public FilePanelBar(FilePanel filePanel) {
        this.filePanel = filePanel;

        init();
    }

    private void init() {
        ToolBarBuilder builder;

        selected = new JLabel();
        lineNumber = new JLabel();
        columnNumber = new JLabel();

        builder = new ToolBarBuilder(this);
        builder.addComponent(selected);
        builder.addSpring();
        builder.addComponent(lineNumber);
        builder.addSeparator();
        builder.addComponent(columnNumber);

        iconSelected = ImageUtil.getImageIcon("panel-selected");
        iconNotSelected = ImageUtil.createTransparentIcon(iconSelected);

        update();
    }

    public void update() {
        Icon icon;
        JTextArea editor;
        int caretPosition;
        String text;
        int line;
        int column;

        icon = filePanel.isSelected() ? iconSelected : iconNotSelected;
        if (selected.getIcon() != icon) {
            selected.setIcon(icon);
        }

        editor = filePanel.getEditor();
        caretPosition = editor.getCaretPosition();
        try {
            line = editor.getLineOfOffset(caretPosition);
        } catch (Exception ex) {
            line = -1;
        }

        try {
            column = caretPosition - editor.getLineStartOffset(line);
        } catch (Exception ex) {
            column = -1;
        }

        text = String
                .format("Line: %05d/%05d", line + 1, editor.getLineCount());
        lineNumber.setText(text);

        text = String.format("Column: %03d", column);
        columnNumber.setText(text);
    }
}
