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

package org.files_comparator.ui.util;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.files_comparator.settings.FilesComparatorSettings;

/**
 * Very ugly hack to make possible a close button on a
 * tabbedpane.
 * 
 * @author D. Campione
 */
public class TabIcon implements Icon {

    // class variables:
    private static int CLOSE_ICON_HEIGHT = 7;
    private static int CLOSE_ICON_WIDTH = 7;
    private static int SPACE_WIDTH = 5;

    // instance variables:
    private Icon icon;
    private String text;
    private int width;
    private int height;
    private JLabel label;
    private int stringWidth;
    private Rectangle closeBounds;
    private JTabbedPane tabbedPane;
    private Icon currentIcon;
    private Icon closeIcon;
    private Icon closeIcon_rollover;
    private Icon closeIcon_pressed;
    private Icon closeIcon_disabled;
    private boolean pressed;
    private boolean ignoreNextMousePressed;
    private ChangeListener changeListener;
    private MouseListener mouseListener;
    private MouseMotionListener mouseMotionListener;
    private ArrayList<TabExitListenerIF> tabExitListeners;

    public TabIcon(Icon icon, String text) {
        this.icon = icon;
        this.text = text;

        init();

        tabExitListeners = new ArrayList<TabExitListenerIF>();
    }

    private void init() {
        Font font;
        FontMetrics fm;

        height = 0;
        width = 0;

        closeIcon = ImageUtil.getImageIcon("files-comparator_close");
        closeIcon_rollover = ImageUtil
                .getImageIcon("files-comparator_close-rollover");
        closeIcon_pressed = ImageUtil
                .getImageIcon("files-comparator_close-pressed");
        closeIcon_disabled = ImageUtil
                .getImageIcon("files-comparator_close-disabled");
        if (closeIcon != null) {
            CLOSE_ICON_WIDTH = closeIcon.getIconWidth();
            CLOSE_ICON_HEIGHT = closeIcon.getIconHeight();
        }

        if (icon != null) {
            height = height < icon.getIconHeight()
                    ? icon.getIconHeight()
                    : height;
            width = width + icon.getIconWidth();
        }

        if (text != null) {
            label = new JLabel(text);
            font = label.getFont();
            fm = label.getFontMetrics(font);

            height = height < fm.getHeight() ? fm.getHeight() : height;
            stringWidth = fm.stringWidth(text);
            width += stringWidth;
        }

        height = height < CLOSE_ICON_HEIGHT ? CLOSE_ICON_HEIGHT : height;
        width += CLOSE_ICON_WIDTH;

        if (icon != null) {
            width += SPACE_WIDTH;
        }

        if (text != null) {
            width += SPACE_WIDTH;
        }
    }

    public void addExitListener(TabExitListenerIF listener) {
        tabExitListeners.add(listener);
    }

    public void removeExitListener(TabExitListenerIF listener) {
        tabExitListeners.remove(listener);
    }

