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

package org.files_comparator.ui.swing.table;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorTable extends JTable {

    private static final long serialVersionUID = 8463982919112219012L;

    private int charWidth;

    public FilesComparatorTable() {
        charWidth = getFontMetrics(getFont()).charWidth('W');
    }

    public void setModel(FilesComparatorTableModel tableModel) {
        TableColumnModel columnModel;
        TableColumn column;
        int preferredWidth;
        TableCellEditor editor;
        TableCellRenderer renderer;

        super.setModel(tableModel);

        if (tableModel != null) {
            // Make sure the icons fit well.
            if (getRowHeight() < 22) {
                setRowHeight(22);
            }

            columnModel = getColumnModel();

            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                column = columnModel.getColumn(i);

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
                }
            }
        }
    }

    public TableCellEditor getCellEditor(int row, int column) {
        Class<?> clazz;
        TableCellEditor editor;

        clazz = ((FilesComparatorTableModel) getModel()).getColumnClass(row,
                column);
        editor = getDefaultEditor(clazz);
        if (editor != null) {
            return editor;
        }

        return super.getCellEditor(row, column);
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        Class<?> clazz;
        TableCellRenderer renderer;
        TableModel model;

        model = getModel();
        if (model instanceof FilesComparatorTableModel) {
            clazz = ((FilesComparatorTableModel) model).getColumnClass(row,
                    column);
            renderer = getDefaultRenderer(clazz);
            if (renderer != null) {
                return renderer;
            }
        }

        return super.getCellRenderer(row, column);
    }
}