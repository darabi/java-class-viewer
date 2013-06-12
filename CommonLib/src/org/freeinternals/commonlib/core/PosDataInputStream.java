/*
 * PosDataInputStream.java    August 8, 2007, 12:48 PM
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.commonlib.core;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import org.freeinternals.commonlib.util.Tool;

/**
 *
 * @author Amos Shi
 * @since JDK 6.0
 */
public class PosDataInputStream extends DataInputStream implements DataInputEx {

    /**
     * Offset of the 1st byte
     */
    private int offset = 0;

    /**
     * Creates a new instance of PosDataInputStream
     *
     * @param in
     */
    public PosDataInputStream(final PosByteArrayInputStream in) {
        super(in);
    }

    public PosDataInputStream(final PosByteArrayInputStream in, int offset) {
        super(in);
        this.offset = offset;
    }

    /**
     * Get the absolute position of the starting point of the buffer.
     *
     * @return buffer absolute position
     */
    public int getOffset() {
        return this.offset;
    }

    /**
     * Get current absolute position of the file.
     *
     * @return The index of the next character to read from the input stream
     * buffer, or <code>-1</code> if there is internal error, the input stream
     * is not <code>PosByteArrayInputStream</code>.
     */
    public int getPos() {
        int pos = -1;
        if (this.in instanceof PosByteArrayInputStream) {
            pos = ((PosByteArrayInputStream) this.in).getPos() + this.offset;
        }

        return pos;
    }

    /**
     * Get the byte array buffer of the input stream.
     *
     * @return the byte array
     */
    public byte[] getBuf() {
        if (this.in instanceof PosByteArrayInputStream) {
            return ((PosByteArrayInputStream) this.in).getBuf();
        } else {
            return null;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Interface Methods
    public int readUnsignedShort_LittleEndian() throws IOException {
        int ch1 = this.in.read();
        int ch2 = this.in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (ch2 << 8) + (ch1);
    }

    public int readInt_LittleEndian() throws IOException {
        int ch1 = this.in.read();
        int ch2 = this.in.read();
        int ch3 = this.in.read();
        int ch4 = this.in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1));
    }

    public long readUnsignedInt() throws IOException {
        final byte readBuffer[] = new byte[8];

        super.readFully(readBuffer, 4, 4);
        readBuffer[0] = 0;
        readBuffer[1] = 0;
        readBuffer[2] = 0;
        readBuffer[3] = 0;

        return (((long) readBuffer[0] << 56)
                + ((long) (readBuffer[1] & 255) << 48)
                + ((long) (readBuffer[2] & 255) << 40)
                + ((long) (readBuffer[3] & 255) << 32)
                + ((long) (readBuffer[4] & 255) << 24)
                + ((readBuffer[5] & 255) << 16)
                + ((readBuffer[6] & 255) << 8)
                + ((readBuffer[7] & 255)));
    }

    public long readUnsignedInt_LittleEndian() throws IOException {
        final byte readBuffer[] = new byte[8];

        super.readFully(readBuffer, 0, 4);
        readBuffer[7] = readBuffer[0];
        readBuffer[6] = readBuffer[1];
        readBuffer[5] = readBuffer[2];
        readBuffer[4] = readBuffer[3];
        readBuffer[0] = 0;
        readBuffer[1] = 0;
        readBuffer[2] = 0;
        readBuffer[3] = 0;

        return (((long) readBuffer[0] << 56)
                + ((long) (readBuffer[1] & 255) << 48)
                + ((long) (readBuffer[2] & 255) << 40)
                + ((long) (readBuffer[3] & 255) << 32)
                + ((long) (readBuffer[4] & 255) << 24)
                + ((readBuffer[5] & 255) << 16)
                + ((readBuffer[6] & 255) << 8)
                + ((readBuffer[7] & 255)));
    }

    public String readASCII(int length) throws IOException {
        if (length <= 0) {
            throw new IllegalArgumentException(
                    String.format("Parameter length should be greater than 0. (length = %d)", length));
        }

        StringBuilder sb = new StringBuilder(length + 1);
        for (int i = 0; i < length; i++) {
            sb.append((char) this.readByte());
        }
        return sb.toString();
    }

    public String readASCII() throws IOException {
        return this.readASCIIUntil((byte) 0);
    }

    public String readASCIIUntil(byte end) throws IOException {
        byte b;
        StringBuilder sb = new StringBuilder(100);

        do {
            try {
                b = this.readByte();
                if (b == end) {
                    break;
                }
                sb.append((char) b);
            } catch (EOFException eof) {
                break;
            }
        } while (true);

        return sb.toString();
    }

