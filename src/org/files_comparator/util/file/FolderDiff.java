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

package org.files_comparator.util.file;

import java.util.Collection;

import org.files_comparator.util.node.FilesComparatorDiffNode;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class FolderDiff {

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public enum Mode {
        ONE_WAY, TWO_WAY;
    }

    private String rightFolderShortName;
    private String leftFolderShortName;
    private String rightFolderName;
    private String leftFolderName;
    @SuppressWarnings("unused")
    private Mode mode;

    public FolderDiff(Mode mode) {
        this.mode = mode;
    }

    protected void setLeftFolderShortName(String leftFolderShortName) {
        this.leftFolderShortName = leftFolderShortName;
    }

    public String getLeftFolderShortName() {
        return leftFolderShortName;
    }

    protected void setRightFolderShortName(String rightFolderShortName) {
        this.rightFolderShortName = rightFolderShortName;
    }

    public String getRightFolderShortName() {
        return rightFolderShortName;
    }

    protected void setLeftFolderName(String leftFolderName) {
        this.leftFolderName = leftFolderName;
    }

    public String getLeftFolderName() {
        return leftFolderName;
    }

    protected void setRightFolderName(String rightFolderName) {
        this.rightFolderName = rightFolderName;
    }

    public String getRightFolderName() {
        return rightFolderName;
    }

    public abstract FilesComparatorDiffNode getRootNode();

    public abstract Collection<FilesComparatorDiffNode> getNodes();

    public void refresh() {
        diff();
    }

    public abstract void diff();
}
