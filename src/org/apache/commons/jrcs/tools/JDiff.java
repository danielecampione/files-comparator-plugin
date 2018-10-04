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

package org.apache.commons.jrcs.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A program to compare two files.
 * <p>JDiff produces the deltas between the two given files in Unix diff
 * format.
 * </p>
 * <p>The program was written as a simple test of the
 * {@linkplain org.apache.commons.jrcs.diff diff} package.
 * 
 * @author D. Campione
 * 
 */
public class JDiff {

    static final String[] loadFile(String name) throws IOException {
        BufferedReader data = new BufferedReader(new FileReader(name));
        List<String> lines = new ArrayList<String>();
        String s;

        while ((s = data.readLine()) != null) {
            lines.add(s);
        }

        return (String[]) lines.toArray(new String[lines.size()]);
    }
}