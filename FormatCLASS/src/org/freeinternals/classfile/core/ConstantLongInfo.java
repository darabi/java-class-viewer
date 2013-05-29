/*
 * ConstantLongInfo.java    4:43 AM, August 5, 2007
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.classfile.core;

import java.io.IOException;

/**
 * The class for the {@code CONSTANT_Long_info} structure in constant pool.
 * The {@code CONSTANT_Long_info} structure has the following format:
 *
 * <pre>
 *    CONSTANT_Long_info {
 *        u1 tag;
 *        u4 high_bytes;
 *        u4 low_bytes;
 *    }
 * </pre>
 *
 * @author Amos Shi
 * @since JDK 6.0
 * @see <a href="http://www.freeinternals.org/mirror/java.sun.com/vmspec.2nded/ClassFile.doc.html#1348">
 * VM Spec:  The CONSTANT_Long_info Structure
 * </a>
 */
public class ConstantLongInfo extends AbstractCPInfo {
    //private u4 high_bytes;
    //private u4 low_bytes;

    private final long longValue;

    ConstantLongInfo(final PosDataInputStream posDataInputStream)
            throws IOException {
        super();
        this.tag.value = AbstractCPInfo.CONSTANT_Long;

        this.startPos = posDataInputStream.getPos() - 1;
        this.length = 9;

        this.longValue = posDataInputStream.readLong();
    }

    @Override
    public String getName() {
        return "Long";
    }

    @Override
    public String getDescription() {
        return String.format("ConstantLongInfo: Start Position: [%d], length: [%d], value: [%d].", this.startPos, this.length, this.longValue);
    }

    /**
     * Get the value of {@link java.lang.Long}.
     *
     * @return The long value
     */
    public long getValue() {
        return this.longValue;
    }
}
