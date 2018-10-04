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

package org.files_comparator.settings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.files_comparator.util.conf.AbstractConfiguration;
import org.files_comparator.util.conf.ConfigurationManager;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "files-comparator")
public class FilesComparatorSettings extends AbstractConfiguration {

    // class variables:
    public static FilesComparatorSettings instance;

    // Instance variables:
    @XmlElement(name = "editor")
    private EditorSettings editor = new EditorSettings();
    @XmlElement(name = "filter")
    private FilterSettings filter = new FilterSettings();
    @XmlElement(name = "folder")
    private FolderSettings folder = new FolderSettings();

    public FilesComparatorSettings() {
    }

    public static synchronized FilesComparatorSettings getInstance() {
        return (FilesComparatorSettings) ConfigurationManager.getInstance()
                .get(FilesComparatorSettings.class);
    }

    @Override
    public void init() {
        editor.init(this);
        filter.init(this);
        folder.init(this);
    }

    public EditorSettings getEditor() {
        return editor;
    }

    public FilterSettings getFilter() {
        return filter;
    }

    public FolderSettings getFolder() {
        return folder;
    }
}
