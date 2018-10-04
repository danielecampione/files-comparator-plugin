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

package org.apache.files_comparator.tools.ant.types;

/**
 * Describes a File or a ZipEntry.
 *
 * This class is meant to be used by classes needing to record path
 * and date/time information about a file, a zip entry or some similar
 * resource (URL, archive in a version control repository, ...).
 *
 * @author D. Campione
 * 
 */

public class Resource implements Cloneable, Comparable<Object> {
    /** Constant unknown size */
    public static final long UNKNOWN_SIZE = -1;
    private String name = null;
    private boolean exists = true;
    private long lastmodified = 0;
    private boolean directory = false;
    private long size = UNKNOWN_SIZE;

    /**
     * Default constructor.
     */
    public Resource() {
    }

    /**
     * Only sets the name.
     *
     * <p>This is a dummy, used for not existing resources.</p>
     *
     * @param name relative path of the resource.  Expects
     * &quot;/&quot; to be used as the directory separator.
     */
    public Resource(String name) {
        this(name, false, 0, false);
    }

    /**
     * Sets the name, lastmodified flag, and exists flag.
     *
     * @param name relative path of the resource.  Expects
     * &quot;/&quot; to be used as the directory separator.
     * @param exists if true, this resource exists.
     * @param lastmodified the last modification time of this resource.
     */
    public Resource(String name, boolean exists, long lastmodified) {
        this(name, exists, lastmodified, false);
    }

    /**
     * Sets the name, lastmodified flag, exists flag, and directory flag.
     *
     * @param name relative path of the resource.  Expects
     * &quot;/&quot; to be used as the directory separator.
     * @param exists if true the resource exists
     * @param lastmodified the last modification time of the resource
     * @param directory    if true, this resource is a directory
     */
    public Resource(String name, boolean exists, long lastmodified,
            boolean directory) {
        this(name, exists, lastmodified, directory, UNKNOWN_SIZE);
    }

    /**
     * Sets the name, lastmodified flag, exists flag, directory flag, and size.
     *
     * @param name relative path of the resource.  Expects
     * &quot;/&quot; to be used as the directory separator.
     * @param exists if true the resource exists
     * @param lastmodified the last modification time of the resource
     * @param directory    if true, this resource is a directory
     * @param size the size of this resource.
     */
    public Resource(String name, boolean exists, long lastmodified,
            boolean directory, long size) {
        this.name = name;
        setName(name);
        setExists(exists);
        setLastModified(lastmodified);
        setDirectory(directory);
        setSize(size);
    }

    /**
     * Name attribute will contain the path of a file relative to the
     * root directory of its fileset or the recorded path of a zip
     * entry.
     *
     * <p>example for a file with fullpath /var/opt/adm/resource.txt
     * in a file set with root dir /var/opt it will be
     * adm/resource.txt.</p>
     *
     * <p>&quot;/&quot; will be used as the directory separator.</p>
     * @return the name of this resource.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this Resource.
     * @param name relative path of the resource.  Expects
     * &quot;/&quot; to be used as the directory separator.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The exists attribute tells whether a file exists.
     * @return true if this resource exists.
     */
    public boolean isExists() {
        return exists;
    }

    /**
     * Set the exists attribute.
     * @param exists if true, this resource exists.
     */
    public void setExists(boolean exists) {
        this.exists = exists;
    }

    /**
     * Tells the modification time in milliseconds since 01.01.1970 .
     *
     * @return 0 if the resource does not exist to mirror the behavior
     * of {@link java.io.File File}.
     */
    public long getLastModified() {
        return !exists || lastmodified < 0 ? 0L : lastmodified;
    }

    /**
     * Set the last modification attribute.
     * @param lastmodified the modification time in milliseconds since 01.01.1970.
     */
    public void setLastModified(long lastmodified) {
        this.lastmodified = lastmodified;
    }

    /**
     * Tells if the resource is a directory.
     * @return boolean flag indicating if the resource is a directory.
     */
    public boolean isDirectory() {
        return directory;
    }

    /**
     * Set the directory attribute.
     * @param directory if true, this resource is a directory.
     */
    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    /**
     * Set the size of this Resource.
     * @param size the size, as a long.
     */
    public void setSize(long size) {
        this.size = (size > UNKNOWN_SIZE) ? size : UNKNOWN_SIZE;
    }

    /**
     * Get the size of this Resource.
     * @return the size, as a long, 0 if the Resource does not exist (for
     *         compatibility with java.io.File), or UNKNOWN_SIZE if not known.
     */
    public long getSize() {
        return (exists) ? size : 0L;
    }

    /**
     * Clone this Resource.
     * @return copy of this.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnse) {
            throw new Error("CloneNotSupportedException for a "
                    + "Clonable Resource caught?");
        }
    }

    /**
     * Delegates to a comparison of names.
     * @param other the object to compare to.
     * @return a negative integer, zero, or a positive integer as this Resource
     *         is less than, equal to, or greater than the specified Resource.
     */
    public int compareTo(Object other) {
        if (!(other instanceof Resource)) {
            throw new IllegalArgumentException("Can only be compared with "
                    + "Resources");
        }
        Resource r = (Resource) other;
        return getName().compareTo(r.getName());
    }
}