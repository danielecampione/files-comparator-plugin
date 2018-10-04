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

package org.files_comparator.util.file.cmd;

import java.io.File;

import javax.swing.undo.CannotUndoException;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

import org.files_comparator.util.file.FileUtil;
import org.files_comparator.util.node.FileNode;
import org.files_comparator.util.node.FilesComparatorDiffNode;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class RemoveFileCmd extends AbstractCmd {

    private static final long serialVersionUID = -3070321767148403269L;

    private FilesComparatorDiffNode diffNode;
    private FileNode fileNode;

    public RemoveFileCmd(FilesComparatorDiffNode diffNode, FileNode fileNode) {
        this.diffNode = diffNode;
        this.fileNode = fileNode;
    }

    public void createCommands() throws Exception {
        addCommand(new RemoveCommand(fileNode.getFile()));
        addFinallyCommand(new ResetCommand(fileNode));
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class RemoveCommand extends Command {

        private File file;
        private File originalFile;

        RemoveCommand(File file) {
            this.file = file;
        }

        public void execute() throws Exception {
            if (file.exists()) {
                originalFile = FileUtil.createTempFile("files-comparator",
                        "backup");

                if (debug) {
                    ApplicationFrame.getInstance().getConsole()
                            .println("copy : " + file + " -> " + originalFile);
                }

                FileUtil.copy(file, originalFile);
            }

            if (debug) {
                ApplicationFrame.getInstance().getConsole()
                        .println("delete : " + file);
            }
            file.delete();
        }

        public void undo() {
            try {
                if (originalFile != null) {
                    if (debug) {
                        ApplicationFrame
                                .getInstance()
                                .getConsole()
                                .println(
                                        "copy : " + originalFile + " -> "
                                                + file);
                    }
                    FileUtil.copy(originalFile, file);
                }
            } catch (Exception e) {
                throw new CannotUndoException();
            }
        }

        @Override
        public void discard() {
            if (originalFile != null) {
                if (debug) {
                    ApplicationFrame.getInstance().getConsole()
                            .println("delete : " + originalFile);
                }
                originalFile.delete();
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class ResetCommand extends Command {

        private FileNode fileNode;

        ResetCommand(FileNode fileNode) {
            this.fileNode = fileNode;
        }

        public void execute() throws Exception {
            reset();
        }

        public void undo() {
            reset();
        }

        @Override
        public void discard() {
            reset();
        }

        private void reset() {
            fileNode.resetContent();
            diffNode.compareContents();
        }
    }
}