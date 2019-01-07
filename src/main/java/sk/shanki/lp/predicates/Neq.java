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
public class Neq implements GroundingPredicate {

    private final Term left;
    private final Term right;
    
    private int hash;
    
    public Neq(Term left, Term right) {
        this.left   = left;
        this.right  = right;
        
        updateHashCode();
    }
    
    @Override
    public boolean evaluate(Substitution sub, ObjectConstantMapping cache) throws UnboundVariableException {
        Term cx = (Term)left.fullySubstitute(sub);
        Term cy = (Term)right.fullySubstitute(sub);        

        return !cx.equals(cy);
    }

    private void updateHashCode() {
        hash = 3;
        hash = 83 * hash + Objects.hashCode(this.left);
        hash = 83 * hash + Objects.hashCode(this.right);        
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
        final Neq other = (Neq) obj;
        
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        
        if (!Objects.equals(this.left, other.left)) {
            return false;
        }
        if (!Objects.equals(this.right, other.right)) {
            return false;
        }
        return true;
    }

}