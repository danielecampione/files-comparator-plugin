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
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.diff.FilesComparatorChunk;
import org.files_comparator.diff.FilesComparatorDelta;
import org.files_comparator.diff.FilesComparatorDiff;
import org.files_comparator.diff.FilesComparatorRevision;
import org.files_comparator.util.Ignore;
import org.files_comparator.util.TokenizerFactory;
import org.files_comparator.util.WordTokenizer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DiffLabel extends JTextPane {

    private static final long serialVersionUID = 1783064323601022314L;

    public DiffLabel() {
        init();
    }

    public void init() {
        Style s;
        Style defaultStyle;
        StyledDocument doc;

        setEditable(false);
        setOpaque(false);
        // Bug in Nimbus L&F doesn't honour the opaqueness of a JLabel.
        // Setting a fully transparent color is a workaround:
        setBackground(new Color(0, 0, 0, 0));
        setBorder(null);

        defaultStyle = getStyle(StyleContext.DEFAULT_STYLE);

        doc = getStyledDocument();
        s = doc.addStyle("bold", defaultStyle);
        StyleConstants.setBold(s, true);
    }

    /** Set the text on this label.
     *  Some parts of the text will be displayed in bold-style.
     *  These parts are the differences between text and otherText.
     */
    public void setText(String text, String otherText) {
        WordTokenizer wt;
        List<String> textList;
        List<String> otherTextList;
        FilesComparatorRevision revision;
        String[] styles;
        FilesComparatorChunk chunk;
        StyledDocument doc;

        try {
            wt = TokenizerFactory.getFileNameTokenizer();
            textList = wt.getTokens(text);
            otherTextList = wt.getTokens(otherText);

            styles = new String[textList.size()];

            if (otherTextList.size() != 0) {
                revision = new FilesComparatorDiff().diff(textList,
                        otherTextList, Ignore.NULL_IGNORE);

                for (FilesComparatorDelta delta : revision.getDeltas()) {
                    chunk = delta.getOriginal();
                    for (int i = 0; i < chunk.getSize(); i++) {
                        styles[chunk.getAnchor() + i] = "bold";
                    }
                }
            }

            doc = getStyledDocument();
            doc.remove(0, doc.getLength());

            for (int i = 0; i < textList.size(); i++) {
                doc.insertString(doc.getLength(), textList.get(i),
                        (styles[i] != null ? doc.getStyle(styles[i]) : null));
            }
        } catch (Exception e) {
            ExceptionDialog.hideException(e);

            // Make the best out of this situation (should never happen)
            setText(text);
        }
    }
}