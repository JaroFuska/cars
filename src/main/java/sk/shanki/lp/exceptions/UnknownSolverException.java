/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.exceptions;

/**
 *
 * @author shanki
 */
public class UnknownSolverException extends Exception {

    private final String solverName;
    
    public UnknownSolverException(String solverName) {
        this.solverName = solverName;
    }

    @Override
    public String toString() {
        return "Unknown solver '" + solverName + "'";
    }

}