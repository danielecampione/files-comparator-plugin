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

package org.files_comparator.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;

import javax.swing.Action;

import org.files_comparator.ui.action.Actions;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorComponent extends Container {

    private static final long serialVersionUID = 3181215304744200750L;

    private FilesComparatorPanel filesComparatorPanel;

    public FilesComparatorComponent() {
        filesComparatorPanel = new FilesComparatorPanel();

        filesComparatorPanel.SHOW_TABBEDPANE_OPTION.disable();
        filesComparatorPanel.SHOW_TOOLBAR_OPTION.disable();
        filesComparatorPanel.SHOW_STATUSBAR_OPTION.disable();
        filesComparatorPanel.SHOW_FILE_TOOLBAR_OPTION.disable();
        filesComparatorPanel.SHOW_FILE_STATUSBAR_OPTION.disable();

        setLayout(new BorderLayout());
        add(filesComparatorPanel, BorderLayout.CENTER);
    }

    public void openComparison(File fileLeft, File fileRight) {
        filesComparatorPanel.openFileComparison(fileRight, fileRight, false);
    }

    public Actions getActions() {
        return filesComparatorPanel.actions;
    }

    public Action getAction(Actions.Action action) {
        return filesComparatorPanel.getAction(action);
    }
}