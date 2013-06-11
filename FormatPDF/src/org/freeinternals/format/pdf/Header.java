package org.freeinternals.format.pdf;

import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.ui.GenerateTreeNode;
import org.freeinternals.commonlib.ui.JTreeNodeFileComponent;

/**
 *
 * @author Amos Shi
 */
public class Header extends FileComponent implements GenerateTreeNode {

    /**
     * The first line of a PDF file shall be a header consisting of the 5
     * characters %PDF– followed by a version number of the form 1.N, where N is
     * a digit between 0 and 7.
     */
    public static final String PDF_HEADER = "%PDF-";
    public final String Version;

    Header(PosDataInputStream stream) throws IOException {
        super.startPos = 0;
        this.Version = stream.readASCIIUntil(PDFStatics.WhiteSpace.LF);
        super.length = stream.getPos() - super.startPos;
    }

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                    this.getStartPos(),
                    super.length,
                    String.format("PDF Header: version = %s", this.Version));
        nodeComp.setDescription(Descriptions.getString(Descriptions.KEY_PDF_FILE_HEADER));
        DefaultMutableTreeNode treenodePDFHeader = new DefaultMutableTreeNode(nodeComp);

        treenodePDFHeader.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                this.startPos,
                PDF_HEADER.length(),
                "PDF Signature"
                )));
        treenodePDFHeader.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                this.startPos + PDF_HEADER.length(),
                this.Version.length(),
                this.Version
                )));
        parentNode.add(treenodePDFHeader);
    }
}
