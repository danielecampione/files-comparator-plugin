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

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import open_teradata_viewer.plugin.FilesComparatorException;

import org.files_comparator.ui.text.AbstractBufferDocument;
import org.files_comparator.util.Ignore;
import org.files_comparator.util.StopWatch;
import org.files_comparator.util.file.CompareUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorDiff {

    // Class variables:
    // Allocate a charBuffer once for performance. The charbuffer is used to
    //   store a 'line' without it's ignored characters. 
    static final private CharBuffer inputLine = CharBuffer.allocate(10000);
    static final private CharBuffer outputLine = CharBuffer.allocate(10000);
    // Instance variables:
    private List<FilesComparatorDiffAlgorithmIF> algorithms;

    public FilesComparatorDiff() {
        MyersDiff myersDiff;

        // Timing/Memory (msec/Mb):
        //                                             Myers  Eclipse GNU Hunt
        //  ================================================================================
        //  2 Totally different files  (116448 lines)  31317  1510    340 195
        //  2 Totally different files  (232896 lines)  170673 212     788 354
        //  2 Medium different files  (1778583 lines)  41     55      140 24679
        //  2 Medium different files (10673406 lines)  216    922     632 >300000
        //  2 Equal files             (1778583 lines)  32     55      133 24632
        //  2 Equal files            (10673406 lines)  121    227     581 >60000
        myersDiff = new MyersDiff();
        myersDiff.checkMaxTime(true);

        // MyersDiff is the fastest but can be very slow when 2 files
        //   are very different.
        algorithms = new ArrayList<FilesComparatorDiffAlgorithmIF>();
        //algorithms.add(myersDiff);

        // GNUDiff is a little bit slower than Myersdiff but performs way
        //   better if the files are very different.
        // Don't use it for now because of GPL
        //algorithms.add(new GNUDiff());

        // EclipseDiff looks like Myersdiff but is slower.
        // It performs much better if the files are totally different
        algorithms.add(new EclipseDiff());

        // HuntDiff (from netbeans) is very, very slow
        //algorithms.add(new HuntDiff());
    }

    public FilesComparatorRevision diff(List<String> a, List<String> b,
            Ignore ignore) throws FilesComparatorException {
        if (a == null) {
            a = Collections.emptyList();
        }
        if (b == null) {
            b = Collections.emptyList();
        }
        return diff(a.toArray(), b.toArray(), ignore);
    }

    public FilesComparatorRevision diff(Object[] a, Object[] b, Ignore ignore)
            throws FilesComparatorException {
        FilesComparatorRevision revision;
        StopWatch sp;
        boolean filtered;
        Object[] org;
        Object[] rev;
        long filteredTime;

        org = a;
        rev = b;

        if (org == null) {
            org = new Object[]{};
        }
        if (rev == null) {
            rev = new Object[]{};
        }

        if (org instanceof AbstractBufferDocument.Line[]
                && rev instanceof AbstractBufferDocument.Line[]) {
            filtered = true;
        } else {
            filtered = false;
        }

        sp = new StopWatch();
        sp.start();

        if (filtered) {
            org = filter(ignore, org);
            rev = filter(ignore, rev);
        }

        filteredTime = sp.getElapsedTime();

        for (FilesComparatorDiffAlgorithmIF algorithm : algorithms) {
            try {
                revision = algorithm.diff(org, rev);
                revision.setIgnore(ignore);
                revision.update(a, b);
                //revision.filter();
                if (filtered) {
                    adjustRevision(revision, a, (FilesComparatorString[]) org,
                            b, (FilesComparatorString[]) rev);
                }

                if (a.length > 1000) {
                    ApplicationFrame.getInstance().changeLog
                            .append("diff took " + sp.getElapsedTime()
                                    + " msec. [filter=" + filteredTime
                                    + " msec][" + algorithm.getClass() + "]\n");
                }

                return revision;
            } catch (FilesComparatorException ex) {
                if (ex.getCause() instanceof MaxTimeExceededException) {
                    ApplicationFrame.getInstance().changeLog.append(
                            "Time exceeded for " + algorithm.getClass()
                                    + ": try next algorithm.\n",
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                } else {
                    throw ex;
                }
            }
        }

        return null;
    }

    private void adjustRevision(FilesComparatorRevision revision,
            Object[] orgArray, FilesComparatorString[] orgArrayFiltered,
            Object[] revArray, FilesComparatorString[] revArrayFiltered) {
        FilesComparatorChunk chunk;
        int anchor;
        int size;
        int index;

        for (FilesComparatorDelta delta : revision.getDeltas()) {
            chunk = delta.getOriginal();
            // ApplicationFrame.getInstance().changeLog.append("  original=" + chunk);
            index = chunk.getAnchor();
            if (index < orgArrayFiltered.length) {
                anchor = orgArrayFiltered[index].lineNumber;
            } else {
                anchor = orgArray.length;
            }

            size = chunk.getSize();
            if (size > 0) {
                index += chunk.getSize() - 1;
                if (index < orgArrayFiltered.length) {
                    size = orgArrayFiltered[index].lineNumber - anchor + 1;
                }
                /*
                   index += chunk.getSize();
                   if (index < orgArrayFiltered.length)
                   {
                     size = orgArrayFiltered[index].lineNumber - anchor;
                   }
                 */
            }
            chunk.setAnchor(anchor);
            chunk.setSize(size);
            // ApplicationFrame.getInstance().changeLog.append(" => " + chunk + "\n");

            chunk = delta.getRevised();
            // ApplicationFrame.getInstance().changeLog.append("  revised=" + chunk);
            index = chunk.getAnchor();
            if (index < revArrayFiltered.length) {
                // ApplicationFrame.getInstance().changeLog.append(" [index=" + index + ", text="
                //  + revArrayFiltered[index].s + "]");
                anchor = revArrayFiltered[index].lineNumber;
            } else {
                anchor = revArray.length;
            }
            size = chunk.getSize();
            if (size > 0) {
                index += chunk.getSize() - 1;
                if (index < revArrayFiltered.length) {
                    size = revArrayFiltered[index].lineNumber - anchor + 1;
                }
                /*
                   index += chunk.getSize();
                   if (index < revArrayFiltered.length)
                   {
                     size = revArrayFiltered[index].lineNumber - anchor;
                   }
                 */
            }
            chunk.setAnchor(anchor);
            chunk.setSize(size);
            // ApplicationFrame.getInstance().changeLog.append(" => " + chunk + "\n");
        }
    }

    private FilesComparatorString[] filter(Ignore ignore, Object[] array) {
        List<FilesComparatorString> result;
        FilesComparatorString filesComparatorString;
        int lineNumber;

        synchronized (inputLine) {
            // ApplicationFrame.getInstance().changeLog.append("> start\n");
            result = new ArrayList<FilesComparatorString>(array.length);
            lineNumber = -1;
            for (Object o : array) {
                lineNumber++;

                inputLine.clear();
                inputLine.put(o.toString());
                CompareUtil.removeIgnoredChars(inputLine, ignore, outputLine);
                if (outputLine.remaining() == 0) {
                    continue;
                }

                filesComparatorString = new FilesComparatorString();
                filesComparatorString.s = outputLine.toString();
                filesComparatorString.lineNumber = lineNumber;
                result.add(filesComparatorString);

                // ApplicationFrame.getInstance().changeLog.append("  " + filesComparatorString + "\n");
            }
        }

        return result.toArray(new FilesComparatorString[result.size()]);
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class FilesComparatorString {
        String s;
        int lineNumber;

        @Override
        public int hashCode() {
            return s.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return s.equals(((FilesComparatorString) o).s);
        }

        @Override
        public String toString() {
            return "[" + lineNumber + "] " + s;
        }
    }
}
