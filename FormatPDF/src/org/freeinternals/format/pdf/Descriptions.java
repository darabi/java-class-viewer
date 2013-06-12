package org.freeinternals.format.pdf;

import java.util.ResourceBundle;

/**
 *
 * @author Amos Shi
 */
public class Descriptions {

    private static final ResourceBundle res;

    static {
        res = ResourceBundle.getBundle(Descriptions.class.getName().replace('.', '/'));
    }

    public static String getString(String key) {
        return res.getString(key);
    }
    public static final String PDF_FILE_HEADER = "PDF_FILE_HEADER";
    public static final String PDF_INDIRECT_OBJECT = "PDF_INDIRECT_OBJECT";
    public static final String PDF_CROSS_REFERENCE_TABLE = "PDF_CROSS_REFERENCE_TABLE";
    
}
