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
    public final List<CrossReferenceSubsection> Subsections = new ArrayList<CrossReferenceSubsection>(5);

    CrossReferenceTable(PosDataInputStream stream, String line) throws IOException, FileFormatException {
        super.startPos = stream.getPos() - line.length() - 1;
        this.parse(stream);

        // The Length
        super.length = line.length();
        for (CrossReferenceSubsection subsection : Subsections) {
            super.length += subsection.getLength();
        }
    }

    private void parse(PosDataInputStream stream) throws IOException, FileFormatException {
        String line;
        do {
            line = stream.readASCIIUntil(PDFStatics.WhiteSpace.LF);
            if (Trailer.SIGNATURE.equalsIgnoreCase(line)) {
                stream.backward(Trailer.SIGNATURE.length());
                break;
            }
            this.Subsections.add(new CrossReferenceSubsection(stream, line));
        } while (stream.hasNext());
    }

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                super.startPos,
                super.length,
                "Cross Reference Table");
        nodeComp.setDescription(Descriptions.getString(Descriptions.PDF_CROSS_REFERENCE_TABLE));
        DefaultMutableTreeNode nodeCRT = new DefaultMutableTreeNode(nodeComp);

        int pos = this.startPos;
        nodeCRT.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                CrossReferenceTable.SIGNATURE.length(),
                "Signature")));
        pos += CrossReferenceTable.SIGNATURE.length();
        nodeCRT.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                1,
                "LINE FEED (LF)")));

        for (CrossReferenceSubsection subsection : Subsections) {
            subsection.generateTreeNode(nodeCRT);
        }
    }

    public static class CrossReferenceSubsection extends FileComponent implements GenerateTreeNode {

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
        public final List<CrossReferenceEntry> Entries = new ArrayList<CrossReferenceEntry>(20);

        CrossReferenceSubsection(PosDataInputStream stream, String line) throws FileFormatException, IOException {
            super.startPos = stream.getPos();
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
                this.Entries.add(new CrossReferenceEntry(stream));
            }

            super.length = line.length() + this.NumberOfEntries * CrossReferenceEntry.LENGTH;
        }

        public void generateTreeNode(DefaultMutableTreeNode parentNode) {
            int pos = this.startPos;
            DefaultMutableTreeNode nodeSS = new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    super.length,
                    String.format("Subsection: From %d Length %d", this.FirstObjectNumber, this.NumberOfEntries)));

            nodeSS.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    this.HeaderLine.length(),
                    "Header: " + this.HeaderLine)));
            pos += this.HeaderLine.length();
            nodeSS.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    1,
                    "LINE FEED (LF)")));

            for (CrossReferenceEntry entry : Entries) {
                entry.generateTreeNode(nodeSS);
            }
        }
    }

    public static class CrossReferenceEntry extends FileComponent implements GenerateTreeNode {

        public static final int LENGTH = 20;
        public static final int OFFSET_LENGTH = 10;
        public static final int GENERATIONNUMBER_LENGTH = 5;
        public static final char TYPE_IN_USE = 'n';
        public static final char TYPE_FREE = 'f';
        public final int Offset;
        public final int GenerationNumber;
        public final char Type;

        CrossReferenceEntry(PosDataInputStream stream) throws IOException {
            super.startPos = stream.getPos();
            super.length = LENGTH;

            this.Offset = Integer.valueOf(stream.readASCII(OFFSET_LENGTH));
            stream.skip(1);
            this.GenerationNumber = Integer.valueOf(stream.readASCII(GENERATIONNUMBER_LENGTH));
            stream.skip(1);
            this.Type = stream.readChar();
            stream.skip(2);
        }

        public void generateTreeNode(DefaultMutableTreeNode parentNode) {
            int pos = this.startPos;
            DefaultMutableTreeNode nodeEntry = new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    super.length,
                    String.format("Entry %d", this.Offset)));

            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    OFFSET_LENGTH,
                    String.format("Offset: %d" + this.Offset))));
            pos += OFFSET_LENGTH;
            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    1,
                    "Separator")));
            pos += 1;
            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    GENERATIONNUMBER_LENGTH,
                    String.format("Generation Number: %d" + this.GenerationNumber))));
            pos += GENERATIONNUMBER_LENGTH;
            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    1,
                    "Separator")));
            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    1,
                    String.format("Type - %s", String.valueOf(this.Type)))));
            pos += 1;
            nodeEntry.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                    pos,
                    2,
                    "End of Line")));
        }
    }
}
