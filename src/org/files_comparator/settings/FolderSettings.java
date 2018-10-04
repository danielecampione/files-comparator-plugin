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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.files_comparator.util.conf.AbstractConfigurationElement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class FolderSettings extends AbstractConfigurationElement {

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public enum FolderView {

        fileView("File view"), directoryView("Folder view"), packageView(
                "Package view");

        // instance variables:
        private String text;

        private FolderView(String text) {
            this.text = text;
        }

        public String toString() {
            return text;
        }
    }

    // Instance variables:
    @XmlElement
    private FolderView view = FolderView.packageView;
    @XmlElement
    private boolean onlyLeft = true;
    @XmlElement
    private boolean leftRightChanged = true;
    @XmlElement
    private boolean onlyRight = false;
    @XmlElement
    private boolean leftRightUnChanged = false;

    public FolderSettings() {
    }

    public FolderView getView() {
        return view;
    }

    public void setView(FolderView view) {
        if (this.view != view) {
            this.view = view;
            fireChanged();
        }
    }

    public void setOnlyLeft(boolean onlyLeft) {
        if (this.onlyLeft != onlyLeft) {
            this.onlyLeft = onlyLeft;
            fireChanged();
        }
    }

    public boolean getOnlyLeft() {
        return onlyLeft;
    }

    public void setLeftRightChanged(boolean leftRightChanged) {
        if (this.leftRightChanged != leftRightChanged) {
            this.leftRightChanged = leftRightChanged;
            fireChanged();
        }
    }

    public boolean getLeftRightChanged() {
        return leftRightChanged;
    }

    public void setOnlyRight(boolean onlyRight) {
        if (this.onlyRight != onlyRight) {
            this.onlyRight = onlyRight;
            fireChanged();
        }
    }

    public boolean getOnlyRight() {
        return onlyRight;
    }

    public void setLeftRightUnChanged(boolean leftRightUnChanged) {
        if (this.leftRightUnChanged != leftRightUnChanged) {
            this.leftRightUnChanged = leftRightUnChanged;
            fireChanged();
        }
    }

    public boolean getLeftRightUnChanged() {
        return leftRightUnChanged;
    }
}
