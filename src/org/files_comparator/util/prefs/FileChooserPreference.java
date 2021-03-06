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

package org.files_comparator.util.prefs;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FileChooserPreference extends Preference {

    // Class variables:
    private static String FILE = "FILE";

    // Instance variables:
    private JFileChooser target;

    public FileChooserPreference(String preferenceName, JFileChooser target) {
        super("FileChooser-" + preferenceName);

        this.target = target;

        init();
    }

    private void init() {
        String fileName;

        fileName = getString(FILE, null);
        if (fileName != null) {
            target.setCurrentDirectory(new File(fileName));
        }
    }

    public void save() {
        String fileName;
        File file;

        file = target.getSelectedFile();
        if (file != null) {
            try {
                fileName = file.getCanonicalPath();
                putString(FILE, fileName);
            } catch (IOException ioe) {
                ExceptionDialog.hideException(ioe);
            }
        }
    }
}