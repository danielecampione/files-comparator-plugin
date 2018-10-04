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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "info")
public class InfoData {

    @XmlElement(name = "entry")
    private List<Entry> entryList;

    public InfoData() {
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
    static class Entry {

        @XmlAttribute
        private String path;
        @XmlAttribute
        private String dir;
        @XmlAttribute
        private String file;
        @XmlAttribute
        private Integer revision;
        @XmlElement
        private String url;
        @XmlElement
        private Repository repository;
        @XmlElement(name = "wc-info")
        private WcInfo wcInfo;
        @XmlElement
        private Commit commit;
        @SuppressWarnings("unused")
        @XmlElement
        private Lock lock;

        public Entry() {
        }

        public String getDir() {
            return dir;
        }

        public String getFile() {
            return file;
        }

        public String getPath() {
            return path;
        }

        public Integer getRevision() {
            return revision;
        }

        public String getUrl() {
            return url;
        }

        public Repository getRepository() {
            return repository;
        }

        public WcInfo getWcInfo() {
            return wcInfo;
        }

        public Commit getCommit() {
            return commit;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class Repository {

        @XmlElement
        private String root;
        @XmlElement
        private String uuid;

        public Repository() {
        }

        public String getRoot() {
            return root;
        }

        public String getUUID() {
            return uuid;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class WcInfo {

        @XmlElement
        private String schedule;
        @SuppressWarnings("unused")
        @XmlElement(name = "copy-from-url")
        private String copyFromUrl;
        @SuppressWarnings("unused")
        @XmlElement(name = "copy-from-rev")
        private String copyFromRev;
        @XmlElement(name = "text-updated")
        private Date textUpdated;
        @SuppressWarnings("unused")
        @XmlElement(name = "prop-updated")
        private Date propUpdated;
        @XmlElement
        private String checksum;
        @SuppressWarnings("unused")
        @XmlElement
        private Confict conflict;

        public WcInfo() {
        }

        public String getSchedule() {
            return schedule;
        }

        public Date getTextUpdated() {
            return textUpdated;
        }

        public String getChecksum() {
            return checksum;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class Confict {

        @SuppressWarnings("unused")
        @XmlElement(name = "prev-base-file")
        private String prevBaseFile;
        @SuppressWarnings("unused")
        @XmlElement(name = "prev-wc-file")
        private String prevWcFile;
        @SuppressWarnings("unused")
        @XmlElement(name = "cur-base-file")
        private String curBaseFile;
        @SuppressWarnings("unused")
        @XmlElement(name = "prop-file")
        private String propFile;

        public Confict() {
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class Commit {

        @XmlAttribute
        private Integer revision;
        @XmlElement
        private String author;
        @XmlElement
        private Date date;

        public Commit() {
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
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class Lock {

        @SuppressWarnings("unused")
        @XmlElement
        private String token;
        @SuppressWarnings("unused")
        @XmlElement
        private String owner;
        @SuppressWarnings("unused")
        @XmlElement
        private String comment;
        @SuppressWarnings("unused")
        @XmlElement
        private Date created;
        @SuppressWarnings("unused")
        @XmlElement
        private Date expires;

        public Lock() {
        }
    }
}
