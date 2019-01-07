/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.smodels;

import sk.shanki.lp.Collector;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import sk.shanki.lp.AnswerSets;

/**
 *
 * @author shanki
 */
public class SmodelsAlgorithm {
    
    private final SmodelsProgram program;

    // Constructors
    
    public SmodelsAlgorithm(SmodelsProgram program) {
        this.program = program;
    }

    // Helpers
    
    private Set<SmodelsLiteral> literals() {
        return program.literals();
    }
    
    private Collection<SmodelsLiteral> negativeBodyLiterals() {
        return program.negativeBodyLiterals();
    }
    
    // Main methods

    
    public AnswerSets evaluate() {
        return evaluate(0);
    }
    
    public AnswerSets evaluate(int maxAnswerSets) {
        SmodelsThreeValueSet a     = new SmodelsThreeValueSet(literals());
        
        Collector ass   = new Collector(program.getWeakConstraints(), maxAnswerSets);
        
        smodels(a, ass);
        
        return ass.getAnswerSets();
    }
    
    // Algorithm methods
    
    private boolean smodels(SmodelsThreeValueSet a, Collector ass) {
        a   = expand(a);
        a   = lookAhead(a);
        
        if (a.isInconsistent()) {
            return true;
        }
        
        if (a.isTotal()){            
            // since we are modifying a, we need to make a copy
            SmodelsAnswerSet as = new SmodelsAnswerSet(a.getTrue());
            ass.add(as.decompile());
            
            return ass.searchNext();
        } else {
            SmodelsLiteral x = heuristics(a);
            
            if (smodelsWithTrue(a, x, ass) == false) {
                return false;
            }

            //todo vyhodit porovnanie
            return smodelsWithFalse(a, x, ass) != false;
        }
    }
    
    private boolean smodelsWithTrue(SmodelsThreeValueSet a, SmodelsLiteral x, Collector ass) {
        if (a.addTrue(x)) {
            boolean ret = smodels(a, ass);
            a.removeTrue(x);
            
            return ret;
        }
        
        return true;
    }
    
    private boolean smodelsWithFalse(SmodelsThreeValueSet a, SmodelsLiteral x, Collector ass) {
        if (a.addFalse(x)) {
            boolean ret = smodels(a, ass);
            a.removeFalse(x);
            
            return ret;
        }
        
        return true;
    }    
        
    
    private SmodelsThreeValueSet expand(SmodelsThreeValueSet a) {
        
        SmodelsThreeValueSet aPrime;
        
        do {
            aPrime  = a;
            a       = atLeast(a);
                        
            Set<SmodelsLiteral> neg = notAtMost(a);
            
            a.addFalse(neg);
            
        } while(a.equals(aPrime) == false);

        return a;
    }
    
    private SmodelsThreeValueSet atLeast(SmodelsThreeValueSet a) {
        
        program.atLeast().init();
        program.atLeast().start(a);
        
        return program.atLeast().collect();
    }
    
    private Set<SmodelsLiteral> notAtMost(SmodelsThreeValueSet a) {
        program.atMost().init();
        program.atMost().start(a);
        
        return program.atMost().collect();
    }
    
    private SmodelsThreeValueSet lookAhead(SmodelsThreeValueSet a) {
        SmodelsThreeValueSet aPrime;
        
        do {
            aPrime  = a;
            a       = lookAheadOnce(a);
            
        } while(a.equals(aPrime) == false);
        
        return a;
    }

    private SmodelsThreeValueSet expandWithTrue(SmodelsThreeValueSet a, SmodelsLiteral x) {
        if (a.addTrue(x)) {
            SmodelsThreeValueSet aprime    = expand(a);
            a.removeTrue(x);
            return aprime;
        } else {
            return a;
        }
    }
    
    private SmodelsThreeValueSet expandWithFalse(SmodelsThreeValueSet a, SmodelsLiteral x) {
        if (a.addFalse(x)) {
            SmodelsThreeValueSet aprime    = expand(a);
            a.removeFalse(x);
            return aprime;
        } else {
            return a;
        }
    }    
    
    private SmodelsThreeValueSet lookAheadOnce(SmodelsThreeValueSet a) {
        Set<SmodelsLiteral> bTrue  = new HashSet<>(literals());
        bTrue.removeAll(a.getTrue());
        bTrue.removeAll(a.getFalse());
        
        Set<SmodelsLiteral> bFalse = new HashSet<>(bTrue);
        
        while (bTrue.isEmpty() == false || bFalse.isEmpty() == false) {
            if (bTrue.isEmpty() == false) {
                SmodelsLiteral x = bTrue.iterator().next();

                SmodelsThreeValueSet aprime = expandWithTrue(a, x);

                if (aprime != a) {
                    bTrue.removeAll(aprime.getTrue());
                    bFalse.removeAll(aprime.getFalse());
                    
                }

                if (aprime.isInconsistent()) {
                    return expandWithFalse(a, x);
                }
            }
            
            if (bFalse.isEmpty() == false) {
                SmodelsLiteral x = bFalse.iterator().next();

                SmodelsThreeValueSet aprime    = expandWithFalse(a, x);
                
                if (aprime != a) {
                    bTrue.removeAll(aprime.getTrue());
                    bFalse.removeAll(aprime.getFalse());
                }

                if (aprime.isInconsistent()) {
                    return expandWithTrue(a, x);
                }
            }
        }
        
        return a;
    }
    
    private SmodelsLiteral heuristics(SmodelsThreeValueSet a) {
        Set<SmodelsLiteral> toDecide  = new HashSet<>(negativeBodyLiterals());
        
        toDecide.removeAll(a.getTrue());
        toDecide.removeAll(a.getFalse());

        int val1 = Integer.MIN_VALUE;
        int val2 = Integer.MIN_VALUE;
        SmodelsLiteral sel = null;
        
        for (SmodelsLiteral x : toDecide) {
            
            SmodelsThreeValueSet axtExpanded = expandWithTrue(a, x);            
            SmodelsThreeValueSet axfExpanded = expandWithFalse(a, x);            
            
            int actValue1 = Math.min(axtExpanded.size(), axfExpanded.size());
            int actValue2 = Math.max(axtExpanded.size(), axfExpanded.size());
            
            if (sel == null || actValue1 > val1 || (actValue1 == val1 && actValue2 > val2)) {
                val1 = actValue1;
                val2 = actValue2;
                sel = x;
            }
        }
        
        return sel;
    }    

}