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
import java.util.Collections;
import java.util.List;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.util.file.FileUtil;
import org.files_comparator.util.node.FileNode;
import org.files_comparator.util.node.FilesComparatorDiffNode;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class CopyFileCmd extends AbstractCmd {

    private static final long serialVersionUID = -6625225561600510507L;

    private FilesComparatorDiffNode diffNode;
    private FileNode fromFileNode;
    private FileNode toFileNode;

    public CopyFileCmd(FilesComparatorDiffNode diffNode, FileNode fromFileNode,
            FileNode toFileNode) throws Exception {
        this.diffNode = diffNode;
        this.fromFileNode = fromFileNode;
        this.toFileNode = toFileNode;
    }

    public void createCommands() throws Exception {
        List<File> parentFiles;
        File fromFile;
        File toFile;

        fromFile = fromFileNode.getFile().getCanonicalFile();
        toFile = toFileNode.getFile().getCanonicalFile();

        parentFiles = FileUtil.getParentFiles(toFile);
        Collections.reverse(parentFiles);
        for (File parentFile : parentFiles) {
            if (!parentFile.exists()) {
                addCommand(new MkDirCommand(parentFile));
            }
        }
        addCommand(new CopyCommand(fromFile, toFile));
        addFinallyCommand(new ResetCommand(toFileNode));
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class MkDirCommand extends Command {

        private File dirFile;

        MkDirCommand(File dirFile) throws Exception {
            this.dirFile = dirFile;
        }

        public void execute() throws Exception {
            if (debug) {
                ApplicationFrame.getInstance().getConsole()
                        .println("mkdir : " + dirFile);
            }
            dirFile.mkdir();
        }

        public void undo() {
            if (debug) {
                ApplicationFrame.getInstance().getConsole()
                        .println("rmdir : " + dirFile);
            }
            dirFile.delete();
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class CopyCommand extends Command {

        private File fromFile;
        private File toFile;
        private File backupFile;
        private boolean toFileExists;

        CopyCommand(File fromFile, File toFile) {
            this.fromFile = fromFile;
            this.toFile = toFile;
        }

        public void execute() throws Exception {
            if (toFile.exists()) {
                toFileExists = true;

                backupFile = FileUtil.createTempFile("files-comparator",
                        "backup");

                if (debug) {
                    ApplicationFrame.getInstance().getConsole()
                            .println("copy : " + toFile + " -> " + backupFile);
                }

                FileUtil.copy(toFile, backupFile);
            }

            if (debug) {
                ApplicationFrame.getInstance().getConsole()
                        .println("copy : " + fromFile + " -> " + toFile);
            }

            FileUtil.copy(fromFile, toFile);
        }

        public void undo() {
            try {
                if (toFileExists) {
                    if (backupFile != null) {
                        if (debug) {
                            ApplicationFrame
                                    .getInstance()
                                    .getConsole()
                                    .println(
                                            "copy : " + backupFile + " -> "
                                                    + toFile);
                        }

                        FileUtil.copy(backupFile, toFile);
                        backupFile.delete();
                        backupFile = null;
                    }
                } else {
                    if (debug) {
                        ApplicationFrame.getInstance().getConsole()
                                .println("delete : " + toFile);
                    }

                    toFile.delete();
                }
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        }

        @Override
        public void discard() {
            if (backupFile != null) {
                if (debug) {
                    ApplicationFrame.getInstance().getConsole()
                            .println("delete : " + backupFile);
                }

                backupFile.delete();
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