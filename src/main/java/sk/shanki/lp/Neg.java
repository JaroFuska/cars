/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.UnboundVariableException;
import java.util.Objects;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public class Neg extends BaseLiteral {
    private final Atom atom;

    public Neg(Atom atom) {
        this.atom = atom;
    }

	@Override
    public boolean unify(Literal literal, Substitution substitution) {
        if (literal instanceof Neg == false) {
            return false;
        }
        
        Neg other = (Neg)literal;
        
        return atom.unify((Literal)other.atom, substitution);
    }

    @Override
    public Neg fullySubstitute(Substitution substitution) throws UnboundVariableException {
        return new Neg(atom.fullySubstitute(substitution));
    }
    
    @Override
    public Neg partiallySubstitute(Substitution substitution) {
        return new Neg(atom.partiallySubstitute(substitution)); 
    }    

    @Override
    public String getType() {
        return "-" + atom.getType();
    }

    @Override
    public boolean isGround() {
        return atom.isGround();
    }
    
    @Override
    public Atom getAtom() {
        return atom;
    }

    @Override
    public Neg rewriteIdsToObjectConstants(ObjectConstantMapping cache) {
        return new Neg(atom.rewriteIdsToObjectConstants(cache));
    }

    @Override
    public Atom negate() {
        return atom;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.atom);        
        
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
        final Neg other = (Neg) obj;
        
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        
        return Objects.equals(this.atom, other.atom);
    }

	@Override
    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printNeg(atom);
    }

    @Override
    public Neg rewriteObjectConstantsToIds(ObjectConstantMapping mapping) {
        return new Neg(atom.rewriteObjectConstantsToIds(mapping));
    }

    @Override
    public boolean willChange(Substitution substitution) {
        return atom.willChange(substitution);
    }

    @Override
    public boolean isOfSymbol(String predicateName) {
        return atom.isOfSymbol(predicateName);
    }
}