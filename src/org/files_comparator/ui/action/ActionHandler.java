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

package org.files_comparator.ui.action;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ActionHandler {

    private Map<Actions.Action, FilesComparatorAction> actions = new HashMap<Actions.Action, FilesComparatorAction>();

    public ActionHandler() {
    }

    public FilesComparatorAction get(Actions.Action a) {
        return actions.get(a);
    }

    public FilesComparatorAction createAction(Object object, Actions.Action a) {
        FilesComparatorAction action;

        action = new FilesComparatorAction(this, object, a.getName());
        actions.put(a, action);

        checkActions();

        return action;
    }

    public void checkActions() {
        boolean actionEnabled;
        boolean someActionChanged;

        do {
            someActionChanged = false;
            for (FilesComparatorAction action : actions.values()) {
                actionEnabled = action.isActionEnabled();
                if (actionEnabled != action.isEnabled()) {
                    action.setEnabled(actionEnabled);

                    // Some actions depend on other actions!
                    someActionChanged = true;
                }
            }
        } while (someActionChanged);
    }
}
