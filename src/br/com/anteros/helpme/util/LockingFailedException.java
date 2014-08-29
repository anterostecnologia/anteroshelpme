package br.com.anteros.helpme.util;

/**
 * Getting a lock failed.
 */
public final class LockingFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor with message.
     * 
     * @param message
     *            Error message.
     */
    public LockingFailedException(final String message) {
        super(message);
    }

    /**
     * Constructor with message.
     * 
     * @param message
     *            Error message.
     * @param cause
     *            Original error.
     */
    public LockingFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
