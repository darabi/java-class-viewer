/**
 * PDFFile.java    May 17, 2011, 09:29
 *
 * Copyright 2011, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.pdf;

import java.io.File;
import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.biv.plugin.FileFormat;
import org.freeinternals.commonlib.core.PosByteArrayInputStream;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.ui.JTreeNodeFileComponent;
import org.freeinternals.format.FileFormatException;

/**
 *
 * @author Amos Shi
 * @see <a href="http://www.adobe.com/devnet/pdf/pdf_reference.html">PDF Reference</a>
 */
public class PDFFile extends FileFormat {

    public PDFFile(final File file) throws IOException, FileFormatException {
        super(file);
        this.parse();
    }

    static final byte[] END_OF_FILE = {'%', '%', 'E', 'O', 'F'};
    static final byte[] START_XREF = {'s','t','a','r','t','x','r','e','f'};

    private int posEoF;
    private int posStartXref;
    private void parse() throws IOException{
        PosDataInputStream stream = new PosDataInputStream(new PosByteArrayInputStream(this.fileByteArray));

        stream.skipToEnd();
        this.posEoF = stream.backwardTo(END_OF_FILE);
        this.posStartXref = stream.backwardTo(START_XREF);
    }

    public String getContentTabName() {
        return "PDF File";
    }

    public void generateTreeNode(DefaultMutableTreeNode root) {
        root.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                this.posStartXref,
                START_XREF.length,
                "xref position")));
        root.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                this.posEoF,
                END_OF_FILE.length,
                "End of File")));
    }
}
