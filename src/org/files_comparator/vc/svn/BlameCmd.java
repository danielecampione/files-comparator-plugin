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

import java.io.File;

import org.files_comparator.util.Result;
import org.files_comparator.vc.BlameIF;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class BlameCmd extends SvnXmlCmd<BlameData> {

    private File file;

    public BlameCmd(File file) {
        super(BlameData.class);

        this.file = file;
    }

    public Result execute() {
        super.execute("svn", "blame", "--non-interactive", "--xml",
                file.getPath());

        return getResult();
    }

    public BlameIF getBlame() {
        return getResultData();
    }
}
