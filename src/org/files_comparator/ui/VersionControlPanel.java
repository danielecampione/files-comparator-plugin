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

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.files_comparator.ui.swing.table.FilesComparatorTreeTableModel;
import org.files_comparator.util.conf.ConfigurationListenerIF;
import org.files_comparator.util.file.FolderDiff;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class VersionControlPanel extends AbstractContentPanel
        implements
            ConfigurationListenerIF {

    private static final long serialVersionUID = -3915467836601654358L;

    private FilesComparatorPanel mainPanel;
    private FolderDiff diff;

    private JPanel bufferDiffPanelHolder;

    VersionControlPanel(FilesComparatorPanel mainPanel, FolderDiff diff) {
        this.mainPanel = mainPanel;
        this.diff = diff;

        init();
    }

    private void init() {
        FolderDiffPanel folderDiffPanel;
        JSplitPane splitPane;

        folderDiffPanel = new FolderDiffPanel(mainPanel, diff) {

            private static final long serialVersionUID = -3356920489758416353L;

            @Override
            protected FilesComparatorTreeTableModel createTreeTableModel() {
                return new VersionControlTreeTableModel();
            }
        };
        bufferDiffPanelHolder = new JPanel();
        bufferDiffPanelHolder.setLayout(new BorderLayout());

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, folderDiffPanel,
                bufferDiffPanelHolder);

        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, splitPane);
    }

    public void configurationChanged() {
    }
}