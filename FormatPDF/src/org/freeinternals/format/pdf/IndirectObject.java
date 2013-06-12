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
 * <code>7.3.10</code>: Indirect Object.
 *
 * @author Amos Shi
 */
public class IndirectObject extends FileComponent implements GenerateTreeNode {

    static final String SIGNATURE_START = "obj";
    static final String SIGNATURE_END = "endobj";
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
     * The first line length of the indirect object.
     */
    public final int HeaderLineLength;

    IndirectObject(PosDataInputStream stream, String line) throws IOException, FileFormatException {
        super.startPos = stream.getPos() - line.length() - 1;
        String[] obj_header = line.split(" ");
        if (obj_header.length != 3) {
            throw new FileFormatException(String.format(
                    "This is not a valid indirect object header. Start postion [%d], text [%s].",
                    super.startPos,
                    line));
        }
        this.ObjectNumber = Integer.valueOf(obj_header[0]);
        this.GenerationNumber = Integer.valueOf(obj_header[1]);
        this.HeaderLineLength = line.length();
        this.parse(stream);
    }

    private void parse(PosDataInputStream stream) throws IOException {
        String line;
        do {
            line = stream.readASCIIUntil(PDFStatics.WhiteSpace.LF);
            if (line != null && line.length() == SIGNATURE_END.length() && SIGNATURE_END.equalsIgnoreCase(line)) {
                super.length = stream.getPos() - super.startPos;
                break;
            }
        } while (stream.getPos() < (stream.getBuf().length - 1));
    }

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                this.getStartPos(),
                super.length,
                String.format("Indirect Object: %d %d", this.ObjectNumber, this.GenerationNumber));
        nodeComp.setDescription(Descriptions.getString(Descriptions.PDF_INDIRECT_OBJECT));
        DefaultMutableTreeNode nodeIO = new DefaultMutableTreeNode(nodeComp);

        int pos = this.startPos;
        nodeIO.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                this.HeaderLineLength,
                String.format("Object Number = %d, Generation Number = %d", this.ObjectNumber, this.GenerationNumber))));
        pos += this.HeaderLineLength;
        nodeIO.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                1,
                "LINE FEED (LF)")));

        pos += 1;
        int contLen = super.startPos + super.length - pos - (SIGNATURE_END.length() + 1);
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
                1,
                "LINE FEED (LF)")));

        parentNode.add(nodeIO);
    }
}
