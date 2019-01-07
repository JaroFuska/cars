/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.compilation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class CompiledLiteralCache<L extends CompiledLiteral> implements Iterable<L> {
    
    private final CompiledLiteralFactory<L> factory;
    private final Map<Literal, L> cache = new HashMap<>();
    
    public CompiledLiteralCache(CompiledLiteralFactory<L> builder) {
        this.factory = builder;
    }
    
    public L ensure(Literal literal) {
        L lit = cache.get(literal);
        
        if (lit == null) {
            lit = factory.createLiteral(literal);
            cache.put(literal, lit);
        }
        
        return lit;
    }
    
    CompiledLiteral get(Literal literal) {
        return cache.get(literal);
    }

    @Override
    public Iterator<L> iterator() {
        return cache.values().iterator();
    }
}
