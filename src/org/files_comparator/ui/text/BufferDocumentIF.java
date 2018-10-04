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

import java.io.Reader;

import javax.swing.text.PlainDocument;

import open_teradata_viewer.plugin.FilesComparatorException;

import org.files_comparator.vc.BlameIF;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface BufferDocumentIF {

    // class variables:
    public static String ORIGINAL = "Original";
    public static String REVISED = "Revised";

    public String getName();

    public String getShortName();

    public void addChangeListener(BufferDocumentChangeListenerIF listener);

    public void removeChangeListener(BufferDocumentChangeListenerIF listener);

    public boolean isChanged();

    public PlainDocument getDocument();

    public BlameIF getVersionControlBlame();

    public AbstractBufferDocument.Line[] getLines();

    public String getLineText(int lineNumber);

    public int getNumberOfLines();

    public int getOffsetForLine(int lineNumber);

    public int getLineForOffset(int offset);

    public void read() throws FilesComparatorException;

    public void write() throws FilesComparatorException;

    public void print();

    public Reader getReader() throws FilesComparatorException;

    public boolean isReadonly();
}
