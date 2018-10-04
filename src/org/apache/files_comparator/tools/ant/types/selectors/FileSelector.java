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

package org.apache.files_comparator.tools.ant.types.selectors;

import java.io.File;

import org.apache.files_comparator.tools.ant.BuildException;

/**
 * This is the interface to be used by all selectors.
 * 
 * @author D. Campione
 * 
 */
public interface FileSelector {

    /**
     * Method that each selector will implement to create their
     * selection behaviour. If there is a problem with the setup
     * of a selector, it can throw a BuildException to indicate
     * the problem.
     *
     * @param basedir A java.io.File object for the base directory
     * @param filename The name of the file to check
     * @param file A File object for this filename
     * @return whether the file should be selected or not
     * @exception BuildException if the selector was not configured correctly
     */
    boolean isSelected(File basedir, String filename, File file)
            throws BuildException;
}
