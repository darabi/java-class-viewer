/*
 * AttributeCode.java    09:24 AM, April 28, 2014
 *
 * Copyright  2014, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile;

import java.io.IOException;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.format.FileFormatException;

/**
 * The class for the {@code RuntimeVisibleAnnotations} attribute. The
 * {@code RuntimeVisibleAnnotations} attribute has the following format:
 *
 * <pre>
 * RuntimeVisibleAnnotations_attribute {
 *   u2         attribute_name_index;
 *   u4         attribute_length;
 *   u2         num_annotations;
 *   annotation annotations[num_annotations];
 * }
 *
 * annotation {
 *   u2 type_index;
 *   u2 num_element_value_pairs;
 *   {   u2            element_name_index;
 *       element_value value;
 *   } element_value_pairs[num_element_value_pairs];
 * }
 * </pre>
 *
 * @author Amos Shi
 * @since JDK 6.0
 * @see <a
 * href="http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.16">
 * VM Spec: The RuntimeVisibleAnnotations Attribute
 * </a>
 */
public class AttributeRuntimeVisibleAnnotations extends AttributeInfo {

    private transient final u2 num_annotations;
    private transient Annotation[] annotations;

    AttributeRuntimeVisibleAnnotations(final u2 nameIndex, final String type, final PosDataInputStream posDataInputStream, final AbstractCPInfo[] cp)
            throws IOException, FileFormatException {
        super(nameIndex, type, posDataInputStream);

        int i;

        this.num_annotations = new u2();
        this.num_annotations.value = posDataInputStream.readUnsignedShort();

        // TODO FOR annotation
        super.checkSize(posDataInputStream.getPos());
    }

    /**
     * Get the value of {@code num_annotations}.
     *
     * @return The value of {@code num_annotations}
     */
    public int getMaxStack() {
        return this.num_annotations.value;
    }

    /**
     * Get the value of {@code annotations}[{@code index}].
     *
     * @param index Index of the annotation
     * @return The value of {@code annotations}[{@code index}]
     */
    public Annotation getAnnotation(final int index) {
        Annotation a = null;
        if (this.annotations != null) {
            a = this.annotations[index];
        }
        return a;
    }

    /**
     * The {@code annotation} structure in {@code RuntimeVisibleAnnotations}
     * attribute. It has the following format:
     *
     * <pre>
     * annotation {
     *   u2 type_index;
     *   u2 num_element_value_pairs;
     *   {   u2            element_name_index;
     *       element_value value;
     *   } element_value_pairs[num_element_value_pairs];
     * }
     * </pre>
     *
     * @author Amos Shi
     */
    public final class Annotation extends ClassComponent {

        private transient final u2 type_index;
        private transient final u2 num_element_value_pairs;
        private transient ElementValuePair[] element_value_pairs;

        protected Annotation(final PosDataInputStream posDataInputStream)
                throws IOException {
            this.startPos = posDataInputStream.getPos();
            this.length = 8;

            this.type_index = new u2();
            this.type_index.value = posDataInputStream.readUnsignedShort();
            this.num_element_value_pairs = new u2();
            this.num_element_value_pairs.value = posDataInputStream.readUnsignedShort();
            // TODO
        }

        /**
         * Get the value of {@code num_element_value_pairs}[{@code index}].
         *
         * @param index Index of the num_element_value_pairs item
         * @return The value of {@code num_element_value_pairs}[{@code index}]
         */
        public ElementValuePair getElementvaluePair(final int index) {
            ElementValuePair p = null;
            if (this.num_element_value_pairs != null) {
                p = this.element_value_pairs[index];
            }
            return p;
        }

    }

    public final class ElementValuePair extends ClassComponent {

    }

}
