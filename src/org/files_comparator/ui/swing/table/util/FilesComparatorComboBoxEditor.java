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

package org.files_comparator.ui.swing.table.util;

import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorComboBoxEditor extends DefaultCellEditor {

    private static final long serialVersionUID = 7850441017510119350L;

    public FilesComparatorComboBoxEditor(Object[] items) {
        super(new JComboBox<Object>(items));
    }

    public FilesComparatorComboBoxEditor(List<?> items) {
        this(items.toArray());
    }
}