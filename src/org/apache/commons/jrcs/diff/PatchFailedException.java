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
 * Thrown whenever a delta cannot be applied as a patch to a given text.
 *
 * @author D. Campione
 * @see Delta
 * @see Diff
 */
public class PatchFailedException extends DiffException {

    private static final long serialVersionUID = 3813046902139783937L;

    public PatchFailedException() {
    }

    public PatchFailedException(String msg) {
        super(msg);
    }
}
