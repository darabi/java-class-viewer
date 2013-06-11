package org.freeinternals.format.pdf;

import java.util.ResourceBundle;

/**
 *
 * @author Amos Shi
 */
public class Descriptions {

    private static final ResourceBundle res;

    static{
        res = ResourceBundle.getBundle(Descriptions.class.getName().replace('.', '/'));
    }

    public static String getString(String key){
        return res.getString(key);
    }
    
     public static final String KEY_PDF_FILE_HEADER = "PDF_FILE_HEADER";
}
