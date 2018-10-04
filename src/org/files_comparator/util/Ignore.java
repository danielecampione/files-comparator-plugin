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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.files_comparator.util.conf.AbstractConfiguration;
import org.files_comparator.util.conf.AbstractConfigurationElement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Ignore extends AbstractConfigurationElement {

    // Class variables:
    static public final Ignore NULL_IGNORE = new Ignore();

    // Instance variables:
    @XmlElement
    public boolean ignoreWhitespaceAtBegin;
    @XmlElement
    public boolean ignoreWhitespaceInBetween;
    @XmlElement
    public boolean ignoreWhitespaceAtEnd;
    @XmlElement
    public boolean ignoreEOL;
    @XmlElement
    public boolean ignoreBlankLines;
    @XmlElement
    public boolean ignoreCase;
    @XmlElement
    // Transient:
    public boolean ignore;
    public boolean ignoreWhitespace;

    public Ignore(Ignore ignore) {
        this(ignore.ignoreWhitespaceAtBegin, ignore.ignoreWhitespaceInBetween,
                ignore.ignoreWhitespaceAtEnd, ignore.ignoreEOL,
                ignore.ignoreBlankLines, ignore.ignoreCase);
    }

    public Ignore() {
        this(false, false, false);
    }

    public Ignore(boolean ignoreWhitespace, boolean ignoreEOL,
            boolean ignoreBlankLines) {
        this(ignoreWhitespace, ignoreWhitespace, ignoreWhitespace, ignoreEOL,
                ignoreBlankLines, false);
    }

    public Ignore(boolean ignoreWhitespaceAtBegin,
            boolean ignoreWhitespaceInBetween, boolean ignoreWhitespaceAtEnd,
            boolean ignoreEOL, boolean ignoreBlankLines, boolean ignoreCase) {
        this.ignoreWhitespaceAtBegin = ignoreWhitespaceAtBegin;
        this.ignoreWhitespaceInBetween = ignoreWhitespaceInBetween;
        this.ignoreWhitespaceAtEnd = ignoreWhitespaceAtEnd;
        this.ignoreEOL = ignoreEOL;
        this.ignoreBlankLines = ignoreBlankLines;
        this.ignoreCase = ignoreCase;

        init();
    }

    @Override
    public void init(AbstractConfiguration configuration) {
        super.init(configuration);
        init();
    }

    private void init() {
        this.ignore = (ignoreWhitespaceAtBegin || ignoreWhitespaceInBetween
                || ignoreWhitespaceAtEnd || ignoreEOL || ignoreBlankLines || ignoreCase);
        this.ignoreWhitespace = (ignoreWhitespaceAtBegin
                || ignoreWhitespaceInBetween || ignoreWhitespaceAtEnd);
    }

    @Override
    public String toString() {
        return "ignore: " + (!ignore ? "nothing" : "")
                + (ignoreWhitespaceAtBegin ? "whitespace[begin] " : "")
                + (ignoreWhitespaceInBetween ? "whitespace[in between] " : "")
                + (ignoreWhitespaceAtEnd ? "whitespace[end] " : "")
                + (ignoreEOL ? "eol " : "")
                + (ignoreBlankLines ? "blanklines " : "")
                + (ignoreCase ? "case " : "");
    }
}
