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

package org.files_comparator.vc.svn;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.files_comparator.util.JaxbPersister;
import org.files_comparator.util.Result;
import org.files_comparator.vc.util.VcCmd;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SvnXmlCmd<T> extends VcCmd<T> {

    private Class<T> clazz;

    public SvnXmlCmd(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void build(byte[] data) {
        InputStream is;

        try {
            is = new ByteArrayInputStream(data);
            setResultData(JaxbPersister.getInstance().load(clazz, is));
            is.close();
            setResult(Result.TRUE());
        } catch (Exception e) {
            setResult(Result.FALSE(e.getMessage(), e));
        }
    }
}