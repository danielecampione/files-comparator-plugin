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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorChunk {

    private int anchor;
    private int size;

    public FilesComparatorChunk(int anchor, int size) {
        this.anchor = anchor;
        this.size = size;
    }

    void setAnchor(int anchor) {
        this.anchor = anchor;
    }

    public int getAnchor() {
        return anchor;
    }

    void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public boolean equals(Object o) {
        FilesComparatorChunk c;

        if (!(o instanceof FilesComparatorChunk)) {
            return false;
        }

        c = (FilesComparatorChunk) o;

        return c.size == size && c.anchor == anchor;
    }

    public String toString() {
        return "anchor=" + anchor + ",size=" + size;
    }
}
