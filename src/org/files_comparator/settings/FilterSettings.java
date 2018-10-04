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

package org.files_comparator.settings;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.files_comparator.settings.util.Filter;
import org.files_comparator.settings.util.FilterRule;
import org.files_comparator.util.ObjectUtil;
import org.files_comparator.util.conf.AbstractConfigurationElement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class FilterSettings extends AbstractConfigurationElement {
    @XmlElement
    private List<Filter> filters;

    public FilterSettings() {
        filters = new ArrayList<Filter>();
    }

    public void init(FilesComparatorSettings parent) {
        super.init(parent);

        for (Filter f : filters) {
            f.init(parent);
        }

        initDefault();
    }

    public void addFilter(Filter filter) {
        filter.init(configuration);
        filters.add(filter);
        fireChanged();
    }

    public void removeFilter(Filter filter) {
        filters.remove(filter);
        fireChanged();
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public Filter getFilter(String name) {
        for (Filter f : filters) {
            if (ObjectUtil.equals(f.getName(), name)) {
                return f;
            }
        }

        return null;
    }

    private void initDefault() {
        Filter filter;

        if (getFilter("default") != null) {
            return;
        }

        filter = new Filter("default");
        filter.addRule(new FilterRule("Temporary files",
                FilterRule.Rule.excludes, "**/*~", true));
        filter.addRule(new FilterRule("Temporary files",
                FilterRule.Rule.excludes, "**/#*#", true));
        filter.addRule(new FilterRule("Temporary files",
                FilterRule.Rule.excludes, "**/.#*", true));
        filter.addRule(new FilterRule("Temporary files",
                FilterRule.Rule.excludes, "**/%*%", true));
        filter.addRule(new FilterRule("Temporary files",
                FilterRule.Rule.excludes, "**/._*", true));
        filter.addRule(new FilterRule("Versioncontrol",
                FilterRule.Rule.excludes, "**/.svn", true));
        filter.addRule(new FilterRule("Versioncontrol",
                FilterRule.Rule.excludes, "**/.svn/**", true));
        filter.addRule(new FilterRule("Versioncontrol",
                FilterRule.Rule.excludes, "**/CVS", true));
        filter.addRule(new FilterRule("Versioncontrol",
                FilterRule.Rule.excludes, "**/CVS/**", true));
        filter.addRule(new FilterRule("Versioncontrol",
                FilterRule.Rule.excludes, "**/SCCS", true));
        filter.addRule(new FilterRule("Versioncontrol",
                FilterRule.Rule.excludes, "**/SCCS/**", true));
        filter.addRule(new FilterRule("Versioncontrol",
                FilterRule.Rule.excludes, "**/vssver.scc", true));
        filter.addRule(new FilterRule("Versioncontrol",
                FilterRule.Rule.excludes, "**/.SYNC", true));
        filter.addRule(new FilterRule("Mac", FilterRule.Rule.excludes,
                "**/.DS_Store", true));
        filter.addRule(new FilterRule("Media", FilterRule.Rule.excludes,
                "**/.jpg", true));
        filter.addRule(new FilterRule("Media", FilterRule.Rule.excludes,
                "**/.gif", true));
        filter.addRule(new FilterRule("Media", FilterRule.Rule.excludes,
                "**/.png", true));
        filter.addRule(new FilterRule("Media", FilterRule.Rule.excludes,
                "**/.wav", true));
        filter.addRule(new FilterRule("Media", FilterRule.Rule.excludes,
                "**/.mp3", true));
        filter.addRule(new FilterRule("Media", FilterRule.Rule.excludes,
                "**/.ogg", true));
        filter.addRule(new FilterRule("Media", FilterRule.Rule.excludes,
                "**/.xcf", true));
        filter.addRule(new FilterRule("Media", FilterRule.Rule.excludes,
                "**/.xpm", true));
        filter.addRule(new FilterRule("Binaries", FilterRule.Rule.excludes,
                "**/.pyc", true));
        filter.addRule(new FilterRule("Binaries", FilterRule.Rule.excludes,
                "**/.a", true));
        filter.addRule(new FilterRule("Binaries", FilterRule.Rule.excludes,
                "**/.obj", true));
        filter.addRule(new FilterRule("Binaries", FilterRule.Rule.excludes,
                "**/.o", true));
        filter.addRule(new FilterRule("Binaries", FilterRule.Rule.excludes,
                "**/.so", true));
        filter.addRule(new FilterRule("Binaries", FilterRule.Rule.excludes,
                "**/.la", true));
        filter.addRule(new FilterRule("Binaries", FilterRule.Rule.excludes,
                "**/.lib", true));
        filter.addRule(new FilterRule("Binaries", FilterRule.Rule.excludes,
                "**/.dll", true));
        filter.addRule(new FilterRule("Java", FilterRule.Rule.excludes,
                "**/*.class", true));
        filter.addRule(new FilterRule("Java", FilterRule.Rule.excludes,
                "**/*.jar", true));

        addFilter(filter);
    }
}
