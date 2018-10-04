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

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.files_comparator.settings.FilesComparatorSettings;
import org.files_comparator.ui.FilesComparatorPanel;
import org.files_comparator.ui.StatusBar;
import org.files_comparator.ui.util.ImageUtil;
import org.files_comparator.util.conf.ConfigurationListenerIF;
import org.files_comparator.util.conf.ConfigurationManager;
import org.files_comparator.util.prefs.FileChooserPreference;

/**
 *
 *
 * @author D. Campione
 * 
 */
public class SettingsPanel extends SettingsPanelForm
        implements
            ConfigurationListenerIF {

    private static final long serialVersionUID = -6121287484337988862L;

    private DefaultListModel<Settings> listModel;
    private FilesComparatorPanel mainPanel;

    public SettingsPanel(FilesComparatorPanel mainPanel) {
        this.mainPanel = mainPanel;

        init();
        initConfiguration();

        getConfiguration().addConfigurationListener(this);
    }

    private void init() {
        settingsPanel.setLayout(new CardLayout());
        for (Settings setting : Settings.values()) {
            settingsPanel.add(setting.getPanel(), setting.getName());
        }

        initButton(saveButton, "stock_save", "Save settings");
        saveButton.addActionListener(getSaveAction());

        initButton(saveAsButton, "stock_save-as",
                "Save settings to a different file");
        saveAsButton.addActionListener(getSaveAsAction());

        initButton(reloadButton, "stock_reload",
                "Reload settings from a different file");
        reloadButton.addActionListener(getReloadAction());

        fileLabel.setText("");

        listModel = new DefaultListModel<Settings>();
        for (Settings setting : Settings.values()) {
            listModel.addElement(setting);
        }
        settingItems.setModel(listModel);
        settingItems.setCellRenderer(new SettingCellRenderer());
        settingItems.setSelectedIndex(0);
        settingItems.addListSelectionListener(getSettingItemsAction());
    }

    private void initButton(JButton button, String iconName, String toolTipText) {
        ImageIcon icon;

        button.setText("");
        button.setToolTipText(toolTipText);
        button.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        button.setContentAreaFilled(false);
        icon = ImageUtil.getSmallImageIcon(iconName);
        button.setIcon(icon);
        button.setDisabledIcon(ImageUtil.createTransparentIcon(icon));
        button.setPressedIcon(ImageUtil.createDarkerIcon(icon));
        button.setFocusable(false);
    }

    public ActionListener getSaveAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                getConfiguration().save();
                StatusBar.getInstance().setText("Configuration saved");
            }
        };
    }

    public ActionListener getSaveAsAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser;
                int result;
                File file;
                FileChooserPreference pref;
                Window ancestor;

                chooser = new JFileChooser();
                chooser.setApproveButtonText("Save");
                chooser.setDialogTitle("Save settings");
                pref = new FileChooserPreference("SettingsSave", chooser);

                ancestor = SwingUtilities.getWindowAncestor((Component) ae
                        .getSource());
                result = chooser.showOpenDialog(ancestor);
                if (result == JFileChooser.APPROVE_OPTION) {
                    pref.save();
                    file = chooser.getSelectedFile();
                    getConfiguration().setConfigurationFile(file);
                    getConfiguration().save();
                    StatusBar.getInstance().setText(
                            "Configuration saved to " + file);
                }
            }
        };
    }

    public ActionListener getReloadAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser;
                int result;
                File file;
                FileChooserPreference pref;
                Window ancestor;

                chooser = new JFileChooser();
                chooser.setApproveButtonText("Reload");
                chooser.setDialogTitle("Reload settings");
                pref = new FileChooserPreference("SettingsSave", chooser);

                ancestor = SwingUtilities.getWindowAncestor((Component) ae
                        .getSource());
                result = chooser.showOpenDialog(ancestor);
                if (result == JFileChooser.APPROVE_OPTION) {
                    pref.save();
                    file = chooser.getSelectedFile();
                    if (!ConfigurationManager.getInstance().reload(file,
                            getConfiguration().getClass())) {
                        StatusBar.getInstance().setAlarm(
                                "Failed to reload from " + file);
                    }
                }
            }
        };
    }

    public ListSelectionListener getSettingItemsAction() {
        return new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                CardLayout layout;
                Settings settings;

                settings = (Settings) settingItems.getSelectedValue();
                layout = (CardLayout) settingsPanel.getLayout();
                layout.show(settingsPanel, settings.getName());
            }
        };
    }

    public void configurationChanged() {
        initConfiguration();
    }

    private void initConfiguration() {
        FilesComparatorSettings c;

        c = getConfiguration();

        fileLabel.setText(c.getConfigurationFileName());
        saveButton.setEnabled(c.isChanged());
    }

    public boolean checkSave() {
        SaveSettingsDialog dialog;

        if (getConfiguration().isChanged()) {
            dialog = new SaveSettingsDialog(mainPanel);
            dialog.show();
            if (dialog.isOK()) {
                dialog.doSave();
            }
        }

        return true;
    }

    private FilesComparatorSettings getConfiguration() {
        return FilesComparatorSettings.getInstance();
    }
}