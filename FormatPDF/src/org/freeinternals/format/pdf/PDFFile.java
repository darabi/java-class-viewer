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

        // File Header - Verify if this is an PDF file
        String pdfHeader = stream.readASCII(Header.PDF_HEADER.length());
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
        while (stream.hasNext()) {
            line = stream.readASCIIUntil(PDFStatics.WhiteSpace.LF, PDFStatics.WhiteSpace.CR);
            System.out.println("PDFFile: line = " + line);
            if (line.length() == 0) {
                // Error
                break;
            }

            if (line.equalsIgnoreCase(EndOfFile.SIGNATURE)) {                     // %%EOF
                this.components.add(new EndOfFile(stream, line));
            } else if (line.charAt(0) == PDFStatics.DelimiterCharacter.PS_CHAR) { // %, Comment line
                this.components.add(new Comment(stream, line));
            } else if (line.endsWith(IndirectObject.SIGNATURE_START)) {           // obj
                this.components.add(new IndirectObject(stream, line));
            } else if (line.equalsIgnoreCase(CrossReferenceTable.SIGNATURE)) {     // xref
                this.components.add(new CrossReferenceTable(stream, line));
            } else if (line.equalsIgnoreCase(Trailer.SIGNATURE)) {                 // trailer
                this.components.add(new Trailer(stream, line));
            } else if (line.equalsIgnoreCase(StartXRef.SIGNATURE)) {               // startxref
                this.components.add(new StartXRef(stream, line));
            }

            counter++;
            System.out.println("Counter: " + counter);
            if (counter >= 39) {
                break;
            }
        }
    }

    public String getContentTabName() {
        return "PDF File";
    }

    public void generateTreeNode(DefaultMutableTreeNode root) {
        System.err.println(String.format("Generate Tree Node: Get Component Size: %d", this.components.size()));
        for (FileComponent comp : this.components) {
            if (comp instanceof GenerateTreeNode) {
                ((GenerateTreeNode) comp).generateTreeNode(root);
            }
        }
    }
}
