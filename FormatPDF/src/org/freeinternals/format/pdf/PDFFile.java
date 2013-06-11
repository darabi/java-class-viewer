/**
 * PDFFile.java May 17, 2011, 09:29
 *
 * Copyright 2011, FreeInternals.org. All rights reserved. Use is subject to
 * license terms.
 */
package org.freeinternals.format.pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.biv.plugin.FileFormat;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosByteArrayInputStream;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.ui.GenerateTreeNode;
import org.freeinternals.format.FileFormatException;

/**
 *
 * @author Amos Shi
 * @see <a href="http://www.adobe.com/devnet/pdf/pdf_reference.html">PDF
 * Reference</a>
 */
public class PDFFile extends FileFormat {
    
    private List<FileComponent> components = Collections.synchronizedList(new ArrayList<FileComponent>(100));
    
    public PDFFile(final File file) throws IOException, FileFormatException {
        super(file);
        this.parse();
    }
    
    private void parse() throws IOException, FileFormatException {
        PosDataInputStream stream = new PosDataInputStream(new PosByteArrayInputStream(super.fileByteArray));
        System.out.println("PDF File Length = " + super.fileByteArray.length);

        // File Header - Verify if this is an PDF file
        System.out.println(Header.PDF_HEADER.length());
        String pdfHeader = stream.readASCII(Header.PDF_HEADER.length());
        System.out.println("PDF Header read result: " + pdfHeader);
        if (!Header.PDF_HEADER.equals(pdfHeader)) {
            throw new FileFormatException(String.format(
                    "This is not a PDF file. Expeted file header [%s], but it is [%s].",
                    Header.PDF_HEADER,
                    pdfHeader));
        }
        Header header = new Header(stream);
        this.components.add(header);


        // Read PDF Components
        String line;
        int counter = 0;
        while (stream.getPos() < (this.fileByteArray.length - 1)) {
            line = stream.readASCIIUntil(PDFStatics.WhiteSpace.LF);
            if (line.length() == 0) {
                // Error
                break;
            }
            
            if (line.equalsIgnoreCase(EndOfFile.SIGNATURE)) {                   // %%EOF
            } else if (line.charAt(0) == PDFStatics.DelimiterCharacter.PS_CHAR) {
                // This is Comment line
            } else if (line.endsWith(IndirectObject.SIGNATURE_START)) {         // obj
            } else if (line.equalsIgnoreCase(CrossReferenceTable.SIGNATURE)){   // xref
            } else if (line.equalsIgnoreCase(Trailer.SIGNATURE)){               // trailer
            } else if (line.equalsIgnoreCase(StartXRef.SIGNATURE)){             // startxref
            }
            
            counter++;
            if (counter >=1) {
                break;
            }
        }
    }
    
    public String getContentTabName() {
        return "PDF File";
    }
    
    public void generateTreeNode(DefaultMutableTreeNode root) {
        for (FileComponent comp : this.components) {
            if (comp instanceof GenerateTreeNode) {
                ((GenerateTreeNode) comp).generateTreeNode(root);
            }
        }
    }
}
