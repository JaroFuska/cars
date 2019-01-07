/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.grounder;

import java.util.ArrayList;
import java.util.List;
import sk.shanki.lp.Constraint;
import sk.shanki.lp.GroundingPredicates;
import sk.shanki.lp.Literals;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.Not;
import sk.shanki.lp.Substitution;
import sk.shanki.lp.exceptions.UnboundVariableException;
import sk.shanki.lp.NafLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class GroundingConstraint extends GroundingElement {
    
    public GroundingConstraint(Literals posBody, Literals negBody, GroundingPredicates predicates) {
        super(posBody, negBody, predicates);
    }
 
    @Override
    void addInstance(Instances instances, Substitution substitution) throws UnboundVariableException {
        instances.add(this.substitute(substitution));
    }

    private Constraint substitute(Substitution substitution) throws UnboundVariableException {
        Literals nPosBody   = posBody.fullySubstitute(substitution);
        Literals nNegBody   = negBody.fullySubstitute(substitution);
        
        List<NafLiteral> lits = new ArrayList<>();
        for (Literal lit : nPosBody) {
            lits.add(lit);
        }
        for (Literal lit : nNegBody) {
            lits.add(new Not(lit));
        }
        NafLiterals nBody = new NafLiterals(lits);
        
        return new Constraint(nBody);

    }

    @Override
    public String toString() {
        return "";
    }

}