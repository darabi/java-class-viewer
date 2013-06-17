package org.freeinternals.format.pdf;

import java.io.IOException;
import org.freeinternals.commonlib.core.PosDataInputStream;

/**
 *
 * @author Amos
 */
public class PDFStatics {

    /**
     * See
     * <pre>PDF 32000-1:2008</pre>
     * <code>7.2.2</code>: Table 1 – White-space characters.
     */
    public static class WhiteSpace {

        /**
         * Null (NUL).
         */
        public static final byte NUL = 0x00;
        /**
         * HORIZONTAL TAB (HT).
         */
        public static final byte HT = 0x09;
        /**
         * LINE FEED (LF). New line character.
         */
        public static final byte LF = 0x0A;
        /**
         * FORM FEED (FF).
         */
        public static final byte FF = 0x0C;
        /**
         * CARRIAGE RETURN (CR).
         */
        public static final byte CR = 0x0D;
        /**
         * SPACE (SP).
         */
        public static final byte SP = 0x20;
    }

    /**
     * See
     * <pre>PDF 32000-1:2008</pre>
     * <code>7.2.2</code>: Table 2 – Delimiter characters.
     */
    public static class DelimiterCharacter {

        /**
         * LEFT PARENTHESIS:
         * <code>(</code>.
         */
        public static final byte LP = 0x28;
        /**
         * RIGHT PARENTHESIS:
         * <code>)</code>.
         */
        public static final byte RP = 0x29;
        /**
         * LESS-THAN SIGN:
         * <code>&#60;</code>.
         */
        public static final byte LT = 0x3C;
        /**
         * GREATER-THAN SIGN:
         * <code>&#62;</code>.
         */
        public static final byte GT = 0x3E;
        /**
         * LEFT SQUARE BRACKET:
         * <code>[</code>.
         */
        public static final byte LS = 0x5B;
        /**
         * RIGHT SQUARE BRACKET:
         * <code>]</code>.
         */
        public static final byte RS = 0x5D;
        /**
         * LEFT CURLY BRACKET:
         * <code>{</code>.
         */
        public static final byte LC = 0x7B;
        /**
         * RIGHT CURLY BRACKET:
         * <code>}</code>.
         */
        public static final byte RC = 0x7D;
        /**
         * SOLIDUS:
         * <code>/</code>.
         */
        public static final byte SO = 0x2F;
        /**
         * PERCENT SIGN:
         * <code>%</code>.
         */
        public static final byte PS = 0x25;
        /**
         * PERCENT SIGN:
         * <code>%</code>.
         *
         * @see #PS
         */
        public static final char PS_CHAR = '%';
    }

    public static Line readLine(PosDataInputStream stream) throws IOException {
        String line = stream.readASCIIUntil(PDFStatics.WhiteSpace.LF, PDFStatics.WhiteSpace.CR);
        int len = 1;
        if (stream.hasNext()) {
            byte next = stream.readByte();
            if (next == PDFStatics.WhiteSpace.LF || next == PDFStatics.WhiteSpace.CR) {
                len += 1;
            } else {
                stream.backward(1);
            }
        }

        return new Line(line, len);
    }

    public static class Line {

        public final String Line;
        public final int NewLineLength;

        Line(String line, int len) {
            this.Line = line;
            this.NewLineLength = len;
        }
    }
}
