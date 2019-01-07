/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp;

import sk.shanki.lp.exceptions.UnboundVariableException;

/**
 *
 * @author shanki
 */
public interface Literal extends NafLiteral {

	boolean unify(Literal other, Substitution substitution);
    @Override
    public Literal fullySubstitute(Substitution substitution) throws UnboundVariableException;
    @Override
    public Literal partiallySubstitute(Substitution substitution);

    @Override
    public Literal rewriteObjectConstantsToIds(ObjectConstantMapping mapping);
    public Literal rewriteIdsToObjectConstants(ObjectConstantMapping mapping);

    public Atom wrap(String name);
    public Literal negate();
	public Rule toFactRule();

    public String getType();
    public boolean isOfType(String type);	
    public boolean isOfSymbol(String symbol);
	
}