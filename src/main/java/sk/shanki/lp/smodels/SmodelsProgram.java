/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.smodels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.WeakConstraint;
import sk.shanki.lp.WeakConstraints;

/**
 *
 * @author shanki
 */
public class SmodelsProgram implements Iterable<SmodelsRule> {
    private final SmodelsLiteralCache cache;
    
    private final Set<SmodelsLiteral> literals    = new HashSet<>();
    private final Set<SmodelsLiteral> negativeBodyLiterals    = new HashSet<>();
    private final List<SmodelsRule> rules           = new ArrayList<>();
    private final WeakConstraints weaks             = new WeakConstraints();
    
    private final AtLeast atLeast                   = new AtLeast();
    private final AtMost atMost                     = new AtMost();
    
    // Constructors
    
    public SmodelsProgram(SmodelsLiteralCache cache) {
        this.cache = cache;
    }
    
    // Collection like methods
    
    public void add(SmodelsRule rule) {
        rules.add(rule);
        
        literals.addAll(rule.getAllLiterals());
        negativeBodyLiterals.addAll(rule.getNegativeBodyLiterals());
    }
    
    public void add(WeakConstraint weak) {
        weaks.add(weak);
//        levels.add(weak.getLevel());
    }    
    
    @Override
    public Iterator<SmodelsRule> iterator() {
        return rules.iterator();
    }
    
    public Set<SmodelsLiteral> literals() {
        return literals;
    }
    
    public Collection<SmodelsLiteral> negativeBodyLiterals() {
        return negativeBodyLiterals;
    }
    
    // Evaluate
    
    public AnswerSets evaluate(int maxAnswerSets) {
        
        SmodelsAlgorithm smodels = new SmodelsAlgorithm(this);        
        
        return smodels.evaluate(maxAnswerSets);
    }
        
    // Computation context acccesors
    
    public AtLeast atLeast() {
        return atLeast;
    }
    
    public AtMost atMost() {
        return atMost;
    }

    boolean hasWeakConstraints() {
        return weaks.isEmpty() == false;
    }

    WeakConstraints getWeakConstraints() {
        return weaks;
    }

    public class AtLeast {

        public void init() {
            for (SmodelsLiteral literal : literals) {
                literal.atLeast().init();
            }

            for (SmodelsRule rule : rules) {
                rule.atLeast().init();
            }
        }

        public void start(SmodelsThreeValueSet val) {
            for (SmodelsLiteral literal : val.getTrue()) {
                literal.atLeast().setTrue();
            }
            
            for (SmodelsLiteral literal : val.getFalse()) {
                literal.atLeast().setFalse();
            }

            for (SmodelsRule rule : rules) {
                rule.atLeast().checkActivity();
            }
        }
        
        public SmodelsThreeValueSet collect() {
            SmodelsThreeValueSet b = new SmodelsThreeValueSet(literals);

            for (SmodelsLiteral literal : literals) {
                
                if (literal.atLeast().isTrue()) {
                    b.addTrue(literal);
                }
                
                if (literal.atLeast().isFalse()) {
                    b.addFalse(literal);
                }
            }

            return b;
        }        
    }
    
    public class AtMost {
        
        public void init() {
            for (SmodelsLiteral literal : literals) {
                literal.atMost().init();
            }

            for (SmodelsRule rule : rules) {
                rule.atMost().init();
            }            
        }        
        
        public void start(SmodelsThreeValueSet val) {
                        
            for (SmodelsLiteral literal : val.getTrue()) {
                literal.atMost().setInAtrue();
            }

            for (SmodelsLiteral literal : val.getFalse()) {
                literal.atMost().setInAfalse();
            }
            
            for (SmodelsRule rule : rules) {
                rule.atMost().checkActivity();
            }            
        }
        
        Set<SmodelsLiteral> collect() {
            Set<SmodelsLiteral> set = new HashSet<>();

            for (SmodelsLiteral literal : literals) {
                if (literal.atMost().isTrue() == false) {
                    set.add(literal);
                }
            }

            return set;
        }           
    }

}