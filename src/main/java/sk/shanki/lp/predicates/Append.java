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
public class Append implements GroundingPredicate {
    
    private final Term x;
    private final Term y;
    private final Term z;
    
    public Append(Term x, Term y, Term z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean evaluate(Substitution substitution, ObjectConstantMapping cache) throws UnboundVariableException {
        StringConstant vx = (StringConstant)x.fullySubstitute(substitution);
        StringConstant vy = (StringConstant)y.fullySubstitute(substitution);
        
        StringConstant res = vx.append(vy);
        
        Term vz = z.partiallySubstitute(substitution);
        
        if (vz instanceof Variable) {
            return substitution.put((Variable)vz, res);
        } else {
            return res.equals(vz);
        }
    }

}