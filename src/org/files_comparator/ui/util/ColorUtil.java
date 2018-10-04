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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ColorUtil {

    private ColorUtil() {
    }

    public static Color lighter(Color color) {
        return lighter(color, -0.10f);
    }

    public static Color brighter(Color color) {
        return brighter(color, 0.05f);
    }

    public static Color darker(Color color) {
        return brighter(color, -0.05f);
    }

    /** Create a brighter color by changing the b component of a
     *    hsb-color (b=brightness, h=hue, s=saturation)
     */
    public static Color brighter(Color color, float factor) {
        float[] hsbvals;

        hsbvals = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(),
                hsbvals);

        return setBrightness(color, hsbvals[2] + factor);
    }

    /** Get the brightness of a color. 
     *    The H from HSB!
     */
    public static float getBrightness(Color color) {
        float[] hsbvals;

        hsbvals = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(),
                hsbvals);

        return hsbvals[2];
    }

    /** Create a brighter color by changing the b component of a
     *    hsb-color (b=brightness, h=hue, s=saturation)
     */
    public static Color lighter(Color color, float factor) {
        float[] hsbvals;

        hsbvals = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(),
                hsbvals);

        return setSaturation(color, hsbvals[1] + factor);
    }

    public static Color setSaturation(Color color, float saturation) {
        float[] hsbvals;

        if (saturation < 0.0f || saturation > 1.0f) {
            return color;
        }

        hsbvals = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(),
                hsbvals);
        hsbvals[1] = saturation;

        color = new Color(Color.HSBtoRGB(hsbvals[0], hsbvals[1], hsbvals[2]));

        return color;
    }

    public static Color setBrightness(Color color, float brightness) {
        float[] hsbvals;

        hsbvals = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(),
                hsbvals);
        hsbvals[2] = brightness;
        hsbvals[2] = Math.min(hsbvals[2], 1.0f);
        hsbvals[2] = Math.max(hsbvals[2], 0.0f);

        color = new Color(Color.HSBtoRGB(hsbvals[0], hsbvals[1], hsbvals[2]));

        return color;
    }
}
