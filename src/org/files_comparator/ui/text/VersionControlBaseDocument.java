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

package org.files_comparator.ui.text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import open_teradata_viewer.plugin.FilesComparatorException;

import org.files_comparator.util.CharsetDetector;
import org.files_comparator.util.node.FileNode;
import org.files_comparator.vc.BaseFile;
import org.files_comparator.vc.StatusResult;
import org.files_comparator.vc.VersionControlIF;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class VersionControlBaseDocument extends AbstractBufferDocument {

    // Instance variables:
    private VersionControlIF versionControl;
    private StatusResult.Entry entry;
    private FileNode fileNode;
    private File file;
    private BaseFile baseFile;
    private boolean baseFileInitialized;
    private Charset charset;

    public VersionControlBaseDocument(VersionControlIF versionControl,
            StatusResult.Entry entry, FileNode fileNode, File file) {
        this.versionControl = versionControl;
        this.entry = entry;
        this.fileNode = fileNode;
        this.file = file;

        try {
            setName(file.getCanonicalPath());
        } catch (Exception ex) {
            ex.printStackTrace();
            setName(file.getName());
        }

        setShortName(file.getName());
    }

    @Override
    public int getBufferSize() {
        if (useBaseFile()) {
            initBaseFile();
            return baseFile == null ? -1 : baseFile.getLength();
        } else {
            return fileNode.getDocument().getBufferSize();
        }
    }

    @Override
    public Reader getReader() throws FilesComparatorException {
        BufferedInputStream bais;

        if (useBaseFile()) {
            try {
                initBaseFile();
                bais = new BufferedInputStream(new ByteArrayInputStream(
                        baseFile.getByteArray()));
                charset = CharsetDetector.getInstance().getCharset(bais);
                return new BufferedReader(new InputStreamReader(bais, charset));
            } catch (Exception ex) {
                throw new FilesComparatorException(
                        "Could not create FileReader for : " + file.getName(),
                        ex);
            }
        } else {
            return fileNode.getDocument().getReader();
        }
    }

    @Override
    protected Writer getWriter() throws FilesComparatorException {
        return null;
    }

    private boolean useBaseFile() {
        switch (entry.getStatus()) {
            case modified :
            case removed :
            case missing :
                return true;

            default :
                return false;
        }
    }

    private void initBaseFile() {
        if (!baseFileInitialized) {
            baseFile = versionControl.getBaseFile(file);
            baseFileInitialized = true;
        }
    }

    @Override
    public boolean isReadonly() {
        return true;
    }
}
