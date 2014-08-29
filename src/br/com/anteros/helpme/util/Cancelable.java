package br.com.anteros.helpme.util;

/**
 * A unit of work that is cancelable.
 */
public interface Cancelable {

    /**
     * Try to cancel the unit of work.
     */
    public void cancel();

    /**
     * Returns if the unit of work was canceled.
     * 
     * @return If it was canceled <code>true</code> else <code>false</code>.
     */
    public boolean isCanceled();

}
