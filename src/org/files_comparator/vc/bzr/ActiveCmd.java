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

package org.files_comparator.vc.bzr;

import java.io.File;

import org.files_comparator.util.Result;
import org.files_comparator.vc.util.VcCmd;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ActiveCmd extends VcCmd<Boolean> {

    private File file;

    public ActiveCmd(File file) {
        this.file = file;
    }

    public Result execute() {
        // If a root can be found than we have a mercurial working directory!
        super.execute("bzr", "info", file.getAbsolutePath());

        return getResult();
    }

    protected void build(byte[] data) {
        setResultData(Boolean.TRUE);
    }
}
