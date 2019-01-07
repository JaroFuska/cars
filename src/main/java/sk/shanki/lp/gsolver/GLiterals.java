/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.gsolver;

import sk.shanki.lp.compilation.CompiledLiterals;
import java.util.ArrayList;
import java.util.Collection;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.NafLiteral;

/**
 *
 * @author shanki
 */
public class GLiterals extends CompiledLiterals<GLiteral> {

    public GLiterals(Collection<GLiteral> literals) {
        super(literals);
    }
    
    NafLiterals toLit() {
        ArrayList<NafLiteral> lits = new ArrayList<>();
        
        for (GLiteral literal : this) {
            lits.add(literal.toLit());
        }
        
        return new NafLiterals(lits);
    }
    
    NafLiterals toLitPrime() {
        ArrayList<NafLiteral> lits = new ArrayList<>();
        
        for (GLiteral literal : this) {
            lits.add(literal.toLitPrime());
        }
        
        return new NafLiterals(lits);        
    }

}