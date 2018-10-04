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

package org.apache.commons.jrcs.diff;

import java.util.List;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * Holds a "delta" difference between to revisions of a text.
 *
 * @author D. Campione
 * @see Diff
 * @see Chunk
 * @see Revision
 */
public abstract class Delta extends org.apache.commons.jrcs.util.ToString {

    protected Chunk original;
    protected Chunk revised;
    static Class[][] DeltaClass;

    static {
        DeltaClass = new Class[2][2];
        try {
            DeltaClass[0][0] = org.apache.commons.jrcs.diff.ChangeDelta.class;
            DeltaClass[0][1] = org.apache.commons.jrcs.diff.AddDelta.class;
            DeltaClass[1][0] = org.apache.commons.jrcs.diff.DeleteDelta.class;
            DeltaClass[1][1] = org.apache.commons.jrcs.diff.ChangeDelta.class;
        } catch (Throwable t) {
            ExceptionDialog.ignoreException(t);
        }
    }

    /**
     * Returns a Delta that corresponds to the given chunks in the
     * original and revised text respectively.
     * 
     * @param orig the chunk in the original text.
     * @param rev  the chunk in the revised text.
     */

    public static Delta newDelta(Chunk orig, Chunk rev) {
        Class<?> c = DeltaClass[orig.size() > 0 ? 1 : 0][rev.size() > 0 ? 1 : 0];
        Delta result;

        try {
            result = (Delta) c.newInstance();
        } catch (Throwable t) {
            return null;
        }

        result.init(orig, rev);
        return result;
    }

    /**
     * Creates an uninitialized delta.
     */
    protected Delta() {
    }

    /**
     * Creates a delta object with the given chunks from the original
     * and revised texts.
     */
    protected Delta(Chunk orig, Chunk rev) {
        init(orig, rev);
    }

    /**
     * Initializaes the delta with the given chunks from the original
     * and revised texts.
     */
    protected void init(Chunk orig, Chunk rev) {
        original = orig;
        revised = rev;
    }

    /**
     * Verifies that this delta can be used to patch the given text.
     * @param target the text to patch.
     * @throws PatchFailedException if the patch cannot be applied.
     */
    public abstract void verify(List<?> target) throws PatchFailedException;

    /**
     * Applies this delta as a patch to the given text.
     * @param target the text to patch.
     * @throws PatchFailedException if the patch cannot be applied.
     */
    public final void patch(List<?> target) throws PatchFailedException {
        verify(target);
        try {
            applyTo(target);
        } catch (Exception e) {
            throw new PatchFailedException(e.getMessage());
        }
    }

    /**
     * Applies this delta as a patch to the given text.
     * @param target the text to patch.
     * @throws PatchFailedException if the patch cannot be applied.
     */
    public abstract void applyTo(List<?> target);

    /**
     * Converts this delta into its Unix diff style string representation.
     * @param s a {@link StringBuilder StringBuilder} to which the string
     * representation will be appended.
     */
    @Override
    public void toString(StringBuilder s) {
        original.rangeString(s);
        s.append("x");
        revised.rangeString(s);
        s.append(Diff.NL);
        original.toString(s, "> ", "\n");
        s.append("---");
        s.append(Diff.NL);
        revised.toString(s, "< ", "\n");
    }

    /**
     * Converts this delta into its RCS style string representation.
     * @param s a {@link StringBuilder StringBuilder} to which the string
     * representation will be appended.
     * @param EOL the string to use as line separator.
     */
    public abstract void toRCSString(StringBuilder s, String EOL);

    /**
     * Converts this delta into its RCS style string representation.
     * @param EOL the string to use as line separator.
     */
    public String toRCSString(String EOL) {
        StringBuilder s = new StringBuilder();

        toRCSString(s, EOL);
        return s.toString();
    }

    /**
     * Accessor method to return the chunk representing the original
     * sequence of items
     *
     * @return the original sequence
     */
    public Chunk getOriginal() {
        return original;
    }

    /**
     * Accessor method to return the chunk representing the updated
     * sequence of items.
     *
     * @return the updated sequence
     */
    public Chunk getRevised() {
        return revised;
    }

    /**
     * Accepts a visitor.
     * <p>
     * See the Visitor pattern in "Design Patterns" by the GOF4.
     * @param visitor The visitor.
     */
    public abstract void accept(RevisionVisitor visitor);
}