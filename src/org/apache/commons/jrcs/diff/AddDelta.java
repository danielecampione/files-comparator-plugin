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

import java.util.List;

/**
 * Holds an add-delta between to revisions of a text.
 *
 * @author D. Campione
 * @see Delta
 * @see Diff
 * @see Chunk
 */
public class AddDelta extends Delta {
    AddDelta() {
        super();
    }

    public AddDelta(int origpos, Chunk rev) {
        init(new Chunk(origpos, 0), rev);
    }

    @Override
    public void verify(List<?> target) throws PatchFailedException {
        if (original.first() > target.size()) {
            throw new PatchFailedException("original.first() > target.size()");
        }
    }

    @Override
    public void applyTo(List<?> target) {
        revised.applyAdd(original.first(), target);
    }

    public void toString(StringBuilder s) {
        s.append(original.anchor());
        s.append("a");
        s.append(revised.rangeString());
        s.append(Diff.NL);
        revised.toString(s, "> ", Diff.NL);
    }

    @Override
    public void toRCSString(StringBuilder s, String EOL) {
        s.append("a");
        s.append(original.anchor());
        s.append(" ");
        s.append(revised.size());
        s.append(EOL);
        revised.toString(s, "", EOL);
    }

    public void Accept(RevisionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(RevisionVisitor visitor) {
        visitor.visit(this);
    }
}