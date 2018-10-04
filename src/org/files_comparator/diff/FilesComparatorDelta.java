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

import java.util.List;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.util.Ignore;
import org.files_comparator.util.TokenizerFactory;
import org.files_comparator.util.WordTokenizer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorDelta {

    // Class variables:
    /**
     * 
     * 
     * @author D. Campione
     *
     */
    enum Type {

        ADD, DELETE, CHANGE;
    }

    private static boolean debug = false;

    // Instance variables:
    private FilesComparatorChunk original;
    private FilesComparatorChunk revised;
    private Type type;
    private FilesComparatorRevision revision;
    private FilesComparatorRevision changeRevision;

    public FilesComparatorDelta(FilesComparatorChunk original,
            FilesComparatorChunk revised) {
        this.original = original;
        this.revised = revised;

        initType();
    }

    void setRevision(FilesComparatorRevision revision) {
        this.revision = revision;
    }

    public FilesComparatorChunk getOriginal() {
        return original;
    }

    public FilesComparatorChunk getRevised() {
        return revised;
    }

    public boolean isAdd() {
        return type == Type.ADD;
    }

    public boolean isDelete() {
        return type == Type.DELETE;
    }

    public boolean isChange() {
        return type == Type.CHANGE;
    }

    public void invalidateChangeRevision() {
        changeRevision = null;
    }

    public FilesComparatorRevision getChangeRevision() {
        if (changeRevision == null) {
            changeRevision = createChangeRevision();
        }

        return changeRevision;
    }

    private FilesComparatorRevision createChangeRevision() {
        char[] original1;
        Character[] original2;
        char[] revised1;
        Character[] revised2;
        List<String> o2;
        List<String> r2;
        FilesComparatorRevision rev;
        FilesComparatorRevision rev2;
        FilesComparatorChunk o;
        FilesComparatorChunk r;
        FilesComparatorDelta d2;
        int anchor;
        int size;
        WordTokenizer wt;
        int[] oIndex;
        int[] rIndex;
        int oAnchor;
        int oLength;
        int rAnchor;
        int rLength;

        original1 = revision.getOriginalString(original).toCharArray();
        original2 = new Character[original1.length];
        for (int j = 0; j < original1.length; j++) {
            original2[j] = new Character(original1[j]);
        }

        revised1 = revision.getRevisedString(revised).toCharArray();
        revised2 = new Character[revised1.length];
        for (int j = 0; j < revised1.length; j++) {
            revised2[j] = new Character(revised1[j]);
        }

        try {
            wt = TokenizerFactory.getInnerDiffTokenizer();
            o2 = wt.getTokens(revision.getOriginalString(original));
            r2 = wt.getTokens(revision.getRevisedString(revised));
            rev = new FilesComparatorDiff().diff(o2, r2, Ignore.NULL_IGNORE);

            oIndex = new int[o2.size()];
            for (int i = 0; i < o2.size(); i++) {
                oIndex[i] = o2.get(i).length();
                if (i > 0) {
                    oIndex[i] += oIndex[i - 1];
                }
                debug("oIndex[" + i + "] = " + oIndex[i] + " \"" + o2.get(i)
                        + "\"");
            }

            rIndex = new int[r2.size()];
            for (int i = 0; i < r2.size(); i++) {
                rIndex[i] = r2.get(i).length();
                if (i > 0) {
                    rIndex[i] += rIndex[i - 1];
                }
                debug("rIndex[" + i + "] = " + rIndex[i] + " \"" + r2.get(i)
                        + "\"");
            }

            rev2 = new FilesComparatorRevision(original2, revised2);
            rev2.setIgnore(Ignore.NULL_IGNORE);
            for (FilesComparatorDelta d : rev.getDeltas()) {
                o = d.getOriginal();
                r = d.getRevised();

                anchor = o.getAnchor();
                size = o.getSize();
                oAnchor = anchor == 0 ? 0 : oIndex[anchor - 1];
                oLength = size > 0 ? (oIndex[anchor + size - 1] - oAnchor) : 0;

                anchor = r.getAnchor();
                size = r.getSize();
                rAnchor = anchor == 0 ? 0 : rIndex[anchor - 1];
                rLength = size > 0 ? (rIndex[anchor + size - 1] - rAnchor) : 0;

                d2 = new FilesComparatorDelta(new FilesComparatorChunk(oAnchor,
                        oLength), new FilesComparatorChunk(rAnchor, rLength));
                rev2.add(d2);

                debug("delta = " + d + " -> " + d2);
            }

            return rev2;
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }

        return null;
    }

    void initType() {
        if (original.getSize() > 0 && revised.getSize() == 0) {
            type = Type.DELETE;
        } else if (original.getSize() == 0 && revised.getSize() > 0) {
            type = Type.ADD;
        } else {
            type = Type.CHANGE;
        }
    }

    @Override
    public boolean equals(Object o) {
        FilesComparatorDelta d;

        if (!(o instanceof FilesComparatorDelta)) {
            return false;
        }

        d = (FilesComparatorDelta) o;
        if (revision != d.revision) {
            return false;
        }

        if (!original.equals(d.original) || !revised.equals(d.revised)) {
            return false;
        }

        return true;
    }

    private void debug(String s) {
        if (debug) {
            ApplicationFrame.getInstance().getConsole().println(s);
        }
    }

    @Override
    public String toString() {
        return type + ": org[" + original + "] rev[" + revised + "]";
    }
}