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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JLabel;

import org.files_comparator.ui.util.Colors;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GradientLabel extends JLabel {

    private static final long serialVersionUID = -4478477176541931104L;

    private Color fromColor;
    private Color toColor;

    public GradientLabel(String text) {
        super(text);

        initialize();
    }

    private void initialize() {
        setOpaque(false);
        setGradientColor(Colors.getDarkLookAndFeelColor());
        setForeground(Color.white);
    }

    public GradientLabel() {
        super();

        initialize();
    }

    public void setGradientColor(Color fromColor) {
        setGradientColor(
                fromColor,
                new Color(fromColor.getRed(), fromColor.getGreen(), fromColor
                        .getBlue(), 0));
    }

    public void setGradientColor(Color fromColor, Color toColor) {
        this.fromColor = fromColor;
        this.toColor = toColor;
    }

    public Color getFromColor() {
        return fromColor;
    }

    public Color getToColor() {
        return toColor;
    }

    public void paint(Graphics g) {
        Rectangle r;
        GradientPaint paint;
        Graphics2D g2;

        g2 = (Graphics2D) g;

        r = getBounds();

        paint = new GradientPaint(0, 0, fromColor, (int) (r.width / 1.10),
                r.height, toColor);

        g2.setPaint(paint);
        g2.fillRect(0, 0, r.width, r.height);

        super.paint(g);
    }
}
