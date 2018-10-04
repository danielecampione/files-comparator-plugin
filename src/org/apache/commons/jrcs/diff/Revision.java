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

package org.apache.commons.jrcs.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.jrcs.util.ToString;

/**
 * A Revision holds the series of deltas that describe the differences
 * between two sequences.
 *
 * @author D. Campione
 * @see Delta
 * @see Diff
 * @see Chunk
 * @see Revision
 */
public class Revision extends ToString {

    private List<Delta> deltas_ = new LinkedList<Delta>();
    private int orgSize;
    private int revSize;

    /**
     * Creates an empty Revision.
     */
    public Revision() {
    }

    public void setOrgSize(int orgSize) {
        this.orgSize = orgSize;
    }

    public int getOrgSize() {
        return orgSize;
    }

    public void setRevSize(int revSize) {
        this.revSize = revSize;
    }

    public int getRevSize() {
        return revSize;
    }

    /**
     * Adds a delta to this revision.
     * @param delta the {@link Delta Delta} to add.
     */
    public synchronized void addDelta(Delta delta) {
        if (delta == null) {
            throw new IllegalArgumentException("new delta is null");
        }

        deltas_.add(delta);
    }

    /**
     * Adds a delta to the start of this revision.
     * @param delta the {@link Delta Delta} to add.
     */
    public synchronized void insertDelta(Delta delta) {
        if (delta == null) {
            throw new IllegalArgumentException("new delta is null");
        }

        deltas_.add(0, delta);
    }

    /**
     * Retrieves a delta from this revision by position.
     * @param i the position of the delta to retrieve.
     * @return the specified delta
     */
    public Delta getDelta(int i) {
        return (Delta) deltas_.get(i);
    }

    /**
     * Returns the number of deltas in this revision.
     * @return the number of deltas.
     */
    public int size() {
        return deltas_.size();
    }

    /**
     * Applies the series of deltas in this revision as patches to
     * the given text.
     * @param src the text to patch, which the method doesn't change.
     * @return the resulting text after the patches have been applied.
     * @throws PatchFailedException if any of the patches cannot be applied.
     */
    public Object[] patch(Object[] src) throws PatchFailedException {
        List<Object> target = new ArrayList<Object>(Arrays.asList(src));

        applyTo(target);
        return target.toArray();
    }

    /**
     * Applies the series of deltas in this revision as patches to
     * the given text.
     * @param target the text to patch.
     * @throws PatchFailedException if any of the patches cannot be applied.
     */
    public synchronized void applyTo(List<Object> target)
            throws PatchFailedException {
        ListIterator<Delta> i = deltas_.listIterator(deltas_.size());

        while (i.hasPrevious()) {
            Delta delta = (Delta) i.previous();

            delta.patch(target);
        }
    }

    /**
     * Converts this revision into its Unix diff style string representation.
     * @param s a {@link StringBuilder StringBuilder} to which the string
     * representation will be appended.
     */
    public synchronized void toString(StringBuilder s) {
        Iterator<Delta> i = deltas_.iterator();

        while (i.hasNext()) {
            ((Delta) i.next()).toString(s);
        }
    }

    /**
     * Converts this revision into its RCS style string representation.
     * @param s a {@link StringBuilder StringBuilder} to which the string
     * representation will be appended.
     * @param EOL the string to use as line separator.
     */
    public synchronized void toRCSString(StringBuilder s, String EOL) {
        Iterator<Delta> i = deltas_.iterator();

        while (i.hasNext()) {
            ((Delta) i.next()).toRCSString(s, EOL);
        }
    }

    /**
     * Converts this revision into its RCS style string representation.
     * @param s a {@link StringBuilder StringBuilder} to which the string
     * representation will be appended.
     */
    public void toRCSString(StringBuilder s) {
        toRCSString(s, Diff.NL);
    }

    /**
     * Converts this delta into its RCS style string representation.
     * @param EOL the string to use as line separator.
     */
    public String toRCSString(String EOL) {
        StringBuilder s = new StringBuilder();

        toRCSString(s, EOL);
        return s.toString();
    }

    /**
     * Converts this delta into its RCS style string representation
     * using the default line separator.
     */
    public String toRCSString() {
        return toRCSString(Diff.NL);
    }

    /**
     * Accepts a visitor.
     * @param visitor the {@link Visitor} visiting this instance
     */
    public void accept(RevisionVisitor visitor) {
        visitor.visit(this);
        Iterator<Delta> iter = deltas_.iterator();

        while (iter.hasNext()) {
            ((Delta) iter.next()).accept(visitor);
        }
    }
}