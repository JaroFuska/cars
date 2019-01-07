/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.meta;

import java.io.IOException;
import sk.shanki.lp.Program;
import sk.shanki.lp.exceptions.SemanticException;
import sk.shanki.lp.parser.ProgramFactory;
import sk.shanki.lp.solvers.Solver;

/**
 *
 * @author shanki
 */
public class PasGMetaSolver extends BaseGroundMetaSolver {

    public PasGMetaSolver(Solver solver) {
        super(PasGMetaInterpreter, solver);
    }
    
    private static final Program PasGMetaInterpreter;
    private static final String raw = "" +
        "pr(X,Z) :- pr(X,Y), pr(Y,Z)." +
        ":- pr(X,X)." +            
        "rule_set_count(C) :- rule_count(N), pow(2,N,C)." +
        "rule_set(CC) :- rule_set_count(C), more(C, 0), minus(C, 1, CC)." +
        "rule_set(CC) :- rule_set(C), more(C, 0), minus(C, 1, CC)." +
        "neg_rule_first(X) :- rule(X), rule(Y), less(Y, X)." +
        "rule_first(X) :- rule(X), not neg_rule_first(X)." +
        "neg_rule_next(X,X) :- rule(X)." +
        "neg_rule_next(X,Y) :- rule(X), rule(Y), less(Y, X)." +
        "neg_rule_next(X,Y) :- rule(X), rule(Y), rule(Z), less(X, Z), less(Z, Y)." +
        "rule_next(X,Y) :- rule(X), rule(Y), not neg_rule_next(X,Y)." +
        "rule_at(0,X) :- rule_first(X)." +
        "rule_at(I2,X2) :- rule_at(I,X), rule_next(X,X2), plus(I, 1, I2), rule_set_count(N), less(I2, N)." +
        "rule_set_contains(SET,ITEM) :- rule_set(SET), rule_at(INDEX,ITEM), pow(2, INDEX, MASK), bwand(SET, MASK, AND), neq(AND, 0)." +
            
        "neg_empty_pos_body(R)	:- pbl(X,R)." +
        "neg_pbl_last(X,R)	:- pbl(X,R), pbl(Y,R), less(X, Y)." +
        "pbl_last(X,R)		:- pbl(X,R), not neg_pbl_last(X,R)." +
        "neg_pbl_first(X,R)	:- pbl(X,R), pbl(Y,R), less(Y, X)." +
        "pbl_first(X,R)		:- pbl(X,R), not neg_pbl_first(X,R)." +
        "pbl_inbetween(X,Y,R)	:- pbl(X,R), pbl(Y,R), pbl(Z,R), less(X, Z), less(Z, Y)." +
        "pos_body_true(SET,R)		:- pbl_set(SET), rule(R), not neg_empty_pos_body(R)." +
        "pos_body_true_upto(SET,R,X)	:- pbl_set(SET), pbl_first(X,R), in_set(SET,X)." +
        "pos_body_true_upto(SET,R,X)	:- pbl_set(SET), pos_body_true_upto(SET,R,Y), pbl(X,R), in_set(SET,X), less(Y, X), not pbl_inbetween(Y,X,R)." +
        "pos_body_true(SET,R)		:- pbl_set(SET), pos_body_true_upto(SET,R,X), pbl_last(X,R)." +
        
        "pbl_set(head(SET_OUT))		:- q_set(SET_IN, SET_OUT)." +
        "in_set(head(SET_OUT), X)	:- q_set(SET_IN, SET_OUT), in_set(SET_OUT, R), head(X,R)." +
        "in_set(SET_OUT, R)             :- q_set(SET_IN, SET_OUT), in_set(SET_IN, R), pos_body_true(head(SET_OUT), R)." +
        "q_set(rule_set(X), test_arg_set(X)) :- rule_set(X)." +
        "in_set(rule_set(X), R) :- rule_set(X), rule_set_contains(X, R)." +
        "not_arg(X) :- rule_set(X), rule_set_contains(X, R), not in_set(test_arg_set(X), R)." +
        "not_arg(X) :- rule_set(X), in_set(test_arg_set(X), R), not rule_set_contains(X, R)." +
        "arg(X) :- rule_set(X), not not_arg(X)." +
        "blocks_rule(X, R) :- arg(X), rule(R), rule_set_contains(X, RX), head(L, RX), nbl(L, R)." +
        "blocks(X, Y) :- blocks_rule(X, R), rule_set_contains(Y, R)." +
        "ex(R, Y, X) :- rule_set_contains(Y, P), blocks_rule(X, P), pr(R,P)." +
        "not_overrides(X, Y) :- rule_set_contains(X, R), blocks_rule(Y, R), not ex(R, Y, X)." +
        "overrides(X, Y) :- arg(X), arg(Y), not not_overrides(X, Y)." +
        "in_ext(X) :- arg(X), not not_in_ext(X)." +
        "not_in_ext(X) :- in_ext(Y), blocks(Y, X), not overrides(X, Y)." +
        "in_gen_set(R) :- in_ext(A), rule_set_contains(A, R)." +
        "in_sem(X) :- in_gen_set(R), head(X, R).";

    static {
        try {
            PasGMetaInterpreter = new ProgramFactory().fromString(raw);
        } catch (SemanticException | IOException ex) {
            throw new RuntimeException("This should not happen. There is a bug in a program.", ex);
        }
    }

}