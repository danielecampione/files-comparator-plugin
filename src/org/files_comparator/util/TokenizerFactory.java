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

package org.files_comparator.util;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TokenizerFactory {

    // class variables:
    private static TokenizerFactory instance = new TokenizerFactory();

    // instance variables:
    private WordTokenizer innerDiffTokenizer;
    private WordTokenizer fileNameTokenizer;

    private TokenizerFactory() {
    }

    public static synchronized WordTokenizer getInnerDiffTokenizer() {
        if (instance.innerDiffTokenizer == null) {
            instance.innerDiffTokenizer = new WordTokenizer(
                    "\\s|;|:|\\(|\\)|\\[|\\]|[-+*&^%\\/}{=<>`'\"|]+|\\.");
            instance.innerDiffTokenizer = new WordTokenizer("\\b\\B*");
        }

        return instance.innerDiffTokenizer;
    }

    public static synchronized WordTokenizer getFileNameTokenizer() {
        if (instance.fileNameTokenizer == null) {
            instance.fileNameTokenizer = new WordTokenizer("[ /\\\\]+");
        }

        return instance.fileNameTokenizer;
    }
}
