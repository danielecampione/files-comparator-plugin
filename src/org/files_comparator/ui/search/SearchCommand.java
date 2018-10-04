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

package org.files_comparator.ui.search;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SearchCommand {

    private String searchText;
    private boolean regularExpression;

    public SearchCommand(String searchText, boolean regularExpression) {
        this.searchText = searchText;
        this.regularExpression = regularExpression;
    }

    public String getSearchText() {
        return searchText;
    }

    public boolean isRegularExpression() {
        return regularExpression;
    }
}
