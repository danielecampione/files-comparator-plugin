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

package org.files_comparator.ui;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

import org.files_comparator.ui.swing.table.FilesComparatorTreeTableModel;
import org.files_comparator.ui.util.ImageUtil;
import org.files_comparator.util.node.FilesComparatorDiffNode;
import org.files_comparator.util.node.VersionControlBaseNode;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@SuppressWarnings("unchecked")
public class VersionControlTreeTableModel extends FilesComparatorTreeTableModel {

    private Column fileNameColumn;
    private Column statusColumn;

    public VersionControlTreeTableModel() {
        fileNameColumn = addColumn("fileName", null, "File", null, -1, false);
        statusColumn = addColumn("status", "Left", "Status", Icon.class, 12,
                false);
    }

    public Object getValueAt(Object objectNode, Column column) {
        UINode uiNode;
        FilesComparatorDiffNode diffNode;
        VersionControlBaseNode vcbNode;

        uiNode = (UINode) objectNode;
        diffNode = uiNode.getDiffNode();

        if (column == fileNameColumn) {
            return uiNode.toString();
        }

        if (column == statusColumn) {
            vcbNode = (VersionControlBaseNode) diffNode.getBufferNodeLeft();
            if (vcbNode == null) {
                return "";
            }

            ApplicationFrame.getInstance().changeLog.append(vcbNode.getEntry()
                    .getStatus().getIconName()
                    + "\n");
            return ImageUtil.getImageIcon("16x16/"
                    + vcbNode.getEntry().getStatus().getIconName());
        }

        return null;
    }

    public void setValueAt(Object value, Object objectNode, Column column) {
    }
}
