/*
 * _JAsciiDataViewer.java    September 12, 2007, 2:15 PM
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.commonlib.ui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JTextArea;

/**
 *
 * @author Amos Shi
 * @since JDK 6.0
 */
class _JAsciiDataViewer extends JTextArea {

    private static final long serialVersionUID = 4876543219876500004L;
    protected static final int WIDTH_VALUE = 130;
    private int selectedStartIndex = 0;
    private int selectedLength = 0;

    _JAsciiDataViewer() {
        super();
        this.setFont(JBinaryViewer.font);
        this.setEditable(false);
    }

    protected void setData(final byte[] data) {
        this.setText(null);
        if (data == null) {
            return;
        }

        final int dataLength = data.length;
        int breakCounter = 0;
        for (int i = 0; i < dataLength; i++) {
            this.append(this.getAsciiStringValue(data[i]));
            breakCounter++;

            if (breakCounter > JBinaryViewer.ROW_ITEM_MAX_INDEX) {
                this.append("\n");
                breakCounter = 0;
            }
        }
    }

    private String getAsciiStringValue(final byte b) {
        String s = ".";

        if (((b > 32) && (b < 127)) ||
                ((b > 146) && (b < 165))) {
            s = new Character((char) b).toString();
        }

        return s;
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
            g.setColor(Color.ORANGE);
            g.drawRect(
                    JBinaryViewer.ITEM_WIDTH * column,
                    JBinaryViewer.ITEM_HEIGHT * row,
                    JBinaryViewer.ITEM_WIDTH,
                    JBinaryViewer.ITEM_HEIGHT);
        }
    }
}
