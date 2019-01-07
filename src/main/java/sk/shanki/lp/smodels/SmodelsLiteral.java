/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.smodels;

import java.util.ArrayList;
import java.util.List;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class SmodelsLiteral {
    private final Literal literal;
    
    private final List<SmodelsRule> inHead       = new ArrayList<>();
    
    private final List<SmodelsRule> inPosBody    = new ArrayList<>();
    private final List<SmodelsRule> inNegBody    = new ArrayList<>();
    
    private final AtLeast atLeast                 = new AtLeast();
    private final AtMost atMost                   = new AtMost();
    
    // Constructors
    
    SmodelsLiteral(Literal literal) {
        this.literal = literal;
    }
    
    // main methods
    
    void addInHead(SmodelsRule rule) {
        inHead.add(rule);
    }

    void addInPosBody(SmodelsRule rule) {
        inPosBody.add(rule);
    }

    void addInNegBody(SmodelsRule rule) {
        inNegBody.add(rule);
    }

    Literal getLiteral() {
        return literal;
    }
    
    // other

    @Override
    public String toString() {
        return literal.toString();
    }
    
    // Computation context acccesors
    
    public AtLeast atLeast() {
        return atLeast;
    }
    
    public AtMost atMost() {
        return atMost;
    }    
    
    public class AtLeast {
        private boolean isTrue;
        private boolean isFalse;        
        private int notFalseRules;

        void init() {
            notFalseRules   = inHead.size();
            isTrue          = false;
            isFalse         = false;
        }
        
        void setTrue() {
            if (isTrue) {
                return;
            }
            
            isTrue = true;
            
            for (SmodelsRule rule : inPosBody) {
                rule.atLeast().posLiteralMadeTrue();
            }

            for (SmodelsRule rule : inNegBody) {
                rule.atLeast().negLiteralMadeTrue();
            }
            
            backPropagateTrue();
        }
        
        void setFalse() {
            if (isFalse) {
                return;
            }
            
            isFalse = true;
            
            for (SmodelsRule rule : inPosBody) {
                rule.atLeast().posLiteralMadeFalse();
            }

            for (SmodelsRule rule : inNegBody) {
                rule.atLeast().negLiteralMadeFalse();
            }
            
            backPropagateFalse();
        }
        
        boolean isTrue() {
            return isTrue;
        }
    
        boolean isFalse() {
            return isFalse;
        }        

        void derivingRuleMadeInactive() {
            --notFalseRules;

            if (notFalseRules == 0) {
                setFalse();
            }

            backPropagateTrue();
        }

        private void backPropagateTrue() {
            if (isTrue && notFalseRules == 1) {

                for (SmodelsRule rule : inHead) {
                    if (rule.atLeast().hasFalseBody() == false) {
                        rule.atLeast().makeBodyTrue();
                    }
                }

            }
        }

        private void backPropagateFalse() {
            if (isFalse) {
                for (SmodelsRule rule : inHead) {
                    rule.atLeast().backPropagateFalse();
                }
            }
        }
    }
    
    public class AtMost {
        private boolean isTrue;
        private boolean inAFalse;
        
        void init() {
            isTrue = false;
            inAFalse = false;
        }

        void setTrue() {
            if (isTrue) {
                return;
                
            }
            if (inAFalse) {
                return;
            }
            
            isTrue = true;
            
            for (SmodelsRule rule : inPosBody) {
                rule.atMost().posLiteralMadeTrue();
            }
        }
        
        boolean isTrue() {
            return isTrue;
        }

        void setInAfalse() {
            inAFalse = true;
            
            for (SmodelsRule rule : inPosBody) {
                rule.atMost().setPosBodyFalseInA();
            }
        }

        void setInAtrue() {
            for (SmodelsRule rule : inNegBody) {
                rule.atMost().setNegBodyFalseInA();
            }
        }
    }

}