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

package open_teradata_viewer.plugin;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.plugin.Plugin;
import open_teradata_viewer.plugin.actions.Actions;

import org.files_comparator.settings.FilesComparatorSettings;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class CustomPlugin implements Plugin {

    boolean installed;
    private JFrame mainFrame;

    @Override
    public void setup() throws Throwable {
        installed = false;
        // e.debug.EventDispatchThreadHangMonitor.initMonitoring();
        if (FilesComparatorSettings.getInstance().getEditor()
                .isAntialiasEnabled()) {
            System.setProperty("swing.aatext", "true");
        }

        // Everything regarding Swing should be executed on the
        // EventDispatchThread
        SwingUtilities.invokeLater(new FilesComparator(new String[]{}));

        JMenuBar menuBar = ApplicationFrame.getInstance().getJMenuBar();
        int menuPos = getMenuPositionByActionCommand(menuBar, "file");
        if (menuPos != -1) {
            JMenu fileMenu = menuBar.getMenu(menuPos);
            fileMenu.insertSeparator(fileMenu.getMenuComponentCount());
            fileMenu.add(Actions.FILES_COMPARATOR);
        }
        installed = true;
    }

    @Override
    public String analyzeException(String exception) {
        return "";
    }

    @Override
    public void showPanel() {
        mainFrame.setVisible(true);
        mainFrame.toFront();
    }

    @Override
    public String toString() {
        return "Files Comparator plug-in";
    }

    public int getMenuPositionByActionCommand(JMenuBar menuBar,
            String actionCommand) {
        if (menuBar != null) {
            for (int i = 0; i < menuBar.getMenuCount(); i++) {
                JMenu menu = menuBar.getMenu(i);
                if (menu.getActionCommand().equalsIgnoreCase(actionCommand)) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override
    public boolean isInstalled() {
        return installed;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
}
