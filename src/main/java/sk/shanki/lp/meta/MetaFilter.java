/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.meta;

import sk.shanki.lp.AnswerSet;
import sk.shanki.lp.AnswerSets;
import sk.shanki.lp.Atom;
import sk.shanki.lp.Term;
import sk.shanki.lp.Literal;

/**
 *
 * @author shanki
 */
public class MetaFilter {
    public AnswerSet filterAs(AnswerSet as) {
        AnswerSet filtered = new AnswerSet();
        
        for (Literal lit : as) {
            if (lit instanceof Atom) {
                Atom atom = (Atom) lit;
                
                if (atom.hasPredicate("in_sem")) {
                    Term constant        = atom.getTerm(0);
                    Literal inner           = (Literal)constant;
                    
                    filtered.add(inner);
                }
            }
        }
        
        return filtered;
    }
    
    public AnswerSets filterAss(AnswerSets ass) {
        AnswerSets filtered = new AnswerSets();
        
        for (AnswerSet as : ass) {
            filtered.add(filterAs(as));
        }
        
        return filtered;
    }

}