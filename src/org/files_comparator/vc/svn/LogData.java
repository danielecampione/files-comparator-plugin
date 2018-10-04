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

package org.files_comparator.vc.svn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "log")
public class LogData {

    @XmlElement(name = "logentry")
    private List<Entry> entryList;

    public LogData() {
        entryList = new ArrayList<Entry>();
    }

    public List<Entry> getEntryList() {
        return entryList;
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    @XmlAccessorType(XmlAccessType.NONE)
    static class Entry {

        @XmlAttribute
        private Integer revision;
        @XmlElement
        private String author;
        @XmlElement
        private Date date;
        @XmlElementWrapper(name = "paths")
        @XmlElement(name = "path")
        private List<Path> pathList;
        @XmlElement
        private String msg;

        public Entry() {
        }

        public Integer getRevision() {
            return revision;
        }

        public String getAuthor() {
            return author;
        }

        public Date getDate() {
            return date;
        }

        public String getMsg() {
            return msg;
        }

        public List<Path> getPathList() {
            return pathList;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class Path {

        @XmlAttribute
        private String action;
        @XmlAttribute(name = "copyfrom-path")
        private String copyFromPath;
        @XmlAttribute(name = "copyfrom-rev")
        private Integer copyFromRev;
        private String pathName;

        public Path() {
        }

        public String getAction() {
            return action;
        }

        @XmlValue
        public String getPathName() {
            return pathName;
        }

        public void setPathName(String pathName) {
            if (pathName != null) {
                pathName = pathName.trim();
            }

            this.pathName = pathName;
        }
    }
}