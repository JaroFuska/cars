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
public class Variable extends BaseTerm {

    private final String name;
    
    public Variable(String name) {
        this.name   = name;
    }

    @Override
    public boolean unify(Term other, Substitution substitution) {
        return substitution.put(this, other);
    }

    @Override
    public Variable rewriteObjectConstantsToIds(ObjectConstantMapping cache) {
        return this;
    }
    
    @Override
    public Variable rewriteIdsToObjectConstants(ObjectConstantMapping mapping) {
        return this;
    }

    @Override
    public boolean isGround() {
        return false;
    }

    @Override
    public Term fullySubstitute(Substitution substitution) throws UnboundVariableException {
        Term term = substitution.get(this);
        
        if (term == null) {
            throw new UnboundVariableException(this);
        } else {
            return term;
        }
    }

    @Override
    public Term partiallySubstitute(Substitution substitution) {
        Term term = substitution.get(this);
        
        if (term == null) {
            return new Variable(name);
        } else {
            return term;
        }
    }

    @Override
    public boolean willChange(Substitution substitution) {
        // vzdy vratime kopiu, kvoli kontextom, v ktorych sa premenne pouzivaju (potrebne pri unifikovani)
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.name);
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
        final Variable other = (Variable) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public ProgramPrinter print(ProgramPrinter printer) {
        return printer.printVariable(name);
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public int compareTo(Term element, ObjectConstantMapping mapping) {
        if (element instanceof Variable) {
            Variable other = (Variable)element;
            
            return this.name.compareTo(other.name);
        } else {
            return compareToDifferent(element);
        }
    }    
    
}