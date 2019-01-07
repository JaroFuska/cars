/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.disjunction;

import java.util.Collection;
import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.compilation.CompiledLiterals;

/**
 *
 * @author shanki
 */
public class DisLiterals extends CompiledLiterals<DisLiteral> {

    public DisLiterals(Collection<DisLiteral> literals) {
        super(literals);
    }

    boolean allIn(AnswerSet m) {
        for (DisLiteral literal : this) {
            if (literal.isIn(m) == false) {
                return false;
            }
        }
        
        return true;
    }

}