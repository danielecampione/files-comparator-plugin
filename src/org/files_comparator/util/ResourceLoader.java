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

package org.files_comparator.util;

import java.io.InputStream;
import java.net.URL;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ResourceLoader {

    static ResourceLoader resourceLoader = new ResourceLoader();

    private ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    public static synchronized InputStream getResourceAsStream(
            String resourceName) {
        return resourceLoader.getClassLoader()
                .getResourceAsStream(resourceName);
    }

    public static synchronized URL getResource(String resourceName) {
        return resourceLoader.getClassLoader().getResource(resourceName);
    }
}
