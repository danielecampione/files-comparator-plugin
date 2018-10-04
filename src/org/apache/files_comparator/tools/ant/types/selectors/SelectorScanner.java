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

package org.apache.files_comparator.tools.ant.types.selectors;

/**
 * An interface used to describe the actions required by any type of
 * directory scanner that supports Selecters.
 * 
 * @author D. Campione
 * 
 */
public interface SelectorScanner {

    /**
     * Sets the selectors the scanner should use.
     *
     * @param selectors the list of selectors
     */
    void setSelectors(FileSelector[] selectors);

    /**
     * Directories which were selected out of a scan.
     *
     * @return list of directories not selected
     */
    String[] getDeselectedDirectories();

    /**
     * Files which were selected out of a scan.
     *
     * @return list of files not selected
     */
    String[] getDeselectedFiles();
}
