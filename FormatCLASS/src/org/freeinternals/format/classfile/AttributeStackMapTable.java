/*
 * AttributeStackMapTable.java    10:47 AM, April 28, 2014
 *
 * Copyright  2014, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile;

import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.format.FileFormatException;

/**
 * A variable-length attribute in the {@code attributes} table of a {@code Code}
 * attribute.
 *
 * @author Amos Shi
 * @see <a
 * href="http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.4">
 * VM Spec: The StackMapTable Attribute
 * </a>
 */
public class AttributeStackMapTable extends AttributeInfo {

    AttributeStackMapTable(final u2 nameIndex, final String type, final PosDataInputStream posDataInputStream, final AbstractCPInfo[] cp)
            throws java.io.IOException, FileFormatException {
        super(nameIndex, type, posDataInputStream);
        super.checkSize(posDataInputStream.getPos());
    }
}
