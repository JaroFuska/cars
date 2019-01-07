/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.compilation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.Rule;

/**
 *
 * @author shanki
 */
public class CompiledRule<R extends CompiledRule, L extends CompiledLiteral, LS extends CompiledLiterals<L>> {
    protected final Rule original;
    
    protected final LS head;
    protected final LS positive;
    protected final LS negative;
    
    protected final Set<R> lessRules  = new HashSet<>();
        
    // Constructors
    
    private void wire() {
        for (L literal : head) {
            literal.addInHead(this);
        }

        for (L literal : positive) {
            literal.addInPosBody(this);
        }

        for (L literal : negative) {
            literal.addInNegBody(this);
        }
    }    
    
    public CompiledRule(Rule original, LS head, LS positive, LS negative) {
        this.original   = original;
        this.head       = head;
        this.positive   = positive;
        this.negative   = negative;
        
        wire();
    }
    
    public LS getHead() {
        return head;
    }
    
    public Collection<CompiledLiteral> getAllLiterals() {
        Set<CompiledLiteral> lits   = new HashSet<>();
        
        lits.addAll(head);
        lits.addAll(positive);
        lits.addAll(negative);
        
        return lits;
    }
    
    public Collection<L> getNegativeBodyLiterals() {
        return negative;
    }    

    public Rule original() {
        return original;
    }

    public void addLessPreferred(R less) {
        if (lessRules.add(less)) {
            // a recursive call should not be needed as we iteratively add preferences
            lessRules.addAll(less.getLessPreferred());
        }
    }

    public Set<R> getLessPreferred() {
        return lessRules;
    }
    
    public boolean isIn(Set<Rule> set) {
        return set.contains(original);
    }
    
    public boolean hasDisjunction() {
        return original.hasDisjunction();
    }
    
    public boolean hasBodySatisfiedIn(AnswerSet m) {
        return original.hasBodySatisfiedIn(m);
    }    

}