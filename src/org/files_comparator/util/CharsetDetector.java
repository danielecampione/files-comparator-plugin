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

package org.files_comparator.util;

import java.io.BufferedInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.files_comparator.settings.EditorSettings;
import org.files_comparator.settings.FilesComparatorSettings;

import com.ibm.icu.text.CharsetMatch;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class CharsetDetector {

    // Class variables:
    // Singleton:
    private static CharsetDetector instance = new CharsetDetector();

    // Instance variables:
    private Map<String, Charset> charsetMap;

    private CharsetDetector() {
        charsetMap = Charset.availableCharsets();
    }

    public static CharsetDetector getInstance() {
        return instance;
    }

    public Charset getCharset(BufferedInputStream bis) {
        Charset charset;
        EditorSettings settings;

        settings = FilesComparatorSettings.getInstance().getEditor();

        charset = null;
        if (settings.getDefaultFileEncodingEnabled()) {
            charset = getDefaultCharset();
        } else if (settings.getSpecificFileEncodingEnabled()) {
            charset = charsetMap.get(settings.getSpecificFileEncodingName());
        } else if (settings.getDetectFileEncodingEnabled()) {
            charset = detectCharset(bis);
        }

        if (charset == null) {
            charset = getDefaultCharset();
        }

        return charset;
    }

    private Charset detectCharset(BufferedInputStream bis) {
        try {
            com.ibm.icu.text.CharsetDetector detector;
            CharsetMatch match;
            Charset foundCharset;

            detector = new com.ibm.icu.text.CharsetDetector();
            detector.setText(bis);

            match = detector.detect();
            if (match != null) {
                foundCharset = charsetMap.get(match.getName());
                if (foundCharset != null) {
                    return foundCharset;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Charset getDefaultCharset() {
        return Charset.defaultCharset();
    }

    public List<String> getCharsetNameList() {
        List<String> charsetNameList;

        charsetNameList = new ArrayList<String>();
        for (String name : charsetMap.keySet()) {
            charsetNameList.add(name);
        }

        return charsetNameList;
    }
}
