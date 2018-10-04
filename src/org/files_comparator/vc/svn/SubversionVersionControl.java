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

package org.files_comparator.vc.svn;

import java.io.File;

import org.files_comparator.vc.BaseFile;
import org.files_comparator.vc.BlameIF;
import org.files_comparator.vc.DiffIF;
import org.files_comparator.vc.StatusResult;
import org.files_comparator.vc.VersionControlIF;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SubversionVersionControl implements VersionControlIF {

    private Boolean installed;

    public String getName() {
        return "subversion";
    }

    public boolean isInstalled() {
        InstalledCmd cmd;

        if (installed == null) {
            cmd = new InstalledCmd();
            cmd.execute();
            installed = cmd.getResult().isTrue();
        }

        return installed.booleanValue();
    }

    public boolean isEnabled(File file) {
        ActiveCmd cmd;

        cmd = new ActiveCmd(file);
        cmd.execute();

        return cmd.getResult().isTrue();
        /*
        StatusCmd cmd;
        StatusResult statusResult;

        // Don't check for existence of '.svn' because an installations
        //   can change that default.
        // Don't use the info command because it will fail for unversioned
        //   files that ARE in a versioned directory.

        cmd = new StatusCmd(file, false);
        if (!cmd.execute().isTrue())
        {
          return false;
        }

        // Subversion has a bug until 1.5.1.
        // It will return an invalid xmldocument on a file that is not
        //   in a working copy.
        statusResult = cmd.getStatusResult();
        if (statusResult == null)
        {
          return false;
        }

        return statusResult.getEntryList().size() >= 1;
        */
    }

    public BlameIF executeBlame(File file) {
        BlameCmd cmd;

        cmd = new BlameCmd(file);
        cmd.execute();
        return cmd.getResultData();
    }

    public DiffIF executeDiff(File file, boolean recursive) {
        DiffCmd cmd;

        cmd = new DiffCmd(file, recursive);
        cmd.execute();
        return cmd.getResultData();
    }

    public StatusResult executeStatus(File file) {
        StatusCmd cmd;

        cmd = new StatusCmd(file, true);
        cmd.execute();
        return cmd.getStatusResult();
    }

    public BaseFile getBaseFile(File file) {
        CatCmd cmd;

        cmd = new CatCmd(file);
        cmd.execute();
        return cmd.getResultData();
    }

    public String toString() {
        return getName();
    }
}
