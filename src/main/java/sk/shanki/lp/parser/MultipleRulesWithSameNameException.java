/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.parser;

import sk.shanki.lp.exceptions.SemanticException;

/**
 *
 * @author shanki
 */
public class MultipleRulesWithSameNameException extends SemanticException {

    public MultipleRulesWithSameNameException(String name) {
        super("Multiple rules with the same name: " + name);
    }
    
}
