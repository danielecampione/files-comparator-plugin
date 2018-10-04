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

package org.files_comparator.util.file.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class AbstractCmd extends AbstractUndoableEdit {

    private static final long serialVersionUID = -347102775573708728L;
    private List<Command> commandList = new ArrayList<Command>();
    private List<Command> finallyCommandList = new ArrayList<Command>();
    protected boolean debug = true;

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public synchronized void execute() throws Exception {
        commandList.clear();
        finallyCommandList.clear();
        createCommands();

        for (Command command : commandList) {
            command.execute();
        }

        for (Command command : finallyCommandList) {
            command.execute();
        }
    }

    protected abstract void createCommands() throws Exception;

    protected void addCommand(Command command) {
        commandList.add(command);
    }

    protected void addFinallyCommand(Command command) {
        finallyCommandList.add(command);
    }

    @Override
    public synchronized void redo() {
        super.redo();
        for (Command command : commandList) {
            command.redo();
        }

        for (Command command : finallyCommandList) {
            command.redo();
        }
    }

    @Override
    public synchronized void undo() {
        super.undo();

        // Undo should be executed in the reverse order!
        // Note: the commandList itself is reversed and that is OK because
        //       at the end of this method the commandList is cleared.
        try {
            Collections.reverse(commandList);
            for (Command command : commandList) {
                command.undo();
            }
            Collections.reverse(commandList);

            Collections.reverse(finallyCommandList);
            for (Command command : finallyCommandList) {
                command.undo();
            }
            Collections.reverse(finallyCommandList);
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
            throw new CannotRedoException();
        }
    }

    public synchronized void discard() {
        Collections.reverse(commandList);
        for (Command command : commandList) {
            command.discard();
        }

        commandList.clear();

        Collections.reverse(finallyCommandList);
        for (Command command : finallyCommandList) {
            command.discard();
        }

        finallyCommandList.clear();
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    abstract class Command {
        public abstract void execute() throws Exception;

        public void redo() {
            try {
                execute();
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
                throw new CannotRedoException();
            }
        }

        public abstract void undo();

        public void discard() {
        }
    }
}