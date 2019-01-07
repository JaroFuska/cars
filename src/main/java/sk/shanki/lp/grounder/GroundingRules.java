/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.grounder;

import java.util.ArrayList;
import sk.shanki.lp.ObjectConstantMapping;
import sk.shanki.lp.exceptions.NotSafeException;
import sk.shanki.lp.exceptions.UnboundVariableException;

/**
 *
 * @author shanki
 */
public class GroundingRules {
    private final ArrayList<GroundingRule> rules    = new ArrayList<>();

    void add(GroundingRule rule) {
        rules.add(rule);
    }
    
    GroundingRules nonFacts() {
        GroundingRules ret  = new GroundingRules();
        
        for (GroundingRule rule : rules) {
            if (rule.isFact()== false) {
                ret.add(rule);
            }
        }
        
        return ret;
    }
 
    Instances facts() {
        Instances ground = new Instances();
        
        for (GroundingRule rule : rules) {
            if (rule.isFact()) {
                ground.add(rule.toRule());
            }
        }
        
        return ground;
    }
    
    Instances groundInstances(GroundingDatabase db, ObjectConstantMapping cache) throws NotSafeException {
       final Instances instances            = new Instances();
        
        for (GroundingRule rule : rules) {
            try {
                instances.addAll(rule.ground(db, cache));
            } catch (UnboundVariableException ex) {
                throw new NotSafeException(rule, ex);
            }
        }        
        
        return instances;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        for (GroundingRule rule : rules) {
            sb.append(rule.toString());
        }
        
        return sb.toString();
    }

}