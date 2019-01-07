/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.printing.StandardProgramPrinter;

/**
 *
 * @author shanki
 */
public abstract class BaseTerm implements Term {
    
    @Override
    public String toString() {
        return print(new StandardProgramPrinter()).toString();
    }
    
    public int compareToDifferent(Term element) {
        return Integer.compare(getPriority(), element.getPriority());
    }

}