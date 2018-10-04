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
 * A diffnode in a diffpath.
 * <p>
 * A DiffNode and its previous node mark a delta between
 * two input sequences, that is, two differing subsequences
 * between (possibly zero length) matching sequences.
 *
 * {@link DiffNode DiffNodes} and {@link Snake Snakes} allow for compression
 * of diffpaths, as each snake is represented by a single {@link Snake Snake}
 * node and each contiguous series of insertions and deletions is represented
 * by a single {@link DiffNode DiffNodes}.
 *
 * @author D. Campione
 *
 */
public final class DiffNode extends PathNode {
    /**
     * Constructs a DiffNode.
     * <p>
     * DiffNodes are compressed. That means that
     * the path pointed to by the <code>prev</code> parameter
     * will be followed using {@link PathNode#previousSnake}
     * until a non-diff node is found.
     *
     * @param the position in the original sequence
     * @param the position in the revised sequence
     * @param prev the previous node in the path.
     */
    public DiffNode(int i, int j, PathNode prev) {
        super(i, j, (prev == null ? null : prev.previousSnake()));
    }

    /**
     * {@inheritDoc}
     * @return false, always
     */
    public boolean isSnake() {
        return false;
    }
}
