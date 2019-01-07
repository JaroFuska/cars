/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.exceptions;

import sk.shanki.lp.grounder.GroundingRule;

/**
 *
 * @author shanki
 */
public class RuleNotGroundException extends Exception {

    private GroundingRule rule;
    
    public RuleNotGroundException(GroundingRule rule) {
        this.rule = rule;
    }

    public RuleNotGroundException() {
    }

    @Override
    public String toString() {
        return "Rule '" + rule + "' is not ground";
    }

}