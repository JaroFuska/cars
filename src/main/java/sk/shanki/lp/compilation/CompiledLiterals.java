/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.compilation;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author shanki
 */
public class CompiledLiterals<L extends CompiledLiteral> extends ArrayList<L> {
    
    public CompiledLiterals(Collection<L> literals) {
        super(literals);
    }
    
    public L getSingleton() {
        return get(0);
    }

}