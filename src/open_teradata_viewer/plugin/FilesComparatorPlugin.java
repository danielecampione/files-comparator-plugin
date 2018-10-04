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

package open_teradata_viewer.plugin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.Main;
import net.sourceforge.open_teradata_viewer.plugin.IPluginEntry;
import net.sourceforge.open_teradata_viewer.util.SwingUtil;
import open_teradata_viewer.plugin.actions.Actions;

import org.files_comparator.settings.FilesComparatorSettings;
import org.files_comparator.ui.FilesComparatorPanel;
import org.files_comparator.ui.util.ImageUtil;
import org.files_comparator.util.prefs.WindowPreference;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorPlugin extends JFrame
        implements
            IPluginEntry,
            Runnable {

    private static final long serialVersionUID = -8762617897106467374L;

    private static FilesComparatorPlugin PLUGIN;

    private List<String> fileNameList;

    private FilesComparatorPanel filesComparatorPanel;

    public FilesComparatorPlugin() {
        PLUGIN = this;
        try {
            initComponents();
        } catch (Throwable t) {
            ExceptionDialog.hideException(t);
        }
    }

    public FilesComparatorPlugin(String[] args) {
        this();
        fileNameList = new ArrayList<String>();
        for (String arg : args) {
            fileNameList.add(arg);
        }
    }

    private void initComponents() throws Throwable {
        // e.debug.EventDispatchThreadHangMonitor.initMonitoring();
        if (FilesComparatorSettings.getInstance().getEditor()
                .isAntialiasEnabled()) {
            System.setProperty("swing.aatext", "true");
        }

        JMenuBar menuBar = ApplicationFrame.getInstance().getJMenuBar();
        int menuPos = getMenuPositionByActionCommand(menuBar, "file");
        if (menuPos != -1) {
            JMenu fileMenu = menuBar.getMenu(menuPos);
            fileMenu.insertSeparator(fileMenu.getMenuComponentCount());
            fileMenu.add(Actions.FILES_COMPARATOR);
        }

        setTitle(String.format("%s ( %s )", Main.APPLICATION_NAME, this));
        double screenWidth = Toolkit.getDefaultToolkit().getScreenSize()
                .getWidth();
        double screenHeight = Toolkit.getDefaultToolkit().getScreenSize()
                .getHeight();
        setIconImage(ImageUtil.getImageIcon("files-comparator-small")
                .getImage());
        setSize((int) (screenWidth * .4), (int) (screenHeight * .4));
        setMinimumSize(new Dimension((int) (screenWidth * .2),
                (int) (screenHeight * .2)));
        SwingUtil.centerWithinScreen(this);
        setLayout(new BorderLayout());
        filesComparatorPanel = new FilesComparatorPanel();
        getContentPane().add(getFilesComparatorPanel(), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        new WindowPreference(getTitle(), this);
    }

    @Override
    public void initPluginEntry(Object param) {
    }

    @Override
    public void startPluginEntry() {
        setVisible(true);
        toFront();
    }

    @Override
    public String toString() {
        return "Files Comparator plug-in";
    }

    @Override
    public void pausePluginEntry() {
        setVisible(false);
    }

    @Override
    public void stopPluginEntry() {
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

    public static FilesComparatorPlugin getInstance() {
        return PLUGIN;
    }

    public FilesComparatorPanel getFilesComparatorPanel() {
        return filesComparatorPanel;
    }

    public void setFilesComparatorPanel(
            FilesComparatorPanel filesComparatorPanel) {
        this.filesComparatorPanel = filesComparatorPanel;
    }

    @Override
    public void run() {
        debugKeyboard();
        setFilesComparatorPanel(new FilesComparatorPanel());
    }

    void debugKeyboard() {
        KeyboardFocusManager
                .setCurrentKeyboardFocusManager(new DefaultKeyboardFocusManager() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        return super.dispatchKeyEvent(e);
                    }

                    @Override
                    public void processKeyEvent(Component focusedComponent,
                            KeyEvent e) {
                        super.processKeyEvent(focusedComponent, e);
                    }
                });
    }
}