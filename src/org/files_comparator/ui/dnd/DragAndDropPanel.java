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

package org.files_comparator.ui.dnd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import open_teradata_viewer.plugin.FilesComparatorPlugin;

import org.files_comparator.ui.util.ColorUtil;
import org.files_comparator.ui.util.Colors;
import org.files_comparator.util.StringUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DragAndDropPanel extends JPanel {

    private static final long serialVersionUID = 4400403045211051344L;

    private JComponent leftDragAndDropArea;
    private JComponent rightDragAndDropArea;
    private String leftFileName = "";
    private String rightFileName = "";

    public DragAndDropPanel() {
        setOpaque(true);
        setBackground(Color.white);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        leftDragAndDropArea = createDragAndDropArea();
        rightDragAndDropArea = createDragAndDropArea();

        add(leftDragAndDropArea);
        add(Box.createRigidArea(new Dimension(3, 0)));
        add(rightDragAndDropArea);

        addHierarchyListener(getHierarchyListener());
        addMouseListener(getMouseListener());
    }

    private HierarchyListener getHierarchyListener() {
        return new HierarchyListener() {

            public void hierarchyChanged(HierarchyEvent e) {
                JRootPane rootPane;

                if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
                    rootPane = getRootPane();
                    if (rootPane == null) {
                        return;
                    }

                    rootPane.setDropTarget(getDragAndDropTarget());
                }
            }
        };
    }

    private DropTarget getDragAndDropTarget() {
        return new DropTarget() {

            private static final long serialVersionUID = 1339068740036584801L;

            Component orgGlassPane;
            JPanel glassPane;

            public void dragEnter(DropTargetDragEvent dtde) {
                super.dragEnter(dtde);

                if (orgGlassPane == null) {
                    glassPane = new JPanel(new GridLayout(0, 2, 40, 40));
                    glassPane.setBorder(BorderFactory.createEmptyBorder(60, 10,
                            40, 10));
                    glassPane.setOpaque(false);

                    glassPane.add(createDropPane(leftFileName));
                    glassPane.add(createDropPane(rightFileName));

                    orgGlassPane = getRootPane().getGlassPane();
                    getRootPane().setGlassPane(glassPane);
                    glassPane.setVisible(true);
                    getRootPane().repaint();
                }
            }

            private JPanel createDropPane(String text) {
                JPanel p;
                JLabel label;

                label = new JLabel(text);
                label.setOpaque(false);
                label.setHorizontalAlignment(JLabel.LEFT);
                label.setVerticalAlignment(JLabel.TOP);
                label.setFont(label.getFont().deriveFont(16.0f));

                p = new JPanel(new BorderLayout());
                p.add(label, BorderLayout.CENTER);
                p.setBackground(new Color(238, 227, 187, 200));
                p.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));

                return p;
            }

            public void dragOver(DropTargetDragEvent dtde) {
                super.dragOver(dtde);
            }

            public void dragExit(DropTargetEvent dte) {
                super.dragExit(dte);
                resetGlassPane();
            }

            private void resetGlassPane() {
                if (orgGlassPane != null) {
                    getRootPane().setGlassPane(orgGlassPane);
                    orgGlassPane.setVisible(false);
                    orgGlassPane = null;
                }
            }

            public void drop(DropTargetDropEvent dtde) {
                Rectangle b;
                Point p;
                boolean left;
                String fileName;

                b = getRootPane().getBounds();
                p = dtde.getLocation();

                fileName = getFileName(dtde);
                if (StringUtil.isEmpty(fileName)) {
                    return;
                }

                left = p.x < (b.width - b.x) / 2;
                if (left) {
                    leftDragAndDropArea.setBackground(Colors.DND_SELECTED_NEW);
                    leftFileName = fileName;
                } else {
                    rightDragAndDropArea.setBackground(Colors.DND_SELECTED_NEW);
                    rightFileName = fileName;
                }

                resetGlassPane();
            }

            private String getFileName(DropTargetDropEvent dtde) {
                Transferable t;
                Object data;
                DataFlavor[] dataFlavors;
                String fileName;

                t = dtde.getTransferable();
                dataFlavors = t.getTransferDataFlavors();
                if (dataFlavors == null) {
                    return null;
                }

                dtde.acceptDrop(dtde.getSourceActions());

                try {
                    // Simplistic method that searches for a string which 
                    //   starts with the prefix "file:"
                    for (DataFlavor dataFlavor : dataFlavors) {
                        data = t.getTransferData(dataFlavor);
                        if (data.getClass() != String.class) {
                            continue;
                        }

                        fileName = (String) data;
                        if (fileName.startsWith("file:")) {
                            return fileName;
                        }
                    }
                } catch (Exception e) {
                    ExceptionDialog.hideException(e);
                }

                return null;
            }
        };
    }

    private JComponent createDragAndDropArea() {
        JPanel p;

        p = new JPanel();
        p.setOpaque(true);
        p.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        p.setBackground(ColorUtil.brighter(Color.LIGHT_GRAY));
        p.setPreferredSize(new Dimension(20, 0));

        return p;
    }

    private MouseListener getMouseListener() {
        return new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (StringUtil.isEmpty(leftFileName)
                        || StringUtil.isEmpty(rightFileName)) {
                    return;
                }

                if (leftFileName.equals(rightFileName)) {
                    return;
                }

                leftDragAndDropArea.setBackground(Colors.DND_SELECTED_USED);
                rightDragAndDropArea.setBackground(Colors.DND_SELECTED_USED);

                try {
                    FilesComparatorPlugin
                            .getInstance()
                            .getFilesComparatorPanel()
                            .openComparison(
                                    new File(new URL(leftFileName).toURI())
                                            .getAbsolutePath(),
                                    new File(new URL(rightFileName).toURI())
                                            .getAbsolutePath());
                } catch (Exception e) {
                    ExceptionDialog.hideException(e);
                }
            }
        };
    }
}