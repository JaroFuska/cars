/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.parser;

import java.util.HashMap;
import java.util.Map;
import sk.shanki.lp.Rule;

/**
 *
 * @author shanki
 */
public class UniqueRuleNameMap {

    private final Map<String,Rule> map = new HashMap<>();

    public UniqueRuleNameMap(Iterable<Rule> rules) throws MultipleRulesWithSameNameException {
        for (Rule rule : rules) {
            add(rule);
        }
    }

    UniqueRuleNameMap() {
    }
    
    void add(Rule rule) throws MultipleRulesWithSameNameException {
        if (rule.hasName() == false) {
            return;
        }
        
        String name = rule.getName();
        
        Rule existing = map.get(name);
        if (existing != null && existing.equals(rule) == false) {
            throw new MultipleRulesWithSameNameException(rule.getName());
        }
        
        map.put(name, rule);
    }

    Rule get(String name) {
        return map.get(name);
    }

}