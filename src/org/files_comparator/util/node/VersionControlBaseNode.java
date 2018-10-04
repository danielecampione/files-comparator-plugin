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

import org.files_comparator.ui.text.VersionControlBaseDocument;
import org.files_comparator.vc.StatusResult;
import org.files_comparator.vc.VersionControlIF;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class VersionControlBaseNode extends FilesComparatorNode
        implements
            BufferNode {

    private VersionControlIF versionControl;
    private StatusResult.Entry entry;
    private FileNode fileNode;
    private File file;
    private VersionControlBaseDocument document;

    public VersionControlBaseNode(VersionControlIF versionControl,
            StatusResult.Entry entry, FileNode fileNode, File file) {
        super(entry.getName(), !file.isDirectory());

        this.versionControl = versionControl;
        this.entry = entry;
        this.file = file;
        this.fileNode = fileNode;
    }

    public File getFile() {
        return file;
    }

    public StatusResult.Entry getEntry() {
        return entry;
    }

    @Override
    public void resetContent() {
        document = null;
        initialize();
    }

    public boolean exists() {
        return true;
    }

    public VersionControlBaseDocument getDocument() {
        if (document == null) {
            document = new VersionControlBaseDocument(versionControl, entry,
                    fileNode, file);
        }

        return document;
    }

    @Override
    public long getSize() {
        return getDocument().getBufferSize();
    }

    private void initialize() {
    }
}
