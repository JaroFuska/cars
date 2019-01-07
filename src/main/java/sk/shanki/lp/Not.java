/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.UnboundVariableException;
import java.util.Objects;
import sk.shanki.lp.printing.ProgramPrinter;
import sk.shanki.lp.printing.StandardProgramPrinter;

/**
 *
 * @author shanki
 */
public class Not implements NafLiteral {

    private final Literal literal;
    
    public Not(Literal literal) {
        this.literal    = literal;
    }

    @Override
    public Literal getLiteral() {
        return literal;
    }
    
    @Override
    public Atom getAtom() {
        return literal.getAtom();
    }    

    @Override
    public boolean isGround() {
        return literal.isGround();
    }
    
    @Override
    public boolean isSatisfiedIn(AnswerSet answerSet) {
        return answerSet.contains(literal) == false;
    }    

    @Override
    public NafLiteral fullySubstitute(Substitution substitution) throws UnboundVariableException {
        return new Not(literal.fullySubstitute(substitution));
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.literal);        
        
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
        final Not other = (Not) obj;
        
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        
        return Objects.equals(this.literal, other.literal);
    }

	@Override
    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printNot(literal);
    }

    @Override
    public Not rewriteObjectConstantsToIds(ObjectConstantMapping mapping) {
        return new Not(literal.rewriteObjectConstantsToIds(mapping));
    }

    @Override
    public Not partiallySubstitute(Substitution substitution) {
        return new Not(literal.partiallySubstitute(substitution));
    }

    @Override
    public boolean willChange(Substitution substitution) {
        return literal.willChange(substitution);
    }

    public Not rewriteIdsToObjects(ObjectConstantMapping mapping) {
        return new Not(literal.rewriteIdsToObjectConstants(mapping));
    }

    @Override
    public String toString() {
        return print(new StandardProgramPrinter()).toString();
    }	

}