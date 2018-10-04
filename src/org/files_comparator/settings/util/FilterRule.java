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

package org.files_comparator.settings.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.files_comparator.util.conf.AbstractConfigurationElement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class FilterRule extends AbstractConfigurationElement {

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public enum Rule {

        includes("includes"), excludes("excludes"), importFilter(
                "import filter");

        // instance variables:
        private String text;

        private Rule(String text) {
            this.text = text;
        }

        public String toString() {
            return text;
        }
    }

    @XmlAttribute
    private boolean active;
    @XmlAttribute
    private String pattern;
    @XmlAttribute
    private Rule rule;
    @XmlAttribute
    private String description;

    public FilterRule(String description, Rule rule, String pattern,
            boolean active) {
        setDescription(description);
        setRule(rule);
        setPattern(pattern);
        setActive(active);
    }

    public FilterRule() {
    }

    public void setDescription(String description) {
        this.description = description;
        fireChanged();
    }

    public String getDescription() {
        return description;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
        fireChanged();
    }

    public Rule getRule() {
        return rule;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        fireChanged();
    }

    public String getPattern() {
        return pattern;
    }

    public void setActive(boolean active) {
        this.active = active;
        fireChanged();
    }

    public boolean isActive() {
        return active;
    }
}
