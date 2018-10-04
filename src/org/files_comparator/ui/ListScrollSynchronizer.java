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

package org.files_comparator.ui;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ListScrollSynchronizer implements ChangeListener {

    private JViewport vp1;
    private JViewport vp2;

    public ListScrollSynchronizer(JScrollPane sc1, JScrollPane sc2) {
        vp1 = sc1.getViewport();
        vp2 = sc2.getViewport();

        vp1.addChangeListener(this);
        vp2.addChangeListener(this);
    }

    public void stateChanged(ChangeEvent event) {
        JViewport fromViewport;
        JViewport toViewport;

        fromViewport = event.getSource() == vp1 ? vp1 : vp2;
        toViewport = fromViewport == vp1 ? vp2 : vp1;

        toViewport.removeChangeListener(this);
        toViewport.setViewPosition(fromViewport.getViewPosition());
        toViewport.addChangeListener(this);
    }
}
