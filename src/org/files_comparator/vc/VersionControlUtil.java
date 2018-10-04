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

package org.files_comparator.vc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class VersionControlUtil {

    static private List<VersionControlIF> versionControlList;

    public static boolean isVersionControlled(File file) {
        return getVersionControl(file) != null;
    }

    public static List<VersionControlIF> getVersionControl(File file) {
        List<VersionControlIF> list;

        list = new ArrayList<VersionControlIF>();
        for (VersionControlIF versionControl : getVersionControlList()) {
            if (!versionControl.isInstalled()) {
                continue;
            }

            if (!versionControl.isEnabled(file)) {
                continue;
            }

            list.add(versionControl);
        }

        return list;
    }

    public static List<VersionControlIF> getVersionControlList() {
        if (versionControlList == null) {
            versionControlList = new ArrayList<VersionControlIF>();
            versionControlList
                    .add(new org.files_comparator.vc.svn.SubversionVersionControl());
            versionControlList
                    .add(new org.files_comparator.vc.hg.MercurialVersionControl());
            versionControlList
                    .add(new org.files_comparator.vc.bzr.BazaarVersionControl());
        }

        return versionControlList;
    }
}
