package org.freeinternals.format.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.ui.GenerateTreeNode;
import org.freeinternals.commonlib.ui.JTreeNodeFileComponent;
import org.freeinternals.format.FileFormatException;

/**
 * See
 * <pre>PDF 32000-1:2008</pre>
 * <code>7.5.4</code>: Cross-Reference Table.
 *
 * @author Amos Shi
 */
public class CrossReferenceTable extends FileComponent implements GenerateTreeNode {

    static final String SIGNATURE = "xref";
    public final List<Subsection> Subsections = new ArrayList<Subsection>(5);

    CrossReferenceTable(PosDataInputStream stream, String line) throws IOException, FileFormatException {
        super.startPos = stream.getPos() - line.length() - 1;
        this.parse(stream);

        // The Length
        super.length = line.length() + 1;
        for (Subsection subsection : Subsections) {
            super.length += subsection.getLength();
        }
    }

    private void parse(PosDataInputStream stream) throws IOException, FileFormatException {
        PDFStatics.Line line;
        do {
            line = PDFStatics.readLine(stream);
            if (Trailer.SIGNATURE.equalsIgnoreCase(line.Line)) {
                stream.backward(Trailer.SIGNATURE.length() + 1);
                break;
            }
            this.Subsections.add(new Subsection(stream, line.Line));
        } while (stream.hasNext());
    }

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                super.startPos,
                super.length,
                "Cross Reference Table");
        nodeComp.setDescription(Texts.getString(Texts.PDF_CROSS_REFERENCE_TABLE));
        DefaultMutableTreeNode nodeCRT = new DefaultMutableTreeNode(nodeComp);
        parentNode.add(nodeCRT);

        int pos = this.startPos;
        nodeCRT.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                CrossReferenceTable.SIGNATURE.length(),
                "Signature")));
        pos += CrossReferenceTable.SIGNATURE.length();
        nodeCRT.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                1,
                "New Line")));

        for (Subsection subsection : Subsections) {
            subsection.generateTreeNode(nodeCRT);
        }
    }

    public static class Subsection extends FileComponent implements GenerateTreeNode {

        /**
         * First object number.
         */
        public final int FirstObjectNumber;
        /**
         * Number of entries.
         */
        public final int NumberOfEntries;
        /**
         * The first line of the Subsection.
         */
        public final String HeaderLine;
        /**
         * Cross reference entries list.
         */
        public final List<Entry> Entries = new ArrayList<Entry>(20);

        Subsection(PosDataInputStream stream, String line) throws FileFormatException, IOException {
            super.startPos = stream.getPos() - line.length() - 1;
            this.HeaderLine = line;

            String[] subsection_header = line.split(" ");
            if (subsection_header.length < 2) {
                throw new FileFormatException(String.format(
                        "This is not a valid Cross Reerence Table Subsection header. Postion [%d], text [%s].",
                        stream.getPos(),
                        line));
            }

            this.FirstObjectNumber = Integer.valueOf(subsection_header[0]);
            this.NumberOfEntries = Integer.valueOf(subsection_header[1]);
            for (int i = 0; i < this.NumberOfEntries; i++) {
                this.Entries.add(new Entry(stream));
            }

            super.length = line.length() + 1 + this.NumberOfEntries * Entry.LENGTH;
        }

        public void generateTreeNode(DefaultMutableTreeNode parentNode) {
            int pos = this.startPos;
            DefaultMutableTreeNode nodeSS = new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    super.length,
                    String.format("Subsection: From %d Length %d", this.FirstObjectNumber, this.NumberOfEntries)));
            parentNode.add(nodeSS);

            nodeSS.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    this.HeaderLine.length(),
                    String.format("Header '%s'", this.HeaderLine))));
            pos += this.HeaderLine.length();
            nodeSS.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    1,
                    "LINE FEED (LF)")));

            for (Entry entry : Entries) {
                entry.generateTreeNode(nodeSS);
            }
        }
    }

    public static class Entry extends FileComponent implements GenerateTreeNode {

        public static final int LENGTH = 20;
        public static final int OFFSET_LENGTH = 10;
        public static final int GENERATIONNUMBER_LENGTH = 5;
        public static final char TYPE_IN_USE = 'n';
        public static final char TYPE_FREE = 'f';
        public final int Offset;
        public final int GenerationNumber;
        public final char Type;

        Entry(PosDataInputStream stream) throws IOException {
            super.startPos = stream.getPos();
            super.length = LENGTH;

            this.Offset = Integer.valueOf(stream.readASCII(OFFSET_LENGTH));
            stream.skip(1);
            this.GenerationNumber = Integer.valueOf(stream.readASCII(GENERATIONNUMBER_LENGTH));
            stream.skip(1);
            this.Type = (char) stream.readByte();
            stream.skip(2);
        }

        public void generateTreeNode(DefaultMutableTreeNode parentNode) {
            int pos = this.startPos;
            DefaultMutableTreeNode nodeEntry = new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    super.length,
                    String.format("Entry %010d", this.Offset)));
            parentNode.add(nodeEntry);

            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    OFFSET_LENGTH,
                    String.format("Offset: %d", this.Offset))));
            pos += OFFSET_LENGTH;
            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    1,
                    "Separator")));
            pos += 1;
            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    GENERATIONNUMBER_LENGTH,
                    String.format("Generation Number: %d", this.GenerationNumber))));
            pos += GENERATIONNUMBER_LENGTH;
            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    1,
                    "Separator")));
            pos += 1;
            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    1,
                    String.format("Type - " + Character.toString(this.Type)) )));
            pos += 1;
            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    2,
                    "End of Line")));
        }
    }
}
