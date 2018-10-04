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

package org.files_comparator.vc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class StatusResult {

    private File path;
    private Set<Entry> entryList = new HashSet<Entry>();

    public StatusResult(File path) {
        this.path = path;
    }

    public File getPath() {
        return path;
    }

    public void addEntry(String name, Status status) {
        Entry entry;

        entry = new Entry(name, status);
        if (entryList.contains(entry)) {
            return;
        }

        entryList.add(entry);
    }

    public List<Entry> getEntryList() {
        List<Entry> list;

        list = new ArrayList(entryList);
        Collections.sort(list);

        return list;
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public class Entry implements Comparable<Entry> {

        private String name;
        private Status status;

        Entry(String name, Status status) {
            this.name = name;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public Status getStatus() {
            return status;
        }

        public int compareTo(Entry entry) {
            return name.compareTo(entry.name);
        }

        public String toString() {
            return name;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }

            return name.equals(((Entry) o).name);
        }

        public int hashCode() {
            return name.hashCode();
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public enum Status {

        modified('M', "vcModified"), added('A', "vcAdded"), removed('D',
                "vcRemoved"), clean(' ', "vcClean"), conflicted('C',
                "vcConflicted"), ignored('I', "vcIgnored"), unversioned('?',
                "vcUnversioned"), missing('!', "vcMissing"), dontknow('#',
                "vcMissing");

        private char shortText;
        private String iconName;

        Status(char shortTexti, String iconName) {
            this.shortText = shortTexti;
            this.iconName = iconName;
        }

        public char getShortText() {
            return shortText;
        }

        public String getIconName() {
            return iconName;
        }
    }
}