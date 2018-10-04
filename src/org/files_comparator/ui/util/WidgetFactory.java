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

import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;

import org.files_comparator.settings.EditorSettings;
import org.files_comparator.settings.FilesComparatorSettings;
import org.files_comparator.ui.action.FilesComparatorAction;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class WidgetFactory {

    public static JMenuItem getMenuItem(Action action) {
        JMenuItem item;
        ImageIcon icon;

        item = new JMenuItem(action);

        icon = (ImageIcon) action.getValue(FilesComparatorAction.SMALL_ICON);
        if (icon != null) {
            item.setDisabledIcon(ImageUtil.createTransparentIcon(icon));
        }

        return item;
    }

    public static JButton getToolBarButton(Action action) {
        JButton button;
        ImageIcon icon;
        Dimension size;
        EditorSettings settings;
        EditorSettings.ToolbarButtonIcon toolbarButtonIcon;

        settings = FilesComparatorSettings.getInstance().getEditor();

        button = new JButton(action);
        button.setVerticalTextPosition(AbstractButton.BOTTOM);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setFocusable(false);

        toolbarButtonIcon = settings.getToolbarButtonIcon();

        icon = null;
        if (toolbarButtonIcon == EditorSettings.ToolbarButtonIcon.SMALL) {
            icon = (ImageIcon) action
                    .getValue(FilesComparatorAction.SMALL_ICON);
        } else if (toolbarButtonIcon == EditorSettings.ToolbarButtonIcon.LARGE) {
            icon = (ImageIcon) action
                    .getValue(FilesComparatorAction.LARGE_ICON_KEY);
        }

        button.setIcon(icon);
        button.setDisabledIcon(ImageUtil.createTransparentIcon(icon));

        if (!settings.isToolbarButtonTextEnabled()) {
            button.setText("");
        }

        size = button.getPreferredSize();
        if (size.height > size.width) {
            size.width = size.height;
        }

        button.setPreferredSize(size);

        return button;
    }
}
