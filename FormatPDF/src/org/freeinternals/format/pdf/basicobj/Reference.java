package org.freeinternals.format.pdf.basicobj;

import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.ui.GenerateTreeNode;
import org.freeinternals.commonlib.ui.JTreeNodeFileComponent;

/**
 * Reference of PDF Indirect Object, see
 * <pre>PDF 32000-1:2008</pre>
 * <code>7.3.10</code>: Indirect Object.
 *
 * @author Amos Shi
 */
public final class Reference extends FileComponent implements GenerateTreeNode {

    static final byte SIGNATURE = 'R';

    public final byte Value;

    Reference(PosDataInputStream stream) throws IOException {
        super.startPos = stream.getPos();
        this.Value = stream.readByte();
        this.length = stream.getPos() - super.startPos;

        // System.out.println(this.toString());   // Deubg output
    }

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                super.startPos,
                super.length,
                "Refence to Indirect Object");
        DefaultMutableTreeNode nodeRef = new DefaultMutableTreeNode(nodeComp);

        parentNode.add(nodeRef);
    }

    @Override
    public String toString() {
        return String.format("Refence to Indirect Object: Start Position = %d, Length = %d, Raw Value = '%s'",
                super.startPos,
                super.length,
                (char) this.Value);
    }
}
