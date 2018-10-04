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

package org.files_comparator.util.conf;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.util.WeakHashSet;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ConfigurationManager {

    // class variables:
    private static ConfigurationManager instance = new ConfigurationManager();

    // instance variables:
    private Map<String, AbstractConfiguration> configurations;
    private Map<String, WeakHashSet<ConfigurationListenerIF>> listenerMap;

    private ConfigurationManager() {
        configurations = new HashMap<String, AbstractConfiguration>();
        listenerMap = new HashMap<String, WeakHashSet<ConfigurationListenerIF>>();
    }

    public static ConfigurationManager getInstance() {
        return instance;
    }

    public boolean reload(File file, Class<?> clazz) {
        AbstractConfiguration configuration;

        configuration = load(clazz, file);
        if (configuration == null) {
            return false;
        }

        // Set the new filename AFTER the load was succesfull!
        configuration.setConfigurationFile(file);

        configurations.put(clazz.getName(), configuration);

        // Let everybody know that there is a new configuration!
        fireChanged(clazz);

        return true;
    }

    public AbstractConfiguration get(Class<?> clazz) {
        AbstractConfiguration configuration;
        String key;

        key = clazz.getName();

        configuration = configurations.get(key);
        if (configuration == null) {
            configuration = load(clazz);
            if (configuration == null) {
                try {
                    configuration = (AbstractConfiguration) clazz.newInstance();
                    configuration.disableFireChanged(true);
                    configuration.init();
                    configuration.disableFireChanged(false);
                } catch (Exception e) {
                    ExceptionDialog.hideException(e);
                }
            }

            configurations.put(key, configuration);
        }

        return configuration;
    }

    private AbstractConfiguration load(Class<?> clazz) {
        ConfigurationPreference preference;
        File file;

        preference = new ConfigurationPreference(clazz);
        file = preference.getFile();

        return load(clazz, file);
    }

    private AbstractConfiguration load(Class clazz, File file) {
        if (file.exists()) {
            try {
                return ConfigurationPersister.getInstance().load(clazz, file);
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        }

        return null;
    }

    void addConfigurationListener(Class<?> clazz,
            ConfigurationListenerIF listener) {
        WeakHashSet<ConfigurationListenerIF> listeners;
        String key;

        key = clazz.getName();

        listeners = listenerMap.get(key);
        if (listeners == null) {
            listeners = new WeakHashSet<ConfigurationListenerIF>();
            listenerMap.put(key, listeners);
        }

        listeners.add(listener);
    }

    void removeConfigurationListener(Class<?> clazz,
            ConfigurationListenerIF listener) {
        Set<ConfigurationListenerIF> listeners;

        listeners = listenerMap.get(clazz.getName());
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    void fireChanged(Class<?> clazz) {
        Set<ConfigurationListenerIF> listeners;

        listeners = listenerMap.get(clazz.getName());
        if (listeners != null) {
            for (ConfigurationListenerIF listener : listeners) {
                listener.configurationChanged();
            }
        }
    }
}