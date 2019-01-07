/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.util.Collection;
import java.util.Objects;
import sk.shanki.lp.predicates.GroundingPredicateFactory;
import sk.shanki.lp.grounder.GroundingConstraint;
import sk.shanki.lp.printing.StandardProgramPrinter;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public class Constraint {

    static Constraint forNegation(Atom tuple) {
        NafLiterals body = new NafLiterals(tuple, tuple.negate());
                
        return new Constraint(body);
    }
    
    private final NafLiterals body;
    
    public Constraint(NafLiterals body) {
        this.body = body;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.body);
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
        final Constraint other = (Constraint) obj;
        return Objects.equals(this.body, other.body);
    }

    Rule toRule() {
        NafLiterals newHead = new NafLiterals(Inconsistency.getInstance());
        NafLiterals newBody = new NafLiterals(body);
        newBody.add(new Not(Inconsistency.getInstance()));

        return new Rule(newHead, newBody);
    }
    
    GroundingConstraint toGroundingConstraint(GroundingPredicateFactory factory) {
        Literals posBody = body.getPositiveLiterals();
        Literals negBody = body.getNegativeLiterals();
        
        Literals.Carret split = posBody.split(factory);
        
        return new GroundingConstraint(split.literals, negBody, split.predicates);
    }    

    Collection<? extends Literal> collectAllLiterals() {
        return body.collectAllLiterals();
    }

    Collection<? extends Atom> collectAllAtoms() {
        return body.collectAllAtoms();
    }

    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printConstraint(body);
    }

    @Override
    public String toString() {
        return print(new StandardProgramPrinter()).toString();
    }
    
    Constraint rewriteElementsToIds(ObjectConstantMapping mapping) {
        return new Constraint(body.rewriteElementsToIds(mapping));
    }
}
