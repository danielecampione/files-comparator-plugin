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

package org.files_comparator.vc.svn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.files_comparator.vc.BlameIF;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "blame")
public class BlameData implements BlameIF {

    @XmlElement(name = "target")
    private List<Target> targetList;

    public BlameData() {
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
    @XmlAccessorType(XmlAccessType.NONE)
    static class Target implements BlameIF.TargetIF {

        @XmlAttribute
        private String path;
        @XmlElement(name = "entry")
        private List<Entry> entryList;

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
    static class Entry implements BlameIF.EntryIF {

        @XmlAttribute(name = "line-number")
        private Integer lineNumber;
        @XmlElement
        private Commit commit;

        public Entry() {
        }

        public Integer getLineNumber() {
            return lineNumber;
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
    static class Commit implements BlameIF.CommitIF {

        @XmlAttribute
        private Integer revision;
        @XmlElement
        private String author;
        @XmlElement
        private String date;

        public Commit() {
        }

        public Integer getRevision() {
            return revision;
        }

        public String getAuthor() {
            return author;
        }

        public String getDate() {
            return date;
        }
    }
}
