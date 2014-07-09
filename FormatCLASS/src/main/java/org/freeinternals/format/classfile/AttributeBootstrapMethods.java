/*
 * AttributeBootstrapMethods.java    11:41 AM, April 28, 2014
 *
 * Copyright  2014, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile;

import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.format.FileFormatException;

/**
 * The {@code BootstrapMethods} attribute is a variable-length attribute in the
 * {@code attributes} table of a {@code ClassFile} structure. The {@code BootstrapMethods}
 * attribute records bootstrap method specifiers referenced by {@code invokedynamic}
 * instructions.
 *
 * @author Amos Shi
 * @see <a
 * href="http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.21">
 * VM Spec: The BootstrapMethods attribute
 * </a>
 */
public class AttributeBootstrapMethods extends AttributeInfo {

    AttributeBootstrapMethods(final u2 nameIndex, final String type, final PosDataInputStream posDataInputStream, final AbstractCPInfo[] cp)
            throws java.io.IOException, FileFormatException {
        super(nameIndex, type, posDataInputStream);
        super.checkSize(posDataInputStream.getPos());
    }
}
