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

package org.files_comparator.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class WordTokenizer {

    private Pattern p;

    public WordTokenizer(String pattern) {
        p = Pattern.compile(pattern);
    }

    /** Get the tokens of the text.
     *   All tokens and non-tokens are returned in the result.
     *   So that the length of the text is the same length as
     *   the length of all tokens.
     */
    public List<String> getTokens(String text) {
        Matcher m;
        List<String> result;
        int index;
        String s;

        result = new ArrayList<String>();

        index = 0;
        m = p.matcher(text);
        while (m.find()) {
            s = text.substring(index, m.start());
            // Here the text starts with a token!
            if (s.length() > 0) {
                result.add(s);
                index += s.length();
            }

            // Add the string that matches the token also to the result.
            s = text.substring(m.start(), m.end());
            if (s.length() > 0) {
                result.add(s);
                index += s.length();
            }
        }

        // Here the text does not end with the pattern!
        if (index < text.length()) {
            s = text.substring(index, text.length());
            if (s.length() > 0) {
                result.add(s);
            }
        }

        return result;
    }
}
