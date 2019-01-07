/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.predicates;

import sk.shanki.lp.Atom;
import sk.shanki.lp.GroundingPredicate;
import sk.shanki.lp.Term;
import sk.shanki.lp.NafLiteral;

/**
 *
 * @author shanki
 */
public class GroundingPredicateFactory {
    
    public GroundingPredicate create(NafLiteral naf) {
        
        if (naf instanceof Atom) {
            Atom atom = (Atom)naf;
            
			switch (atom.getArity()) {
				case 1:	return createUnary(atom, atom.getSymbol(), atom.getTerm(0));
				case 2: return createBinary(atom, atom.getSymbol(), atom.getTerm(0), atom.getTerm(1));
				case 3:	return createTernary(atom, atom.getSymbol(), atom.getTerm(0), atom.getTerm(1), atom.getTerm(2));
				default: return null;
			}
        } else {
            return null;
        }
    }

    private GroundingPredicate createBinary(Atom atom, String symbol, Term left, Term right) {
        switch (symbol) {
            case "less": return new Less(left, right);
            case "lesseq": return new LessEq(left, right);
            case "more": return new Greater(left, right);
            case "moreeq": return new GreaterEq(left, right);
            case "eq": return new Eq(left, right);
            case "neq" : return new Neq(left, right);
            case "abs" : return new Abs(left, right);
            case "str_remove" : return new Remove(left, right);
            default: return null;
        }                
                
    }

    private GroundingPredicate createUnary(Atom atom, String symbol, Term term) {
        return null;
    }

    private GroundingPredicate createTernary(Atom atom, String symbol, Term x, Term y, Term right) {
        switch (symbol) {
            case "plus" : return new Plus(x, y, right);
            case "minus" : return new Minus(x, y, right);
            case "str_append" : return new Append(x, y, right);
            case "pow" : return new Pow(x, y, right);
            case "bwand" : return new BitwiseAnd(x, y, right);
            default: return null;
        }
    }

}