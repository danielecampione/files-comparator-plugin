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

package org.files_comparator.vc.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

import org.files_comparator.util.Result;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class VcCmd<T> {

    // Class variables:
    static private boolean debug = true;

    // Instance variables:
    private Result result;
    private T resultData;
    private File workingDirectory;

    public void initWorkingDirectory(File file) {
        file = file.getAbsoluteFile();

        if (!file.isDirectory()) {
            file = file.getParentFile();
        }

        if (file.isDirectory()) {
            workingDirectory = file;
        } else {
            workingDirectory = null;
        }
    }

    public void execute(String... command) {
        setResult(_execute(command));
    }

    private final Result _execute(String... command) {
        ProcessBuilder pb;
        Process p;
        BufferedReader br;
        InputStream is;
        int count;
        ByteArrayOutputStream baos;
        String text;
        StringBuilder errorText;
        byte[] data;

        try {
            pb = new ProcessBuilder(command);
            if (workingDirectory != null) {
                pb = pb.directory(workingDirectory);
                ApplicationFrame.getInstance().changeLog.append("wd="
                        + workingDirectory + "\n");
            }

            ApplicationFrame.getInstance().changeLog.append("wd2="
                    + pb.directory() + "\n");
            p = pb.start();

            debug("execute: " + Arrays.asList(command));

            data = new byte[4096];

            baos = new ByteArrayOutputStream();
            is = new BufferedInputStream(p.getInputStream());
            while ((count = is.read(data, 0, data.length)) != -1) {
                baos.write(data, 0, count);
            }
            is.close();

            p.waitFor();

            debug("  exitValue = " + p.exitValue());
            if (p.exitValue() != 0) {
                errorText = new StringBuilder();
                br = new BufferedReader(new InputStreamReader(
                        p.getErrorStream()));
                while ((text = br.readLine()) != null) {
                    errorText.append(text);
                }
                br.close();
                return Result.FALSE(errorText.toString() + " (exitvalue="
                        + p.exitValue() + ")");
            }

            build(baos.toByteArray());
            baos.close();
            baos = null;
        } catch (Exception ex) {
            result = Result.FALSE(ex.getMessage(), ex);
            baos = null;
            return result;
        }

        return Result.TRUE();
    }

    protected abstract void build(byte[] data);

    public void printError() {
        ApplicationFrame.getInstance().changeLog.append(result.getDescription()
                + "\n");
        if (result.hasException()) {
            result.getException().printStackTrace();
        }
    }

    public Result getResult() {
        return result;
    }

    protected void setResult(Result result) {
        this.result = result;
    }

    protected void setResultData(T resultData) {
        this.resultData = resultData;
    }

    public T getResultData() {
        return resultData;
    }

    private void debug(String text) {
        if (debug) {
            ApplicationFrame.getInstance().changeLog.append(text + "\n");
        }
    }
}
