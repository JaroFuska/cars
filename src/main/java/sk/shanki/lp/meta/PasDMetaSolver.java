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
public class PasDMetaSolver extends BaseGroundMetaSolver {
    
    public PasDMetaSolver(Solver solver) {
        super(PasDMetaInterpreter, solver);
    }
    
    private static final Program PasDMetaInterpreter;
    private static final String raw = "" +
        "pr(R1, R3) :- pr(R1, R2), pr(R2, R3)." +
        ":- pr(R1, R2), pr(R2, R1)." +
        "compl(X, Y) :- compl(Y,X)." +
        ":- compl(X,Y), in_sem(X), in_sem(Y)." +
        "in_sem(X) :- q(R), head(X,R)." +
        "conflicts(R1,R2) :- nbl(X,R1), head(X,R2), nbl(Y,R2), head(Y,R1)." +
        "neg_dangerous(R1,R2) :- conflicts(R1,R2), pr(R2,R1)." +
        "neg_reduct(R1) :- rule(R1), q(R2), head(X,R2), nbl(X,R1), not neg_dangerous(R2, R1)." +
        "reduct(R) :- rule(R), not neg_reduct(R)." +
        "neg_empty_pos_body(R) :- pbl(X,R)." +
        "neg_pbl_last(X,R) :- pbl(X,R), pbl(Y,R), less(X, Y)." +
        "pbl_last(X,R) :- pbl(X,R), not neg_pbl_last(X,R)." +
        "neg_pbl_first(X,R) :- pbl(X,R), pbl(Y,R), less(Y, X)." +
        "pbl_first(X,R) :- pbl(X,R), not neg_pbl_first(X,R)." +
        "pbl_inbetween(X,Y,R) :- pbl(X,R), pbl(Y,R), pbl(Z,R), less(X, Z), less(Z, Y)." +
        "pos_body_true(R) :- rule(R), not neg_empty_pos_body(R)." +
        "pos_body_true_upto(R,X) :- pbl_first(X,R), in_sem(X)." +
        "pos_body_true_upto(R,X) :- pos_body_true_upto(R,Y), pbl(X,R), in_sem(X), less(Y, X), not pbl_inbetween(Y,X,R)." +
        "pos_body_true(R) :- pos_body_true_upto(R,X), pbl_last(X,R)." +
        "q(R) :- reduct(R), pos_body_true(R).";

    static {
        try {
            PasDMetaInterpreter = new ProgramFactory().fromString(raw);
        } catch (SemanticException | IOException ex) {
            throw new RuntimeException("Error", ex);
        }
    }

}