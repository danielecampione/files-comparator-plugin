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

package org.files_comparator.util.node;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorNode implements Comparable<FilesComparatorNode> {

    // instance variables:
    private String name;
    private boolean isLeaf;
    private boolean collapsed;

    public FilesComparatorNode(String name, boolean isLeaf) {
        this.name = name;
        this.isLeaf = isLeaf;
    }

    public String getName() {
        return name;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public long getSize() {
        return 0;
    }

    public int compareTo(FilesComparatorNode o) {
        return name.compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FilesComparatorNode)) {
            return false;
        }

        return name.equals(((FilesComparatorNode) o).getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void print() {
        ApplicationFrame.getInstance().changeLog.append(name + "\n");
    }

    public void resetContent() {
    }

    @Override
    public String toString() {
        return getName();
    }
}
