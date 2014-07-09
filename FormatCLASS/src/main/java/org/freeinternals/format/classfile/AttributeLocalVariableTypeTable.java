/*
 * AttributeLocalVariableTypeTable.java    11:08 AM, April 28, 2014
 *
 * Copyright  2014, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile;

import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.format.FileFormatException;

/**
 * The {@code LocalVariableTypeTable} attribute is an optional variable-length
 * attribute in the {@code attributes} table of a {@code Code} attribute; 
 * It may be used by debuggers to determine the value of a given local variable during the
 * execution of a method.
 *
 * The {@code LocalVariableTypeTable} attribute has the following format:
 * 
 * <pre>
 * LocalVariableTypeTable_attribute {
 *    u2 attribute_name_index;
 *    u4 attribute_length;
 *    u2 local_variable_type_table_length;
 *    {   u2 start_pc;
 *        u2 length;
 *        u2 name_index;
 *        u2 signature_index;
 *        u2 index;
 *    } local_variable_type_table[local_variable_type_table_length];
 * }
 * </pre>
 *
 * @author Amos Shi
 * @see <a
 * href="http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.14">
 * VM Spec: The LocalVariableTypeTable Attribute
 * </a>
 */
public class AttributeLocalVariableTypeTable extends AttributeInfo {

    AttributeLocalVariableTypeTable(final u2 nameIndex, final String type, final PosDataInputStream posDataInputStream, final AbstractCPInfo[] cp)
            throws java.io.IOException, FileFormatException {
        super(nameIndex, type, posDataInputStream);
        super.checkSize(posDataInputStream.getPos());
    }
}
