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

package org.files_comparator.util.file;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.apache.files_comparator.tools.ant.DirectoryScanner;
import org.files_comparator.settings.util.Filter;
import org.files_comparator.ui.StatusBar;
import org.files_comparator.util.StopWatch;
import org.files_comparator.util.StringUtil;
import org.files_comparator.util.node.FileNode;
import org.files_comparator.util.node.FilesComparatorDiffNode;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DirectoryDiff extends FolderDiff {

    private File rightDirectory;
    private File leftDirectory;
    private FilesComparatorDiffNode rootNode;
    private Map<String, FilesComparatorDiffNode> nodes;
    private Filter filter;

    public DirectoryDiff(File leftDirectory, File rightDirectory,
            Filter filter, Mode mode) {
        super(mode);

        this.leftDirectory = leftDirectory;
        this.rightDirectory = rightDirectory;
        this.filter = filter;

        try {
            setLeftFolderShortName(leftDirectory.getName());
            setRightFolderShortName(rightDirectory.getName());
            setLeftFolderName(leftDirectory.getCanonicalPath());
            setRightFolderName(rightDirectory.getCanonicalPath());
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    public FilesComparatorDiffNode getRootNode() {
        return rootNode;
    }

    public Collection<FilesComparatorDiffNode> getNodes() {
        return nodes.values();
    }

    public void diff() {
        DirectoryScanner ds;
        FilesComparatorDiffNode node;
        StopWatch stopWatch;
        int numberOfNodes;
        int currentNumber;
        FileNode fn;

        stopWatch = new StopWatch();
        stopWatch.start();

        StatusBar.getInstance().start();
        StatusBar.getInstance().setState("Start scanning directories...");

        rootNode = new FilesComparatorDiffNode("<root>", false);
        nodes = new HashMap<String, FilesComparatorDiffNode>();

        ds = new DirectoryScanner();
        ds.setShowStateOn(true);
        ds.setBasedir(leftDirectory);
        if (filter != null) {
            ds.setIncludes(filter.getIncludes());
            ds.setExcludes(filter.getExcludes());
        }
        ds.setCaseSensitive(true);
        ds.scan();

        for (FileNode fileNode : ds.getIncludedFilesMap().values()) {
            node = addNode(fileNode.getName());
            node.setBufferNodeLeft(fileNode);
        }

        ds = new DirectoryScanner();
        ds.setShowStateOn(true);
        ds.setBasedir(rightDirectory);
        if (filter != null) {
            ds.setIncludes(filter.getIncludes());
            ds.setExcludes(filter.getExcludes());
        }
        ds.setCaseSensitive(true);
        ds.scan();

        for (FileNode fileNode : ds.getIncludedFilesMap().values()) {
            node = addNode(fileNode.getName());
            node.setBufferNodeRight(fileNode);
        }

        StatusBar.getInstance().setState("Comparing nodes...");
        numberOfNodes = nodes.size();
        currentNumber = 0;
        for (FilesComparatorDiffNode n : nodes.values()) {
            // Make sure that each node has it's opposite. 
            // This makes the following copying actions possible :
            // - copy 'left' to 'not existing'
            // - copy 'right' to 'not existing'
            if (n.getBufferNodeRight() == null || n.getBufferNodeLeft() == null) {
                if (n.getBufferNodeRight() == null) {
                    fn = (FileNode) n.getBufferNodeLeft();
                    fn = new FileNode(fn.getName(), new File(rightDirectory,
                            fn.getName()));
                    n.setBufferNodeRight(fn);
                } else {
                    fn = (FileNode) n.getBufferNodeRight();
                    fn = new FileNode(fn.getName(), new File(leftDirectory,
                            fn.getName()));
                    n.setBufferNodeLeft(fn);
                }
            }

            n.compareContents();

            StatusBar.getInstance().setProgress(++currentNumber, numberOfNodes);
        }

        StatusBar.getInstance().setState(
                "Ready comparing directories (took "
                        + (stopWatch.getElapsedTime() / 1000) + " seconds)");
        StatusBar.getInstance().stop();
    }

    private FilesComparatorDiffNode addNode(String name) {
        FilesComparatorDiffNode node;

        node = nodes.get(name);
        if (node == null) {
            node = addNode(new FilesComparatorDiffNode(name, true));
        }

        return node;
    }

    private FilesComparatorDiffNode addNode(FilesComparatorDiffNode node) {
        String parentName;
        FilesComparatorDiffNode parent;

        nodes.put(node.getName(), node);

        parentName = node.getParentName();
        if (StringUtil.isEmpty(parentName)) {
            parent = rootNode;
        } else {
            parent = nodes.get(parentName);
            if (parent == null) {
                parent = addNode(new FilesComparatorDiffNode(parentName, false));
                parent.setBufferNodeRight(new FileNode(parentName, new File(
                        rightDirectory, parentName)));
                parent.setBufferNodeLeft(new FileNode(parentName, new File(
                        leftDirectory, parentName)));
            }
        }

        parent.addChild(node);
        return node;
    }

    public void print() {
        rootNode.print("");
    }
}