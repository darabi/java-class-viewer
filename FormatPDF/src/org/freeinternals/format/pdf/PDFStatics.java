package org.freeinternals.format.pdf;

/**
 *
 * @author Amos
 */
public class PDFStatics {

    static final byte[] END_OF_FILE = {'%', '%', 'E', 'O', 'F'};
    static final byte[] START_XREF = {'s','t','a','r','t','x','r','e','f'};

    /**
     * See <pre>PDF 32000-1:2008</pre> <code>7.2.2</code>: Table 1 – White-space characters.
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
}
