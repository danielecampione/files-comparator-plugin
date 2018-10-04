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

package org.apache.commons.jrcs.diff;

import java.util.List;

/**
 * Holds an change-delta between to revisions of a text.
 *
 * @author D. Campione
 * @see Delta
 * @see Diff
 * @see Chunk
 */
public class ChangeDelta extends Delta {
    ChangeDelta() {
        super();
    }

    public ChangeDelta(Chunk orig, Chunk rev) {
        init(orig, rev);
    }

    public void verify(List<?> target) throws PatchFailedException {
        if (!original.verify(target)) {
            throw new PatchFailedException();
        }

        if (original.first() > target.size()) {
            throw new PatchFailedException("original.first() > target.size()");
        }
    }

    public void applyTo(List<?> target) {
        original.applyDelete(target);
        revised.applyAdd(original.first(), target);
    }

    public void toString(StringBuffer s) {
        original.rangeString(s);
        s.append("c");
        revised.rangeString(s);
        s.append(Diff.NL);
        original.toString(s, "< ", "\n");
        s.append("---");
        s.append(Diff.NL);
        revised.toString(s, "> ", "\n");
    }

    public void toRCSString(StringBuffer s, String EOL) {
        s.append("d");
        s.append(original.rcsfrom());
        s.append(" ");
        s.append(original.size());
        s.append(EOL);
        s.append("a");
        s.append(original.rcsto());
        s.append(" ");
        s.append(revised.size());
        s.append(EOL);
        revised.toString(s, "", EOL);
    }

    public void accept(RevisionVisitor visitor) {
        visitor.visit(this);
    }
}