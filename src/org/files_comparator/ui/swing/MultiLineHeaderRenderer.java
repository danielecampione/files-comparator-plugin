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

package org.files_comparator.ui.swing;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class MultiLineHeaderRenderer extends JLabel
        implements
            TableCellRenderer {

    public MultiLineHeaderRenderer() {
        LookAndFeel.installColorsAndFont(this, "TableHeader.background",
                "TableHeader.foreground", "TableHeader.font");
        LookAndFeel.installBorder(this, "TableHeader.cellBorder");

        /*
            renderer = getCellRenderer();
            if (renderer instanceof JLabel)
            {
              ((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
              setCellRenderer(renderer);
            }
            */

        setOpaque(false);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        String str;

        str = (value == null) ? "" : value.toString();

        setText(str);
        setHorizontalAlignment(JLabel.CENTER);

        return this;
    }

    /*
      public void paintComponent(Graphics g)
      {
        Rectangle r;
        int       x;
        int       y;

        super.paintComponent(g);

        if (icon != null)
        {
          r = getBounds();
          x = r.width - icon.getIconWidth();
          y = ((r.height - icon.getIconHeight()) / 2);

          icon.paintIcon(this, g, x, y);
        }
      }
      */

    private static final long serialVersionUID = 101783804743496189L;
}