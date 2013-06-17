package org.freeinternals.format.pdf;

import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.ui.GenerateTreeNode;
import org.freeinternals.commonlib.ui.JTreeNodeFileComponent;
import org.freeinternals.format.FileFormatException;

/**
 * See
 * <pre>PDF 32000-1:2008</pre>
 * <code>7.5.5</code>: File Trailer.
 *
 * @author Amos Shi
 */
public class StartXRef extends FileComponent implements GenerateTreeNode {

    static final String SIGNATURE = "startxref";
    public final int Offset;

    StartXRef(PosDataInputStream stream, String line) throws IOException, FileFormatException {
        super.startPos = stream.getPos() - line.length() - 1;
        this.Offset = Integer.valueOf(stream.readASCIIUntil(PDFStatics.WhiteSpace.LF, PDFStatics.WhiteSpace.CR));
        super.length = stream.getPos() - super.startPos;
    }

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                super.startPos,
                super.length,
                "Start X Ref");
        DefaultMutableTreeNode nodeTrailer = new DefaultMutableTreeNode(nodeComp);
        parentNode.add(nodeTrailer);

        int pos = this.startPos;
        nodeTrailer.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                StartXRef.SIGNATURE.length(),
                "Signature")));
        pos += StartXRef.SIGNATURE.length();
        nodeTrailer.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                1,
                "New Line")));
        pos += 1;
        int len = super.length - SIGNATURE.length() - 1 - 1;
        nodeTrailer.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                len,
                String.format("Offset of last Cross Reference Section: %d (%08X)", this.Offset, this.Offset))));
        pos += len;
        nodeTrailer.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                1,
                "New Line")));
    }
}
