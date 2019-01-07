/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.grounder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import sk.shanki.lp.GroundingPredicates;
import sk.shanki.lp.Literals;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.Not;
import sk.shanki.lp.Rule;
import sk.shanki.lp.Substitution;
import sk.shanki.lp.exceptions.UnboundVariableException;
import sk.shanki.lp.NafLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class GroundingRule extends GroundingElement {
    private final Rule original;
    
    private final NafLiterals head;
    
    public GroundingRule(Rule rule, NafLiterals head, Literals posBody, Literals negBody, GroundingPredicates predicates) {
        super(posBody, negBody, predicates);
        this.original   = rule;
        this.head       = head;
    }
    
    // Main

    public NafLiterals getHead() {
        return head;
    }
    
    public boolean isFact() {
        return posBody.isEmpty() && negBody.isEmpty() && head.isSingleton() && head.isGround();
    }
    
    public boolean isGround() {
        return head.isGround() && posBody.isGround() && negBody.isGround();
    }
    
    Rule substitute(Substitution substitution) throws UnboundVariableException {
        NafLiterals nHead   = head.fullySubstitute(substitution);
        Literals nPosBody   = posBody.fullySubstitute(substitution);
        Literals nNegBody   = negBody.fullySubstitute(substitution);
        
        List<NafLiteral> lits = new ArrayList<>();
        for (Literal lit : nPosBody) {
            lits.add(lit);
        }
        for (Literal lit : nNegBody) {
            lits.add(new Not(lit));
        }
        NafLiterals nBody = new NafLiterals(lits);
        
        return new Rule(nHead, nBody);
    }
    
    @Override
    void addInstance(Instances instances, Substitution substitution) throws UnboundVariableException {
        instances.add(this.substitute(substitution), original);
    }
    
    // Other
    
   @Override
    public boolean equals(Object obj) {
        if (obj instanceof GroundingRule == false) {
            return false;
        } else {
            GroundingRule rule = (GroundingRule)obj;
                       
            return  this.head.equals(rule.head) &&
                    this.posBody.equals(rule.posBody) &&
                    this.negBody.equals(rule.negBody) &&
                    this.predicates.equals(rule.predicates);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.head);
        hash = 79 * hash + Objects.hashCode(this.posBody);
        hash = 79 * hash + Objects.hashCode(this.negBody);
        hash = 79 * hash + Objects.hashCode(this.predicates);
        return hash;
    }

    public Set<Literal> getLiterals() {
        Set<Literal> literals = new HashSet<>();
        
        for (NafLiteral nafLiteral : head) {
            literals.add(nafLiteral.getLiteral());
        }
        
        for (Literal literal : posBody) {
            literals.add(literal);
        }
        
        for (Literal literal : negBody) {
            literals.add(literal);
        }
        
        return literals;
    }

    NafLiteral getHeadSingleton() {
        return head.getSingleton();
    }

    Rule toRule() {
        return original;
    }

    @Override
    public String toString() {
        return original.toString();
    }

}