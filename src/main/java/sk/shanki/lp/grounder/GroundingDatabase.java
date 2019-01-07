/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.grounder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import sk.shanki.lp.Rule;
import sk.shanki.lp.NafLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
class GroundingDatabase {

    private final Map<String, GroundingLiterals> db   = new HashMap<>();
    
    // Constructors
    
    // Collection like rules
    
    private GroundingLiterals ensureSet(String signature) {
        GroundingLiterals literals = db.get(signature);
        
        if (literals == null) {
            literals    = new GroundingLiterals(signature);
            db.put(signature, literals);
        }

        return literals;
    }    
    
    boolean add(Literal literal) {
        GroundingLiterals literals = ensureSet(literal.getType());
        
        return literals.add(literal);
    }

    void addAll(Set<Literal> literals) {
        for (Literal literal : literals) {
            add(literal);
        }
    }

    void updateLastUsed() {
        for (GroundingLiterals literals : db.values()) {
            literals.updateLastUsed();
        }
    }

    boolean addHeads(Instances rules) {
        boolean added = false;
        
        for (Rule instance : rules) {
            for (NafLiteral literal : instance.getHead()) {
                added = add(literal.getLiteral()) || added;
            }
        }
        
        return added;
    }

    @Override
    public String toString() {
        return db.toString();
    }
        
    boolean contains(Literal literal) {
        GroundingLiterals lits = ensureSet(literal.getType());

        return lits.contains(literal);
    }

    boolean hasUnusedLiterals(String signature) {
        GroundingLiterals literals = ensureSet(signature);

        return literals.hasUnusedLiterals();
    }

    GroundingLiterals getPotentialLiteralsFor(Literal literal) {
        return ensureSet(literal.getType());
    }

}