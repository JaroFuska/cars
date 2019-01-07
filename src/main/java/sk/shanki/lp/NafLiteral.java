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
public interface NafLiteral {

    public Literal getLiteral();
    public Atom getAtom();
	
    public NafLiteral fullySubstitute(Substitution substitution) throws UnboundVariableException;
    public NafLiteral partiallySubstitute(Substitution substitution);
	public boolean willChange(Substitution substitution);	
    
	public NafLiteral rewriteObjectConstantsToIds(ObjectConstantMapping mapping);
    
	public boolean isGround();
    public boolean isSatisfiedIn(AnswerSet answerSet);

	public ProgramPrinter print(ProgramPrinter aThis);

}