    public void exit() {
        // This exit can be called in 2 different ways:
        // 1. From within this Icon itself (pressing the close-button on the tab)
        // 2. From within the application (for instance when pressing ESCAPE)
        // This method was primarily made 'public' for the second way.

        // Don't create memory leaks!
        tabbedPane.removeMouseListener(getMouseListener());
        tabbedPane.removeMouseMotionListener(getMouseMotionListener());
        tabbedPane.removeChangeListener(getChangeListener());
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        FontMetrics fm;
        int x0;
        int y0;
        Icon cIcon;
        Graphics2D g2;

        g2 = (Graphics2D) g;

        x0 = x;

        if (tabbedPane == null) {
            tabbedPane = (JTabbedPane) c;
            tabbedPane.addMouseListener(getMouseListener());
            tabbedPane.addMouseMotionListener(getMouseMotionListener());
            tabbedPane.addChangeListener(getChangeListener());
        }

        if (icon != null) {
            icon.paintIcon(c, g, x0, y + ((height - icon.getIconHeight()) / 2));
            x0 += icon.getIconWidth();
            x0 += SPACE_WIDTH;
        }

        if (text != null) {
            if (FilesComparatorSettings.getInstance().getEditor()
                    .isAntialiasEnabled()) {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }

            fm = label.getFontMetrics(label.getFont());
            y0 = y + fm.getAscent() + ((height - fm.getHeight()) / 2);

            g.setFont(label.getFont());
            g.drawString(text, x0, y0);

            x0 += stringWidth;
            x0 += SPACE_WIDTH;
        }

        y0 = y + (height - CLOSE_ICON_HEIGHT) / 2;
        if (closeIcon != null) {
            cIcon = currentIcon;
            if (!isSelected()) {
                cIcon = closeIcon_disabled;
            }

            if (cIcon == null) {
                cIcon = closeIcon;
            }

            cIcon.paintIcon(c, g, x0, y0);
            closeBounds = new Rectangle(x0, y0, CLOSE_ICON_WIDTH,
                    CLOSE_ICON_HEIGHT);
        } else {
            g.drawLine(x0, y0, x0 + CLOSE_ICON_HEIGHT, y0 + CLOSE_ICON_WIDTH);
            g.drawLine(x0 + CLOSE_ICON_HEIGHT, y0, x0, y0 + CLOSE_ICON_WIDTH);
            closeBounds = new Rectangle(x0, y0, CLOSE_ICON_WIDTH,
                    CLOSE_ICON_HEIGHT);
        }

        x0 += CLOSE_ICON_WIDTH;
    }

    private MouseListener getMouseListener() {
        if (mouseListener == null) {
            mouseListener = new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    Icon icon;

                    if (ignoreNextMousePressed) {
                        ignoreNextMousePressed = false;
                        return;
                    }

                    if (isCloseHit(me)) {
                        pressed = true;

                        icon = closeIcon_pressed;
                        if (currentIcon != icon) {
                            currentIcon = icon;
                            tabbedPane.repaint();
                        }
                    } else {
                        pressed = false;
                    }
                }

                public void mouseReleased(MouseEvent me) {
                    int index;

                    if (pressed && isCloseHit(me)) {
                        index = tabbedPane.indexOfTab(TabIcon.this);

                        // Only allow selected tabs to be closed.
                        if (index != tabbedPane.getSelectedIndex()) {
                            return;
                        }

                        if (index != -1) {
                            for (TabExitListenerIF listener : tabExitListeners) {
                                listener.doExit(new TabExitEvent(TabIcon.this,
                                        index));
                            }

                            me.consume();
                        }
                    }

                    if (currentIcon != closeIcon) {
                        currentIcon = closeIcon;
                        tabbedPane.repaint();
                    }
                }
            };
        }

        return mouseListener;
    }

    private MouseMotionListener getMouseMotionListener() {
        if (mouseMotionListener == null) {
            mouseMotionListener = new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent me) {
                    Icon icon;

                    if (isSelected()) {
                        ignoreNextMousePressed = false;
                    }

                    if (isCloseHit(me)) {
                        icon = closeIcon_rollover;
                    } else {
                        pressed = false;
                        icon = closeIcon;
                    }

                    if (icon != currentIcon) {
                        currentIcon = icon;
                        tabbedPane.repaint();
                    }
                }
            };
        }

        return mouseMotionListener;
    }

    private ChangeListener getChangeListener() {
        if (changeListener == null) {
            changeListener = new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {
                    ignoreNextMousePressed = true;
                }
            };
        }

        return changeListener;
    }

    private boolean isCloseHit(MouseEvent me) {
        return (!me.isConsumed() && closeBounds != null
                && closeBounds.contains(me.getX(), me.getY()) && isSelected());
    }

    private boolean isSelected() {
        int index;

        index = tabbedPane.indexOfTab(this);

        return (index != -1 && tabbedPane.getSelectedIndex() == index);
    }
}