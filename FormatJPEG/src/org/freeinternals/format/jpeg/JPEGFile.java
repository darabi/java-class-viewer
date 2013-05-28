/*
 * JPEGFile.java    August 21, 2010, 21:30
 *
 * Copyright 2010, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.jpeg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import javax.swing.tree.DefaultMutableTreeNode;
import org.freeinternals.biv.plugin.FileFormat;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosByteArrayInputStream;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.ui.GenerateTreeNode;
import org.freeinternals.format.FileFormatException;

/**
 *
 * @author Amos Shi
 */
public class JPEGFile extends FileFormat{

    private List<FileComponent> components = Collections.synchronizedList(new ArrayList<FileComponent>(100));

    public JPEGFile(File file) throws IOException, FileFormatException {
        super(file);
        this.parse();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Get raw data
    /**
     * Get the byte array of current file.
     *
     * @return Byte array of the file
     */
    public byte[] getFileByteArray() {
        return this.fileByteArray;
    }

    /**
     * Get part of the file byte array.
     * The array begins at the specified {@code startIndex} and extends to
     * the byte at {@code startIndex}+{@code length}.
     *
     * @param startIndex The start index
     * @param length The length of the array
     * @return Part of the class byte array
     */
    public byte[] getFileByteArray(final int startIndex, final int length) {
        if ((startIndex < 0) || (length < 1)) {
            throw new IllegalArgumentException("startIndex or length is not valid. startIndex = " + startIndex + ", length = " + length);
        }
        if (startIndex + length - 1 > this.fileByteArray.length) {
            throw new ArrayIndexOutOfBoundsException("The last item index is bigger than class byte array size.");
        }

        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = this.fileByteArray[startIndex + i];
        }

        return data;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Get each components

    public List<FileComponent> getFileComponents() {
        return Collections.unmodifiableList(this.components);
    }

    private void parse() throws IOException, FileFormatException {
        Marker marker;
        PosDataInputStream posDataInputStream = new PosDataInputStream(new PosByteArrayInputStream(this.fileByteArray));

        // Marker - SOI
        final int soi = posDataInputStream.readUnsignedShort();
        if (MarkerCode.SOI != soi) {
            throw new FileFormatException(String.format(
                    "File is not started with JPEG SOI. Expected value %x, current value %x.",
                    MarkerCode.SOI,
                    soi));
        }
        this.components.add(new Marker_SOI(posDataInputStream, soi));

        // Markers & File Data
        boolean isCompressedData = false;
        boolean isNextMarkerFound;
        int i;
        int length;
        while (posDataInputStream.getPos() < (this.fileByteArray.length - 1)) {
            if (MarkerCode.isValid(this.getMarkerCode(posDataInputStream.getPos()))) {
                marker = MarkerParse.parse(posDataInputStream);
                this.components.add(marker);
                isCompressedData = MarkerCode.isCompressedDataFollowed(marker.marker_code);
            } else {
                // find next marker
                isNextMarkerFound = false;
                for (i = posDataInputStream.getPos() + 1; i < this.fileByteArray.length - 1; i++) {
                    if (MarkerCode.isValid(this.getMarkerCode(i))) {
                        isNextMarkerFound = true;
                        break;
                    }
                }
                // Determine the length
                if (isNextMarkerFound) {
                    length = i - posDataInputStream.getPos();
                } else {
                    length = this.fileByteArray.length - posDataInputStream.getPos();
                }
                // Add one component
                this.components.add(new FileData(posDataInputStream, length, isCompressedData));
            }
        }

        // Parse each Marker
        final ListIterator<FileComponent> iteratorMarker = this.components.listIterator();
        FileComponent comp;
        byte[] markerByteArray;
        int markerByteArraySize;

        while (iteratorMarker.hasNext()) {
            comp = iteratorMarker.next();
            if (comp instanceof Marker) {
                marker = (Marker) comp;
                if (marker.getMarkerLength() > 0) {
                    markerByteArraySize = marker.getLength() + MarkerCode.MARKER_CODE_BYTES_COUNT;
                    markerByteArray = new byte[markerByteArraySize];
                    System.arraycopy(this.fileByteArray, marker.getStartPos(), markerByteArray, 0, markerByteArraySize);
                    try {
                        marker.parse(new PosDataInputStream(new PosByteArrayInputStream(markerByteArray), marker.getStartPos()));
                    } catch (Exception ex) {
                        System.out.println("JPEGFile.parse() - " + marker.getMarkerName() + " - " + ex.toString());
                    }
                }
            }
        }
    } // End method parse

    private int getMarkerCode(int markerOffset) {
        byte[] data = this.getFileByteArray(markerOffset, 2);
        return ((data[0] & 0x000000FF) << 8) + (data[1] & 0x000000FF);
    }

    @Override
    public String getContentTabName() {
        return "JPEG File";
    }

    @Override
    public void generateTreeNode(DefaultMutableTreeNode root) {
        final List<FileComponent> markers = this.getFileComponents();
        final ListIterator<FileComponent> iterator = markers.listIterator();

        while (iterator.hasNext()) {
            FileComponent comp = iterator.next();
            if (comp instanceof GenerateTreeNode) {
                GenerateTreeNode generator = (GenerateTreeNode) comp;
                generator.generateTreeNode(root);
            }
        } // End while
    }
}
