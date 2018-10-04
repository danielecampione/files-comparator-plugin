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

package org.files_comparator.ui.swing.table;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXTreeTable;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorTreeTable extends JXTreeTable {

    private static final long serialVersionUID = 4607628970961527307L;

    private int charWidth;
    private TreeTableHackerExt myTreeTableHacker;

    public FilesComparatorTreeTable() {
        charWidth = getFontMetrics(getFont()).charWidth('W');

        setAutoResizeMode(AUTO_RESIZE_NEXT_COLUMN);
    }

    public void setTreeTableModel(FilesComparatorTreeTableModel tableModel) {
        TableColumnModel cModel;
        TableColumn column;
        int preferredWidth;
        TableCellEditor editor;
        TableCellRenderer renderer;

        super.setTreeTableModel(tableModel);

        setColumnControlVisible(true);
        /*
           ((DefaultCellEditor) getDefaultEditor(AbstractTreeTableModel.hierarchicalColumnClass))
             .setClickCountToStart(1);
         */
        if (tableModel != null) {
            // Make sure the icons fit well.
            if (getRowHeight() < 22) {
                setRowHeight(22);
            }

            cModel = getColumnModel();
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                column = cModel.getColumn(i);
                renderer = tableModel.getRenderer(i);
                if (renderer != null) {
                    column.setCellRenderer(renderer);
                }

                editor = tableModel.getEditor(i);
                if (renderer != null) {
                    column.setCellEditor(editor);
                }

                preferredWidth = charWidth * tableModel.getColumnSize(i);
                if (preferredWidth > 0) {
                    column.setMinWidth(preferredWidth);
                    column.setMaxWidth(preferredWidth);
                    column.setPreferredWidth(preferredWidth);
                } else {
                    getTableHeader().setResizingColumn(column);
                }
            }
        }
    }

    @Override
    protected TreeTableHacker getTreeTableHacker() {
        if (myTreeTableHacker == null) {
            myTreeTableHacker = new TreeTableHackerExt() {
                @Override
                protected boolean isHitDetectionFromProcessMouse() {
                    return false;
                }
            };
        }

        return myTreeTableHacker;
    }
}
