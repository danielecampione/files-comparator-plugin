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

package org.files_comparator.ui;

import javax.swing.JPanel;

/**
 * 
 * 
 * @author D. Campione
 *
 */
abstract public class AbstractBarDialog extends JPanel {

    private static final long serialVersionUID = 672841266726000534L;

    // Instance variables:
    private FilesComparatorPanel filesComparatorPanel;

    public AbstractBarDialog(FilesComparatorPanel filesComparatorPanel) {
        this.filesComparatorPanel = filesComparatorPanel;

        init();
    }

    abstract protected void init();

    abstract protected void _activate();

    abstract protected void _deactivate();

    protected FilesComparatorPanel getFilesComparatorPanel() {
        return filesComparatorPanel;
    }

    final public void activate() {
        _activate();
    }

    final public void deactivate() {
        _deactivate();
    }
}
