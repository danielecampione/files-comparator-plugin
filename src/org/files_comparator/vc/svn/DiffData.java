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

package org.files_comparator.vc.svn;

import java.util.ArrayList;
import java.util.List;

import org.files_comparator.diff.FilesComparatorRevision;
import org.files_comparator.vc.DiffIF;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DiffData implements DiffIF {

    private List<Target> targetList;

    public DiffData() {
        targetList = new ArrayList<Target>();
    }

    public void addTarget(String path, FilesComparatorRevision revision) {
        targetList.add(new Target(path, revision));
    }

    public List<Target> getTargetList() {
        return targetList;
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class Target implements DiffIF.TargetIF {

        private String path;
        private FilesComparatorRevision revision;

        public Target(String path, FilesComparatorRevision revision) {
            this.path = path;
            this.revision = revision;
        }

        public String getPath() {
            return path;
        }

        public FilesComparatorRevision getRevision() {
            return revision;
        }
    }
}
