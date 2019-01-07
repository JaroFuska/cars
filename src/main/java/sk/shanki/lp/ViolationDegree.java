/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author shanki
 */
public class ViolationDegree implements Comparable<ViolationDegree> {

    private final List<BigDecimal> weights;
    
    public ViolationDegree(List<BigDecimal> weights) {
        this.weights = weights;
    }

    public int compareTo(ViolationDegree other, int ifNull) {
        return other == null? ifNull : this.compareTo(other);
    }

    @Override
    public int compareTo(ViolationDegree other) {
        if (other == null) {
            throw new IllegalArgumentException("Other cannot be null");
        }
        
        if (this.weights.size() != other.weights.size()) {
            throw new IllegalArgumentException("Other must be of the same length as this.");
        }
        
        Iterator<BigDecimal> i1 = this.weights.iterator();
        Iterator<BigDecimal> i2 = other.weights.iterator();
        
        while(i1.hasNext()) {
            BigDecimal v1 = i1.next();
            BigDecimal v2 = i2.next();
            
            int c = v1.compareTo(v2);
            
            if (c != 0) {
                return c;
            }
        }
        
        return 0;
    }

    @Override
    public String toString() {
        return weights.toString();
    }

}