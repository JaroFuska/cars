/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.util.Collection;
import java.util.HashSet;
import sk.shanki.lp.exceptions.SolverException;
import sk.shanki.lp.printing.StandardProgramPrinter;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 * 
 * Mutable
 */
public class AnswerSets extends HashSet<AnswerSet> {

    @Override
    public boolean add(AnswerSet answerSet) {
        if (answerSet == null) {
            throw new IllegalArgumentException("answerSet cannot be null");
        }
        
        return super.add(answerSet);
    }

    public AnswerSets rewriteIdsToObjects(ObjectConstantMapping cache) {
        AnswerSets sets = new AnswerSets();
        
        for (AnswerSet as : this) {
            sets.add(as.rewriteIdsToObjects(cache));
        }
        
        return sets;
    }

    @Override
    public String toString() {
        return print(new StandardProgramPrinter()).toString();
    }
    
    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printAnswerSets(this);
    }

    public AnswerSet first() {
        return iterator().next();
    }

    public AnswerSets filter(AnswerSetChecker checker) throws SolverException {
        AnswerSets ret = new AnswerSets();
        
        for (AnswerSet as : this) {
            if (checker.isAnswerSetOk(as)) {
                ret.add(as);
            }
        }
        
        return ret;
    }

    public AnswerSets restrictToLiterals(Collection<Literal> literals) {
        AnswerSets ret = new AnswerSets();
        
        for (AnswerSet as : this) {
            ret.add(as.restrictToLiterals(literals));
        }
        
        return ret;

    }

    public AnswerSets optimal(WeakConstraints weakConstraints) {
        Collector collector = new Collector(weakConstraints, 0);

        for (AnswerSet as : this) {
            collector.add(as);
        }

        return collector.getAnswerSets();
    }

}