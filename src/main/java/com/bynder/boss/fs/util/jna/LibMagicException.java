package com.bynder.boss.fs.util.jna;

public class LibMagicException extends Exception {
    /** Magic exception serialization version identifier. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiate a new <code>MagicException</code>
     *
     * @param message
     * @param cause
     */
    public LibMagicException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiate a new <code>MagicException</code>
     * @param message
     */
    public LibMagicException(String message) {
        super(message);
    }
}



 



 

