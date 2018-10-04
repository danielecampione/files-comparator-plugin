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

package org.files_comparator.diff;

import open_teradata_viewer.plugin.FilesComparatorException;

import org.gnu.diff.Diff;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GNUDiff extends AbstractFilesComparatorDiffAlgorithm {

    public GNUDiff() {
    }

    public FilesComparatorRevision diff(Object[] orig, Object[] rev)
            throws FilesComparatorException {
        Diff diff;
        Diff.change change;

        try {
            diff = new Diff(orig, rev);
            change = diff.diff_2();
        } catch (Exception e) {
            throw new FilesComparatorException("Diff failed [" + getClass()
                    + "]", e);
        }

        return buildRevision(change, orig, rev);
    }

    private FilesComparatorRevision buildRevision(Diff.change change,
            Object[] orig, Object[] rev) {
        FilesComparatorRevision result;

        if (orig == null) {
            throw new IllegalArgumentException("original sequence is null");
        }

        if (rev == null) {
            throw new IllegalArgumentException("revised sequence is null");
        }

        result = new FilesComparatorRevision(orig, rev);
        while (change != null) {
            result.add(new FilesComparatorDelta(new FilesComparatorChunk(
                    change.line0, change.deleted), new FilesComparatorChunk(
                    change.line1, change.inserted)));

            change = change.link;
        }

        return result;
    }
}