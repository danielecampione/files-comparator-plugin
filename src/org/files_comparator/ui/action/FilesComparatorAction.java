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

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.ui.util.ImageUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorAction extends AbstractAction {

    private static final long serialVersionUID = 2140903276163384788L;

    // class variables:
    public static String LARGE_ICON_KEY = "SwingLargeIconKey";

    // instance variables:
    private Object object;
    private Method actionMethod;
    private Method isActionEnabledMethod;
    private ActionHandler actionHandler;

    FilesComparatorAction(ActionHandler actionHandler, Object object,
            String name) {
        super(name);

        this.actionHandler = actionHandler;
        this.object = object;
        initMethods();
    }

    private void initMethods() {
        try {
            actionMethod = object.getClass().getMethod("do" + getName(),
                    ActionEvent.class);
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
            System.exit(1);
        }

        try {
            // This method is not mandatory! 
            //   If it is not available the method is always enabled.
            isActionEnabledMethod = object.getClass().getMethod(
                    "is" + getName() + "Enabled");
        } catch (NoSuchMethodException nsme) {
            ExceptionDialog.ignoreException(nsme);
        }
    }

    public String getName() {
        return (String) getValue(NAME);
    }

    public void setToolTip(String toolTip) {
        putValue(SHORT_DESCRIPTION, toolTip);
    }

    public void setIcon(String iconName) {
        putValue(SMALL_ICON, ImageUtil.getSmallImageIcon(iconName));
        putValue(LARGE_ICON_KEY, ImageUtil.getImageIcon(iconName));
    }

    public ImageIcon getTransparentSmallImageIcon() {
        return ImageUtil
                .createTransparentIcon((ImageIcon) getValue(SMALL_ICON));
    }

    public void actionPerformed(ActionEvent ae) {
        if (object == null || actionMethod == null) {
            ApplicationFrame.getInstance().getConsole()
                    .println("setActionCommand() has not been executed.");
            return;
        }

        try {
            actionMethod.invoke(object, ae);

            actionHandler.checkActions();
        } catch (IllegalAccessException iae) {
            ExceptionDialog.hideException(iae);
        } catch (IllegalArgumentException iae2) {
            ExceptionDialog.hideException(iae2);
        } catch (InvocationTargetException ite) {
            ExceptionDialog.hideException(ite);
        }
    }

    public boolean isActionEnabled() {
        if (object == null || isActionEnabledMethod == null) {
            return true;
        }

        try {
            return (Boolean) isActionEnabledMethod.invoke(object);
        } catch (IllegalAccessException iae) {
            ExceptionDialog.hideException(iae);
        } catch (IllegalArgumentException iae2) {
            ExceptionDialog.hideException(iae2);
        } catch (InvocationTargetException ite) {
            ExceptionDialog.hideException(ite);
        }

        return true;
    }
}