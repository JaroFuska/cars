/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author shanki
 */
class WeakConstraintTuple {

    private final BigDecimal weight;
    private final Terms terms;
    
    WeakConstraintTuple(BigDecimal weight, Terms terms) {
        this.weight = weight;
        this.terms  = terms;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.weight);
        hash = 97 * hash + Objects.hashCode(this.terms);
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
        final WeakConstraintTuple other = (WeakConstraintTuple) obj;
        if (!Objects.equals(this.weight, other.weight)) {
            return false;
        }
        if (!Objects.equals(this.terms, other.terms)) {
            return false;
        }
        return true;
    }

    BigDecimal getWeight() {
        return weight;
    }
}