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

package org.files_comparator.ui.search;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.files_comparator.ui.AbstractBarDialog;
import org.files_comparator.ui.FilesComparatorPanel;
import org.files_comparator.ui.util.ImageUtil;
import org.files_comparator.util.StringUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SearchBarDialog extends AbstractBarDialog {

    private static final long serialVersionUID = -8026734272358239023L;

    // class variables:
    private static final String CP_FOREGROUND = "FilesComparator.foreground";
    @SuppressWarnings("unused")
    private static final String CP_BACKGROUND = "FilesComparator.background";

    // Instance variables:
    private JTextField searchField;
    private JLabel searchResult;
    private Timer timer;

    public SearchBarDialog(FilesComparatorPanel filesComparatorPanel) {
        super(filesComparatorPanel);
    }

    protected void init() {
        JButton closeButton;
        JButton previousButton;
        JButton nextButton;

        setLayout(new FlowLayout(FlowLayout.LEADING));

        // Close the search dialog:
        closeButton = new JButton(
                ImageUtil.getImageIcon("files-comparator_close"));
        closeButton.setRolloverIcon(ImageUtil
                .getImageIcon("files-comparator_close-rollover"));
        closeButton.setPressedIcon(ImageUtil
                .getImageIcon("files-comparator_close-pressed"));
        closeButton.addActionListener(getCloseAction());
        initButton(closeButton);
        closeButton.setBorder(null);

        // Incremental search:
        searchField = new JTextField(15);
        searchField.getDocument().addDocumentListener(getSearchAction());
        searchField.addKeyListener(getSearchKeyAction());

        // Find previous match:
        previousButton = new JButton("Previous",
                ImageUtil.getImageIcon("stock_data-previous"));
        previousButton.addActionListener(getPreviousAction());
        initButton(previousButton);

        // Find next match:
        nextButton = new JButton("Next",
                ImageUtil.getImageIcon("stock_data-next"));
        nextButton.addActionListener(getNextAction());
        initButton(nextButton);

        searchResult = new JLabel();

        initButton(previousButton);
        add(closeButton);
        add(Box.createHorizontalStrut(5));
        add(new JLabel("Find:"));
        add(searchField);
        add(previousButton);
        add(nextButton);
        add(Box.createHorizontalStrut(10));
        add(searchResult);

        timer = new Timer(500, executeSearch());
        timer.setRepeats(false);
    }

    private void initButton(AbstractButton button) {
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.setBorder(new EmptyBorder(0, 5, 0, 5));
    }

    public SearchCommand getCommand() {
        return new SearchCommand(searchField.getText(), false);
    }

    public void setSearchText(String searchText) {
        if (StringUtil.isEmpty(searchText)) {
            return;
        }

        // You can start a search by selecting some text and then
        //   hit CTRL-F. But if you have selected more than x 
        //   characters you probably don't want to search for that.
        //   So I choose to ignore those texts.
        if (searchText.length() > 50) {
            return;
        }

        searchField.setText(searchText);
    }

    public void _activate() {
        searchField.requestFocus();
        searchField.selectAll();

        if (!StringUtil.isEmpty(searchField.getText())) {
            timer.restart();
        }
    }

    public void _deactivate() {
    }

    private DocumentListener getSearchAction() {
        return new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                timer.restart();
            }

            public void insertUpdate(DocumentEvent e) {
                timer.restart();
            }

            public void removeUpdate(DocumentEvent e) {
                timer.restart();
            }
        };
    }

    private ActionListener executeSearch() {
        return new ActionListener() {
            @SuppressWarnings("unused")
            public void actionPerformed(ActionEvent ae) {
                boolean notFound;
                Color color;
                String searchText;
                SearchHits searchHits;

                searchText = searchField.getText();

                searchHits = getFilesComparatorPanel().doSearch(null);
                notFound = (searchHits == null || searchHits.getSearchHits()
                        .size() == 0);

                if (notFound) {
                    // If GTK Look & Feel is chosen it's not possible to set
                    // the background color to red.
                    if (searchField.getForeground() != Color.red) {
                        // Remember the original colors:
                        searchField.putClientProperty(CP_FOREGROUND,
                                searchField.getForeground());

                        // Set the new colors:
                        searchField.setForeground(Color.red);
                    }

                    searchResult.setIcon(ImageUtil
                            .getImageIcon("bullet-warning"));
                    searchResult.setText("Phrase not found");
                } else {
                    // Set the original colors:
                    color = (Color) searchField
                            .getClientProperty(CP_FOREGROUND);
                    if (color != null) {
                        searchField.setForeground(color);
                        searchField.putClientProperty(CP_FOREGROUND, null);
                    }

                    if (!StringUtil.isEmpty(searchResult.getText())) {
                        searchResult.setIcon(null);
                        searchResult.setText("");
                    }
                }
            }
        };
    }

    private KeyListener getSearchKeyAction() {
        return new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    getFilesComparatorPanel().doNextSearch(null);
                }
            }
        };
    }

    private ActionListener getCloseAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                getFilesComparatorPanel().doStopSearch(null);
            }
        };
    }

    private ActionListener getPreviousAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                getFilesComparatorPanel().doPreviousSearch(null);
            }
        };
    }

    private ActionListener getNextAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                getFilesComparatorPanel().doNextSearch(null);
            }
        };
    }
}
