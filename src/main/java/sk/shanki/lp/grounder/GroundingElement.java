/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.grounder;

import java.util.Objects;
import sk.shanki.lp.GroundingPredicates;
import sk.shanki.lp.Literals;
import sk.shanki.lp.ObjectConstantMapping;
import sk.shanki.lp.Substitution;
import sk.shanki.lp.exceptions.UnboundVariableException;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public abstract class GroundingElement {
    protected final Literals posBody;
    protected final Literals negBody;
    protected final GroundingPredicates predicates;
    
    

    public GroundingElement(Literals posBody, Literals negBody, GroundingPredicates predicates) {
        this.posBody    = posBody;
        this.negBody    = negBody;
        this.predicates = predicates;
    }
    
    Instances ground(GroundingDatabase db, final ObjectConstantMapping cache) throws UnboundVariableException {
        Instances instances         = new Instances();
                
        Substitution substitution   = new Substitution();
        
        groundIterate(db, new PositiveBody(posBody), cache, instances, substitution, false);
        
        return instances;          
    }
    
    private void groundIterate(GroundingDatabase db, PositiveBody wBody, ObjectConstantMapping cache, Instances instances, Substitution substitution, boolean newLiteralIsUsed) throws UnboundVariableException {
        
        wBody = wBody.simplify(substitution, db);

        if (wBody == null) {
            return;
        }
        
        if (wBody.isEmpty()) {
            if (predicates.evaluate(substitution, cache)) {
                addInstance(instances, substitution);
             }  
        } else {

            if (newLiteralIsUsed == false && wBody.hasNewLiteralIn(db) == false) {
                return;
            }

            Literal literal = wBody.next();
            GroundingLiterals literals = db.getPotentialLiteralsFor(literal);            
            
            int literalIndex = 0;

            if (newLiteralIsUsed == false && wBody.hasRestNewListeralIn(db) == false ) {
                literalIndex = literals.lastUsed() + 1;
            }

            for (;literalIndex < literals.size(); ++literalIndex) {
                Literal other       = literals.get(literalIndex);

                Substitution sub    = new Substitution(substitution);
                if (literal.unify(other, sub)) {
                    groundIterate(db, wBody, cache, instances, sub, newLiteralIsUsed || literalIndex > literals.lastUsed());
                }
            }          
        }
    }

    abstract void addInstance(Instances instances, Substitution substitution) throws UnboundVariableException;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.posBody);
        hash = 17 * hash + Objects.hashCode(this.negBody);
        hash = 17 * hash + Objects.hashCode(this.predicates);
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
        final GroundingElement other = (GroundingElement) obj;
        if (!Objects.equals(this.posBody, other.posBody)) {
            return false;
        }
        if (!Objects.equals(this.negBody, other.negBody)) {
            return false;
        }
        return Objects.equals(this.predicates, other.predicates);
    }
   
}