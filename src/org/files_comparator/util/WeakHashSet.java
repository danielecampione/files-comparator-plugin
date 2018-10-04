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

package org.files_comparator.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class WeakHashSet<E> extends AbstractSet<E> implements Set<E> {

    private Boolean value = Boolean.TRUE;
    private WeakHashMap<E, Boolean> map;

    public WeakHashSet() {
        map = new WeakHashMap<E, Boolean>();
    }

    public WeakHashSet(Collection<E> c) {
        map = new WeakHashMap<E, Boolean>();
        addAll(c);
    }

    public WeakHashSet(int initialCapacity, float loadFactor) {
        map = new WeakHashMap<E, Boolean>(initialCapacity, loadFactor);
    }

    public WeakHashSet(int initialCapacity) {
        map = new WeakHashMap<E, Boolean>(initialCapacity);
    }

    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public boolean add(E o) {
        return map.put(o, value) == null;
    }

    public boolean remove(Object o) {
        return map.remove(o) == value;
    }

    public void clear() {
        map.clear();
    }
}
