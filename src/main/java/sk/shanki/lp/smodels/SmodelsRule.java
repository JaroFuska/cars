/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.smodels;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author shanki
 */
public class SmodelsRule {
    private final SmodelsLiteral head;
    private final List<SmodelsLiteral> positive;
    private final List<SmodelsLiteral> negative;
    
    private final AtLeast atLeast = new AtLeast();
    private final AtMost atMost = new AtMost();
    
    // Constructors
    
    public void wire() {
        head.addInHead(this);

        for (SmodelsLiteral literal : positive) {
            literal.addInPosBody(this);
        }

        for (SmodelsLiteral literal : negative) {
            literal.addInNegBody(this);
        }
    }    
    
    public SmodelsRule(SmodelsLiteral head, List<SmodelsLiteral> positive, List<SmodelsLiteral> negative) {
        this.head       = head;
        this.positive   = positive;
        this.negative   = negative;
    }
    
    Collection<SmodelsLiteral> getAllLiterals() {
        Set<SmodelsLiteral> lits   = new HashSet<>();
        
        lits.add(head);
        lits.addAll(positive);
        lits.addAll(negative);
        
        return lits;
    }
    
    Collection<SmodelsLiteral> getNegativeBodyLiterals() {
        return negative;
    }
        
    // Computation context acccesors
    
    public AtLeast atLeast() {
        return atLeast;
    }
    
    public AtMost atMost() {
        return atMost;
    }
    
    public class AtLeast {
        private int inactiveLiterals;
        private int falseLiterals;
        
        void init() {
            inactiveLiterals = positive.size() + negative.size();
            falseLiterals   = 0;
        }
        
        void posLiteralMadeTrue() {
             decrementInactiveLiterals();
        }
        
        void posLiteralMadeFalse() {
            incrementFalseLiterals();
        }
        
        void negLiteralMadeTrue() {
            incrementFalseLiterals();
        }
        
        void negLiteralMadeFalse() {
            decrementInactiveLiterals();
        }   

        void incrementFalseLiterals() {
            ++falseLiterals;

            if (falseLiterals == 1) {
                head.atLeast().derivingRuleMadeInactive();
            }
        }

        void decrementInactiveLiterals() {
            --inactiveLiterals;

            checkActivity();
        }

        void checkActivity() {

            backPropagateFalse();

            if (inactiveLiterals == 0) {
                head.atLeast().setTrue();
            }
        }


        boolean hasFalseBody() {
            return falseLiterals > 0;
        }

        void makeBodyTrue() {
            for (SmodelsLiteral literal : positive) {
                literal.atLeast().setTrue();
            }

            for (SmodelsLiteral literal : negative) {
                literal.atLeast().setFalse();
            }
        }

        void backPropagateFalse() {
            if (head.atLeast().isFalse() && inactiveLiterals == 1) {
                for (SmodelsLiteral literal : positive) {
                    if (literal.atLeast().isTrue() == false) {
                        literal.atLeast().setFalse();
                    }
                }

                for (SmodelsLiteral literal : negative) {
                    if (literal.atLeast().isFalse() == false) {
                        literal.atLeast().setTrue();
                    }
                }            
            }
        }
    }
    
    public class AtMost {
        private int inactiveposBodyLiterals;
        
        private boolean posBodyFalseInA;
        private boolean negBodyFalseInA;
        
        void init() {
            inactiveposBodyLiterals  = positive.size();
            posBodyFalseInA     = false;
            negBodyFalseInA     = false;
        }
        
        void posLiteralMadeTrue() {
            decrementInactiveLiterals();
        }

        private void decrementInactiveLiterals() {
            --inactiveposBodyLiterals;

            checkActivity();
        }

        void checkActivity() {
            if (inactiveposBodyLiterals == 0 && isBodyFalseInA() == false) {
                head.atMost().setTrue();
            }
        }

        void setPosBodyFalseInA() {
            posBodyFalseInA = true;
        }

        void setNegBodyFalseInA() {
            negBodyFalseInA = true;
        }
        
        boolean isBodyFalseInA() {
            return posBodyFalseInA || negBodyFalseInA;
        }
    }

}