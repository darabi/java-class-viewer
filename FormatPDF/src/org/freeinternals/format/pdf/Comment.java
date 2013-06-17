package org.freeinternals.format.pdf;

import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.ui.GenerateTreeNode;
import org.freeinternals.commonlib.ui.JTreeNodeFileComponent;

/**
 * See
 * <pre>PDF 32000-1:2008</pre>
 * <code>7.2.3</code>: Comments.
 *  
 * @author Amos Shi
 */
public class Comment extends FileComponent implements GenerateTreeNode{

    /**
     * Comment text.
     */
    public final String Text;
    
    Comment(PosDataInputStream stream, String line) throws IOException {
        super.length = line.length() + 1;
        super.startPos = stream.getPos() - super.length;
        this.Text = line;
    }
    
    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                this.getStartPos(),
                super.length,
                "Comment");
        DefaultMutableTreeNode nodeComment = new DefaultMutableTreeNode(nodeComp);
        nodeComment.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                this.startPos,
                this.Text.length(),
                "PDF Signature")));
        nodeComment.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                this.startPos + this.Text.length(),
                1,
                "New Line")));
        parentNode.add(nodeComment);
    }
    
}
