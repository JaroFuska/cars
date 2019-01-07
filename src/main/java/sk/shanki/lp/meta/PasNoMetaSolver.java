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
public class PasNoMetaSolver extends BaseGroundMetaSolver {
    
    public PasNoMetaSolver(Solver solver) {   
        super(PasNoMetaInterpreter, solver);
    }
    
    private static final Program PasNoMetaInterpreter;
    private static final String raw = "" +
    
        "pr(R1, R3) :- pr(R1, R2), pr(R2, R3)." +
        ":- pr(R1, R2), pr(R2, R1)." +
        "compl(X,Y) :- compl(Y,X)." +
        ":- compl(X,Y), in_sem(X), in_sem(Y)." +
        "in_sem(X) :- q(R), head(X,R)." +
        "neg_reduct(R1) :- rule(R1), trule(R1, R2), nbl(X,R1), head(X, R2)." +
        "reduct(R) :- rule(R), not neg_reduct(R)." +
        "neg_empty_pos_body(R) :- pbl(X,R)." +
        "neg_pbl_last(X,R) :- pbl(X,R), pbl(Y,R), less(X, Y)." +
        "pbl_last(X,R) :- pbl(X,R), not neg_pbl_last(X,R)." +
        "neg_pbl_first(X,R) :- pbl(X,R), pbl(Y,R), less(Y, X)." +
        "pbl_first(X,R) :- pbl(X,R), not neg_pbl_first(X,R)." +
        "pbl_inbetween(X,Y,R) :- pbl(X,R), pbl(Y,R), pbl(Z,R), less(X, Z), less(Z, Y)." +
        "pos_body_true(SET,R) :- pbl_set(SET), rule(R), not neg_empty_pos_body(R)." +
        "pos_body_true_upto(SET,R,X) :- pbl_set(SET), pbl_first(X,R), in_set(SET,X)." +
        "pos_body_true_upto(SET,R,X) :- pbl_set(SET), pos_body_true_upto(SET,R,Y), pbl(X,R), in_set(SET,X), less(Y, X), not pbl_inbetween(Y,X,R)." +
        "pos_body_true(SET,R) :- pbl_set(SET), pos_body_true_upto(SET,R,X), pbl_last(X,R)." +
        "in_set(head_q, X) :- q(R), head(X,R)." +
        "pbl_set(head_q)." +
        "q(R) :- reduct(R), pos_body_true(head_q, R)." +
        "in_set(trule_head(C),X) :- trule(C,R), head(X,R)." +
        "pbl_set(trule_head(C)) :- rule(C)." +
        "trule(C,R) :- rule(C), q(R), not pr(C,R), pos_body_true(trule_head(C), R)." +
         // toto som pridal, myslim, ze som na to predtym zabudol
         ":- q(R), nbl(X, R), in_sem(X).";
    
    static {
        try {
            PasNoMetaInterpreter = new ProgramFactory().fromString(raw);
        } catch (SemanticException | IOException ex) {
            throw new RuntimeException("Error.", ex);
        }
    }

}