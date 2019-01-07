/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.smodels;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author shanki
 */
public class SmodelsThreeValueSet {
    private final Set<SmodelsLiteral> allLiterals;
    private final Set<SmodelsLiteral> trueLiterals;
    private final Set<SmodelsLiteral> falseLiterals;

    public SmodelsThreeValueSet(SmodelsThreeValueSet other) {
        allLiterals     = other.allLiterals;
        trueLiterals    = new HashSet<>(other.trueLiterals);
        falseLiterals   = new HashSet<>(other.falseLiterals);
    }

    public SmodelsThreeValueSet(Set<SmodelsLiteral> allLiterals) {
        this.allLiterals    = allLiterals;
        trueLiterals        = new HashSet<>();
        falseLiterals       = new HashSet<>();
    }

    public int size() {
        return trueLiterals.size() + falseLiterals.size();
    }
    
    public void addFalse(Set<SmodelsLiteral> literals) {
        falseLiterals.addAll(literals);
    }
    
    public boolean addFalse(SmodelsLiteral literal) {
        return falseLiterals.add(literal);
    }
    
    public Set<SmodelsLiteral> getFalse() {
        return falseLiterals;
    }
    
    public void addTrue(Set<SmodelsLiteral> literals) {
        trueLiterals.addAll(literals);
    }
    
    public boolean addTrue(SmodelsLiteral literal) {
        return trueLiterals.add(literal);
    }    

    public Set<SmodelsLiteral> getTrue() {
        return trueLiterals;
    }

    public boolean isTrue(SmodelsLiteral literal) {
        return trueLiterals.contains(literal);
    }
    
    public boolean isFalse(SmodelsLiteral literal) {
        return falseLiterals.contains(literal);
    }

    public void removeTrue(SmodelsLiteral literal) {
        trueLiterals.remove(literal);
    }
    
    public void removeFalse(SmodelsLiteral literal) {
        falseLiterals.remove(literal);
    }

    public boolean isInconsistent() {
        return !isConsistent();
    }

    public boolean isConsistent() {
        for (SmodelsLiteral literal : trueLiterals) {
            if (falseLiterals.contains(literal)) {
                return false;
            }
        }
        
        return true;
    }

    public boolean isTotal() {
        for (SmodelsLiteral literal : allLiterals) {
            if (trueLiterals.contains(literal) == false &&
                falseLiterals.contains(literal) == false) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SmodelsThreeValueSet) {
            SmodelsThreeValueSet other = (SmodelsThreeValueSet)obj;
            
            return  this.trueLiterals.equals(other.trueLiterals) &&
                    this.falseLiterals.equals(other.falseLiterals);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.trueLiterals);
        hash = 11 * hash + Objects.hashCode(this.falseLiterals);
        return hash;
    }

}