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

import javax.swing.event.DocumentEvent;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorDocumentEvent {

    private AbstractBufferDocument document;
    private DocumentEvent de;
    private int startLine;
    private int numberOfLines;

    public FilesComparatorDocumentEvent(AbstractBufferDocument document) {
        this.document = document;
    }

    public FilesComparatorDocumentEvent(AbstractBufferDocument document,
            DocumentEvent de) {
        this(document);

        this.de = de;
    }

    public AbstractBufferDocument getDocument() {
        return document;
    }

    public DocumentEvent getDocumentEvent() {
        return de;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }
}
