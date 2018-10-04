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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.TreeNode;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

import org.files_comparator.ui.swing.table.FilesComparatorTreeTableModel;
import org.files_comparator.util.node.FilesComparatorDiffNode;
import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class UINode implements TreeTableNode, Comparable<UINode> {

    private FilesComparatorTreeTableModel treeTableModel;
    private String text;
    private String name;
    private boolean leaf;
    private FilesComparatorDiffNode diffNode;
    private UINode parent;
    private List<UINode> children = new ArrayList<UINode>();
    private Map<String, UINode> childrenMap = new HashMap<String, UINode>();
    private boolean checkSort;

    public UINode(FilesComparatorTreeTableModel treeTableModel,
            FilesComparatorDiffNode diffNode) {
        this.treeTableModel = treeTableModel;
        this.diffNode = diffNode;

        this.name = diffNode.getName();
        this.leaf = diffNode.isLeaf();
    }

    public UINode(FilesComparatorTreeTableModel treeTableModel, String name,
            boolean leaf) {
        assert name != null;

        this.treeTableModel = treeTableModel;
        this.name = name;
        this.leaf = leaf;
    }

    public String getName() {
        return name;
    }

    public FilesComparatorDiffNode getDiffNode() {
        return diffNode;
    }

    public UINode addChild(UINode child) {
        UINode c;

        c = childrenMap.get(child.getName());
        if (c == null) {
            childrenMap.put(child.getName(), child);
            children.add(child);
            child.setParent(this);
            checkSort = true;

            c = child;
        }

        return c;
    }

    private void setParent(UINode parent) {
        this.parent = parent;
    }

    public List<UINode> getChildren() {
        checkSort();
        return children;
    }

    public Enumeration<UINode> children() {
        checkSort();
        return Collections.enumeration(children);
    }

    public boolean getAllowsChildren() {
        return isLeaf();
    }

    public UINode getChildAt(int childIndex) {
        checkSort();
        return children.get(childIndex);
    }

    public int getChildCount() {
        checkSort();
        return children.size();
    }

    public int getIndex(TreeNode node) {
        checkSort();
        return children.indexOf(node);
    }

    public UINode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return leaf;
    }

    private void checkSort() {
        if (checkSort) {
            Collections.sort(children);
            checkSort = false;
        }
    }

    public void print(String indent) {
        ApplicationFrame.getInstance().getConsole().println(indent + name);
        indent += "  ";
        checkSort();
        for (UINode node : children) {
            node.print(indent);
        }
    }

    @Override
    public String toString() {
        String pn;

        if (text == null) {
            text = name;
            if (parent != null) {
                pn = parent.getName();
                if (name.startsWith(pn)) {
                    text = name.substring(pn.length());
                    if (text.startsWith(File.separator)) {
                        text = text.substring(1);
                    }
                }
            }
        }

        return text;
    }

    public int compareTo(UINode o) {
        return toString().compareTo(o.toString());
    }

    public Object getValueAt(int column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getColumnCount() {
        return treeTableModel.getColumnCount();
    }

    public boolean isEditable(int column) {
        return false;
    }

    public void setValueAt(Object aValue, int column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getUserObject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setUserObject(Object userObject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}