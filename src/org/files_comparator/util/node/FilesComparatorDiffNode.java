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

package org.files_comparator.util.node;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import open_teradata_viewer.plugin.FilesComparatorException;

import org.files_comparator.diff.FilesComparatorDiff;
import org.files_comparator.diff.FilesComparatorRevision;
import org.files_comparator.settings.FilesComparatorSettings;
import org.files_comparator.ui.StatusBar;
import org.files_comparator.ui.text.BufferDocumentIF;
import org.files_comparator.util.Ignore;
import org.files_comparator.util.file.CompareUtil;
import org.files_comparator.util.file.cmd.AbstractCmd;
import org.files_comparator.util.file.cmd.CopyFileCmd;
import org.files_comparator.util.file.cmd.RemoveFileCmd;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorDiffNode implements TreeNode {

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public enum Compare {
        Equal, NotEqual, RightMissing, LeftMissing, BothMissing;
    }

    private String text;
    private String name;
    private String id;
    private String shortName;
    private String parentName;
    private FilesComparatorDiffNode parent;
    private List<FilesComparatorDiffNode> children;
    private BufferNode nodeLeft;
    private BufferNode nodeRight;
    private boolean leaf;
    private Compare compareState;
    private FilesComparatorDiff diff;
    private FilesComparatorRevision revision;
    private Ignore ignore;

    public FilesComparatorDiffNode(String name, boolean leaf) {
        this.name = name;
        this.shortName = name;
        this.leaf = leaf;

        ignore = FilesComparatorSettings.getInstance().getEditor().getIgnore();

        children = new ArrayList();
        calculateNames();
    }

    public String getId() {
        return id;
    }

    private void initId() {
        id = (nodeLeft != null ? nodeLeft.getName() : "x")
                + (nodeRight != null ? nodeRight.getName() : "x");
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public Ignore getIgnore() {
        return ignore;
    }

    public String getParentName() {
        return parentName;
    }

    public void addChild(FilesComparatorDiffNode child) {
        children.add(child);
        child.setParent(this);
    }

    private void setParent(FilesComparatorDiffNode parent) {
        this.parent = parent;
    }

    public void setBufferNodeLeft(BufferNode bufferNode) {
        nodeLeft = bufferNode;
        initId();
    }

    public BufferNode getBufferNodeLeft() {
        return nodeLeft;
    }

    public void setBufferNodeRight(BufferNode bufferNode) {
        nodeRight = bufferNode;
        initId();
    }

    public BufferNode getBufferNodeRight() {
        return nodeRight;
    }

    public List<FilesComparatorDiffNode> getChildren() {
        return children;
    }

    public Enumeration<FilesComparatorDiffNode> children() {
        return Collections.enumeration(children);
    }

    public boolean getAllowsChildren() {
        return isLeaf();
    }

    public FilesComparatorDiffNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    public int getChildCount() {
        return children.size();
    }

    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    public FilesComparatorDiffNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return leaf;
    }

    private void calculateNames() {
        int index;

        index = name.lastIndexOf(File.separator);
        if (index == -1) {
            parentName = null;
            return;
        }

        parentName = name.substring(0, index);
        shortName = name.substring(index + 1);
    }

    public AbstractCmd getCopyToRightCmd() throws Exception {
        if (nodeLeft.exists() && nodeLeft instanceof FileNode
                && nodeRight instanceof FileNode) {
            return new CopyFileCmd(this, (FileNode) nodeLeft,
                    (FileNode) nodeRight);
        }

        return null;
    }

    public AbstractCmd getCopyToLeftCmd() throws Exception {
        if (nodeRight.exists() && nodeLeft instanceof FileNode
                && nodeRight instanceof FileNode) {
            return new CopyFileCmd(this, (FileNode) nodeRight,
                    (FileNode) nodeLeft);
        }

        return null;
    }

    public AbstractCmd getRemoveLeftCmd() throws Exception {
        if (nodeLeft instanceof FileNode) {
            return new RemoveFileCmd(this, (FileNode) nodeLeft);
        }

        return null;
    }

    public AbstractCmd getRemoveRightCmd() throws Exception {
        if (nodeRight instanceof FileNode) {
            return new RemoveFileCmd(this, (FileNode) nodeRight);
        }

        return null;
    }

    public void compareContents() {
        boolean equals;

        if (!nodeLeft.exists() && !nodeRight.exists()) {
            setCompareState(Compare.BothMissing);
            return;
        }

        if (nodeLeft.exists() && !nodeRight.exists()) {
            setCompareState(Compare.RightMissing);
            return;
        }

        if (!nodeLeft.exists() && nodeRight.exists()) {
            setCompareState(Compare.LeftMissing);
            return;
        }

        if (!isLeaf()) {
            setCompareState(Compare.Equal);
            return;
        }

        equals = CompareUtil.contentEquals(nodeLeft, nodeRight, ignore);
        setCompareState(equals ? Compare.Equal : Compare.NotEqual);
    }

    public void diff() throws FilesComparatorException {
        BufferDocumentIF documentLeft;
        BufferDocumentIF documentRight;
        Object[] left, right;

        StatusBar.getInstance().start();

        documentLeft = null;
        documentRight = null;

        if (nodeLeft != null) {
            documentLeft = nodeLeft.getDocument();
            StatusBar.getInstance().setState("Reading left : %s",
                    nodeLeft.getName());
            if (documentLeft != null) {
                documentLeft.read();
            }
        }

        if (nodeRight != null) {
            documentRight = nodeRight.getDocument();
            StatusBar.getInstance().setState("Reading right: %s",
                    nodeRight.getName());
            if (documentRight != null) {
                documentRight.read();
            }
        }

        StatusBar.getInstance().setState("Calculating differences");
        diff = new FilesComparatorDiff();
        left = documentLeft == null ? null : documentLeft.getLines();
        right = documentRight == null ? null : documentRight.getLines();

        revision = diff.diff(left, right, ignore);
        StatusBar.getInstance().setState("Ready calculating differences");
        StatusBar.getInstance().stop();
    }

    public FilesComparatorDiff getDiff() {
        return diff;
    }

    public FilesComparatorRevision getRevision() {
        return revision;
    }

    public void setCompareState(Compare state) {
        compareState = state;
    }

    public boolean isCompareEqual(Compare state) {
        return compareState == state;
    }

    public void print(String indent) {
        ApplicationFrame.getInstance().getConsole()
                .println(indent + shortName + " (" + compareState + ")");
        indent += "  ";
        for (FilesComparatorDiffNode node : children) {
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
                    text = name.substring(pn.length() + 1);
                }
            }
        }

        return text;
    }
}