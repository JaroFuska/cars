/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.util.Collection;
import java.util.HashSet;
import sk.shanki.lp.printing.StandardProgramPrinter;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 * 
 * Mutable
 */
public class AnswerSet extends HashSet<Literal> {
    
    public AnswerSet() {
    }
    
    public AnswerSet(Collection<Literal> literals) {
        super(literals);
    }

    @Override
    public boolean add(Literal literal) {
        if (literal == null) throw new IllegalArgumentException("literal cannot be null");
        
        return super.add(literal);
    }

    AnswerSet rewriteIdsToObjects(ObjectConstantMapping cache) {
        AnswerSet rewritten = new AnswerSet();
        
        for (Literal literal : this) {
            rewritten.add(literal.rewriteIdsToObjectConstants(cache));
        }
        
        return rewritten;
    }

    @Override
    public String toString() {
        return print(new StandardProgramPrinter()).toString();
    }

    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printAnswerSet(this);
    }

    public AnswerSet restrictToLiterals(Collection<Literal> domain) {
        AnswerSet ret = new AnswerSet(this);
        ret.retainAll(domain);
                
        return ret;
    }

}