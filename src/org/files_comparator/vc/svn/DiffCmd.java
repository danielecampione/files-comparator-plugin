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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.diff.FilesComparatorChunk;
import org.files_comparator.diff.FilesComparatorDelta;
import org.files_comparator.diff.FilesComparatorRevision;
import org.files_comparator.util.Result;
import org.files_comparator.vc.util.VcCmd;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DiffCmd extends VcCmd<DiffData> {

    // Instance variables:
    private File file;
    private boolean recursive;
    private BufferedReader reader;
    private String unreadLine;

    public DiffCmd(File file, boolean recursive) {
        this.file = file;
        this.recursive = recursive;
    }

    public Result execute() {
        super.execute("svn", "diff", "--non-interactive", "--no-diff-deleted",
                recursive ? "" : "-N", file.getPath());

        return getResult();
    }

    protected void build(byte[] data) {
        String path;
        FilesComparatorRevision revision;
        FilesComparatorDelta delta;
        DiffData diffData;

        diffData = new DiffData();

        reader = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(data)));

        try {
            for (;;) {
                path = readIndex();
                if (path == null) {
                    break;
                }

                ApplicationFrame.getInstance().getConsole()
                        .println("path = " + path);

                revision = new FilesComparatorRevision(null, null);
                diffData.addTarget(path, revision);

                readLine(); // =====================================
                readLine(); // --- <Path>   (revision ...)
                readLine(); // +++ <Path>   (working copy)
                for (;;) {
                    delta = readDelta();
                    if (delta == null) {
                        break;
                    }
                    revision.add(delta);
                }
            }
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
            setResult(Result.FALSE("Parse failed"));
        }

        setResultData(diffData);
    }

    private String readIndex() throws IOException {
        final String indexMarker = "Index: ";
        String line;

        line = readLine();
        if (line == null || !line.startsWith(indexMarker)) {
            return null;
        }

        return line.substring(indexMarker.length());
    }

    private FilesComparatorDelta readDelta() throws IOException {
        final Pattern deltaPattern = Pattern
                .compile("@@ -(\\d*),(\\d*) \\+(\\d*),(\\d*) @@");

        String line;
        Matcher m;
        FilesComparatorDelta delta;
        FilesComparatorChunk originalChunk;
        FilesComparatorChunk revisedChunk;

        // @@ <LineNumberRevision>,<NumberOfLines> <lineNumberWorkingCopy>,<NumberOfLines> @@
        line = readLine();
        if (line == null) {
            return null;
        }

        m = deltaPattern.matcher(line);
        if (!m.matches()) {
            unreadLine(line);
            return null;
        }

        originalChunk = new FilesComparatorChunk(Integer.valueOf(m.group(1)),
                Integer.valueOf(m.group(2)));
        revisedChunk = new FilesComparatorChunk(Integer.valueOf(m.group(3)),
                Integer.valueOf(m.group(4)));

        delta = new FilesComparatorDelta(originalChunk, revisedChunk);

        while ((line = readLine()) != null) {
            if (line.startsWith(" ")) {
                continue;
            }

            if (line.startsWith("+")) {
                continue;
            }

            if (line.startsWith("-")) {
                continue;
            }

            unreadLine(line);
            break;
        }

        ApplicationFrame.getInstance().getConsole().println("delta = " + delta);

        return delta;
    }

    private void unreadLine(String unreadLine) {
        this.unreadLine = unreadLine;
    }

    private String readLine() throws IOException {
        String line;

        if (unreadLine != null) {
            line = unreadLine;
            unreadLine = null;
            return line;
        }

        return reader.readLine();
    }
}