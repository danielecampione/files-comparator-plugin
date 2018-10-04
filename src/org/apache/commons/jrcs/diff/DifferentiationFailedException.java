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
 * Thrown whenever the differencing engine cannot produce the differences
 * between two revisions of ta text.
 *
 * @author D. Campione
 * @see Diff
 * @see DiffAlgorithm
 */
public class DifferentiationFailedException extends DiffException {

    private static final long serialVersionUID = -785717195839051852L;

    public DifferentiationFailedException() {
    }

    public DifferentiationFailedException(String msg) {
        super(msg);
    }
}
