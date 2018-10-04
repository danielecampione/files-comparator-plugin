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

/**
 * A simple interface for implementations of differencing algorithms.
 *
 * @author D. Campione
 * 
 */
public interface DiffAlgorithm {

    /**
     * Computes the difference between the original
     * sequence and the revised sequence and returns it
     * as a {@link org.apache.commons.jrcs.diff.Revision Revision}
     * object.
     * <p>
     * The revision can be used to construct the revised sequence
     * from the original sequence.
     *
     * @param rev the revised text
     * @return the revision script.
     * @throws DifferentiationFailedException if the diff could not be computed.
     */
    public abstract Revision diff(Object[] orig, Object[] rev)
            throws DifferentiationFailedException;
}
