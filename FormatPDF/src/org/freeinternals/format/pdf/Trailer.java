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
public class Trailer extends FileComponent implements GenerateTreeNode {

    static final String SIGNATURE = "trailer";

    Trailer(PosDataInputStream stream, String line) throws IOException, FileFormatException {
        super.startPos = stream.getPos() - line.length() - 1;
        this.parse(stream);
        super.length = stream.getPos() - super.startPos;
    }

    private void parse(PosDataInputStream stream) throws IOException, FileFormatException {
        String line;
        do {
            line = stream.readASCIIUntil(PDFStatics.WhiteSpace.LF, PDFStatics.WhiteSpace.CR);
            if (StartXRef.SIGNATURE.equalsIgnoreCase(line)) {
                stream.backward(StartXRef.SIGNATURE.length() + 1);
                break;
            }
        } while (stream.hasNext());
    }

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                super.startPos,
                super.length,
                "Trailer");
        DefaultMutableTreeNode nodeTrailer = new DefaultMutableTreeNode(nodeComp);
        parentNode.add(nodeTrailer);

        int pos = this.startPos;
        nodeTrailer.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                Trailer.SIGNATURE.length(),
                "Signature")));
        pos += Trailer.SIGNATURE.length();
        nodeTrailer.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                1,
                "New Line")));
        pos += 1;
        int len = super.length - Trailer.SIGNATURE.length() - 1;
        if (len > 0) {
            nodeTrailer.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    len,
                    "Trailer Content")));
        }

    }
}
