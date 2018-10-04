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

package org.files_comparator.ui.util;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ToolBarSeparator extends JComponent {

    private static final long serialVersionUID = -7081222409711573392L;

    public ToolBarSeparator() {
        this(10, 10);
    }

    public ToolBarSeparator(int width, int height) {
        Dimension dimension;

        dimension = new Dimension(width, height);

        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
    }

    protected void paintComponent(Graphics g) {
        Dimension d;
        int h;
        int x;

        d = getSize();

        x = d.width / 2;
        h = d.height / 4;

        g.setColor(getBackground().darker());
        g.drawLine(x, h, x, d.height - h - 1);
        g.drawLine(x, h - 1, x + 1, h - 1);

        g.setColor(getBackground().brighter());
        g.drawLine(x + 1, h, x + 1, d.height - h - 1);
        g.drawLine(x, d.height - h, x + 1, d.height - h);
    }
}
