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

package org.files_comparator.ui.util;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

/**
 * Filter that adds transparency
 *
 * @author D. Campione
 */
class BrightnessFilter extends RGBImageFilter {
    float percent;

    public BrightnessFilter(float percent) {
        canFilterIndexColorModel = true;
        this.percent = percent;
    }

    public int filterRGB(int x, int y, int rgb) {
        float[] hsb;
        int r;
        int g;
        int b;
        int a;

        b = rgb & 0xFF;
        g = (rgb >> 8) & 0xFF;
        r = (rgb >> 16) & 0xFF;
        a = (rgb >> 24) & 0xFF;

        if (a == 255) {
            hsb = Color.RGBtoHSB(r, g, b, null);
            hsb[2] = hsb[2] + percent;
            if (hsb[2] > 1.0) {
                hsb[2] = 1.0f;
            }

            if (hsb[2] < 0.0) {
                hsb[2] = 0.0f;
            }

            rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            rgb |= ((a & 0xFF) << 24);
        }

        return rgb;
    }
}
