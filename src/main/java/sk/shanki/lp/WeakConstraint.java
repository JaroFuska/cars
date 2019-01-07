/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import sk.shanki.lp.predicates.GroundingPredicateFactory;
import sk.shanki.lp.grounder.GroundingWeakConstraint;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public class WeakConstraint {
    private final NafLiterals body;
    private final BigDecimal weight;
    private final BigDecimal level;
    private final Terms terms;
    
    public WeakConstraint(NafLiterals body, BigDecimal weight, BigDecimal level, Terms terms) {
        this.body   = body;
        this.weight = weight;
        this.level  = level;
        this.terms  = terms;
    }
    
    GroundingWeakConstraint toGroundingWeakConstraint(GroundingPredicateFactory factory) {
        Literals posBody = body.getPositiveLiterals();
        Literals negBody = body.getNegativeLiterals();
        
        Literals.Carret split = posBody.split(factory);
        
        return new GroundingWeakConstraint(split.literals, negBody, split.predicates, weight, level, terms);

    }

    Collection<? extends Literal> collectAllLiterals() {
        return body.collectAllLiterals();
    }

    Collection<? extends Atom> collectAllAtoms() {
        return body.collectAllAtoms();
    }   

    public void print(ProgramPrinter printer) {
        printer.printWeakConstraint(body, weight, level, terms);
    }

    WeakConstraint rewriteElementsToIds(ObjectConstantMapping mapping) {
        return new WeakConstraint(
                body.rewriteElementsToIds(mapping),
                weight,
                level,
                terms.rewriteObjectConstantsToIds(mapping));
    }

    BigDecimal getLevel() {
        return level;
    }
    
    WeakConstraintTuple toTuple() {
        return new WeakConstraintTuple(weight, terms);
    }    
    
    boolean isViolatedIn(AnswerSet as) {
        return body.isSatisfiedIn(as);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.body);
        hash = 43 * hash + Objects.hashCode(this.weight);
        hash = 43 * hash + Objects.hashCode(this.level);
        hash = 43 * hash + Objects.hashCode(this.terms);
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
        final WeakConstraint other = (WeakConstraint) obj;
        if (!Objects.equals(this.body, other.body)) {
            return false;
        }
        if (!Objects.equals(this.weight, other.weight)) {
            return false;
        }
        if (!Objects.equals(this.level, other.level)) {
            return false;
        }
        if (!Objects.equals(this.terms, other.terms)) {
            return false;
        }
        return true;
    }
    
    
    
}