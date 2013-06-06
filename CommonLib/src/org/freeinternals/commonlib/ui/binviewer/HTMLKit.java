package org.freeinternals.commonlib.ui.binviewer;

import org.freeinternals.commonlib.ui.JBinaryViewer;

/**
 *
 * @author Amos
 */
class HTMLKit {

    private static final int FONT_SIZE = 12;
    protected static final String FONT_COLOR_YELLOW = "yellow";
    protected static final String FONT_COLOR_ORANGE = "#FFA500";

    static String Start(){
        return "<!DOCTYPE html><html><head></head><body>";
    }
    
    static String End(){
        return "\n</body>\n</html>";
        
    }
    
    static String NewLine(){
        return "<br />";
    }
    
    static String Space(){
        return "&nbsp;";
    }

    /**
     * Get HTML format for text with new line.
     */
    static String Span(String text) {
        return String.format("\n<span style=\"font-size:%dpx; font-family:%s;\">%s</span>",
                FONT_SIZE,
                JBinaryViewer.FONT.getFamily(),
                text);
    }

    static String Span(String text, String color) {
        return String.format("\n<span style=\"background-color:%s; font-size:%dpx; font-family:%s;\">%s</span>",
                color,
                FONT_SIZE,
                JBinaryViewer.FONT.getFamily(),
                text);
    }

    /**
     * Get HTML marks for the byte.
     * 
     * @see <a href="http://ascii.cl/htmlcodes.htm">HTML Codes - Characters and symbols</a>
     */
    static public String getByteText(final byte b) {
        String s = ".";

        if (((b > 32) && (b < 127)) ||
                ((b > 160) && (b <= 255))) {
            s = String.format("&#%d;", b);
        }

        return s;
    }

}
