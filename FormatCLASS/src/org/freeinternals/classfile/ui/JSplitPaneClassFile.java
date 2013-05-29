/*
 * JSplitPaneClassFile.java    19:58, April 06, 2009
 *
 * Copyright  2009, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.classfile.ui;

import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.classfile.core.ClassFile;
import org.freeinternals.classfile.core.ClassFormatException;
import org.freeinternals.classfile.core.Opcode;
import org.freeinternals.commonlib.ui.JBinaryViewer;
import org.freeinternals.commonlib.ui.JPanelForTree;

/**
 * A split panel created from a class file byte array.
 *
 * @author Amos Shi
 * @since JDK 6.0
 * @see ClassFile
 */
public class JSplitPaneClassFile extends JSplitPane {

    private static final long serialVersionUID = 4876543219876500000L;
    private ClassFile classFile;

    private JBinaryViewer binaryViewer = null;
    private JScrollPane binaryViewerView = null;
    private JTextArea opcode = null;

    /**
     * Creates a split panel from a Java class file byte array.
     *
     * @param byteArray Java class file byte array
     */
    public JSplitPaneClassFile(final byte[] byteArray, JFrame top) {
        try {
            this.classFile = new ClassFile(byteArray.clone());
        } catch (IOException ex) {
            Logger.getLogger(JSplitPaneClassFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassFormatException ex) {
            Logger.getLogger(JSplitPaneClassFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.createAndShowGUI(top);
    }

    private void createAndShowGUI(JFrame top) {

        // Construct class file viewer
        final JTreeClassFile jTreeClassFile = new JTreeClassFile(this.classFile);
        jTreeClassFile.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(final javax.swing.event.TreeSelectionEvent evt) {
                jTreeClassFileSelectionChanged(evt);
            }
        });
        final JPanelForTree panel = new JPanelForTree(jTreeClassFile, top);

        final JTabbedPane tabbedPane = new JTabbedPane();

        // Construct binary viewer
        this.binaryViewer = new JBinaryViewer();
        this.binaryViewer.setData(this.classFile.getClassByteArray());
        this.binaryViewerView = new JScrollPane(this.binaryViewer);
        this.binaryViewerView.getVerticalScrollBar().setValue(0);
        tabbedPane.add("Class File", this.binaryViewerView);

        // Construct opcode viewer
        this.opcode = new JTextArea();
        this.opcode.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 14));
        this.opcode.setEditable(false);
        tabbedPane.add("Opcode", new JScrollPane(this.opcode));

        this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.setDividerSize(5);
        this.setDividerLocation(280);
        this.setLeftComponent(panel);
        this.setRightComponent(tabbedPane);

        this.binaryViewerView.getVerticalScrollBar().setValue(0);
    }

    private void jTreeClassFileSelectionChanged(final TreeSelectionEvent evt) {
        Object obj = evt.getPath().getLastPathComponent();
        if (obj instanceof DefaultMutableTreeNode) {
            final DefaultMutableTreeNode objDmtn = (DefaultMutableTreeNode) obj;
            obj = objDmtn.getUserObject();
            if (obj instanceof JTreeNodeClassComponent) {
                final JTreeNodeClassComponent objTncc = (JTreeNodeClassComponent) obj;

                // Select the bytes of this data
                this.binaryViewer.setSelection(objTncc.getStartPos(), objTncc.getLength());

                // clear opcode values;
                this.opcode.setText(null);
                // Get the code bytes
                if (objTncc.getText().equals("code")) {
                    //System.out.println("code");
                    final byte[] data = this.classFile.getClassByteArray(objTncc.getStartPos(), objTncc.getLength());

                    this.opcode.append(Tool.getByteDataHexView(data));
                    this.opcode.append("\n");
                    this.opcode.append(Opcode.parseCode(data, this.classFile));
                }
            }
        }

        // Print out current scrool bar position.
        //System.out.println("Max = " + this.binaryViewerView.getVerticalScrollBar().getMaximum() + ", current = " + this.binaryViewerView.getVerticalScrollBar().getValue());
    }
}
