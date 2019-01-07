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
public class PasCnfMetaSolver extends BaseGroundMetaSolver {
    
    public PasCnfMetaSolver(Solver solver) {
        super(PasCnfMetaInterpreter, solver);
    }
    
    private static final Program PasCnfMetaInterpreter;
    private static final String raw = "" + 
        "pr(R1, R3) :- pr(R1, R2), pr(R2, R3)." +
        ":- pr(R1, R2), pr(R2, R1)." +
        "compl(X,Y) :- compl(Y,X)." +
        ":- compl(X,Y), in_sem(X), in_sem(Y)." + 
        "in_sem(X) :- q(R), head(X,R)." + 
        "conflicts(R1,R2) :- nbl(X,R1), cp(R2,X), nbl(Y,R2), cp(R1,Y)." +
        "neg_dangerous(R1,R2) :- conflicts(R1,R2), pr(R2,R1)." +
        "ip(C,R) :- conflicts(C,Q), up(Q,R), q(R)." +
        "neg_reduct(R1) :- rule(R1), q(R2), cp(R2,X), nbl(X,R1), not neg_dangerous(R2, R1), not ip(R1,R2)." +
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
        "lit(X) :- head(X,R)." +
        "lit(X) :- pbl(X,R)." +
        "lit(X) :- nbl(X,R)." +
        "neg_dir_dep(X,Y) :- head(X,R), nbl(Y,R)." +
        "pos_dir_dep(X,Y) :- head(X,R), pbl(Y,R)." +
        "dir_dep(X,Y) :- neg_dir_dep(X,Y)." +
        "dir_dep(X,Y) :- pos_dir_dep(X,Y)." +
        "dep(X,Y) :- dir_dep(X,Y)." +
        "dep(X,Y) :- dep(X,Z), dep(Z,Y)." +
        "dep_eq(X,Y) :- dep(X,Y)." +
        "dep_eq(X,X) :- lit(X)." +
        "neg_dep(X,Y) :- dep_eq(X, XX), neg_dir_dep(XX,YY), dep_eq(YY,Y)." +
        "neg_disallowed(X) :- neg_dep(X,X)." +
        "neg_disallowed(X) :- dep(X,Y), neg_disallowed(Y)." +
        "not_cnf(R) :- nbl(X,R), neg_disallowed(X)." +
        "cnf(R) :- rule(R), not not_cnf(R)." +
        "neg_body_false(R) :- nbl(X,R), in_sem(X)." +
        "neg_body_true(R) :- rule(R), not neg_body_false(R)." +
        "closure_rule(R) :- cnf(R), neg_body_true(R)." +
        "qr(C,R) :- rule(C), closure_rule(R), pos_body_true(qr_lit(C),R)." +
        "in_set(qr_lit(C),X) :- rule(C), qr(C,R), head(X,R)." +
        "in_set(qr_lit(C), X) :- rule(C), head(X,C)." +
        "pbl_set(qr_lit(C)) :- rule(C)." +
        "q0(R) :- closure_rule(R), pos_body_true(q0_lit,R)." +
        "in_set(q0_lit,X) :- q0(R), head(X,R)." +
        "pbl_set(q0_lit)." +
        "up(C,R) :- qr(C,R), not q0(R)." +
        "tp(C,R) :- up(C,R)." +
        "tp(C,C) :- rule(C)." +
        "cp(C,X) :- tp(C,R), head(X,R).";
    
    static {
        try {
            PasCnfMetaInterpreter = new ProgramFactory().fromString(raw);
        } catch (SemanticException | IOException ex) {
            throw new RuntimeException("Error.", ex);
        }
    }

}