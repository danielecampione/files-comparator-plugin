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
import java.io.IOException;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class AbstractConfiguration {

    private boolean changed;
    private ConfigurationPreference preference;
    private boolean disableFireChanged;

    public AbstractConfiguration() {
        preference = new ConfigurationPreference(getClass());
    }

    public abstract void init();

    public String getConfigurationFileName() {
        try {
            return preference.getFile().getCanonicalPath();
        } catch (IOException ex) {
            return "??";
        }
    }

    public void setConfigurationFile(File file) {
        preference.setFile(file);
    }

    public boolean isChanged() {
        return changed;
    }

    public void save() {
        try {
            ConfigurationPersister.getInstance().save(this,
                    preference.getFile());
            changed = false;
            fireChanged(changed);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addConfigurationListener(ConfigurationListenerIF listener) {
        getManager().addConfigurationListener(getClass(), listener);
    }

    public void removeConfigurationListener(ConfigurationListenerIF listener) {
        getManager().removeConfigurationListener(getClass(), listener);
    }

    void disableFireChanged(boolean disableFireChanged) {
        this.disableFireChanged = disableFireChanged;
    }

    public void fireChanged() {
        if (disableFireChanged) {
            return;
        }

        fireChanged(true);
    }

    public void fireChanged(boolean changed) {
        this.changed = changed;
        getManager().fireChanged(getClass());
    }

    private ConfigurationManager getManager() {
        return ConfigurationManager.getInstance();
    }
}
