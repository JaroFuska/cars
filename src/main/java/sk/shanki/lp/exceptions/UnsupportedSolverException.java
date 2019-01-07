/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.exceptions;

/**
 *
 * @author shanki
 */
public class UnsupportedSolverException extends Exception {

    private final String solverName;
    private final String instead;

    public UnsupportedSolverException(String solverName, String instead) {
        this.solverName = solverName;
        this.instead    = instead;
    }

    public String instead() {
        return instead;
    }

    @Override
    public String toString() {
        return "'" + solverName + "' is not supported for this semantics. You must use '" + instead + "' instead.";
    }

}