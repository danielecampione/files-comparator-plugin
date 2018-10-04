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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Result {

    // instance variables
    private boolean result;
    private String description = "";
    private Exception exception;

    private Result(boolean result, String description, Exception exception) {
        this.result = result;
        this.description = description;
        this.exception = exception;
    }

    public static Result TRUE() {
        return new Result(true, null, null);
    }

    public static Result FALSE(String description) {
        return new Result(false, description, null);
    }

    public static Result FALSE(String description, Exception ex) {
        return new Result(false, description, ex);
    }

    public boolean isTrue() {
        return getResult();
    }

    public boolean isFalse() {
        return !getResult();
    }

    public boolean getResult() {
        return result;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }

        return description.toString();
    }

    public boolean hasException() {
        return exception != null;
    }

    public Exception getException() {
        return exception;
    }

    public String toString() {
        return result + " :" + description;
    }
}
