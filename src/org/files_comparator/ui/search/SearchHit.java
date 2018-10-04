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

package org.files_comparator.ui.search;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SearchHit {

    int line;
    int fromOffset;
    int toOffset;
    int size;

    public SearchHit(int line, int offset, int size) {
        this.line = line;
        this.fromOffset = offset;
        this.size = size;
        this.toOffset = offset + size;
    }

    public int getLine() {
        return line;
    }

    public int getFromOffset() {
        return fromOffset;
    }

    public int getSize() {
        return size;
    }

    public int getToOffset() {
        return toOffset;
    }

    public boolean equals(Object o) {
        SearchHit sh;

        if (!(o instanceof SearchHit)) {
            return false;
        }

        sh = (SearchHit) o;

        return (sh.getFromOffset() == getFromOffset() && sh.getToOffset() == getToOffset());
    }
}
