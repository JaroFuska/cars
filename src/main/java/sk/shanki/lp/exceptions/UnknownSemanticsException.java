/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.exceptions;

/**
 *
 * @author shanki
 */
public class UnknownSemanticsException extends Exception {
    
    private final String semanticsName;

    public UnknownSemanticsException(String semanticsName) {
        this.semanticsName = semanticsName;
    }

    @Override
    public String toString() {
        return "Unkwnon semantics name '" + semanticsName + "'";
    }

}