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

import java.awt.ComponentOrientation;
import java.awt.Container;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class LeftScrollPaneLayout extends javax.swing.ScrollPaneLayout {

    private static final long serialVersionUID = 3010901954633232087L;

    public void layoutContainer(Container parent) {
        ComponentOrientation originalOrientation;

        // Dirty trick to get the vertical scrollbar to the left side of
        //  a scrollpane.
        originalOrientation = parent.getComponentOrientation();
        parent.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        super.layoutContainer(parent);
        parent.setComponentOrientation(originalOrientation);
    }
}
