/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author shanki
 */
class ViolationDegreeComputer {
    
    private final SortedMap<BigDecimal,Set<WeakConstraintTuple>> tuples = new TreeMap<>();

    void consume(WeakConstraint weak, boolean isViolated) {
        BigDecimal level = weak.getLevel();
        
        Set<WeakConstraintTuple> set = tuples.get(level);
        if (set == null) {
            set = new HashSet<>();
            tuples.put(level, set);
        }
        
        if (isViolated) {
            set.add(weak.toTuple());
        }
    }

    ViolationDegree getValue() {
        List<BigDecimal> weights = new ArrayList<>();
        
        for (BigDecimal level : tuples.keySet()) {
            Set<WeakConstraintTuple> set = tuples.get(level);
            
            BigDecimal weight = BigDecimal.ZERO;
            
            for (WeakConstraintTuple tuple : set) {
                weight = weight.add(tuple.getWeight());
            }
            
            weights.add(weight);
        }
        
        return new ViolationDegree(weights);
    }
    
}
