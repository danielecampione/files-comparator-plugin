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

package org.files_comparator.ui.text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import open_teradata_viewer.plugin.FilesComparatorException;

import org.files_comparator.util.CharsetDetector;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FileDocument extends AbstractBufferDocument {

    // instance variables:
    private File file;
    private Charset charset;

    public FileDocument(File file) {
        this.file = file;

        try {
            setName(file.getCanonicalPath());
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
            setName(file.getName());
        }

        setShortName(file.getName());
    }

    public int getBufferSize() {
        return (int) file.length();
    }

    public Reader getReader() throws FilesComparatorException {
        BufferedInputStream bis;

        if (!file.isFile() || !file.canRead()) {
            throw new FilesComparatorException("Could not open file: "
                    + file.getAbsolutePath());
        }

        try {
            // Try to create a reader that has the right charset. If you use new
            // FileReader(file) you get a reader with the default charset 
            bis = new BufferedInputStream(new FileInputStream(file));
            charset = CharsetDetector.getInstance().getCharset(bis);
            return new BufferedReader(new InputStreamReader(bis, charset));
        } catch (Exception e) {
            throw new FilesComparatorException(
                    "Could not create FileReader for : " + file.getName(), e);
        }
    }

    protected Writer getWriter() throws FilesComparatorException {
        BufferedOutputStream bos;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            return new BufferedWriter(new OutputStreamWriter(bos, charset));
        } catch (IOException ioe) {
            throw new FilesComparatorException(
                    "Cannot create FileWriter for file: " + file.getName(), ioe);
        }
    }
}