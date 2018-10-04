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

package org.files_comparator.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FileUtil {

    public static List<File> getParentFiles(File file) {
        List<File> result;
        String parentName;

        result = new ArrayList<File>();
        while ((parentName = file.getParent()) != null) {
            file = new File(parentName);
            result.add(file);
        }

        return result;
    }

    public static void copy(File src, File dst) throws IOException {
        FileChannel inChannel;
        FileChannel outChannel;

        inChannel = new FileInputStream(src).getChannel();
        outChannel = new FileOutputStream(dst).getChannel();

        outChannel.transferFrom(inChannel, 0, inChannel.size());

        inChannel.close();
        outChannel.close();
    }

    public static void copy2(File src, File dst) throws IOException {
        InputStream in;
        OutputStream out;
        byte[] buf;
        int len;

        in = new FileInputStream(src);
        out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        buf = new byte[1024];
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
    }

    public static File createTempFile(String prefix, String suffix)
            throws IOException {
        File file;

        file = File.createTempFile(prefix, suffix);
        file.deleteOnExit();

        return file;
    }
}
