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

package org.files_comparator.util.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.files_comparator.util.JaxbPersister;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ConfigurationPersister {

    // class variables:
    private static ConfigurationPersister instance = new ConfigurationPersister();

    private ConfigurationPersister() {
    }

    public static ConfigurationPersister getInstance() {
        return instance;
    }

    /** Load a configuration of type 'clazz' from a file.
     */
    public <T extends AbstractConfiguration> T load(Class<T> clazz, File file)
            throws FileNotFoundException {
        T configuration;

        try {
            configuration = JaxbPersister.getInstance().load(clazz, file);
        } catch (Exception ex) {
            return null;
        }

        configuration.init();

        return configuration;
    }

    /** Save a configuration to a file. */
    public void save(AbstractConfiguration configuration, File file)
            throws JAXBException, IOException {
        JaxbPersister.getInstance().save(configuration, file);
    }
}