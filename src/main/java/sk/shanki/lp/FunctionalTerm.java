/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import java.util.Arrays;
import java.util.Objects;
import sk.shanki.lp.exceptions.UnboundVariableException;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public class FunctionalTerm extends BaseTerm {

    private final String symbol;
    private final Terms terms;

    public FunctionalTerm(String symbol, Term... terms) {
        this.symbol  = symbol;
        this.terms   = new Terms(Arrays.asList(terms));
    }
    
    public FunctionalTerm(String symbol, Terms terms) {
        this.symbol  = symbol;
        this.terms   = terms;
    }


    @Override
    public FunctionalTerm fullySubstitute(Substitution substitution) throws UnboundVariableException {
        return new FunctionalTerm(symbol, terms.fullySubstitute(substitution));
    }
    
    @Override
    public FunctionalTerm partiallySubstitute(Substitution substitution) {
        return new FunctionalTerm(symbol, terms.partiallySubstitute(substitution));
    }    

    @Override
    public boolean isGround() {
        return terms.isGround();
    }
    
    public boolean hasPredicate(String predicate) {
        return this.symbol.equals(predicate);
    }

    public Term getTerm(int index) {
        return terms.get(index);
    }

    @Override
    public Term rewriteIdsToObjectConstants(ObjectConstantMapping mapping) {
		
		ObjectConstant constant = mapping.fromId(this);
		
		if (constant != null) {
			return constant;
		} else {
			return new FunctionalTerm(symbol, terms.rewriteIdsToObjectConstants(mapping));
		}
    }
    
    public Literal unwrap() {
        return (Literal)terms.get(0);
    }       
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.symbol);
        hash = 19 * hash + Objects.hashCode(this.terms);
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
		
        final FunctionalTerm other = (FunctionalTerm) obj;
        
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        
        if (!Objects.equals(this.symbol, other.symbol)) {
            return false;
        }
        return Objects.equals(this.terms, other.terms);
    }

	@Override
    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printTuple(symbol, terms);
    }

    @Override
    public FunctionalTerm rewriteObjectConstantsToIds(ObjectConstantMapping mapping) {
        return new FunctionalTerm(symbol, terms.rewriteObjectConstantsToIds(mapping));
    }

    @Override
    public boolean willChange(Substitution substitution) {
        return terms.willChange(substitution);
    }

	@Override
    public int getPriority() {
        return 12;
    }

	@Override
    public int compareTo(Term element, ObjectConstantMapping mapping) {
        if (element instanceof FunctionalTerm) {
            FunctionalTerm other = (FunctionalTerm)element;
            
            int a = this.symbol.compareTo(other.symbol);
            if (a != 0) {
                return a;
            }
            
            return this.terms.compareTo(other.terms, mapping);
        } else {
            return this.compareToDifferent(element);
        }
    }

    public int getArity() {
        return terms.size();
    }

    public String getSymbol() {
        return symbol;
    }

	@Override
	public boolean unify(Term term, Substitution substitution) {
        if (term instanceof Atom == false) {
            return false;
        }
        
        FunctionalTerm other = (FunctionalTerm)term;

        if (this.symbol.equals(other.symbol) == false) {
            return false;
        }

        return terms.unify(other.terms, substitution);
	}
	
}