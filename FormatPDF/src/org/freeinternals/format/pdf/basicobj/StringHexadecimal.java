package org.freeinternals.format.pdf.basicobj;

import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.ui.GenerateTreeNode;
import org.freeinternals.commonlib.ui.JTreeNodeFileComponent;
import org.freeinternals.format.pdf.PDFStatics;
import org.freeinternals.format.pdf.Texts;

/**
 * PDF basic object Hexadecimal String, see
 * <pre>PDF 32000-1:2008</pre>
 * <code>7.3.4.3</code>: Hexadecimal Strings.
 *
 * @author Amos Shi
 */
public final class StringHexadecimal extends FileComponent implements GenerateTreeNode {

    /**
     * String text in
     * <code>Raw</code> format.
     */
    private final String RawText;

    StringHexadecimal(PosDataInputStream stream) throws IOException {
        super.startPos = stream.getPos();
        stream.skip(1);
        this.RawText = stream.readASCIIUntil(PDFStatics.DelimiterCharacter.GT);
        super.length = stream.getPos() - super.startPos;

        // System.out.println(this.toString());   // Deubg output
    }

    public String getStringValue() {
        // TODO - Implement the method
        return this.RawText;
    }

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                super.startPos,
                super.length,
                "Hexadecimal String");
        nodeComp.setDescription("Raw String Text: " + this.RawText);
        DefaultMutableTreeNode nodeStr = new DefaultMutableTreeNode(nodeComp);

        int pos = super.startPos;
        nodeStr.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                1,
                Texts.Signature + PDFStatics.DelimiterCharacter.LT)));
        pos += 1;

        nodeStr.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                super.length - 2,
                "Raw Data")));
        pos += (super.length - 2);

        nodeStr.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                1,
                Texts.Signature + PDFStatics.DelimiterCharacter.GT)));

        parentNode.add(nodeStr);
    }

    @Override
    public String toString() {
        return String.format("Hexadecimal String Object: Start Position = %d, Length = %d, Raw Text = '%s'",
                super.startPos,
                super.length,
                this.RawText);
    }
}
