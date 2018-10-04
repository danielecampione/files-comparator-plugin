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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import open_teradata_viewer.plugin.FilesComparatorPlugin;

import org.files_comparator.settings.EditorSettings;
import org.files_comparator.settings.FilesComparatorSettings;
import org.files_comparator.ui.util.EmptyIcon;
import org.files_comparator.ui.util.FontUtil;
import org.files_comparator.util.CharsetDetector;
import org.files_comparator.util.Ignore;
import org.files_comparator.util.conf.ConfigurationListenerIF;

import com.l2fprod.common.swing.JFontChooser;

/**
 *
 *
 * @author D. Campione
 * 
 */
public class EditorSettingsPanel extends EditorSettingsForm
        implements
            ConfigurationListenerIF {

    private static final long serialVersionUID = 8952965640919154495L;

    private static JDialog colorDialog;
    private static JColorChooser colorChooser;
    private boolean originalAntialias;

    public EditorSettingsPanel() {
        originalAntialias = getEditorSettings().isAntialiasEnabled();

        initConfiguration();
        init();

        FilesComparatorSettings.getInstance().addConfigurationListener(this);
    }

    private void init() {
        // ignore:
        ignoreWhitespaceAtBeginCheckBox
                .addActionListener(getIgnoreWhitespaceAtBeginAction());
        ignoreWhitespaceInBetweenCheckBox
                .addActionListener(getIgnoreWhitespaceInBetweenAction());
        ignoreWhitespaceAtEndCheckBox
                .addActionListener(getIgnoreWhitespaceAtEndAction());
        ignoreEOLCheckBox.addActionListener(getIgnoreEOLAction());
        ignoreBlankLinesCheckBox.addActionListener(getIgnoreBlankLinesAction());
        ignoreCaseCheckBox.addActionListener(getIgnoreCaseAction());

        // Miscellaneous:
        leftsideReadonlyCheckBox.addActionListener(getLeftsideReadonlyAction());
        rightsideReadonlyCheckBox
                .addActionListener(getRightsideReadonlyAction());
        antialiasCheckBox.addActionListener(getAntialiasAction());
        tabSizeSpinner.addChangeListener(getTabSizeChangeListener());
        showLineNumbersCheckBox.addActionListener(getShowLineNumbersAction());

        // Colors:
        colorAddedButton.addActionListener(getColorAddedAction());
        colorDeletedButton.addActionListener(getColorDeletedAction());
        colorChangedButton.addActionListener(getColorChangedAction());
        restoreOriginalColorsButton
                .addActionListener(getRestoreOriginalColorsAction());

        // Font:
        defaultFontRadioButton.addActionListener(getDefaultFontAction());
        customFontRadioButton.addActionListener(getCustomFontAction());
        fontChooserButton.addActionListener(getFontChooserAction());

        // File encoding:
        defaultEncodingRadioButton.setText(defaultEncodingRadioButton.getText()
                + " (" + CharsetDetector.getInstance().getDefaultCharset()
                + ")");

        defaultEncodingRadioButton
                .addActionListener(getDefaultEncodingAction());
        detectEncodingRadioButton.addActionListener(getDetectEncodingAction());
        specificEncodingRadioButton
                .addActionListener(getSpecificEncodingAction());
        specificEncodingComboBox.setModel(new DefaultComboBoxModel<Object>(
                CharsetDetector.getInstance().getCharsetNameList().toArray()));
        specificEncodingComboBox.setSelectedItem(getEditorSettings()
                .getSpecificFileEncodingName());
        specificEncodingComboBox
                .addActionListener(getSpecificEncodingNameAction());

        // Toolbar appearance:
        toolbarButtonIconComboBox.setModel(getToolbarButtonIconModel());
        toolbarButtonIconComboBox.setSelectedItem(getEditorSettings()
                .getToolbarButtonIcon());
        toolbarButtonIconComboBox
                .addActionListener(getToolbarButtonIconAction());
        toolbarButtonTextEnabledCheckBox
                .addActionListener(getToolbarButtonTextEnabledAction());
    }

    private ChangeListener getTabSizeChangeListener() {
        return new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                getEditorSettings().setTabSize(
                        (Integer) tabSizeSpinner.getValue());
            }
        };
    }

    private ActionListener getColorAddedAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Color color;

                color = chooseColor(getEditorSettings().getAddedColor());
                if (color != null) {
                    getEditorSettings().setAddedColor(color);
                }
            }
        };
    }

    private ActionListener getColorDeletedAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Color color;

                color = chooseColor(getEditorSettings().getDeletedColor());
                if (color != null) {
                    getEditorSettings().setDeletedColor(color);
                }
            }
        };
    }

    private ActionListener getColorChangedAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Color color;

                color = chooseColor(getEditorSettings().getChangedColor());
                if (color != null) {
                    getEditorSettings().setChangedColor(color);
                }
            }
        };
    }

    private ActionListener getShowLineNumbersAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getEditorSettings().setShowLineNumbers(
                        showLineNumbersCheckBox.isSelected());
            }
        };
    }

    private ActionListener getIgnoreWhitespaceAtBeginAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getEditorSettings().setIgnoreWhitespaceAtBegin(
                        ignoreWhitespaceAtBeginCheckBox.isSelected());
            }
        };
    }

    private ActionListener getIgnoreWhitespaceInBetweenAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getEditorSettings().setIgnoreWhitespaceInBetween(
                        ignoreWhitespaceInBetweenCheckBox.isSelected());
            }
        };
    }

    private ActionListener getIgnoreWhitespaceAtEndAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getEditorSettings().setIgnoreWhitespaceAtEnd(
                        ignoreWhitespaceAtEndCheckBox.isSelected());
            }
        };
    }

    private ActionListener getIgnoreEOLAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getEditorSettings()
                        .setIgnoreEOL(ignoreEOLCheckBox.isSelected());
            }
        };
    }

    private ActionListener getIgnoreBlankLinesAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getEditorSettings().setIgnoreBlankLines(
                        ignoreBlankLinesCheckBox.isSelected());
            }
        };
    }

    private ActionListener getIgnoreCaseAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getEditorSettings().setIgnoreCase(
                        ignoreCaseCheckBox.isSelected());
            }
        };
    }

    private ActionListener getLeftsideReadonlyAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getEditorSettings().setLeftsideReadonly(
                        leftsideReadonlyCheckBox.isSelected());
            }
        };
    }

    private ActionListener getRightsideReadonlyAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getEditorSettings().setRightsideReadonly(
                        rightsideReadonlyCheckBox.isSelected());
            }
        };
    }

    private ActionListener getAntialiasAction() {
        return new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getEditorSettings().enableAntialias(
                        antialiasCheckBox.isSelected());
            }
        };
    }

    private ActionListener getRestoreOriginalColorsAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                getEditorSettings().restoreColors();
            }
        };
    }

    private ActionListener getDefaultEncodingAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                getEditorSettings().setDefaultFileEncodingEnabled(true);
                getEditorSettings().setDetectFileEncodingEnabled(false);
                getEditorSettings().setSpecificFileEncodingEnabled(false);
            }
        };
    }

    private ActionListener getDetectEncodingAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                getEditorSettings().setDefaultFileEncodingEnabled(false);
                getEditorSettings().setDetectFileEncodingEnabled(true);
                getEditorSettings().setSpecificFileEncodingEnabled(false);
            }
        };
    }

    private ActionListener getSpecificEncodingAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                getEditorSettings().setDefaultFileEncodingEnabled(false);
                getEditorSettings().setDetectFileEncodingEnabled(false);
                getEditorSettings().setSpecificFileEncodingEnabled(true);
            }
        };
    }

    private ActionListener getSpecificEncodingNameAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                getEditorSettings().setSpecificFileEncodingName(
                        (String) specificEncodingComboBox.getSelectedItem());
            }
        };
    }

    private ActionListener getToolbarButtonIconAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                getEditorSettings()
                        .setToolbarButtonIcon(
                                (EditorSettings.ToolbarButtonIcon) toolbarButtonIconComboBox
                                        .getSelectedItem());
                FilesComparatorPlugin.getInstance().getFilesComparatorPanel()
                        .addToolBar();
            }
        };
    }

    private ActionListener getToolbarButtonTextEnabledAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                getEditorSettings().setToolbarButtonTextEnabled(
                        toolbarButtonTextEnabledCheckBox.isSelected());
                FilesComparatorPlugin.getInstance().getFilesComparatorPanel()
                        .addToolBar();
            }
        };
    }

    private ActionListener getDefaultFontAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                getEditorSettings().enableCustomFont(
                        !defaultFontRadioButton.isSelected());
            }
        };
    }

    private ActionListener getCustomFontAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                getEditorSettings().enableCustomFont(
                        customFontRadioButton.isSelected());
            }
        };
    }

    private ActionListener getFontChooserAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Font font;

                font = chooseFont(getEditorFont());
                if (font != null) {
                    getEditorSettings().setFont(font);
                }
            }
        };
    }

    private Color chooseColor(Color initialColor) {
        // Do not instantiate ColorChooser multiple times because it contains
        //   a memory leak.
        if (colorDialog == null) {
            colorChooser = new JColorChooser(initialColor);
            colorDialog = JColorChooser.createDialog(null, "Choose color",
                    true, colorChooser, null, null);
        }

        colorChooser.setColor(initialColor);
        colorDialog.setVisible(true);

        return colorChooser.getColor();
    }

    private Font chooseFont(Font initialFont) {
        JFontChooser fontChooser;

        fontChooser = new JFontChooser();
        fontChooser.setSelectedFont(initialFont);

        return fontChooser.showFontDialog(this, "");
    }

    private ComboBoxModel<?> getToolbarButtonIconModel() {
        return new DefaultComboBoxModel<Object>(getEditorSettings()
                .getToolbarButtonIcons());
    }

    public void configurationChanged() {
        initConfiguration();
    }

    private void initConfiguration() {
        EditorSettings settings;
        Font font;
        Ignore ignore;

        settings = getEditorSettings();
        ignore = settings.getIgnore();
        colorAddedButton
                .setIcon(new EmptyIcon(settings.getAddedColor(), 20, 20));
        colorAddedButton.setText("");
        colorDeletedButton.setIcon(new EmptyIcon(settings.getDeletedColor(),
                20, 20));
        colorDeletedButton.setText("");
        colorChangedButton.setIcon(new EmptyIcon(settings.getChangedColor(),
                20, 20));
        colorChangedButton.setText("");
        showLineNumbersCheckBox.setSelected(settings.getShowLineNumbers());
        ignoreWhitespaceAtBeginCheckBox
                .setSelected(ignore.ignoreWhitespaceAtBegin);
        ignoreWhitespaceInBetweenCheckBox
                .setSelected(ignore.ignoreWhitespaceInBetween);
        ignoreWhitespaceAtEndCheckBox.setSelected(ignore.ignoreWhitespaceAtEnd);
        ignoreEOLCheckBox.setSelected(ignore.ignoreEOL);
        ignoreBlankLinesCheckBox.setSelected(ignore.ignoreBlankLines);
        ignoreCaseCheckBox.setSelected(ignore.ignoreCase);
        leftsideReadonlyCheckBox.setSelected(settings.getLeftsideReadonly());
        rightsideReadonlyCheckBox.setSelected(settings.getRightsideReadonly());
        antialiasCheckBox.setSelected(settings.isAntialiasEnabled());
        if (originalAntialias != settings.isAntialiasEnabled()) {
            antialiasCheckBox.setText("antialias on (NEED A RESTART)");
        } else {
            antialiasCheckBox.setText("antialias on");
        }
        tabSizeSpinner.setValue(settings.getTabSize());
        font = getEditorFont();
        fontChooserButton.setFont(font);
        fontChooserButton.setText(font.getName() + " (" + font.getSize() + ")");
        defaultFontRadioButton.setSelected(!settings.isCustomFontEnabled());
        customFontRadioButton.setSelected(settings.isCustomFontEnabled());

        defaultEncodingRadioButton.setSelected(settings
                .getDefaultFileEncodingEnabled());
        detectEncodingRadioButton.setSelected(settings
                .getDetectFileEncodingEnabled());
        specificEncodingRadioButton.setSelected(settings
                .getSpecificFileEncodingEnabled());

        toolbarButtonIconComboBox.setSelectedItem(getEditorSettings()
                .getToolbarButtonIcon());
        toolbarButtonTextEnabledCheckBox.setSelected(getEditorSettings()
                .isToolbarButtonTextEnabled());

        revalidate();
    }

    private EditorSettings getEditorSettings() {
        return FilesComparatorSettings.getInstance().getEditor();
    }

    private Font getEditorFont() {
        Font font;

        font = getEditorSettings().getFont();
        font = font == null ? FontUtil.defaultTextAreaFont : font;

        return font;
    }
}