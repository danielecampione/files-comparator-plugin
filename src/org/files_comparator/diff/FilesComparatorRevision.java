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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

import org.files_comparator.util.DiffUtil;
import org.files_comparator.util.Ignore;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorRevision {

    // Class variables:
    private static boolean incrementalUpdateActivated = false;

    // Instance variables:
    private Object[] orgArray;
    private Object[] revArray;
    private LinkedList<FilesComparatorDelta> deltaList;
    private Ignore ignore;

    public FilesComparatorRevision(Object[] orgArray, Object[] revArray) {
        this.orgArray = orgArray;
        this.revArray = revArray;

        deltaList = new LinkedList<FilesComparatorDelta>();

        ignore = Ignore.NULL_IGNORE;
    }

    public void setIgnore(Ignore ignore) {
        this.ignore = ignore;
    }

    public void add(FilesComparatorDelta delta) {
        deltaList.add(delta);
        delta.setRevision(this);
    }

    public List<FilesComparatorDelta> getDeltas() {
        return deltaList;
    }

    public void update(Object[] oArray, Object[] rArray) {
        this.orgArray = oArray;
        this.revArray = rArray;
    }

    /** The arrays have changed! Try to change the delta's incrementally.
     * This solves a performance issue while editing one of the array's.
     */
    public boolean update(Object[] oArray, Object[] rArray, boolean original,
            int startLine, int numberOfLines) {
        update(oArray, rArray);
        return incrementalUpdate(original, startLine, numberOfLines);
    }

    private boolean incrementalUpdate(boolean original, int startLine,
            int numberOfLines) {
        FilesComparatorChunk chunk;
        List<FilesComparatorDelta> deltaListToRemove;
        List<FilesComparatorChunk> chunkListToChange;
        int endLine;
        int orgStartLine;
        int orgEndLine;
        int revStartLine;
        int revEndLine;
        FilesComparatorRevision deltaRevision;
        int index;
        Object[] orgArrayDelta;
        Object[] revArrayDelta;
        FilesComparatorDelta firstDelta;
        int length;

        // It is not yet prroduction ready !
        if (!incrementalUpdateActivated) {
            return false;
        }

        ApplicationFrame.getInstance().changeLog.append((original
                ? "left"
                : "right")
                + " changed starting at line "
                + startLine
                + " #"
                + numberOfLines + "\n");

        if (original) {
            orgStartLine = startLine;
            orgEndLine = startLine + (numberOfLines < 0 ? 0 : numberOfLines)
                    + 1;
            revStartLine = DiffUtil.getRevisedLine(this, startLine);
            revEndLine = DiffUtil.getRevisedLine(this, startLine
                    + (numberOfLines > 0 ? 0 : -numberOfLines)) + 1;
        } else {
            revStartLine = startLine;
            revEndLine = startLine + (numberOfLines < 0 ? 0 : numberOfLines)
                    + 1;
            orgStartLine = DiffUtil.getOriginalLine(this, startLine);
            orgEndLine = DiffUtil.getOriginalLine(this, startLine
                    + (numberOfLines > 0 ? 0 : -numberOfLines)) + 1;
        }

        ApplicationFrame.getInstance().changeLog.append("orgStartLine="
                + orgStartLine + "\n");
        ApplicationFrame.getInstance().changeLog.append("orgEndLine  ="
                + orgEndLine + "\n");
        ApplicationFrame.getInstance().changeLog.append("revStartLine="
                + revStartLine + "\n");
        ApplicationFrame.getInstance().changeLog.append("revEndLine  ="
                + revEndLine + "\n");

        deltaListToRemove = new ArrayList<FilesComparatorDelta>();
        chunkListToChange = new ArrayList<FilesComparatorChunk>();

        // Find the delta's of this change!
        endLine = startLine + Math.abs(numberOfLines);
        for (FilesComparatorDelta delta : deltaList) {
            chunk = original ? delta.getOriginal() : delta.getRevised();

            // The change is above this Chunk! It will not change!
            if (endLine < chunk.getAnchor() - 5) {
                continue;
            }

            // The change is below this chunk! The anchor of the chunk will be changed!
            if (startLine > chunk.getAnchor() + chunk.getSize() + 5) {
                // No need to change chunks if the numberoflines haven't changed.
                if (numberOfLines != 0) {
                    chunkListToChange.add(chunk);
                }
                continue;
            }

            // This chunk is affected by the change. It will eventually be removed.
            //   The lines that are affected will be compared and they will insert
            //   new delta's if necessary.
            deltaListToRemove.add(delta);

            // Revise the start and end if there are overlapping chunks.
            chunk = delta.getOriginal();
            if (chunk.getAnchor() < orgStartLine) {
                orgStartLine = chunk.getAnchor();
            }
            if (chunk.getAnchor() + chunk.getSize() > orgEndLine) {
                orgEndLine = chunk.getAnchor() + chunk.getSize();
            }

            chunk = delta.getRevised();
            if (chunk.getAnchor() < revStartLine) {
                revStartLine = chunk.getAnchor();
            }
            if (chunk.getAnchor() + chunk.getSize() > revEndLine) {
                revEndLine = chunk.getAnchor() + chunk.getSize();
            }
        }

        orgStartLine = orgStartLine < 0 ? 0 : orgStartLine;
        revStartLine = revStartLine < 0 ? 0 : revStartLine;

        // Check with 'max' if we are dealing with the end of the file.
        length = Math.min(orgArray.length, orgEndLine) - orgStartLine;
        orgArrayDelta = new Object[length];
        System.arraycopy(orgArray, orgStartLine, orgArrayDelta, 0,
                orgArrayDelta.length);

        length = Math.min(revArray.length, revEndLine) - revStartLine;
        revArrayDelta = new Object[length];
        System.arraycopy(revArray, revStartLine, revArrayDelta, 0,
                revArrayDelta.length);

        try {
            for (int i = 0; i < orgArrayDelta.length; i++) {
                ApplicationFrame.getInstance().changeLog.append("  org[" + i
                        + "]:" + orgArrayDelta[i] + "\n");
            }
            for (int i = 0; i < revArrayDelta.length; i++) {
                ApplicationFrame.getInstance().changeLog.append("  rev[" + i
                        + "]:" + revArrayDelta[i] + "\n");
            }
            deltaRevision = new FilesComparatorDiff().diff(orgArrayDelta,
                    revArrayDelta, ignore);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        // OK, Make the changes now
        if (!deltaListToRemove.isEmpty()) {
            for (FilesComparatorDelta delta : deltaListToRemove) {
                deltaList.remove(delta);
            }
        }

        for (FilesComparatorChunk c : chunkListToChange) {
            c.setAnchor(c.getAnchor() + numberOfLines);
        }

        // Prepare the diff's to be copied into this revision.
        for (FilesComparatorDelta delta : deltaRevision.deltaList) {
            chunk = delta.getOriginal();
            chunk.setAnchor(chunk.getAnchor() + orgStartLine);

            chunk = delta.getRevised();
            chunk.setAnchor(chunk.getAnchor() + revStartLine);
        }

        // Find insertion index point
        if (deltaRevision.deltaList.size() > 0) {
            firstDelta = deltaRevision.deltaList.get(0);
            index = 0;
            for (FilesComparatorDelta delta : deltaList) {
                if (delta.getOriginal().getAnchor() > firstDelta.getOriginal()
                        .getAnchor()) {
                    break;
                }

                index++;
            }

            for (FilesComparatorDelta diffDelta : deltaRevision.deltaList) {
                diffDelta.setRevision(this);
                deltaList.add(index, diffDelta);
                index++;
            }
        }

        return true;
    }

    @SuppressWarnings("unused")
    private void insert(FilesComparatorDelta delta) {
        int index;
        int anchor;

        index = 0;
        anchor = delta.getOriginal().getAnchor();
        for (FilesComparatorDelta d : deltaList) {
            if (d.getOriginal().getAnchor() > anchor) {
                deltaList.add(index, delta);
                return;
            }

            index++;
        }

        deltaList.add(delta);
    }

    @SuppressWarnings("unused")
    private FilesComparatorDelta findDelta(boolean original, int anchor,
            int size) {
        FilesComparatorChunk chunk;

        size = size == 0 ? 1 : size;
        for (FilesComparatorDelta delta : deltaList) {
            chunk = original ? delta.getOriginal() : delta.getRevised();
            if (anchor >= chunk.getAnchor()
                    && anchor <= chunk.getAnchor() + chunk.getSize()) {
                return delta;
            }

            if (anchor + size >= chunk.getAnchor()
                    && anchor + size <= chunk.getAnchor() + chunk.getSize()) {
                return delta;
            }
        }

        return null;
    }

    public int getOrgSize() {
        return orgArray == null ? 0 : orgArray.length;
    }

    public int getRevSize() {
        return revArray == null ? 0 : revArray.length;
    }

    public String getOriginalString(FilesComparatorChunk chunk) {
        return getObjects(orgArray, chunk);
    }

    public String getRevisedString(FilesComparatorChunk chunk) {
        return getObjects(revArray, chunk);
    }

    @SuppressWarnings("unused")
    private String getObjects(Object[] objects, FilesComparatorChunk chunk) {
        Object[] result;
        StringBuffer sb;
        int end;

        if (chunk.getSize() <= 0) {
            return "";
        }

        sb = new StringBuffer();
        end = chunk.getAnchor() + chunk.getSize();
        for (int offset = chunk.getAnchor(); offset < end; offset++) {
            sb.append(objects[offset].toString());
        }

        return sb.toString();
    }
}
