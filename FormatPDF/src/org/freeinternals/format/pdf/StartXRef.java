package org.freeinternals.format.pdf;

import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.ui.GenerateTreeNode;

/**
 * See
 * <pre>PDF 32000-1:2008</pre>
 * <code>7.5.5</code>: File Trailer.
 *  
 * @author Amos Shi
 */
public class StartXRef extends FileComponent implements GenerateTreeNode{

    static final String SIGNATURE = "startxref";

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
    }
    
}
