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
public class EndOfFile extends FileComponent implements GenerateTreeNode {

    static final String SIGNATURE = "%%EOF";

    EndOfFile(PosDataInputStream stream, String line) throws IOException, FileFormatException {
        super.length = line.length() + 1;
        super.startPos = stream.getPos() - super.length;
    }

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                this.getStartPos(),
                super.length,
                "End of File");
        DefaultMutableTreeNode nodeEoF = new DefaultMutableTreeNode(nodeComp);
        nodeEoF.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                this.startPos,
                SIGNATURE.length(),
                "Signature")));
        nodeEoF.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                this.startPos + SIGNATURE.length(),
                1,
                "New Line")));
        parentNode.add(nodeEoF);
    }
}
