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
public class WeakConstraints extends ArrayList<WeakConstraint> {
    
    public WeakConstraints() {
    }
    
    public WeakConstraints(Collection<WeakConstraint> weaks) {
        super(weaks);
    }

    Collection<Atom> collectAllAtoms() {
        Set<Atom> atoms = new HashSet<>();

        for (WeakConstraint weak : this) {
            atoms.addAll(weak.collectAllAtoms());
        }

        return atoms;
    }

    Collection<? extends Literal> collectAllLiterals() {
        Set<Literal> literals = new HashSet<>();
        
        for (WeakConstraint w : this) {
            literals.addAll(w.collectAllLiterals());
        }

        return literals;
    }

    @Override
    public String toString() {
        return print(new StandardProgramPrinter()).toString();
    }
    
    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printWeakConstraints(this);
    }
    
    public WeakConstraints rewriteElementsToIds(ObjectConstantMapping mapping) {
        WeakConstraints ret = new WeakConstraints();
        
        for (WeakConstraint weak : this) {
            ret.add(weak.rewriteElementsToIds(mapping));
        }
        
        return ret;
    }
    
    public ViolationDegree computeViolationDegreeOf(AnswerSet as) {
        ViolationDegreeComputer computer = new ViolationDegreeComputer();
        
        for(WeakConstraint weak : this) {
            computer.consume(weak, weak.isViolatedIn(as));
        }
        
        return computer.getValue();
    }

}