/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.exceptions;

/**
 *
 * @author shanki
 */
public class SolverException extends Exception {

    public SolverException(String message) {
        super(message);
    }
    
    public SolverException(Exception sub) {
        super(sub);
    }
    
    public SolverException() {

    }

}