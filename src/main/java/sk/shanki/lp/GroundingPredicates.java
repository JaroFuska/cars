/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.UnboundVariableException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author shanki
 */
public class GroundingPredicates extends ArrayList<GroundingPredicate> {
    
    public GroundingPredicates() {
    }
    
    public GroundingPredicates(Collection<GroundingPredicate> predicates) {
        super(predicates);
    }

    public boolean evaluate(Substitution substitution,  ObjectConstantMapping cache) throws UnboundVariableException {
        for (GroundingPredicate predicate : this) {
            if (predicate.evaluate(substitution, cache) == false) {
                return false;
            }
        }
        
        return true;
    }

}