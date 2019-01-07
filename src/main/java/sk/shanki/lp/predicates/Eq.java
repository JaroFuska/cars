/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.predicates;

import java.util.Objects;
import sk.shanki.lp.GroundingPredicate;
import sk.shanki.lp.ObjectConstantMapping;
import sk.shanki.lp.Substitution;
import sk.shanki.lp.exceptions.UnboundVariableException;
import sk.shanki.lp.Variable;
import sk.shanki.lp.Term;

/**
 *
 * @author shanki
 */
public class Eq implements GroundingPredicate {
    
    private final Term left;
    private final Term right;
    
    private int hash;

    public Eq(Term left, Term right) {
        this.left   = left;
        this.right  = right;
        
        updateHashCode();
    }
    
    private void updateHashCode() {
        hash = 7;
        hash = 67 * hash + Objects.hashCode(this.left);
        hash = 67 * hash + Objects.hashCode(this.right);        
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
        final Eq other = (Eq) obj;
        
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        
        if (!Objects.equals(this.left, other.left)) {
            return false;
        }
        return Objects.equals(this.right, other.right);
    }

    @Override
    public boolean evaluate(Substitution substitution, ObjectConstantMapping cache) throws UnboundVariableException {
        Term cx = left.partiallySubstitute(substitution);
        Term cy = right.partiallySubstitute(substitution);

         if (cx instanceof Variable) {
            if (cy instanceof Variable) {
                throw new UnboundVariableException((Variable)left, (Variable)right);
            } else {
                return substitution.put((Variable)left, cy);
            }
        } else {
            if (cy instanceof Variable)  {
                return substitution.put((Variable)right, cx);
            } else {
                return cx.equals(cy);
            }
        }
    }

}