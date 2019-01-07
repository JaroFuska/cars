/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shanki
 */
public class Substitution {
    private final Map<Variable, Term> map;
    
    public Substitution() {
        map = new HashMap<>();
    }
    
    public Substitution(Substitution sub) {
        map = new HashMap<>(sub.map);
    }

    public boolean put(Variable variable, Term value) {
        if (map.containsKey(variable)) {
            Term otherValue = map.get(variable);
            
            return otherValue.unify(value, this);            
        } else {
            Term subEl = value.partiallySubstitute(this);
            map.put(variable, subEl);
            
            for (Variable var : map.keySet()) {
                Term olde = map.get(var);
                Term newe = olde.partiallySubstitute(this);
                map.put(var, newe);
            }
            
            return true;
        }
    }

    public Term get(Variable variable) {
        return map.get(variable);
    }

    public Iterable<Variable> variables() {
        return map.keySet();
    }

    @Override
    public String toString() {
        return map.toString();
    }

    boolean contains(Variable variable) {
        return map.containsKey(variable);
    }
    
}