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

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.ui.StatusBar;
import org.files_comparator.util.StopWatch;
import org.files_comparator.util.StringUtil;
import org.files_comparator.util.node.FileNode;
import org.files_comparator.util.node.FilesComparatorDiffNode;
import org.files_comparator.util.node.VersionControlBaseNode;
import org.files_comparator.vc.StatusResult;
import org.files_comparator.vc.VersionControlIF;
import org.files_comparator.vc.VersionControlUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class VersionControlDiff extends FolderDiff {

    private File directory;
    private FilesComparatorDiffNode rootNode;
    private Map<String, FilesComparatorDiffNode> nodes;

    public VersionControlDiff(File directory, Mode mode) {
        super(mode);

        this.directory = directory;

        try {
            setLeftFolderShortName(directory.getName());
            setRightFolderShortName("");
            setLeftFolderName(directory.getCanonicalPath());
            setRightFolderName("");
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
        FilesComparatorDiffNode node;
        StopWatch stopWatch;
        File file;
        List<VersionControlIF> versionControlList;
        VersionControlIF versionControl;
        StatusResult statusResult;
        FileNode fileNode;

        stopWatch = new StopWatch();
        stopWatch.start();

        StatusBar.getInstance().start();
        StatusBar.getInstance().setState("Start scanning directories...");

        rootNode = new FilesComparatorDiffNode("<root>", false);
        nodes = new HashMap<String, FilesComparatorDiffNode>();

        versionControlList = VersionControlUtil.getVersionControl(directory);
        if (versionControlList.isEmpty()) {
            return;
        }

        versionControl = versionControlList.get(0);

        statusResult = versionControl.executeStatus(directory);

        for (StatusResult.Entry entry : statusResult.getEntryList()) {
            //file = new File(statusResult.getPath(), entry.getName());
            file = new File(entry.getName());

            node = addNode(entry.getName(), !file.isDirectory());

            fileNode = new FileNode(entry.getName(), file);
            node.setBufferNodeLeft(new VersionControlBaseNode(versionControl,
                    entry, fileNode, file));
            node.setBufferNodeRight(fileNode);

            switch (entry.getStatus()) {
                case modified :
                case conflicted :
                case missing :
                case dontknow :
                    node.setCompareState(FilesComparatorDiffNode.Compare.NotEqual);
                    break;
                case unversioned :
                case added :
                    node.setCompareState(FilesComparatorDiffNode.Compare.LeftMissing);
                    break;
                case removed :
                    node.setCompareState(FilesComparatorDiffNode.Compare.RightMissing);
                    break;
                case clean :
                case ignored :
                    node.setCompareState(FilesComparatorDiffNode.Compare.Equal);
                    break;
            }
        }

        StatusBar.getInstance().setState(
                "Ready comparing directories (took "
                        + (stopWatch.getElapsedTime() / 1000) + " seconds)");
        StatusBar.getInstance().stop();
    }

    private FilesComparatorDiffNode addNode(String name, boolean leaf) {
        FilesComparatorDiffNode node;

        node = nodes.get(name);
        if (node == null) {
            node = addNode(new FilesComparatorDiffNode(name, leaf));
        }

        return node;
    }

    private FilesComparatorDiffNode addNode(FilesComparatorDiffNode node) {
        String parentName;
        FilesComparatorDiffNode parent;
        FileNode fn;

        nodes.put(node.getName(), node);

        parentName = node.getParentName();
        if (StringUtil.isEmpty(parentName)) {
            parent = rootNode;
        } else {
            parent = nodes.get(parentName);
            if (parent == null) {
                parent = addNode(new FilesComparatorDiffNode(parentName, false));
                fn = new FileNode(parentName, new File(directory, parentName));
                parent.setBufferNodeRight(fn);
                parent.setBufferNodeLeft(fn);
            }
        }

        parent.addChild(node);
        return node;
    }

    public void print() {
        rootNode.print("");
    }
}