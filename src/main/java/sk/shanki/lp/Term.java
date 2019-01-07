/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.UnboundVariableException;
import sk.shanki.lp.printing.ProgramPrinter;

/**
 *
 * @author shanki
 */
public interface Term {
    
    public boolean isGround();

    public boolean unify(Term term, Substitution substitution);
    public Term fullySubstitute(Substitution substitution) throws UnboundVariableException;
    public Term partiallySubstitute(Substitution substitution);
    public boolean willChange(Substitution substitution);
    
    public Term rewriteObjectConstantsToIds(ObjectConstantMapping mapping);
    public Term rewriteIdsToObjectConstants(ObjectConstantMapping mapping);    

    public ProgramPrinter print(ProgramPrinter printer);

    public int getPriority();
    public int compareTo(Term element, ObjectConstantMapping mapping);

}