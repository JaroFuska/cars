/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.disjunction;

import java.util.HashSet;
import java.util.Set;
import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.Constraint;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.Program;
import sk.shanki.lp.compilation.CompiledLiteralCache;
import sk.shanki.lp.compilation.CompiledProgram;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class DisProgram extends CompiledProgram<DisRule, DisLiteral> {

    public DisProgram(CompiledLiteralCache cache) {
        super(cache);
    }
    
    public Program generator() {
        Program generator = new Program();
        
        Set<DisLiteral> headspd = new HashSet<>();
        
        // G1
        
        for (DisRule rule : this) {
            if (rule.hasDisjunction()) {
                generator.add(rule.g1a());
                generator.add(rule.g1c());
                
                headspd.addAll(rule.getHead());
            } else {
                generator.add(rule.original());
            }
        }
        
        for (DisRule rule : this) {
            generator.add(rule.suppa(headspd));
        }
        
        for (DisLiteral literal : headspd) {
            generator.add(literal.g1b());
            generator.add(literal.suppb());
        }
        
        return generator;
    }
    
    public Program tester(AnswerSet m) {
        Program tester = new Program();
        
        Set<DisLiteral> headspd = new HashSet<>();
        
        for (DisRule rule : this) {
            if (rule.hasDisjunction()) {
                headspd.addAll(rule.getHead());
                
                if (rule.hasBodySatisfiedIn(m)) {
                    tester.add(rule.t1(m));
                    tester.add(rule.t3());
                }
            } else {
                if (rule.hasBodySatisfiedIn(m) && rule.getHead().allIn(m)) {
                    tester.add(rule.t4());
                }
            }
        }
        
        for (DisLiteral literal : headspd) {
            tester.add(literal.t2());
        }
        
        tester.add(this.t5(m));
        
        return tester;
    }

    private Constraint t5(AnswerSet m) {
        NafLiterals body = new NafLiterals();
        for (Literal literal : m) {
            body.add(literal);
        }
        
        return new Constraint(body);
    }

}