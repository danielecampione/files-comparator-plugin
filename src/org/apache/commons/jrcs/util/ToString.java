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

package org.apache.commons.jrcs.util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 * This class delegates handling of the to a StringBuffer based version.
 *
 * @author D. Campione
 * 
 */
public class ToString {

    public ToString() {
    }

    /**
     * Default implementation of the
     * {@link java.lang.Object#toString toString() } method that
     * delegates work to a {@link java.lang.StringBuffer StringBuffer}
     * base version.
     */
    public String toString() {
        StringBuffer s = new StringBuffer();

        toString(s);
        return s.toString();
    }

    /**
     * Place a string image of the object in a StringBuffer.
     * @param s the string buffer.
     */
    public void toString(StringBuffer s) {
        s.append(super.toString());
    }

    /**
     * Breaks a string into an array of strings.
     * Use the value of the <code>line.separator</code> system property
     * as the linebreak character.
     * @param value the string to convert.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String[] stringToArray(String value) {
        BufferedReader reader = new BufferedReader(new StringReader(value));
        List l = new LinkedList();
        String s;

        try {
            while ((s = reader.readLine()) != null) {
                l.add(s);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return (String[]) l.toArray(new String[l.size()]);
    }

    /**
     * Converts an array of {@link Object Object} to a string
     * Use the value of the <code>line.separator</code> system property
     * the line separator.
     * @param o the array of objects.
     */
    public static String arrayToString(Object[] o) {
        return arrayToString(o, System.getProperty("line.separator"));
    }

    /**
     * Converts an array of {@link Object Object} to a string
     * using the given line separator.
     * @param o the array of objects.
     * @param EOL the string to use as line separator.
     */
    public static String arrayToString(Object[] o, String EOL) {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < o.length - 1; i++) {
            buf.append(o[i]);
            buf.append(EOL);
        }

        buf.append(o[o.length - 1]);
        return buf.toString();
    }
}
