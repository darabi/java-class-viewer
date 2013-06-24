package org.freeinternals.format.pdf.basicobj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.ui.GenerateTreeNode;
import org.freeinternals.commonlib.ui.JTreeNodeFileComponent;
import org.freeinternals.format.pdf.PDFStatics;
import org.freeinternals.format.pdf.Texts;

/**
 * PDF basic object Dictionary, see
 * <pre>PDF 32000-1:2008</pre>
 * <code>7.3.7</code>: Dictionary Object.
 *
 * @author Amos Shi
 */
public class Dictionary extends FileComponent implements GenerateTreeNode {

    /**
     * Signature indicates the start of current object.
     */
    public static final String SIGNATURE_START = "<<";
    /**
     * Signature indicates the end of current object.
     */
    public static final String SIGNATURE_END = ">>";
    /**
     * Component of current object.
     */
    private List<FileComponent> components = Collections.synchronizedList(new ArrayList<FileComponent>(31));

    Dictionary(PosDataInputStream stream) throws IOException {
        // System.out.println("==== PDF Dictionary");   // Deubg output
        super.startPos = stream.getPos();
        this.parse(stream);
        this.organizeDictionary();
        super.length = stream.getPos() - super.startPos;
    }

    private void parse(PosDataInputStream stream) throws IOException {
        // The '<<' sign
        stream.skip(SIGNATURE_START.length());

        FileComponent comp;
        Analysis analysis = new Analysis();

        byte next1;
        byte next2;
        boolean stop = false;
        while (stream.hasNext()) {
            comp = analysis.ParseNextObject(stream, this.components);
            if (comp == null) {
                next1 = stream.readByte();
                switch (next1) {
                    // Dictionary Ends
                    case PDFStatics.DelimiterCharacter.GT:
                        next2 = stream.readByte();
                        if (next2 == PDFStatics.DelimiterCharacter.GT) {
                            // Stop current Dictionary Object
                            stop = true;
                            // System.out.println("==== PDF Dictionary -- Ends at " + stream.getPos());   // Deubg output
                        }
                        break;
                    default:
                        System.out.println(String.format("ERROR ======== Dictionary.parse() - Location = %d (%X), Value = %s",
                                stream.getPos(), stream.getPos(), String.valueOf((char) next1)));
                        break;
                } // switch
                if (stop == true) {
                    break;   // Quit current while-loop
                }
            }
        } // End While
    }

    private void organizeDictionary() {
    }

    public void generateTreeNode(DefaultMutableTreeNode parentNode) {
        JTreeNodeFileComponent nodeComp = new JTreeNodeFileComponent(
                super.startPos,
                super.length,
                "Dictionary Object");
        DefaultMutableTreeNode nodeDic = new DefaultMutableTreeNode(nodeComp);

        int pos = super.startPos;
        nodeDic.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                SIGNATURE_START.length(),
                Texts.Signature + SIGNATURE_START)));
        pos += SIGNATURE_START.length();

        int contLen = super.length - SIGNATURE_START.length() - SIGNATURE_END.length();
        DefaultMutableTreeNode nodeContent;
        nodeDic.add(nodeContent = new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                contLen,
                "Dictionary Content")));

        for (FileComponent comp : this.components) {
            if (comp instanceof GenerateTreeNode) {
                ((GenerateTreeNode) comp).generateTreeNode(nodeContent);
            }
        }

        pos += contLen;
        nodeDic.add(new DefaultMutableTreeNode(new JTreeNodeFileComponent(
                pos,
                SIGNATURE_END.length(),
                Texts.Signature + SIGNATURE_END)));

        parentNode.add(nodeDic);
    }
}
