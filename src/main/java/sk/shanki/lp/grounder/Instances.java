/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.grounder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import sk.shanki.lp.Constraint;
import sk.shanki.lp.Constraints;
import sk.shanki.lp.Preference;
import sk.shanki.lp.Preferences;
import sk.shanki.lp.Program;
import sk.shanki.lp.Rule;
import sk.shanki.lp.Rules;
import sk.shanki.lp.WeakConstraint;
import sk.shanki.lp.WeakConstraints;

/**
 *
 * @author shanki
 */
public class Instances implements Iterable<Rule> {
    
    private final Map<Rule, Set<Rule>> instances    = new HashMap<>();
    private final Set<Rule> allInstances            = new HashSet<>();
    private final Set<Constraint> constraints       = new HashSet<>();
    private final Set<WeakConstraint> weaks         = new HashSet<>();

    Program toProgram() {
        //TODO toto by uz malo asi davat aj zagroundovane preferencie
        return new Program(new Rules(allInstances), new Constraints(constraints), new WeakConstraints(weaks), new Preferences());
    }

    @Override
    public Iterator<Rule> iterator() {
        return allInstances.iterator();
    }

    void addAll(Instances other) {
        for (Rule original : other.instances.keySet()) {
            Set<Rule> rules = ensureSet(original);
            rules.addAll(other.instances.get(original));
        }
        
        allInstances.addAll(other.allInstances);
        constraints.addAll(other.constraints);
        weaks.addAll(other.weaks);
    }

    private Set<Rule> ensureSet(Rule original) {
        Set<Rule> set = instances.get(original);
        if (set == null) {
            set = new HashSet<>();
            instances.put(original, set);
        }
        
        return set;
    }
    
    void add(Rule instance, Rule original) {
        ensureSet(original).add(instance);
        allInstances.add(instance);
    }

    Preferences groundPreferences(Preferences preferences) {
        
        Set<Preference> ground = new HashSet<>();
        
        for (Preference preference : preferences) {
            Set<Rule> lessInstances = ensureSet(preference.getLess());
            Set<Rule> moreInstances = ensureSet(preference.getMore());
            
            for (Rule less : lessInstances) {
                for (Rule more : moreInstances) {
                    ground.add(new Preference(less, more));
                }
            }
        }
        
        return new Preferences(ground);
    }

    @Override
    public String toString() {
        return allInstances.toString();
    }

    void add(Rule instance) {
        add(instance, instance);
    }
    
    void add(Constraint constraint) {
        constraints.add(constraint);
    }
    
    void add(WeakConstraint weak) {
        weaks.add(weak);
    }

}