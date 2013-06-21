package org.freeinternals.format.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.core.PosDataInputStream.ASCIILine;
import org.freeinternals.commonlib.ui.GenerateTreeNode;
import org.freeinternals.commonlib.ui.JTreeNodeFileComponent;
import org.freeinternals.commonlib.util.DefaultFileComponent;
import org.freeinternals.format.FileFormatException;
import org.freeinternals.format.pdf.basicobj.Stream;

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
     * Length of {@link #ObjectNumber} and {@link #GenerationNumber}.
     */
    public final int NumberLen;
    /**
     * The last line of the indirect object.
     */
    public final ASCIILine SignatureStart;
    /**
     * The last line of the indirect object.
     */
    public ASCIILine SignatureEnd;
    /**
     * Component of current object.
     */
    private List<FileComponent> components = Collections.synchronizedList(new ArrayList<FileComponent>(100));

    IndirectObject(PosDataInputStream stream, ASCIILine line) throws IOException, FileFormatException {
        stream.backward(line.Length());
        super.startPos = stream.getPos();
        this.ObjectNumber = Integer.valueOf(stream.readASCIIUntil(PDFStatics.WhiteSpace.SP));
        this.GenerationNumber = Integer.valueOf(stream.readASCIIUntil(PDFStatics.WhiteSpace.SP));
        this.NumberLen = stream.getPos() - super.startPos;
        stream.skip(SIGNATURE_START.length());
        byte b1 = stream.readByte();
        byte b2 = stream.readByte();
        if (b1 == PDFStatics.WhiteSpace.CR && b2 == PDFStatics.WhiteSpace.LF) {
            this.SignatureStart = new ASCIILine(SIGNATURE_START, 2);
        } else if (b1 == PDFStatics.WhiteSpace.CR || b1 == PDFStatics.WhiteSpace.LF) {
            this.SignatureStart = new ASCIILine(SIGNATURE_START, 1);
            stream.backward(1);
        } else {
            this.SignatureStart = new ASCIILine(SIGNATURE_START, 0);
            stream.backward(2);
        }

        // parse
        this.parseStartEnd(stream);

        // Furthur parse
        this.parseStreamObject(stream);
    }

    private void parseStartEnd(PosDataInputStream stream) throws IOException {
        ASCIILine line;
        do {
            line = stream.readASCIILine();
            if (line.Line.length() == SIGNATURE_END.length() && SIGNATURE_END.equalsIgnoreCase(line.Line)) {
                super.length = stream.getPos() - super.startPos;
                this.SignatureEnd = line;
                break;
            }
        } while (stream.getPos() < (stream.getBuf().length - 1));
    }

    private void parseStreamObject(PosDataInputStream root) throws IOException, FileFormatException {
        PosDataInputStream stream = root.getPartialStream(
                super.startPos + this.NumberLen + this.SignatureStart.Length(),
                super.length - this.NumberLen - this.SignatureStart.Length() - this.SignatureEnd.Length());

        // Filter Stream Object First
        ASCIILine line;
        while (stream.hasNext()) {
            line = stream.readASCIILine();
            if (line.Line.endsWith(Stream.SIGNATURE_START)) {
                this.components.add(new Stream(stream, line));
            }
        }

        // Analysis Object Before Stream Object
        int lastIndex = super.startPos + super.length - this.SignatureEnd.Length();
        if (this.components.size() > 0) {
            for (FileComponent comp : this.components) {
                if (comp instanceof Stream) {
                    lastIndex = (lastIndex > comp.getStartPos()) ? comp.getStartPos() : lastIndex;
                }
            }
        }

        // Add the rest content
        int pos = super.startPos + this.NumberLen + this.SignatureStart.Length();
        int len = lastIndex - pos;
        this.components.add(0, new DefaultFileComponent(
                pos, len, "Content"));
        stream = root.getPartialStream(pos, len);
        while (stream.hasNext()) {
            byte next1 = stream.readByte();
            switch (next1){
                case PDFStatics.DelimiterCharacter.LP:   //  '(' - Leteral String
                    break;
                case PDFStatics.DelimiterCharacter.LT:   //  '<' - Hexadecimal String; << Dictionary
                    break;
                case PDFStatics.DelimiterCharacter.SO:   //  '/' - Name
                    break;
                case PDFStatics.DelimiterCharacter.LS:   //  '[' - Array
                    break;
                default:
                    // boolean, Numeric, null; Comment
                    break;
                    
            }
        }
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
                this.NumberLen,
                String.format("Object Number = %d, Generation Number = %d", this.ObjectNumber, this.GenerationNumber))));
        pos += this.NumberLen;

        nodeIO.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                this.SignatureStart.Line.length(),
                Texts.Signature + this.SignatureStart.Line)));
        pos += this.SignatureStart.Line.length();

        if (this.SignatureStart.NewLineLength > 0) {
            nodeIO.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    this.SignatureStart.NewLineLength,
                    Texts.NewLine)));
            pos += this.SignatureStart.NewLineLength;
        }

        int contLen = super.length - this.NumberLen - this.SignatureStart.Length() - this.SignatureEnd.Length();
        DefaultMutableTreeNode nodeContent;
        nodeIO.add(nodeContent = new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                contLen,
                "Indirect Object Content")));

        for (FileComponent comp : this.components) {
            if (comp instanceof GenerateTreeNode) {
                ((GenerateTreeNode) comp).generateTreeNode(nodeContent);
            }
        }

        pos += contLen;
        nodeIO.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                SIGNATURE_END.length(),
                Texts.Signature + SIGNATURE_END)));
        pos += SIGNATURE_END.length();
        nodeIO.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                this.SignatureEnd.NewLineLength,
                Texts.NewLine)));

        parentNode.add(nodeIO);
    }
}
