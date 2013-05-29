/*
 * ConstantFloatInfo.java    4:41 AM, August 5, 2007
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */

package org.freeinternals.classfile.core;

import java.io.IOException;

/**
 * The class for the {@code CONSTANT_Float_info} structure in constant pool.
 * The {@code CONSTANT_Float_info} structure has the following format:
 *
 * <pre>
 *    CONSTANT_Float_info {
 *        u1 tag;
 *        u4 bytes;
 *    }
 * </pre>
 *
 * @author Amos Shi
 * @since JDK 6.0
 * @see <a href="http://www.freeinternals.org/mirror/java.sun.com/vmspec.2nded/ClassFile.doc.html#21942">
 * VM Spec: The CONSTANT_Float_info Structure
 * </a>
 */
public class ConstantFloatInfo extends AbstractCPInfo {

    private final Float floatValue;

    ConstantFloatInfo(final PosDataInputStream posDataInputStream)
        throws IOException
    {
        super();
        this.tag.value = AbstractCPInfo.CONSTANT_Float;

        this.startPos = posDataInputStream.getPos() - 1;
        this.length = 5;
        
        this.floatValue = posDataInputStream.readFloat();
    }
    
    @Override
    public String getName()
    {
        return "Float";
    }

    @Override
    public String getDescription()
    {
        return String.format("ConstantFloatInfo: Start Position: [%d], length: [%d], value: [%f].", this.startPos, this.length, this.floatValue);
    }
    
    /**
     * Get the value of {@link java.lang.Float}.
     *
     * @return The float value
     */
    public float getValue()
    {
        return this.floatValue;
    }
}
