/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.printing.StandardProgramPrinter;

/**
 *
 * @author shanki
 */
public abstract class BaseLiteral implements Literal {

    @Override
    public Atom wrap(String name) {
        return new Atom(name, new ObjectConstant(this));
    }
    
    @Override
    public Literal getLiteral() {
        return this;
    }
    
    @Override
    public boolean isOfType(String signature) {
        return getType().equals(signature);
    }
    
    @Override
    public Rule toFactRule() {
        return new Rule(this);
    }    
    
    @Override
    public boolean isSatisfiedIn(AnswerSet answerSet) {
        return answerSet.contains(this);
    }

	@Override
	public String toString() {
		return print(new StandardProgramPrinter()).toString();
	}
    
}