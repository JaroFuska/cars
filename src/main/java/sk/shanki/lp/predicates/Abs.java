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
import sk.shanki.lp.Term;

/**
 *
 * @author shanki
 */
public class Abs implements GroundingPredicate {
    private final Term left;
    private final Term right;
    
    private int hash;
    
    public Abs(Term left, Term right) {
        this.left = left;
        this.right = right;
        
        updateHashCode();
    }

    private void updateHashCode() {
        hash = 3;
        hash = 97 * hash + Objects.hashCode(this.left);
        hash = 97 * hash + Objects.hashCode(this.right);  
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
        final Abs other = (Abs) obj;
        
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

    @Override
    public boolean evaluate(Substitution substitution, ObjectConstantMapping cache) {
        NumberConstant c    = (NumberConstant)left.partiallySubstitute(substitution);
        NumberConstant abs  = c.abs();
        Term r              = right.partiallySubstitute(substitution);
        
        if (r instanceof Variable) {
            return substitution.put((Variable)r, c);
        } else {
            return abs.equals(r);
        }

    }

}