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

package org.files_comparator.util.conf;

import java.io.File;
import java.io.IOException;

import org.files_comparator.util.prefs.Preference;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ConfigurationPreference extends Preference {

    // Class variables:
    private static String FILENAME = "FILENAME";

    // Instance variables:
    @SuppressWarnings("rawtypes")
    private Class clazz;
    private File file;

    @SuppressWarnings("rawtypes")
    public ConfigurationPreference(Class clazz) {
        super("Configuration-" + clazz);

        this.clazz = clazz;

        init();
    }

    private void init() {
        String fileName;
        String defaultFileName;
        int index;

        defaultFileName = clazz.getName();
        index = defaultFileName.lastIndexOf(".");
        if (index != -1) {
            defaultFileName = defaultFileName.substring(index + 1);
        }
        try {
            defaultFileName = new File(System.getProperty("user.home"),
                    defaultFileName).getCanonicalPath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        defaultFileName += ".xml";

        fileName = getString(FILENAME, defaultFileName);

        file = new File(fileName);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        save();
    }

    public void save() {
        try {
            putString(FILENAME, file.getCanonicalPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
