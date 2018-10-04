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

package org.apache.commons.jrcs.diff.myers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jrcs.diff.Chunk;
import org.apache.commons.jrcs.diff.Delta;
import org.apache.commons.jrcs.diff.Diff;
import org.apache.commons.jrcs.diff.DiffAlgorithm;
import org.apache.commons.jrcs.diff.DifferentiationFailedException;
import org.apache.commons.jrcs.diff.Revision;

/**
 * A clean-room implementation of
 * <a href="http://www.cs.arizona.edu/people/gene/">
 * Eugene Myers</a> differencing algorithm.
 * <p>
 * See the paper at
 * <a href="http://www.cs.arizona.edu/people/gene/PAPERS/diff.ps">
 * http://www.cs.arizona.edu/people/gene/PAPERS/diff.ps</a>
 *
 * @author D. Campione
 * @see Delta
 * @see Revision
 * @see Diff
 */
public class MyersDiff implements DiffAlgorithm {

    private static int MAXTIME = 3000;
    public boolean checkMaxTime = false;

    /**
     * Constructs an instance of the Myers differencing algorithm.
     */
    public MyersDiff() {
    }

    public void checkMaxTime(boolean checkMaxTime) {
        this.checkMaxTime = checkMaxTime;
    }

    /**
     * {@inheritDoc}
     */
    public Revision diff(Object[] orig, Object[] rev)
            throws DifferentiationFailedException {
        PathNode path = buildPath(orig, rev);

        return buildRevision(path, orig, rev);
    }

    /**
     * Computes the minimum diffpath that expresses de differences
     * between the original and revised sequences, according
     * to Gene Myers differencing algorithm.
     *
     * @param orig The original sequence.
     * @param rev The revised sequence.
     * @return A minimum {@link PathNode Path} accross the differences graph.
     * @throws DifferentiationFailedException if a diff path could not be found.
     */
    public PathNode buildPath(Object[] orig, Object[] rev)
            throws DifferentiationFailedException {
        int N;
        int M;
        int MAX;
        int size;
        int middle;
        Map<Integer, PathNode> diagonal;
        PathNode d_kminus;
        PathNode d_kplus;
        PathNode node;
        int kmiddle;
        int kplus;
        int kminus;
        PathNode prev;
        int i;
        int j;
        long startTime;

        if (orig == null) {
            throw new IllegalArgumentException("original sequence is null");
        }

        if (rev == null) {
            throw new IllegalArgumentException("revised sequence is null");
        }

        // these are local constants
        N = orig.length;
        M = rev.length;

        MAX = N + M + 1;
        size = 1 + 2 * MAX;
        middle = (size + 1) / 2;
        diagonal = new HashMap<Integer, PathNode>();

        startTime = System.currentTimeMillis();

        diagonal.put(middle + 1, new Snake(0, -1, null));
        for (int d = 0; d < MAX; d++) {
            if (checkMaxTime
                    && System.currentTimeMillis() - startTime > MAXTIME) {
                throw new org.files_comparator.diff.MaxTimeExceededException(
                        "Algoritm is taking up to much time");
            }

            for (int k = -d; k <= d; k += 2) {
                kmiddle = middle + k;
                kplus = kmiddle + 1;
                kminus = kmiddle - 1;
                prev = null;

                d_kminus = diagonal.get(kminus);
                d_kplus = diagonal.get(kplus);
                if ((k == -d) || (k != d && d_kminus.i < d_kplus.i)) {
                    i = d_kplus.i;
                    prev = d_kplus;
                } else {
                    i = d_kminus.i + 1;
                    prev = d_kminus;
                }

                diagonal.remove(kminus);

                j = i - k;

                node = new DiffNode(i, j, prev);

                // orig and rev are zero-based
                // but the algorithm is one-based
                // that's why there's no +1 when indexing the sequences
                while (i < N && j < M && orig[i].equals(rev[j])) {
                    i++;
                    j++;
                }

                if (i > node.i) {
                    node = new Snake(i, j, node);
                }

                diagonal.put(kmiddle, node);

                if (i >= N && j >= M) {
                    return diagonal.get(kmiddle);
                }
            }

            diagonal.put(middle + d - 1, null);
        }

        // According to Myers, this cannot happen
        throw new DifferentiationFailedException("could not find a diff path");
    }

    /**
     * Constructs a {@link Revision} from a difference path.
     *
     * @param path The path.
     * @param orig The original sequence.
     * @param rev The revised sequence.
     * @return A {@link Revision} script corresponding to the path.
     * @throws DifferentiationFailedException if a {@link Revision} could
     *         not be built from the given path.
     */
    public Revision buildRevision(PathNode path, Object[] orig, Object[] rev) {
        if (path == null) {
            throw new IllegalArgumentException("path is null");
        }

        if (orig == null) {
            throw new IllegalArgumentException("original sequence is null");
        }

        if (rev == null) {
            throw new IllegalArgumentException("revised sequence is null");
        }

        Revision revision = new Revision();

        if (path.isSnake()) {
            path = path.prev;
        }
        while (path != null && path.prev != null && path.prev.j >= 0) {
            if (path.isSnake()) {
                throw new IllegalStateException(
                        "bad diffpath: found snake when looking for diff");
            }

            int i = path.i;
            int j = path.j;

            path = path.prev;
            int ianchor = path.i;
            int janchor = path.j;

            Delta delta = Delta.newDelta(new Chunk(orig, ianchor, i - ianchor),
                    new Chunk(rev, janchor, j - janchor));

            revision.insertDelta(delta);
            if (path.isSnake()) {
                path = path.prev;
            }
        }

        return revision;
    }
}