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

package org.files_comparator.diff;

import open_teradata_viewer.plugin.FilesComparatorException;

import org.apache.commons.jrcs.diff.Chunk;
import org.apache.commons.jrcs.diff.Delta;
import org.apache.commons.jrcs.diff.Revision;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class MyersDiff extends AbstractFilesComparatorDiffAlgorithm {

    public MyersDiff() {
    }

    public FilesComparatorRevision diff(Object[] orig, Object[] rev)
            throws FilesComparatorException {
        org.apache.commons.jrcs.diff.myers.MyersDiff diff;
        Revision revision;

        try {
            diff = new org.apache.commons.jrcs.diff.myers.MyersDiff();
            diff.checkMaxTime(isMaxTimeChecked());
            revision = diff.diff(orig, rev);
        } catch (Exception e) {
            throw new FilesComparatorException("Diff failed [" + getClass()
                    + "]", e);
        }

        return buildRevision(revision, orig, rev);
    }

    private FilesComparatorRevision buildRevision(Revision revision,
            Object[] orig, Object[] rev) {
        FilesComparatorRevision result;
        Delta delta;
        Chunk original;
        Chunk revised;

        if (orig == null) {
            throw new IllegalArgumentException("original sequence is null");
        }

        if (rev == null) {
            throw new IllegalArgumentException("revised sequence is null");
        }

        result = new FilesComparatorRevision(orig, rev);
        for (int i = 0; i < revision.size(); i++) {
            delta = revision.getDelta(i);
            original = delta.getOriginal();
            revised = delta.getRevised();

            result.add(new FilesComparatorDelta(new FilesComparatorChunk(
                    original.anchor(), original.size()),
                    new FilesComparatorChunk(revised.anchor(), revised.size())));
        }

        return result;
    }
}