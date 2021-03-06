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

package org.files_comparator.ui.util;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JComponent;

import com.jgoodies.forms.builder.AbstractFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ToolBarBuilder extends AbstractFormBuilder {

    public ToolBarBuilder(JComponent toolBar) {
        super(new FormLayout("", "fill:p"), toolBar);

        initialize();
    }

    private void initialize() {
        getContainer().setLayout(getLayout());
    }

    public void addButton(AbstractButton button) {
        appendColumn("pref:none");
        //getLayout().addGroupedColumn(getColumn());
        //button.putClientProperty(NARROW_KEY, Boolean.TRUE);
        add(button);
        nextColumn();
    }

    public void addComponent(JComponent component) {
        appendColumn("pref:none");
        add(component);
        nextColumn();
    }

    public void addSeparator() {
        appendColumn("pref:none");
        add(new ToolBarSeparator());
        nextColumn();
    }

    public void addSpring() {
        appendColumn("pref:grow");

        // Any old component will do here!
        add(Box.createHorizontalGlue());
        nextColumn();
    }
}