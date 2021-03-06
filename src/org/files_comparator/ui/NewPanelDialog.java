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
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.UISupport;

import org.files_comparator.settings.FilesComparatorSettings;
import org.files_comparator.settings.util.Filter;
import org.files_comparator.util.ObjectUtil;
import org.files_comparator.util.prefs.ComboBoxPreference;
import org.files_comparator.util.prefs.ComboBoxSelectionPreference;
import org.files_comparator.util.prefs.DirectoryChooserPreference;
import org.files_comparator.util.prefs.FileChooserPreference;
import org.files_comparator.util.prefs.TabbedPanePreference;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NewPanelDialog {

    // Class variables:
    // File comparison:
    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public enum Function {

        FILE_COMPARISON, DIRECTORY_COMPARISON, VERSION_CONTROL;
    }

    private static String RIGHT_FILENAME = "RIGHT_FILENAME";
    private static String LEFT_FILENAME = "LEFT_FILENAME";
    private static String previousDirectoryName;

    // Directory comparison:
    private static String RIGHT_DIRECTORY = "RIGHT_DIRECTORY";
    private static String LEFT_DIRECTORY = "LEFT_DIRECTORY";

    // Instance variables:
    private FilesComparatorPanel filesComparatorPanel;
    private JTabbedPane tabbedPane;
    private Function function;
    private String leftFileName;
    private String rightFileName;
    private JComboBox<String> leftFileComboBox;
    private JComboBox<String> rightFileComboBox;
    private String leftDirectoryName;
    private String rightDirectoryName;
    private JComboBox<String> leftDirectoryComboBox;
    private JComboBox<String> rightDirectoryComboBox;
    private String versionControlDirectoryName;
    private JComboBox<String> versionControlDirectoryComboBox;
    private JComboBox<?> filterComboBox;
    private JDialog dialog;

    public NewPanelDialog(FilesComparatorPanel filesComparatorPanel) {
        this.filesComparatorPanel = filesComparatorPanel;
    }

    public void show() {
        JOptionPane pane;

        pane = new JOptionPane(getChooseFilePanel());
        pane.setOptionType(JOptionPane.OK_CANCEL_OPTION);

        dialog = pane.createDialog(filesComparatorPanel, "Choose files");
        dialog.setResizable(true);
        try {
            UISupport.showDialog(dialog);

            if (ObjectUtil.equals(pane.getValue(), JOptionPane.OK_OPTION)) {
                switch (tabbedPane.getSelectedIndex()) {

                case 0:
                    setFunction(Function.FILE_COMPARISON);
                    break;

                case 1:
                    setFunction(Function.DIRECTORY_COMPARISON);
                    break;

                case 2:
                    setFunction(Function.VERSION_CONTROL);
                    break;
                }
            }
        } finally {
            // Always dispose a dialog -> otherwise there is a memory leak
            dialog.dispose();
        }
    }

    private void setFunction(Function function) {
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    public String getLeftFileName() {
        return leftFileName;
    }

    public String getRightFileName() {
        return rightFileName;
    }

    public String getLeftDirectoryName() {
        return leftDirectoryName;
    }

    public String getRightDirectoryName() {
        return rightDirectoryName;
    }

    public String getVersionControlDirectoryName() {
        return versionControlDirectoryName;
    }

    public Filter getFilter() {
        if (filterComboBox.getSelectedItem() instanceof Filter) {
            return (Filter) filterComboBox.getSelectedItem();
        }

        return null;
    }

    private JComponent getChooseFilePanel() {
        JPanel panel;

        tabbedPane = new JTabbedPane();
        tabbedPane.add("File Comparison", getFileComparisonPanel());
        tabbedPane.add("Directory Comparison", getDirectoryComparisonPanel());
        //tabbedPane.add("Version control", getVersionControlPanel());

        new TabbedPanePreference("NewPanelTabbedPane", tabbedPane);

        panel = new JPanel(new BorderLayout());
        panel.add(tabbedPane, BorderLayout.CENTER);

        return panel;
    }

    private JComponent getFileComparisonPanel() {
        JPanel panel;
        String columns;
        String rows;
        FormLayout layout;
        CellConstraints cc;
        JLabel label;
        JButton button;

        columns = "10px, right:pref, 10px, max(150dlu;pref):grow, 5px, pref, 10px";
        rows = "10px, fill:pref, 5px, fill:pref, 5px, fill:pref, 10px";
        layout = new FormLayout(columns, rows);
        cc = new CellConstraints();

        panel = new JPanel(layout);

        label = new JLabel("Left");
        button = new JButton("Browse...");
        leftFileComboBox = new JComboBox<String>();
        leftFileComboBox.setEditable(false);
        leftFileComboBox.addActionListener(getFileSelectAction());
        new ComboBoxPreference("LeftFile", leftFileComboBox);

        button.setActionCommand(LEFT_FILENAME);
        button.addActionListener(getFileBrowseAction());
        panel.add(label, cc.xy(2, 2));
        panel.add(leftFileComboBox, cc.xy(4, 2));
        panel.add(button, cc.xy(6, 2));

        label = new JLabel("Right");
        button = new JButton("Browse...");
        button.setActionCommand(RIGHT_FILENAME);
        button.addActionListener(getFileBrowseAction());
        rightFileComboBox = new JComboBox<String>();
        rightFileComboBox.setEditable(false);
        rightFileComboBox.addActionListener(getFileSelectAction());
        new ComboBoxPreference("RightFile", rightFileComboBox);
        panel.add(label, cc.xy(2, 4));
        panel.add(rightFileComboBox, cc.xy(4, 4));
        panel.add(button, cc.xy(6, 4));

        return panel;
    }

    public ActionListener getFileBrowseAction() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                FileChooserPreference pref;
                JFileChooser chooser;
                int result;
                String fileName;
                JComboBox<String> comboBox;
                Window ancestor;

                // Don't allow accidentaly creation or rename of files.
                UIManager.put("FileChooser.readOnly", Boolean.TRUE);
                chooser = new JFileChooser();
                // Reset the readOnly property as it is systemwide.
                UIManager.put("FileChooser.readOnly", Boolean.FALSE);
                chooser.setApproveButtonText("Choose");
                chooser.setDialogTitle("Choose file");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                pref = new FileChooserPreference("Browse-"
                        + ae.getActionCommand(), chooser);

                ancestor = SwingUtilities.getWindowAncestor((Component) ae
                        .getSource());
                result = chooser.showOpenDialog(ancestor);

                if (result == JFileChooser.APPROVE_OPTION) {
                    pref.save();

                    try {
                        fileName = chooser.getSelectedFile().getCanonicalPath();

                        comboBox = null;
                        if (ae.getActionCommand().equals(LEFT_FILENAME)) {
                            comboBox = leftFileComboBox;
                        } else if (ae.getActionCommand().equals(RIGHT_FILENAME)) {
                            comboBox = rightFileComboBox;
                        }

                        if (comboBox != null) {
                            comboBox.insertItemAt(fileName, 0);
                            comboBox.setSelectedIndex(0);
                            dialog.pack();
                        }
                    } catch (Exception e) {
                        ExceptionDialog.hideException(e);
                    }
                }
            }
        };
    }

    public ActionListener getFileSelectAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Object source;

                source = ae.getSource();
                if (source == leftFileComboBox) {
                    leftFileName = (String) leftFileComboBox.getSelectedItem();
                } else if (source == rightFileComboBox) {
                    rightFileName = (String) rightFileComboBox
                            .getSelectedItem();
                }
            }
        };
    }

    private JComponent getDirectoryComparisonPanel() {
        JPanel panel;
        String columns;
        String rows;
        FormLayout layout;
        CellConstraints cc;
        JLabel label;
        JButton button;

        columns = "10px, right:pref, 10px, max(150dlu;pref):grow, 5px, pref, 10px";
        rows = "10px, fill:pref, 5px, fill:pref, 5px, fill:pref, 5px, fill:pref, 10px";
        layout = new FormLayout(columns, rows);
        cc = new CellConstraints();

        panel = new JPanel(layout);

        label = new JLabel("Left");
        button = new JButton("Browse...");
        leftDirectoryComboBox = new JComboBox<String>();
        leftDirectoryComboBox.setEditable(false);
        leftDirectoryComboBox.addActionListener(getDirectorySelectAction());
        new ComboBoxPreference("LeftDirectory", leftDirectoryComboBox);

        button.setActionCommand(LEFT_DIRECTORY);
        button.addActionListener(getDirectoryBrowseAction());
        panel.add(label, cc.xy(2, 2));
        panel.add(leftDirectoryComboBox, cc.xy(4, 2));
        panel.add(button, cc.xy(6, 2));

        label = new JLabel("Right");
        button = new JButton("Browse...");
        button.setActionCommand(RIGHT_DIRECTORY);
        button.addActionListener(getDirectoryBrowseAction());
        rightDirectoryComboBox = new JComboBox<String>();
        rightDirectoryComboBox.setEditable(false);
        rightDirectoryComboBox.addActionListener(getDirectorySelectAction());
        new ComboBoxPreference("RightDirectory", rightDirectoryComboBox);
        panel.add(label, cc.xy(2, 4));
        panel.add(rightDirectoryComboBox, cc.xy(4, 4));
        panel.add(button, cc.xy(6, 4));

        label = new JLabel("Filter");
        filterComboBox = new JComboBox<Object>(getFilters());
        panel.add(label, cc.xy(2, 6));
        panel.add(filterComboBox, cc.xy(4, 6));
        new ComboBoxSelectionPreference("Filter", filterComboBox);

        return panel;
    }

    public ActionListener getDirectoryBrowseAction() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                DirectoryChooserPreference pref;
                JFileChooser chooser;
                int result;
                String fileName;
                JComboBox<String> comboBox;
                Window ancestor;

                // Don't allow accidentaly creation or rename of files.
                UIManager.put("FileChooser.readOnly", Boolean.TRUE);
                chooser = new JFileChooser();
                // Reset the readOnly property as it is systemwide.
                UIManager.put("FileChooser.readOnly", Boolean.FALSE);
                chooser.setApproveButtonText("Choose");
                chooser.setDialogTitle("Choose directory");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                pref = new DirectoryChooserPreference("Browse-"
                        + ae.getActionCommand(), chooser, previousDirectoryName);

                ancestor = SwingUtilities.getWindowAncestor((Component) ae
                        .getSource());
                result = chooser.showOpenDialog(ancestor);

                if (result == JFileChooser.APPROVE_OPTION) {
                    pref.save();

                    try {
                        fileName = chooser.getSelectedFile().getCanonicalPath();
                        previousDirectoryName = fileName;

                        comboBox = null;
                        if (ae.getActionCommand().equals(LEFT_DIRECTORY)) {
                            comboBox = leftDirectoryComboBox;
                        } else if (ae.getActionCommand()
                                .equals(RIGHT_DIRECTORY)) {
                            comboBox = rightDirectoryComboBox;
                        }

                        if (comboBox != null) {
                            comboBox.insertItemAt(fileName, 0);
                            comboBox.setSelectedIndex(0);
                            dialog.pack();
                        }
                    } catch (Exception e) {
                        ExceptionDialog.hideException(e);
                    }
                }
            }
        };
    }

    public ActionListener getDirectorySelectAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Object source;

                source = ae.getSource();
                if (source == leftDirectoryComboBox) {
                    leftDirectoryName = (String) leftDirectoryComboBox
                            .getSelectedItem();
                } else if (source == rightDirectoryComboBox) {
                    rightDirectoryName = (String) rightDirectoryComboBox
                            .getSelectedItem();
                }
            }
        };
    }

    public ActionListener getVersionControlDirectoryBrowseAction() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                DirectoryChooserPreference pref;
                JFileChooser chooser;
                int result;
                String fileName;
                JComboBox<String> comboBox;
                Window ancestor;

                // Don't allow accidentaly creation or rename of files.
                UIManager.put("FileChooser.readOnly", Boolean.TRUE);
                chooser = new JFileChooser();
                // Reset the readOnly property as it is systemwide.
                UIManager.put("FileChooser.readOnly", Boolean.FALSE);
                chooser.setApproveButtonText("Choose");
                chooser.setDialogTitle("Choose directory");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                pref = new DirectoryChooserPreference("VersionControlBrowse",
                        chooser, previousDirectoryName);

                ancestor = SwingUtilities.getWindowAncestor((Component) ae
                        .getSource());
                result = chooser.showOpenDialog(ancestor);

                if (result == JFileChooser.APPROVE_OPTION) {
                    pref.save();

                    try {
                        fileName = chooser.getSelectedFile().getCanonicalPath();
                        previousDirectoryName = fileName;

                        comboBox = versionControlDirectoryComboBox;
                        comboBox.insertItemAt(fileName, 0);
                        comboBox.setSelectedIndex(0);
                        dialog.pack();
                    } catch (Exception e) {
                        ExceptionDialog.hideException(e);
                    }
                }
            }
        };
    }

    public ActionListener getVersionControlDirectorySelectAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                versionControlDirectoryName = (String) versionControlDirectoryComboBox
                        .getSelectedItem();
            }
        };
    }

    private Object[] getFilters() {
        List<Object> filters;

        filters = new ArrayList<Object>();
        filters.add("No filter");
        for (Filter f : FilesComparatorSettings.getInstance().getFilter()
                .getFilters()) {
            filters.add(f);
        }

        return filters.toArray();
    }
}