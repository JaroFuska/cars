/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.predicates;

import java.util.Objects;
import sk.shanki.lp.GroundingPredicate;
import sk.shanki.lp.ObjectConstantMapping;
import sk.shanki.lp.Substitution;
import sk.shanki.lp.exceptions.UnboundVariableException;
import sk.shanki.lp.Term;

/**
 *
 * @author shanki
 */
public class GreaterEq implements GroundingPredicate {

    private final Term left;
    private final Term right;
    
    private int hash;
    
    public GreaterEq(Term left, Term right) {
        this.left   = left;
        this.right  = right;
        
        updateHashCode();
    }
    
    private void updateHashCode() {
        hash = 5;
        hash = 59 * hash + Objects.hashCode(this.left);
        hash = 59 * hash + Objects.hashCode(this.right);        
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
        final GreaterEq other = (GreaterEq) obj;
        
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
        Term cx = left.fullySubstitute(substitution);        
        Term cy = right.fullySubstitute(substitution);
        
        return cx.compareTo(cy, cache) >= 0;
    }

}