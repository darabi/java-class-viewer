package org.freeinternals.format.pdf;

import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.core.PosDataInputStream.ASCIILine;
import org.freeinternals.commonlib.ui.GenerateTreeNode;
import org.freeinternals.commonlib.ui.JTreeNodeFileComponent;
import org.freeinternals.format.FileFormatException;

/**
 * See
 * <pre>PDF 32000-1:2008</pre>
 * <code>7.3.10</code>: Indirect Object.
 *
 * @author Amos Shi
 */
public class IndirectObject extends FileComponent implements GenerateTreeNode {

    /**
     * Start definition regular expression. <p> Example: </p>
     * <pre>
     *  5534 0 obj
     *  5491 0 obj SOME TEXT OF OBJECT
     *  5491 0 objSOME TEXT OF OBJECT
     * </pre>
     *
     */
    public static final String SIGNATURE_START_REGEXP = "\\d+.*\\d+.*obj.*";
    /**
     * Key word in the first line of current object.
     */
    public static final String SIGNATURE_START = "obj";
    /**
     * End line of current object.
     */
    public static final String SIGNATURE_END = "endobj";
    /**
     * A positive integer object number. <p> Indirect objects may be numbered
     * sequentially within a PDF file, but this is not required; object numbers
     * may be assigned in any arbitrary order. </p>
     */
    public final int ObjectNumber;
    /**
     * A non-negative integer generation number. <p> In a newly created file,
     * all indirect objects shall have generation numbers of 0. Nonzero
     * generation numbers may be introduced when the file is later updated. </p>
     */
    public final int GenerationNumber;
    /**
     * The signature length.
     */
    public final int SignatureLen;
    /**
     * The last line of the indirect object.
     */
    public ASCIILine EndLine;

    IndirectObject(PosDataInputStream stream, ASCIILine line) throws IOException {
        stream.backward(line.Length());
        super.startPos = stream.getPos();
        this.ObjectNumber = Integer.valueOf(stream.readASCIIUntil(PDFStatics.WhiteSpace.SP));
        this.GenerationNumber = Integer.valueOf(stream.readASCIIUntil(PDFStatics.WhiteSpace.SP));
        stream.skip(SIGNATURE_START.length());
        this.SignatureLen = stream.getPos() - super.startPos;

        // parse
        this.parse(stream);
    }

    private void parse(PosDataInputStream stream) throws IOException {
        ASCIILine line;
        do {
            line = stream.readASCIILine();
            if (line.Line.length() == SIGNATURE_END.length() && SIGNATURE_END.equalsIgnoreCase(line.Line)) {
                super.length = stream.getPos() - super.startPos;
                this.EndLine = line;
                break;
            }
        } while (stream.getPos() < (stream.getBuf().length - 1));
    }

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                super.startPos,
                super.length,
                String.format("Indirect Object: %d %d", this.ObjectNumber, this.GenerationNumber));
        nodeComp.setDescription(Texts.getString(Texts.PDF_INDIRECT_OBJECT));
        DefaultMutableTreeNode nodeIO = new DefaultMutableTreeNode(nodeComp);

        int pos = super.startPos;
        nodeIO.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                this.SignatureLen,
                String.format("Object Number = %d, Generation Number = %d", this.ObjectNumber, this.GenerationNumber))));
        pos += this.SignatureLen;
        int contLen = super.length - this.SignatureLen - this.EndLine.Length();
        nodeIO.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                contLen,
                "Indirect Object Content")));

        pos += contLen;
        nodeIO.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                SIGNATURE_END.length(),
                "End")));
        pos += SIGNATURE_END.length();
        nodeIO.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                this.EndLine.NewLineLength,
                "New Line")));

        parentNode.add(nodeIO);
    }
}
