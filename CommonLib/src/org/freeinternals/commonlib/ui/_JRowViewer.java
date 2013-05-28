/*
 * _JRowViewer.java    September 12, 2007, 2:28 PM
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.commonlib.ui;

import java.awt.Component;
import javax.swing.JTextArea;

/**
 *
 * @author Amos Shi
 * @since JDK 6.0
 */
class _JRowViewer extends JTextArea {

    private static final long serialVersionUID = 4876543219876500000L;
    protected static final int WIDTH_VALUE = 75;

    _JRowViewer() {
        super();
        this.setFont(JBinaryViewer.font);

        this.setAlignmentX(Component.LEFT_ALIGNMENT);
        //this.setBorder(BorderFactory.createCompoundBorder(
        //           BorderFactory.createLineBorder(Color.red),
        //           this.getBorder()));

        this.setEditable(false);
    }

    protected void setData(final int rowStart, final int rowCount, final int rowMax) {

        // Update contents
        this.setText(null);
        if (rowCount <= 0) {
            return;
        }
        if (rowStart >= rowMax) {
            return;
        }

        int rowValue = rowStart * JBinaryViewer.ROW_ITEM_MAX;
        for (int i = 0; i < rowCount; i++) {
            if ((rowStart + i) < rowMax) {
                this.append(String.format("%08Xh\n", rowValue));
                rowValue += JBinaryViewer.ROW_ITEM_MAX;
            }
        }
    }
}
