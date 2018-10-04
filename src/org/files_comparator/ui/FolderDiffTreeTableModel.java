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

import org.files_comparator.ui.swing.table.FilesComparatorTreeTableModel;
import org.files_comparator.ui.util.ImageUtil;
import org.files_comparator.util.node.BufferNode;
import org.files_comparator.util.node.FilesComparatorDiffNode;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@SuppressWarnings("unchecked")
public class FolderDiffTreeTableModel extends FilesComparatorTreeTableModel {

    private Column fileNameColumn;
    private Column leftSizeColumn;
    private Column leftStateColumn;
    private Column rightStateColumn;
    private Column rightSizeColumn;

    public FolderDiffTreeTableModel() {
        fileNameColumn = addColumn("fileName", null, "File", null, -1, false);
        leftSizeColumn = addColumn("leftSize", "Left", "Size", Integer.class,
                8, false);
        leftStateColumn = addColumn("leftState", "Left", "L", Icon.class, 3,
                false);
        rightStateColumn = addColumn("rightState", "Right", "R", Icon.class, 3,
                false);
        rightSizeColumn = addColumn("rightSize", "Right", "Size",
                Integer.class, 8, false);
    }

    public Object getValueAt(Object objectNode, Column column) {
        UINode uiNode;
        FilesComparatorDiffNode diffNode;
        BufferNode bufferNode;

        uiNode = (UINode) objectNode;
        diffNode = uiNode.getDiffNode();

        if (column == fileNameColumn) {
            return uiNode.toString();
        }

        if (column == leftStateColumn) {
            return ImageUtil.getSmallImageIcon(getLeftStateIconName(diffNode));
        }

        if (column == leftSizeColumn) {
            if (diffNode == null) {
                return "";
            }

            bufferNode = diffNode.getBufferNodeLeft();
            if (bufferNode == null) {
                return "";
            }

            return bufferNode.getSize();
        }

        if (column == rightStateColumn) {
            return ImageUtil.getSmallImageIcon(getRightStateIconName(diffNode));
        }

        if (column == rightSizeColumn) {
            if (diffNode == null) {
                return "";
            }

            bufferNode = diffNode.getBufferNodeRight();
            if (bufferNode == null) {
                return "";
            }

            return bufferNode.getSize();
        }

        return null;
    }

    public void setValueAt(Object value, Object objectNode, Column column) {
    }

    private String getLeftStateIconName(FilesComparatorDiffNode diffNode) {
        if (diffNode != null) {
            if (diffNode
                    .isCompareEqual(FilesComparatorDiffNode.Compare.NotEqual)) {
                return "stock_changed2";
            }

            if (diffNode
                    .isCompareEqual(FilesComparatorDiffNode.Compare.LeftMissing)
                    || diffNode
                            .isCompareEqual(FilesComparatorDiffNode.Compare.BothMissing)) {
                return "stock_deleted3";
            }
        }

        return "stock_equal";
    }

    private String getRightStateIconName(FilesComparatorDiffNode diffNode) {
        if (diffNode != null) {
            if (diffNode
                    .isCompareEqual(FilesComparatorDiffNode.Compare.NotEqual)) {
                return "stock_changed2";
            }

            if (diffNode
                    .isCompareEqual(FilesComparatorDiffNode.Compare.RightMissing)
                    || diffNode
                            .isCompareEqual(FilesComparatorDiffNode.Compare.BothMissing)) {
                return "stock_deleted3";
            }
        }

        return "stock_equal";
    }
}
