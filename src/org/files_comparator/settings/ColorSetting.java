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

package org.files_comparator.settings;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.files_comparator.util.conf.AbstractConfigurationElement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ColorSetting extends AbstractConfigurationElement {

    @XmlAttribute
    private int b = -1;
    @XmlAttribute
    private int g = -1;
    @XmlAttribute
    private int r = -1;
    private Color color;

    public ColorSetting() {
    }

    public ColorSetting(Color color) {
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();

        this.color = color;
    }

    public Color getColor() {
        if (r == -1 || g == -1 || b == -1) {
            return null;
        }

        if (color == null) {
            color = new Color(r, g, b);
        }

        return color;
    }
}
