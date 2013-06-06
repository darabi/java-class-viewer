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
import org.freeinternals.format.FileFormatException;

/**
 *
 * @author Amos Shi
 * @see <a href="http://www.adobe.com/devnet/pdf/pdf_reference.html">PDF Reference</a>
 */
public class PDFFile extends FileFormat {
    
    private Header header;
    private Body body;
    private CRTable crTable;
    private Trailer trailer;

    public PDFFile(final File file) throws IOException, FileFormatException {
        super(file);
        this.parse();
    }


    private void parse() throws IOException{
        PosDataInputStream stream = new PosDataInputStream(new PosByteArrayInputStream(this.fileByteArray));
        stream.readASCIIUntil(PDFStatics.WhiteSpace.LF);
        stream.skipToEnd();
        
//        this.posEoF = stream.backwardTo(PDFStatics.END_OF_FILE);
//        this.posStartXref = stream.backwardTo(PDFStatics.START_XREF);
    }

    public String getContentTabName() {
        return "PDF File";
    }

    public void generateTreeNode(DefaultMutableTreeNode root) {
//        root.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
//                this.posStartXref,
//                PDFStatics.START_XREF.length,
//                "xref position")));
//        root.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
//                this.posEoF,
//                PDFStatics.END_OF_FILE.length,
//                "End of File")));
    }

    


}
