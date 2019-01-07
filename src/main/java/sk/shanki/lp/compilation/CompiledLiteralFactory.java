/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.compilation;

import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public interface CompiledLiteralFactory<L> {
	
    public L createLiteral(Literal literal);

}