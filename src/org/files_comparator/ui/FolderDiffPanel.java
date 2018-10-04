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

package org.files_comparator.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.undo.CompoundEdit;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.settings.EditorSettings;
import org.files_comparator.settings.FilesComparatorSettings;
import org.files_comparator.settings.FolderSettings;
import org.files_comparator.ui.action.ActionHandler;
import org.files_comparator.ui.action.FilesComparatorAction;
import org.files_comparator.ui.swing.table.FilesComparatorTreeTableModel;
import org.files_comparator.ui.util.Colors;
import org.files_comparator.ui.util.ImageUtil;
import org.files_comparator.ui.util.SwingUtil;
import org.files_comparator.util.conf.ConfigurationListenerIF;
import org.files_comparator.util.file.FolderDiff;
import org.files_comparator.util.file.cmd.AbstractCmd;
import org.files_comparator.util.node.FilesComparatorDiffNode;
import org.jdesktop.swingworker.SwingWorker;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FolderDiffPanel extends FolderDiffForm implements
        ConfigurationListenerIF {

    private static final long serialVersionUID = 1018963206109894888L;

    private FilesComparatorPanel mainPanel;
    private FolderDiff diff;
    private ActionHandler actionHandler;
    private FilesComparatorTreeTableModel treeTableModel;

    FolderDiffPanel(FilesComparatorPanel mainPanel, FolderDiff diff) {
        this.mainPanel = mainPanel;
        this.diff = diff;

        init();
    }

    private void init() {
        actionHandler = new ActionHandler();

        hierarchyComboBox.setModel(new DefaultComboBoxModel(
                FolderSettings.FolderView.values()));
        hierarchyComboBox.setSelectedItem(getFolderSettings().getView());
        hierarchyComboBox.setFocusable(false);

        initActions();

        onlyRightButton.setText(null);
        onlyRightButton.setIcon(ImageUtil
                .getImageIcon("files-comparator_only-right"));
        onlyRightButton.setFocusable(false);
        onlyRightButton.setSelected(getFolderSettings().getOnlyRight());

        leftRightChangedButton.setText(null);
        leftRightChangedButton.setIcon(ImageUtil
                .getImageIcon("files-comparator_left-right-changed"));
        leftRightChangedButton.setFocusable(false);
        leftRightChangedButton.setSelected(getFolderSettings()
                .getLeftRightChanged());

        onlyLeftButton.setText(null);
        onlyLeftButton.setIcon(ImageUtil
                .getImageIcon("files-comparator_only-left"));
        onlyLeftButton.setFocusable(false);
        onlyLeftButton.setSelected(getFolderSettings().getOnlyLeft());

        leftRightUnChangedButton.setText(null);
        leftRightUnChangedButton.setIcon(ImageUtil
                .getImageIcon("files-comparator_left-right-unchanged"));
        leftRightUnChangedButton.setFocusable(false);
        leftRightUnChangedButton.setSelected(getFolderSettings()
                .getLeftRightUnChanged());

        expandAllButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        expandAllButton.setContentAreaFilled(false);
        expandAllButton.setText(null);
        expandAllButton
                .setIcon(ImageUtil.getSmallImageIcon("stock_expand-all"));
        expandAllButton.setPressedIcon(ImageUtil
                .createDarkerIcon((ImageIcon) expandAllButton.getIcon()));
        expandAllButton.setFocusable(false);

        collapseAllButton
                .setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        collapseAllButton.setContentAreaFilled(false);
        collapseAllButton.setText(null);
        collapseAllButton.setIcon(ImageUtil
                .getSmallImageIcon("stock_collapse-all"));
        collapseAllButton.setPressedIcon(ImageUtil
                .createDarkerIcon((ImageIcon) collapseAllButton.getIcon()));
        collapseAllButton.setFocusable(false);

        folder1Label.init();
        folder1Label.setText(diff.getLeftFolderName(),
                diff.getRightFolderName());

        folder2Label.init();
        folder2Label.setText(diff.getRightFolderName(),
                diff.getLeftFolderName());

        folderTreeTable.setTreeTableModel(getTreeTableModel());

        folderTreeTable
                .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        folderTreeTable.setToggleClickCount(1);
        folderTreeTable.setTerminateEditOnFocusLost(false);
        folderTreeTable.setRowSelectionAllowed(true);
        folderTreeTable.addMouseListener(getMouseListener());
        folderTreeTable.expandAll();

        folderTreeTable.addHighlighter(new ColorHighlighter(
                HighlightPredicate.EVEN, Color.white, Color.black));
        folderTreeTable.addHighlighter(new ColorHighlighter(
                HighlightPredicate.ODD, Colors.getTableRowHighLighterColor(),
                Color.black));
        //folderTreeTable.setHighlighters(new AlternateRowHighlighter(Color.white,
        //Colors.getTableRowHighLighterColor(), Color.black));

        FilesComparatorSettings.getInstance().addConfigurationListener(this);
    }

    private void initActions() {
        FilesComparatorAction action;

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_SELECT_NEXT_ROW);
        installKey("DOWN", action);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_SELECT_PREVIOUS_ROW);
        installKey("UP", action);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_NEXT_NODE);
        installKey("RIGHT", action);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_PREVIOUS_NODE);
        installKey("LEFT", action);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_OPEN_FILE_COMPARISON);
        action.setIcon("stock_compare");
        compareButton.setAction(action);
        compareButton.setText(null);
        compareButton.setFocusable(false);
        compareButton.setDisabledIcon(action.getTransparentSmallImageIcon());
        installKey("ENTER", action);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_OPEN_FILE_COMPARISON_BACKGROUND);
        action.setIcon("stock_compare");
        installKey("alt ENTER", action);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_EXPAND_ALL);
        expandAllButton.setAction(action);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_COLLAPSE_ALL);
        collapseAllButton.setAction(action);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_REFRESH);
        action.setIcon("stock_refresh");
        refreshButton.setAction(action);
        refreshButton.setText(null);
        refreshButton.setFocusable(false);
        refreshButton.setDisabledIcon(action.getTransparentSmallImageIcon());

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_REMOVE_RIGHT);
        action.setIcon("stock_delete");
        deleteRightButton.setAction(action);
        deleteRightButton.setText(null);
        deleteRightButton.setFocusable(false);
        deleteRightButton
                .setDisabledIcon(action.getTransparentSmallImageIcon());
        installKey("ctrl alt RIGHT", action);
        installKey("ctrl alt KP_RIGHT", action);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_REMOVE_LEFT);
        action.setIcon("stock_delete");
        deleteLeftButton.setAction(action);
        deleteLeftButton.setText(null);
        deleteLeftButton.setFocusable(false);
        deleteLeftButton.setDisabledIcon(action.getTransparentSmallImageIcon());
        installKey("ctrl alt LEFT", action);
        installKey("ctrl alt KP_LEFT", action);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_COPY_TO_LEFT);
        action.setIcon("stock_left");
        copyToLeftButton.setAction(action);
        copyToLeftButton.setText(null);
        copyToLeftButton.setFocusable(false);
        copyToLeftButton.setDisabledIcon(action.getTransparentSmallImageIcon());
        installKey("alt LEFT", action);
        installKey("alt KP_LEFT", action);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_COPY_TO_RIGHT);
        action.setIcon("stock_right");
        copyToRightButton.setAction(action);
        copyToRightButton.setText(null);
        copyToRightButton.setFocusable(false);
        copyToRightButton
                .setDisabledIcon(action.getTransparentSmallImageIcon());
        installKey("alt RIGHT", action);
        installKey("alt KP_RIGHT", action);

        //deleteRightButton.setVisible(false);
        //copyToRightButton.setVisible(false);
        //copyToLeftButton.setVisible(false);
        //deleteLeftButton.setVisible(false);

        action = actionHandler.createAction(this,
                mainPanel.actions.FOLDER_FILTER);
        onlyRightButton.setAction(action);
        leftRightChangedButton.setAction(action);
        onlyLeftButton.setAction(action);
        leftRightUnChangedButton.setAction(action);
        hierarchyComboBox.setAction(action);
    }

    private void installKey(String key, FilesComparatorAction action) {
        SwingUtil.installKey(folderTreeTable, key, action);
    }

    public String getTitle() {
        return diff.getLeftFolderShortName() + " - "
                + diff.getRightFolderShortName();
    }

    private TreeTableNode getRootNode() {
        return filter(diff.getRootNode());
    }

    private TreeTableNode filter(FilesComparatorDiffNode diffNode) {
        List<FilesComparatorDiffNode> nodes;
        UINode uiParentNode;
        UINode uiNode;
        String parentName;
        UINode rootNode;
        FilesComparatorDiffNode parent;
        Object hierarchy;

        // Filter the nodes:
        nodes = new ArrayList<FilesComparatorDiffNode>();
        for (FilesComparatorDiffNode node : diff.getNodes()) {
            if (!node.isLeaf()) {
                continue;
            }

            if (node.isCompareEqual(FilesComparatorDiffNode.Compare.Equal)) {
                if (leftRightUnChangedButton.isSelected()) {
                    nodes.add(node);
                }
            } else if (node
                    .isCompareEqual(FilesComparatorDiffNode.Compare.NotEqual)) {
                if (leftRightChangedButton.isSelected()) {
                    nodes.add(node);
                }
            } else if (node
                    .isCompareEqual(FilesComparatorDiffNode.Compare.RightMissing)) {
                if (onlyLeftButton.isSelected()) {
                    nodes.add(node);
                }
            } else if (node
                    .isCompareEqual(FilesComparatorDiffNode.Compare.LeftMissing)) {
                if (onlyRightButton.isSelected()) {
                    nodes.add(node);
                }
            }
        }

        rootNode = new UINode(getTreeTableModel(), "<root>", false);
        hierarchy = hierarchyComboBox.getSelectedItem();

        // Build the hierarchy:
        if (hierarchy == FolderSettings.FolderView.packageView) {
            for (FilesComparatorDiffNode node : nodes) {
                parent = node.getParent();
                uiNode = new UINode(getTreeTableModel(), node);

                if (parent != null) {
                    parentName = parent.getName();
                    uiParentNode = new UINode(getTreeTableModel(), parent);
                    uiParentNode = rootNode.addChild(uiParentNode);
                    uiParentNode.addChild(uiNode);
                } else {
                    rootNode.addChild(uiNode);
                }
            }
        } else if (hierarchy == FolderSettings.FolderView.fileView) {
            for (FilesComparatorDiffNode node : nodes) {
                rootNode.addChild(new UINode(getTreeTableModel(), node));
            }
        } else if (hierarchy == FolderSettings.FolderView.directoryView) {
            for (FilesComparatorDiffNode node : nodes) {
                addDirectoryViewNode(rootNode, node);
            }
        }

        return rootNode;
    }

    private void addDirectoryViewNode(UINode rootNode,
            FilesComparatorDiffNode node) {
        UINode parent;
        FilesComparatorDiffNode uiNode;
        List<FilesComparatorDiffNode> uiNodes;

        uiNodes = new ArrayList<FilesComparatorDiffNode>();
        do {
            uiNodes.add(node);
        } while ((node = node.getParent()) != null);

        Collections.reverse(uiNodes);

        parent = rootNode;
        for (int i = 1; i < uiNodes.size(); i++) {
            uiNode = uiNodes.get(i);
            parent = parent.addChild(new UINode(getTreeTableModel(), uiNode));
        }
    }

    public void doSelectPreviousRow(ActionEvent ae) {
        int row;

        row = folderTreeTable.getSelectedRow() - 1;
        row = row < 0 ? (folderTreeTable.getRowCount() - 1) : row;
        folderTreeTable.setRowSelectionInterval(row, row);
        folderTreeTable.scrollRowToVisible(row);
    }

    public void doSelectNextRow(ActionEvent ae) {
        int row;

        row = folderTreeTable.getSelectedRow() + 1;
        row = row >= folderTreeTable.getRowCount() ? 0 : row;
        folderTreeTable.setRowSelectionInterval(row, row);
        folderTreeTable.scrollRowToVisible(row);
    }

    public void doNextNode(ActionEvent ae) {
        int row;

        row = folderTreeTable.getSelectedRow();
        if (row == -1) {
            return;
        }

        if (folderTreeTable.isCollapsed(row)) {
            folderTreeTable.expandRow(row);
        }

        doSelectNextRow(ae);
    }

    public void doPreviousNode(ActionEvent ae) {
        int row;

        row = folderTreeTable.getSelectedRow();
        if (row == -1) {
            return;
        }

        if (folderTreeTable.isExpanded(row)) {
            folderTreeTable.collapseRow(row);
        }

        doSelectPreviousRow(ae);
    }

    public void doOpenFileComparisonBackground(ActionEvent ae) {
        doOpenFileComparison(ae, true);
    }

    public void doOpenFileComparison(ActionEvent ae) {
        doOpenFileComparison(ae, false);
    }

    private void doOpenFileComparison(ActionEvent ae, boolean background) {
        for (UINode uiNode : getSelectedUINodes()) {
            mainPanel.openFileComparison(uiNode.getDiffNode(), background);
        }
    }

    @Override
    public boolean checkExit() {
        return false;
    }

    public void doExpandAll(ActionEvent ae) {
        folderTreeTable.expandAll();
    }

    public void doCollapseAll(ActionEvent ae) {
        folderTreeTable.collapseAll();
    }

    public boolean isCopyToLeftEnabled() {
        return !getEditorSettings().getLeftsideReadonly();
    }

    public void doCopyToLeft(ActionEvent ae) {
        CompoundCommand cc;

        cc = new CompoundCommand();
        for (UINode uiNode : getSelectedUINodes()) {
            try {
                cc.add(uiNode, uiNode.getDiffNode().getCopyToLeftCmd());
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        }
        cc.execute();
    }

    public boolean isCopyToRightEnabled() {
        return !getEditorSettings().getRightsideReadonly();
    }

    public void doCopyToRight(ActionEvent ae) {
        CompoundCommand cc;

        cc = new CompoundCommand();
        for (UINode uiNode : getSelectedUINodes()) {
            try {
                cc.add(uiNode, uiNode.getDiffNode().getCopyToRightCmd());
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        }
        cc.execute();
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class CompoundCommand extends CompoundEdit {

        private static final long serialVersionUID = -6575351994747430968L;

        List<AbstractCmd> cmds;
        Map<AbstractCmd, UINode> uiNodeMap;

        CompoundCommand() {
            uiNodeMap = new HashMap<AbstractCmd, UINode>();
            cmds = new ArrayList<AbstractCmd>();
        }

        void add(UINode uiNode, AbstractCmd cmd) {
            if (cmd == null) {
                return;
            }

            uiNodeMap.put(cmd, uiNode);
            cmds.add(cmd);
        }

        void execute() {
            try {
                for (AbstractCmd cmd : cmds) {
                    cmd.execute();
                    addEdit(cmd);
                }
                end();

                getUndoHandler().add(this);
                compareContents();
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }

            check();
        }

        @Override
        public void redo() {
            super.redo();
            compareContents();
            check();
        }

        @Override
        public void undo() {
            super.undo();
            compareContents();
            check();
        }

        private void check() {
            mainPanel.checkActions();
            repaint();
        }

        private void compareContents() {
            for (AbstractCmd cmd : cmds) {
                uiNodeMap.get(cmd).getDiffNode().compareContents();
            }
        }
    }

    public void doRefresh(ActionEvent ae) {
        new RefreshAction().execute();
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class RefreshAction extends SwingWorker<String, Object> {

        RefreshAction() {
        }

        @Override
        public String doInBackground() {
            diff.refresh();

            return null;
        }

        @Override
        protected void done() {
            treeTableModel = null;
            folderTreeTable.setTreeTableModel(getTreeTableModel());
            folderTreeTable.expandAll();
        }
    }

    public boolean isRemoveRightEnabled() {
        return !getEditorSettings().getRightsideReadonly();
    }

    public void doRemoveRight(ActionEvent ae) {
        CompoundCommand cc;

        cc = new CompoundCommand();
        for (UINode uiNode : getSelectedUINodes()) {
            try {
                cc.add(uiNode, uiNode.getDiffNode().getRemoveRightCmd());
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        }
        cc.execute();
    }

    public boolean isRemoveLeftEnabled() {
        return !getEditorSettings().getLeftsideReadonly();
    }

    public void doRemoveLeft(ActionEvent ae) {
        CompoundCommand cc;

        cc = new CompoundCommand();
        for (UINode uiNode : getSelectedUINodes()) {
            try {
                cc.add(uiNode, uiNode.getDiffNode().getRemoveLeftCmd());
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        }
        cc.execute();
    }

    public void doFilter(ActionEvent ae) {
        ((FilesComparatorTreeTableModel) folderTreeTable.getTreeTableModel())
                .setRoot(getRootNode());
        folderTreeTable.expandAll();
    }

    private EditorSettings getEditorSettings() {
        return FilesComparatorSettings.getInstance().getEditor();
    }

    private FolderSettings getFolderSettings() {
        return FilesComparatorSettings.getInstance().getFolder();
    }

    private Set<UINode> getSelectedUINodes() {
        Set<UINode> result;
        TreePath path;
        UINode uiNode;

        result = new HashSet<UINode>();
        for (int row : folderTreeTable.getSelectedRows()) {
            path = folderTreeTable.getPathForRow(row);
            if (path == null) {
                continue;
            }

            uiNode = (UINode) path.getLastPathComponent();
            if (uiNode == null) {
                continue;
            }

            buildResult(result, uiNode);
        }

        return result;
    }

    private void buildResult(Set<UINode> result, UINode uiNode) {
        if (uiNode.isLeaf() && uiNode.getDiffNode() != null) {
            result.add(uiNode);
            return;
        }

        for (UINode node : uiNode.getChildren()) {
            buildResult(result, node);
        }
    }

    private MouseListener getMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                UINode uiNode;
                TreePath path;
                int row;
                boolean open;
                boolean background;
                List<FilesComparatorDiffNode> diffNodeList;
                int openCount;

                background = me.getClickCount() == 1
                        && me.getButton() == MouseEvent.BUTTON2;
                open = me.getClickCount() == 2 || background;

                if (open) {
                    row = ((JTable) me.getSource()).rowAtPoint(me.getPoint());

                    path = folderTreeTable.getPathForRow(row);
                    if (path == null) {
                        return;
                    }

                    uiNode = (UINode) path.getLastPathComponent();
                    if (uiNode == null) {
                        return;
                    }

                    diffNodeList = getDiffNodeList(uiNode);
                    if (diffNodeList.isEmpty()) {
                        return;
                    }

                    openCount = 0;
                    for (FilesComparatorDiffNode diffNode : diffNodeList) {
                        if (openCount++ > 20) {
                            break;
                        }

                        mainPanel.openFileComparison(diffNode, background);
                    }

                    // Hack to make it possible to select with the MIDDLE 
                    //   button of a mouse. 
                    if (folderTreeTable.getSelectedRow() != row) {
                        folderTreeTable.setRowSelectionInterval(row, row);
                    }

                    // Make sure that UP and DOWN keys work the way I want.
                    folderTreeTable.requestFocus();
                }
            }
        };
    }

    private List<FilesComparatorDiffNode> getDiffNodeList(UINode uiNode) {
        return new CollectDiffNodeLeaf(uiNode).getResult();
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class CollectDiffNodeLeaf {
        private Set<FilesComparatorDiffNode> diffNodeSet;

        CollectDiffNodeLeaf(UINode uiNode) {
            diffNodeSet = new HashSet<FilesComparatorDiffNode>();

            collectDiffNode(uiNode);
        }

        private void collectDiffNode(UINode uiNode) {
            FilesComparatorDiffNode diffNode;

            if (!uiNode.isLeaf()) {
                for (UINode childUINode : uiNode.getChildren()) {
                    collectDiffNode(childUINode);
                }
            } else {
                diffNode = uiNode.getDiffNode();
                if (diffNode != null) {
                    diffNodeSet.add(diffNode);
                }
            }
        }

        public List<FilesComparatorDiffNode> getResult() {
            return new ArrayList<FilesComparatorDiffNode>(diffNodeSet);
        }
    }

    private FilesComparatorTreeTableModel getTreeTableModel() {
        if (treeTableModel == null) {
            treeTableModel = createTreeTableModel();
            treeTableModel.setRoot(getRootNode());
        }

        return treeTableModel;
    }

    protected FilesComparatorTreeTableModel createTreeTableModel() {
        return new FolderDiffTreeTableModel();
    }

    @Override
    public void configurationChanged() {
        actionHandler.checkActions();
    }
}