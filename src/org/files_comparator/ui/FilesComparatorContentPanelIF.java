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

import org.files_comparator.ui.search.SearchHits;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public interface FilesComparatorContentPanelIF {

    public String getId();

    public void setId(String id);

    public boolean isSaveEnabled();

    public void doSave();

    public boolean checkSave();

    public boolean isUndoEnabled();

    public void doUndo();

    public boolean isRedoEnabled();

    public void doRedo();

    public void doLeft();

    public void doRight();

    public void doUp();

    public void doDown();

    public void doZoom(boolean direction);

    public void doGoToSelected();

    public void doGoToFirst();

    public void doGoToLast();

    public void doGoToLine(int line);

    public void doStopSearch();

    public SearchHits doSearch();

    public void doNextSearch();

    public void doPreviousSearch();

    public void doRefresh();

    public void doMergeMode(boolean mergeMode);

    public boolean checkExit();

    public String getSelectedText();
}
