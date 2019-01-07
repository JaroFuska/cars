/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.smodels;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.AnswerSets;

/**
 *
 * @author shanki
 */
public class SmodelsAnswerSet {
    private final Set<SmodelsLiteral> literals;
    
    public SmodelsAnswerSet(Set<SmodelsLiteral> literals) {
        this.literals    = new HashSet<>(literals);
    }
    
    public AnswerSet decompile() {
        AnswerSet decomp = new AnswerSet();
        
        for (SmodelsLiteral literal : literals) {
            decomp.add(literal.getLiteral());
        }
        
        return decomp;
    }
    
    public static AnswerSets decompileAndAdd(Collection<SmodelsAnswerSet> ass) {
        AnswerSets decomp = new AnswerSets();
        
        for (SmodelsAnswerSet cAs : ass) {
            AnswerSet as = cAs.decompile();
            decomp.add(as);
        }

        return decomp;
    }

    boolean containsAll(List<SmodelsLiteral> positive) {
        return literals.containsAll(positive);
    }

    boolean containsNone(List<SmodelsLiteral> negative) {
        for (SmodelsLiteral literal : negative) {
            if (literals.contains(literal)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public String toString() {
        return literals.toString();
    }

}