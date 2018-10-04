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

package org.files_comparator.ui.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.settings.EditorSettings;
import org.files_comparator.settings.FilesComparatorSettings;
import org.files_comparator.ui.util.Colors;
import org.files_comparator.util.conf.ConfigurationListenerIF;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FilesComparatorHighlightPainter
        extends
            DefaultHighlighter.DefaultHighlightPainter
        implements
            ConfigurationListenerIF {

    public static final FilesComparatorHighlightPainter ADDED;
    public static final FilesComparatorHighlightPainter ADDED_LINE;
    public static final FilesComparatorHighlightPainter CHANGED;
    public static final FilesComparatorHighlightPainter CHANGED_LIGHTER;
    public static final FilesComparatorHighlightPainter DELETED;
    public static final FilesComparatorHighlightPainter DELETED_LINE;
    public static final FilesComparatorHighlightPainter CURRENT_SEARCH;
    public static final FilesComparatorHighlightPainter SEARCH;

    static {
        ADDED = new FilesComparatorHighlightPainter(Colors.ADDED);
        ADDED.initConfiguration();
        ADDED_LINE = new FilesComparatorHighlightPainter(Colors.ADDED, true);
        ADDED_LINE.initConfiguration();
        CHANGED = new FilesComparatorHighlightPainter(Colors.CHANGED);
        CHANGED.initConfiguration();
        CHANGED_LIGHTER = new FilesComparatorHighlightPainter(
                Colors.CHANGED_LIGHTER);
        CHANGED_LIGHTER.initConfiguration();
        DELETED = new FilesComparatorHighlightPainter(Colors.DELETED);
        DELETED.initConfiguration();
        DELETED_LINE = new FilesComparatorHighlightPainter(Colors.DELETED, true);
        DELETED_LINE.initConfiguration();
        SEARCH = new FilesComparatorHighlightPainter(Color.yellow);
        SEARCH.initConfiguration();
        CURRENT_SEARCH = new FilesComparatorHighlightPainter(
                Color.yellow.darker());
        CURRENT_SEARCH.initConfiguration();
    }

    private Color color;
    private boolean line;

    private FilesComparatorHighlightPainter(Color color) {
        this(color, false);
    }

    private FilesComparatorHighlightPainter(Color color, boolean line) {
        super(color);

        this.color = color;
        this.line = line;

        FilesComparatorSettings.getInstance().addConfigurationListener(this);
    }

    @Override
    public void paint(Graphics g, int p0, int p1, Shape shape,
            JTextComponent comp) {
        Rectangle b;
        Rectangle r1;
        Rectangle r2;
        int x;
        int y;
        int width;
        int height;
        int count;

        b = shape.getBounds();

        try {
            r1 = comp.modelToView(p0);
            r2 = comp.modelToView(p1);

            g.setColor(color);
            if (line) {
                g.drawLine(0, r1.y, b.x + b.width, r1.y);
            } else {
                if (this == CHANGED_LIGHTER || this == SEARCH
                        || this == CURRENT_SEARCH) {
                    if (r1.y == r2.y) {
                        g.fillRect(r1.x, r1.y, r2.x - r1.x, r1.height);
                    } else {
                        count = ((r2.y - r1.y) / r1.height) + 1;
                        y = r1.y;
                        for (int i = 0; i < count; i++, y += r1.height) {
                            if (i == 0) {
                                // firstline:
                                x = r1.x;
                                width = b.width - b.x;
                            } else if (i == count - 1) {
                                // lastline:
                                x = b.x;
                                width = r2.x - x;
                            } else {
                                // all lines in between the first and the lastline:
                                x = b.x;
                                width = b.width - b.x;
                            }

                            g.fillRect(x, y, width, r1.height);
                        }
                    }
                } else {
                    height = r2.y - r1.y;
                    if (height == 0) {
                        height = r1.height;
                    }
                    g.fillRect(0, r1.y, b.x + b.width, height);
                }
            }
        } catch (BadLocationException ble) {
            ExceptionDialog.hideException(ble);
        }
    }

    public void configurationChanged() {
        initConfiguration();
    }

    private void initConfiguration() {
        if (this == ADDED || this == ADDED_LINE) {
            color = getSettings().getAddedColor();
        } else if (this == DELETED || this == DELETED_LINE) {
            color = getSettings().getDeletedColor();
        } else if (this == CHANGED) {
            color = getSettings().getChangedColor();
        } else if (this == CHANGED_LIGHTER) {
            color = Colors.getChangedLighterColor(getSettings()
                    .getChangedColor());
        }
    }

    private EditorSettings getSettings() {
        return FilesComparatorSettings.getInstance().getEditor();
    }
}