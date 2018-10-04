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

package open_teradata_viewer.plugin;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorException extends Exception {

    private static final long serialVersionUID = 7040681811065132200L;

    public FilesComparatorException(String m) {
        super(m);
    }

    public FilesComparatorException(String m, Throwable t) {
        super(m, t);
    }
}
