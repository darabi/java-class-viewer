/*
 * AttributesCount.java    17:32 PM, May 15, 2014
 *
 * Copyright  2014, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile;

import org.freeinternals.commonlib.core.FileComponent;
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
 * @see <a
 * href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.16.1">
 * VM Spec:  The element_value structure  </a>
 */
public class AttributeSubElementValue extends FileComponent {

    public final u1 tag;
    public final u2 const_value_index;
    public final EnumConstValue enum_const_value;
    public final u2 class_info_index;
    public final AttributeSubAnnotation annotation_value;

    AttributeSubElementValue(final PosDataInputStream stream)
            throws java.io.IOException, FileFormatException {

        this.tag = null;
        this.const_value_index = null;
        this.enum_const_value = new EnumConstValue(stream);
        this.class_info_index = null;
        this.annotation_value = null;

    }

    public enum Tag {

        /**
         * byte.
         */
        B(AttributeSignature.BaseType.B.signature, JavaLangSpec.Keyword.kw_byte),
        /**
         * char.
         */
        C(AttributeSignature.BaseType.C.signature, JavaLangSpec.Keyword.kw_char),
        /**
         * double.
         */
        D(AttributeSignature.BaseType.D.signature, JavaLangSpec.Keyword.kw_double),
        /**
         * float.
         */
        F(AttributeSignature.BaseType.F.signature, JavaLangSpec.Keyword.kw_float),
        /**
         * int.
         */
        I(AttributeSignature.BaseType.I.signature, JavaLangSpec.Keyword.kw_int),
        /**
         * long.
         */
        J(AttributeSignature.BaseType.J.signature, JavaLangSpec.Keyword.kw_long),
        /**
         * short.
         */
        S(AttributeSignature.BaseType.S.signature, JavaLangSpec.Keyword.kw_short),
        /**
         * boolean.
         */
        Z(AttributeSignature.BaseType.Z.signature, JavaLangSpec.Keyword.kw_boolean),
        /**
         * String.
         */
        s('s', "String"),
        /**
         * Enum type.
         */
        e('e', "Enum type"),
        /**
         * Class.
         */
        c('c', "Class"),
        /**
         * Annotation type.
         */
        AT('@', "Annotation type"),
        /**
         * Array type.
         */
        AR(AttributeSignature.ReferenceType.ArrayTypeSignature.signature, "Array type");

        public final char tag;
        public final String description;

        private Tag(char tag, String desc) {
            this.tag = tag;
            this.description = desc;
        }

    }

    public static class EnumConstValue {

        public final u2 type_name_index;
        public final u2 const_name_index;

        private EnumConstValue(final PosDataInputStream stream) {
            this.type_name_index = null;
            this.const_name_index = null;
        }
    }

    public static class ArrayValue {

        public final u2 num_values;
        public final AttributeSubElementValue[] values;

        private ArrayValue(final PosDataInputStream stream) {
            this.num_values = null;
            this.values = null;
        }
    }
}
