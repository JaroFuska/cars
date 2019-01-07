/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.compilation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import sk.shanki.lp.Preference;
import sk.shanki.lp.Preferences;
import sk.shanki.lp.Rule;

/**
 *
 * @author shanki
 */
public class CompiledProgram<R extends CompiledRule, L extends CompiledLiteral> implements Iterable<R> {
    
    private final CompiledLiteralCache<L> cache;
    private final Map<Rule, R> map      = new HashMap<>();
    

    public CompiledProgram(CompiledLiteralCache cache) {
        this.cache = cache;
    }

    public void add(R rule) {
        map.put(rule.original(), rule);
    }
    
    public void add(Preferences preferences) {
        for (Preference preference : preferences) {
            add(preference);
        }
    }

    public void add(Preference preference) {
        CompiledRule less = map.get(preference.getLess());
        CompiledRule more = map.get(preference.getMore());
        
        more.addLessPreferred(less);
    }

    @Override
    public Iterator<R> iterator() {
        return map.values().iterator();
    }

    public Iterable<L> literals() {
        return cache;
    }

}