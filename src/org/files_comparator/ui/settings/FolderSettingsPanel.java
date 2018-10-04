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

import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;

import org.files_comparator.settings.FilesComparatorSettings;
import org.files_comparator.settings.FolderSettings;
import org.files_comparator.ui.util.ImageUtil;
import org.files_comparator.util.conf.ConfigurationListenerIF;

/**
 *
 *
 * @author D. Campione
 * 
 */
public class FolderSettingsPanel extends FolderSettingsForm
        implements
            ConfigurationListenerIF {

    private static final long serialVersionUID = 1163398071113229679L;

    public FolderSettingsPanel() {
        init();

        FilesComparatorSettings.getInstance().addConfigurationListener(this);
    }

    private void init() {
        FolderSettings settings;

        settings = getSettings();

        hierarchyComboBox.setModel(new DefaultComboBoxModel(
                FolderSettings.FolderView.values()));
        hierarchyComboBox.setSelectedItem(getSettings().getView());
        hierarchyComboBox.setFocusable(false);
        hierarchyComboBox.addActionListener(getHierarchyAction());

        onlyLeftButton.setText(null);
        onlyLeftButton.setIcon(ImageUtil
                .getImageIcon("files-comparator_only-left"));
        onlyLeftButton.setFocusable(false);
        onlyLeftButton.setSelected(settings.getOnlyLeft());
        onlyLeftButton.addActionListener(getOnlyLeftAction());

        leftRightChangedButton.setText(null);
        leftRightChangedButton.setIcon(ImageUtil
                .getImageIcon("files-comparator_left-right-changed"));
        leftRightChangedButton.setFocusable(false);
        leftRightChangedButton.setSelected(settings.getLeftRightChanged());
        leftRightChangedButton.addActionListener(getLeftRightChangedAction());

        onlyRightButton.setText(null);
        onlyRightButton.setIcon(ImageUtil
                .getImageIcon("files-comparator_only-right"));
        onlyRightButton.setFocusable(false);
        onlyRightButton.setSelected(settings.getOnlyRight());
        onlyRightButton.addActionListener(getOnlyRightAction());

        leftRightUnChangedButton.setText(null);
        leftRightUnChangedButton.setIcon(ImageUtil
                .getImageIcon("files-comparator_left-right-unchanged"));
        leftRightUnChangedButton.setFocusable(false);
        leftRightUnChangedButton.setSelected(settings.getLeftRightUnChanged());
        leftRightUnChangedButton
                .addActionListener(getLeftRightUnChangedAction());
    }

    private ActionListener getHierarchyAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getSettings().setView(
                        (FolderSettings.FolderView) hierarchyComboBox
                                .getSelectedItem());
            }
        };
    }

    private ActionListener getOnlyLeftAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getSettings().setOnlyLeft(onlyLeftButton.isSelected());
            }
        };
    }

    private ActionListener getLeftRightChangedAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getSettings().setLeftRightChanged(
                        leftRightChangedButton.isSelected());
            }
        };
    }

    private ActionListener getOnlyRightAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getSettings().setOnlyRight(onlyRightButton.isSelected());
            }
        };
    }

    private ActionListener getLeftRightUnChangedAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getSettings().setLeftRightUnChanged(
                        leftRightUnChangedButton.isSelected());
            }
        };
    }

    public void configurationChanged() {
        //initConfiguration();
    }

    private FolderSettings getSettings() {
        return FilesComparatorSettings.getInstance().getFolder();
    }
}
