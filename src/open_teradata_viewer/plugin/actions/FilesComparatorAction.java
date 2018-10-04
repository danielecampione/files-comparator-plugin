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

package open_teradata_viewer.plugin.actions;

import java.awt.event.ActionEvent;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.actions.CustomAction;
import open_teradata_viewer.plugin.CustomPlugin;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorAction extends CustomAction {

    private static final long serialVersionUID = -247597193455004912L;

    protected FilesComparatorAction() {
        super("Files comparator..", null, null, null);
        setEnabled(true);
    }

    public void actionPerformed(final ActionEvent e) {
        // The "check time period" selection process can be performed altough
        // other processes are running. No ThreadAction object must be
        // instantiated because the focus must still remains on the calendar
        // frame.
        try {
            performThreaded(e);
        } catch (Throwable t) {
            ApplicationFrame.getInstance().printStackTraceOnGUI(t);
        }
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        CustomPlugin plugin = (CustomPlugin) ApplicationFrame.getInstance().PLUGIN;
        if (plugin.getMainFrame() != null && plugin.getMainFrame().isVisible()) {
            return;
        }
        plugin.showPanel();
    }
}