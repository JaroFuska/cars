/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.disjunction;

import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.Constraint;
import sk.shanki.lp.NafLiterals;
import sk.shanki.lp.Not;
import sk.shanki.lp.Rule;
import sk.shanki.lp.compilation.CompiledLiteral;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class DisLiteral extends CompiledLiteral<DisRule>{

    public DisLiteral(Literal literal) {
        super(literal);
    }

    Literal getOriginal() {
        return literal;
    }

    Literal prime() {
        return literal.wrap("prime");
    }

    Rule g1b() {
        Literal gHead      = prime();
        NafLiterals gBody   = new NafLiterals(new Not(literal));
        
        return new Rule(gHead, gBody);
    }

    Literal support() {
        return literal.wrap("support");
    }

    Constraint suppb() {
        return new Constraint(new NafLiterals(literal, new Not(support())));
    }

    boolean isIn(AnswerSet m) {
        return m.contains(literal);
    }

    Literal over() {
        return literal.wrap("over");
    }

    Rule t2() {
        Literal nHead = this.over();
        NafLiterals nBody = new NafLiterals(new Not(literal));
        
        return new Rule(nHead, nBody);
    }

}