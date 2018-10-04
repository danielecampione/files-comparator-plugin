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

package org.files_comparator.util;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

import org.files_comparator.diff.FilesComparatorDelta;
import org.files_comparator.diff.FilesComparatorRevision;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DiffUtil {

    public static boolean debug = false;

    public static int getRevisedLine(FilesComparatorRevision revision,
            int originalLine) {
        FilesComparatorDelta delta;
        int originalAnchor;
        int originalSize;
        int revisedAnchor;
        int revisedSize;
        int revisedLine;

        if (revision == null) {
            return 0;
        }

        revisedLine = originalLine;

        delta = findOriginalDelta(revision, originalLine);
        if (delta != null) {
            originalAnchor = delta.getOriginal().getAnchor();
            originalSize = delta.getOriginal().getSize();
            revisedAnchor = delta.getRevised().getAnchor();
            revisedSize = delta.getRevised().getSize();

            if (originalLine - originalAnchor < originalSize) {
                revisedLine = revisedAnchor;
            } else {
                revisedLine = revisedAnchor + revisedSize - originalSize
                        + (originalLine - originalAnchor);
            }
        } else {
            originalAnchor = 0;
            originalSize = 0;
            revisedAnchor = 0;
            revisedSize = 0;
        }

        if (debug) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .print(String.format("%03d-%02d, %03d-%02d == ",
                            originalAnchor, originalSize, revisedAnchor,
                            revisedSize));
        }

        return revisedLine;
    }

    public static int getOriginalLine(FilesComparatorRevision revision,
            int revisedLine) {
        FilesComparatorDelta delta;
        int originalAnchor;
        int originalSize;
        int revisedAnchor;
        int revisedSize;
        int originalLine;

        originalLine = revisedLine;

        delta = findRevisedDelta(revision, revisedLine);
        if (delta != null) {
            originalAnchor = delta.getOriginal().getAnchor();
            originalSize = delta.getOriginal().getSize();
            revisedAnchor = delta.getRevised().getAnchor();
            revisedSize = delta.getRevised().getSize();

            if (revisedLine - revisedAnchor < revisedSize) {
                originalLine = originalAnchor;
            } else {
                originalLine = originalAnchor + originalSize - revisedSize
                        + (revisedLine - revisedAnchor);
            }
        } else {
            originalAnchor = 0;
            originalSize = 0;
            revisedAnchor = 0;
            revisedSize = 0;
        }

        if (debug) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .print(String.format("%03d-%02d, %03d-%02d == ",
                            originalAnchor, originalSize, revisedAnchor,
                            revisedSize));
        }

        return originalLine;
    }

    private static FilesComparatorDelta findOriginalDelta(
            FilesComparatorRevision revision, int line) {
        return findDelta(revision, line, true);
    }

    private static FilesComparatorDelta findRevisedDelta(
            FilesComparatorRevision revision, int line) {
        return findDelta(revision, line, false);
    }

    private static FilesComparatorDelta findDelta(
            FilesComparatorRevision revision, int line, boolean originalDelta) {
        FilesComparatorDelta previousDelta;
        int anchor;

        if (revision == null) {
            return null;
        }

        previousDelta = null;
        for (FilesComparatorDelta delta : revision.getDeltas()) {
            if (originalDelta) {
                anchor = delta.getOriginal().getAnchor();
            } else {
                anchor = delta.getRevised().getAnchor();
            }

            if (anchor > line) {
                break;
            }

            previousDelta = delta;
        }

        return previousDelta;
    }
}