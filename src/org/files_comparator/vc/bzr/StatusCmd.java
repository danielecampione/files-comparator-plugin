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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.util.Result;
import org.files_comparator.vc.StatusResult;
import org.files_comparator.vc.util.VcCmd;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class StatusCmd extends VcCmd<StatusResult> {

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private enum Phase {
        state, inventory;
    }

    private File file;
    private Phase phase;

    public StatusCmd(File file) {
        this.file = file;

        initWorkingDirectory(file);
    }

    public Result execute() {
        phase = Phase.state;
        super.execute("bzr", "status", "-S", file.getAbsolutePath());

        phase = Phase.inventory;
        super.execute("bzr", "inventory", file.getAbsolutePath());

        return getResult();
    }

    protected void build(byte[] data) {
        StatusResult statusResult;
        StatusResult.Status status;
        BufferedReader reader;
        String text;

        if (phase == Phase.state) {
            statusResult = new StatusResult(file);
        } else {
            statusResult = getResultData();
        }

        reader = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(data)));
        try {
            while ((text = reader.readLine()) != null) {
                if (phase == Phase.state) {
                    if (text.length() < 5) {
                        continue;
                    }

                    status = null;
                    switch (text.charAt(0)) {
                        case '+' :
                            switch (text.charAt(1)) {
                                case 'M' :
                                case 'K' :
                                    status = StatusResult.Status.modified;
                                    break;
                                case 'N' :
                                    status = StatusResult.Status.added;
                                    break;
                                case 'D' :
                                    status = StatusResult.Status.removed;
                                    break;
                            }
                            break;
                        case ' ' :
                            switch (text.charAt(1)) {
                                case 'D' :
                                    status = StatusResult.Status.missing;
                                    break;
                            }
                            break;
                        case '-' :
                            status = StatusResult.Status.ignored;
                            break;
                        case '?' :
                            status = StatusResult.Status.unversioned;
                            break;
                    }

                    statusResult.addEntry(text.substring(4), status);
                } else if (phase == Phase.inventory) {
                    status = StatusResult.Status.clean;
                    statusResult.addEntry(text, status);
                }
            }
        } catch (IOException ioe) {
            // This cannot happen. We are reading from a byte array
            ExceptionDialog.ignoreException(ioe);
        }

        setResultData(statusResult);
    }
}