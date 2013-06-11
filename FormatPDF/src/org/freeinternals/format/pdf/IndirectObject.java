package org.freeinternals.format.pdf;

import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.ui.GenerateTreeNode;

/**
 * See
 * <pre>PDF 32000-1:2008</pre>
 * <code>7.3.10</code>: Indirect Object.
 *  
 * @author Amos Shi
 */
public class IndirectObject extends FileComponent implements GenerateTreeNode{

    static final String SIGNATURE_START = "obj";
    static final String SIGNATURE_END = "endobj";

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
    }
    
}
