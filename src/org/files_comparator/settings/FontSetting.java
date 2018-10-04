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

package org.files_comparator.settings;

import java.awt.Font;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.files_comparator.util.StringUtil;
import org.files_comparator.util.conf.AbstractConfigurationElement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class FontSetting extends AbstractConfigurationElement {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private int style;
    @XmlAttribute
    private int size;

    private Font font;

    public FontSetting() {
    }

    public FontSetting(Font font) {
        this.name = font.getName();
        this.style = font.getStyle();
        this.size = font.getSize();

        this.font = font;
    }

    public Font getFont() {
        if (font == null && !StringUtil.isEmpty(name)) {
            font = new Font(name, style, size);
        }

        return font;
    }
}
