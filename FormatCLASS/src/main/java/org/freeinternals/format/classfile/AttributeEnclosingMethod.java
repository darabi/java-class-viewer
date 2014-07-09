/*
 * AttributeEnclosingMethod.java    10:48 AM, April 28, 2014
 *
 * Copyright  2004, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile;

import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.format.FileFormatException;

/**
 * An optional fixed-length attribute in the attributes table of a
 * {@code ClassFile} structure. A {@code class} must have an
 * {@code EnclosingMethod} attribute if and only if it is a local class or an
 * anonymous class. A A {@code class} may have no more than one
 * {@code EnclosingMethod} attribute.
 *
 * @author Amos Shi
 * @see <a
 * href="http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.7">
 * VM Spec: The EnclosingMethod Attribute
 * </a>
 */
public class AttributeEnclosingMethod extends AttributeInfo {

    AttributeEnclosingMethod(final u2 nameIndex, final String type, final PosDataInputStream posDataInputStream, final AbstractCPInfo[] cp)
            throws java.io.IOException, FileFormatException {
        super(nameIndex, type, posDataInputStream);
        super.checkSize(posDataInputStream.getPos());
    }
}
