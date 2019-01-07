/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.predicates;

import sk.shanki.lp.GroundingPredicate;
import sk.shanki.lp.ObjectConstantMapping;
import sk.shanki.lp.StringConstant;
import sk.shanki.lp.Substitution;
import sk.shanki.lp.Variable;
import sk.shanki.lp.exceptions.UnboundVariableException;
import sk.shanki.lp.Term;

/**
 *
 * @author shanki
 */
public class Remove implements GroundingPredicate {
    
    private final Term x;
    private final Term y;
    
    public Remove(Term x, Term y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean evaluate(Substitution substitution, ObjectConstantMapping cache) throws UnboundVariableException {
        StringConstant vx = (StringConstant)x.fullySubstitute(substitution);
        
        StringConstant res = vx.remove();
        
        Term vy = y.partiallySubstitute(substitution);
        
        if (vy instanceof Variable) {
            return substitution.put((Variable)vy, res);
        } else {
            return res.equals(vy);
        }
    }

}