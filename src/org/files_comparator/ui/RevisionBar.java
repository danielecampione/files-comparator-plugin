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

package org.files_comparator.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollBar;

import org.files_comparator.diff.FilesComparatorChunk;
import org.files_comparator.diff.FilesComparatorDelta;
import org.files_comparator.diff.FilesComparatorRevision;
import org.files_comparator.ui.util.ColorUtil;
import org.files_comparator.ui.util.Colors;
import org.files_comparator.ui.util.RevisionUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class RevisionBar extends JComponent {

    private static final long serialVersionUID = 8472104426189418694L;

    private BufferDiffPanel diffPanel;
    private FilePanel filePanel;
    private boolean original;

    public RevisionBar(BufferDiffPanel diffPanel, FilePanel filePanel,
            boolean original) {
        this.diffPanel = diffPanel;
        this.filePanel = filePanel;
        this.original = original;

        setBorder(BorderFactory.createLineBorder(ColorUtil.darker(ColorUtil
                .darker(Colors.getPanelBackground()))));

        addMouseListener(getMouseListener());
    }

    private MouseListener getMouseListener() {
        return new MouseAdapter() {

            public void mouseClicked(MouseEvent me) {
                Rectangle r;
                int y;
                int line;
                int lineBefore;
                int lineAfter;
                FilesComparatorRevision revision;
                int numberOfLines;
                FilesComparatorChunk original;

                r = getDrawableRectangle();
                if (r == null) {
                    return;
                }

                if (r.height <= 0) {
                    return;
                }

                y = me.getY() - r.y;

                revision = diffPanel.getCurrentRevision();
                if (revision == null) {
                    return;
                }

                numberOfLines = getNumberOfLines(revision);
                line = (y * numberOfLines) / r.height;
                if (line > numberOfLines) {
                    line = numberOfLines;
                }

                if (line < 0) {
                    line = 0;
                }

                // If the files are very large the resolution of one pixel contains 
                //   a lot of lines of the document. Check if there is a chunk in 
                //   the revision between those lines and if there is position on 
                //   that chunk.
                lineBefore = ((y - 3) * numberOfLines) / r.height;
                lineAfter = ((y + 3) * numberOfLines) / r.height;
                for (FilesComparatorDelta delta : revision.getDeltas()) {
                    original = delta.getOriginal();

                    // The chunk starts within the bounds of the line-resolution.
                    if (original.getAnchor() > lineBefore
                            && original.getAnchor() < lineAfter) {
                        diffPanel.doGotoDelta(delta);
                        return;
                    }
                }

                diffPanel.doGotoLine(line);
            }
        };
    }

    /** Calculate the rectangle that can be used to draw the diffs.
     *    It is essentially the size of the scrollbar minus its buttons.
     */

    private Rectangle getDrawableRectangle() {
        JScrollBar sb;
        Rectangle r;

        sb = filePanel.getScrollPane().getVerticalScrollBar();
        r = sb.getBounds();
        r.x = 0;
        r.y = 0;

        for (Component c : sb.getComponents()) {
            if (c instanceof AbstractButton) {
                r.y += c.getHeight();
                r.height -= (2 * c.getHeight());
                break;
            }
        }

        return r;
    }

    public void paintComponent(Graphics g) {
        Rectangle r;
        Graphics2D g2;
        FilesComparatorRevision revision;
        FilesComparatorChunk chunk;
        int y;
        int height;
        int numberOfLines;
        Rectangle clipBounds;

        g2 = (Graphics2D) g;

        clipBounds = g.getClipBounds();

        r = getDrawableRectangle();
        r.x = clipBounds.x;
        r.width = clipBounds.width;

        g2.setColor(Color.white);
        g2.fill(r);

        revision = diffPanel.getCurrentRevision();
        if (revision == null) {
            return;
        }

        numberOfLines = getNumberOfLines(revision);
        if (numberOfLines <= 0) {
            return;
        }

        for (FilesComparatorDelta delta : revision.getDeltas()) {
            chunk = original ? delta.getOriginal() : delta.getRevised();

            g.setColor(RevisionUtil.getColor(delta));
            y = r.y + (r.height * chunk.getAnchor()) / numberOfLines;
            height = (r.height * chunk.getSize()) / numberOfLines;
            if (height <= 0) {
                height = 1;
            }

            g.fillRect(0, y, r.width, height);
        }
    }

    private int getNumberOfLines(FilesComparatorRevision revision) {
        return original ? revision.getOrgSize() : revision.getRevSize();
    }
}