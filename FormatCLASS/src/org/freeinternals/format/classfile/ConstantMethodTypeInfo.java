/*
 * ConstantMethodTypeInfo.java    12:04 AM, April 28, 2014
 *
 * Copyright 2014, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile;

import java.io.IOException;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.format.FileFormatException;

/**
 * The {@code CONSTANT_MethodHandle_info} structure is used to represent a
 * method handle.
 *
 * <pre>
 *    CONSTANT_MethodType_info {
 *        u1 tag;
 *        u2 descriptor_index;
 *    }
 * </pre>
 *
 * @author Amos Shi
 * @see <a
 * href="http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.9">
 * VM Spec: The CONSTANT_MethodType_info Structure
 * </a>
 */
public class ConstantMethodTypeInfo extends AbstractCPInfo {

    /**
     * The value of the {@code descriptor_index} item must be a valid index into
     * the {@code constant_pool} table. The {@code constant_pool} entry at that
     * index must be a {@code CONSTANT_Utf8_info} structure representing a
     * method descriptor.
     */
    private final u2 descriptor_index;

    ConstantMethodTypeInfo(final PosDataInputStream posDataInputStream)
            throws IOException, FileFormatException {
        super();
        this.tag.value = AbstractCPInfo.CONSTANT_MethodType;
        super.startPos = posDataInputStream.getPos() - 1;
        this.descriptor_index = new u2();
        this.descriptor_index.value = posDataInputStream.readUnsignedShort();
        super.length = 1 + 2;
    }

    @Override
    public String getName() {
        return "MethodType";
    }

    @Override
    public String getDescription() {
        return String.format("ConstantMethodTypeInfo: Start Position: [%d], length: [%d], descriptor_index: [%d]. ",
                this.startPos, super.length, this.descriptor_index.value);
    }

    /**
     * Get the {@code descriptor_index} value.
     *
     * @return The value of {@code descriptor_index}
     */
    public int getDescriptorIndex() {
        return this.descriptor_index.value;
    }
}
