/*
 * JRawDataViewer.java    September 12, 2007, 2:12 PM
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.commonlib.ui.binviewer;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JTextArea;
import org.freeinternals.commonlib.ui.JBinaryViewer;

/**
 *
 * @author Amos Shi
 * @since JDK 6.0
 */
public class JRawDataViewer extends JTextArea {

    private static final long serialVersionUID = 4876543219876500000L;
    public static final int WIDTH_VALUE = 392;
    private int selectedStartIndex = 0;
    private int selectedLength = 0;

    public JRawDataViewer() {
        super();
        this.setFont(JBinaryViewer.font);
        this.setEditable(false);
    }

    public void setData(final byte[] data) {
        this.setText(null);
        if (data == null) {
            return;
        }

        final int dataLength = data.length;
        int breakCounter = 0;
        for (int i = 0; i < dataLength; i++) {
            this.append(String.format(" %02X", data[i]));
            breakCounter++;

            if (breakCounter > JBinaryViewer.ROW_ITEM_MAX_INDEX) {
                this.append(" \n");
                breakCounter = 0;
            }
        }
    }

    public void setSelection(final int startIndex, final int length) {
        this.selectedStartIndex = startIndex;
        this.selectedLength = length;

        this.repaint();
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        // Paint Selectd Items
        if ((this.selectedLength < 1) || (this.selectedStartIndex < 0)) {
            return;
        }

        final int endIndex = this.selectedStartIndex + this.selectedLength;
        int row;
        int column;
        for (int i = this.selectedStartIndex; i < endIndex; i++) {
            // calculate row and length
            column = i;
            row = 0;
            while (column >= JBinaryViewer.ROW_ITEM_MAX) {
                column -= JBinaryViewer.ROW_ITEM_MAX;
                row = row + 1;
            }

            this.paintDataRect(g, row, column);
        }
    }

    private void paintDataRect(final Graphics g, final int row, final int column) {
        if ((row > -1) && (column > -1)) {
            g.setColor(Color.BLUE);
            g.drawRect(
                    JBinaryViewer.ITEM_WIDTH_HALF + JBinaryViewer.DATA_ITEM_WIDTH * column,
                    JBinaryViewer.ITEM_HEIGHT * row,
                    JBinaryViewer.DATA_ITEM_WIDTH,
                    JBinaryViewer.ITEM_HEIGHT);
        }
    }
}
