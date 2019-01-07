/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.predicates;

import java.util.Objects;
import sk.shanki.lp.GroundingPredicate;
import sk.shanki.lp.NumberConstant;
import sk.shanki.lp.ObjectConstantMapping;
import sk.shanki.lp.Substitution;
import sk.shanki.lp.Variable;
import sk.shanki.lp.exceptions.UnboundVariableException;
import sk.shanki.lp.Term;

/**
 *
 * @author shanki
 */
public class Plus implements GroundingPredicate {
    private final Term x;
    private final Term y;
    private final Term right;
    
    private int hash;
    
    public Plus(Term x, Term y, Term right) {
        this.x = x;
        this.y = y;
        this.right = right;
        
        updateHashCode();
    }

    private void updateHashCode() {
        hash = 5;
        hash = 89 * hash + Objects.hashCode(this.x);
        hash = 89 * hash + Objects.hashCode(this.y);
        hash = 89 * hash + Objects.hashCode(this.right);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Plus other = (Plus) obj;
        
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        
        if (!Objects.equals(this.x, other.x)) {
            return false;
        }
        if (!Objects.equals(this.y, other.y)) {
            return false;
        }
        return Objects.equals(this.right, other.right);
    }

    @Override
    public boolean evaluate(Substitution substitution, ObjectConstantMapping cache) throws UnboundVariableException {
        NumberConstant xc   = (NumberConstant)x.fullySubstitute(substitution);
        NumberConstant yc   = (NumberConstant)y.fullySubstitute(substitution);
        
        NumberConstant res  = xc.add(yc);
        Term r              = right.partiallySubstitute(substitution);
        
        if (r instanceof Variable) {
            return substitution.put((Variable)right, res);
        } else {
            return res.equals(r);
        }

    }

}