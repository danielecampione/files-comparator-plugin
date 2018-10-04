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

package org.files_comparator.ui.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ColumnGroup {

    protected TableCellRenderer renderer;
    protected Vector<Object> v;
    protected String text;

    public ColumnGroup(String text) {
        this(null, text);
    }

    public ColumnGroup(TableCellRenderer renderer, String text) {
        MultiLineHeaderRenderer multiHeaderRenderer;

        if (renderer == null) {
            multiHeaderRenderer = new MultiLineHeaderRenderer();

            /*
                  internalRenderer = multiHeaderRenderer.getCellRenderer();
                  if (internalRenderer instanceof JLabel)
                  {
                    ((JLabel) internalRenderer).setOpaque(false);
                    multiHeaderRenderer.setCellRenderer(internalRenderer);
                  }
                  */
            this.renderer = multiHeaderRenderer;
        } else {
            this.renderer = renderer;
        }
        this.text = text;
        v = new Vector<Object>();
    }

    /**
     * @param obj    TableColumn or ColumnGroup
     */
    public void add(Object obj) {
        if (obj == null) {
            return;
        }
        v.addElement(obj);
    }

    /**
     * @param c    TableColumn
     * @param v    ColumnGroups
     */
    public Vector<ColumnGroup> getColumnGroups(TableColumn c,
            Vector<ColumnGroup> g) {
        g.addElement(this);
        if (v.contains(c)) {
            return g;
        }
        Enumeration<Object> enumerate = v.elements();

        while (enumerate.hasMoreElements()) {
            Object obj = enumerate.nextElement();

            if (obj instanceof ColumnGroup) {
                Vector<ColumnGroup> groups = (Vector<ColumnGroup>) ((ColumnGroup) obj)
                        .getColumnGroups(c, (Vector<ColumnGroup>) g.clone());

                if (groups != null) {
                    return groups;
                }
            }
        }
        return null;
    }

    public TableCellRenderer getHeaderRenderer() {
        return renderer;
    }

    public void setHeaderRenderer(TableCellRenderer renderer) {
        if (renderer != null) {
            this.renderer = renderer;
        }
    }

    public Object getHeaderValue() {
        return text;
    }

    public Dimension getSize(JTable table) {
        Component comp;
        int height;
        int width;
        Enumeration<Object> enumerate;
        Object obj;
        TableColumn aColumn;

        comp = renderer.getTableCellRendererComponent(table, getHeaderValue(),
                false, false, -1, -1);
        height = comp.getPreferredSize().height;
        width = 0;
        enumerate = v.elements();

        while (enumerate.hasMoreElements()) {
            obj = enumerate.nextElement();

            if (obj instanceof TableColumn) {
                aColumn = (TableColumn) obj;

                width += aColumn.getWidth();
            } else {
                width += ((ColumnGroup) obj).getSize(table).width;
            }
        }
        return new Dimension(width, height);
    }

    public String toString() {
        return super.toString() + ":" + text;
    }
}