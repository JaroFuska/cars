/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.smodels;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class SmodelsLiteralCache implements Iterable<SmodelsLiteral> {
    
    private final Map<Literal, SmodelsLiteral> cache = new HashMap<>();
    
    public SmodelsLiteral ensure(Literal literal) {
        SmodelsLiteral lit = cache.get(literal);
        
        if (lit == null) {
            lit = new SmodelsLiteral(literal);
            cache.put(literal, lit);
        }
        
        return lit;
    }
    
    SmodelsLiteral get(Literal literal) {
        return cache.get(literal);
    }

    @Override
    public Iterator<SmodelsLiteral> iterator() {
        return cache.values().iterator();
    }

}