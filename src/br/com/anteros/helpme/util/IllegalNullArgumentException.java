package br.com.anteros.helpme.util;

/**
 * Thrown to indicate that a method has been passed an illegal <code>null</code>
 * argument.
 */
public final class IllegalNullArgumentException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /** Name of the argument that caused the exception. */
    private final String argument;

    /**
     * Constructor with argument name.
     * 
     * @param argument
     *            Name of the argument that caused the exception.
     */
    public IllegalNullArgumentException(final String argument) {
        super("The argument '" + argument + "' cannot be null!");
        this.argument = argument;
    }

    /**
     * Returns the name of the argument that caused the exception.
     * 
     * @return Argument name.
     */
    public final String getArgument() {
        return argument;
    }

}
