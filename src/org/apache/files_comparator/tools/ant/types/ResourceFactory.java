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

package org.apache.files_comparator.tools.ant.types;

/**
 * this interface should be implemented by classes (Scanners) needing
 * to deliver information about resources.
 *
 * @author D. Campione
 * 
 */
public interface ResourceFactory {

    /**
     * Query a resource (file, zipentry, ...) by name
     *
     * @param name relative path of the resource about which
     * information is sought.  Expects &quot;/&quot; to be used as the
     * directory separator.
     * @return instance of Resource; the exists attribute of Resource
     * will tell whether the sought resource exists
     */
    Resource getResource(String name);
}
