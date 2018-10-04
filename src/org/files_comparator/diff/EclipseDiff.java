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

import org.eclipse.compare.rangedifferencer.IRangeComparator;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.rangedifferencer.RangeDifferencer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class EclipseDiff extends AbstractFilesComparatorDiffAlgorithm {

    public EclipseDiff() {
    }

    public FilesComparatorRevision diff(Object[] orig, Object[] rev)
            throws FilesComparatorException {
        RangeDifference[] differences;

        differences = RangeDifferencer.findDifferences(
                new RangeComparator(orig), new RangeComparator(rev));

        return buildRevision(differences, orig, rev);
    }

    private FilesComparatorRevision buildRevision(
            RangeDifference[] differences, Object[] orig, Object[] rev) {
        FilesComparatorRevision result;

        if (orig == null) {
            throw new IllegalArgumentException("original sequence is null");
        }

        if (rev == null) {
            throw new IllegalArgumentException("revised sequence is null");
        }

        result = new FilesComparatorRevision(orig, rev);
        for (RangeDifference rd : differences) {
            result.add(new FilesComparatorDelta(new FilesComparatorChunk(rd
                    .leftStart(), rd.leftLength()), new FilesComparatorChunk(rd
                    .rightStart(), rd.rightLength())));
        }

        return result;
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private class RangeComparator implements IRangeComparator {
        private Object[] objectArray;

        RangeComparator(Object[] objectArray) {
            this.objectArray = objectArray;
        }

        public int getRangeCount() {
            return objectArray.length;
        }

        public boolean rangesEqual(int thisIndex, IRangeComparator other,
                int otherIndex) {
            Object o1;
            Object o2;

            o1 = objectArray[thisIndex];
            o2 = ((RangeComparator) other).objectArray[otherIndex];

            if (o1 == o2) {
                return true;
            }

            if (o1 == null && o2 != null) {
                return false;
            }

            if (o1 != null && o2 == null) {
                return false;
            }

            return o1.equals(o2);
        }

        public boolean skipRangeComparison(int length, int maxLength,
                IRangeComparator other) {
            return false;
        }
    }
}
