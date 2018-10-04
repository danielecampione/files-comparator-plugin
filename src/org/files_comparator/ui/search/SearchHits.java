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

package org.files_comparator.ui.search;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SearchHits {

    private List<SearchHit> searchHits;
    private SearchHit current;

    public SearchHits() {
        searchHits = new ArrayList<SearchHit>();
    }

    public void add(SearchHit sh) {
        searchHits.add(sh);
        if (getCurrent() == null) {
            setCurrent(sh);
        }
    }

    public List<SearchHit> getSearchHits() {
        return searchHits;
    }

    public boolean isCurrent(SearchHit sh) {
        return sh.equals(getCurrent());
    }

    public SearchHit getCurrent() {
        return current;
    }

    private void setCurrent(SearchHit sh) {
        current = sh;
    }

    public void next() {
        int index;

        index = searchHits.indexOf(getCurrent());
        index++;

        if (index >= searchHits.size()) {
            index = 0;
        }

        if (index >= 0 && index < searchHits.size()) {
            setCurrent(searchHits.get(index));
        }
    }

    public void previous() {
        int index;

        index = searchHits.indexOf(getCurrent());
        index--;

        if (index < 0) {
            index = searchHits.size() - 1;
        }

        if (index >= 0 && index < searchHits.size()) {
            setCurrent(searchHits.get(index));
        }
    }
}
