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
import java.util.HashMap;
import java.util.Map;

import org.files_comparator.diff.FilesComparatorDelta;
import org.files_comparator.settings.EditorSettings;
import org.files_comparator.settings.FilesComparatorSettings;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class RevisionUtil {

    private static Map<Color, Color> darker = new HashMap<Color, Color>();

    public static Color getColor(FilesComparatorDelta delta) {
        if (delta.isDelete()) {
            return getSettings().getDeletedColor();
        }

        if (delta.isChange()) {
            return getSettings().getChangedColor();
        }

        return getSettings().getAddedColor();
    }

    public static Color getDarkerColor(FilesComparatorDelta delta) {
        Color c;
        Color result;

        c = getColor(delta);

        result = darker.get(c);
        if (result == null) {
            result = c.darker();
            darker.put(c, result);
        }

        return result;
    }

    static private EditorSettings getSettings() {
        return FilesComparatorSettings.getInstance().getEditor();
    }
}
