/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.grounder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import sk.shanki.lp.Literals;
import sk.shanki.lp.Substitution;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class PositiveBody implements Iterable<Literal> {
    private List<Literal> lts;
        
    public PositiveBody(Literals literals) {
        this.lts   = new ArrayList<>();
        
        for (Literal literal : literals) {
            this.lts.add(literal);
        }
    }
    
    private PositiveBody(List<Literal> literals) {
        this.lts   = literals;
    }
    
    @Override
    public Iterator<Literal> iterator() {
        return lts.iterator();
    }

    
    public PositiveBody simplify(Substitution substitution, GroundingDatabase db) {
        ArrayList<Literal> substituted = new ArrayList<>();
        
        for (Literal literal : lts) {
            substituted.add(literal.partiallySubstitute(substitution));
        }
        
        Iterator<Literal> it = substituted.iterator();

        while(it.hasNext()) {
            Literal literal = it.next();
            
            if (literal.isGround()) {                
                if (db.contains(literal)) {
                    it.remove();
                } else {
                    return null;
                }
            }
            
        }
        
        return new PositiveBody(substituted);
    }
    
    boolean hasNewLiteralIn(GroundingDatabase db) {
       for (Literal literal : lts) {

            if (db.hasUnusedLiterals(literal.getType())) {
                return true;
            }
        }
        
        return false;
    }
    
    boolean hasRestNewListeralIn(GroundingDatabase db) {
        int size = lts.size();
        for (int i = 1; i < size; ++i) {
            if (db.hasUnusedLiterals(lts.get(i).getType())) {
                return true;
            }
        }
        
        return false;
    }
    
    public int size() {
        return lts.size();
    }
    
    public boolean isSingleton() {
        return size() == 1;
    }    

    @Override
    public String toString() {
        return lts.toString();
    }

    boolean isEmpty() {
        return lts.isEmpty();
    }

    Literal next() {
        return lts.get(0);
    }

}