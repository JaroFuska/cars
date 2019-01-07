/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import sk.shanki.lp.printing.StandardProgramPrinter;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public class Constraints extends ArrayList<Constraint> {
        
    public Constraints() {
    }    

    public Constraints(Collection<Constraint> constraints) {
        super(constraints);
    }

    Rules toRules() {
        Rules rules = new Rules();
        
        for (Constraint constraint : this) {
            rules.add(constraint.toRule());
        }
        
        return rules;
    }

    Collection<Atom> collectAllAtoms() {
        Set<Atom> atoms = new HashSet<>();

        for (Constraint constraint : this) {
            atoms.addAll(constraint.collectAllAtoms());
        }
        

        return atoms;
    }

    @Override
    public String toString() {
        return print(new StandardProgramPrinter()).toString();
    }

    Collection<? extends Literal> collectAllLiterals() {
        Set<Literal> literals = new HashSet<>();
        
        for (Constraint constraint : this) {
            literals.addAll(constraint.collectAllLiterals());
        }

        return literals;
    }

    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printConstraints(this);
    }
    
    public Constraints rewriteElementsToIds(ObjectConstantMapping mapping) {
        Constraints ret = new Constraints();
        
        for (Constraint constraint : this) {
            ret.add(constraint.rewriteElementsToIds(mapping));
        }
        
        return ret;
    }
}