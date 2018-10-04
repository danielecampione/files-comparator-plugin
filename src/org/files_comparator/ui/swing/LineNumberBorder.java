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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.settings.FilesComparatorSettings;
import org.files_comparator.ui.FilePanel;
import org.files_comparator.ui.util.ColorUtil;
import org.files_comparator.ui.util.Colors;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class LineNumberBorder extends EmptyBorder {

    private static final long serialVersionUID = -1760632187706974560L;

    private static int MARGIN = 4;
    private FilePanel filePanel;
    private Color background;
    private Color lineColor;
    private Font font;
    private int fontWidth;
    private int fontHeight;

    public LineNumberBorder(FilePanel filePanel) {
        super(0, 40 + MARGIN, 0, 0);

        this.filePanel = filePanel;

        init();
    }

    private void init() {
        FontMetrics fm;
        Color baseColor;

        baseColor = Colors.getPanelBackground();
        lineColor = ColorUtil.darker(baseColor);
        background = ColorUtil.brighter(baseColor);
        font = new Font("Monospaced", Font.PLAIN, 10);

        fm = filePanel.getEditor().getFontMetrics(font);
        fontWidth = fm.stringWidth("0");
        fontHeight = fm.getHeight();
    }

    public void paintBefore(Graphics g) {
        Rectangle clip;

        clip = g.getClipBounds();

        g.setColor(background);
        g.fillRect(0, clip.y, left - MARGIN, clip.y + clip.height);
    }

    public void paintAfter(Graphics g, int startOffset, int endOffset) {
        Rectangle clip;
        int startLine;
        int endLine;
        int y;
        int lineHeight;
        String s;
        int heightCorrection;
        Rectangle r1;
        JTextArea textArea;
        Graphics2D g2;

        g2 = (Graphics2D) g;

        clip = g.getClipBounds();

        try {
            textArea = filePanel.getEditor();
            startLine = textArea.getLineOfOffset(startOffset);
            endLine = textArea.getLineOfOffset(endOffset);
            r1 = textArea.modelToView(startOffset);
            y = r1.y;
            lineHeight = r1.height;
            heightCorrection = (lineHeight - fontHeight) / 2;

            g.setColor(lineColor);
            g.drawLine(left - MARGIN, clip.y, left - MARGIN, clip.y
                    + clip.height);

            if (FilesComparatorSettings.getInstance().getEditor()
                    .isAntialiasEnabled())
                ;
            {
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }

            g.setFont(font);
            g.setColor(Color.black);
            for (int line = startLine; line <= endLine; line++) {
                y += lineHeight;
                s = Integer.toString(line + 1);
                g.drawString(s, left - (fontWidth * s.length()) - 1 - MARGIN, y
                        - heightCorrection);
            }
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }
}