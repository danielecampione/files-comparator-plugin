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

import java.awt.image.RGBImageFilter;

/**
 * Filter that adds transparency
 *
 * @author D. Campione
 */
class TransparentFilter extends RGBImageFilter {
    int percent;

    public TransparentFilter(int percent) {
        canFilterIndexColorModel = true;
        this.percent = percent;
    }

    public int filterRGB(int x, int y, int rgb) {
        int alpha;

        alpha = (rgb >> 24) & 0xff;
        alpha = Math.min(255, (int) (alpha * percent) / 100);

        return (rgb & 0xffffff) + (alpha << 24);
    }
}
