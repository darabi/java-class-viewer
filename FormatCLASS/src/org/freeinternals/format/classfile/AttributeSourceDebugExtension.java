/*
 * AttributeSourceDebugExtension.java    11:00 AM, April 28, 2014
 *
 * Copyright  2014, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile;

import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.format.FileFormatException;

/**
 * The {@code SourceDebugExtension} attribute is an optional attribute in the
 * {@code attributes} table of a {@code ClassFile} structure. There can be no
 * more than one {@code SourceDebugExtension} attribute in the
 * {@code attributes} table of a given {@code ClassFile} structure.
 *
 * The {@code SourceDebugExtension} attribute has the following format:
 * <pre>
 * SourceDebugExtension_attribute {
 *   u2 attribute_name_index;
 *   u4 attribute_length;
 *   u1 debug_extension[attribute_length];
 * }
 * </pre>
 *
 * @author Amos Shi
 * @see <a
 * href="http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.9">
 * VM Spec: The Signature Attribute
 * </a>
 */
public class AttributeSourceDebugExtension extends AttributeInfo {

    AttributeSourceDebugExtension(final u2 nameIndex, final String type, final PosDataInputStream posDataInputStream, final AbstractCPInfo[] cp)
            throws java.io.IOException, FileFormatException {
        super(nameIndex, type, posDataInputStream);
        super.checkSize(posDataInputStream.getPos());
    }
}
