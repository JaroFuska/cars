/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.gsolver;

import sk.shanki.lp.compilation.CompiledLiteral;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import sk.shanki.lp.Atom;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.Rule;
import sk.shanki.lp.NafLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class GLiteral extends CompiledLiteral<GRule> {

    public GLiteral(Literal literal) {
        super(literal);
    }
    
    
    public Literal toLit() {
        return literal.wrap("lit");
    }
    
    public Literal toNot() {
        return literal.wrap("litnot");
    }    
    
    public Literal toLitPrime() {
        return literal.wrap("litprime");
    }

    public Literal toNotPrime() {
        return literal.wrap("litnotprime");
    }
    
    public Rule toRule4() {
        Literal head    = this.toNot();
        
        ArrayList<NafLiteral> bodyLits = new ArrayList<>();
        for (GRule rule : inHead) {
            bodyLits.add(rule.toNegApp());
        }
        
        NafLiterals body = new NafLiterals(bodyLits);

        return new Rule(head, body);
    }    

    public Rule toRulePrime4() {
        Literal head    = this.toNotPrime();
        
        ArrayList<NafLiteral> bodyLits = new ArrayList<>();
        for (GRule rule : inHead) {
            bodyLits.add(rule.toNegAppPrime());
        }

        NafLiterals body = new NafLiterals(bodyLits);
        
        return new Rule(head, body);
    } 

    public Set<Rule> toRuleSaturation() {
        Set<Rule> rules = new HashSet<>();
        
        rules.add(new Rule(this.toLitPrime(), new NafLiterals(new Atom("allok"))));
        rules.add(new Rule(this.toNotPrime(), new NafLiterals(new Atom("allok"))));
        
        return rules;
        
    }    

}