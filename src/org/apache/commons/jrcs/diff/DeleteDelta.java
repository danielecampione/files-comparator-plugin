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
 * Holds a delete-delta between to revisions of a text.
 *
 * @author D. Campione
 * @see Delta
 * @see Diff
 * @see Chunk
 */
public class DeleteDelta extends Delta {
    DeleteDelta() {
        super();
    }

    public DeleteDelta(Chunk orig) {
        init(orig, null);
    }

    @SuppressWarnings("rawtypes")
    public void verify(List target) throws PatchFailedException {
        if (!original.verify(target)) {
            throw new PatchFailedException();
        }
    }

    @SuppressWarnings("rawtypes")
    public void applyTo(List target) {
        original.applyDelete(target);
    }

    public void toString(StringBuffer s) {
        s.append(original.rangeString());
        s.append("d");
        s.append(revised.rcsto());
        s.append(Diff.NL);
        original.toString(s, "< ", Diff.NL);
    }

    public void toRCSString(StringBuffer s, String EOL) {
        s.append("d");
        s.append(original.rcsfrom());
        s.append(" ");
        s.append(original.size());
        s.append(EOL);
    }

    public void accept(RevisionVisitor visitor) {
        visitor.visit(this);
    }
}
