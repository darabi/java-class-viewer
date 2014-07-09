/*
 * AttributeSubAnnotation.java    16:30 PM, May 21, 2014
 *
 * Copyright  2014, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile;

/**
 * The annotation structure has the following format.
 *
 * <pre>
 * annotation {
 *     u2 type_index;
 *     u2 num_element_value_pairs;
 *     {   u2            element_name_index;
 *         element_value value;
 *     } element_value_pairs[num_element_value_pairs];
 * }
 * </pre>
 *
 * @author Amos Shi
 * @see <a
 * href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.16">
 * VM Spec: annotation  </a>
 * @see AttributeRuntimeInvisibleAnnotations
 * @see AttributeRuntimeInvisibleParameterAnnotations
 * @see AttributeRuntimeVisibleAnnotations
 * @see AttributeRuntimeVisibleParameterAnnotations
 */
public class AttributeSubAnnotation {

}
