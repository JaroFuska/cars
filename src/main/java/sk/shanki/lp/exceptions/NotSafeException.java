/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.exceptions;

import sk.shanki.lp.grounder.GroundingElement;

/**
 *
 * @author shanki
 */
public class NotSafeException extends SolverException {

    private final GroundingElement element;
    
    public NotSafeException(GroundingElement rule) {
        this.element = rule;
    }
    
    public NotSafeException(GroundingElement rule, Exception sub) {
        super(sub);
        
        this.element = rule;
    }

    @Override
    public String toString() {
        return "Element '" + element + "' is not safe. ";
    }

}