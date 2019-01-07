/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.grounder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import sk.shanki.lp.GroundingPredicates;
import sk.shanki.lp.Literals;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.Not;
import sk.shanki.lp.Substitution;
import sk.shanki.lp.Terms;
import sk.shanki.lp.WeakConstraint;
import sk.shanki.lp.exceptions.UnboundVariableException;
import sk.shanki.lp.NafLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class GroundingWeakConstraint extends GroundingElement {
    
    private final BigDecimal weight;
    private final BigDecimal level;
    private final Terms terms;
    
    private int hash;

    public GroundingWeakConstraint(Literals p, Literals negBody, GroundingPredicates g, BigDecimal weight, BigDecimal level, Terms terms) {
        super(p, negBody, g);
        this.weight = weight;
        this.level  = level;
        this.terms  = terms;
        
        updateHashCode();
    }
    
    private void updateHashCode() {
        hash = 7;
        hash = 13 * hash + Objects.hashCode(this.weight);
        hash = 13 * hash + Objects.hashCode(this.level);
        hash = 13 * hash + Objects.hashCode(this.terms);        
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GroundingWeakConstraint other = (GroundingWeakConstraint) obj;
        if (!Objects.equals(this.weight, other.weight)) {
            return false;
        }
        if (!Objects.equals(this.level, other.level)) {
            return false;
        }
        return Objects.equals(this.terms, other.terms);
    }

    @Override
    void addInstance(Instances instances, Substitution substitution) throws UnboundVariableException {
        instances.add(this.substitute(substitution));
    }

    private WeakConstraint substitute(Substitution substitution) throws UnboundVariableException {
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
        
        Terms nTerms        = terms.fullySubstitute(substitution);
        
        return new WeakConstraint(nBody, weight, level, nTerms);
    }

}