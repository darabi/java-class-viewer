/*
 * ClassFormatException.java    August 7, 2007, 8:31 PM
 *
 * Copyright  2007, FreeInternals.org. All rights reserved.
 * Use is subject to license terms.
 */
package org.freeinternals.format.classfile;

/**
 * The byte array is not a valid class file.
 *
 * @author Amos Shi
 * @since JDK 6.0
 */
public class ClassFormatException extends java.lang.Exception {

    private static final long serialVersionUID = 4876543219876500001L;

    /**
     * Creates a new instance of <code>ClassFormatException</code> without detail message.
     */
    public ClassFormatException() {
    }

    /**
     * Constructs an instance of <code>ClassFormatException</code> with the specified detail message.
     * 
     * @param msg The detail message.
     */
    public ClassFormatException(final String msg) {
        super(msg);
    }
}
