/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.grounder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import sk.shanki.lp.NafLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class GroundingLiterals {
    private final String signature;
    
    private final Set<Literal> literals;
    private final ArrayList<Literal> array;
    
    private int lastUsed;
    
    public GroundingLiterals(String signature) {
        this.signature  = signature;
        this.literals   = new HashSet<>();
        array           = new ArrayList<>();
        lastUsed        = -1;
    }
    
    public void updateLastUsed() {
        lastUsed    = array.size() - 1;
    }
    
    public boolean addHeads(Iterable<GroundingRule> rules) {
        boolean newLiteral  = false;

        for (GroundingRule rule : rules) {
            for (NafLiteral nafLiteral : rule.getHead()) {
                
                if (nafLiteral instanceof Literal) {
                    newLiteral = newLiteral | add((Literal)nafLiteral); // we need non-lazy OR here
                }
            }
        }
        
        return newLiteral;
    }

    boolean add(Literal literal) {
        if (literal.isOfType(signature) == false) {
            return false;
        }
        
        boolean added = literals.add(literal);
        if (added) {
            array.add(literal);
        }

        return added;
    }

    boolean contains(Literal literal) {
        return literals.contains(literal);
    }

    int lastUsed() {
        return lastUsed;
    }

    int size() {
        return literals.size();
    }

    Literal get(int index) {
        return array.get(index);
    }

    boolean hasUnusedLiterals() {
        return lastUsed < size() - 1;
    }

    @Override
    public String toString() {
        return literals.toString();
    }
    
}