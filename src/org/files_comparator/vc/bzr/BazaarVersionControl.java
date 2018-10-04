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

import org.files_comparator.vc.BaseFile;
import org.files_comparator.vc.StatusResult;
import org.files_comparator.vc.VersionControlIF;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class BazaarVersionControl implements VersionControlIF {

    private Boolean installed;

    public String getName() {
        return "bazaar";
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
    }

    public StatusResult executeStatus(File file) {
        StatusCmd cmd;

        cmd = new StatusCmd(file);
        cmd.execute();
        return cmd.getResultData();
    }

    public BaseFile getBaseFile(File file) {
        CatCmd cmd;

        cmd = new CatCmd(file);
        cmd.execute();
        return cmd.getResultData();
    }

    @Override
    public String toString() {
        return getName();
    }
}
