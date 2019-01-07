/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.UnboundVariableException;

/**
 *
 * @author shanki
 */
public interface GroundingPredicate {

    public boolean evaluate(Substitution substitution, ObjectConstantMapping cache) throws UnboundVariableException;

}