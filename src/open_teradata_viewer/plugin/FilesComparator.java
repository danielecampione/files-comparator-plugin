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

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Main;
import net.sourceforge.open_teradata_viewer.SwingUtil;

import org.files_comparator.ui.FilesComparatorPanel;
import org.files_comparator.ui.util.ImageUtil;
import org.files_comparator.util.prefs.WindowPreference;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparator implements Runnable {
    private List<String> fileNameList;
    private static FilesComparatorPanel filesComparatorPanel;

    public FilesComparator(String[] args) {
        fileNameList = new ArrayList<String>();
        for (String arg : args) {
            fileNameList.add(arg);
        }
    }
    public static FilesComparatorPanel getFilesComparatorPanel() {
        return filesComparatorPanel;
    }

    public static void setFilesComparatorPanel(
            FilesComparatorPanel filesComparatorPanel) {
        FilesComparator.filesComparatorPanel = filesComparatorPanel;
    }

    @Override
    public void run() {
        debugKeyboard();
        setFilesComparatorPanel(new FilesComparatorPanel());
        buildMainFrame();
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

    public void buildMainFrame() {
        CustomPlugin plugin = (CustomPlugin) ApplicationFrame.getInstance().PLUGIN;
        JFrame mainFrame = new JFrame(String.format("%s ( %s )",
                Main.APPLICATION_NAME, plugin));
        double screenWidth = Toolkit.getDefaultToolkit().getScreenSize()
                .getWidth();
        double screenHeight = Toolkit.getDefaultToolkit().getScreenSize()
                .getHeight();
        mainFrame.setIconImage(ImageUtil.getImageIcon("files-comparator-small")
                .getImage());
        mainFrame.setSize((int) (screenWidth * .4), (int) (screenHeight * .4));
        mainFrame.setMinimumSize(new Dimension((int) (screenWidth * .2),
                (int) (screenHeight * .2)));
        SwingUtil.centerWithinScreen(mainFrame);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().add(
                FilesComparator.getFilesComparatorPanel(), BorderLayout.CENTER);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        new WindowPreference(mainFrame.getTitle(), mainFrame);

        plugin.setMainFrame(mainFrame);
    }
}
