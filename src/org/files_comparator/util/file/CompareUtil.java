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

package org.files_comparator.util.file;

import java.io.CharArrayReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.files_comparator.util.Ignore;
import org.files_comparator.util.node.BufferNode;
import org.files_comparator.util.node.FileNode;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class CompareUtil {

    private static CharBuffer leftLineBuffer = CharBuffer.allocate(10000);
    private static CharBuffer rightLineBuffer = CharBuffer.allocate(10000);
    private static CharBuffer leftLineOutputBuffer = CharBuffer.allocate(10000);
    private static CharBuffer rightLineOutputBuffer = CharBuffer
            .allocate(10000);

    private CompareUtil() {
    }

    public static boolean contentEquals(BufferNode nodeLeft,
            BufferNode nodeRight, Ignore ignore) {
        if (nodeLeft instanceof FileNode && nodeRight instanceof FileNode) {
            return contentEquals((FileNode) nodeLeft, (FileNode) nodeRight,
                    ignore);
        } else {
            try {
                return contentEquals(nodeLeft.getDocument().getReader(),
                        nodeRight.getDocument().getReader(), ignore);
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        }

        return false;
    }

    private static boolean contentEquals(FileNode nodeLeft, FileNode nodeRight,
            Ignore ignore) {
        File fileLeft;
        File fileRight;
        RandomAccessFile fLeft;
        RandomAccessFile fRight;
        FileChannel fcLeft;
        FileChannel fcRight;
        ByteBuffer bbLeft;
        ByteBuffer bbRight;
        boolean equals;

        fileLeft = nodeLeft.getFile();
        fileRight = nodeRight.getFile();

        fLeft = null;
        fRight = null;

        try {
            if (fileLeft.isDirectory() || fileRight.isDirectory()) {
                return true;
            }

            if (!ignore.ignore && fileLeft.length() != fileRight.length()) {
                return false;
            }

            // In practice most files that have the same length will
            //   be equal. So eventhough some ignore feature is activated
            //   we will examine if the files are equal. If they are
            //   equal we won't have to execute the expensive 
            //   contentEquals method below. This should speed up directory
            //   comparisons quite a bit.
            if (!ignore.ignore || fileLeft.length() == fileRight.length()) {
                fLeft = new RandomAccessFile(fileLeft, "r");
                fRight = new RandomAccessFile(fileRight, "r");
                fcLeft = fLeft.getChannel();
                fcRight = fRight.getChannel();

                bbLeft = fcLeft.map(FileChannel.MapMode.READ_ONLY, 0,
                        (int) fcLeft.size());
                bbRight = fcRight.map(FileChannel.MapMode.READ_ONLY, 0,
                        (int) fcRight.size());

                equals = bbLeft.equals(bbRight);
                if (!ignore.ignore || equals) {
                    return equals;
                }
            }

            equals = contentEquals(nodeLeft.getDocument().getReader(),
                    nodeRight.getDocument().getReader(), ignore);

            return equals;
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
            return false;
        } finally {
            try {
                if (fLeft != null) {
                    fLeft.close();
                }
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }

            try {
                if (fRight != null) {
                    fRight.close();
                }
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        }
    }

    public static boolean contentEquals(char[] left, char[] right, Ignore ignore) {
        try {
            return contentEquals(new CharArrayReader(left),
                    new CharArrayReader(right), ignore);
        } catch (IOException ioe) {
            // IOException will never happen
            return false;
        }
    }

    /** Test if 2 readers are equals (with ignore possibilities).
     *  Synchronized because leftLine and rightLine are static variables for
     *    performance reasons.
     */
    private static synchronized boolean contentEquals(Reader readerLeft,
            Reader readerRight, Ignore ignore) throws IOException {
        boolean leftEOF, rightEOF;

        try {
            for (;;) {
                for (;;) {
                    leftEOF = readLine(readerLeft, leftLineBuffer);

                    removeIgnoredChars(leftLineBuffer, ignore,
                            leftLineOutputBuffer);
                    if (leftLineOutputBuffer.remaining() != 0) {
                        break;
                    }

                    if (leftEOF) {
                        break;
                    }
                }

                for (;;) {
                    rightEOF = readLine(readerRight, rightLineBuffer);

                    removeIgnoredChars(rightLineBuffer, ignore,
                            rightLineOutputBuffer);
                    if (rightLineOutputBuffer.remaining() != 0) {
                        break;
                    }

                    if (rightEOF) {
                        break;
                    }
                }

                if (leftLineOutputBuffer.remaining() != 0
                        && rightLineOutputBuffer.remaining() != 0) {
                    if (!leftLineOutputBuffer.equals(rightLineOutputBuffer)) {
                        return false;
                    }
                }

                if (leftEOF && !rightEOF || !leftEOF && rightEOF) {
                    return false;
                }

                if (leftEOF && rightEOF) {
                    return true;
                }
            }
        } finally {
            readerLeft.close();
            readerRight.close();
        }
    }

    private static boolean readLine(Reader reader, CharBuffer lineBuffer)
            throws IOException {
        int c, nextChar;

        lineBuffer.clear();
        while ((c = reader.read()) != -1) {
            lineBuffer.put((char) c);

            if (c == '\n') {
                break;
            }

            if (c == '\r') {
                reader.mark(1);
                nextChar = reader.read();
                if (nextChar == '\n') {
                    lineBuffer.put((char) nextChar);
                    break;
                } else {
                    reader.reset();
                }

                break;
            }
        }

        if (c == -1) {
            return true;
        }

        return false;
    }

    public static boolean isEOL(int character) {
        return character == '\n' || character == '\r';
    }

    /** Remove all characters from the 'line' that can be ignored.
     *  @param  inputLine char[] representing a line.
     *  @param  ignore an object with the ignore options.
     *  @param  outputLine return value which contains all characters from line that cannot be
     *          ignored. It is a parameter that can be reused (which is important for
     *          performance)
     */
    public static void removeIgnoredChars(CharBuffer inputLine, Ignore ignore,
            CharBuffer outputLine) {
        boolean whitespaceAtBegin;
        boolean blankLine;
        int lineEndingEndIndex;
        int whitespaceEndIndex;
        int length;
        char c;
        boolean whiteSpaceInBetweenIgnored;

        inputLine.flip();
        outputLine.clear();

        length = inputLine.remaining();
        lineEndingEndIndex = length;
        blankLine = true;
        whiteSpaceInBetweenIgnored = false;

        c = 0;

        for (int index = lineEndingEndIndex - 1; index >= 0; index--) {
            if (!isEOL(inputLine.charAt(index))) {
                break;
            }

            lineEndingEndIndex--;
        }

        whitespaceEndIndex = lineEndingEndIndex;
        for (int index = whitespaceEndIndex - 1; index >= 0; index--) {
            if (!Character.isWhitespace(inputLine.charAt(index))) {
                break;
            }

            whitespaceEndIndex--;
        }

        whitespaceAtBegin = true;
        for (int i = 0; i < length; i++) {
            c = inputLine.get(i);

            if (i < whitespaceEndIndex) {
                if (Character.isWhitespace(c)) {
                    if (whitespaceAtBegin) {
                        if (ignore.ignoreWhitespaceAtBegin) {
                            continue;
                        }
                    } else {
                        if (ignore.ignoreWhitespaceInBetween) {
                            whiteSpaceInBetweenIgnored = true;
                            continue;
                        }
                    }
                }

                whitespaceAtBegin = false;
                blankLine = false;

                // The character won't be ignored!
            } else if (i < lineEndingEndIndex) {
                if (ignore.ignoreWhitespaceAtEnd) {
                    continue;
                }
                blankLine = false;

                // The character won't be ignored!
            } else {
                if (ignore.ignoreEOL) {
                    continue;
                }
                // The character won't be ignored!
            }

            if (ignore.ignoreCase) {
                c = Character.toLowerCase(c);
            }

            if (whiteSpaceInBetweenIgnored) {
                //outputLine.put(' ');
                whiteSpaceInBetweenIgnored = false;
            }
            outputLine.put(c);
        }

        if (outputLine.position() == 0 && !ignore.ignoreBlankLines) {
            outputLine.put('\n');
        }

        if (blankLine && ignore.ignoreBlankLines) {
            outputLine.clear();
        }

        outputLine.flip();
    }
}