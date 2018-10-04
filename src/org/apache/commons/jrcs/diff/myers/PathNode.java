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

package org.apache.commons.jrcs.diff.myers;

/**
 * A node in a diffpath.
 *
 * @author D. Campione
 *
 * @see DiffNode
 * @see Snake
 *
 */
public abstract class PathNode {

    /** Position in the original sequence. */
    public final int i;
    /** Position in the revised sequence. */
    public final int j;
    /** The previous node in the path. */
    public final PathNode prev;

    /**
     * Concatenates a new path node with an existing diffpath.
     * @param i The position in the original sequence for the new node.
     * @param j The position in the revised sequence for the new node.
     * @param prev The previous node in the path.
     */
    public PathNode(int i, int j, PathNode prev) {
        this.i = i;
        this.j = j;
        this.prev = prev;
    }

    /**
     * Is this node a {@link Snake Snake node}?
     * 
     * @return true if this is a {@link Snake Snake node}
     */
    public abstract boolean isSnake();

    /**
     * Is this a bootstrap node?
     * <p>
     * In bottstrap nodes one of the two corrdinates is
     * less than zero.
     * @return tru if this is a bootstrap node.
     */
    public boolean isBootstrap() {
        return i < 0 || j < 0;
    }

    /**
     * Skips sequences of {@link DiffNode DiffNodes} until a
     * {@link Snake} or bootstrap node is found, or the end
     * of the path is reached.
     * @return The next first {@link Snake} or bootstrap node in the path, or
     * <code>null</code>
     * if none found.
     */
    public final PathNode previousSnake() {
        if (isBootstrap()) {
            return null;
        }

        if (!isSnake() && prev != null) {
            return prev.previousSnake();
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("[");
        PathNode node = this;

        while (node != null) {
            buf.append("(");
            buf.append(Integer.toString(node.i));
            buf.append(",");
            buf.append(Integer.toString(node.j));
            buf.append(")");
            node = node.prev;
        }

        buf.append("]");
        return buf.toString();
    }
}
