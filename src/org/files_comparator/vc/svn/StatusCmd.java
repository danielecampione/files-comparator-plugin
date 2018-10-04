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

import org.files_comparator.util.Result;
import org.files_comparator.vc.StatusResult;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class StatusCmd extends SvnXmlCmd<StatusData> {

    private File file;
    private boolean recursive;

    public StatusCmd(File file, boolean recursive) {
        super(StatusData.class);

        this.file = file;
        this.recursive = recursive;
    }

    public Result execute() {
        super.execute("svn", "status", "--non-interactive", "-v", "--xml",
                recursive ? "" : "-N", file.getPath());

        return getResult();
    }

    public StatusResult getStatusResult() {
        StatusData sd;
        StatusResult result;
        StatusResult.Status status;

        sd = getResultData();

        result = new StatusResult(file);
        if (sd != null) {
            for (StatusData.Target t : sd.getTargetList()) {
                for (StatusData.Entry te : t.getEntryList()) {
                    status = null;
                    switch (te.getWcStatus().getItem()) {
                        case added :
                            status = StatusResult.Status.added;
                            break;
                        case conflicted :
                            status = StatusResult.Status.conflicted;
                            break;
                        case deleted :
                            status = StatusResult.Status.removed;
                            break;
                        case ignored :
                            status = StatusResult.Status.ignored;
                            break;
                        case modified :
                            status = StatusResult.Status.modified;
                            break;
                        case replaced :
                            status = StatusResult.Status.modified;
                            break;
                        case external :
                            status = StatusResult.Status.dontknow;
                            break;
                        case unversioned :
                            status = StatusResult.Status.unversioned;
                            break;
                        case incomplete :
                            status = StatusResult.Status.missing;
                            break;
                        case obstructed :
                            status = StatusResult.Status.dontknow;
                            break;
                        case normal :
                            status = StatusResult.Status.clean;
                            break;
                        case none :
                            status = StatusResult.Status.clean;
                            break;
                        case missing :
                            status = StatusResult.Status.missing;
                            break;
                    }

                    result.addEntry(te.getPath(), status);
                }
            }
        }

        return result;
    }
}
