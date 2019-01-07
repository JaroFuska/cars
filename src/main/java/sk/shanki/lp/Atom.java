/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.UnboundVariableException;
import java.util.Arrays;
import java.util.Objects;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public class Atom extends BaseLiteral {
    private final String symbol;
    private final Terms terms;

    public Atom(String symbol, Term... terms) {
        this.symbol  = symbol;
        this.terms   = new Terms(Arrays.asList(terms));
    }
    
    public Atom(String symbol, Terms terms) {
        this.symbol  = symbol;
        this.terms   = terms;
    }

	@Override
    public boolean unify(Literal literal, Substitution substitution) {
        
        if (literal instanceof Atom == false) {
            return false;
        }
        
        Atom other = (Atom)literal;

        if (this.symbol.equals(other.symbol) == false) {
            return false;
        }

        return terms.unify(other.terms, substitution);
    }


    @Override
    public Atom fullySubstitute(Substitution substitution) throws UnboundVariableException {
        return new Atom(symbol, terms.fullySubstitute(substitution));
    }
    
    @Override
    public Atom partiallySubstitute(Substitution substitution) {
        return new Atom(symbol, terms.partiallySubstitute(substitution));
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
    public Atom rewriteIdsToObjectConstants(ObjectConstantMapping mapping) {
        return new Atom(symbol, terms.rewriteIdsToObjectConstants(mapping));
    }
    
    public Literal unwrap() {
        return (Literal)terms.get(0);
    }       
    
    @Override
    public String getType() {
        return symbol + "/" + terms.size();
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
        final Atom other = (Atom) obj;
        
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
    public Atom rewriteObjectConstantsToIds(ObjectConstantMapping mapping) {
        return new Atom(symbol, terms.rewriteObjectConstantsToIds(mapping));
    }

    @Override
    public boolean willChange(Substitution substitution) {
        return terms.willChange(substitution);
    }

    public int getArity() {
        return terms.size();
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean isOfSymbol(String predicateName) {
        return symbol.equals(predicateName);
    }
	
   @Override
    public Atom getAtom() {
        return this;
    }

    public Rule toMetaComplement() {
        return new Rule(new Atom("compl", new ObjectConstant(this), new ObjectConstant(this.negate())));
    }    

    @Override
    public Neg negate() {
        return new Neg(this);
    }	

}