    public byte[] readBinary() throws IOException {
        int size = this.getBuf().length - this.getPos() + this.offset + 1;
        byte[] big = new byte[size];
        int big_counter = 0;
        byte b;

        do {
            try {
                b = this.readByte();
                if (b == 0) {
                    break;
                }
                big[big_counter] = b;
                big_counter++;
            } catch (EOFException eof) {
                break;
            }
        } while (true);

        if (big_counter > 0) {
            byte[] result = new byte[big_counter];
            System.arraycopy(big, 0, result, 0, big_counter);
            return result;
        } else {
            return null;
        }
    }

    /**
     * Set the current position back for
     * <code>i</code> positions.
     *
     * This method supports {@link PosByteArrayInputStream} only, nothing will
     * do for other input stream types.
     *
     * @see PosByteArrayInputStream
     */
    public int backward(int i) {
        int result = -1;

        if (this.in instanceof PosByteArrayInputStream) {
            PosByteArrayInputStream posIn = ((PosByteArrayInputStream) this.in);
            int currentPos = posIn.getPos();
            ((PosByteArrayInputStream) this.in).setPos(
                    result = ((currentPos - i) > 0) ? (currentPos - i) : 0);
        }

        return result;
    }

    /**
     * Backward current position until the byte value
     * <code>b</code>.
     *
     * This method supports {@link PosByteArrayInputStream} only as input stream
     * only, otherwise
     * <code>-1</code> is returned.
     *
     * @see PosByteArrayInputStream
     * @return the new position, or -1 if <code>b</code> not found
     */
    public int backwardTo(byte b) {
        int result = -1;

        if (this.in instanceof PosByteArrayInputStream) {
            PosByteArrayInputStream posIn = ((PosByteArrayInputStream) this.in);
            byte[] buf = posIn.getBuf();
            for (int i = posIn.getPos(); i > -1; i--) {
                if (buf[i] == b) {
                    result = i;
                    break;
                }
            }

            if (result != -1) {
                posIn.setPos(result);
            }
        }

        return result;
    }

    /**
     * Forward current position until the byte value
     * <code>b</code>.
     *
     * This method supports {@link PosByteArrayInputStream} only as input stream
     * only, otherwise
     * <code>-1</code> is returned.
     *
     * @see PosByteArrayInputStream
     * @return the new position, or -1 if <code>b</code> not found
     */
    public int forwardTo(byte b) {
        int result = -1;

        if (this.in instanceof PosByteArrayInputStream) {
            PosByteArrayInputStream posIn = ((PosByteArrayInputStream) this.in);
            byte[] buf = posIn.getBuf();
            for (int i = posIn.getPos(); i < buf.length; i++) {
                if (buf[i] == b) {
                    result = i;
                    break;
                }
            }

            if (result != -1) {
                posIn.setPos(result);
            }
        }

        return result;
    }

    /**
     * Backward current position until the byte array value
     * <code>b</code>.
     *
     * This method supports {@link PosByteArrayInputStream} only as input stream
     * only, otherwise
     * <code>-1</code> is returned.
     *
     * @see PosByteArrayInputStream
     */
    public int backwardTo(byte[] b) {
        int result = -1;

        if ((b == null) || (b.length == 0)) {
            throw new IllegalArgumentException("Parameter b is null or empty.");
        }

        if (this.in instanceof PosByteArrayInputStream) {
            PosByteArrayInputStream posIn = ((PosByteArrayInputStream) this.in);
            byte[] buf = posIn.getBuf();
            for (int i = posIn.getPos() - b.length; i > -1; i--) {
                if (Tool.isByteArraySame(b, buf, i)) {
                    result = i;
                    break;
                }
            }

            if (result != -1) {
                posIn.setPos(result);
            }
        }

        return result;
    }

    /**
     * This method supports {@link PosByteArrayInputStream} only, nothing will
     * do for other input stream types.
     *
     * @see PosByteArrayInputStream
     */
    public void skipToEnd() throws IOException {
        if (this.in instanceof PosByteArrayInputStream) {
            PosByteArrayInputStream posIn = ((PosByteArrayInputStream) this.in);
            posIn.setPos(0);
            posIn.skip(posIn.getBuf().length);
        }
    }

    /**
     * This method supports {@link PosByteArrayInputStream} only, nothing will
     * do for other input stream types.
     *
     * @see PosByteArrayInputStream
     */
    public void flyTo(int position) {
        if (this.in instanceof PosByteArrayInputStream) {
            ((PosByteArrayInputStream) this.in).setPos(position);
        }
    }

    /**
     * Whether current location is the end or not.
     *
     * @return true Still has next byte to be read; false Current location is
     * the end
     */
    public boolean hasNext() {
        return this.getPos() < (this.getBuf().length - 1);
    }
}
