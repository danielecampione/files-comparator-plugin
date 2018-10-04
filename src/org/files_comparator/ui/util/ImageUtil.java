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

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.util.ResourceLoader;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ImageUtil {

    public static synchronized ImageIcon getSmallImageIcon(String iconName) {
        return getImageIcon("16x16/" + iconName + "-16");
    }

    public static synchronized ImageIcon getLargeImageIcon(String iconName) {
        return getImageIcon("32x32/" + iconName);
    }

    public static synchronized ImageIcon getImageIcon(String iconName) {
        URL url;

        iconName = "icons/" + iconName + ".png";

        url = ResourceLoader.getResource(iconName);
        if (url == null) {
            return null;
        }

        return new ImageIcon(url);
    }

    public static ImageIcon createDarkerIcon(ImageIcon icon) {
        return createDarkerIcon(icon, -0.10f);
    }

    /** Create a x% Transparent icon */
    public static ImageIcon createDarkerIcon(ImageIcon icon, float percentage) {
        return createIcon(icon, new BrightnessFilter(percentage));
    }

    /** Create a 20% Transparent icon */
    public static ImageIcon createTransparentIcon(ImageIcon icon) {
        return createTransparentIcon(icon, 20);
    }

    /** Create a x% Transparent icon */
    public static ImageIcon createTransparentIcon(ImageIcon icon, int percentage) {
        return createIcon(icon, new TransparentFilter(percentage));
    }

    /** Create a new icon which is filtered by some ImageFilter */
    private static synchronized ImageIcon createIcon(ImageIcon icon,
            ImageFilter filter) {
        ImageProducer ip;
        Image image;
        MediaTracker tracker;

        if (icon == null) {
            return null;
        }

        ip = new FilteredImageSource(icon.getImage().getSource(), filter);
        image = Toolkit.getDefaultToolkit().createImage(ip);

        tracker = new MediaTracker(new JPanel());
        tracker.addImage(image, 1);
        try {
            tracker.waitForID(1);
        } catch (InterruptedException ie) {
            ExceptionDialog.hideException(ie);
            return null;
        }

        return new ImageIcon(image);
    }
}