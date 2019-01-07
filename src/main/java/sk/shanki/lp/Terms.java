/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.UnboundVariableException;
import java.util.ArrayList;
import java.util.Collection;
import sk.shanki.lp.printing.StandardProgramPrinter;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public class Terms extends ArrayList<Term> {

    public Terms() {
    }
    
    public Terms(Collection<Term> terms) {
        super(terms);
    }

    boolean unify(Terms other, Substitution substitution) {
        if (this.size() != other.size()) {
            return false;
        }
        
        int n = size();
        for (int i = 0; i < n; ++i) {
            Term thisTerm   = this.get(i);
            Term otherTerm  = other.get(i);
                        
            if (thisTerm.unify(otherTerm, substitution) == false) {
                return false;
            }
        }
        
        return true;
    }
    
    boolean isGround() {        
        for (Term term : this) {
            if (term.isGround() == false) {
                return false;
            }
        }
        
        return true;
    }

    public Terms fullySubstitute(Substitution substitution) throws UnboundVariableException {
        if (willChange(substitution) == false) {
            return this;
        }
        
        ArrayList<Term> substituted = new ArrayList<>();
        
        for (Term term : this) {
            substituted.add(term.fullySubstitute(substitution));
        }
        
        return new Terms(substituted);
    }
    
    boolean willChange(Substitution substitution) {
        for (Term term : this) {
            if (term.willChange(substitution)) {
                return true;
            }
        }
        
        return false;
    }

    Terms partiallySubstitute(Substitution substitution) {
        if (willChange(substitution) == false) {
            return this;
        }
        
        ArrayList<Term> substituted = new ArrayList<>();
        
        for (Term term : this) {
            substituted.add(term.partiallySubstitute(substitution));
        }
        
        return new Terms(substituted);
    }

    @Override
    public String toString() {
        return print(new StandardProgramPrinter(), " ").toString();
    }

    Terms rewriteObjectConstantsToIds(ObjectConstantMapping cache) {
        ArrayList<Term> rewritten = new ArrayList<>();
        
        for (Term term : this) {
            rewritten.add(term.rewriteObjectConstantsToIds(cache));
        }
        
        return new Terms(rewritten);
    }
    
    Terms rewriteIdsToObjectConstants(ObjectConstantMapping cache) {
        ArrayList<Term> rewritten = new ArrayList<>();
        
        for (Term term : this) {
            rewritten.add(term.rewriteIdsToObjectConstants(cache));
        }
        
        return new Terms(rewritten);
    }    

    public ProgramPrinter print(ProgramPrinter printer, String separator) {
        boolean isFirst = true;
        
        for (Term term : this) {
            if (isFirst == false) {
                printer.printSeparator(separator);
            } else {
                isFirst = false;
            }
            
            term.print(printer);
        }
        
        return printer;
    }

    int compareTo(Terms other, ObjectConstantMapping mapping) {
        
        int thisSize = this.size();
        int otherSize = other.size();
        
        int a = Integer.compare(thisSize, otherSize);
        if (a != 0) {
            return a;
        }
        
        for (int i = 0; i < thisSize; ++i) {
            Term thisElement = this.get(i);
            Term otherElement = other.get(i);
            
            int b = thisElement.compareTo(otherElement, mapping);
            if (b != 0) {
                return b;
            }
        }
        
        return 0;
    }

}