/*
 * Copyright shanki. All rights reserved.
 */
package sk.shanki.lp.exceptions;

import sk.shanki.lp.Variable;

/**
 *
 * @author shanki
 */
public class UnboundVariableException extends Exception {

    private final Variable[] vars;

    public UnboundVariableException(Variable... vars) {
        this.vars   = vars;
    }

    @Override
    public String toString() {
        if (vars.length == 1) {
            return "Variable '" + vars[0] + "' has to be bound.";
        } else {
            StringBuilder sb = new StringBuilder("At least of the variables ");
            
            for (Variable var : vars) {
                sb.append("'");
                sb.append(var);
                sb.append("'");
            }
            
            sb.append(" has to be bound.");
            
            return sb.toString();
        }
    }

}