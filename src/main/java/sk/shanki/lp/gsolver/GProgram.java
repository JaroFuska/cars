/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.gsolver;

import sk.shanki.lp.compilation.CompiledProgram;
import sk.shanki.lp.compilation.CompiledLiteralCache;
import java.util.ArrayList;
import java.util.Set;
import sk.shanki.lp.Atom;
import sk.shanki.lp.Constraint;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.Not;
import sk.shanki.lp.Program;
import sk.shanki.lp.Rule;
import sk.shanki.lp.NafLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class GProgram extends CompiledProgram<GRule, GLiteral> {

    public GProgram(CompiledLiteralCache cache) {
        super(cache);
    }
    
  public Program transform(Set<Rule> gamma) {
        // we cannot use explicit negation due to saturation
        // we cannot use explicit negation as some counterexample guesses would be discarded if explicitly negated literal are derived (but such counterexamples are legal)
        Program tr = new Program();
        
        for (GRule rule : this) {
            tr.add(rule.toRule0(rule.isIn(gamma)));
            tr.add(rule.toRule1());
            tr.add(rule.toRule2());
            tr.add(rule.toRule3());
            tr.add(rule.toRulex());
        }
        
        tr.add(ruley());
        
        for (GLiteral literal : this.literals()) {
            tr.add(literal.toRule4());
        }

        for (GRule rule : this) {
            tr.add(rule.toRulePrime1());
            tr.add(rule.toRulePrime2());
            tr.add(rule.toRulePrime3());
            tr.add(rule.toRulePrime5());
        }
        
        for (GLiteral literal : this.literals()) {
            tr.add(literal.toRulePrime4());
        }

        for (GRule rule : this) {
            tr.add(rule.toRuleDef1());
            tr.add(rule.toRuleDef2());
            
            tr.add(rule.toRuleDefPrime1());
            
            tr.add(rule.toRuleOk1());
            tr.add(rule.toRuleOk2());
            tr.add(rule.toRuleOk3());
            
            tr.add(rule.toRuleOk4());
        }
        
        tr.add(this.toRuleOk5());
        tr.add(this.toRuleOk6());
        
        for (GRule rule : this) {
            tr.add(rule.toRuleSaturation());
        }
        
        for (GLiteral literal : this.literals()) {
            tr.add(literal.toRuleSaturation());
        }

        return tr;
    }

        
    private Constraint ruley() {
        return new Constraint(new NafLiterals(new Not(new Atom("hasnongen"))));
    }    
    
    private Rule toRuleOk5() {
        
        Literal head    = new Atom("allok");
        ArrayList<NafLiteral> bodyLits   = new ArrayList<>();        
        
        for (GRule rule : this) {
            bodyLits.add(rule.toOk());
        }
        
        NafLiterals body = new NafLiterals(bodyLits);
        
        return new Rule(head, body);
    }
    
    private Constraint toRuleOk6() {
        return new Constraint(new NafLiterals(new Not(new Atom("allok"))));
    }

}