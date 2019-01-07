/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.disjunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.Constraint;
import sk.shanki.lp.Inconsistency;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.Not;
import sk.shanki.lp.Rule;
import sk.shanki.lp.Rules;
import sk.shanki.lp.compilation.CompiledRule;
import sk.shanki.lp.NafLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class DisRule extends CompiledRule<DisRule,DisLiteral,DisLiterals> {

    public DisRule(Rule original, DisLiterals head, DisLiterals positive, DisLiterals negative) {
        super(original, head, positive, negative);
    }

    Rules g1a() {
        Rules generator = new Rules();
        
        for (DisLiteral a : head) {
            generator.add(g1a(a));
        }

        return generator;
    }

    private Rule g1a(DisLiteral a) {
        Literal gHead      = a.getOriginal();
        NafLiterals gBody   = new NafLiterals(original.body());
        gBody.add(new Not(a.prime()));
        
        return new Rule(gHead, gBody);
    }

    public Rule g1c() {
        Literal gHead      = Inconsistency.getInstance();
        
        ArrayList<NafLiteral> lits = new ArrayList<>();
        
        lits.add(new Not(gHead));
        
        for (DisLiteral lit : head) {
            lits.add(new Not(lit.getOriginal()));
        }
        
        for (NafLiteral lit : original.body()) {
            lits.add(lit);
        }
        
        NafLiterals gBody   = new NafLiterals(lits);
        
        return new Rule(gHead, gBody);
    }

    Rules suppa(Set<DisLiteral> headspd) {
        Rules support = new Rules();
        
        for (DisLiteral a : head) {
            if (headspd.contains(a)) {
                support.add(suppa(a));
            }
        }
        
        return support;
    }

    private Rule suppa(DisLiteral a) {
        Literal sHead = a.support();
        
        List<NafLiteral> lits = new ArrayList<>();
        
        for (DisLiteral lit : head) {
            if (lit != a) {
                lits.add(new Not(lit.getOriginal()));
            }
        }
        
        for (NafLiteral lit : original.body()) {
            lits.add(lit);
        }
        
        NafLiterals sBody = new NafLiterals(lits);
        
        return new Rule(sHead, sBody);
    }

    Rules t1(AnswerSet m) {
        Rules rules = new Rules();
        
        for (DisLiteral a : head) {
            if (a.isIn(m)) {
                rules.add(t1(a));
            }
        }
        
        return rules;
    }

    private Rule t1(DisLiteral a) {
        Literal nHead = a.getOriginal();
        NafLiterals nBody = original.body().getPositive();
        nBody.add(new Not(a.over()));
        
        return new Rule(nHead, nBody);
    }

    Constraint t3() {
        NafLiterals nBody = original.body().getPositive();
        
        for (DisLiteral a : head) {
            nBody.add(new Not(a.getOriginal()));
        }
        
        return new Constraint(nBody);
    }

    Rule t4() {
        NafLiterals nHead = original.getHead();
        NafLiterals nBody = original.body().getPositive();
        
        return new Rule(nHead, nBody);
    }

}