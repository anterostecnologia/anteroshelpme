package br.com.anteros.helpme.util;

import java.io.File;

/**
 * One or more attempts to merge properties failed.
 */
public final class MergeException extends Exception {

    private static final long serialVersionUID = 1L;

    private final File file;

    private final Problem[] problems;

    /**
     * Constructor with file and problem list.
     * 
     * @param file
     *            File that caused the exception.
     * @param problems
     *            List of one or more problems.
     */
    public MergeException(final File file, final Problem[] problems) {
        super("The properties file '" + file + "' was modified by someone else "
                + "and cannot be merged!");

        ClassLoaderUtil.checkNotNull("file", file);
        this.file = file;

        ClassLoaderUtil.checkNotNull("problems", problems);
        this.problems = problems;
    }

    /**
     * File that caused the exception.
     * 
     * @return Merged file.
     */
    public final File getFile() {
        return file;
    }

    /**
     * Returns an array with the problems.
     * 
     * @return Problem detail list.
     */
    public final Problem[] getProblems() {
        return problems;
    }

    /**
     * Error while merging a property.
     */
    public static final class Problem {

        /** Message describing the problem. */
        private final String text;

        /** Property from memory. */
        private final Property prop;

        /** Property from file. */
        private final Property fileProp;

        /**
         * Constructor with message and properties.
         * 
         * @param text
         *            Message describing the problem.
         * @param prop
         *            Property from memory.
         * @param fileProp
         *            Property from file.
         */
        public Problem(final String text, final Property prop, final Property fileProp) {
            super();

            ClassLoaderUtil.checkNotNull("text", text);
            this.text = text;

            ClassLoaderUtil.checkNotNull("prop", prop);
            this.prop = prop;

            ClassLoaderUtil.checkNotNull("fileProp", fileProp);
            this.fileProp = fileProp;

        }

        /**
         * Returns the message describing the problem.
         * 
         * @return Detailed message.
         */
        public final String getText() {
            return text;
        }

        /**
         * Returns the property from memory.
         * 
         * @return Property.
         */
        public final Property getProp() {
            return prop;
        }

        /**
         * Returns the property from disk.
         * 
         * @return Property.
         */
        public final Property getFileProp() {
            return fileProp;
        }

        /**
         * {@inheritDoc}
         */
        public final String toString() {
            return text + " - Property: {" + prop + "}, File Property: {" + fileProp + "}";
        }

    }

}
