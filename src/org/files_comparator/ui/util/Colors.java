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

import java.awt.Color;

import javax.swing.JList;
import javax.swing.JPanel;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Colors {

    public static final Color ADDED = new Color(180, 255, 180);
    public static final Color CHANGED = new Color(160, 200, 255);
    public static final Color CHANGED_LIGHTER = getChangedLighterColor(CHANGED);
    public static final Color DELETED = new Color(255, 160, 180);
    public static final Color DND_SELECTED_NEW = new Color(13, 143, 13);
    public static final Color DND_SELECTED_USED = new Color(238, 214, 128);

    public static Color getChangedLighterColor(Color changedColor) {
        Color c;

        c = changedColor;
        c = ColorUtil.brighter(c);
        c = ColorUtil.brighter(c);
        c = ColorUtil.lighter(c);
        c = ColorUtil.lighter(c);

        return c;
    }

    /** Get a highlighter that will match the current l&f.
     */
    public static Color getTableRowHighLighterColor() {
        Color color;

        color = getSelectionColor();
        color = ColorUtil.setSaturation(color, 0.05f);
        color = ColorUtil.setBrightness(color, 1.00f);

        return color;
    }

    public static Color getDarkLookAndFeelColor() {
        Color color;

        color = getSelectionColor();
        color = ColorUtil.setBrightness(color, 0.40f);

        return color;
    }

    public static Color getSelectionColor() {
        // DO NOT USE UIManager to get colors because it is not lookandfeel
        //   independent! (Learned it the hard way with Nimbus l&f)
        return new JList().getSelectionBackground();
    }

    public static Color getPanelBackground() {
        return new JPanel().getBackground();
    }
}
