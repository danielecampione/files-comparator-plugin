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
@XmlRootElement(name = "status")
public class StatusData {

    @XmlElement(name = "target")
    private List<Target> targetList;

    public StatusData() {
        targetList = new ArrayList<Target>();
    }

    public List<Target> getTargetList() {
        return targetList;
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class Target {

        @XmlAttribute
        private String path;
        @XmlElement(name = "entry")
        private List<Entry> entryList;
        @SuppressWarnings("unused")
        @XmlElement
        private Against against;

        public Target() {
        }

        public String getPath() {
            return path;
        }

        public List<Entry> getEntryList() {
            return entryList;
        }
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
        @XmlElement(name = "wc-status")
        private WcStatus wcStatus;
        @SuppressWarnings("unused")
        @XmlElement(name = "repos-status")
        private ReposStatus reposStatus;

        public Entry() {
        }

        public String getPath() {
            return path;
        }

        public WcStatus getWcStatus() {
            return wcStatus;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class WcStatus {

        @XmlAttribute
        private ItemStatus item;
        @XmlAttribute
        private String props;
        @XmlAttribute
        private Integer revision;
        @SuppressWarnings("unused")
        @XmlAttribute(name = "wc-locked")
        private Boolean wcLocked;
        @SuppressWarnings("unused")
        @XmlAttribute
        private Boolean copied;
        @SuppressWarnings("unused")
        @XmlAttribute
        private Boolean switched;
        @SuppressWarnings("unused")
        @XmlElement
        private Commit commit;
        @SuppressWarnings("unused")
        @XmlElement
        private Lock lock;

        public WcStatus() {
        }

        public String getProps() {
            return props;
        }

        public String getRevision() {
            if (revision == null) {
                return "0";
            }

            return revision.toString();
        }

        public ItemStatus getItem() {
            return item;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class ReposStatus {

        @XmlAttribute
        private String item;
        @XmlAttribute
        private String props;
        @SuppressWarnings("unused")
        @XmlElement
        private Lock lock;

        public ReposStatus() {
        }

        public String getProps() {
            return props;
        }

        public String getItem() {
            return item;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class Against {

        @SuppressWarnings("unused")
        @XmlAttribute
        private Integer revision;

        public Against() {
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

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public static enum ItemStatus {

        added('A'), conflicted('C'), deleted('D'), ignored('I'), modified('M'), replaced(
                'R'), external('X'), unversioned('?'), incomplete('!'), obstructed(
                '-'), normal(' '), none(' '), missing('!');

        private char shortText;

        ItemStatus(char shortText) {
            this.shortText = shortText;
        }

        public char getShortText() {
            return shortText;
        }
    }
}
