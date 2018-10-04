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

package open_teradata_viewer.plugin;

import java.util.Properties;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.util.ResourceLoader;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Version {
    // Class variables:
    // Singleton:
    private static Version instance = new Version();

    // Instance variables:
    private String version;

    private Version() {
        init();
    }

    public static String getVersion() {
        return instance.version;
    }

    private void init() {
        Properties p;

        try {
            p = new Properties();
            p.load(ResourceLoader.getResourceAsStream("ini/Version.txt"));
            version = p.getProperty("version");
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }
}