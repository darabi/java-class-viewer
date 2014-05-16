/*
 * AttributesCount.java    17:32 PM, May 15, 2014
 *
 * Copyright  2014, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile;

import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.format.FileFormatException;

/**
 * The element_value structure is a discriminated union representing the value
 * of an element-value pair. It is used to represent element values in all
 * attributes that describe annotations (RuntimeVisibleAnnotations,
 * RuntimeInvisibleAnnotations, RuntimeVisibleParameterAnnotations, and
 * RuntimeInvisibleParameterAnnotations).
 * <p>
 * The element_value structure has the following format:
 * </p>
 *
 * <pre>
 * element_value {
 *   u1 tag;
 *   union {
 *       u2 const_value_index;
 *
 *       {   u2 type_name_index;
 *           u2 const_name_index;
 *       } enum_const_value;
 *
 *       u2 class_info_index;
 *
 *       annotation annotation_value;
 *
 *        {   u2            num_values;
 *            element_value values[num_values];
 *        } array_value;
 *    } value;
 * }
 * </pre>
 *
 * @author Amos Shi
 */
public class AttributeAnnotationElementValue extends AttributeInfo {

    AttributeAnnotationElementValue(final u2 nameIndex, final String type, final PosDataInputStream posDataInputStream, final AbstractCPInfo[] cp)
            throws java.io.IOException, FileFormatException {
        super(nameIndex, type, posDataInputStream);
        super.checkSize(posDataInputStream.getPos());
    }

}
