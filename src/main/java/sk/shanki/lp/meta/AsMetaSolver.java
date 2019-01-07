/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.meta;

import java.io.IOException;
import sk.shanki.lp.Program;
import sk.shanki.lp.parser.ProgramFactory;
import sk.shanki.lp.solvers.Solver;
import sk.shanki.lp.exceptions.SemanticException;

/**
 *
 * @author shanki
 */
public class AsMetaSolver extends BaseGroundMetaSolver {
        
    public AsMetaSolver(Solver solver) {
        
        super(interpreter, solver);
    }
    
    private static final Program interpreter;
    private static final String raw = "" +
            "in_sem(X) :- head(X,R), pos_body_true(R), not neg_body_false(R)." +
            "pos_body_exists(R) :- pbl(X,R)." +
            "pos_body_true(R) :- rule(R), not pos_body_exists(R)." + 
            "pbl_inbetween(X,Y,R) :- pbl(X,R), pbl(Y,R), pbl(Z,R), less(X, Z), less(Z, Y)." +
            "pbl_notlast(X,R) :- pbl(X,R), pbl(Y,R), less(X, Y)." +
            "pbl_notfirst(X,R) :- pbl(X,R), pbl(Y,R), less(Y, X)." +
            "pos_body_true_upto(R,X) :- pbl(X,R), not pbl_notfirst(X,R), in_sem(X)." +
            "pos_body_true_upto(R,X) :- pos_body_true_upto(R,Y), pbl(X,R), in_sem(X), less(Y, X), not pbl_inbetween(Y,X,R)." +
            "pos_body_true(R) :- pos_body_true_upto(R,X), not pbl_notlast(X,R)." +
            "neg_body_false(R) :- nbl(X,R), in_sem(X)." +
            ":- compl(X,Y), in_sem(X), in_sem(Y).";
    
    static {
        try {
            interpreter = new ProgramFactory().fromString(raw);
        } catch (SemanticException | IOException ex) {
            throw new RuntimeException("This should not happen. There is a bug in a program.", ex);
        }
    }

}