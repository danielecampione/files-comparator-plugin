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

package org.files_comparator.util.node;

import java.io.File;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

import org.files_comparator.ui.text.FileDocument;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FileNode extends FilesComparatorNode implements BufferNode {

    private File file;
    private long fileLastModified;
    private FileDocument document;
    private boolean exists;

    public FileNode(String name, File file) {
        super(name, !file.isDirectory());
        this.file = file;

        initialize();
    }

    public File getFile() {
        return file;
    }

    @Override
    public void resetContent() {
        document = null;
        initialize();
    }

    public boolean exists() {
        return exists;
    }

    public FileDocument getDocument() {
        if (document == null || isDocumentOutOfDate()) {
            initialize();
            if (exists()) {
                document = new FileDocument(file);
                fileLastModified = file.lastModified();
            }
        }

        return document;
    }

    @Override
    public long getSize() {
        return file.length();
    }

    private boolean isDocumentOutOfDate() {
        boolean outOfDate;

        if (file == null || !exists()) {
            return false;
        }

        outOfDate = file.lastModified() != fileLastModified;

        if (outOfDate) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(
                            "FileNode[" + this + "] is out of date ["
                                    + file.lastModified() + " != "
                                    + fileLastModified + "]");
        }
        return outOfDate;
    }

    private void initialize() {
        exists = file.exists();
    }

    public boolean isReadonly() {
        return false;
    }
}